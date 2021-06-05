// Authors: Liplan Lekipising and catherine Muthoni
package com.autoparts.autoparts.classes;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table(name = "shippings")
public class Shipping {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "shippingId")
	private Long shippingId;

    @Column(name = "status", nullable = false)
    private boolean status;

    @Column(name = "shipmentStage", nullable = false)
    String shipmentStage;

    @Column(name = "address", nullable = false)
    String address;


    @Column(name = "shippingDate", nullable = false)
    LocalDate shippingDate;

    @Column(name = "shippingDays", nullable = false)
    int shippingDays;

    @Column(name = "ArrivalDate", nullable = false)
    LocalDate ArrivalDate;

    @OneToOne
    @JoinColumn(name = "email", nullable = false)
    private Account account;

    @OneToOne
    @JoinColumn(name="orderId", nullable = false)
    private Orders orders;


    Shipping(Account account, Orders orders, String address, int shippingDays){
        shippingDate = LocalDate.now();
        this.address = address;
        ArrivalDate = shippingDate.plusDays(shippingDays);
        this.shippingDays = shippingDays;
        this.account = account;
        this.orders = orders;
    }

    public String getShipmentStage(){
        return shipmentStage;
    }

    public void setShipmentStage(String shipmentStage){
        this.shipmentStage = shipmentStage;
    }

    public void setEmail(Account account) {
        this.account = account;
    }

    public Account getEmail() {
        return account;
    }


    void cancelShipment(){
        this.status = true;
    }

    public void setStatus(boolean status){
        this.status = status;
    }
    public boolean getStatus(){
        return status;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setArrivalDate(LocalDate arrivalDate) {
        ArrivalDate = arrivalDate;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    public void setShippingDate(LocalDate shippingDate) {
        this.shippingDate = shippingDate;
    }

    public void setShippingDays(int shippingDays) {
        this.shippingDays = shippingDays;
    }

    public String getAddress() {
        return address;
    }

    public LocalDate getArrivalDate() {
        return ArrivalDate;
    }

    public Orders getOrders() {
        return orders;
    }

    public LocalDate getShippingDate() {
        return shippingDate;
    }

    public int getShippingDays() {
        return shippingDays;
    }

    public Long getShippingId() {
        return shippingId;
    }

    public Shipping(){
        shippingDate = LocalDate.now();
        ArrivalDate = shippingDate.plusDays(this.shippingDays);
        
    }
}
