package io.vertx.ext.web.sstore;

import io.vertx.core.Vertx;
import io.vertx.ext.web.sstore.impl.RedisSessionStoreImpl;

/**
 * Created by robinyang on 2017/3/13.
 */
public interface RedisSessionStore extends SessionStore {


    /**
     * Default of how often, in ms, to check for expired sessions
     */
    long DEFAULT_REAPER_INTERVAL = 1000;

    /**
     * Default name for map used to store sessions
     */
    String DEFAULT_SESSION_MAP_NAME = "vertx-web.sessions";

    static RedisSessionStore create(Vertx vertx) {
        return new RedisSessionStoreImpl(vertx, DEFAULT_SESSION_MAP_NAME, DEFAULT_REAPER_INTERVAL);
    }
}
