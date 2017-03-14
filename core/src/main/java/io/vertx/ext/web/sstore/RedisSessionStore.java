package io.vertx.ext.web.sstore;

import io.vertx.core.Vertx;
import io.vertx.ext.web.sstore.impl.LocalSessionStoreImpl;
import io.vertx.ext.web.sstore.impl.RedisSessionStoreImpl;

/**
 * Created by robinyang on 2017/3/13.
 */
public interface RedisSessionStore extends SessionStore {


    /**
     * Default of how often, in ms, to check for expired sessions
     */
    long DEFAULT_RETRY_TIMEOUT = 2 * 1000;

    /**
     * Default name for map used to store sessions
     */
    String DEFAULT_SESSION_MAP_NAME = "vertx-web.sessions";

    static RedisSessionStore create(Vertx vertx) {
        return new RedisSessionStoreImpl(vertx, DEFAULT_SESSION_MAP_NAME, DEFAULT_RETRY_TIMEOUT);
    }


    /**
     * Create a session store
     *
     * @param vertx  the Vert.x instance
     * @param sessionMapName  name for map used to store sessions
     * @return the session store
     */
    static RedisSessionStore create(Vertx vertx, String sessionMapName) {
        return new RedisSessionStoreImpl(vertx, sessionMapName, DEFAULT_RETRY_TIMEOUT);
    }

    /**
     * Create a session store
     *
     * @param vertx  the Vert.x instance
     * @param sessionMapName  name for map used to s tore sessions
     * @param reaperInterval  how often, in ms, to check for expired sessions
     * @return the session store
     */
    static RedisSessionStore create(Vertx vertx, String sessionMapName, long reaperInterval) {
        return new RedisSessionStoreImpl(vertx, sessionMapName, reaperInterval);
    }

    RedisSessionStore host(String host);

    RedisSessionStore port(int port);
}
