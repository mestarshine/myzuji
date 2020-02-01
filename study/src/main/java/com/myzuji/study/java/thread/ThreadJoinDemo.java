package com.myzuji.study.java.thread;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/01
 */
public class ThreadJoinDemo {

    public static void main(String[] args) {
        ThreadDemo one = new ThreadDemo("one");
        ThreadDemo two = new ThreadDemo("two");
        ThreadDemo three = new ThreadDemo("three");
        System.out.println("线程" + one.getName() + "is alive:" + one.thread.isAlive() + "priority" + one.thread.getPriority());
        System.out.println("线程" + two.getName() + "is alive:" + two.thread.isAlive() + "priority" + two.thread.getPriority());
        System.out.println("线程" + three.getName() + "is alive:" + three.thread.isAlive() + "priority" + three.thread.getPriority());
        System.out.println("等待线程完成");
        try {
            System.out.println("线程" + one.getName() + "状态" + one.getThread().getState());
            one.thread.join();
            System.out.println("线程" + one.getName() + "状态" + one.getThread().getState());
            two.thread.join();
            three.thread.join();
        } catch (InterruptedException e) {
            System.out.println("主线程中断");
        }
        System.out.println("线程" + one.getName() + "is alive:" + one.thread.isAlive());
        System.out.println("线程" + two.getName() + "is alive:" + two.thread.isAlive());
        System.out.println("线程" + three.getName() + "is alive:" + three.thread.isAlive());
        System.out.println("主线程退出");
    }
}
