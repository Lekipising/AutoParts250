package com.autoparts.autoparts.config;

import java.io.File;
import java.io.FilenameFilter;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class UploadLogs {

    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    public boolean scanForLogs() {
        return false;
    }

    public File[] finder(String dirName) {
        File dir = new File(dirName);
        return dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String filename) {
                return filename.endsWith(".gz");
            }
        });

    }

    public void delFiles(File[] files) {
        // String[] pathnames;
        // pathnames = f.list();
        for (int i = 0; i < files.length; i++) {
            File file = new File(files[0].getName());
            file.delete();
        }
    }

    public void uploadS3(File[] files) {
        for (int i = 0; i < files.length; i++) {
            File file = new File(files[0].getName());
            // AWS S3
            AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
            final AmazonS3 s3client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.US_EAST_2)
                    .build();

            s3client.putObject("autoparts250", file.getName(), file);
        }
    }

    public void dailyUpload(String dirName){
        String[] pathnames;
        File f = new File("/home/liplan/Documents/AutoParts250");
        pathnames = f.list();
        for (String pathname : pathnames) {
            System.out.println(pathname);
        }
    }

    public UploadLogs(){}
}
