// Authors: Liplan Lekipising and catherine Muthoni
package com.autoparts.autoparts.services;

import java.util.List;

import com.autoparts.autoparts.classes.OrderProduct;
import com.autoparts.autoparts.repository.OrderProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderProductService {

    @Autowired
    OrderProductRepository orderProductRepository;

    // create 
    public void addOrderProduct(OrderProduct orderProduct){
        orderProductRepository.save(orderProduct);
    }

    // view an order
    public OrderProduct getOneOrderProduct(Long orderProductId){
        return orderProductRepository.findById(orderProductId).get();
    }

    public List<OrderProduct> getAllOrderProduct(){
        return (List<OrderProduct>) orderProductRepository.findAll();
    }

    public OrderProduct updateOrderProduct(Long orderProductId){
        return orderProductRepository.findById(orderProductId).get();
    }

    // delete order
    public void delOrderProduct(Long orderProductId){
        boolean exists = orderProductRepository.existsById(orderProductId);
        if (exists){
            orderProductRepository.deleteById(orderProductId);
        }
        else {
            throw new IllegalStateException("ID doesn't exist"); 
        }
    }
    
    
}
