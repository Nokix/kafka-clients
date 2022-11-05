package gmbh.conteco;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.Produced;

import java.util.Arrays;

public class WordCountTopology {
    public static Topology build() {
        StreamsBuilder builder = new StreamsBuilder();
        KStream<Void, String> stream = builder.stream("sentences");
        stream.flatMapValues(value -> Arrays.asList(value.toLowerCase().split(" ")))
                .map((k,v) -> new KeyValue<>(v,v))
                .groupByKey()
                .count()
                .mapValues(v -> Long.toString(v))
                .toStream()
                .to("wordcount");
//                .toStream()
//                .foreach((k,v) -> System.out.println(k + " " + v));


        return builder.build();
    }
}
