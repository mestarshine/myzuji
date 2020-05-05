package com.myzuji.study.gof23.singleton.type1;

/**
 * 饿汉模式（静态变量）
 * 优点：写法简单，在类装载的适合就完成类实例化，避免类线程同步问题
 * 缺点：类装载的时就完成类实例化，没有达到 lazy loading 的效果，如果从为使用过该实例，会造成内存的浪费
 * 使用类装载避免多线程问题，可能造成内存浪费
 */
public class SingletonTest01 {

    public static void main(String[] args) {
        Singleton instance1 = Singleton.getInstance();
        Singleton instance2 = Singleton.getInstance();
        assert instance1.hashCode() == instance2.hashCode();
    }
}

//饿汉模式（静态变量）
class Singleton {

    // 本类内部创建对象实例
    private final static Singleton instance = new Singleton();

    //私有化构造器，不能外部new
    private Singleton() {
    }

    //提供一个公有的静态方法，返回实例对象
    public static Singleton getInstance() {
        return instance;
    }
}
