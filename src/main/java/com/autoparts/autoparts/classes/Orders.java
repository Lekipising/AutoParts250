// Authors: Liplan Lekipising and catherine Muthoni
package com.autoparts.autoparts.classes;
import java.util.*;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Orders")
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "orderId")
	private Long orderId;

    @Column(name = "orderDate", nullable = false)
    Date orderDate;

    @Column(name = "egift")
    private boolean egift = false;

    @OneToMany(mappedBy = "orders")
    private List<OrderProduct> orderProduct;

    @OneToOne
    @JoinColumn(name="email", nullable = true)
    private Account account;

    public Orders(){}

    public Orders(Date orderDate, boolean egift, List<OrderProduct> orderProduct, Account account) {

        this.orderDate = orderDate;
        this.egift = egift;
        this.orderProduct = orderProduct;
        this.account = account;

    }

    public boolean isEgift() {
        return egift;
    }

    public List<OrderProduct> getOrderProduct() {
        return orderProduct;
    }

    public void setOrderProduct(List<OrderProduct> orderProduct) {
        this.orderProduct = orderProduct;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setProduct(List<OrderProduct> orderProduct) {
        this.orderProduct = orderProduct;
    }

    public void setAccount(Account account){
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }

    public List<OrderProduct> getProduct() {
        return orderProduct;
    }

    public Date getOrderDate() {
        return orderDate;
    }


    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }


    public void setEgift(boolean egift) {
        this.egift = egift;
    }

    public boolean getEgift(){
        return egift;
    }


}
