package io.vertx.ext.web.sstore.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Session;
import io.vertx.ext.web.sstore.RedisSessionStore;
import io.vertx.redis.RedisClient;
import io.vertx.redis.RedisOptions;

/**
 * Created by robinyang on 2017/3/14.
 */
public class RedisSessionStoreImpl implements RedisSessionStore {

    private final Vertx vertx;
    private final String sessionMapName;
    private final long retryTimeout;

    private String host = "localhost";
    private int port = 6379;

    RedisClient redisClient;


    public RedisSessionStoreImpl(Vertx vertx, String defaultSessionMapName, long retryTimeout) {
        this.vertx = vertx;
        this.sessionMapName = defaultSessionMapName;
        this.retryTimeout = retryTimeout;

        redisManager();
    }

    @Override
    public long retryTimeout() {
        return retryTimeout;
    }

    @Override
    public Session createSession(long timeout) {
        return new SessionImpl(timeout);
    }

    @Override
    public void get(String id, Handler<AsyncResult<Session>> resultHandler) {

    }

    @Override
    public void delete(String id, Handler<AsyncResult<Boolean>> resultHandler) {

    }

    @Override
    public void put(Session session, Handler<AsyncResult<Boolean>> resultHandler) {

//        redisClient.
    }

    @Override
    public void clear(Handler<AsyncResult<Boolean>> resultHandler) {

    }

    @Override
    public void size(Handler<AsyncResult<Integer>> resultHandler) {

    }

    @Override
    public void close() {

    }

    private void redisManager() {
        RedisOptions redisOptions = new RedisOptions();
        redisOptions.setAddress(host).setPort(port);

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
}
