package com.myzuji.backend.rpt.sys;

import com.myzuji.backend.domain.system.SysUser;
import com.myzuji.backend.rpt.base.BaseRptImpl;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/22
 */
@Repository
public class SysUserRptImpl extends BaseRptImpl<SysUser> implements SysUserRpt {

    @Override
    public SysUser save(SysUser sysUser) {
        saveOrUpdate(sysUser);
        return sysUser;
    }

    @Override
    public SysUser obtainUserByLoginName(String loginName) {
        CriteriaBuilder criteriaBuilder = criteriaBuilder();
        CriteriaQuery<SysUser> criteriaQuery = criteriaBuilder.createQuery(SysUser.class);
        Root<SysUser> root = criteriaQuery.from(SysUser.class);
        Predicate predicate = criteriaBuilder.equal(root.get("loginName"), loginName);
        return getSession().createQuery(criteriaQuery.where(predicate)).uniqueResult();
    }
}
