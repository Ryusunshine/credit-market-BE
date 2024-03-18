package com.example.creditmarket.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.IOException;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_board_img")
public class EntityBoardImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

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
    @Column(name = "file_path")
    private String filePath;

    public static EntityBoardImg of(EntityBoard board, String originalFileName, String savedName, String filePath) throws IOException {
        EntityBoardImg img = new EntityBoardImg();
        img.setBoard(board);
        img.setOriginalFileName(originalFileName);
        img.setStoredFileName(savedName);
        img.setFilePath(filePath);
        return img;
    }

    public void updateImg(String originalFileName, String savedName, String filePath){
        this.originalFileName = originalFileName;
        this.storedFileName = savedName;
        this.filePath = filePath;
    }
}
