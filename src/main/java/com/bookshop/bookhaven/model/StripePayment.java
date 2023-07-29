// Author		: Zay Yar Tun
// Admin No		: 2235035
// Class		: DIT/FT/2A/02
// Group		: 10
// Date			: 29.7.2023
// Description	: test stripe payment (test card - 4242 4242 4242 4242)

package com.bookshop.bookhaven.model;

import java.util.HashMap;
import java.util.Map;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;

public class StripePayment {
	private final String secretKey = "sk_test_51NYV9WEOz0583OXtWmf68ztajK3AfSyMDcAYqD5XGkbv1kGT3wdvbxAIAANxJ0BppfGBU5LnmucLrj1sJEX6e7Cr00iPH15dVY";
	
	public Charge processPayment(String token, int amount, String currency) throws StripeException {
		Stripe.apiKey = secretKey;
		
		Map<String, Object> chargeParams = new HashMap<>();
		chargeParams.put("amount", amount);
		chargeParams.put("currency", currency);
		chargeParams.put("source", token);
		
		return Charge.create(chargeParams);
	}
}