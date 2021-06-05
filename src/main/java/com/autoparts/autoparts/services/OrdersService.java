// Authors: Liplan Lekipising and catherine Muthoni
package com.autoparts.autoparts.services;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.autoparts.autoparts.classes.OrderProduct;
import com.autoparts.autoparts.classes.Orders;
import com.autoparts.autoparts.repository.OrdersRepoI;
import com.autoparts.autoparts.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class OrdersService {

    @Autowired
    OrdersRepository ordersRepository;

    @Autowired
    OrdersRepoI ordersRepoI;

    // create order
    public Orders addOrder(Orders orders){
        orders.setOrderDate(new Date(Calendar.getInstance().getTimeInMillis()));

        return ordersRepository.save(orders);

    }

    // view an order
    public Orders getOneOrder(Long orderId){
        return ordersRepository.findById(orderId).get();
    }

    // Get all Orders
    public List<Orders> getAllOrders(){
        return (List<Orders>) ordersRepository.findAll();
    }

    // Updating an order
    public Orders updateOrder(Long orderId){
        return ordersRepository.findById(orderId).get();
    }

    // Get cart items
    public List<OrderProduct> getCartItems(Long id){
        Orders order = ordersRepository.findById(id).get();
        return order.getOrderProduct();
    }

    // Delete order
    public void delOrder(Long orderId){
            ordersRepository.deleteById(orderId);
    }
    
}
