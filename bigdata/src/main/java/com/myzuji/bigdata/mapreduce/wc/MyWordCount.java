package com.myzuji.bigdata.mapreduce.wc;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

@Slf4j
public class MyWordCount {

    public static Configuration configuration = null;
    public static FileSystem fileSystem = null;

    public static void main(String[] args) throws Exception {

        configuration = new Configuration(true);
        // 让框架知道是windows异构平台运行
        configuration.set("mapreduce.app-submission.cross-platform", "true");

        Job job = Job.getInstance(configuration);
        job.setJar("/Users/starshine/workspace/myzuji/bigdata/build/libs/bigdata-1.0.0-SNAPSHOT.jar");
        job.setJarByClass(MyWordCount.class);
        job.setJobName("Word Count Job Test");

        Path inFile = new Path("/data/wc/input");
        TextInputFormat.addInputPath(job, inFile);

        Path outFile = new Path("/data/wc/output");
        if (outFile.getFileSystem(configuration).exists(outFile)) {
            outFile.getFileSystem(configuration).delete(outFile, true);
        }
        TextOutputFormat.setOutputPath(job, outFile);

        job.setMapperClass(MyMapper.class);
        job.setReducerClass(MyReducer.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        job.waitForCompletion(true);
    }

}
