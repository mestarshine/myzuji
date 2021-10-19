package com.myzuji.bigdata.mapreduce.topn;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class MyTopN {

    public static void main(String[] args) throws Exception {
        Configuration configuration = new Configuration(true);
        String[] other = new GenericOptionsParser(configuration, args).getRemainingArgs();

        Job job = Job.getInstance(configuration);
        job.setJarByClass(MyTopN.class);

        job.setJobName("topN");

        // client 端端代理梳理
        // 1. mapTask
        // 2. input
        TextInputFormat.addInputPath(job, new Path(other[0]));
        Path outPath = new Path(other[1]);
        if (outPath.getFileSystem(configuration).exists(outPath)) {
            outPath.getFileSystem(configuration).delete(outPath, true);
        }
        TextOutputFormat.setOutputPath(job, outPath);
        // 3. map
        job.setMapperClass(TMapper.class);
        job.setMapOutputKeyClass(Tkey.class);
        job.setMapOutputValueClass(IntWritable.class);
        // 4. partitioner 按年，月 分区 --> 分区>分组
        job.setPartitionerClass(TPartitioner.class);
        // 5. sortComparator 年,月,温度 且温度倒序
        job.setSortComparatorClass(TSortComparator.class);
        // 6. combine


        // reduceTask
        // 7. groupingComparator
        job.setGroupingComparatorClass(TGroupingComparator.class);
        // 8. reduce
        job.setReducerClass(TReducer.class);

        job.waitForCompletion(true);
    }
}

