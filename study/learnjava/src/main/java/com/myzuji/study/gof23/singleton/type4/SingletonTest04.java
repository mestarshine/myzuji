package com.myzuji.study.gof23.singleton.type4;

/**
 * 懒模式（线程安全,同步方法）
 * 优点：解决了线程不安全问题
 * 缺点：效率太低了，每个线程在向获得类的实例时都要进行同步，而这个方法只执行一次实例化代码就可以了，方法进行同步效率太低
 * 在实际开放中，不推荐使用这种方式
 */
public class SingletonTest04 {

    public static void main(String[] args) {
        Singleton instance1 = Singleton.getInstance();
        Singleton instance2 = Singleton.getInstance();
        assert instance1.hashCode() == instance2.hashCode();
    }
}

//懒模式（线程安全,同步方法）
class Singleton {

    // 本类内部创建对象实例
    private static Singleton instance;

    //私有化构造器，不能外部new
    private Singleton() {
    }

    //提供一个静态公有方法，加入同步处理的代码，解决线程安全问题
    public static synchronized Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
}
