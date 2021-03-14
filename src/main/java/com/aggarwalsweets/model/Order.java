package com.aggarwalsweets.model;

import java.util.ArrayList;
import java.util.List;

public class Order {

	private int orderId;
	private BillingInfo billingInfo;
	private ContactInfo contactInfo;
	private List<LineItem> lineItems;

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public BillingInfo getBillingInfo() {
		return billingInfo;
	}

	public void setBillingInfo(BillingInfo billingInfo) {
		this.billingInfo = billingInfo;
	}

	public ContactInfo getContactInfo() {
		return contactInfo;
	}

	public void setContactInfo(ContactInfo contactInfo) {
		this.contactInfo = contactInfo;
	}

	public List<LineItem> getLineItems() {
		return lineItems;
	}

	public void setLineItems(List<LineItem> lineItems) {
		this.lineItems = lineItems;
	}

	public static class Builder {
		private int orderId;
		private List<String> productName = new ArrayList<>();
		private List<Integer> quantity = new ArrayList<>();

		public Builder orderId(int orderId) {
			this.orderId = orderId;
			return this;
		}

		public Builder productName(String productName) {
			this.productName.add(productName);
			return this;
		}

		public Builder quantity(int quantity) {
			this.quantity.add(quantity);
			return this;
		}

		public Order build() {
			validate();
			Order order = new Order();
			order.lineItems = new ArrayList<>();
			order.orderId = this.orderId;
			for (int i = 0; i < productName.size(); i++) {
				LineItem item = new LineItem(productName.get(i), quantity.get(i));
				order.lineItems.add(item);
			}
			return order;
		}
		
		private void validate() throws IllegalStateException{
			if(this.productName.size() != this.quantity.size()) {
				throw new IllegalStateException(
						"The number of products added is not equal to number of quantities added");
			}
		}
	}
}
