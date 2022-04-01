package com.myzuji.bigdata.mapreduce.topn;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

/**
 * 说明
 *
 * @author shine
 * @date 2021/10/18
 */
public class TGroupingComparator extends WritableComparator {

    public TGroupingComparator() {
        super(Tkey.class, true);
    }

    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        Tkey k1 = (Tkey) a;
        Tkey k2 = (Tkey) b;
        // 按年，月，温度，且温度倒序
        int c1 = Integer.compare(k1.getYear(), k2.getYear());
        if (c1 == 0) {
            return Integer.compare(k1.getMonth(), k2.getMonth());
        }
        return c1;
    }
}
