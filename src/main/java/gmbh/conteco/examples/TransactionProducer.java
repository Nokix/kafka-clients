package gmbh.conteco.examples;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.Scanner;

/*
Start Consumer with:
./bin/kafka-console-consumer.sh \
    --topic test \
    --bootstrap-server localhost:9092 \
    --property print.key=true \
    --from-beginning \
    --consumer-property isolation.level=read_committed
 */
public class TransactionProducer {

    public TransactionProducer(String topic) {

        String transactionalId = "random-trans-id-1";

        Properties config = new Properties();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ProducerConfig.CLIENT_ID_CONFIG, "TransactionProducer");
        config.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, transactionalId);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        config.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);

        KafkaProducer<Long, String> producer = new KafkaProducer<>(config);

        Runtime.getRuntime().addShutdownHook(new Thread(producer::close));

        Scanner scanner = new Scanner(System.in);

        System.out.println("New Message");
        producer.initTransactions();
        producer.beginTransaction();
        long i = 0;
        int count = 0;

        while (true) {
            System.out.println("Press enter to commit messages or type in new message");
            String text = scanner.nextLine();
            if (text.equals("")) {
                if (count == 0) {
                    producer.abortTransaction();
                    return;
                }
                producer.commitTransaction();
                System.out.println("New Message. Or press enter to end Producer.");
                producer.beginTransaction();
                count = 0;
                continue;
            }
            count++;

            producer.send(new ProducerRecord<>(topic, i, text));
            i++;
        }
    }

    public static void main(String[] args) {
        new TransactionProducer("test");
    }
}
