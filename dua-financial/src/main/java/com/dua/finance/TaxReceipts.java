package com.dua.finance;

import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;

import com.opencsv.CSVReader;

public class TaxReceipts {
	
	public static final org.slf4j.Logger logger = LoggerFactory.getLogger(TaxReceipts.class);
	static final String DONORBOX_FILE = "C:/Work/Personal/GIT-Repos/duaFinancial/reports/DUA-2020/donorbox_darululoom-austin_from_2020-01-01_to_2020-12-31_cst.csv";
	static final String FEEL_BLESSED_FILE = "C:/Work/Personal/GIT-Repos/duaFinancial/reports/DUA-2020/feelingBlessed_org_a6f59bc20c289d2de30592abd2b0f0ac.csv";
	static final String SQUARE_FILE = "C:/Work/Personal/GIT-Repos/duaFinancial/reports/DUA-2020/square_transactions-2020-01-01-2021-01-01.csv";
	static Map<String, Donor> donorMap = new HashMap<String, Donor>();
	static String fullName, firstName, lastName, email, phone, address1, address2, city, state, zip;
	static double donationAmount;
	
	public static void main(String[] args) {

		readFromDonorBoxReport();
		readFromFeelBlessedReport();
		//readFromSquarePos();
		logger.info("Map size: "+donorMap.size());
		for (Map.Entry<String, Donor> entry : donorMap.entrySet()) {
	        logger.info(entry.getKey() + ":" + entry.getValue());
	    }
	}
	
	public static void readFromSquarePos() 
	{	
        try (CSVReader reader = new CSVReader(new FileReader(SQUARE_FILE))) {
            List<String[]> recList = reader.readAll();
                        
            int i=0;
            for(String[] rec : recList)
            {	
            	i++;
            	if(i==1) {
            		continue;
            	}
            	
            	fullName = rec[34];
            	firstName = null;
            	lastName = null;
            	email = null;
            	donationAmount = Utility.getDouble(rec[3]);
            	phone = null;
            	address1 = null;
            	address2 = null;
            	city = null;
            	state = null;
            	zip = null;
            	Donor donor = donorMap.get(email);
            	if(donor==null) {
            		donor = new Donor(fullName, firstName, lastName, email, donationAmount, phone, address1, address2, city, state, zip);
            	}
            	else
            	{
            		donor.setDonationAmount(donor.getDonationAmount()+donationAmount);
            		logger.info("------------------------------------------------------"+donor.getFullName());
            	}
            	donorMap.put(email, donor);
            	
            }
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
	}
	
	public static void readFromFeelBlessedReport() 
	{	
        try (CSVReader reader = new CSVReader(new FileReader(FEEL_BLESSED_FILE))) {
            List<String[]> recList = reader.readAll();
                        
            int i=0;
            for(String[] rec : recList)
            {	
            	i++;
            	if(i==1) {
            		continue;
            	}
            	
            	fullName = rec[1];
            	firstName = null;
            	lastName = null;
            	email = rec[2];
            	donationAmount = Utility.getDouble(rec[8]);
            	phone = rec[6];
            	address1 = rec[3];
            	address2 = null;
            	city = null;
            	state = null;
            	zip = null;
            	Donor donor = donorMap.get(email);
            	if(donor==null) {
            		donor = new Donor(fullName, firstName, lastName, email, donationAmount, phone, address1, address2, city, state, zip);
            	}
            	else
            	{
            		donor.setDonationAmount(donor.getDonationAmount()+donationAmount);
            	}
            	donorMap.put(email, donor);
            	
            }
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
	}
	
	public static void readFromDonorBoxReport() 
	{	
        try (CSVReader reader = new CSVReader(new FileReader(DONORBOX_FILE))) {
            List<String[]> recList = reader.readAll();
            
            int i=0;
            for(String[] rec : recList)
            {	
            	i++;
            	if(i==1) {
            		continue;
            	}
            	
            	fullName = rec[0];
            	firstName = rec[1];
            	lastName = rec[2];
            	email = rec[3];
            	donationAmount = Utility.getDouble(rec[7]);
            	phone = rec[17];
            	address1 = rec[18];
            	address2 = null;
            	city = rec[19];
            	state = rec[20];
            	zip = rec[21];
            	Donor donor = donorMap.get(email);
            	if(donor==null) {
            		donor = new Donor(fullName, firstName, lastName, email, donationAmount, phone, address1, address2, city, state, zip);
            	}
            	else
            	{
            		donor.setDonationAmount(donor.getDonationAmount()+donationAmount);
            	}
            	donorMap.put(email, donor);            	
            	
            }
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
	}
	
}