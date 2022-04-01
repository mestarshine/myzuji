package com.myzuji.bigdata.mapreduce.topn;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class TSortComparator extends WritableComparator {

    public TSortComparator() {
        super(Tkey.class, true);
    }

    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        Tkey k1 = (Tkey) a;
        Tkey k2 = (Tkey) b;
        // 按年，月，温度，且温度倒序
        int c1 = Integer.compare(k1.getYear(), k2.getYear());
        if (c1 == 0) {
            int c2 = Integer.compare(k1.getMonth(), k2.getMonth());
            if (c2 == 0) {
                return -Integer.compare(k1.getWd(), k2.getWd());
            }
            return c2;
        }
        return c1;
    }
}
