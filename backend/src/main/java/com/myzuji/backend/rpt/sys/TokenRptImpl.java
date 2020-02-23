package com.myzuji.backend.rpt.sys;

import com.myzuji.backend.domain.system.SysToken;
import com.myzuji.backend.dto.Token;
import com.myzuji.backend.rpt.base.BaseRptImpl;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.*;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/22
 */
@Repository
public class TokenRptImpl extends BaseRptImpl<Token> implements TokenRpt {

    @Override
    public void save(SysToken token) {
        saveOrUpdate(token);
    }

    @Override
    public SysToken getById(String token) {
        CriteriaBuilder criteriaBuilder = criteriaBuilder();
        CriteriaQuery<SysToken> criteriaQuery = criteriaBuilder.createQuery(SysToken.class);
        Root<SysToken> root = criteriaQuery.from(SysToken.class);
        Predicate predicate = criteriaBuilder.equal(root.get("token"), token);
        return getSession().createQuery(criteriaQuery.where(predicate)).uniqueResult();
    }

    @Override
    public void update(SysToken token) {
        getSession().update(token);
    }

    @Override
    public void delete(String uuid) {
        CriteriaBuilder criteriaBuilder = criteriaBuilder();
        CriteriaDelete<SysToken> criteriaDelete = criteriaBuilder.createCriteriaDelete(SysToken.class);
        Root<SysToken> root = criteriaDelete.from(SysToken.class);
        Predicate predicate = criteriaBuilder.equal(root.get("token"), uuid);
        getSession().createQuery(criteriaDelete.where(predicate)).executeUpdate();
    }
}
