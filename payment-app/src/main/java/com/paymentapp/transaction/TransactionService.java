package com.paymentapp.transaction;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paymentapp.authorization.AuthorizeService;
import com.paymentapp.notification.NotificationService;
import com.paymentapp.wallet.Wallet;
import com.paymentapp.wallet.WalletRepository;
import com.paymentapp.wallet.WalletType;

@Service
public class TransactionService {
	
	private final TransactionRepository trepository;
	private final WalletRepository wrepository;
	private final AuthorizeService aservice;
	private final NotificationService nservice;
	
	public TransactionService(TransactionRepository trepository, WalletRepository wrepository,
			AuthorizeService aservice, NotificationService nservice) {
		this.trepository = trepository;
		this.wrepository = wrepository;
		this.aservice = aservice;
		this.nservice = nservice;
	}
	
	@Transactional
	public Transaction create(Transaction transaction) {
		//1 - Validar
		this.validate(transaction);
		
		//2 - Criar a transação
		var newTransaction = trepository.save(transaction);
		
		//3 - debitar da carteira
		var walletPayer = wrepository.findById(transaction.payer()).get();
		var walletPayee = wrepository.findById(transaction.payee()).get();
		
		wrepository.save(walletPayer.debit(transaction.value()));
		wrepository.save(walletPayee.credit(transaction.value()));
		
		//4 - chamar serviços externos
		aservice.autorize(transaction);
		
		//5 - notificação
		nservice.notify(transaction);
		
		return newTransaction;
		
	}

	/**
	 * 
	 * @param transaction
	 * 
	 * - the payer has a common wallet
	 * - the payer has enough balance
	 * - the payer is not the payee
	 * 
	 */
	private void validate(Transaction transaction) {
		wrepository.findById(transaction.payee())
			.map(payee -> wrepository.findById(transaction.payer())
				.map(payer -> isTransactionValid(transaction, payer) ? transaction : null)
					.orElseThrow(() -> new InvalidTransactionException("Invalid transaction - %s".formatted(transaction))))
			.orElseThrow(() -> new InvalidTransactionException("Invalid transaction - %s".formatted(transaction)));
	}
	
	private boolean isTransactionValid(Transaction transaction, Wallet payer) {
		return payer.type() == WalletType.COMUM.getValue() && 
				payer.balance().compareTo(transaction.value()) >= 0 && 
				!payer.id().equals(transaction.payee());
	}
	
	public List<Transaction> list() {
		return trepository.findAll();
	}

}
