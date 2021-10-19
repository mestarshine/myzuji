package com.myzuji.bigdata.mapreduce.topn;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.util.StringUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TMapper extends Mapper<LongWritable, Text, Tkey, IntWritable> {

    // 定义在方法外减少GC
    Tkey tkey = new Tkey();
    IntWritable intWritable = new IntWritable();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] split = StringUtils.split(value.toString(), '\t');
        LocalDateTime localDateTime = LocalDateTime.parse(split[0], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        tkey.setYear(localDateTime.getYear());
        tkey.setMonth(localDateTime.getMonth().getValue());
        tkey.setDay(localDateTime.getDayOfMonth());
        tkey.setWd(Integer.parseInt(split[2]));
        intWritable.set(Integer.parseInt(split[2]));
        context.write(tkey, intWritable);
    }
}
