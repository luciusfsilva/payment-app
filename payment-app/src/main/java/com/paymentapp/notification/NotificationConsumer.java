package com.paymentapp.notification;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.paymentapp.transaction.Transaction;

@Service
public class NotificationConsumer {
	
	private final RestClient restClient;
	
	@Value("${endpoint.external.notification}")
	private String endepointNotication;

	public NotificationConsumer(RestClient.Builder builder) {
		this.restClient = builder
				.baseUrl(endepointNotication)
				.build();
	}
	
	@KafkaListener(topics = "transaction-notification", groupId = "payment-app")
	public void receiveNotification(Transaction transaction) {
		var response = restClient.get()
				.retrieve()
				.toEntity(Notification.class);
		
		if (response.getStatusCode().isError() || !response.getBody().message()) {
			throw new NotificationException("Error sending notification");
		}
	}

}
