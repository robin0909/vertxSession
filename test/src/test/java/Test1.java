import io.vertx.core.Vertx;
import io.vertx.redis.RedisClient;
import io.vertx.redis.RedisOptions;
import org.junit.Test;
import java.util.concurrent.*;

/**
 * Created by robinyang on 2017/3/15.
 */
public class Test1 {

    @Test
    public void test01() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Vertx vertx = Vertx.vertx();
        RedisOptions redisOptions = new RedisOptions();
        redisOptions.setHost("123.207.151.74").setPort(6379);


        RedisClient redisClient = RedisClient.create(vertx, redisOptions);

        redisClient.get("name", res->{
            if (res.succeeded()) {
                System.out.println(res.result());
                latch.countDown();
            } else {
                System.out.println("fail");
                latch.countDown();
            }

        });

        latch.await();
    }

    @Test
    public void test02() {
        CountDownLatch latch = new CountDownLatch(1);

        Vertx vertx = Vertx.vertx();
        RedisOptions redisOptions = new RedisOptions();
        redisOptions.setHost("123.207.151.74").setPort(6379);


        RedisClient redisClient = RedisClient.create(vertx, redisOptions);

    }
}
