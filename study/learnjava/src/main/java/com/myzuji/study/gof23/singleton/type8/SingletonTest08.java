package com.myzuji.study.gof23.singleton.type8;

//单例 - 枚举
enum Singleton {

    INSTANCE;

    public void sayOK() {
        System.out.println("ok");
    }
}

/**
 * 单例 - 枚举
 * 优点：借助 JDK1.5 中添加的枚举来实现单例，不仅能避免多线程同步问题，而且还能防止反序列化重新创建新的对象。
 * Effective Java 作者 Josh Bloch 提倡的方式
 * 推荐使用
 */
public class SingletonTest08 {

    public static void main(String[] args) {
        Singleton instance1 = Singleton.INSTANCE;
        Singleton instance2 = Singleton.INSTANCE;
        assert instance1.hashCode() == instance2.hashCode();
        instance1.sayOK();
    }
}
