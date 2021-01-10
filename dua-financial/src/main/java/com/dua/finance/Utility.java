package com.dua.finance;

import org.apache.commons.text.WordUtils;
import org.slf4j.LoggerFactory;

public class Utility {
	
	public static final org.slf4j.Logger logger = LoggerFactory.getLogger(Utility.class);

	public static void main(String args[])
	{
		logger.info(formatPhone("1234456787"));
	}
	
	public static double getDouble(String s)
	{
		if(s == null)
			return 0;
		
		s = s.replace("$", "");
		s = s.replace(",", "");

		return Double.parseDouble(s);
	}
	
	public static String getFormattedAmt(double d)
	{
		return String.format("%1$,.2f", d);
	}
	
	public static String getCamelCase(String s)
	{
		if(s==null) {
			return "";
		}
		s = s.toLowerCase();
		return WordUtils.capitalizeFully(s);
	}
	
	public static String formatPhone(String s)
	{
		if(s == null)
		{
			return "";
		}
		s = s.replace(" ", "").trim();
		s = s.replace("(", "");
		s = s.replace(")", "");
		s = s.replace("-", "");
		s =  s.replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1) $2-$3");
		return s;
	}
	
	public static boolean matchNames(String name1, String name2)
	{
		boolean matchFound = false;
		if(name1 == null)
			return false;
		
		if(name2 == null)
			return false; 
		
		name1 = name1.trim().toUpperCase().replace(" ", "");
		name2 = name2.trim().toUpperCase().replace(" ", "");
		
		matchFound = name1.equals(name2);
		if(matchFound)
		{
			logger.info("Match found");
		}
		return matchFound;
	}
}
