package gmbh.conteco;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.Scanner;

public class MyProducer {
    private Scanner scanner;
    private KafkaProducer<String, String> producer;
    private String topic;

    public MyProducer(String topic, Properties properties) {
        this.scanner = new Scanner(System.in);
        this.topic = topic;
        this.producer = new KafkaProducer<>(properties);
        Runtime.getRuntime().addShutdownHook(new Thread(this.producer::close));
    }

    public void startProducer() {
        System.out.println("Start Producing");

        while (true) {
            System.out.println("type in key");
            String key = scanner.nextLine();
            System.out.println("type in value");
            String value = scanner.nextLine();
            if (key.isBlank() || value.isBlank()) break;

            ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);
            producer.send(record);
        }
    }

    public static void main(String[] args) {
        String top = "confluent";
        Properties props = PropertiesLoader.loadConfluent();
        MyProducer myProducer = new MyProducer(top, props);
        myProducer.startProducer();
    }
}
