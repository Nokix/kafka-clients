package gmbh.conteco;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class SampleProducer {
    public SampleProducer() throws IOException {
        Properties properties = PropertiesLoader.load();
        KafkaProducer<String,String> producer = new KafkaProducer<>(properties);

        String topic = "test";
        String key = "r1";
        String value = "10";

        ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);

        producer.send(record);
        producer.close();

    }

    public static void main(String[] args) throws IOException {
        new SampleProducer();
    }
}
