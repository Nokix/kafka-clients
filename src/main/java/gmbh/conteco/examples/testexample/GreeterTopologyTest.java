package gmbh.conteco.examples.testexample;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.test.TestRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

public class GreeterTopologyTest {
    private TopologyTestDriver testDriver;
    private TestInputTopic<Void, String> inputTopic;
    private TestOutputTopic<Void, String> outputTopic;

    @BeforeEach
    void setup() {
        Topology topology = GreeterTopology.build();

        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "test");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "dummy:1234");

        testDriver = new TopologyTestDriver(topology, props);

        inputTopic = testDriver.createInputTopic("users", Serdes.Void().serializer(), Serdes.String().serializer());
        outputTopic = testDriver.createOutputTopic("greetings", Serdes.Void().deserializer(), Serdes.String().deserializer());
    }

    @AfterEach
    void teardownd() {
        testDriver.close();
    }

    @Test
    void testUsersGreeted() {
        String value = "Izzy";
        inputTopic.pipeInput(value);
        assertThat(outputTopic.isEmpty()).isFalse();
        inputTopic.pipeInput("Randy"); //should be ignored

        List<TestRecord<Void, String>> outputRecords = outputTopic.readRecordsToList();
        assertThat(outputRecords).hasSize(1);
        assertThat(outputRecords.get(0).getValue()).isEqualTo("Hallo Izzy");
    }

}
