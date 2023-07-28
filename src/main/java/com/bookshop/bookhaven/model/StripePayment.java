package com.bookshop.bookhaven.model;

import java.util.HashMap;
import java.util.Map;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;

public class StripePayment {
	private final String secretKey;
	
	public StripePayment(String secretKey) {
		this.secretKey = secretKey;
	}
	
	public Charge processPayment(String token, double amount, String currency) throws StripeException {
		Stripe.apiKey = secretKey;
		
		Map<String, Object> chargeParams = new HashMap<>();
		chargeParams.put("amount", amount);
		chargeParams.put("currency", currency);
		chargeParams.put("source", token);
		
		return Charge.create(chargeParams);
	}
}