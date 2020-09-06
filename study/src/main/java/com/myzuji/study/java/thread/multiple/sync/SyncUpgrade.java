package com.myzuji.study.java.thread.multiple.sync;

/**
 * JDK1.5 之前是重量级的，需要找操作系统来完成
 * 1.5 之后引入了锁升级
 * sync (Object)
 * mark word 记录这个线程ID，（偏向锁
 * 如果有线程争用，升级为 自旋锁
 * 自旋 10 次以后，还没获取到，则升级为重量级锁  = OS
 * <p>
 * 执行时间短（加锁代码），线程数比较少使用自旋锁
 * <p>
 * 自旋锁 占用CPU资源，不访问操作系统
 * 锁升级后不能别降级
 */
public class SyncUpgrade {
}
