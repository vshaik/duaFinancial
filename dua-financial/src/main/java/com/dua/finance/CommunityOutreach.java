package com.dua.finance;

import java.io.FileReader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opencsv.CSVReader;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

public class CommunityOutreach {

	public static final Logger logger = LogManager.getLogger(CommunityOutreach.class);
	public static final String DONOR_CONTACTS = "C:/git-repos/duaFinancial/reports/DUA-2021/Community-Outreach-raw.csv";
	
	public static void main(String[] args) {

		try {
			
			Map<String, DonorContact> donorMap = new HashMap<String, DonorContact>();			
			readData(donorMap, DONOR_CONTACTS);
			
			Writer writer = Files.newBufferedWriter(Paths.get("C:/git-repos/duaFinancial/reports/DUA-2021/Community-Outreach.csv"));
			writer.append(DonorContact.getHeader());
			StatefulBeanToCsvBuilder<DonorContact> builder = new StatefulBeanToCsvBuilder<>(writer);
	        StatefulBeanToCsv<DonorContact> beanWriter = builder.build();
				        
	        List<DonorContact> finalList = new ArrayList<DonorContact>();
	        
			for (Map.Entry<String, DonorContact> entry : donorMap.entrySet()) {			
				DonorContact donor = entry.getValue();				
		        finalList.add(donor);
		    }
			
			beanWriter.write(finalList);
			writer.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public static void readData(Map<String, DonorContact> donorMap, String fileName) throws Exception {
		
		logger.info("reading file {}",fileName);
		
		try (CSVReader reader = new CSVReader(new FileReader(fileName))) {

			List<String[]> recList = reader.readAll();
			int i=0;
			for(String[] rec : recList)
            {            	
            	i++;
            	if(i==1) {
            		continue;
            	}

            	String name=null, email=null, phone=null;
            	
            	name = rec[0];
            	email = rec[1];
            	phone = rec[2];          
            	phone = Utility.formatPhone(phone);
            	logger.info("name: {}, email: {}, phone: {}", name, email, phone);
            	            	
            	DonorContact donor = donorMap.get(email);
            	if(donor == null)
            	{
            		donor = new DonorContact(name, email, phone);
            	}
            	else
            	{
            		if(Utility.isBlank(donor.name))
            		{
            			donor.name = name;
            		}
            		if(Utility.isBlank(donor.phone))
            		{
            			donor.phone = phone;
            		}
            	}
            	donorMap.put(email, donor);
            }
			
		} catch (Exception e) {
			throw new Exception(e);
		}
		
	}

}
