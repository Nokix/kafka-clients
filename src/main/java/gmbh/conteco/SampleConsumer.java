package gmbh.conteco;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Properties;

public class SampleConsumer {
    public SampleConsumer() {
        Properties properties = PropertiesLoader.load();
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put("group.id", "SampleConsumer2");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        consumer.subscribe(Collections.singletonList("test"));

        Duration timeout = Duration.ofMillis(100);

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(timeout);
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("topic = %s, partition = %d, offset = %d, key = %s, value = %s",
                        record.topic(), record.partition(), record.offset(), record.key(), record.value());
            }
        }
    }

    public static void main(String[] args) {
        new SampleConsumer();
    }


}
