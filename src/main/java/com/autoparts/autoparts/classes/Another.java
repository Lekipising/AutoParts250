package com.autoparts.autoparts.classes;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.NaturalId;

import lombok.Builder;

@Entity
@Table(name = "another")
public class Another {
    @Id
    @Column(name = "email")
    private String email;

    @NaturalId
    @Column(name = "conf", unique = true)
    private String conf;

    @Column(name = "sendTime")
    private LocalDateTime sendTime;

    @Builder.Default
	private Boolean enabled = false;

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getConf() {
        return conf;
    }

    public void setConf(String conf) {
        this.conf = conf;
    }

    public LocalDateTime getSendTime() {
        return sendTime;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }


    public Another(){
        this.sendTime = LocalDateTime.now();
    }

    
}

