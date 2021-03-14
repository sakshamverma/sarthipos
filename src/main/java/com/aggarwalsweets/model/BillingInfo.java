package com.aggarwalsweets.model;

import java.util.Currency;

import com.aggarwalsweets.sarthipos.Constants;

public class BillingInfo {

	private String paymentMethod;
	private Currency currency = Currency.getInstance(Constants.CANADIAN_DOLLAR);
	private float cartCost;

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public float getCartCost() {
		return cartCost;
	}

	public void setCartCost(float cartCost) {
		this.cartCost = cartCost;
	}

	@Override
	public String toString() {
		return "BillingInfo [paymentMethod=" + paymentMethod + ", currency=" + currency + ", cartCost=" + cartCost
				+ "]";
	}
}
