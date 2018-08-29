package com.Utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Formatter {
	  
	private static NumberFormat integerFormatter = new DecimalFormat("###,###,###");

    public static String trimToDigit(String s){

    	int dotIdx = s.indexOf(".");
    	if(dotIdx>0 && s.length()>dotIdx+3)
    		return s.substring(0, dotIdx+3);
    	else
    		return s;
    }
    
    public static String formatInt(int i){
    	return ""+integerFormatter.format(i);
    }
    
    public static String convertDate(String sDate){ // "YY MM DD" 18 04 06 - > 06 Apr 2018
    	String[] tokens = sDate.split(" ");
  	  	return tokens[2]+" "+getMonth(tokens[1])+" 20"+tokens[0];
    }
    
    public static String getMonthNumber(String monthString){
		if(monthString.equalsIgnoreCase("Jan")){
			return "01";
		}else if(monthString.equalsIgnoreCase("Feb")){
			return "02";
		}else if(monthString.equalsIgnoreCase("Mar")){
			return "03";
		}else if(monthString.equalsIgnoreCase("Apr")){
			return "04";
		}else if(monthString.equalsIgnoreCase("May")){
			return "05";
		}else if(monthString.equalsIgnoreCase("Jun")){
			return "06";
		}else if(monthString.equalsIgnoreCase("Jul")){
			return "07";
		}else if(monthString.equalsIgnoreCase("Aug")){
			return "08";
		}else if(monthString.equalsIgnoreCase("Sep")){
			return "09";
		}else if(monthString.equalsIgnoreCase("Oct")){
			return "10";
		}else if(monthString.equalsIgnoreCase("Nov")){
			return "11";
		}else if(monthString.equalsIgnoreCase("Dec")){
			return "12";
		}
		
		return "ERR";
	}
    
    public static String getMonth(String monthString){

		if(monthString.equalsIgnoreCase("01")){
			return "Jan";
		}else if(monthString.equalsIgnoreCase("02")){
			return "Feb";
		}else if(monthString.equalsIgnoreCase("03")){
			return "Mar";
		}else if(monthString.equalsIgnoreCase("04")){
			return "Apr";
		}else if(monthString.equalsIgnoreCase("05")){
			return "May";
		}else if(monthString.equalsIgnoreCase("06")){
			return "Jun";
		}else if(monthString.equalsIgnoreCase("07")){
			return "Jul";
		}else if(monthString.equalsIgnoreCase("08")){
			return "Aug";
		}else if(monthString.equalsIgnoreCase("09")){
			return "Sep";
		}else if(monthString.equalsIgnoreCase("10")){
			return "Oct";
		}else if(monthString.equalsIgnoreCase("11")){
			return "Nov";
		}else if(monthString.equalsIgnoreCase("12")){
			return "Dec";
		}
		
		return "ERR";
	}
}
