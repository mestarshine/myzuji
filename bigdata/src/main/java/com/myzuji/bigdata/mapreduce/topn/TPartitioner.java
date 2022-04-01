package com.myzuji.bigdata.mapreduce.topn;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Partitioner;

public class TPartitioner extends Partitioner<Tkey, IntWritable> {

    @Override
    public int getPartition(Tkey tkey, IntWritable intWritable, int numPartitions) {

        return tkey.getYear() % numPartitions;
    }
}
