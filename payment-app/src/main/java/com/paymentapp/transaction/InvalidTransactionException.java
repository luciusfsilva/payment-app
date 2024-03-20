package com.paymentapp.transaction;

public class InvalidTransactionException extends RuntimeException{

	private static final long serialVersionUID = -2073955744126782344L;
	
	public InvalidTransactionException(String message) {
		super(message);
	}
}
