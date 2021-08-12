// Authors: Liplan Lekipising and catherine Muthoni
package com.autoparts.autoparts.classes;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Random;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
@Entity
@Table(name = "users")
public class Account {

    @Id
    @Column(unique = true)
    private String username;
 
    @Size(min=2, max=255)
    @Column(name = "firstName", nullable = false)
    @NotEmpty(message = "Please provide your first name")
    private String firstName;

    @Size(min=2, max=255)
    @Column(name = "secondName", nullable = false)
    @NotEmpty(message = "Please provide your second name")
    private String secondName;

    @Column(name = "phoneNumber", nullable = false)
    private String phoneNumber;

    @Column(name = "password")
    private String password;

    @Column(name = "createdDate", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "confirmation_token")
	private String confirmationToken;

    @Column(name = "lastupdate", nullable = true)
    private LocalDateTime lastUpdate;

    @Column(name = "tokendate", nullable = true)
    private LocalDateTime tokenDate;

    @Builder.Default
	private Boolean locked = false;

	@Builder.Default
	private Boolean enabled = false;

    @Column(name = "role")
    private String role = "USER";

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public void setTokenDate(LocalDateTime tokenDate) {
        this.tokenDate = tokenDate;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public LocalDateTime getTokenDate() {
        return tokenDate;
    }
    
    public void setPassword(String password){
        this.password = password;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
        
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setRole(String role){
        this.role = role;
    }

    public String getRole(){
        return role;
    }
    
    public void setSecondName(String secondName) {
        this.secondName = secondName;
        
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public void setConfirmationToken(String confirmationToken) {
        this.confirmationToken = confirmationToken;
        this.setTokenDate(LocalDateTime.now());
    }

    public String getConfirmationToken() {
        return confirmationToken;
    }

    public String generateId(){
        Random random = new Random();
        int val = random.nextInt(999999);
        return String.format("%06d", val);
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public Boolean getLocked() {
        return locked;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public Account(){
        this.createdDate = LocalDateTime.now();
    }


}


