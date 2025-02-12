package faang.school.postservice.kafka;

import faang.school.postservice.model.LikeEvent;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class LikeEventPublisher implements EventPublisher<LikeEvent> {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final NewTopic likeTopic;

    public LikeEventPublisher(KafkaTemplate<String, Object> kafkaTemplate,
                              @Qualifier("likeTopic") NewTopic likeTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.likeTopic = likeTopic;
    }

    @Override
    public void publish(LikeEvent message) {
        kafkaTemplate.send(likeTopic.name(), message);
    }
}
