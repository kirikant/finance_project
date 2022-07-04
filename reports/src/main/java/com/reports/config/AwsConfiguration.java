package com.reports.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsConfiguration {

    @Bean
    public AmazonS3 getS3client(){
        AWSCredentials credentials = new BasicAWSCredentials(
                "AKIATAA3GGJMPJCNDUH4",
                "QgsM+vQCASqgeUblCE6Nz8Y5D8Dnd7UtXpiAd1Mz"
        );

        AmazonS3 s3client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion("eu-north-1")
                .build();
        return  s3client;
    }


}
