package com.myzuji.bigdata.mapreduce.topn;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.util.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class TMapper extends Mapper<LongWritable, Text, Tkey, IntWritable> {

    public HashMap<String, String> dict = new HashMap<>();
    // 定义在方法外减少GC
    Tkey mkey = new Tkey();
    IntWritable mval = new IntWritable();

    @Override
    protected void setup(Mapper<LongWritable, Text, Tkey, IntWritable>.Context context) throws IOException, InterruptedException {
        URI[] cacheFiles = context.getCacheFiles();
        Path path = new Path(cacheFiles[0].getPath());
        BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(path.getName())));
        String line = bufferedReader.readLine();
        while (line != null) {
            String[] split = line.split("\t");
            dict.put(split[0], split[1]);
            line = bufferedReader.readLine();
        }
        super.setup(context);
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] split = StringUtils.split(value.toString(), '\t');
        LocalDateTime localDateTime = LocalDateTime.parse(split[0], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        mkey.setYear(localDateTime.getYear());
        mkey.setMonth(localDateTime.getMonth().getValue());
        mkey.setDay(localDateTime.getDayOfMonth());
        mkey.setWd(Integer.parseInt(split[2]));
        mkey.setLocation(dict.get(split[1]));
        mval.set(Integer.parseInt(split[2]));
        context.write(mkey, mval);
    }
}
