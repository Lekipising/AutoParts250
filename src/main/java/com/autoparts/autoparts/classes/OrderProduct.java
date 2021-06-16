// Authors: Liplan Lekipising and catherine Muthoni
package com.autoparts.autoparts.classes;


import javax.persistence.*;


@Entity
@Table(name = "orderProduct")
public class OrderProduct {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "orderProductId")
    private long orderProductId;
    
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="ordersId", nullable = true)
    private Orders orders;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="productId", nullable = false)
    private Products products;

    @Column
    private int quantity;

    public OrderProduct(Orders orders, Products products, int quantity, long price) {
        this.orders = orders;
        this.products = products;
        this.quantity = quantity;
        //this.price = price;
    }

    public OrderProduct() {

    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    public void setProducts(Products products) {
        this.products = products;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Orders getOrders() {
        return orders;
    }


    public Products getProducts() {
        return products;
    }

    public int getQuantity() {
        return quantity;
    }

    public long getOrderProductId() {
        return orderProductId;
    }

    public void setProductsRemaining(){
         this.getProducts().setCount(this.getProducts().getCount() - this.getQuantity());
    }

    public double getTotalPrice(){
        return this.getProducts().getPrice() * this.getQuantity();
    }





}
