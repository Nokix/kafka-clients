package gmbh.conteco.examples.serdeexample;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class CustomerSerializer implements Serializer<Customer> {
    @Override
    public byte[] serialize(String topic, Customer data) {
        if (data == null) return null;

        try {
            byte[] serializedName = data.getCustomerName().getBytes("UTF-8");
            int stringSize =  serializedName.length;
            ByteBuffer buffer = ByteBuffer.allocate(4 + 4 + stringSize);
            buffer.putInt(data.getCustomerID());
            buffer.putInt(stringSize);
            buffer.put(serializedName);

            return buffer.array();
        } catch (UnsupportedEncodingException e) {
            throw new SerializationException("Error when serializing Customer to byte[] " + e);
        }
    }
}
