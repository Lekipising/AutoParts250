// Authors: Liplan Lekipising and catherine Muthoni
package com.autoparts.autoparts.controllers;
import java.util.List;
import com.autoparts.autoparts.classes.Shipping;
import com.autoparts.autoparts.repository.ShippingRepository;
import com.autoparts.autoparts.services.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/shipping")
public class ShippingController {
    @Autowired
    ShippingService shippingService;

    @Autowired
    ShippingRepository shippingRepository;

    // GET - one product
    @GetMapping(path = "{shippingId}")
    public Shipping getOneShipping(@PathVariable("shippingId") Long shippingId){
        return shippingService.getOneShipping(shippingId);
    }

    // GET - all products
    @GetMapping
    public List<Shipping> getAllShippings(){
        return shippingService.getAllShippings();
    }

    // update product details

    // POST - add product
    @PostMapping
    public void addShipping(@RequestBody Shipping shipping){
        shippingService.addShipping(shipping);
    }

    // DELETE - delete a product
    @DeleteMapping(path = "{shippingId}")
    public void delShipping(@PathVariable("shippingId") Long shippingId){
        shippingService.delShipping(shippingId);
    }
}
