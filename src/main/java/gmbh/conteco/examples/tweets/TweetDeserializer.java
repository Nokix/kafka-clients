package gmbh.conteco.examples.tweets;

import com.google.gson.Gson;
import org.apache.kafka.common.serialization.Deserializer;

import java.nio.charset.StandardCharsets;

public class TweetDeserializer implements Deserializer<Tweet> {
    private Gson gson = new Gson();

    @Override
    public Tweet deserialize(String s, byte[] bytes) {
        if (bytes == null)
            return null;

        return gson.fromJson(new String(bytes, StandardCharsets.UTF_8), Tweet.class);
    }
}
