package com.dua.finance;

import java.io.File;
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
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

public class ReadAllDonations {
	
	public static final org.slf4j.Logger logger = LoggerFactory.getLogger(ReadAllDonations.class);	
	static final String FINAL_REPORT = "ConsolidatedList.csv";		
	
	public static void main(String[] args) {
		
				if(args.length == 0)
				{
					logger.error("please provide a valid source folder!");
					System.exit(0);
				}
				String sourceFolder = args[0];
				new ReadAllDonations().readDonationData(sourceFolder);
	}
	
	public void readDonationData(String sourceFolder)
	{		
		Map<String, Donor> donorMap = new HashMap<String, Donor>();
		logger.info("scanning {} source folder", sourceFolder);
		File directory = new File(sourceFolder);
		
		if(!directory.exists())
		{
			logger.error("source folder doesn't exist!");
			return;
		}
		
		String files[] = directory.list();
		if(files.length == 0)
		{
			logger.error("no files exist!");
			return;
		}
		
		for(String file : files)
		{
			logger.info("reading {} file",file);
			if(file.contains(FINAL_REPORT))
			{
				logger.info("skipping {} file",file);
				continue;
			}
			readFile(donorMap, sourceFolder+"/"+file);
		}
		
		// Write to a final consolidated report
		logger.info("Map size: " + donorMap.size());
		try {
			
			Writer writer = Files.newBufferedWriter(Paths.get(sourceFolder+"/"+FINAL_REPORT));
			writer.append(Donor.getHeader());
			StatefulBeanToCsvBuilder<Donor> builder = new StatefulBeanToCsvBuilder<>(writer);
	        StatefulBeanToCsv<Donor> beanWriter = builder.build();
				        
	        List<Donor> finalList = new ArrayList<Donor>();
	        int counter = 0;
			for (Map.Entry<String, Donor> entry : donorMap.entrySet()) {
				counter++;
				Donor donor = entry.getValue();
				donor.setDonorId(counter);
		        finalList.add(donor);
		    }
			
			beanWriter.write(finalList);
			writer.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public static void readFile(Map<String, Donor> donorMap, String fileName) 
	{	
        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            List<String[]> recList = reader.readAll();
                        
            int i=0;
            for(String[] rec : recList)
            {            	
            	i++;
            	if(i==1) {
            		continue;
            	}

            	String fullName=null, firstName=null, lastName=null, email=null, phone=null, address1=null, city=null, state=null, zip=null;
            	double donationAmount;

            	fullName = rec[0];            	
            	firstName = rec[1];
            	lastName = rec[2];
            	
            	String[] names = Utility.parseName(fullName);
            	if(names!=null) {
            		if(firstName == null) {
            			firstName = names[0];
            		}
            		if(lastName == null) {
            			lastName = names[1];
            		}
            	} 
            			
            	email = rec[3];
            	
            	if(email == null || "".equals(email.trim()))
            		continue;
            	
            	email = email.trim();           	            	
            	phone = rec[4];
            	address1 = rec[5];
            	city = rec[6];
            	state = rec[7];
            	zip = rec[8];
            	
            	donationAmount = Utility.getDouble(rec[9]);
            	
            	Donor donor = donorMap.get(email);            	
            	Donor temp = new Donor(fullName, firstName, lastName, email, donationAmount, phone, address1, city, state, zip);

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