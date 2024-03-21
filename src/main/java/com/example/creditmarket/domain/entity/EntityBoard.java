package com.example.creditmarket.domain.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "tb_board")
public class EntityBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @ManyToOne(fetch = FetchType.LAZY)
    private EntityUser user;

    @Setter
    private String title;

    @Setter
    private String content;


    @Builder
    public EntityBoard (EntityUser user, String title, String content){
        this.user = user;
        this.title = title;
        this.content = content;
    }

}
