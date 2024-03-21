package com.example.creditmarket.dto.response;

import com.example.creditmarket.domain.entity.EntityBoard;
import com.example.creditmarket.domain.entity.EntityBoardFile;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class BoardResponse {

    private Long boardId;
    private String title;
    private DataResponse image;
    private String fileName;
    private Long fileId;
    private Long fileDownloadCount;

    public static BoardResponse of(EntityBoard board, EntityBoardFile boardFile, DataResponse image){
        return BoardResponse.builder()
                .boardId(board.getBoardId())
                .title(board.getTitle())
                .fileName(boardFile.getOriginalFileName())
                .fileId(boardFile.getBoardFileId())
                .fileDownloadCount(boardFile.getDownloadCount())
                .image(image)
                .build();
    }
}
