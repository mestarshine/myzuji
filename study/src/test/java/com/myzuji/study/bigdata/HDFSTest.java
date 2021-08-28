package com.myzuji.study.bigdata;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.net.URI;

@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class HDFSTest {

    public Configuration configuration = null;
    public FileSystem fileSystem = null;

    @BeforeEach
    void conn() throws Exception {
        configuration = new Configuration(true);
        fileSystem = FileSystem.get(URI.create("hdfs://node01:9000"), configuration, "root");

    }

    @Test
    void mkdir() throws IOException {
        Path path = new Path("/bigdata/abc");
        if (fileSystem.exists(path)) {
            fileSystem.delete(path, true);
        }

        fileSystem.create(path);

    }

    @AfterAll
    void close() throws IOException {
        fileSystem.close();
    }
}
