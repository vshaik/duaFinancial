package com.dua.finance;

import java.io.FileReader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;

import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

public class TaxReceipts {
	
	public static final org.slf4j.Logger logger = LoggerFactory.getLogger(TaxReceipts.class);
	static final String DONORBOX_FILE = "C:/Work/Personal/GIT-Repos/duaFinancial/reports/DUA-2020/donorbox_darululoom-austin_from_2020-01-01_to_2020-12-31_cst.csv";
	static final String FEEL_BLESSED_FILE = "C:/Work/Personal/GIT-Repos/duaFinancial/reports/DUA-2020/feelingBlessed_org_a6f59bc20c289d2de30592abd2b0f0ac.csv";
	static final String SQUARE_FILE = "C:/Work/Personal/GIT-Repos/duaFinancial/reports/DUA-2020/square_transactions-2020-01-01-2021-01-01.csv";
	static final String ZELLE_FILE = "C:/Work/Personal/GIT-Repos/duaFinancial/reports/DUA-2020/ZelleDonations2020.csv";
	static final String FINAL_REPORT = "C:/Work/Personal/GIT-Repos/duaFinancial/reports/DUA-2020/ConsolidatedList.csv";	
	static String fullName, firstName, lastName, email, phone, address1, address2, city, state, zip;
	static double donationAmount;
	
	public static void main(String[] args) {

		Map<String, Donor> donorMap = new HashMap<String, Donor>();
		
		readFromDonorBoxReport(donorMap);
		readFromFeelBlessedReport(donorMap);
		readFromZelleReport(donorMap);
		readFromSquarePos(donorMap);
		
		// Write to a final consolidated report
		logger.info("Map size: " + donorMap.size());
		try {
			
			Writer writer = Files.newBufferedWriter(Paths.get(FINAL_REPORT));	

	        // mapping of columns with their positions
	        ColumnPositionMappingStrategy<Donor> mappingStrategy = new ColumnPositionMappingStrategy<Donor>();
	        // Set mappingStrategy type to Donor Type
	        mappingStrategy.setType(Donor.class);
	        // Fields in Donor Bean
	        String[] columns = new String[] { "Full Name", "First Name", "Last Name", "Email", "Donation Amount", "Phone", "Address Line1", "Address Line2", "City", "State", "Zip" };
	        // Setting the columns for mappingStrategy
	        mappingStrategy.setColumnMapping(columns);
			
			StatefulBeanToCsvBuilder<Donor> builder = new StatefulBeanToCsvBuilder<>(writer);
	        // StatefulBeanToCsv<Donor> beanWriter = builder.build();
			StatefulBeanToCsv<Donor> beanWriter = builder.withMappingStrategy(mappingStrategy).build();
				        
	        List<Donor> finalList = new ArrayList<Donor>();
			for (Map.Entry<String, Donor> entry : donorMap.entrySet()) {
				if("reachvali@yahoo.com".equals(entry.getKey()))
            	{
					logger.info(entry.getKey() + ":" + entry.getValue());
            	}		        
		        finalList.add(entry.getValue());
		    }
			
			beanWriter.write(finalList);
			writer.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public static void readFromSquarePos(Map<String, Donor> donorMap) 
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
            	
            	Donor donor = new Donor(fullName, firstName, lastName, email, donationAmount, phone, address1, address2, city, state, zip);
            	
            	for (Map.Entry<String, Donor> entry : donorMap.entrySet()) {
            		Donor temp = entry.getValue();
            		if(Utility.matchNames(donor.getFullName(), temp.getFullName()))
            		{
            			//logger.info(entry.getKey()+" -----------Match Found----------- "+donor);
            			temp.setDonationAmount(temp.getDonationAmount()+donor.getDonationAmount());
            			donorMap.put(entry.getKey(), temp);
            		}
            	}
            	
            }
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
	}
	
	
	public static void readFromZelleReport(Map<String, Donor> donorMap) 
	{	
        try (CSVReader reader = new CSVReader(new FileReader(ZELLE_FILE))) {
            List<String[]> recList = reader.readAll();
                        
            int i=0;
            for(String[] rec : recList)
            {	
            	i++;
            	if(i==1) {
            		continue;
            	}
            	
            	fullName = rec[1];
            	
            	String[] names = Utility.parseName(fullName);
            	firstName = names[0];
            	lastName = names[1];
            	
            	fullName = firstName + " " + lastName; 
            			
            	email = rec[4];
            	
            	if(email == null || "".equals(email.trim()))
            		continue;
            	
            	email = email.trim();
            	
            	donationAmount = Utility.getDouble(rec[2]);
            	phone = null;
            	address1 = null;
            	address2 = null;
            	city = null;
            	state = null;
            	zip = null;
            	
            	Donor donor = donorMap.get(email);            	
            	Donor temp = new Donor(fullName, firstName, lastName, email, donationAmount, phone, address1, address2, city, state, zip);

            	if(donor==null) {
            		donor = temp;
            	}
            	else
            	{
            		donor = Utility.syncObject(donor, temp);
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
	
	public static void readFromFeelBlessedReport(Map<String, Donor> donorMap) 
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
            	zip = rec[4];
            	Donor donor = donorMap.get(email);
            	Donor temp = new Donor(fullName, firstName, lastName, email, donationAmount, phone, address1, address2, city, state, zip);

            	if(donor==null) {
            		donor = temp;
            	}
            	else
            	{
            		donor = Utility.syncObject(donor, temp);
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
	
	public static void readFromDonorBoxReport(Map<String, Donor> donorMap) 
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
            	Donor temp = new Donor(fullName, firstName, lastName, email, donationAmount, phone, address1, address2, city, state, zip);

            	if(donor==null) {
            		donor = temp;
            	}
            	else
            	{
            		donor = Utility.syncObject(donor, temp);
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