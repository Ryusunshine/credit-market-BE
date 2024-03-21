package com.example.creditmarket.service.Impl;


import com.example.creditmarket.common.AppException;
import com.example.creditmarket.common.ErrorCode;
import com.example.creditmarket.domain.entity.EntityBoard;
import com.example.creditmarket.domain.entity.EntityBoardFile;
import com.example.creditmarket.domain.repository.BoardFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServiceImpl{

    @Value("${file.dir}")
    private String fileDir;

    private final BoardFileRepository boardFileRepository;

    public void init() {
        try {
            Files.createDirectories(Paths.get(fileDir));
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload folder!");
        }
    }

    public EntityBoardFile store(MultipartFile file, EntityBoard board) {
        try {
            if (file.isEmpty()) {
                throw new Exception("ERROR : File is empty.");
            }
            Path root = Paths.get(fileDir);
            if (!Files.exists(root)) {
                init();
            }

            String origName = file.getOriginalFilename();
            // 파일 이름으로 쓸 uuid 생성
            String uuid = UUID.randomUUID().toString();

            String extension = origName.substring(origName.lastIndexOf("."));

            String savedName = uuid + extension;

            // 파일을 불러올 때 사용할 파일 경로
            String filePath = root + "/" + uuid;

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, root.resolve(savedName),
                        StandardCopyOption.REPLACE_EXISTING);

                EntityBoardFile commentaryFile = EntityBoardFile.of(board, origName, savedName, filePath);
                EntityBoardFile savedCommentaryFile = boardFileRepository.save(commentaryFile);
                return savedCommentaryFile;
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    public Path load(String filename) {
        return Paths.get(fileDir).resolve(filename);
    }

    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new RuntimeException("Could not read file: " + filename);
            }
        }
        catch (MalformedURLException e) {
            throw new RuntimeException("Could not read file: " + filename, e);
        }
    }


    public EntityBoardFile getFile(Long id) {
        return boardFileRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.DATABASE_ERROR, ""));
    }

}
