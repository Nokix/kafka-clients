package gmbh.conteco.testexample;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;

public class GreeterTopology {
    public static Topology build() {
        StreamsBuilder builder = new StreamsBuilder();

        builder.stream("users", Consumed.with(Serdes.Void(), Serdes.String()))
                .filterNot((k, v) -> v.toLowerCase().equals("randy"))
                .mapValues(v -> "Hallo " + v)
                .to("greetings", Produced.with(Serdes.Void(), Serdes.String()));

        return builder.build();
    }
}
