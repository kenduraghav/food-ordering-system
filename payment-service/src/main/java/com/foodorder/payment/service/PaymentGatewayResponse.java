package com.foodorder.payment.service;

public class PaymentGatewayResponse {

	private boolean success;
	private String transactionId;
	private String errorMessage;
	private String rawResponse;

	private PaymentGatewayResponse(boolean success, String transactionId, String errorMessage, String rawResponse) {
		this.success = success;
		this.transactionId = transactionId;
		this.errorMessage = errorMessage;
		this.rawResponse = rawResponse;
	}

	public static PaymentGatewayResponse success(String transactionId, String message, String rawResponse) {
		return new PaymentGatewayResponse(true, transactionId, null, rawResponse);
	}

	public static PaymentGatewayResponse failure(String errorMessage, String rawResponse) {
		return new PaymentGatewayResponse(false, null, errorMessage, rawResponse);
	}

	// Getters
	public boolean isSuccess() {
		return success;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public String getRawResponse() {
		return rawResponse;
	}
}
