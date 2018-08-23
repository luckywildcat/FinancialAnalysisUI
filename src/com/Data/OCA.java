package com.Data;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Option Chain Analysis object
 * 
 * @author luckywildcat
 *
 */
public class OCA {

	public String symbol;

	/**
	 * Maps Option Symbols to Expiry Dates
	 */
	public Map<String, Date> contractIDs = new HashMap<String, Date>();
	
	/**
	 * All available Trade days' data accumulated
	 */
	public TD cumulativeTradeData = new TD();
	
	/**
	 * Maps MarketDays to Trade Data captured for Underlying Symbol
	 */
	public Map<String, TD> tradeDataPerDate = new HashMap<String, TD>();
	
	public String toString(){
		String ret = "";

	      DecimalFormat df = new DecimalFormat("###,###,###.00");
  	  NumberFormat doubleFormatter = new DecimalFormat("###,###,###.00");
  	  NumberFormat doubleFormatterPCRatios = new DecimalFormat("###.0000");
  	  
	      System.out.println(" Total $ volume: $"+df.format(cumulativeTradeData.superTotalDollarCost)+" for "+cumulativeTradeData.symbol+" on "+cumulativeTradeData.tradeDateString);
	      //System.out.println(" Conditions: "+conditions);
	      //System.out.println(" Exchanges: "+exchanges);
	      System.out.println(" # active: Call Strikes: "+cumulativeTradeData.activeCallOptionStrikesCount.size()+" Put Strikes: "+cumulativeTradeData.activePutOptionStrikesCount.size());
	      //System.out.println(" # active Put Strikes: "+activePutOptionStrikesCount.size());
	      System.out.println(" # Calls traded: "+cumulativeTradeData.tradedCalls+" for $"+doubleFormatter.format(cumulativeTradeData.cumCallValue)+" Shares equiv.: "+cumulativeTradeData.tradedCalls*100);
	      System.out.println(" # Puts traded: "+cumulativeTradeData.tradedPuts+" for $"+doubleFormatter.format(cumulativeTradeData.cumPutValue)+" Shares equiv.: "+cumulativeTradeData.tradedPuts*100);
	      System.out.println(" P/C by vol.: "+doubleFormatterPCRatios.format((double)cumulativeTradeData.tradedPuts/(double)cumulativeTradeData.tradedCalls)+" P/C by $: "+doubleFormatterPCRatios.format(cumulativeTradeData.cumPutValue/cumulativeTradeData.cumCallValue));
	      System.out.println(" Max traded Call: "+cumulativeTradeData.maxTradedCall+" "+cumulativeTradeData.maxCalls+" contracts traded: Avg.: $"+doubleFormatter.format(cumulativeTradeData.avgCallsPrice)+" Rng.: $"+doubleFormatter.format(cumulativeTradeData.lowPriceCs)+"-$"+doubleFormatter.format(cumulativeTradeData.highPriceCs));
	      System.out.println(" Max traded Put: "+cumulativeTradeData.maxTradedPut+" "+cumulativeTradeData.maxPuts+" contracts traded Avg.: $"+doubleFormatter.format(cumulativeTradeData.avgPutsPrice)+" Rng.: $"+doubleFormatter.format(cumulativeTradeData.lowPricePs)+"-$"+doubleFormatter.format(cumulativeTradeData.highPricePs));
	      System.out.println(" Max DollarVolume @ $"+doubleFormatter.format(cumulativeTradeData.maxDollarVolumeCalls*100)+" Call: "+cumulativeTradeData.maxDollarVolumeCall+" "+cumulativeTradeData.maxDollarVolumeCallCount+" contracts traded: Avg.: $"+doubleFormatter.format(cumulativeTradeData.maxDVCallAvgPrice)+" Rng.: $"+doubleFormatter.format(cumulativeTradeData.lowPriceDVCs)+"-$"+doubleFormatter.format(cumulativeTradeData.highPriceDVCs));
	      System.out.println(" Max DollarVolume @ $"+doubleFormatter.format(cumulativeTradeData.maxDollarVolumePuts*100)+" Put: "+cumulativeTradeData.maxDollarVolumePut+" "+cumulativeTradeData.maxDollarVolumePutCount+" contracts traded Avg.: $"+doubleFormatter.format(cumulativeTradeData.maxDVPutAvgPrice)+" Rng.: $"+doubleFormatter.format(cumulativeTradeData.lowPriceDVPs)+"-$"+doubleFormatter.format(cumulativeTradeData.highPriceDVPs));

		
		return ret;
	}
}
