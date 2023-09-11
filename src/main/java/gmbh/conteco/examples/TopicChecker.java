package gmbh.conteco.examples;

import lombok.SneakyThrows;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.TopicListing;

import java.util.Properties;
import java.util.Scanner;

public class TopicChecker {
    private final AdminClient adminClient;

    public TopicChecker() {
        this(PropertiesLoader.loadDefault());
    }

    public TopicChecker(Properties properties) {
        this.adminClient = AdminClient.create(properties);
    }

    @SneakyThrows
    public Boolean doesTopicExist(String topicName) {
        return adminClient.listTopics().listings().get()
                .stream()
                .map(TopicListing::name)
                .anyMatch(name -> name.equals(topicName));
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TopicChecker topicChecker = new TopicChecker();

        while (true) {
            System.out.println("Type in Topicname:");
            String name = scanner.nextLine();
            if (name.isBlank()) break;
            Boolean topicExists = topicChecker.doesTopicExist(name);
            System.out.println("Topic existiert: " + topicExists);
        }

    }
}
