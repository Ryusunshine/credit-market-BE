package com.example.creditmarket.dto.response;

import com.example.creditmarket.domain.entity.EntityComment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentResponse {
    private Long id;
    private String comment;
    private Long userId;
    private String userName;
    private Long boardId;
    private LocalDateTime registeredAt;
    private LocalDateTime updatedAt;
    private LocalDateTime removedAt;

    public static CommentResponse fromEntity(EntityComment entity) {
        return new CommentResponse(
                entity.getCommentId(),
                entity.getComment(),
                entity.getUser().getUserId(),
                entity.getUser().getUserName(),
                entity.getBoard().getBoardId(),
                entity.getRegisteredAt(),
                entity.getUpdatedAt(),
                entity.getRemovedAt()
        );
    }
}