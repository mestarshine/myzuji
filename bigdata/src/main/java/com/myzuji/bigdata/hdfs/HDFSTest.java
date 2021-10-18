package com.myzuji.bigdata.hdfs;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.net.URI;

@Slf4j
public class HDFSTest {

    public static Configuration configuration = null;
    public static FileSystem fileSystem = null;

    public static void main(String[] args) throws Exception {
        configuration = new Configuration(true);
        fileSystem = FileSystem.get(URI.create("hdfs://mycluster"), configuration, "hadoop");
        // 创建目录
        mkdir();
        // 上传文件
//        put();

        fileSystem.close();
    }

    private static void mkdir() throws IOException {
        Path path = new Path("/data/wc/");
        if (fileSystem.exists(path)) {
            fileSystem.delete(path, true);
        }

        fileSystem.create(path);

    }

    private static void put() throws IOException {
        Path inputPath = new Path("/tmp/test.txt");
        Path outPath = new Path("/data/wc/input/test.txt");
        if (fileSystem.exists(outPath)) {
            fileSystem.delete(outPath, true);
        }
        fileSystem.completeLocalOutput(outPath, inputPath);
    }
}
