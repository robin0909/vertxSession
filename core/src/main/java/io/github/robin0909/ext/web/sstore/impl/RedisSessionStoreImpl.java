package io.github.robin0909.ext.web.sstore.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.shareddata.LocalMap;
import io.vertx.ext.auth.PRNG;
import io.vertx.ext.web.Session;
import io.github.robin0909.ext.web.sstore.RedisSessionStore;
import io.vertx.ext.web.sstore.impl.SessionImpl;
import io.vertx.redis.RedisClient;
import io.vertx.redis.RedisOptions;
import io.vertx.redis.op.SetOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Vector;

/**
 * Created by robinyang on 2017/3/14.
 */
public class RedisSessionStoreImpl implements RedisSessionStore {

    private static final Logger logger = LoggerFactory.getLogger(RedisSessionStoreImpl.class);

    private final Vertx vertx;
    private final String sessionMapName;
    private final long retryTimeout;
    private final LocalMap<String, Session> localMap;

    //默认值
    private String host = "localhost";
    private int port = 6379;
    private String auth;

    RedisClient redisClient;

    // 清除所有时，使用
    private List<String> localSessionIds;


    public RedisSessionStoreImpl(Vertx vertx, String defaultSessionMapName, long retryTimeout) {
        this.vertx = vertx;
        this.sessionMapName = defaultSessionMapName;
        this.retryTimeout = retryTimeout;

        localMap = vertx.sharedData().getLocalMap(sessionMapName);
        localSessionIds = new Vector<>();
        redisManager();
    }

    @Override
    public long retryTimeout() {
        return retryTimeout;
    }

    @Override
    public Session createSession(long timeout) {
        return new SessionImpl(new PRNG(vertx), timeout, DEFAULT_SESSIONID_LENGTH);
    }

    @Override
    public Session createSession(long timeout, int length) {
        return new SessionImpl(new PRNG(vertx), timeout, length);
    }

    @Override
    public void get(String id, Handler<AsyncResult<Session>> resultHandler) {
        redisClient.getBinary(id, res->{
            if(res.succeeded()) {
                Buffer buffer = res.result();
                if(buffer != null) {
                    SessionImpl session = new SessionImpl(new PRNG(vertx));
                    session.readFromBuffer(0, buffer);
                    resultHandler.handle(Future.succeededFuture(session));
                } else {
                    resultHandler.handle(Future.succeededFuture(localMap.get(id)));
                }
            } else {
                resultHandler.handle(Future.failedFuture(res.cause()));
            }
        });
    }

    @Override
    public void delete(String id, Handler<AsyncResult<Boolean>> resultHandler) {
        redisClient.del(id, res->{
            if (res.succeeded()) {
                localSessionIds.remove(id);
                resultHandler.handle(Future.succeededFuture(true));
            } else {
                resultHandler.handle(Future.failedFuture(res.cause()));
                logger.error("redis里删除sessionId: {} 失败", id, res.cause());
            }
        });
    }

    @Override
    public void put(Session session, Handler<AsyncResult<Boolean>> resultHandler) {
        //put 之前判断下是否存在，如果存在的话，校验下
        redisClient.getBinary(session.id(), res1->{
            if (res1.succeeded()) {
                //存在数据
                if(res1.result()!=null) {
                    Buffer buffer = res1.result();
                    SessionImpl oldSession = new SessionImpl(new PRNG(vertx));
                    oldSession.readFromBuffer(0, buffer);
                    SessionImpl newSession = (SessionImpl)session;
                    if(oldSession.version() != newSession.version()) {
                        resultHandler.handle(Future.failedFuture("Version mismatch"));
                        return;
                    }
                    newSession.incrementVersion();
                    writeSession(session, resultHandler);
                } else {
                    //不存在数据
                    SessionImpl newSession = (SessionImpl)session;
                    newSession.incrementVersion();
                    writeSession(session, resultHandler);
                }
            } else {
                resultHandler.handle(Future.failedFuture(res1.cause()));
            }
        });
    }

    private void writeSession(Session session, Handler<AsyncResult<Boolean>> resultHandler) {

        Buffer buffer = Buffer.buffer();
        SessionImpl sessionImpl = (SessionImpl)session;
        //讲session序列化到 buffer里
        sessionImpl.writeToBuffer(buffer);

        SetOptions setOptions = new SetOptions().setPX(session.timeout());
        redisClient.setBinaryWithOptions(session.id(), buffer, setOptions, res->{
            if (res.succeeded()) {
                logger.debug("set key: {} ", session.data());
                localSessionIds.add(session.id());
                resultHandler.handle(Future.succeededFuture(true));
            } else {
                resultHandler.handle(Future.failedFuture(res.cause()));
            }
        });
    }

    @Override
    public void clear(Handler<AsyncResult<Boolean>> resultHandler) {
        localSessionIds.stream().forEach(id->{
            redisClient.del(id, res->{
                //有可能 localSessionIds 里存在的，但是 redis 过期不存在了, 只要通知下就行
                localSessionIds.remove(id);
            });
        });
        resultHandler.handle(Future.succeededFuture(true));
    }

    @Override
    public void size(Handler<AsyncResult<Integer>> resultHandler) {
        resultHandler.handle(Future.succeededFuture(localSessionIds.size()));
    }

    @Override
    public void close() {
        redisClient.close(res->{
            logger.debug("关闭 redisClient ");
        });
    }

    private void redisManager() {
        RedisOptions redisOptions = new RedisOptions();
        redisOptions.setHost(host).setPort(port).setAuth(auth);

        redisClient = RedisClient.create(vertx, redisOptions);
    }

    @Override
    public RedisSessionStore host(String host) {
        this.host = host;
        return this;
    }

    @Override
    public RedisSessionStore port(int port) {
        this.port = port;
        return this;
    }

    @Override
    public RedisSessionStore auth(String pwd) {
        this.auth = pwd;
        return this;
    }
}
