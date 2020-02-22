package com.myzuji.backend.rpt.base;

import com.myzuji.backend.domain.base.BaseEntity;
import com.myzuji.backend.domain.base.DBLockMode;

import java.util.List;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/22
 */
public interface BaseRpt<T extends BaseEntity> {

    T getById(Long id);

    T getById(Long id, DBLockMode lockMode);

    T getById(Class<T> cla, Long id);

    List<T> getList(Long[] ids);

    List<T> getList(Long[] ids, DBLockMode lockMode);

    void saveOrUpdate(Object domain);

    void saveOrUpdateList(List<T> domain);

    void deleteById(Long id);

    void delete(T domain);
}
