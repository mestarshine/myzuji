package com.myzuji.study.algorithm;

import org.apache.commons.math3.util.Pair;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/05
 */

public class WeightRandom<K, V extends Number> {

    private final TreeMap<Double, K> weightMap = new TreeMap<Double, K>();

    public WeightRandom(List<Pair<K, V>> list) {
        for (Pair<K, V> pair : list) {
            //统一转为double
            double lastWeight = this.weightMap.size() == 0 ? 0 : this.weightMap.lastKey().doubleValue();
            //权重累加
            this.weightMap.put(pair.getValue().doubleValue() + lastWeight, pair.getKey());
        }
    }

    public K random() {
        double randomWeight = this.weightMap.lastKey() * Math.random();
        SortedMap<Double, K> tailMap = this.weightMap.tailMap(randomWeight, false);
        return this.weightMap.get(tailMap.firstKey());
    }
}

