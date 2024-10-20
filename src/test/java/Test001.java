import java.io.InputStream;

public class Test001 {
    public static void main(String[] args) {
        ClassLoader classLoader = Test001.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("config\\config.yml");
    }
}
