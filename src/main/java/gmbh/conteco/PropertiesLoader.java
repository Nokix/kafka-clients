package gmbh.conteco;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {
    public static Properties load() {
        try (InputStream is = ClassLoader.getSystemResourceAsStream("application.properties")) {
            return new Properties() {{
                this.load(is);
            }};
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        System.out.println(PropertiesLoader.load());
    }
}
