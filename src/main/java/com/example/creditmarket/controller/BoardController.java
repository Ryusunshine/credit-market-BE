package com.example.creditmarket.controller;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.example.creditmarket.dto.request.BoardRequest;
import com.example.creditmarket.dto.response.BoardResponse;
import com.example.creditmarket.dto.response.DataResponse;
import com.example.creditmarket.entity.EntityBoardFile;
import com.example.creditmarket.exception.AppException;
import com.example.creditmarket.exception.ErrorCode;
import com.example.creditmarket.service.Impl.BoardServiceImpl;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    @Autowired
    ResourceLoader resourceLoader;

    private final BoardServiceImpl boardService;
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;


    @ApiOperation(value = "게시글 등록", notes = "게시글을 등록합니다.")
    @PostMapping("save")
    public ResponseEntity<String> create(
            @RequestPart(value = "board") BoardRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart(value = "file", required = false) MultipartFile file) throws Exception {

        try {
            boardService.create(request, image, file);
            //s3
            String fileName= image.getOriginalFilename();
            String fileUrl= "https://" + bucket + "/image" +fileName;
            ObjectMetadata metadata= new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());
            amazonS3Client.putObject(bucket,fileName,file.getInputStream(),metadata);
            return ResponseEntity.ok(fileUrl);
        } catch (IOException e) {
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR, "저장에 실패하였습니다.");
        }
    }

    @ApiOperation(value = "게시글 조회", notes = "게시글를 보여줍니다.")
    @GetMapping("/{BoardId}")
    public ResponseEntity<BoardResponse> getBoardDetail(@PathVariable("BoardId") Long BoardId){
        BoardResponse response = boardService.getBoardDetail(BoardId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(value = "이미지 조회", notes = "이미지를 보여줍니다.")
    @GetMapping("/image/{BoardId}")
    public ResponseEntity<DataResponse> getImage(@PathVariable("BoardId") Long BoardId){
        DataResponse response = boardService.getResponse(BoardId, null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(value = "첨부파일 다운로드", notes = "첨부파일을 다운로드합니다.")
    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> resourceFileDownload(@PathVariable("fileId") Long fileId) {
        try {
            EntityBoardFile BoardFile = boardService.getFile(fileId);
            if (BoardFile == null) return null;
            String storedFileName = BoardFile.getStoredFileName();
            String originalFileName = BoardFile.getOriginalFileName();
            String encodedUploadFileName = UriUtils.encode(originalFileName, StandardCharsets.UTF_8);
            String contentDisposition = "attachment; filename =\"" + encodedUploadFileName + "\"";
            Resource resource = resourceLoader.getResource("file:" + storedFileName);
            File file = resource.getFile();

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,contentDisposition)	//다운 받아지는 파일 명 설정
                    .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.length()))	//파일 사이즈 설정
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM.toString())	//바이너리 데이터로 받아오기 설정
                    .body(resource);	//파일 넘기기
        } catch (Exception e ) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @ApiOperation(value = "첨부파일 다운로드", notes = "S3에 저장된 첨부파일을 다운로드합니다.")
    @GetMapping("/s3/download/{fileName}")
    public ResponseEntity<byte[]> download(@PathVariable String fileName) throws IOException {
        S3Object o = amazonS3Client.getObject(new GetObjectRequest(bucket, fileName));
        S3ObjectInputStream objectInputStream = o.getObjectContent();
        byte[] bytes = IOUtils.toByteArray(objectInputStream);

        String encodedFileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        httpHeaders.setContentLength(bytes.length);
        httpHeaders.setContentDispositionFormData("attachment", encodedFileName);

        // 파일의 바이트 배열과 설정된 HTTP 헤더를 사용하여 ResponseEntity 객체를 직접 반환합니다.
        return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
    }

}
