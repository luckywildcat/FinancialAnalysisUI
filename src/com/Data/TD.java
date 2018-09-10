package com.Data;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.Model.TDHedgesModel;
import com.Model.TDTreeModel;
import com.Utils.Formatter;

public class TD {
	public String symbol;
	public int largeOrderMinimumSize;
	
	public String tradeDateString;
	public Date tradeDate;
	
	public int tradedCalls = 0;
	public int maxCalls = 0;
	public String maxTradedCall = null;
	public Double maxDollarVolumeCalls = 0.0;
	public String maxDollarVolumeCall = null;

	public int tradedPuts = 0;
	public int maxPuts = 0;
	public String maxTradedPut = null;
	public Double maxDollarVolumePuts = 0.0;
	public String maxDollarVolumePut = null;
	
	public Double avgPutsPrice;
	public Double avgCallsPrice;
	public Double lowPricePs;
	public Double highPricePs;
	public Double lowPriceCs;
	public Double highPriceCs;
	  
	public int maxDollarVolumeCallCount;
	public int maxDollarVolumePutCount;
	public Double maxDVCallAvgPrice;
	public Double maxDVPutAvgPrice;
	public Double lowPriceDVPs;
	public Double highPriceDVPs;
	public Double lowPriceDVCs;
	public Double highPriceDVCs;
	public int totalTransactions = 0;

    public double superTotalDollarCost = 0;
    
    public Double cumCallValue = 0.0;
    public Double cumPutValue = 0.0;
    

	/**
	 * Maps Option Symbols to Strike value.
	 */
	public Map<String, Double> optionToStrike = new HashMap<String, Double>();
	
	/**
	 * Maps Option Symbols to last price value.
	 */
	public Map<String, Double> optionToLastPrice = new HashMap<String, Double>();
    
	// CALLs Info
	/**
	 * Maps Call Option Symbols to number of contracts traded.
	 */
	public Map<String, Integer> activeCallOptionStrikesCount = new HashMap<String, Integer>();

	/**
	 * Maps Call Option Symbols to lowest price traded.
	 */
	public Map<String, Double> lowPricesActiveCallOptionStrikes = new HashMap<String, Double>();
	
	/**
	 * Maps Call Option Symbols to average price traded.
	 */
	public Map<String, Double> avgPricesActiveCallOptionStrikes = new HashMap<String, Double>();

	/**
	 * Maps Call Option Symbols to highest price traded.
	 */
	public Map<String, Double> highPricesActiveCallOptionStrikes = new HashMap<String, Double>();
	
	/**
	 * Maps Call Option Symbols to total dollar volume traded.
	 */
	public Map<String, Double> dollarVolumeActiveCallOptionStrikes = new HashMap<String, Double>();
	
	// PUTs Info
	/**
	 * Maps Put Option Symbols to number of contracts traded.
	 */
	public Map<String, Integer> activePutOptionStrikesCount = new HashMap<String, Integer>();
	
	/**
	 * Maps Put Option Symbols to lowest price traded.
	 */
	public Map<String, Double> lowPricesActivePutOptionStrikes = new HashMap<String, Double>();
	
	/**
	 * Maps Put Option Symbols to average price traded.
	 */
	public Map<String, Double> avgPricesActivePutOptionStrikes = new HashMap<String, Double>();
	
	/**
	 * Maps Put Option Symbols to highest price traded.
	 */
	public Map<String, Double> highPricesActivePutOptionStrikes = new HashMap<String, Double>();
	
	/**
	 * Maps Put Option Symbols to total dollar volume traded.
	 */
	public Map<String, Double> dollarVolumeActivePutOptionStrikes = new HashMap<String, Double>();
	
	public Map<Date, Map<Double, Double>> callsAvgPrices = new HashMap<Date, Map<Double, Double>>();
	public Map<Date, Map<Double, Double>> putsAvgPrices = new HashMap<Date, Map<Double, Double>>();
	
	/**
	 * List of all encountered expiry dates.
	 */
	public List<String> expiryDates = new ArrayList<String>();

	/**
	 * Maps Call Option Symbols to expiry Dates.
	 */
	public Map<String, List<String>> callOptionsPerExpiry = new HashMap<String, List<String>>();
	
	/**
	 * Maps Put Option Symbols to expiry Dates.
	 */
	public Map<String, List<String>> putOptionsPerExpiry = new HashMap<String, List<String>>();

	/**
	 * List of all encountered hedges & large orders.
	 * Keys: COMBO, SPREAD, STRADDLE, BUYWRITE, LARGE
	 */
	public Map<String, List<Transaction>> hedgesToTransactions = new HashMap<String, List<Transaction>>();
	
	/**
	 * List of all encountered hedges & large orders.
	 * Keys: COMBO, SPREAD, STRADDLE, BUYWRITE, LARGE
	 */
	public Map<String, List<Transaction>> hedges = new HashMap<String, List<Transaction>>();

	public List<Transaction> cachedOpenSpreadLegs = new ArrayList<Transaction>();
	public List<Spread> spreads = new ArrayList<Spread>();
	
	public TD(){
		
	}
	
	public TD(String sym, String date, int pivot){
		this.symbol = sym;
		this.tradeDateString = date;
		this.largeOrderMinimumSize = pivot;
	}
	
	public String toString(){
		String ret = "";

	      DecimalFormat df = new DecimalFormat("###,###,###.00");
    	  NumberFormat doubleFormatter = new DecimalFormat("###,###,###.00");
    	  NumberFormat doubleFormatterPCRatios = new DecimalFormat("###.0000");
		
	      /*Double avgPutsPrice = avgPricesActivePutOptionStrikes.get(maxTradedPut);
	      Double avgCallsPrice = avgPricesActiveCallOptionStrikes.get(maxTradedCall);
  	  Double lowPricePs = lowPricesActivePutOptionStrikes.get(maxTradedPut);
  	  Double highPricePs = highPricesActivePutOptionStrikes.get(maxTradedPut);
  	  Double lowPriceCs = lowPricesActiveCallOptionStrikes.get(maxTradedCall);
  	  Double highPriceCs = highPricesActiveCallOptionStrikes.get(maxTradedCall);
  	  
  	  int maxDollarVolumeCallCount = activeCallOptionStrikesCount.get(maxDollarVolumeCall);
  	  int maxDollarVolumePutCount = activePutOptionStrikesCount.get(maxDollarVolumePut);
  	  Double maxDVCallAvgPrice = avgPricesActiveCallOptionStrikes.get(maxDollarVolumeCall);
  	  Double maxDVPutAvgPrice = avgPricesActivePutOptionStrikes.get(maxDollarVolumePut);
  	  Double lowPriceDVPs = lowPricesActivePutOptionStrikes.get(maxDollarVolumePut);
  	  Double highPriceDVPs = highPricesActivePutOptionStrikes.get(maxDollarVolumePut);
  	  Double lowPriceDVCs = lowPricesActiveCallOptionStrikes.get(maxDollarVolumeCall);
  	  Double highPriceDVCs = highPricesActiveCallOptionStrikes.get(maxDollarVolumeCall);*/
  	  
	      System.out.println(" Total $ volume: $"+df.format(superTotalDollarCost)+" for "+this.symbol+" on "+tradeDateString);
	      //System.out.println(" Conditions: "+conditions);
	      //System.out.println(" Exchanges: "+exchanges);
	      System.out.println(" # active: Call Strikes: "+activeCallOptionStrikesCount.size()+" Put Strikes: "+activePutOptionStrikesCount.size());
	      
	      /*for(int i = 0;i<expiryDates.size();i++){
	    	  String expiry = expiryDates.get(i);
	    	  //System.out.println(expiry);
	      }*/
	      System.out.println(" "+expiryDates.size()+" active Expiries. ["+expiryDates.get(0)+" to "+expiryDates.get((expiryDates.size()-1))+"]");
	      //System.out.println(" # active Put Strikes: "+activePutOptionStrikesCount.size());
	      System.out.println(" # Calls traded: "+tradedCalls+" for $"+doubleFormatter.format(cumCallValue)+" Shares equiv.: "+tradedCalls*100);
	      System.out.println(" # Puts traded: "+tradedPuts+" for $"+doubleFormatter.format(cumPutValue)+" Shares equiv.: "+tradedPuts*100);
	      System.out.println(" P/C by vol.: "+doubleFormatterPCRatios.format((double)tradedPuts/(double)tradedCalls)+" P/C by $: "+doubleFormatterPCRatios.format(cumPutValue/cumCallValue));
	      System.out.println(" Max traded Call: "+maxTradedCall+" "+maxCalls+" contracts traded: Avg.: $"+doubleFormatter.format(avgCallsPrice)+" Rng.: $"+doubleFormatter.format(lowPriceCs)+"-$"+doubleFormatter.format(highPriceCs));
	      System.out.println(" Max traded Put: "+maxTradedPut+" "+maxPuts+" contracts traded Avg.: $"+doubleFormatter.format(avgPutsPrice)+" Rng.: $"+doubleFormatter.format(lowPricePs)+"-$"+doubleFormatter.format(highPricePs));
	      System.out.println(" Max DollarVolume @ $"+doubleFormatter.format(maxDollarVolumeCalls*100)+" Call: "+maxDollarVolumeCall+" "+maxDollarVolumeCallCount+" contracts traded: Avg.: $"+doubleFormatter.format(maxDVCallAvgPrice)+" Rng.: $"+doubleFormatter.format(lowPriceDVCs)+"-$"+doubleFormatter.format(highPriceDVCs));
	      System.out.println(" Max DollarVolume @ $"+doubleFormatter.format(maxDollarVolumePuts*100)+" Put: "+maxDollarVolumePut+" "+maxDollarVolumePutCount+" contracts traded Avg.: $"+doubleFormatter.format(maxDVPutAvgPrice)+" Rng.: $"+doubleFormatter.format(lowPriceDVPs)+"-$"+doubleFormatter.format(highPriceDVPs));

		
		return ret;
	}
	
	public TDTreeModel generateModel(){
		Set<String> callSyms = activeCallOptionStrikesCount.keySet();
	      Set<String> putSyms = activePutOptionStrikesCount.keySet();
	      List<String> list = new ArrayList<String>(callSyms);
	      List<String> putList = new ArrayList<String>(putSyms);
	      list.addAll(putList);
	      for(int i = 0;i<list.size();i++){
	    	  String symb = list.get(i);
	    	  String delims = "[ ]+";
	    	  String[] tokens = symb.split(delims);
	    	  String expiry = tokens[2]+" "+Formatter.getMonthNumber(tokens[1])+" "+tokens[0];
	    	  if(!expiryDates.contains(expiry)){
	    		  expiryDates.add(expiry);
	    	  }
	    	  List<String> symbolsForExpiry = null;
	    	  if(symb.contains(" C")){
	    		  symbolsForExpiry = this.callOptionsPerExpiry.get(expiry);
	    	  }else{
	    		  symbolsForExpiry = this.putOptionsPerExpiry.get(expiry);
	    	  }
	    	  if(symbolsForExpiry == null){
	    		  symbolsForExpiry = new ArrayList<String>();
	    	  }
	    	  if(!symbolsForExpiry.contains(symb)){
	    		  symbolsForExpiry.add(symb);
	    	  }
	    	  if(symb.contains(" C")){
	    		  symbolsForExpiry = this.callOptionsPerExpiry.put(expiry, symbolsForExpiry);
	    	  }else{
	    		  symbolsForExpiry = this.putOptionsPerExpiry.put(expiry, symbolsForExpiry);
	    	  }
  	      //System.out.println(expiry+"  -> "+list.get(i));
	      }
	      Collections.sort(expiryDates);
		
		List<OExpiry> expiries = new ArrayList<OExpiry>(expiryDates.size());
		for(String o : expiryDates){
			//System.out.println(o);
			OExpiry ex = new OExpiry();
			
			ex.expiry = o;
			int callCounter = 0;
			int putCounter = 0;
			double callDollarCounter = 0;
			double putDollarCounter = 0;
			
			List<String> symbolsForExpiry = callOptionsPerExpiry.get(o);
			if(symbolsForExpiry!=null){

				List<OS> strikers = new ArrayList<OS>();
				Collections.sort(symbolsForExpiry);
				for(String crtSym : symbolsForExpiry){
					OS optionStrike = new OS();
					if(crtSym.contains(" C")){
						optionStrike.sym = crtSym;
						optionStrike.isCall = true;
						optionStrike.low = ""+this.lowPricesActiveCallOptionStrikes.get(crtSym);
						optionStrike.high = ""+this.highPricesActiveCallOptionStrikes.get(crtSym);
						optionStrike.last = ""+this.optionToLastPrice.get(crtSym);
						optionStrike.avg = ""+ this.avgPricesActiveCallOptionStrikes.get(crtSym);
						if(this.activeCallOptionStrikesCount.get(crtSym) != null){
							callCounter +=this.activeCallOptionStrikesCount.get(crtSym);
							callDollarCounter +=this.activeCallOptionStrikesCount.get(crtSym)*this.avgPricesActiveCallOptionStrikes.get(crtSym)*100;
							
						}
						
						optionStrike.vol = ""+this.activeCallOptionStrikesCount.get(crtSym);
						optionStrike.strike = this.optionToStrike.get(crtSym);
					}else{
						optionStrike.sym = crtSym;
						optionStrike.isCall = false;
						optionStrike.low = ""+this.lowPricesActivePutOptionStrikes.get(crtSym);
						optionStrike.high = ""+this.highPricesActivePutOptionStrikes.get(crtSym);
						optionStrike.last = ""+this.optionToLastPrice.get(crtSym);
						optionStrike.avg = ""+this.avgPricesActivePutOptionStrikes.get(crtSym);
						optionStrike.vol = ""+this.activePutOptionStrikesCount.get(crtSym);
						putCounter +=this.activePutOptionStrikesCount.get(crtSym);
						callDollarCounter +=this.activePutOptionStrikesCount.get(crtSym)*this.avgPricesActivePutOptionStrikes.get(crtSym)*100;
						optionStrike.strike = this.optionToStrike.get(crtSym);
					}
					strikers.add(optionStrike);
				}
				Collections.sort(strikers);
				ex.setCallStrikes(strikers);
			}
			symbolsForExpiry = putOptionsPerExpiry.get(o);
			if(symbolsForExpiry!=null){
				List<OS> strikers = new ArrayList<OS>();
				Collections.sort(symbolsForExpiry);
				for(String crtSym : symbolsForExpiry){
					OS optionStrike = new OS();
					if(crtSym.contains(" C")){
						optionStrike.sym = crtSym;
						optionStrike.isCall = true;
						optionStrike.low = ""+this.lowPricesActiveCallOptionStrikes.get(crtSym);
						optionStrike.high = ""+this.highPricesActiveCallOptionStrikes.get(crtSym);
						optionStrike.last = ""+this.optionToLastPrice.get(crtSym);
						optionStrike.avg = ""+this.avgPricesActiveCallOptionStrikes.get(crtSym);
						optionStrike.vol = ""+this.activeCallOptionStrikesCount.get(crtSym);
						callCounter +=this.activeCallOptionStrikesCount.get(crtSym);
						callDollarCounter +=this.activeCallOptionStrikesCount.get(crtSym)*this.avgPricesActiveCallOptionStrikes.get(crtSym)*100;
						optionStrike.strike = this.optionToStrike.get(crtSym);
					}else{
						optionStrike.sym = crtSym;
						optionStrike.isCall = false;
						optionStrike.low = ""+this.lowPricesActivePutOptionStrikes.get(crtSym);
						optionStrike.high = ""+this.highPricesActivePutOptionStrikes.get(crtSym);
						optionStrike.last = ""+this.optionToLastPrice.get(crtSym);
						optionStrike.avg = ""+this.avgPricesActivePutOptionStrikes.get(crtSym);
						optionStrike.vol = ""+this.activePutOptionStrikesCount.get(crtSym);
						putCounter +=this.activePutOptionStrikesCount.get(crtSym);
						callDollarCounter +=this.activePutOptionStrikesCount.get(crtSym)*this.avgPricesActivePutOptionStrikes.get(crtSym)*100;
						optionStrike.strike = this.optionToStrike.get(crtSym);
					}
					strikers.add(optionStrike);
				}
				Collections.sort(strikers);
				ex.setPutStrikes(strikers);
				
			}
			ex.totalCallVol = callCounter;
			ex.totalPutVol = putCounter;
			ex.totalCallDollarVol = callDollarCounter;
			ex.totalPutDollarVol = putDollarCounter;
			expiries.add(ex);
		}
		
		TDTreeModel model = new TDTreeModel(expiries, this);
		return model;
	}

	public TDHedgesModel generateHedgesModel(){
		List<TDTransactionSet> transactions = new ArrayList<TDTransactionSet>();
		for(String key : this.hedges.keySet()){
			List<Transaction> trades = this.hedges.get(key);
			for(Transaction t : trades){ // add last price
				t.lastPrice =  this.optionToLastPrice.get(t.symbol);
			}
			TDTransactionSet ts = new TDTransactionSet();
			ts.name = key;
			ts.transactions = trades;
			transactions.add(ts);
		}
		if(transactions.size() == 0){
			TDTransactionSet ts = new TDTransactionSet();
			ts.name = "No Special Hedges";
			transactions.add(ts);
		}
		TDHedgesModel model = new TDHedgesModel(transactions);
		
		return model;
	}
}
