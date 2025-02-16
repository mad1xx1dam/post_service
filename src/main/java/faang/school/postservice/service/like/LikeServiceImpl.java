package faang.school.postservice.service.like;

import faang.school.postservice.client.UserServiceClient;
import faang.school.postservice.dto.user.UserDto;
import faang.school.postservice.model.Like;
import faang.school.postservice.model.Post;
import faang.school.postservice.model.Comment;
import faang.school.postservice.repository.PostRepository;
import faang.school.postservice.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserServiceClient userServiceClient;

    private static final int BATCH_SIZE = 100;

    @Override
    public List<UserDto> getUsersWhoLikedPost(Long postId) {
        log.info("Getting users who liked post with id: {}", postId);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        List<Like> likes = post.getLikes();

        List<Long> userIds = likes.stream()
                .map(Like::getUserId)
                .collect(Collectors.toList());

        return getUsersInBatches(userIds);
    }

    @Override
    public List<UserDto> getUsersWhoLikedComment(Long commentId) {
        log.info("Getting users who liked comment with id: {}", commentId);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        List<Like> likes = comment.getLikes();

        List<Long> userIds = likes.stream()
                .map(Like::getUserId)
                .collect(Collectors.toList());

        return getUsersInBatches(userIds);
    }

    private List<UserDto> getUsersInBatches(List<Long> userIds) {
        List<UserDto> users = new ArrayList<>();

        for (int i = 0; i < userIds.size(); i += BATCH_SIZE) {
            List<Long> batch = userIds.subList(i, Math.min(i + BATCH_SIZE, userIds.size()));

            List<UserDto> batchUsers = userServiceClient.getUsersByIds(batch);
            users.addAll(batchUsers);
        }

        return users;
    }
}