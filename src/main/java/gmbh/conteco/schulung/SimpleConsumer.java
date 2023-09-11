package gmbh.conteco.schulung;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.apache.kafka.clients.consumer.ConsumerConfig.*;

public class SimpleConsumer {

    public SimpleConsumer() {
        Properties props = new Properties();
        props.put(BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(GROUP_ID_CONFIG, "SimpleGroup");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        Runtime.getRuntime().addShutdownHook(new Thread(consumer::close));

        consumer.subscribe(List.of("test"));

        Duration delay = Duration.ofMillis(100L);
        System.out.println("Ich höre");
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(delay);
            for (ConsumerRecord<String, String> record : records) {
                System.out.println("Key: " + record.key());
                System.out.println("Value: " + record.value());
                System.out.println("Partition: " + record.partition());
                System.out.println();
            }
        }
    }

    public static void main(String[] args) {
        new SimpleConsumer();
        // Strg+Shift gedrückt: mit Pfeiltasten Zeilen verschieben.
        //Strg+Shift+Enter: Zeile beenden
        // main: main methode schreiben
        // .new: new Keyword vorne hinzufügen
        // Strg+P: Parameter anzeigen
        // Strg+D: Dublizieren einer Zeile
        // Bei Vorschlägen Tab: überschreibe
        // Bei Vorschlägen Enter: Füge hinzu
        // .var: Variable deklariren.
    }
}
