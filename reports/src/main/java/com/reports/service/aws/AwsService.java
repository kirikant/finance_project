package com.reports.service.aws;


import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.reports.exception.GlobalExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.util.Date;

@Service
public class AwsService {

    private Logger logger = LoggerFactory.getLogger(AwsService.class);

    private final AmazonS3 amazonS3;

    private String bucketName = "kirikantbucket";

    public AwsService(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    public void uploadFile(String name, File file) {
        amazonS3.putObject(bucketName, name, file);
    }

    public String downloadFile(String uuidName){

        long expTimeMillis = Instant.now().toEpochMilli();
        expTimeMillis += 1000 * 60 * 60;
        Date dateExp = new Date();
        dateExp.setTime(expTimeMillis);

        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, uuidName+".xlsx")
                        .withMethod(HttpMethod.GET)
                        .withExpiration(dateExp);
        URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
        String stringURL = url.toString();

        return stringURL;
    }

    public void checkExistence(String uuidName){
        ObjectListing objectListing = amazonS3.listObjects(bucketName);
        objectListing.getObjectSummaries().stream().filter(
                x->x.getKey().contains(uuidName)).findAny()
                .orElseThrow(()->new EntityNotFoundException("There is  no such report"));
    }
}
