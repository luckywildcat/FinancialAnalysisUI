package com.Parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.Data.OCA;
import com.Data.Spread;
import com.Data.TD;
import com.Data.Transaction;

import jcifs.smb.SmbException;

public class ToSOptionScan {

	public static String SPECIAL_LARGE = "Large";
	public static String SPECIAL_COMBO = "Combo";
	public static String SPECIAL_SPREAD = "Spread";
	public static String SPECIAL_STRADDLE = "Straddle";
	public static String SPECIAL_BUYWRITE = "BuyWrite";

	public static String LOCAL_SERVER_IP = "192.168.0.189";
	private String user = "info@luckywildcat.com";
	private String pw = "!letsgo777";

	public ArrayList<String> exchanges = new ArrayList<String>();
	public ArrayList<String> conditions = new ArrayList<String>();
	public String[] equity_symbols;
	public String[] dates;
	// public ArrayList<String> activeOptionStrikes = new ArrayList<String>();

	public static int numWksToShowOut = 28;
	public static String targetSymbol = "AAPL";
	public static String targetDate = null;// "09_28_16"; // ex. 08_05_16
	public static boolean fullScan = true;
	public static boolean activeScan = true;

	public ToSOptionScan() {
		// smb://compname/Users/info/Documents/watchlist.txt
		String path = "/Users/luckywildcat/Documents/workspace/HH/data//watchlist.txt";
		ArrayList<String> syms = new ArrayList<String>();
		syms.add("Select");
		try {
			// NtlmPasswordAuthentication auth = new
			// NtlmPasswordAuthentication("", user, pw);
			FileReader fileR = new FileReader(path);
			// try (BufferedReader reader = new BufferedReader(new
			// InputStreamReader(smbFile))) {
			BufferedReader br = new BufferedReader(fileR);

			String line = br.readLine();
			if (line != null) {
				syms.add(line.trim());
			}
			while (line != null) {
				line = br.readLine();
				if (line != null) {
					syms.add(line.trim());
				}
			}
			// }
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (SmbException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		equity_symbols = new String[syms.size()];
		for (int count = 0; count < syms.size(); count++) {
			equity_symbols[count] = syms.get(count);
		}

		path = "/Users/luckywildcat/Documents/workspace/HH/data/";// "smb://"+LOCAL_SERVER_IP+"/Users/info/Documents/workspace_hh/HH/data/";

		ArrayList<String> dats = new ArrayList<String>();
		try {
			// NtlmPasswordAuthentication auth = new
			// NtlmPasswordAuthentication("", user, pw);
			// SmbFile smbFile = new SmbFile(path, auth);
			File file = new File(path);
			String a[] = file.list();
			for (int i = 0; i < a.length; i++) {
				//
				if (a[i] != null && !a[i].equals(".DS_Store") && !a[i].equals("ACTIVE.txt")
						&& !a[i].equals("desktop.ini") && !a[i].equals("watchlist.txt")) {
					// System.out.println(a[i]);
					dats.add(a[i]);
				}
			}

		} catch (Exception e) {

		}

		dates = new String[dats.size()];
		int i = 0;
		for (int count = dats.size() - 1; count >= 0; count--) {
			dates[i++] = dats.get(count);
		}
	}

	public static void main(String[] args) {

		ToSOptionScan scanner = new ToSOptionScan();

		if (activeScan) {
			// scanner.scanActive();
		} else if (fullScan && targetSymbol == null) {
			// scanner.scanActive();
		} else if (fullScan && targetSymbol != null) {
			scanner.scanSymbol(targetSymbol);
		}
		// option | QTY | Price | Exchange | Market | Delta | Implied Volatility
		// | Underlying | Condition

	}

	public TD scan(String ssymbol, String[] dates, int largerSizeMin) {
		// smb://compname/Users/info/Documents/workspace_hh/HH/data/2017_02_03/AAPL/AAPL_02_03_2017.txt
		// smb://compname/Users/info/Documents/workspace_hh/HH/data/2018_03_08/AAPL/AAPL_03_08_2018.txt
		TD tradeData = new TD(ssymbol, (new Date()).toString(), largerSizeMin);
		for (String crtS : dates) {
			String crtPath = "/Users/luckywildcat/Documents/workspace/HH/data/" + crtS + "/" + ssymbol + "/";// +ssymbol+"_"+crtS+".txt";//"+ssymbol+"_"+crtS+".txt";;//"smb://"+LOCAL_SERVER_IP+"/Users/info/Documents/workspace_hh/HH/data/"+crtS+"/"+ssymbol+"/";//"+ssymbol+"_"+crtS+".txt";
			String filePath = getFilePathFromDir(crtPath);
			System.out.println(filePath);
			scanPath(tradeData, filePath);
		}

		return tradeData;
	}

	public void scanPath(TD tradeData, String path) {
		// NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication("",
		// user, pw);
		try {
			File file = new File(path);
			// BufferedReader br = new BufferedReader(new
			// InputStreamReader(file)));
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

			String stringRead = "";// br.readLine();
			// System.out.println(""+stringRead);

			int counter = 0;
			boolean isCall;
			String tradeDate = null;
			Date strikeDate = null;
			String instr = "";
			String size = "";
			int sSize = 0;
			String price = "";
			Double pPrice = 0.0;
			// double superTotalDollarCost = 0;
			// Double cumCallValue = 0.0;
			// Double cumPutValue = 0.0;
			double cost = 0;
			String exchange = "";
			String market = "";
			String delta = "";
			String impliedVolatility = "";
			String underlyingPrice = "";
			String condition = null;
			String type = null;
			// int totalTransactions = 0;
			DecimalFormat df = new DecimalFormat("###,###,###.00");
			Double currentLowPriceCallStrike;
			Double currentAvgPriceCallStrike;
			Double currentHighPriceCallStrike;
			Double currentDollarVolumeCallStrike;
			Integer currentNumTradedCallStrikesCount;

			Integer currentNumTradedPutStrikesCount;
			Double currentLowPricePutStrike;
			Double currentAvgPricePutStrike;
			Double currentHighPricePutStrike;
			Double currentDollarVolumePutStrike;

			long startTime = System.nanoTime();
			Date scanTime = new Date();
			stringRead = br.readLine();

			Format formatter1 = new SimpleDateFormat("dd MMM yy");// yyyy-MM-dd");
			Format formatter2 = new SimpleDateFormat("dd MMM yy");
			Format formatter3 = new SimpleDateFormat("MMMdd yy");
			NumberFormat doubleFormatter = new DecimalFormat("###,###,###.00");
			NumberFormat doubleFormatterPCRatios = new DecimalFormat("###.0000");
			//Spread currentOpenSpread = null; 

			outer: while (stringRead != null) {

				// System.out.println(""+stringRead);
				// System.out.println(stringRead);

				StringTokenizer st = new StringTokenizer(stringRead, "\t");
				int tokenCount = st.countTokens();
				tradeDate = st.nextToken();
				instr = st.nextToken();// 26 AUG 16 110 C
				StringTokenizer stDate = new StringTokenizer(instr, " ");
				String strikeD = null;
				try {
					if (targetSymbol.equalsIgnoreCase("_ES")) {
						String monthToken = stDate.nextToken();
						if (monthToken.length() > 3) {
							String weekToken = "" + monthToken.charAt(3);
							String actualMonthToken = "" + monthToken.substring(0, 3);// first
																						// three
																						// characters
																						// as
																						// in
																						// JAN
																						// or
																						// FEB
							strikeD = weekToken + " " + actualMonthToken + " " + stDate.nextToken();
						} else {
							strikeD = "1 " + monthToken + " " + stDate.nextToken();
						}
					} else {
						strikeD = stDate.nextToken() + " " + stDate.nextToken() + " " + stDate.nextToken();
					}

				} catch (Exception e) {
					stringRead = br.readLine();
					condition = null;
					exchange = null;
					continue;
				}
				Date date = new Date();
				String strikePrice = stDate.nextToken();
				Double strikePriceDV = Double.parseDouble(strikePrice);
				try {
					date = (Date) ((DateFormat) formatter1).parse(strikeD);
					// yyyy-MM-dd");
					String s = formatter2.format(date);
					// System.out.println(s);
				} catch (ParseException e) {
					try {

						date = (Date) ((DateFormat) formatter3).parse(strikeD);
						// yyyy-MM-dd");
						String s = formatter2.format(date);
					} catch (ParseException ee) {
						e.printStackTrace();
					}

				}
				String keyStart = ((formatter2.format(date)).toString().toUpperCase() + " " + strikePrice)
						.toUpperCase();

				// DateFormatter datef = DateFormatter(DateFormat());
				size = st.nextToken();
				size = size.replace(",", "");// remove 1,912 comma -> 1912
				sSize = Integer.parseInt(size);
				price = st.nextToken();
				pPrice = (Double) Double.parseDouble(price);
				cost = sSize * pPrice * 100;
				tradeData.superTotalDollarCost += cost;
				tradeData.totalTransactions++;
				exchange = st.nextToken();
				currentLowPriceCallStrike = null;
				currentAvgPriceCallStrike = null;
				currentHighPriceCallStrike = null;
				currentNumTradedCallStrikesCount = null;
				currentNumTradedPutStrikesCount = null;
				currentAvgPricePutStrike = null;

				Transaction transaction = new Transaction();
				transaction.setTimeStamp(tradeDate);
				Double str = null;
				if (instr.endsWith("C")) {// calls
					tradeData.cumCallValue += cost;
					isCall = true;
					String instrC = keyStart + " C";
					str = tradeData.optionToStrike.get(instrC);
					if (str == null) {
						tradeData.optionToStrike.put(instrC, strikePriceDV);
					}
					transaction.setSymbol(instrC);
					transaction.isCall = true;
					tradeData.optionToLastPrice.put(instrC, pPrice);
					if (tradeData.activeCallOptionStrikesCount.containsKey(instrC)) {
						currentNumTradedCallStrikesCount = tradeData.activeCallOptionStrikesCount.get(instrC);
						currentLowPriceCallStrike = tradeData.lowPricesActiveCallOptionStrikes.get(instrC);
						currentAvgPriceCallStrike = tradeData.avgPricesActiveCallOptionStrikes.get(instrC);
						currentHighPriceCallStrike = tradeData.highPricesActiveCallOptionStrikes.get(instrC);
						currentDollarVolumeCallStrike = tradeData.dollarVolumeActiveCallOptionStrikes.get(instrC);
						currentDollarVolumeCallStrike += (pPrice * sSize);
						if (currentLowPriceCallStrike == null || currentLowPriceCallStrike > pPrice) {
							currentLowPriceCallStrike = new Double(pPrice);
							tradeData.lowPricesActiveCallOptionStrikes.put(instrC, currentLowPriceCallStrike);
						}
						if (currentHighPriceCallStrike == null || currentHighPriceCallStrike < pPrice) {
							currentHighPriceCallStrike = new Double(pPrice);
							tradeData.highPricesActiveCallOptionStrikes.put(instrC, currentHighPriceCallStrike);
						}
						currentAvgPriceCallStrike = ((currentAvgPriceCallStrike * currentNumTradedCallStrikesCount)
								+ (pPrice * sSize)) / (currentNumTradedCallStrikesCount + sSize);
						currentNumTradedCallStrikesCount += sSize;
						if (currentNumTradedCallStrikesCount > tradeData.maxCalls) {
							tradeData.maxCalls = currentNumTradedCallStrikesCount;
							tradeData.maxTradedCall = instrC;
						}
						tradeData.activeCallOptionStrikesCount.put(instrC,
								new Integer(currentNumTradedCallStrikesCount));
						tradeData.avgPricesActiveCallOptionStrikes.put(instrC, new Double(currentAvgPriceCallStrike));
						tradeData.dollarVolumeActiveCallOptionStrikes.put(instrC, currentDollarVolumeCallStrike);
					} else {
						currentDollarVolumeCallStrike = (pPrice * sSize);
						tradeData.dollarVolumeActiveCallOptionStrikes.put(instrC, currentDollarVolumeCallStrike);
						currentNumTradedCallStrikesCount = sSize;
						currentAvgPriceCallStrike = new Double(pPrice);
						tradeData.activeCallOptionStrikesCount.put(instrC, new Integer(sSize));
						tradeData.avgPricesActiveCallOptionStrikes.put(instrC, new Double(pPrice));

						currentLowPriceCallStrike = tradeData.lowPricesActiveCallOptionStrikes.get(instrC);
						currentHighPriceCallStrike = tradeData.highPricesActiveCallOptionStrikes.get(instrC);
						if (currentLowPriceCallStrike == null || currentLowPriceCallStrike > pPrice) {
							currentLowPriceCallStrike = new Double(pPrice);
							tradeData.lowPricesActiveCallOptionStrikes.put(instrC, currentLowPriceCallStrike);
						}
						if (currentHighPriceCallStrike == null || currentHighPriceCallStrike < pPrice) {
							currentHighPriceCallStrike = new Double(pPrice);
							tradeData.highPricesActiveCallOptionStrikes.put(instrC, currentHighPriceCallStrike);
						}
					}
					if (tradeData.maxDollarVolumeCalls < currentDollarVolumeCallStrike) {
						tradeData.maxDollarVolumeCalls = currentDollarVolumeCallStrike;
						tradeData.maxDollarVolumeCall = instrC;
					}
					tradeData.tradedCalls += sSize;
				} else {// puts
					tradeData.cumPutValue += cost;
					isCall = false;
					String instrP = keyStart + " P";
					transaction.setSymbol(instrP);
					str = tradeData.optionToStrike.get(instrP);
					if (str == null) {
						tradeData.optionToStrike.put(instrP, strikePriceDV);
					}
					transaction.isCall = false;
					tradeData.optionToLastPrice.put(instrP, pPrice);
					if (tradeData.activePutOptionStrikesCount.containsKey(instrP)) {
						currentDollarVolumePutStrike = tradeData.dollarVolumeActivePutOptionStrikes.get(instrP)
								+ (pPrice * sSize);
						currentNumTradedPutStrikesCount = tradeData.activePutOptionStrikesCount.get(instrP);
						currentLowPricePutStrike = tradeData.lowPricesActivePutOptionStrikes.get(instrP);
						currentAvgPricePutStrike = tradeData.avgPricesActivePutOptionStrikes.get(instrP);
						currentHighPricePutStrike = tradeData.highPricesActivePutOptionStrikes.get(instrP);

						if (currentLowPricePutStrike == null || currentLowPricePutStrike > pPrice) {
							currentLowPricePutStrike = new Double(pPrice);
							tradeData.lowPricesActivePutOptionStrikes.put(instrP, currentLowPricePutStrike);
						}
						if (currentHighPricePutStrike == null || currentHighPricePutStrike < pPrice) {
							currentHighPricePutStrike = new Double(pPrice);
							tradeData.highPricesActivePutOptionStrikes.put(instrP, currentHighPricePutStrike);
						}

						currentNumTradedPutStrikesCount += sSize;
						currentAvgPricePutStrike = ((currentAvgPricePutStrike * currentNumTradedPutStrikesCount)
								+ (pPrice * sSize)) / (currentNumTradedPutStrikesCount + sSize);// (currentAvgPricePutStrike
																								// +
																								// pPrice)/currentNumTradedPutStrikesCount;
						if (currentNumTradedPutStrikesCount > tradeData.maxPuts) {
							tradeData.maxPuts = currentNumTradedPutStrikesCount;
							tradeData.maxTradedPut = instrP;
						}
						tradeData.activePutOptionStrikesCount.put(instrP, new Integer(currentNumTradedPutStrikesCount));
						tradeData.avgPricesActivePutOptionStrikes.put(instrP, new Double(currentAvgPricePutStrike));
						tradeData.dollarVolumeActivePutOptionStrikes.put(instrP, currentDollarVolumePutStrike);
					} else {
						currentDollarVolumePutStrike = (pPrice * sSize);
						tradeData.dollarVolumeActivePutOptionStrikes.put(instrP, currentDollarVolumePutStrike);
						currentNumTradedPutStrikesCount = sSize;
						currentAvgPricePutStrike = new Double(pPrice);
						tradeData.activePutOptionStrikesCount.put(instrP, new Integer(sSize));
						tradeData.avgPricesActivePutOptionStrikes.put(instrP, new Double(pPrice));

						currentLowPricePutStrike = tradeData.lowPricesActivePutOptionStrikes.get(instrP);
						currentHighPricePutStrike = tradeData.highPricesActivePutOptionStrikes.get(instrP);

						if (currentLowPricePutStrike == null || currentLowPricePutStrike > pPrice) {
							currentLowPricePutStrike = new Double(pPrice);
							tradeData.lowPricesActivePutOptionStrikes.put(instrP, currentLowPricePutStrike);
						}
						if (currentHighPricePutStrike == null || currentHighPricePutStrike < pPrice) {
							currentHighPricePutStrike = new Double(pPrice);
							tradeData.highPricesActivePutOptionStrikes.put(instrP, currentHighPricePutStrike);
						}
					}
					if (tradeData.maxDollarVolumePuts < currentDollarVolumePutStrike) {
						tradeData.maxDollarVolumePuts = currentDollarVolumePutStrike;
						tradeData.maxDollarVolumePut = instrP;
					}
					tradeData.tradedPuts += sSize;
				}
				if (!exchanges.contains(exchange)) {
					exchanges.add(exchange);
				} // 14:59:55 6 APR 18 167.5 P 1 .36 CBOE .34x.37 -.15 35.17%
					// 171.94 Spread
				market = st.nextToken();
				delta = st.nextToken();
				impliedVolatility = st.nextToken();
				underlyingPrice = st.nextToken();
				transaction.exchange = exchange;
				transaction.strike = strikePriceDV;
				transaction.price = pPrice;
				transaction.size = sSize;
				transaction.market = market;
				transaction.underlyingPrice = Double.parseDouble(underlyingPrice);
				;
				if (sSize >= tradeData.largeOrderMinimumSize) {
					List<Transaction> orderList = null == tradeData.hedges.get(SPECIAL_LARGE)
							? new ArrayList<Transaction>() : tradeData.hedges.get(SPECIAL_LARGE);
					orderList.add(transaction);
					tradeData.hedges.put(SPECIAL_LARGE, orderList);
				}
				if (tokenCount >= 10) {
					// System.out.println("token: "+stringRead);
					if (st.hasMoreTokens()) {
						condition = st.nextToken();
						if (condition != null && !conditions.contains(condition)) {
							conditions.add(condition);
						}
						if (condition != null) {
							if (condition.equals(SPECIAL_SPREAD)) {
								List<Transaction> orderList = null == tradeData.hedges.get(SPECIAL_SPREAD)
										? new ArrayList<Transaction>() : tradeData.hedges.get(SPECIAL_SPREAD);
								orderList.add(transaction);
								tradeData.hedges.put(SPECIAL_SPREAD, orderList);
								transaction.specialType = SPECIAL_SPREAD;
								//if(tradeData.cachedOpenSpreadLegs.size()==0){ // just cache the transaction
									tradeData.cachedOpenSpreadLegs.add(transaction);
								//}else{
								//	System.out.println("SPREAD INTEGRITY ERROR: FOUND CACHED TRANSACTIONS!");
								//}
								Spread currentOpenSpread = processSpreadTransaction(tradeData.cachedOpenSpreadLegs);
								if(currentOpenSpread!=null){
									tradeData.spreads.add(currentOpenSpread);
									tradeData.cachedOpenSpreadLegs.clear();
								}
							} else if (condition.equals(SPECIAL_STRADDLE)) {
								List<Transaction> orderList = null == tradeData.hedges.get(SPECIAL_STRADDLE)
										? new ArrayList<Transaction>() : tradeData.hedges.get(SPECIAL_STRADDLE);
								orderList.add(transaction);
								tradeData.hedges.put(SPECIAL_STRADDLE, orderList);
								transaction.specialType = SPECIAL_STRADDLE;
							} else if (condition.equals(SPECIAL_BUYWRITE)) {
								List<Transaction> orderList = null == tradeData.hedges.get(SPECIAL_BUYWRITE)
										? new ArrayList<Transaction>() : tradeData.hedges.get(SPECIAL_BUYWRITE);
								orderList.add(transaction);
								tradeData.hedges.put(SPECIAL_BUYWRITE, orderList);
								transaction.specialType = SPECIAL_BUYWRITE;
							} else if (condition.equals(SPECIAL_COMBO)) {
								List<Transaction> orderList = null == tradeData.hedges.get(SPECIAL_COMBO)
										? new ArrayList<Transaction>() : tradeData.hedges.get(SPECIAL_COMBO);
								orderList.add(transaction);
								tradeData.hedges.put(SPECIAL_COMBO, orderList);
								transaction.specialType = SPECIAL_COMBO;
							}

						}
					}
				}

				// read the next line
				stringRead = br.readLine();
				condition = null;
				exchange = null;
			}
			br.close();

			System.out.println();
			/// System.out.println(" +++ AVERAGE PAID FOR PUTS +++");
			ArrayList<Date> sortedDatesPuts = new ArrayList<Date>();
			for (String putOption : tradeData.avgPricesActivePutOptionStrikes.keySet()) {
				Double crt = tradeData.avgPricesActivePutOptionStrikes.get(putOption);

				StringTokenizer stDate = new StringTokenizer(putOption, " ");
				String strikeD = stDate.nextToken() + " " + stDate.nextToken() + " " + stDate.nextToken();
				Date date = new Date();
				String strikePrice = stDate.nextToken();
				Double strikePriceDV = Double.parseDouble(strikePrice);
				try {
					date = (Date) ((DateFormat) formatter1).parse(strikeD);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				Map<Double, Double> strikesForDate = tradeData.putsAvgPrices.get(date);
				if (strikesForDate != null) {
					strikesForDate.put(strikePriceDV, crt);
				} else {
					strikesForDate = new HashMap<Double, Double>();
					strikesForDate.put(strikePriceDV, crt);
				}
				tradeData.putsAvgPrices.put(date, strikesForDate);
				if (!sortedDatesPuts.contains(date)) {
					sortedDatesPuts.add(date);
				}
			}

			Collections.sort(sortedDatesPuts);
			int counterWks = 0;
			inner: for (Date crtDate : sortedDatesPuts) {
				counterWks++;
				/// System.out.println((formatter2.format(crtDate)).toString().toUpperCase());
				Map<Double, Double> strikesForDate = tradeData.putsAvgPrices.get(crtDate);
				ArrayList<Double> activeStrikesList = new ArrayList<Double>();
				for (Double crtStrike : strikesForDate.keySet()) {
					activeStrikesList.add(crtStrike);
				}
				Collections.sort(activeStrikesList);
				for (Double crtStrike : activeStrikesList) {
					Double crtAvg = strikesForDate.get(crtStrike);
					double mod = crtStrike % 1.0;
					String str = "";
					if (mod > 0) {
						str = "" + ((int) crtStrike.intValue()) + ".5";
					} else {
						str = "" + ((int) crtStrike.intValue());
					}

					String key = ((formatter2.format(crtDate)).toString().toUpperCase() + " " + str + " P")
							.toUpperCase();
					// System.out.println(key);
					int amountTraded = tradeData.activePutOptionStrikesCount.get(key);
					Double lowPrice = tradeData.lowPricesActivePutOptionStrikes.get(key) == null ? -1.0
							: tradeData.lowPricesActivePutOptionStrikes.get(key);
					Double highPrice = tradeData.highPricesActivePutOptionStrikes.get(key) == null ? -1.0
							: tradeData.highPricesActivePutOptionStrikes.get(key);
					/// System.out.println("P Strike:
					/// $"+doubleFormatter.format(crtStrike) + "
					/// ("+amountTraded+" @ avg.
					/// $"+doubleFormatter.format(crtAvg)+")
					/// $"+doubleFormatter.format(lowPrice)+"-$"+doubleFormatter.format(highPrice)+"
					/// Amt:
					/// $"+doubleFormatter.format(amountTraded*crtAvg*100));
				}
				if (numWksToShowOut == counterWks) {
					break inner;
				}
			}

			System.out.println();
			/// System.out.println(" +++ AVERAGE PAID FOR CALLS +++");
			ArrayList<Date> sortedDatesCalls = new ArrayList<Date>();
			for (String callOption : tradeData.avgPricesActiveCallOptionStrikes.keySet()) {
				Double crt = tradeData.avgPricesActiveCallOptionStrikes.get(callOption);

				StringTokenizer stDate = new StringTokenizer(callOption, " ");
				String strikeD = stDate.nextToken() + " " + stDate.nextToken() + " " + stDate.nextToken();
				Date date = new Date();
				String strikePrice = stDate.nextToken();
				Double strikePriceDV = Double.parseDouble(strikePrice);
				try {
					date = (Date) ((DateFormat) formatter1).parse(strikeD);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				Map<Double, Double> strikesForDate = tradeData.callsAvgPrices.get(date);
				if (strikesForDate != null) {
					strikesForDate.put(strikePriceDV, crt);
				} else {
					strikesForDate = new HashMap<Double, Double>();
					strikesForDate.put(strikePriceDV, crt);
				}
				tradeData.callsAvgPrices.put(date, strikesForDate);
				if (!sortedDatesCalls.contains(date)) {
					sortedDatesCalls.add(date);
				}
			}

			Collections.sort(sortedDatesCalls);
			counterWks = 0;
			inner: for (Date crtDate : sortedDatesCalls) {
				counterWks++;
				/// System.out.println((formatter2.format(crtDate)).toString().toUpperCase());
				Map<Double, Double> strikesForDate = tradeData.callsAvgPrices.get(crtDate);
				ArrayList<Double> activeStrikesList = new ArrayList<Double>();
				for (Double crtStrike : strikesForDate.keySet()) {
					activeStrikesList.add(crtStrike);
				}
				Collections.sort(activeStrikesList);
				for (Double crtStrike : activeStrikesList) {
					Double crtAvg = strikesForDate.get(crtStrike);
					double mod = crtStrike % 1.0;
					String str = "";
					if (mod > 0) {
						str = "" + ((int) crtStrike.intValue()) + ".5";
					} else {
						str = "" + ((int) crtStrike.intValue());
					}

					String key = ((formatter2.format(crtDate)).toString().toUpperCase() + " " + str + " C")
							.toUpperCase();
					// System.out.println(key);
					int amountTraded = tradeData.activeCallOptionStrikesCount.get(key);
					Double lowPrice = tradeData.lowPricesActiveCallOptionStrikes.get(key) == null ? -1.0
							: tradeData.lowPricesActiveCallOptionStrikes.get(key);
					Double highPrice = tradeData.highPricesActiveCallOptionStrikes.get(key) == null ? -1.0
							: tradeData.highPricesActiveCallOptionStrikes.get(key);
					/// System.out.println("C Strike:
					/// $"+doubleFormatter.format(crtStrike) + "
					/// v:"+amountTraded+"\t @ avg.
					/// $"+doubleFormatter.format(crtAvg)+ "
					/// $"+doubleFormatter.format(lowPrice)+"-$"+doubleFormatter.format(highPrice)+"
					/// : $"+doubleFormatter.format(amountTraded*crtAvg*100));
				}
				if (numWksToShowOut == counterWks) {
					break inner;
				}
			}

			tradeData.avgPutsPrice = tradeData.avgPricesActivePutOptionStrikes.get(tradeData.maxTradedPut);
			tradeData.avgCallsPrice = tradeData.avgPricesActiveCallOptionStrikes.get(tradeData.maxTradedCall);
			tradeData.lowPricePs = tradeData.lowPricesActivePutOptionStrikes.get(tradeData.maxTradedPut);
			tradeData.highPricePs = tradeData.highPricesActivePutOptionStrikes.get(tradeData.maxTradedPut);
			tradeData.lowPriceCs = tradeData.lowPricesActiveCallOptionStrikes.get(tradeData.maxTradedCall);
			tradeData.highPriceCs = tradeData.highPricesActiveCallOptionStrikes.get(tradeData.maxTradedCall);

			tradeData.maxDollarVolumeCallCount = tradeData.activeCallOptionStrikesCount
					.get(tradeData.maxDollarVolumeCall) == null ? 0
							: tradeData.activeCallOptionStrikesCount.get(tradeData.maxDollarVolumeCall);
			tradeData.maxDollarVolumePutCount = tradeData.activePutOptionStrikesCount
					.get(tradeData.maxDollarVolumePut) == null ? 0
							: tradeData.activePutOptionStrikesCount.get(tradeData.maxDollarVolumePut);
			tradeData.maxDVCallAvgPrice = tradeData.avgPricesActiveCallOptionStrikes.get(tradeData.maxDollarVolumeCall);
			tradeData.maxDVPutAvgPrice = tradeData.avgPricesActivePutOptionStrikes.get(tradeData.maxDollarVolumePut);
			tradeData.lowPriceDVPs = tradeData.lowPricesActivePutOptionStrikes.get(tradeData.maxDollarVolumePut);
			tradeData.highPriceDVPs = tradeData.highPricesActivePutOptionStrikes.get(tradeData.maxDollarVolumePut);
			tradeData.lowPriceDVCs = tradeData.lowPricesActiveCallOptionStrikes.get(tradeData.maxDollarVolumeCall);
			tradeData.highPriceDVCs = tradeData.highPricesActiveCallOptionStrikes.get(tradeData.maxDollarVolumeCall);

			//System.out.println(tradeData.toString());

			// Collections.sort(activeCallOptionStrikes.values());

			long endTime = System.nanoTime();

			long duration = (endTime - startTime); // divide by 1000000 to get
													// milliseconds.
			System.out.println("Processed in " + (duration / 1000000000) + " seconds at " + scanTime);

			// }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void scanSymbol(String ssymbol) {
		// exist?
		String directory = "/Users/luckywildcat/Documents/workspace/HH/data/" + ssymbol + "/";
		String path = directory + "/" + ssymbol;

		// XStream xstream = new XStream();

		OCA crtSymbol = null;// (TD) xstream.fromXML(path);
		if (crtSymbol != null) {

		} else {
			crtSymbol = new OCA();
			crtSymbol.cumulativeTradeData.symbol = ssymbol;
			// crtSymbol.symbol = ssymbol;
		}

		File folder = new File(directory);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile() && listOfFiles[i].getName().endsWith("16")) {
				System.out.println("File " + listOfFiles[i].getName());
				String datePath = directory + "/" + listOfFiles[i].getName();
				TD dataCrtDate = new TD(ssymbol, listOfFiles[i].getName(), 100);
				dataCrtDate.symbol = ssymbol;
				// dataCrtDate.tradeDateString = listOfFiles[i].getName(); //
				// 08_11_16
				this.scanPath(dataCrtDate, datePath);
				crtSymbol.tradeDataPerDate.put(listOfFiles[i].getName(), dataCrtDate);
				this.scanPath(crtSymbol.cumulativeTradeData, datePath);
			} else if (listOfFiles[i].isDirectory()) {
				System.out.println("Directory " + listOfFiles[i].getName());
			}
		}

		// String xml = xstream.toXML(path);
	}

	public String getFilePathFromDir(String path) {
		try {
			// NtlmPasswordAuthentication auth = new
			// NtlmPasswordAuthentication("", user, pw);
			File smbFile = new File(path);
			String a[] = smbFile.list();
			for (int i = 0; i < a.length; i++) {
				//
				if (a[i] != null && !a[i].equals(".DS_Store") && !a[i].equals("ACTIVE.txt")
						&& !a[i].equals("desktop.ini")) {
					System.out.println(a[i]);
					return path + a[i];
				}
			}

		} catch (Exception c) {

		}
		return "";
	}
	
	public static Spread processSpreadTransaction(List<Transaction> cached){
		//currentOpen.setCallSpread(transaction.isCall);
		if(cached.size()<=1){
			return null; // need at least two transaction
		} else {
			Double cachedStrike = null;
			List<Transaction> cachedTransactions = new ArrayList<Transaction>();
			List<Transaction> cachedBuyTransactions = new ArrayList<Transaction>();
			List<Transaction> cachedSellTransactions = new ArrayList<Transaction>();
			List<String> cachedExpiries = new ArrayList<String>();
			Double buyStrike = null;
			int buyStrikeSize = 0;
			Double sellStrike = null;
			int sellStrikeSize = 0;
			int callSpread1putSpread2 = 0;
			String expiry = null;
			boolean possibleTimeSpread = false;
			outer: for(Transaction crt : cached){
				if(expiry==null){
					expiry = crt.getSymbol();
					cachedExpiries.add(expiry);
				}else if(expiry.equals(crt.getSymbol())){
					
				}else{
					System.out.println("TIME SPREAD ! "+expiry+" <--> "+crt.getSymbol()+" BUY: $"+buyStrike+"("+buyStrikeSize+")"+" SELL: $"+sellStrike+"("+sellStrikeSize+")");
					cachedExpiries.add(crt.getSymbol());
					possibleTimeSpread = true;
					// TIME SPREAD !
					//return null;
				}
				
				System.out.println("SA: "+crt.symbol+" "+crt.price+"("+crt.size+") "+crt.market);
				if(callSpread1putSpread2==0 && cachedStrike == null &&cachedTransactions.size()==0){
					callSpread1putSpread2 = crt.isCall ? 1 : 2;
					cachedStrike = crt.strike;
					cachedTransactions.add(crt);
				}else if(cachedTransactions.size()==0){ 
					System.out.println("SPREAD ANALYSIS INTEGRITY ERROR: NO CACHED TRANSACTIONS AFTER STRIKE ASSIGNMENT");
					return null;
				}else if(callSpread1putSpread2==1){ // call spread and we have one cached strike
				
					Double otherCachedStrike = crt.strike;
					if(cachedStrike == otherCachedStrike && cachedExpiries.size()<=1){ // same leg? -> just add it
						cachedTransactions.add(crt);
					} else if(cachedStrike > otherCachedStrike){
						buyStrike = otherCachedStrike;
						sellStrike = cachedStrike;
						break outer;
					} else if(cachedStrike < otherCachedStrike){
						buyStrike = cachedStrike;
						sellStrike = otherCachedStrike;
						break outer;
					}
					
				}else if(callSpread1putSpread2==2){ // put spread and we have one cached strike

					Double otherCachedStrike = crt.strike;
					if(cachedStrike == otherCachedStrike){ // same leg? -> just add it
						cachedTransactions.add(crt);
					} else if(cachedStrike > otherCachedStrike){
						buyStrike = cachedStrike;
						sellStrike = otherCachedStrike;
						break outer;
					} else if(cachedStrike < otherCachedStrike){
						buyStrike = otherCachedStrike;
						sellStrike = cachedStrike;
						break outer;
					}
					
				}else{
					System.out.println("SPREAD ANALYSIS INTEGRITY ERROR: CACHED STRIKE AFTER ASSIGNMENT");
					return null;
				}
			}
			// call/put spread + buy and sell strike known: process transactions and check integrity
			//cachedTransactions.removeAll(cachedTransactions);
			if(callSpread1putSpread2==0){
				System.out.println("SPREAD ANALYSIS INTEGRITY ERROR: callSpread1putSpread2==INVALID");
				return null;
			}else if(buyStrike!=null && sellStrike!=null){ 
				for(Transaction crtT : cached){

					if(crtT.strike.doubleValue() == buyStrike.doubleValue()){
						cachedBuyTransactions.add(crtT);
						buyStrikeSize += crtT.size;
					}else if(crtT.strike.doubleValue() == sellStrike.doubleValue()){
						cachedSellTransactions.add(crtT);
						sellStrikeSize += crtT.size;
					}else{
						System.out.println("SPREAD ANALYSIS INTEGRITY ERROR: strike==INVALID");
						return null;
					}
				}
				if( (buyStrikeSize == sellStrikeSize) || // Spread detected ! 
					possibleTimeSpread){ 
					Spread ret = new Spread();
					String time = possibleTimeSpread?" TIME: "+cachedExpiries:"";
					ret.setSpreadName("+ $"+buyStrike+"("+buyStrikeSize+") - $"+sellStrike+"("+sellStrikeSize+") "+time);
					ret.setCallSpread(callSpread1putSpread2==1);
					ret.setLongStrike(buyStrike);
					ret.setShortStrike(sellStrike);
					ret.setLongTransactions(cachedBuyTransactions);
					ret.setShortTransactions(cachedSellTransactions);
					ret.setNumLongContracts(buyStrikeSize);
					ret.setNumShortContracts(sellStrikeSize);
					System.out.println("SPREAD: "+ret.getSpreadName());
					return ret;
				}
				
			}
		}
		
		return null;
	}
}
