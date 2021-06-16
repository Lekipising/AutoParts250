package com.autoparts.autoparts.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.autoparts.autoparts.classes.BusinessDetails;
import com.autoparts.autoparts.services.BusinessDetailsService;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Configuration
@PropertySource("classpath:application.properties")
@Controller
public class BusinessDetailsController {
    @Autowired
    BusinessDetailsService businessDetailsService;

    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;
    
    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    @GetMapping(path = "/editlogo")
    public String showAddForm(Model model) {
        model.addAttribute("businessDetails", new BusinessDetails());
        return "editlogo";
    }

    @GetMapping("/editlogo/{id}")
    public ModelAndView showUpdateForm(@PathVariable("id") long id) {
        ModelAndView mav = new ModelAndView("editlogo");
        BusinessDetails businessDetails = businessDetailsService.getOneDetail(id);
        mav.addObject("businessDetails", businessDetails);
        return mav;
    }

    @RequestMapping(value = "/editlogo", method = RequestMethod.POST)
    public String addProductSubmission(@ModelAttribute("businessDetails") BusinessDetails businessDetails, BindingResult bindingResult,
            Model model, @RequestParam("logoPhoto") MultipartFile logoPhoto) throws IOException {

        if (bindingResult.hasErrors()) {
            return "editlogo";
        }
        // photo
        businessDetailsService.addDetail(businessDetails);
        String extension = FilenameUtils.getExtension(logoPhoto.getOriginalFilename());
        String nameP = businessDetails.getDetailid() + "." + extension;
        businessDetails.setLogo(nameP);

        // AWS S3
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);

        AmazonS3 s3client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.US_EAST_2).build();

        File file = convertMultiPartToFile(logoPhoto);

        s3client.putObject("autoparts250", nameP, file);

        businessDetailsService.addDetail(businessDetails);
        model.addAttribute("success", "Business details added successfully!");
        return "editlogo";
    }

    @RequestMapping(value = "/editlogo", method = RequestMethod.PUT)
    public String updateProductSubmission(@ModelAttribute("businessDetails") BusinessDetails businessDetails, BindingResult bindingResult,
            Model model, @RequestParam("logoPhoto") MultipartFile logoPhoto) throws IOException {

        if (bindingResult.hasErrors()) {
            return "editlogo";
        }
        // photo
        businessDetailsService.addDetail(businessDetails);
        String extension = FilenameUtils.getExtension(logoPhoto.getOriginalFilename());
        String nameP = businessDetails.getDetailid() + "." + extension;
        businessDetails.setLogo(nameP);

        // AWS S3
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);

        AmazonS3 s3client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.US_EAST_2).build();

        File file = convertMultiPartToFile(logoPhoto);

        s3client.putObject("autoparts250", nameP, file);

        businessDetailsService.addDetail(businessDetails);
        model.addAttribute("successup", "Business details updated successfully!");
        return "editlogo";
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }


}
