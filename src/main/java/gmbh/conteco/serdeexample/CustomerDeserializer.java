package gmbh.conteco.serdeexample;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.nio.ByteBuffer;

public class CustomerDeserializer implements Deserializer<Customer> {

    @Override
    public Customer deserialize(String topic, byte[] data) {
        if (data == null) return null;

        if (data.length < 8) throw new SerializationException("Data too short");

        try {
            ByteBuffer buffer = ByteBuffer.wrap(data);

            int id = buffer.getInt();
            int nameSize = buffer.getInt();
            byte[] namedBytes = new byte[nameSize];
            buffer.get(namedBytes);
            String name = new String(namedBytes, "UTF-8");

            return new Customer(id, name);
        } catch (Exception e) {
            throw new SerializationException("Error when deserializing to Customer " + e);
        }
    }
}
