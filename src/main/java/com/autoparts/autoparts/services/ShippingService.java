// Authors: Liplan Lekipising and catherine Muthoni
package com.autoparts.autoparts.services;

import java.util.List;

import com.autoparts.autoparts.classes.Shipping;
import com.autoparts.autoparts.repository.ShippingRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShippingService {
    @Autowired
    ShippingRepository shippingRepository;

    // get all account
    public List<Shipping> getAllShippings(){
        return (List<Shipping>) shippingRepository.findAll();
    }

    // get one product
    public Shipping getOneShipping(Long shippingId){
        return shippingRepository.findById(shippingId).get();
    }

    // add a product
    public void addShipping(Shipping shipping){
        shippingRepository.save(shipping);
    }

    // update a product - updates the count - sell

    public Shipping updateShipping(Long shippingId){
        return shippingRepository.findById(shippingId).get();
    }

    // delete a product
    public void delShipping(Long shippingId){
        boolean exists = shippingRepository.existsById(shippingId);
        if (exists){
            shippingRepository.deleteById(shippingId);
        }
        else {
            throw new IllegalStateException("ID doesn't exist"); 
        }
    }
}
