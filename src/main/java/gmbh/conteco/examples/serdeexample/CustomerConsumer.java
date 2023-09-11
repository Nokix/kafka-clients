package gmbh.conteco.examples.serdeexample;

import gmbh.conteco.examples.PropertiesLoader;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.LongDeserializer;

import java.time.Duration;
import java.util.*;

public class CustomerConsumer {
    private final KafkaConsumer<Long, Customer> consumer;
    private String topic = "customers";
    private HashMap<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();

    public CustomerConsumer(String groupId) {
        this(groupId, Map.of());
    }

    public CustomerConsumer(String groupId, Map<?, ?> props) {
        Properties properties = PropertiesLoader.loadDefault();
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, CustomerDeserializer.class.getName());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.putAll(props);
        this.consumer = new KafkaConsumer<>(properties);
        this.consumer.subscribe(List.of(topic), new RebalanceHandler());
    }

    public void showCustomers(Long delay) {
        Duration timeout = Duration.ofMillis(100L);
        int count = 0;
        try {
            while (true) {
                ConsumerRecords<Long, Customer> records = consumer.poll(timeout);

                for (ConsumerRecord<Long, Customer> record : records) {
                    Thread.sleep(delay);
                    System.out.println(record.value());
                    updateCurrentOffset(record, currentOffsets);

                    count++;
                    if (count % 5 == 0) {
                        consumer.commitAsync(currentOffsets, null);
                    }
                }
                consumer.commitSync(currentOffsets);
            }
        } catch (Exception e) {
            consumer.commitSync(currentOffsets);
            throw new RuntimeException(e);
        }
    }

    private void updateCurrentOffset(
            ConsumerRecord<?, ?> record,
            HashMap<TopicPartition, OffsetAndMetadata> currentOffsets) {
        currentOffsets.put(
                new TopicPartition(record.topic(), record.partition()),
                new OffsetAndMetadata(record.offset() + 1, "no metadata")
        );
    }

    private class RebalanceHandler implements ConsumerRebalanceListener {
        @Override
        public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
            System.out.println("Lost Partition in rebalance");
            partitions.forEach(System.out::println);
            consumer.commitSync(currentOffsets);
        }

        @Override
        public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
            System.out.println("Got Partions:");
            partitions.forEach(System.out::println);
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
