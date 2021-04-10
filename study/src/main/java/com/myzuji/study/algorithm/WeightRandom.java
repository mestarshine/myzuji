package com.myzuji.study.algorithm;

import javafx.util.Pair;

import java.util.ArrayList;
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

    private TreeMap<Double, K> weightMap = new TreeMap<Double, K>();

    public WeightRandom(List<Pair<K, V>> list) {
        for (Pair<K, V> pair : list) {
            //统一转为double
            double lastWeight = this.weightMap.size() == 0 ? 0 : this.weightMap.lastKey().doubleValue();
            //权重累加
            this.weightMap.put(pair.getValue().doubleValue() + lastWeight, pair.getKey());
        }
    }

    public static void main(String[] args) {
        List<Pair> pairs = new ArrayList<>();
        Pair pair = new Pair(0, 1);
        Pair pair1 = new Pair(1, 3);
        Pair pair2 = new Pair(3, 5);
        Pair pair3 = new Pair(5, 10);
        pairs.add(pair);
        pairs.add(pair1);
        pairs.add(pair2);
        pairs.add(pair3);
        WeightRandom weightRandom = new WeightRandom(pairs);
        int a = 0;
        int b = 0;
        int c = 0;
        int d = 0;
        for (int i = 0; i < 100000000; i++) {
            Integer number = (Integer) weightRandom.random();
            switch (number) {
                case 0:
                    a++;
                    break;
                case 1:
                    b++;
                    break;
                case 2:
                    b++;
                    break;
                case 3:
                    b++;
                    break;
                case 4:
                    c++;
                    break;
                case 5:
                    d++;
                    break;
                case 6:
                    d++;
                    break;
                case 7:
                    d++;
                    break;
                case 8:
                    d++;
                    break;
                case 9:
                    d++;
                    break;
            }
        }
        System.out.println("a：" + a);
        System.out.println("b：" + b);
        System.out.println("c：" + c);
        System.out.println("d：" + d);
    }

    public K random() {
        double randomWeight = this.weightMap.lastKey() * Math.random();
        SortedMap<Double, K> tailMap = this.weightMap.tailMap(randomWeight, false);
        return this.weightMap.get(tailMap.firstKey());
    }
}

