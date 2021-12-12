package com.myzuji.study.gof23.singleton.type7;

/**
 * 单例-静态内部类
 * 采用类装载机制类保证初始化实例只有一个线程
 * 类的静态属性只会在第一次加载类当时候初始化，JVM 保证类线程的安全
 * 优点：线程安全、利用静态内部类特点实现 lazy loading ，效率高
 * 在实际开发中，推荐使用
 */
public class SingletonTest07 {

    public static void main(String[] args) {
        Singleton instance1 = Singleton.getInstance();
        Singleton instance2 = Singleton.getInstance();
        assert instance1.hashCode() == instance2.hashCode();
    }
}

// 单例-静态内部类
class Singleton {

    // 本类内部创建对象实例
    private static volatile Singleton instance;

    // 私有化构造器，不能外部new
    private Singleton() {
    }

    // 提供一个静态公有方法看，直接返回 SingletonInstance.INSTANCE
    public static Singleton getInstance() {
        return SingletonInstance.INSTANCE;
    }

    // 外部类 Singleton 被装载都时候，静态内部类不会本装载，
    // 调用 getInstance() 时，才会被装载，从完成 Singleton 实例化
    private static class SingletonInstance {
        private static final Singleton INSTANCE = new Singleton();
    }
}
