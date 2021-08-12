// Authors: Liplan Lekipising and catherine Muthoni
package com.autoparts.autoparts.classes;
import java.util.List;

import javax.persistence.*;

import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name = "products")
public class Products {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "productId")
	private Long productId;

	@OneToMany(mappedBy = "products")
	private List<OrderProduct> orderProduct;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "description", nullable = false)
	private String description;

	@Column(name = "photo", nullable = true)
	private String photo;

	@Transient
	MultipartFile studentPhoto;

	@Column(name = "price", nullable = false)
	private double price;

	@Column(name = "count", nullable = false)
	private int count;

	public String getName() {
		return this.name;
	}


	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public void setStudentPhoto(MultipartFile studentPhoto) {
		this.studentPhoto = studentPhoto;
	}

	public MultipartFile getStudentPhoto() {
		return studentPhoto;
	}


	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setName(String newName) {
		this.name = newName;
	}


	public double getPrice() {
		return this.price;
	}

	public void setPrice(double newPrice) {
		this.price = newPrice;
	}

	public String getPhoto() {
		return photo;
	}

	@Transient
	public String getPhotosImagePath() {
		if (photo == null || productId == null) return null;

		return "/user-photos/" + productId + "/" + photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public Long getProductId() {
		return this.productId;
	}

	public int getCount() {
		return this.count;
	}
	public void setCount(int newCount) {
		this.count = newCount;
	}

	public Products(){}

	public Products(String name, double price, int count,String description,String photo) {
		this.name = name;
		this.description = description;
		this.price = price;
		this.count = count;
		this.photo = photo;

	}

	@Override
	public String toString(){
		return "Name : " + this.name + "Count : " + this.count + " Price : " + this.price + " PhotoName : " + this.photo;
	}

}