package com.autoparts.autoparts.config;

import java.io.File;
import java.io.FilenameFilter;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.autoparts.autoparts.classes.Account;
import com.autoparts.autoparts.classes.Another;
import com.autoparts.autoparts.services.AccountService;
import com.autoparts.autoparts.services.AnotherService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.properties")
public class UploadLogs {

    @Autowired
    AnotherService newsletterService;

    @Autowired
    AccountService accountService;

    private static final Logger logger = LoggerFactory.getLogger(UploadLogs.class);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    public void uploadS3(String files) {

        String new1 = files.substring(2);

        File file = new File(new1);

        // AWS S3
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        final AmazonS3 s3client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.US_EAST_2).build();

        s3client.putObject("autoparts250", dateTimeFormatter.format(LocalDateTime.now()), file);

    }

    public static List<String> findFiles(Path path, String fileExtension) throws IOException {

        if (!Files.isDirectory(path)) {
            throw new IllegalArgumentException("Path must be a directory!");
        }

        List<String> result;

        try (Stream<Path> walk = Files.walk(path, 1)) {
            result = walk.filter(p -> !Files.isDirectory(p)).map(p -> p.toString().toLowerCase())
                    .filter(f -> f.endsWith(fileExtension)).collect(Collectors.toList());
        }

        return result;
    }

    // @Scheduled(fixedRate = 86400000)
    // public void dailyUpload() {
    //     try {
    //         List<String> files = findFiles(Paths.get("./"), "gz");
    //         files.forEach(x -> uploadS3(x));

    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    //     // File directory = new File("./");
    //     // System.out.println(directory.getAbsolutePath());
    //     logger.info("Uploaded Logs - {}", dateTimeFormatter.format(LocalDateTime.now()));
    // }

    // @Scheduled(fixedRate = 86400000)
    // public void checkDisabled() {
    //     // find all contacts
    //     List<Another> allNews = newsletterService.getAllNewsletters();
    //     List<Account> allAccs = accountService.getAllAccounts();

    //     for (int i = 0; i < allNews.size(); i++) {
    //         try {
    //             if (!allNews.get(i).getEnabled()) {
    //                 newsletterService.deleteNewsletter(allNews.get(i).getEmail());
    //                 logger.info("Deleted news acc");
    //             }
    //         } catch (Exception e) {
    //             newsletterService.deleteNewsletter(allNews.get(i).getEmail());
    //             logger.info("Deleted news acc");
    //         }

    //     }

    //     for (int i = 0; i < allAccs.size(); i++) {
    //         if (!allAccs.get(i).getEnabled()) {
    //             accountService.delAccount(allAccs.get(i).getUsername());
    //             logger.info("Deleted user");
    //         }
    //     }

    //     logger.info("Ran DELETE not enabled");
    // }

}
