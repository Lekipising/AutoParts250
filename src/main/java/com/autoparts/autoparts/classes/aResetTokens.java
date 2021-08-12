// Authors: Liplan Lekipising and catherine Muthoni
package com.autoparts.autoparts.classes;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "tokens")
public class aResetTokens {

    @Id
    @Column(name = "tkn")
    private Integer tkn;

    @Column(name = "status")
    private boolean status = false;

    @Column(name = "sendTime")
    private LocalDateTime sendTime;

    public Integer getTkn() {
        return tkn;
    }

    public boolean getStatus(){
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setTkn(Integer tkn) {
        this.tkn = tkn;
    }

    public LocalDateTime getSendTime() {
        return sendTime;
    }

    public aResetTokens(){
        this.sendTime = LocalDateTime.now();
    }

}
