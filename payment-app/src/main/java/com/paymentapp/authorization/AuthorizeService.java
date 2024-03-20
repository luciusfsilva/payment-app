package com.paymentapp.authorization;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.paymentapp.transaction.Transaction;

@Service
public class AuthorizeService {
	
	private RestClient restClient;
	
	@Value("${endpoint.external.authorization}")
	private String endpointAuthorization;
	
	public AuthorizeService (RestClient.Builder builder) {
		this.restClient = builder
				.baseUrl(endpointAuthorization)
				.build();
	}
	
	public void autorize(Transaction transaction) {
		var response = restClient.get()
		.retrieve()
		.toEntity(Authorization.class);
		
		if (response.getStatusCode().isError() || !response.getBody().isAuthorized()) {
			throw new UnauthorizedTransactionException("Unauthorized transaction");
		}
	}

}
