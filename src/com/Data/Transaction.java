package com.Data;

public class Transaction {

	public String symbol;
	public Double strike;
	public int size;
	public Double price;
	public Double lastPrice;
	public String exchange;
	public Double underlyingPrice;
	public String specialType;
	public String timeStamp;
	public String market;
	public boolean isLowerLeg;
	public boolean isUpperLeg;
	public boolean isCall;

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public Double getStrike() {
		return strike;
	}

	public void setStrike(Double strike) {
		this.strike = strike;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	public Double getUnderlyingPrice() {
		return underlyingPrice;
	}

	public void setUnderlyingPrice(Double underlyingPrice) {
		this.underlyingPrice = underlyingPrice;
	}

	public Double getLastPrice() {
		return lastPrice;
	}

	public void setLastPrice(Double lastPrice) {
		this.lastPrice = lastPrice;
	}

	public String getSpecialType() {
		return specialType;
	}

	public void setSpecialType(String specialType) {
		this.specialType = specialType;
	}

	public boolean isLowerLeg() {
		return isLowerLeg;
	}

	public void setLowerLeg(boolean isLowerLeg) {
		this.isLowerLeg = isLowerLeg;
	}

	public boolean isUpperLeg() {
		return isUpperLeg;
	}

	public void setUpperLeg(boolean isUpperLeg) {
		this.isUpperLeg = isUpperLeg;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getMarket() {
		return market;
	}

	public void setMarket(String market) {
		this.market = market;
	}

	public boolean isCall() {
		return isCall;
	}

	public void setCall(boolean isCall) {
		this.isCall = isCall;
	}

	public Double getEffectivePrice() {
		if (this.isCall) {
			return strike != null ? strike + price : new Double(-1);

		} else {
			return strike != null ? strike - price : new Double(-1);
		}
	}

	public Double getTransactionCost() {
		return size * price * 100;
	}

	public Double getLatestValue() {
		return size * lastPrice * 100;
	}
	
	public String getExpiry(){
		String[] split = this.symbol.split(" ");
		return split[0]+" "+split[1]+" "+split[2];
	}
}
