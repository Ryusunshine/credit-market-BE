package com.example.creditmarket.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "tb_comment")
@NoArgsConstructor
public class EntityComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    private String comment;

    @ManyToOne
    @JoinColumn(name = "user_id_fk")
    private EntityUser user;

    @ManyToOne
    @JoinColumn(name = "board_id_fk")
    private EntityBoard board;

    @Column(name = "registered_at")
    private LocalDateTime registeredAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "removed_at")
    private LocalDateTime removedAt;

    public static EntityComment of(String comment, EntityBoard board, EntityUser user) {
        EntityComment entity = new EntityComment();
        entity.setComment(comment);
        entity.setBoard(board);
        entity.setUser(user);
        return entity;
    }
}
