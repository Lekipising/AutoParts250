// Authors: Liplan Lekipising and catherine Muthoni
package com.autoparts.autoparts.controllers;

import com.autoparts.autoparts.classes.OrderProduct;
import com.autoparts.autoparts.classes.Orders;
import com.autoparts.autoparts.repository.OrderProductRepository;
import com.autoparts.autoparts.repository.OrdersRepository;
import com.autoparts.autoparts.services.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static com.autoparts.autoparts.classes.CartCarrier.cart;


@Controller
public class OrderController {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    OrdersRepository ordersRepository;

    @Autowired
    OrderProductRepository orderProductRepository;


    // get all orders
    @GetMapping(path = "/orders")
    public String getAllOrders(Model model){
        model.addAttribute("orders", ordersService.getAllOrders());
        return "orderlist";
    }

    // DELETE - delete an order
    @GetMapping("/order/delete/{orderId}")
    public String delProduct(@PathVariable("orderId") Long orderId){
        ordersService.delOrder(orderId);

        return "home";
    }

    // GET - one product
    @GetMapping(path = "/orders/{orderId}")
    public Orders getOneOrder(@PathVariable("orderId") Long orderId){
        return ordersService.getOneOrder(orderId);
    }

    // POST - add order(Proceed to Checkout)
    @RequestMapping(value="/addorder", method= RequestMethod.POST)

    public String createOrder(@ModelAttribute("orders") Orders orders, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()){
            return "cart";
        }

        ordersService.addOrder(orders);
        for (OrderProduct cartItem : cart) {
            cartItem.setOrders(orders);
            orderProductRepository.save(cartItem);
        }

        cart.clear();
        return "orderconfirm";
    }

    // Get details of a specific order
    @GetMapping(path = "/order/more/{id}")
    public String getOrderDetails(@PathVariable("id") Long id,Model model){

        model.addAttribute("orderedProducts",ordersService.getOneOrder(id).getOrderProduct());
        return "orderdetails";
    }



}


 
