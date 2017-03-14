import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Session;
import io.vertx.ext.web.sstore.impl.SessionImpl;
import io.vertx.ext.web.utils.SerializeUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

    }
}
