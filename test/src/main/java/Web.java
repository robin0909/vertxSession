import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.github.robin0909.ext.web.sstore.RedisSessionStore;

/**
 * Created by robin on 2017/3/15.
 */
public class Web extends AbstractVerticle{

    final private static String USR = "web_vertx_user";

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        Router router = Router.router(vertx);

        //cookie session Body
        router.route().handler(CookieHandler.create());
        router.route().handler(BodyHandler.create());

        router.route().handler(SessionHandler.create(RedisSessionStore.create(vertx).host("127.0.0.1").port(6349)));

        router.route("/static/*").handler(StaticHandler.create("web/static"));
        router.route("/private/*").handler(this::auth);
        router.route("/loginHandle").handler(this::loginHandle);
        router.route("/private/index").handler(this::index);

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(8081);
        startFuture.complete();
    }

    private void index(RoutingContext routingContext) {
        routingContext.response().end("welcome vertx");
    }

    private void loginHandle(RoutingContext routingContext) {
        String username = routingContext.request().getParam("username");
        String password = routingContext.request().getParam("password");

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        Session session = routingContext.session();
        session.put(USR, user);

        routingContext.response().putHeader("location", "/private/index").setStatusCode(302).end();
    }

    private void auth(RoutingContext routingContext) {
        Session session = routingContext.session();
        User user = session.get(USR);
        if(user==null) {
            routingContext.response().putHeader("location", "/static/login.html").setStatusCode(302).end();
        } else {
            routingContext.next();
        }
    }
}
