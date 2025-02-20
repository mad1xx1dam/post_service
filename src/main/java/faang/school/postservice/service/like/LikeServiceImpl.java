package faang.school.postservice.service.like;

import faang.school.postservice.client.UserServiceClient;
import faang.school.postservice.dto.user.UserDto;
import faang.school.postservice.model.Like;
import faang.school.postservice.model.Post;
import faang.school.postservice.model.Comment;
import faang.school.postservice.repository.PostRepository;
import faang.school.postservice.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserServiceClient userServiceClient;

    @Value("${like-service.batch-size}")
    private int batchSize;

    @PostConstruct
    public void init() {
        log.info("Batch size from configuration: {}", batchSize);
    }

    @Override
    public List<UserDto> getUsersWhoLikedPost(Long postId) {
        log.info("Getting users who liked post with id: {}", postId);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post with id " + postId + " not found"));

        List<Like> likes = post.getLikes();

        List<Long> userIds = likes.stream()
                .map(Like::getUserId)
                .toList();

        return getUsersInBatches(userIds);
    }

    @Override
    public List<UserDto> getUsersWhoLikedComment(Long commentId) {
        log.info("Getting users who liked comment with id: {}", commentId);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment with id " + commentId + " not found"));

        List<Like> likes = comment.getLikes();

        List<Long> userIds = likes.stream()
                .map(Like::getUserId)
                .toList();

        return getUsersInBatches(userIds);
    }

    private List<UserDto> getUsersInBatches(List<Long> userIds) {
        List<UserDto> users = new ArrayList<>();

        List<List<Long>> batches = ListUtils.partition(userIds, batchSize);
        for (List<Long> batch : batches) {
            List<UserDto> batchUsers = userServiceClient.getUsersByIds(batch);
            users.addAll(batchUsers);
        }
        return users;
    }
}