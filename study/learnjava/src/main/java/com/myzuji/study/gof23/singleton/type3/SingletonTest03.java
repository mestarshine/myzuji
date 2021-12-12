package com.myzuji.study.gof23.singleton.type3;

/**
 * 懒模式（线程不安全）
 * 优点：lazy loading ，但是只能在单线程下使用。
 * 缺点：多线程下不能使用
 * 在实际开放中，不要使用这种方式
 */
public class SingletonTest03 {

    public static void main(String[] args) {
        System.out.println("线程不安全的·····");
        Singleton instance1 = Singleton.getInstance();
        Singleton instance2 = Singleton.getInstance();
        assert instance1.hashCode() == instance2.hashCode();
    }
}

//懒模式（线程不安全）
class Singleton {

    // 本类内部创建对象实例
    private static Singleton instance;

    //私有化构造器，不能外部new
    private Singleton() {
    }

    //提供一个静态公有方法，当使用到该方法时，才创建 instance 即 懒汉模式
    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
}
