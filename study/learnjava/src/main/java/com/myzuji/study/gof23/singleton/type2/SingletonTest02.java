package com.myzuji.study.gof23.singleton.type2;

/**
 * 饿汉模式（静态代码块）
 * 优缺点同方式一，静态变量写法
 * 使用类装载避免多线程问题，可能造成内存浪费
 */
public class SingletonTest02 {

    public static void main(String[] args) {
        Singleton instance1 = Singleton.getInstance();
        Singleton instance2 = Singleton.getInstance();
        assert instance1.hashCode() == instance2.hashCode();
    }
}

//饿汉模式（静态代码块）
class Singleton {

    // 本类内部创建对象实例
    private static final Singleton instance;

    //在静态代码块中，创建单例对象
    static {
        instance = new Singleton();
    }

    //私有化构造器，不能外部new
    private Singleton() {
    }

    //提供一个公有的静态方法，返回实例对象
    public static Singleton getInstance() {
        return instance;
    }
}
