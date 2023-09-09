package gmbh.conteco.serdeexample;

import org.apache.kafka.clients.consumer.ConsumerConfig;

import java.util.Map;

public class CustomerConsumer2 {
    public static void main(String[] args) {
        new CustomerConsumer(
                "reader1",
                Map.of(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest",
                        ConsumerConfig.GROUP_INSTANCE_ID_CONFIG, "2",
                        ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false")
        ).showCustomers(1000L);
    }
}
