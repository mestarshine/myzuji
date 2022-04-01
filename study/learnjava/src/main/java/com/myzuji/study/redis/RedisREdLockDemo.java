package com.myzuji.study.redis;


import java.util.concurrent.TimeUnit;

public class RedisREdLockDemo {

//    @Autowired
//    @Qualifier("redissonRed1")
//    private RedissonClient redissonRed1;
//    @Autowired
//    @Qualifier("redissonRed2")
//    private RedissonClient redissonRed2;
//    @Autowired
//    @Qualifier("redissonRed3")
//    private RedissonClient redissonRed3;

    public String redisRedLock() {
        //生成key
//        String lockKey = (RedisKeyConstant.GRAB_LOCK_ORDER_KEY_PRE + orderId).intern();
        //redisson锁 哨兵
//        RLock rLock = redisson.getLock(lockKey);
//        rLock.lock();

        //redisson锁 单节点
//        RLock rLock = redissonRed1.getLock(lockKey);

        //红锁 redis son
//        RLock rLock1 = redissonRed1.getLock(lockKey);
//        RLock rLock2 = redissonRed2.getLock(lockKey);
//        RLock rLock3 = redissonRed3.getLock(lockKey);
//        RedissonRedLock rLock = new RedissonRedLock(rLock1,rLock2,rLock3);


        try {
            //加锁
//            rLock.lock();
            try {
                TimeUnit.MINUTES.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 业务处理


        } finally {
            //释放锁
//            rLock.unlock();
        }
        return null;
    }
}
