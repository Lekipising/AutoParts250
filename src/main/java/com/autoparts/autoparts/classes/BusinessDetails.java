package com.autoparts.autoparts.classes;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name = "businessdetails")
public class BusinessDetails {
    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "detailid")
	private Long detailid;

    @Column(name = "bname", nullable = false)
	private String bname;

    @Column(name = "logo", nullable = true)
	private String logo;

    @Transient
	MultipartFile logoPhoto;

    public String getBname() {
        return bname;
    }

    public Long getDetailid() {
        return detailid;
    }

    public String getLogo() {
        return logo;
    }

    public MultipartFile getLogoPhoto() {
        return logoPhoto;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public void setDetailid(Long detailid) {
        this.detailid = detailid;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public void setLogoPhoto(MultipartFile logoPhoto) {
        this.logoPhoto = logoPhoto;
    }

    public BusinessDetails(){}
}
