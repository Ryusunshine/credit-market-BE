package com.example.creditmarket.dto.request;

import com.example.creditmarket.entity.EntityBoard;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardRequest {

    private String title;
    private String content;

    public EntityBoard toEntity(){
        return EntityBoard.builder()
                .title(title)
                .content(content)
                .build();
    }
}
