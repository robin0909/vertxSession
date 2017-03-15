import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.sstore.impl.SessionImpl;
import io.vertx.redis.RedisClient;
import io.vertx.redis.RedisOptions;

import java.util.concurrent.CountDownLatch;

/**
 * Created by robinyang on 2017/3/15.
 */
public class Test {

    @org.junit.Test
    public void test01() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Vertx vertx = Vertx.vertx();
        RedisOptions redisOptions = new RedisOptions();
        redisOptions.setAddress("123.207.151.74").setPort(6379);


        RedisClient redisClient = RedisClient.create(vertx, redisOptions);

        redisClient.getBinary("7b266564-01f4-409e-85cd-02fdc423c763", res->{
            if (res.succeeded()) {
                Buffer buffer = res.result();
                SessionImpl session = new SessionImpl();
                session.readFromBuffer(0, buffer);

                User user = session.get("web_vertx_user");
                System.out.println(user);
                latch.countDown();
            } else {
                System.out.println("fail");
                latch.countDown();
            }
        });

        latch.await();
    }


    public void test02() throws InterruptedException {

    }

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Vertx vertx = Vertx.vertx();
        RedisOptions redisOptions = new RedisOptions();
        redisOptions.setAddress("123.207.151.74").setPort(6379);


        RedisClient redisClient = RedisClient.create(vertx, redisOptions);

        redisClient.get("name", res->{
            if (res.succeeded()) {
                System.out.println(res.result());
            } else {
                System.out.println("fail");
            }
        });

        latch.await();
    }
}
