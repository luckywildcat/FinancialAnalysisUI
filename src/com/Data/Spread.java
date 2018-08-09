package com.Data;

import java.util.ArrayList;
import java.util.List;

public class Spread {

	String spreadName;
	Double longStrike;
	Double shortStrike;
	int numLongContracts;
	int numShortContracts;
	List<Transaction> longTransactions = new ArrayList<Transaction>();
	List<Transaction> shortTransactions = new ArrayList<Transaction>();
	boolean isCallSpread;
	public String getSpreadName() {
		return spreadName;
	}
	public void setSpreadName(String spreadName) {
		this.spreadName = spreadName;
	}
	
	
	public boolean isCallSpread() {
		return isCallSpread;
	}
	public void setCallSpread(boolean isCallSpread) {
		this.isCallSpread = isCallSpread;
	}
	public Double getLongStrike() {
		return longStrike;
	}
	public void setLongStrike(Double longStrike) {
		this.longStrike = longStrike;
	}
	public Double getShortStrike() {
		return shortStrike;
	}
	public void setShortStrike(Double shortStrike) {
		this.shortStrike = shortStrike;
	}
	public int getNumLongContracts() {
		return numLongContracts;
	}
	public void setNumLongContracts(int numLongContracts) {
		this.numLongContracts = numLongContracts;
	}
	public int getNumShortContracts() {
		return numShortContracts;
	}
	public void setNumShortContracts(int numShortContracts) {
		this.numShortContracts = numShortContracts;
	}
	public List<Transaction> getLongTransactions() {
		return longTransactions;
	}
	public void setLongTransactions(List<Transaction> longTransactions) {
		this.longTransactions = longTransactions;
	}
	public List<Transaction> getShortTransactions() {
		return shortTransactions;
	}
	public void setShortTransactions(List<Transaction> shortTransactions) {
		this.shortTransactions = shortTransactions;
	}
	
}
