package com.example.streetfoodfinder.service;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
@Slf4j
@RequiredArgsConstructor    // final 멤버변수가 있으면 생성자 항목에 포함시킴
@Component
@Service
public class UploadPhotoService {
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile multipartFile, Long reviewId) throws IOException {
        // 업로드 날짜
        LocalDateTime uploadDateTime = LocalDateTime.now();

        //버킷에 저장되는 이미지 이름 지정
        String s3FileName = reviewId + "_" + uploadDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")) + ".jpg";

        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(multipartFile.getInputStream().available());

        amazonS3.putObject(bucket, s3FileName, multipartFile.getInputStream(), objMeta);

        return amazonS3.getUrl(bucket, s3FileName).toString();
    }
    public void delete(Long reviewId) { //0102 이미지 삭제 기능
        // S3 버킷의 객체 목록을 가져온다
        ObjectListing objectListing = amazonS3.listObjects(bucket);
        do {
            for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                String fileName = objectSummary.getKey();
                // 파일 이름이 삭제할 리뷰 ID로 시작하는지 확인
                if (fileName.startsWith(reviewId + "_")) {
                    // S3에서 해당 파일 삭제
                    amazonS3.deleteObject(bucket, fileName);
                }
            }
            objectListing = amazonS3.listNextBatchOfObjects(objectListing);
        } while (objectListing.isTruncated());
    }
}
