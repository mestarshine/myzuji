package com.myzuji.study.redis.config;

//@Component
public class RedisConfig {

    //以下为redisson锁，哨兵
//    @Bean(name = "redisson")
//    @Order(1)
//    public Redisson getRedisson(){
//
//        Config config = new Config();
//        config.useSentinelServers()
//                .setMasterName(properties.getMasterName())
//                .addSentinelAddress(properties.getAddress())
//                .setDatabase(0);
//        return (Redisson) Redisson.create(config);
//    }
    //以上为redisson 哨兵锁

    //以下为红锁
//    @Bean(name = "redissonRed1")
//    @Primary
//    public RedissonClient redissonRed1(){
//        Config config = new Config();
//        config.useSingleServer().setAddress("127.0.0.1:6379").setDatabase(0);
//        return Redisson.create(config);
//    }
//    @Bean(name = "redissonRed2")
//    public RedissonClient redissonRed2(){
//        Config config = new Config();
//        config.useSingleServer().setAddress("127.0.0.1:6380").setDatabase(0);
//        return Redisson.create(config);
//    }
//    @Bean(name = "redissonRed3")
//    public RedissonClient redissonRed3(){
//        Config config = new Config();
//        config.useSingleServer().setAddress("127.0.0.1:6381").setDatabase(0);
//        return Redisson.create(config);
//    }
    //以上为红锁


    // 单个redis
//    @Bean
//    @ConditionalOnMissingBean(StringRedisTemplate.class)
//    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
//    	StringRedisTemplate redisTemplate = new StringRedisTemplate();
//    	redisTemplate.setConnectionFactory(redisConnectionFactory);
//    	return redisTemplate;
//
//    }

    /**
     * 单个redisson
     * @return
     */
//    @Bean
//    public RedissonClient redissonClient() {
//    	Config config = new Config();
//    	config.useSingleServer().setAddress("127.0.0.1:6379").setDatabase(0);
//
//    	return Redisson.create(config);
//    }

//    @Bean
//    public Redisson redisson(){
//        Config config = new Config();
//        config.useSingleServer().setAddress("redis://localhost:6379").setDatabase(0);
//        return (Redisson) Redisson.create(config);
//    }
}
