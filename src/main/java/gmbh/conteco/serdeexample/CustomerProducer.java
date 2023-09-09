package gmbh.conteco.serdeexample;

import com.github.javafaker.Faker;
import gmbh.conteco.PropertiesLoader;
import lombok.Getter;
import lombok.Setter;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.LongSerializer;

import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Getter
@Setter
public class CustomerProducer {
    private final Faker faker;
    private final KafkaProducer<Long, Customer> producer;

    private String topic = "customers";
    private Long key;
    private AtomicInteger idCounter;

    public CustomerProducer() {
        this.faker = new Faker();
        this.key = faker.number().numberBetween(1L,99L);
        this.idCounter = new AtomicInteger();

        Properties properties = PropertiesLoader.load();
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, CustomerSerializer.class.getName());

        this.producer = new KafkaProducer<>(properties);
    }

    public Customer createRandomCustomer() {
        int id = idCounter.incrementAndGet();
        String name = faker.name().name();
        return new Customer(id, name);
    }

    public CustomerProducer saveRandomCustomers(int amount) {
        Stream.generate(this::createRandomCustomer)
                .limit(amount)
                .map(customer -> new ProducerRecord(topic, key, customer))
                .forEach(producer::send);

        return this;
    }

    public static void main(String[] args) {
        CustomerProducer prod = new CustomerProducer();
        try (Scanner scanner = new Scanner(System.in)) {
            while(true){
                System.out.println("How Many Customers should be saved?");
                int i = scanner.nextInt();
                if (i <= 0) break;
                prod.saveRandomCustomers(i);
                System.out.println("Saved " + i  + " random Customers");
            }
        }

    }
}
