package gmbh.conteco.examples;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {
    public static Properties loadDefault() {
        return load("application.properties");
    }

    public static Properties loadConfluent() {
        return load("confluent.properties");
    }

    public static Properties load(String recourcesPath) {
        try (InputStream is = ClassLoader.getSystemResourceAsStream(recourcesPath)) {
            return new Properties() {{
                this.load(is);
            }};
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        System.out.println(PropertiesLoader.loadDefault());
    }
}
