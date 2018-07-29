package com.Data;

public class OS implements Comparable{
	String sym;
	String low;
	String high;
	String last;
	String vol;
	String avg;
	Double strike;
	boolean isCall;
	
	public String getSym() {
		return sym;
	}
	public void setSym(String sym) {
		this.sym = sym;
	}
	public String getLow() {
		return low;
	}
	public void setLow(String low) {
		this.low = low;
	}
	public String getHigh() {
		return high;
	}
	public void setHigh(String high) {
		this.high = high;
	}
	public String getVol() {
		return vol;
	}
	public void setVol(String vol) {
		this.vol = vol;
	}
	public String getAvg() {
		return avg;
	}
	public void setAvg(String avg) {
		this.avg = avg;
	}
	@Override
	public int compareTo(Object o) {
		OS other = (OS) o;
		if(this.strike==null) return 0;
		else if(other == null || other.strike == null) return 1;
		return (this.strike.doubleValue() > other.strike.doubleValue()) ? 1 : 0 ;
	}
	public String getLast() {
		return last;
	}
	public void setLast(String last) {
		this.last = last;
	}
	public Double getStrike() {
		return strike;
	}
	public void setStrike(Double strike) {
		this.strike = strike;
	}
	public boolean isCall() {
		return isCall;
	}
	public void setCall(boolean isCall) {
		this.isCall = isCall;
	}
	
}
