import io.vertx.core.Vertx;

/**
 * Created by robin on 2017/3/15.
 */
public class Main {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new Web());
    }
}
