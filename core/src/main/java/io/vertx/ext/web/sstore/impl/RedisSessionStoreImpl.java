package io.vertx.ext.web.sstore.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Session;
import io.vertx.ext.web.sstore.RedisSessionStore;

/**
 * Created by robinyang on 2017/3/14.
 */
public class RedisSessionStoreImpl implements RedisSessionStore {

    public RedisSessionStoreImpl(Vertx vertx, String defaultSessionMapName, long defaultReaperInterval) {

    }

    @Override
    public long retryTimeout() {
        return 0;
    }

    @Override
    public Session createSession(long timeout) {
        return null;
    }

    @Override
    public void get(String id, Handler<AsyncResult<Session>> resultHandler) {

    }

    @Override
    public void delete(String id, Handler<AsyncResult<Boolean>> resultHandler) {

    }

    @Override
    public void put(Session session, Handler<AsyncResult<Boolean>> resultHandler) {

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
}
