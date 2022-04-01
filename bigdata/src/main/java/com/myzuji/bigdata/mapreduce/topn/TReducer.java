package com.myzuji.bigdata.mapreduce.topn;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Iterator;

public class TReducer extends Reducer<Tkey, IntWritable, Text, IntWritable> {

    Text rkey = new Text();
    IntWritable rval = new IntWritable();

    @Override
    protected void reduce(Tkey key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        Iterator<IntWritable> iterator = values.iterator();

        int flag = 0, day = 0;
        while (iterator.hasNext()) {
            IntWritable val = iterator.next();
            if (flag == 0) {
                rkey.set(key.getYear() + "-" + key.getMonth() + "-" + key.getDay() + "@" + key.getLocation());
                rval.set(key.getWd());
                context.write(rkey, rval);
                flag++;
                day = key.getDay();
            }
            if (flag != 0 && day != key.getDay()) {
                rkey.set(key.getYear() + "-" + key.getMonth() + "-" + key.getDay() + "@" + key.getLocation());
                rval.set(key.getWd());
                context.write(rkey, rval);
                break;
            }
        }
    }
}
