package com.example.creditmarket.domain.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_board_file")
public class EntityBoardFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardFileId;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id_fk")
    private EntityBoard board;

    @Setter
    @Column(name = "original_file_name")
    private String originalFileName;

    @Setter
    @Column(name = "stored_file_name")
    private String storedFileName;

    @Setter
    @Column(name = "download_count")
    private Long downloadCount;

    @Setter
    @Column(name = "file_path")
    private String filePath;

    public static EntityBoardFile of(EntityBoard board, String originalFileName, String storedFileName, String filePath){
        EntityBoardFile boardFile = new EntityBoardFile();
        boardFile.setBoard(board);
        boardFile.setOriginalFileName(originalFileName);
        boardFile.setStoredFileName(storedFileName);
        boardFile.setDownloadCount(0L);
        boardFile.setFilePath(filePath);
        return boardFile;
    }

    public void download(){
        this.downloadCount += 1;
    }

}
