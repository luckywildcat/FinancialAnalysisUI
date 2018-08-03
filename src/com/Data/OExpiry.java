package com.Data;

import java.util.ArrayList;
import java.util.List;

public class OExpiry {
	String expiry;
	int totalCallVol;
	int totalPutVol;
	double totalCallDollarVol;
	double totalPutDollarVol;
	List<OS> callStrikes = new ArrayList<OS>();
	List<OS> putStrikes = new ArrayList<OS>();
	boolean isAnalysis;
	
    public boolean isAnalysis() {
		return isAnalysis;
	}

	public void setAnalysis(boolean isAnalysis) {
		this.isAnalysis = isAnalysis;
	}

	public String getExpiry() {
		return expiry;
	}
    
	public void setExpiry(String expiry) {
		this.expiry = expiry;
	}

	public List<OS> getCallStrikes() {
		return callStrikes;
	}

	public void setCallStrikes(List<OS> callStrikes) {
		this.callStrikes = callStrikes;
	}

	public List<OS> getPutStrikes() {
		return putStrikes;
	}

	public void setPutStrikes(List<OS> putStrikes) {
		this.putStrikes = putStrikes;
	}

	public int getTotalCallVol() {
		return totalCallVol;
	}

	public void setTotalCallVol(int totalCallVol) {
		this.totalCallVol = totalCallVol;
	}

	public int getTotalPutVol() {
		return totalPutVol;
	}

	public void setTotalPutVol(int totalPutVol) {
		this.totalPutVol = totalPutVol;
	}

	public double getTotalCallDollarVol() {
		return totalCallDollarVol;
	}

	public void setTotalCallDollarVol(double totalCallDollarVol) {
		this.totalCallDollarVol = totalCallDollarVol;
	}

	public double getTotalPutDollarVol() {
		return totalPutDollarVol;
	}

	public void setTotalPutDollarVol(double totalPutDollarVol) {
		this.totalPutDollarVol = totalPutDollarVol;
	}

	
}
