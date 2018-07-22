package com.Data;

import java.util.Date;

public class MarketDay implements Comparable{
	//sl1d1t1c1ohgvda2m3m4t8kjf6j1j2eb4rs6s7
	private long id;	
	public long mySecId;
	public Date date;
	public float open=0;
	public float close=0;
	public float gap=0;
	public float high=0;
	public float low=0;
	public long volume=0;
	
	public float dividend=0;
	public long avgdailyvolume = 0;
	public float fiftydayMA = 0;
	public float twohundreddayMA = 0;
	public float firstyeartarget = 0;
	public float fiftytwoweekhigh = 0;
	public float fiftytwoweeklow = 0;
	public long floatsize = 0;
	public float marketcap = 0;
	public float sharesout = 0;
	public float eps = 0;
	public float bookvalue = 0;
	public float pe = 0;
	public float revenue = 0;
	public float shortratio = 0;
	
	
	public int compareTo(Object other){
		return date.compareTo((Date)other);
	}
	
	public MarketDay() {
	}

	public MarketDay(MarketDay md) {
		this.mySecId=md.mySecId;
		this.date=md.date;
		this.open=md.open;
		this.close=md.close;
		this.gap=md.gap;
		this.high=md.high;
		this.low=md.low;
		this.volume=md.volume;
	}
	
	private long getId() {
		return id;
	}

	private void setId(long id) {
		this.id = id;
	}

	public float getClose() {
		return close;
	}

	public void setClose(float close) {
		this.close = close;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public float getGap() {
		return gap;
	}

	public void setGap(float gap) {
		this.gap = gap;
	}

	public float getHigh() {
		return high;
	}

	public void setHigh(float high) {
		this.high = high;
	}

	public float getLow() {
		return low;
	}

	public void setLow(float low) {
		this.low = low;
	}

	public float getOpen() {
		return open;
	}

	public void setOpen(float open) {
		this.open = open;
	}

	public long getVolume() {
		return volume;
	}

	public void setVolume(long volume) {
		this.volume = volume;
	}

	public MarketDay(Date date, float open, float gap, float close, float high, float low, long volume) {
		this.date = date;
		this.open = open;
		this.gap = gap;
		this.close = close;
		this.high = high;
		this.low = low;
		this.volume = volume;
	}
	
	public String toString(){
		return ""+(date!=null?date.toString():"##########NULL#DATE###########")+" o:"+open+" c:"+close+" g:"+gap+" h:"+high+" l:"+low+" v:"+volume+" avgDailyVol:"+avgdailyvolume+" fiftydayMA:"+ fiftydayMA+" twohundreddayMA:"+twohundreddayMA+" firstyeartarget:"+firstyeartarget+" fiftytwoweekhigh:"+fiftytwoweekhigh+" fiftytwoweeklow:"+fiftytwoweeklow+" floatsize:"+floatsize+" marketcap:"+marketcap+" sharesout:"+sharesout+" eps:"+eps+" bookvalue:"+ bookvalue+" pe:"+pe;
	}

	public long getMySecId() {
		return mySecId;
	}

	public void setMySecId(long mySecId) {
		this.mySecId = mySecId;
	}

}
