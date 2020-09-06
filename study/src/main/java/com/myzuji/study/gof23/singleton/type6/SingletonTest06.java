package com.myzuji.study.gof23.singleton.type6;

/**
 * 双重检查
 * 优点：线程安全、lazy loading、效率高
 * 在实际开发中，推荐使用这种方式
 */
public class SingletonTest06 {

    public static void main(String[] args) {
        Singleton instance1 = Singleton.getInstance();
        Singleton instance2 = Singleton.getInstance();
        assert instance1.hashCode() == instance2.hashCode();
    }
}

/**
 * 双重检查
 * 注意：需要添加 volatile ，防止指令重排序
 */
class Singleton {

    //本类内部创建对象实例
    private static volatile Singleton instance;

    //私有化构造器，不能外部new
    private Singleton() {
    }

    //提供一个静态公有方法，加入双重检查代码，解决线程安全问题，同时解决 lazy loading 问题
    // 同时保证了效率
    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
