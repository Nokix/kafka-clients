package gmbh.conteco.tweets;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.LongSerializer;

import java.util.Properties;
import java.util.Scanner;

public class TweetConsoleProducer {
    public static long tweetId = 0;

    private static long nextTweetId() {
        tweetId += 1;
        return tweetId;
    }
    public static void main(String[] args) {

        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, TweetSerializer.class.getName());

        try(KafkaProducer<Long, Tweet> producer = new KafkaProducer<>(properties)) {
            String topic = "tweets";
            Runtime.getRuntime().addShutdownHook(new Thread(producer::close));

            while (true) {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Insert Tweet:");
                String text = scanner.nextLine();

                if (text.equals("")) {
                    return;
                }

                System.out.println("Language Englisch? [Y|n]");
                String isEnglishText = scanner.nextLine();

                boolean isEnglish = isEnglishText.equalsIgnoreCase("y");
                long tweetId = nextTweetId();
                Tweet tweet = new Tweet(tweetId, isEnglish ? "eng" : "non", text);
                System.out.println(tweet);

                ProducerRecord<Long, Tweet> record = new ProducerRecord<>(topic, tweetId, tweet);
                producer.send(record);
            }
        }
    }
}
