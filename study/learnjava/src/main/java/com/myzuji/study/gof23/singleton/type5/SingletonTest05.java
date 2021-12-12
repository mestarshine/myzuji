package com.myzuji.study.gof23.singleton.type5;

/**
 * 懒模式（线程安全,同步代码块）
 * 这种同步并不能起到线程安全都作用，跟第三种实现方式遇到都问题相同
 * 在实际开发中，不能使用这种方式
 */
public class SingletonTest05 {

    public static void main(String[] args) {
        Singleton instance1 = Singleton.getInstance();
        Singleton instance2 = Singleton.getInstance();
        assert instance1.hashCode() == instance2.hashCode();
    }
}

//懒模式（线程安全,同步代码块）
class Singleton {

    // 本类内部创建对象实例
    private static Singleton instance;

    //私有化构造器，不能外部new
    private Singleton() {
    }

    //提供一个静态公有方法，加入同步处理的代码，解决线程安全问题
    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                instance = new Singleton();
            }
        }
        return instance;
    }
}
