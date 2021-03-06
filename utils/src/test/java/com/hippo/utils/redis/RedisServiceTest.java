package com.hippo.utils.redis;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(locations = "classpath:/application-*.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class RedisServiceTest {


  @Test
  public void lockTest() throws InterruptedException {

    RedisService redisService = new RedisService();
    redisService.del("lockTest");
    new Thread(new RedisRunnable(redisService)).start();
    new Thread(new RedisRunnable(redisService)).start();
    new Thread(new RedisRunnable(redisService)).start();
    Thread.sleep(40 * 1000L);
  }
}


class RedisRunnable implements Runnable {

  public RedisService redisService;

  public RedisRunnable(RedisService redisService) {
    this.redisService = redisService;
  }

  @Override
  public void run() {
    try {
      if (redisService.lock("lockTest", 10 * 1000L)) {
        System.out.println(Thread.currentThread().getId());
        TimeUnit.SECONDS.sleep(8);
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      redisService.unlock();
    }
  }
}
