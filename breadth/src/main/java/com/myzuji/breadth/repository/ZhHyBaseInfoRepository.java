package com.myzuji.breadth.repository;

import com.myzuji.breadth.domain.ZhHyBaseInfo;
import org.springframework.data.repository.CrudRepository;

public interface ZhHyBaseInfoRepository extends CrudRepository<ZhHyBaseInfo, Integer> {

    @Override
    <S extends ZhHyBaseInfo> S save(S s);

    /**
     * 根据code获取行业信息
     *
     * @param csrcCode
     * @return
     */
    ZhHyBaseInfo findByCsrcCode(String csrcCode);

    @Override
    Iterable<ZhHyBaseInfo> findAll();
}
