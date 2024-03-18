package com.example.creditmarket.service.Impl;

import com.example.creditmarket.dto.request.BoardRequest;
import com.example.creditmarket.dto.response.BoardResponse;
import com.example.creditmarket.dto.response.DataResponse;
import com.example.creditmarket.entity.EntityBoard;
import com.example.creditmarket.entity.EntityBoardFile;
import com.example.creditmarket.entity.EntityBoardImg;
import com.example.creditmarket.exception.AppException;
import com.example.creditmarket.exception.ErrorCode;
import com.example.creditmarket.repository.BoardFileRepository;
import com.example.creditmarket.repository.BoardImgRepository;
import com.example.creditmarket.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.io.*;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl {
    private final EntityManager em;
    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;
    private final BoardImgRepository boardImgRepository;
    private final FileServiceImpl fileService;

    /**
     * 게시글 등록
     */
    @Transactional
    public String create(BoardRequest request, MultipartFile image, MultipartFile file) throws Exception {
        EntityBoard board = request.toEntity();
        if (image != null && file != null){ // 이미지, 파일 두개 다 존재
            EntityBoardImg newImage = saveImgInDir(image, board);
            boardImgRepository.save(newImage);
            fileService.store(file, board);
        } else if (image == null && file != null){ //파일만 존재
            fileService.store(file, board);
        } else if (image != null && file == null){ // 이미지만 존재
            EntityBoardImg newImage = saveImgInDir(image, board);
            boardImgRepository.save(newImage);
        }
        try {
            boardRepository.save(board);
        } catch (Exception e){
            throw new AppException(ErrorCode.DATABASE_ERROR, "저장에 실패했습니다");
        }

        return "SUCCESS";
    }

    /**
     * 게시글 조회
     */
    @Transactional(readOnly = true)
    public BoardResponse getBoardDetail(Long boardId){
        EntityBoard board = boardRepository.findById(boardId).orElseThrow(
                ()-> new AppException(ErrorCode.DATABASE_ERROR, "해당 게시글이 존재하지 않습니다."));
        EntityBoardFile commentaryFile = boardFileRepository.findByBoard(board)
                .orElseThrow(null);
        DataResponse image = getResponse(boardId, board);

        return BoardResponse.of(board, commentaryFile, image);

    }

    /**
     * 이미지 저장
     */
    @Transactional
    public EntityBoardImg saveImgInDir(MultipartFile imageFile, EntityBoard board) throws Exception {
        String imagePath = null;
        String absolutePath = new File("").getAbsolutePath() +"//";
        String path = "images";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }

        if (!imageFile.isEmpty()) {
            String contentType = imageFile.getContentType();
            String originalFileExtension;
            if (ObjectUtils.isEmpty(contentType)) {
                throw new Exception("이미지 파일은 jpg, png 만 가능합니다.");
            } else {
                if (contentType.contains("image/jpeg")) {
                    originalFileExtension = ".jpg";
                } else if (contentType.contains("image/png")) {
                    originalFileExtension = ".png";
                } else {
                    throw new Exception("이미지 파일은 jpg, png 만 가능합니다.");
                }
            }
            String origName = imageFile.getOriginalFilename();

            // 파일 이름으로 쓸 uuid 생성
            String uuid = UUID.randomUUID().toString();

            // 파일을 불러올 때 사용할 파일 경로
            String savedName = path + "/" + uuid;

            imagePath = path + "/" + uuid + originalFileExtension;

            file = new File(absolutePath + imagePath);
            imageFile.transferTo(file);
            return EntityBoardImg.of(board, origName, savedName, imagePath);
        }
        else {
            throw new Exception("이미지 파일이 비어있습니다.");
        }
    }

    /**
     * 이미지 출력
     */
    @Transactional(readOnly = true)
    public DataResponse getResponse(Long id, EntityBoard board){

        DataResponse response = new DataResponse();

        try {
            if (board == null){
                board = boardRepository.findById(id).orElseThrow(null);
            }

            response.setResponse("success");
            response.setData(getImage(board));
        }
        catch (Exception e) {
            e.printStackTrace();
            response.setResponse("failed");
            response.setData(null);
        }
        return response;
    }

    /**
     * 이미지 불러오기
     */
    @Transactional(readOnly = true)
    public byte[] getImage(EntityBoard board) throws Exception {
        EntityBoardImg boardImg = boardImgRepository.findByBoard(board).orElseThrow(null);
        String imagePath = boardImg.getFilePath();
        FileInputStream inputStream = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        String absolutePath = new File("").getAbsolutePath() + "//";
        try {
            inputStream = new FileInputStream(absolutePath + imagePath);
        }
        catch (FileNotFoundException e) {
            throw new Exception("해당 파일을 찾을 수 없습니다.");
        }

        int readCount = 0;
        byte[] buffer = new byte[1024];
        byte[] fileArray = null;

        try {
            while((readCount = inputStream.read(buffer)) != -1){
                outputStream.write(buffer, 0, readCount);
            }
            fileArray = outputStream.toByteArray();
            inputStream.close();
            outputStream.close();

        }
        catch (IOException e) {
            throw new Exception("파일을 변환하는데 문제가 발생했습니다.");
        }

        return fileArray;
    }

    /**
     * 첨부파일 불러오기
     */
    @Transactional(readOnly = true)
    public EntityBoardFile getFile(Long id){
        return boardFileRepository.findById(id).orElseThrow(null);
    }


}
