package com.ssafy.algonote.config.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.ssafy.algonote.exception.CustomException;
import com.ssafy.algonote.exception.ErrorCode;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AwsFileService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    public String saveFile(MultipartFile multipartFile) {
        String originalName = multipartFile.getOriginalFilename();
        String fileExtension = originalName.substring(originalName.lastIndexOf("."));
        String uniqueFilename = UUID.randomUUID().toString() + fileExtension;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        try{
            amazonS3.putObject(bucket, uniqueFilename, multipartFile.getInputStream(), metadata);
            return amazonS3.getUrl(bucket, uniqueFilename).toString();
        }catch(IOException e){
            throw new CustomException(ErrorCode.FAILED_IMAGE_UPLOAD);
        }
    }
}
