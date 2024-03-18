package com.example.creditmarket.entity;

import com.example.creditmarket.dto.request.BoardRequest;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "tb_board")
public class EntityBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @Setter
    private String title;

    @Setter
    private String content;


    @Builder
    public EntityBoard (String title, String content){
        this.title = title;
        this.content = content;
    }

}
