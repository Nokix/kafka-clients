package gmbh.conteco.serdeexample;

import gmbh.conteco.PropertiesLoader;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.LongSerializer;

import java.time.Duration;
import java.util.*;

public class CustomerConsumer {
    private final KafkaConsumer<Long, Customer> consumer;
    private String topic = "customers";

    public CustomerConsumer(String groupId) {
        this(groupId, Map.of());
    }

    public CustomerConsumer(String groupId, Map<?,?> props) {
        Properties properties = PropertiesLoader.load();
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, CustomerDeserializer.class.getName());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.putAll(props);
        this.consumer = new KafkaConsumer<>(properties);
        this.consumer.subscribe(List.of(topic));
    }

    public void showCustomers(Long delay) {
        Duration timeout = Duration.ofMillis(100L);
        //HashMap<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();

        try {
            while (true) {
                ConsumerRecords<Long, Customer> records = consumer.poll(timeout);

                for (ConsumerRecord<Long, Customer> record : records) {
                    Thread.sleep(delay);
                    System.out.println(record.value());
                }

                consumer.commitAsync();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        new CustomerConsumer(
                "reader1",
                Map.of(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest",
                        ConsumerConfig.GROUP_INSTANCE_ID_CONFIG, "1",
                        ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false")
        ).showCustomers(1000L);
    }
}
