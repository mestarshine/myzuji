package com.myzuji.backend.rpt.base;

import com.google.common.base.Preconditions;
import com.myzuji.backend.domain.base.BaseEntity;
import com.myzuji.backend.domain.base.DBLockMode;
import com.myzuji.util.ClassUtil;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/22
 */
public class BaseRptImpl<T extends BaseEntity> implements BaseRpt<T> {

    private Class domainClass;

    @Autowired
    private SessionFactory sessionFactory;

    public static LockMode convert(DBLockMode lockMode) {
        switch (lockMode) {
            case NONE:
                return LockMode.NONE;
            case WRITE:
                return LockMode.PESSIMISTIC_WRITE;
            case WRITE_NOWAIT:
                return LockMode.UPGRADE_NOWAIT;
            case READ_ONLY:
                return LockMode.READ;
            default:
                throw new RuntimeException("预期之外的数据库锁类型[" + lockMode + "]");
        }
    }

    public Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public CriteriaBuilder criteriaBuilder() {
        return getSession().getCriteriaBuilder();
    }

    @Override
    public T getById(Long id) {
        return getById(id, DBLockMode.NONE);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public T getById(Long id, DBLockMode dbLockMode) {
        if (id == null) {
            return null;
        }

        LockMode lockMode = convert(dbLockMode);
        return (T) getSession().get(getDomainClass(), id, lockMode);
    }

    @Override
    public T getById(Class<T> cla, Long id) {
        return getSession().get(cla, id);
    }

    @Override
    public List<T> getList(final Long[] ids) {
        return getList(ids, DBLockMode.NONE);
    }

    @Override
    public void saveOrUpdate(Object domain) {
        Preconditions.checkNotNull(domain);
        getSession().saveOrUpdate(domain);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public List<T> getList(final Long[] ids, final DBLockMode lockMode) {
        Query query = getSession().createQuery("from " + getDomainClassName() + " where id in (:ids)");
        query.setParameterList("ids", ids);
        query.setLockMode("this", convert(lockMode));
        return query.getResultList();
    }

    @Override
    public void saveOrUpdateList(List<T> list) {
        if (list == null || list.isEmpty()) {
            return;
        }

        for (T domain : list) {
            saveOrUpdate(domain);
        }
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public void deleteById(Long id) {
        CriteriaBuilder criteriaBuilder = criteriaBuilder();
        CriteriaDelete<T> criteriaDelete = criteriaBuilder.createCriteriaDelete(getDomainClass());
        Root<T> root = criteriaDelete.from(getDomainClass());
        Predicate predicate = criteriaBuilder.equal(root.get("id"), id);
        getSession().createQuery(criteriaDelete.where(predicate)).executeUpdate();
    }

    @Override
    public void delete(T domain) {
        getSession().delete(domain);
    }

    protected Class getDomainClass() {
        if (domainClass == null) {
            domainClass = ClassUtil.getGenericType(getClass(), 0);
        }
        return domainClass;
    }

    protected String getDomainClassName() {
        return getDomainClass().getName();
    }

}
