package com.xiaofengstu.demopubsub;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

@SpringBootTest
class DemoPubSubApplicationTests {

  @Resource
  RedisTemplate redisTemplate;


  @Test
  void contextLoads() {
    redisTemplate.convertAndSend("test", "aaa");
  }

}
