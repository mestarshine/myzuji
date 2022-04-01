package com.myzuji.breadth.repository;

import com.myzuji.breadth.domain.ZhStocksInfo;
import org.springframework.data.repository.CrudRepository;

/**
 * 说明
 *
 * @author shine
 * @date 2021/12/25
 */
public interface ZhStocksInfoRepository extends CrudRepository<ZhStocksInfo, Integer> {

    @Override
    <S extends ZhStocksInfo> S save(S s);

    ZhStocksInfo findByStockCode(String stockCode);
}
