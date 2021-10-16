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
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MyWordCount {

    public Configuration configuration = null;
    public FileSystem fileSystem = null;

    @BeforeEach
    void conn() throws Exception {
        configuration = new Configuration(true);

        configuration.set("mapreduce.admin.user.env", "hadoop");
        configuration.set("yarn.app.mapreduce.am.admin.user.env", "hadoop");
        // 让框架知道是windows异构平台运行
        configuration.set("mapreduce.app-submission.cross-platform", "true");

        Job job = Job.getInstance(configuration);
//        job.setJar();
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

    @Test
    void task01() {

    }

    @AfterAll
    void close() throws IOException {
        fileSystem.close();
    }
}
