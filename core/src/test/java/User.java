import java.io.Serializable;

/**
 * Created by robin on 2017/3/14.
 */
public class User implements Serializable{

    private int key;
    private String name;

    public User() {
    }

    public User(String name, int key) {
        this.name = name;
        this.key = key;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "key=" + key +
                ", name='" + name + '\'' +
                '}';
    }
}
