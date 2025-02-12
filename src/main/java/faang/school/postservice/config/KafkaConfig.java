package faang.school.postservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
    @Bean(name = "likeTopic")
    public NewTopic likeTopic() {
        return TopicBuilder.name("like_topic")
                .partitions(10)
                .replicas(1)
                .build();
    }
}
