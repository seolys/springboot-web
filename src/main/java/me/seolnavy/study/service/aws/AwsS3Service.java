package me.seolnavy.study.service.aws;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(rollbackFor = Exception.class)
public class AwsS3Service {

    private final AmazonS3 s3Client;

    @Value("${AWS_S3_BUCKET}")
    private String bucketName;

    public PutObjectResult uploadObject(MultipartFile multipartFile) throws IOException {
        SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");
        ObjectMetadata omd = new ObjectMetadata();
        omd.setContentType(multipartFile.getContentType());
        omd.setContentLength(multipartFile.getSize());
        omd.setHeader("filename", multipartFile.getOriginalFilename());

        // Copy file to the target location (Replacing existing file with the same name)
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName + "/" + "upload",
                                                                            multipartFile.getOriginalFilename(), // 파일명 중복방지 필요(UUID)
                                                                            multipartFile.getInputStream(), omd);
        return s3Client.putObject(putObjectRequest);
    }

    public void deleteObject(String date, String storedFileName) throws AmazonServiceException {
        s3Client.deleteObject(new DeleteObjectRequest(bucketName + "/" + date, storedFileName));
    }

    public Resource getObject(String storedFileName) throws IOException {
        S3Object o = s3Client.getObject(new GetObjectRequest(bucketName + "/" + "upload", storedFileName));
        S3ObjectInputStream objectInputStream = o.getObjectContent();
        byte[] bytes = IOUtils.toByteArray(objectInputStream);

        Resource resource = new ByteArrayResource(bytes);
        return resource;
    }

}