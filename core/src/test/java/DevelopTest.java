import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.PRNG;
import io.vertx.ext.web.Session;
import io.vertx.ext.web.sstore.impl.SessionImpl;
import io.vertx.ext.web.utils.SerializeUtil;
import io.vertx.redis.RedisClient;
import io.vertx.redis.RedisOptions;
import io.vertx.redis.op.SetOptions;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.*;

/**
 * Created by robin on 2017/3/14.
 */
public class DevelopTest {

    @Test
    public void test01() {
        Map<String, Object> map = new HashMap<>();

        map.put("key", new User("robin", 47));

        JsonObject entries = new JsonObject();
        entries.put("session", map);

        System.out.println(entries.toString());

//        Map<String, Object> mqp1 = (Map<String, Object>) entries.getValue("session");
        Map<String, Object> map1 = entries.getMap();

        Map<String, Object> map2 = (Map<String, Object>) map1.get("session");

//        System.out.println("=================================");
//        System.out.println(user.getKey());
//        System.out.println(user.getName());

    }

    @Test
    public void test2() {
        HashMap<String, Object> map = new HashMap<>();

        map.put("key", new User("sss", 47));

        byte[] serialize = SerializeUtil.serialize(map);

        HashMap<String, Object> unserizlize = (HashMap<String, Object>) SerializeUtil.unserizlize(serialize);

        System.out.println("================");
        User u = (User) unserizlize.get("key");
        System.out.println(u.getKey());
        System.out.println(u.getName());

    }

    @Test
    public void test3() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("key", new User("sss", 47));

        byte[] serialize = SerializeUtil.serialize(map);

        Buffer buffer = Buffer.buffer().appendLong(100L).appendBytes(serialize);

        long aLong = buffer.getLong(0);
        System.out.println(aLong);

        byte[] bytes = buffer.getBytes(Long.BYTES, buffer.length());
        HashMap<String, Object> map1 = (HashMap<String, Object>) SerializeUtil.unserizlize(bytes);

    }

    @Test
    public void test4() throws InterruptedException {

        CountDownLatch latch = new CountDownLatch(1);

        Vertx vertx = Vertx.vertx();
        RedisOptions redisOptions = new RedisOptions();
        redisOptions.setAddress("127.0.0.1").setPort(6379);


        RedisClient redisClient = RedisClient.create(vertx, redisOptions);

        redisClient.setBinaryWithOptions("test01", Buffer.buffer().appendBytes("robini".getBytes()), new SetOptions().setEX(100), res->{
            if(res.succeeded()) {
                System.out.println("success");
                latch.countDown();
            }
        });

        latch.await();
    }

    @Test
    public void test05() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Vertx vertx = Vertx.vertx();
        RedisOptions redisOptions = new RedisOptions();
        redisOptions.setAddress("127.0.0.1").setPort(6379);


        RedisClient redisClient = RedisClient.create(vertx, redisOptions);

        redisClient.del("test01", res->{
            if (res.succeeded()) {
                System.out.println(res.result());
                latch.countDown();
            }
        });

        latch.await();
    }

    @Test
    public void test06() {
        List<String> vector = new Vector<>();

        String s = "robin";
        vector.add(s);

        boolean robin = vector.remove("robin");

        vector.forEach(p->{
            System.out.println(p);
        });
    }

    @Test
    public void test7() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Vertx vertx = Vertx.vertx();
        RedisOptions redisOptions = new RedisOptions();
        redisOptions.setAddress("127.0.0.1").setPort(6379);

        RedisClient redisClient = RedisClient.create(vertx, redisOptions);

        SessionImpl session = new SessionImpl(new PRNG(vertx));
        session.put("username", new User("robin", 47));

        Buffer buffer = Buffer.buffer();
        session.writeToBuffer(buffer);

        redisClient.setBinary(session.id(), buffer, res->{
            if (res.succeeded()) {
                System.out.println("success");
                latch.countDown();
            } else {
                System.out.println("失败");
            }
        });

        latch.await();
    }

    @Test
    public void test8() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Vertx vertx = Vertx.vertx();
        RedisOptions redisOptions = new RedisOptions();
        redisOptions.setAddress("127.0.0.1").setPort(6379);

        RedisClient redisClient = RedisClient.create(vertx, redisOptions);

        redisClient.getBinary("7b266564-01f4-409e-85cd-02fdc423c763", res->{
            if (res.succeeded()) {
                Buffer buffer = res.result();

                SessionImpl session = new SessionImpl(new PRNG(vertx));
                session.readFromBuffer(0, buffer);

                User username = session.get("username");

                System.out.println(username);

                latch.countDown();
            }
        });

        latch.await();
    }

    @Test
    public void test9() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Vertx vertx = Vertx.vertx();
        RedisOptions redisOptions = new RedisOptions();
        redisOptions.setHost("123.207.151.77").setPort(6379);

        RedisClient redisClient = RedisClient.create(vertx, redisOptions);

        redisClient.exists("45c99711-a6c0-42db-8126-d567a60d18e0", res->{
            if (res.succeeded() && res.result()!=0) {
                System.out.println("存在 : "+res.result());
            } else {
                System.out.println("不存在");
            }
            latch.countDown();
        });

        latch.await();
    }
}
