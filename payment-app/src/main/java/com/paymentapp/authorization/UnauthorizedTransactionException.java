package com.paymentapp.authorization;

public class UnauthorizedTransactionException extends RuntimeException{
	
	private static final long serialVersionUID = 3893010789644826012L;

	public UnauthorizedTransactionException(String message) {
		super(message);
	}

}
