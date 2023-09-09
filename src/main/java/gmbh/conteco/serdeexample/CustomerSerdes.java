package gmbh.conteco.serdeexample;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

public class CustomerSerdes implements Serde<Customer> {
    @Override
    public Serializer<Customer> serializer() {
        return new CustomerSerializer();
    }

    @Override
    public Deserializer<Customer> deserializer() {
        return new CustomerDeserializer();
    }
}
