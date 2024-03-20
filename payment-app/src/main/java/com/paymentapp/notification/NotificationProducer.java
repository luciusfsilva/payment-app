package com.paymentapp.notification;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.paymentapp.transaction.Transaction;

@Service
public class NotificationProducer {
	
	private KafkaTemplate<String, Transaction> kafkaTemplate;
	
	public NotificationProducer(KafkaTemplate<String, Transaction> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}
	
	public void sendNotification(Transaction transaction) {
		kafkaTemplate.send("transaction-notification", transaction);
	}

}
