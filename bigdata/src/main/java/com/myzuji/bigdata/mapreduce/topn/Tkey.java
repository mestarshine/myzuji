package com.myzuji.bigdata.mapreduce.topn;

import lombok.Data;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

@Data
public class Tkey implements WritableComparable<Tkey> {

    private int year;
    private int month;
    private int day;
    private int wd;
    private String location;

    @Override
    public int compareTo(Tkey tkey) {
        int c1 = Integer.compare(this.year, tkey.getYear());
        if (c1 == 0) {
            int c2 = Integer.compare(this.month, tkey.getMonth());
            if (c2 == 0) {
                return Integer.compare(this.day, tkey.getDay());
            }
            return c2;
        }
        return c1;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.write(year);
        out.write(month);
        out.write(day);
        out.write(wd);
        out.writeUTF(location);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.year = in.readInt();
        this.month = in.readInt();
        this.day = in.readInt();
        this.wd = in.readInt();
        this.location = in.readUTF();
    }
}
