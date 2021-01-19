package com.dua.finance;

import java.util.Map;

import org.apache.commons.text.WordUtils;
import org.slf4j.LoggerFactory;

public class Utility {
	
	public static final org.slf4j.Logger logger = LoggerFactory.getLogger(Utility.class);

	public static void main(String args[])
	{
		//logger.info(formatPhone("1234456787"));
		//String[] names = parseName("Zelle Transfer Conf# 371094556; MASTANVALI KALESHA SHAIK"); 
		//logger.info("First Name: {}",names[0]);
		//logger.info("Last Name: {}",names[1]);
		String s = "Receipt-asthma786@gmail.com.pdf";
		s = s.substring(8,s.indexOf(".pdf"));
		System.out.println(s);
	}
	
	public static Donor syncObject(Donor base, Donor incoming)
	{	
		if(base.getFullName() == null && incoming.getFullName() != null)
		{
			base.setFullName(incoming.getFullName());
		}
		if(base.getFirstName() == null && incoming.getFirstName() != null)
		{
			base.setFirstName(incoming.getFirstName());
		}
		if(base.getLastName() == null && incoming.getLastName() != null)
		{
			base.setLastName(incoming.getLastName());
		}
		if(base.getAddress1() == null && incoming.getAddress1() != null)
		{
			base.setAddress1(incoming.getAddress1());
		}
		if(base.getAddress2() == null && incoming.getAddress2() != null)
		{
			base.setAddress2(incoming.getAddress2());
		}
		if(base.getCity() == null && incoming.getCity() != null)
		{
			base.setCity(incoming.getCity());
		}
		if(base.getState() == null && incoming.getState() != null)
		{
			base.setState(incoming.getState());
		}
		if(base.getZip() == null && incoming.getZip() != null)
		{
			base.setZip(incoming.getZip());
		}
		if(base.getPhone() == null && incoming.getPhone() != null)
		{
			base.setPhone(incoming.getPhone());
		}		
		return base;
	}
	
	public static String[] parseName(String str)
	{
		if(str == null || str.isEmpty())
		{
			return null;
		}
		String temp = str.substring(str.indexOf(";")+1, str.length()).trim();		
		String[] names = temp.split(" ");
		String fName="", lName="";
		int i=0;
		for(String s : names)
		{
			if(i==names.length-1)
			{
				lName = s;
			}
			else
			{
				fName = fName + s +" ";
			}
			i++;
		}
		names = new String[] {fName.trim(), lName.trim()};
		return names;
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
		
		if("".equals(name1) || "".equals(name2))
		{
			return false;
		}
		
		matchFound = name1.equals(name2);
		if(matchFound)
		{
			//logger.info("Match found");
		}
		return matchFound;
	}
	
	public static void printMap(Map<String, Donor> donorMap)
	{
		for (Map.Entry<String, Donor> entry : donorMap.entrySet()) {
			if("reachvali@yahoo.com".equals(entry.getKey()))
        	{
				logger.info(entry.getKey() + ":" + entry.getValue());
        	}	
		}
	}
}