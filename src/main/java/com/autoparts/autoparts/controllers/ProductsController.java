// Authors: Liplan Lekipising and catherine Muthoni
package com.autoparts.autoparts.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.NoSuchElementException;

import javax.servlet.ServletContext;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.autoparts.autoparts.classes.OrderProduct;
import com.autoparts.autoparts.classes.Products;
import com.autoparts.autoparts.repository.ProductsRepository;
import com.autoparts.autoparts.services.BusinessDetailsService;
import com.autoparts.autoparts.services.ProductsService;

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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Configuration
@PropertySource("classpath:application.properties")
@Controller
public class ProductsController {

    @Autowired
    private ProductsService productsService;

    @Autowired
    ProductsRepository productsRepository;

    @Autowired
    ServletContext servletContext;

    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    @Autowired
    BusinessDetailsService businessDetailsService;

    // GET - all products- view all - products page
    @GetMapping("/shop")
    public String getAllProducts(Model model) {
        model.addAttribute("products", productsService.getAllProducts());
        model.addAttribute("businessDetails", businessDetailsService.getOneDetail(15L));
        return "shop";
    }

    // show add product form
    @GetMapping(path = "/newproduct")
    public String showAddForm(Model model) {
        model.addAttribute("products", new Products());
        model.addAttribute("businessDetails", businessDetailsService.getOneDetail(15L));
        return "addproduct";
    }

    // show more about product form
    @GetMapping(path = "/{id}")
    public String showMore(@PathVariable("id") Long id, Model model) {
        try {
            model.addAttribute("products", productsService.getOneProduct(id));
            model.addAttribute("orderProduct", new OrderProduct());
            model.addAttribute("businessDetails", businessDetailsService.getOneDetail(15L));
        } catch (NoSuchElementException e) {
            return "error-404";
        }

        return "productview";
    }

    // Add Product
    @RequestMapping(value = "/addproduct", method = RequestMethod.POST)
    public String saveProductSubmission(@ModelAttribute("products") @Valid Products product, Model model,
            BindingResult bindingResult, @RequestParam("studentPhoto") MultipartFile studentPhoto) throws IOException {
        model.addAttribute("businessDetails", businessDetailsService.getOneDetail(15L));
        if (bindingResult.hasErrors()) {
            model.addAttribute("businessDetails", businessDetailsService.getOneDetail(15L));
            return "addproduct";
        }

        productsService.addProduct(product);
        String extension = FilenameUtils.getExtension(studentPhoto.getOriginalFilename());
        String nameP = product.getProductId() + "." + extension;
        product.setPhoto(nameP);
        productsService.addProduct(product);
        System.out.println(nameP);
        System.out.println(extension);

        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        final AmazonS3 s3client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.US_EAST_2).build();

        File file = convertMultiPartToFile(studentPhoto);
        s3client.putObject("autoparts250", nameP, file);
        model.addAttribute("success", "Product Added Successfully!");
        file.delete();
        return "addproduct";
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    // Show update form
    @GetMapping("/edit/{id}")
    public ModelAndView showUpdateForm(@PathVariable("id") long id, Model model) {
        model.addAttribute("businessDetails", businessDetailsService.getOneDetail(15L));
        ModelAndView mav = new ModelAndView("updateproduct");
        Products product = productsService.getOneProduct(id);
        mav.addObject("product", product);
        return mav;
    }

    // UPDATE A PRODUCT
    @RequestMapping(value = "/updateproduct", method = RequestMethod.POST)
    public String updateProductSubmission(@ModelAttribute("product") Products product, BindingResult bindingResult,
            Model model, @RequestParam(value = "studentPhoto", required = false) MultipartFile studentPhoto)
            throws IOException {
        model.addAttribute("businessDetails", businessDetailsService.getOneDetail(15L));
        if (bindingResult.hasErrors()) {
            model.addAttribute("businessDetails", businessDetailsService.getOneDetail(15L));
            return "updateproduct";
        }
        // photo
        try {
            productsService.addProduct(product);
            String extension = FilenameUtils.getExtension(studentPhoto.getOriginalFilename());
            String nameP = product.getProductId() + "." + extension;
            product.setPhoto(nameP);

            // AWS S3
            AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);

            final AmazonS3 s3client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.US_EAST_2)
                    .build();

            File file = convertMultiPartToFile(studentPhoto);

            s3client.putObject("autoparts250", nameP, file);

            productsService.addProduct(product);
            model.addAttribute("success", "Product updated Successfully!");
            file.delete();
            return "updateproduct";
        } catch (FileNotFoundException e) {
            productsService.addProduct(product);
            model.addAttribute("success", "Product updated Successfully!");
            return "updateproduct";
        }

    }

    // DELETE - delete a product
    @GetMapping("/delete/{productId}")
    public String delProduct(@PathVariable("productId") Long productId, Model model, RedirectAttributes attributes) {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        final AmazonS3 s3client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.US_EAST_2)
                .build();
        s3client.deleteObject("autoparts250", productsService.getOneProduct(productId).getPhoto());
        productsService.delProduct(productId);
        attributes.addFlashAttribute("success", "Product deleted Successfully!");
        return "redirect:/shop";

    }
}