package faang.school.postservice.service.like;

import faang.school.postservice.dto.user.UserDto;

import java.util.List;

public interface LikeService {
    List<UserDto> getUsersWhoLikedPost(Long postId);

    List<UserDto> getUsersWhoLikedComment(Long commentId);
}