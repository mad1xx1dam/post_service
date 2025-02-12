package faang.school.postservice.service;

import faang.school.postservice.model.LikeEvent;
import faang.school.postservice.kafka.LikeEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeEventPublisher likeEventPublisher;

    public void like(LikeEvent event) {
        likeEventPublisher.publish(event);
    }
}
