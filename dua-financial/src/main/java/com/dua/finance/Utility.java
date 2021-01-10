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

		return Double.parseDouble(s);
	}
	
	public static String getCamelCase(String s)
	{
		if(s==null) {
			return "";
		}
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
}
