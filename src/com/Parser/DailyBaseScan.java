package com.Parser;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import com.Data.MarketDay;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class DailyBaseScan {
	private static Logger logger = Logger.getLogger("DailyBaseScan");
	
	public static String urlDaily = //"http://finance.yahoo.com/d/quotes.csv?s=**LOOKUP**&f=sl1d1t1c1ohgv&e=.csv";
	
	 //"http://download.finance.yahoo.com/d/quotes.csv?s=**LOOKUP**&f=sl1d1t1c1ohgva2m3m4t8kjf6j1j2eb4rs7&e=.csv";
	"http://www.google.com/finance/historical?q=**LOOKUP**&histperiod=daily&startdate=Apr+2+2018&enddate=Apr+3+2018&output=csv";

	public static void main(String[] args){
		DailyBaseScan scan = new DailyBaseScan();
		scan.scan();//OptionsData();
	}
	
	public void scanOptionsData(){
		Scanner sc;
		try {
			sc = new Scanner(new File("/Users/luckywildcat/Documents/watchlist.txt"));
			List<String> lines = new ArrayList<String>();
			while (sc.hasNextLine()) {
			  lines.add(sc.nextLine());
			}

			String[] arr = lines.toArray(new String[0]);
			String accum = arr[0];
			System.out.println(accum);
			// read in latest data scan, get all symbols' dates
			
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void scan(){
		
		// open watchlist, iterate and build download URL, download, then iterate over symbols and persist each marketday info
		Scanner sc;
		try {
			sc = new Scanner(new File("/Users/luckywildcat/Documents/watchlist.txt"));
			List<String> lines = new ArrayList<String>();
			while (sc.hasNextLine()) {
			  lines.add(sc.nextLine());
			}

			String[] arr = lines.toArray(new String[0]);
			String accum = arr[0];
			/*for(int i = 1 ; i<arr.length;i++){
				//System.out.println(arr[i]);
				accum = accum +"+"+arr[i];
			}*/
			System.out.println(accum);
			updateDailyData(accum);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public static void updateDailyData(String lookup){//Security stock, Map<Date, MarketDay> historicalTradeData){
		
		boolean done = false;
		int redo = 0;
		//float prevClose = 0;
		boolean retry = true;
		while(retry){
			retry = false;
			try {
				//"GOOG",368.50,"8/11/2006","4:00pm",-5.70,374.35,375.28,368.00,3768869
				//sym, CLOSE, DATE, time, change, OPEN, HIGH, LOW, VOLUME
				while(redo<=6 && done==false){
					URL t = new URL(urlDaily.replace("**LOOKUP**", lookup));
					System.out.println(t.toString());
					String record = "";
					URLConnection con1 = t.openConnection();
					DataInputStream dis = new DataInputStream(con1.getInputStream());
					record = dis.readLine();
					//if(record.contains("Invalid Ticker Symbol"))
					//	return;// null;
					System.out.println(record);
					/*while(record!=null){

						record = dis.readLine();
						System.out.println(record);
					}*/
					int run = 0;
					while(run==0&&record != null && record.length()>30){// seams to be good record
						run =1;
						StringTokenizer parse = new StringTokenizer(record, ",");
						String symbol = parse.nextToken().replace("\"", "");//"GOOG"-->GOOG
/*						if(!symbol.startsWith(stock.symbol)) {
							//System.out.println();
							throw new RuntimeException("Wrong Daily Data found for "+symbol+" <--> "+stock.symbol);
						}*/
						String _close = parse.nextToken();
						float close = !_close.contains("N")?Float.parseFloat(_close):0;//Float.parseFloat(_close);
						float gap = 0;
	/*					if(prevClose!=0) gap = close - prevClose;
						prevClose = close;*/
						StringTokenizer dater = new StringTokenizer(parse.nextToken().replace("\"", ""), "/");
						
						int month = 0;
						int day = 0;
						int year = 0;
						Date date = null;
						
						String _month = dater.nextToken();
						String _day = dater.nextToken();
						String _year = null;
						if(dater.hasMoreTokens()) _year = dater.nextToken();
						
						if(_year!=null|| !_month.startsWith("N")){
							// date appears ok
							month = Integer.parseInt(_month);
							day = Integer.parseInt(_day);
							year = Integer.parseInt(_year);
							date = new Date(year-1900, month-1, day, 16 ,0 ,0 );
							redo=7;
							done = true;
						}
						//else return;// null;//date = new Date(106, 11, 1, 16 ,0 ,0 );
						
						//logger.info(date.toString());
						parse.nextToken();//"4:00pm" - time
						parse.nextToken();//-5.70 - change
						
						String _open = parse.nextToken();
						String _high = parse.nextToken();
						String _low = parse.nextToken();
						String _vol = parse.nextToken();

						//
						String _avgdailyvolume = parse.nextToken();
						String _fiftydayMA = parse.nextToken();
						String _twohundreddayMA = parse.nextToken();
						String _firstyeartarget = parse.nextToken();
						String _fiftytwoweekhigh = parse.nextToken();
						String _fiftytwoweeklow = parse.nextToken();
						String _floatsize = parse.nextToken();
						String _marketcap = parse.nextToken();
						String _sharesout = parse.nextToken();
						String _eps = parse.nextToken();
						String _bookvalue = parse.nextToken();
						String _pe = parse.nextToken();
						//String _revenue = parse.nextToken();
						String _shortratio = parse.nextToken();
						
						float open = !_open.contains("N")?Float.parseFloat(_open):0;
						float high = !_high.contains("N")?Float.parseFloat(_high):0;
						float low = !_low.contains("N")?Float.parseFloat(_low):0;
						long vol = !_vol.contains("N")?Long.parseLong(_vol):0;
						
						//float dividend=!_dividend.contains("N")?Float.parseFloat(_low):0;
						long avgdailyvolume = !_avgdailyvolume.contains("N")?Long.parseLong(_avgdailyvolume):0;
						float fiftydayMA = !_fiftydayMA.contains("N")?Float.parseFloat(_fiftydayMA):0;
						float twohundreddayMA = !_twohundreddayMA.contains("N")?Float.parseFloat(_twohundreddayMA):0;
						float firstyeartarget = !_firstyeartarget.contains("N")?Float.parseFloat(_firstyeartarget):0;
						float fiftytwoweekhigh = !_fiftytwoweekhigh.contains("N")?Float.parseFloat(_fiftytwoweekhigh):0;
						float fiftytwoweeklow = !_fiftytwoweeklow.contains("N")?Float.parseFloat(_fiftytwoweeklow):0;
						long floatsize = !_floatsize.contains("N")?Long.parseLong(_floatsize):0;
						float marketcap = 0;
						if(_marketcap.contains("B")){
							marketcap = Float.parseFloat(_marketcap.replace("B", ""))*1000000000;
						}else if(_marketcap.contains("M")){
							marketcap = Float.parseFloat(_marketcap.replace("M", ""))*1000000;
						}else{
							marketcap = !_marketcap.contains("N")?Float.parseFloat(_marketcap):0;
						}
						float sharesout = !_sharesout.contains("N")?Float.parseFloat(_sharesout):0;
						float eps = !_eps.contains("N")?Float.parseFloat(_eps):0;
						float bookvalue = !_bookvalue.contains("N")?Float.parseFloat(_bookvalue):0;
						float pe = !_pe.contains("N")?Float.parseFloat(_pe):0;
						//float revenue = !_revenue.contains("N")?Float.parseFloat(_low):0;
						float shortratio = !_shortratio.contains("N")?Float.parseFloat(_shortratio):0;
						
						//Long.parseLong(parse.nextToken());
						
						MarketDay md = new MarketDay(date,open,gap,close,high,low, vol);
						//md.dividend = dividend;
						md.avgdailyvolume = avgdailyvolume;
						md.fiftydayMA = fiftydayMA;
						md.twohundreddayMA = twohundreddayMA;
						md.firstyeartarget = firstyeartarget;
						md.fiftytwoweekhigh = fiftytwoweekhigh;
						md.fiftytwoweeklow = fiftytwoweeklow;
						md.floatsize = floatsize;
						md.marketcap = marketcap;
						md.sharesout = sharesout;
						md.eps = eps;
						md.bookvalue = bookvalue;
						md.pe = pe;
						//md.revenue = revenue;
						md.shortratio = shortratio;
						System.out.println(""+symbol+": "+md.toString());
						saveObject("/Users/luckywildcat/Documents/AAPL.txt", md);
						//return;
						//md.setMySecId(stock.getId());
						
						//System.out.println(record);
						//System.out.println(date.toString()+" "+md.toString());
						//logger.info(date.toString()+" "+md.toString());
						/*if(!historicalTradeData.containsKey(date)){
							if(md.volume>0){
								if(stock.latestTradingDay==null || (!stock.latestTradingDay.equals(date)) ){
									//System.out.println("Updated last trading day on "+stock.symbol+" "+stock.exchange+" "+md.toString());
									stock.setLatestTradingDay(md);
								}
								historicalTradeData.put(date,md);
								return;// md; // return to save
							}
							else return;// null;//dont save, but done

							//logger.info("Successfully loaded new daily data for "+stock.symbol+" "+stock.exchange);
						}*/
						

						record = dis.readLine();
						//return;// null;
					}
					/*else{
						long start = System.currentTimeMillis();
						long end = start + 350;// end in milli seconds
						while(System.currentTimeMillis()<end){
						}
						redo++;
					}*/
				}
				
				//return;// null;
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} 
			catch(ConnectException ce){
		 		ce.printStackTrace();
				/*logger.warning("Connection timed out for "+stock.symbol+" "+stock.exchange+" "+urlDaily+"\nRetrying after 3 seconds...");
				retry = true;
				long start = System.currentTimeMillis();
				long end = start + 3000;// end in milli seconds
				while(System.currentTimeMillis()<end){
				}*/
		 	}
			catch(IOException ioe){
		 		ioe.printStackTrace();
				/*logger.warning("IOException for "+stock.symbol+" "+stock.exchange+" "+urlDaily+"\nRetrying after 3 seconds...");
				retry = true;
				long start = System.currentTimeMillis();
				long end = start + 3000;// end in milli seconds
				while(System.currentTimeMillis()<end){
				}*/
		 	}catch(Exception ie){
		 		ie.printStackTrace();
		 	}
			//if(!retry)
				//return;// null;
		}
		//return;// null;
		
		
	}
	
	public static boolean saveObject(String filePath, Object obj){
		XStream xstream=new XStream(new DomDriver());
		
		//System.out.println("done reading xml "+xml);
		try {
			FileOutputStream reader = null;
			try {
				reader = new FileOutputStream(filePath);
				//return reader;
				/*String current="";
				StringBuffer accum=new StringBuffer();
				while((current = reader.readLine())!=null){
					accum.append(current);
				}
				reader.close();
				return accum.toString();*/
			} catch (FileNotFoundException e) {
				//logger.warning("Couldn't find file "+filePath);
				//e.printStackTrace();
			}
			catch (IOException ioe) {
				logger.warning("IOException when trying to read "+filePath);
				ioe.printStackTrace();
			}
			OutputStream closeMe = reader;//
					//.getOutputStream(filePath);
			//if (obj.size() > 0)
				xstream.toXML(obj, closeMe);
			closeMe.close();
			//logger.info("Historical Data saved for "+testSym);
		} catch (IOException e) {
			logger.warning("Failed to store to "+filePath);
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
}
