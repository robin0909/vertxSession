import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.sstore.RedisSessionStore;

/**
 * Created by robin on 2017/3/15.
 */
public class Web extends AbstractVerticle{

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        Router router = Router.router(vertx);

        //cookie session Body
        router.route().handler(CookieHandler.create());
        router.route().handler(BodyHandler.create());

        router.route().handler(SessionHandler.create(RedisSessionStore.create(vertx)));

        router.route("/login").handler(res->{

        });
    }
}
