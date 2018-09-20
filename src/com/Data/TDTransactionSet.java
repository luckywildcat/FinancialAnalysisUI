package com.Data;

import java.util.ArrayList;
import java.util.List;

public class TDTransactionSet {
	String name;
	List<Transaction> transactions = new ArrayList<Transaction>();
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Transaction> getTransactions() {
		return transactions;
	}
	public void setTransactions(List<Transaction> transaction) {
		this.transactions = transaction;
	}
	
	
}
