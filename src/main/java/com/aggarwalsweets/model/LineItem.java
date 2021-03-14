package com.aggarwalsweets.model;

public class LineItem {

	private String id;
	private String name;
	private String productId;

	public LineItem(String name, int quantity) {
		super();
		this.name = name;
		this.quantity = quantity;
	}

	private int quantity;
	private float price;
	private float totalPrice;

	@Override
	public String toString() {
		return "LineItem [id=" + id + ", name=" + name + ", productId=" + productId + ", quantity=" + quantity
				+ ", price=" + price + ", totalPrice=" + totalPrice + "]";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public float getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(float totalPrice) {
		this.totalPrice = totalPrice;
	}
}
