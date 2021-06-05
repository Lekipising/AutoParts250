// Authors: Liplan Lekipising and catherine Muthoni
package com.autoparts.autoparts.services;
import java.util.List;
import com.autoparts.autoparts.classes.Products;
import com.autoparts.autoparts.repository.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service 
public class ProductsService {

    @Autowired
    ProductsRepository productsRepository;

    

    // get all products
    public List<Products> getAllProducts(){
        return (List<Products>) productsRepository.findAll();
    }

    // get one product
    public Products getOneProduct(Long productId){
        return productsRepository.findById(productId).get();
    }

    // add a product
    public Products addProduct(Products products){
        return productsRepository.save(products);
    }

    // update product details
    public Products updateProduct(Long productId){
        return productsRepository.findById(productId)
                .orElseThrow(()-> new IllegalArgumentException("Invalid Student id + productId"));
    }
    
    // delete a product
    public void delProduct(Long productId){
        boolean exists = productsRepository.existsById(productId);
        if (exists){
            productsRepository.deleteById(productId);
        }
        else {
            throw new IllegalStateException("ID doesn't exist"); 
        }
    }

}
