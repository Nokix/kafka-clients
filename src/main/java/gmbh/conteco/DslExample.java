package gmbh.conteco;

import gmbh.conteco.serdeexample.Customer;
import gmbh.conteco.serdeexample.CustomerSerdes;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;

import java.util.Properties;

public class DslExample {
    public static void main(String[] args) {
        StreamsBuilder builder = new StreamsBuilder();

        KStream<Long, Customer> stream = builder.stream("customers");

        stream.foreach((key, value) -> System.out.println("key:" + key + " value: " + value));
        // stream.map((key, value) -> new KeyValue<>(value, value)).to("test2");

        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "dev1");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        //config.put(StreamsConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.Long().getClass());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, CustomerSerdes.class.getName());

        KafkaStreams streams = new KafkaStreams(builder.build(), config);

        streams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }
}
