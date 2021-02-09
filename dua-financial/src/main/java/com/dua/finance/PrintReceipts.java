package com.dua.finance;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.slf4j.LoggerFactory;

import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

public class PrintReceipts {
	
	public static final org.slf4j.Logger logger = LoggerFactory.getLogger(PrintReceipts.class);
	static final String FINAL_REPORT = "ConsolidatedList.csv";
	static final String RECEIPT_TEMPLATE = "DUA Tax Receipt Letter 2020.rtf";	

	public static void main(String[] args) {
		
		try {
			
			if(args.length == 0)
			{
				logger.error("please provide a valid source folder!");
				System.exit(0);
			}
			String sourceFolder = args[0];
			
			print(sourceFolder);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void print(String sourceFolder) throws Exception
	{
		List<Donor> donors = Utility.beanBuilder(Paths.get(sourceFolder+"/"+FINAL_REPORT), Donor.class);
		logger.info("Donors list size: "+donors.size());
		String templateContent = readAllBytes(sourceFolder+"/receipts/"+RECEIPT_TEMPLATE);
		for(Donor donor : donors) {
			String content = templateContent.replaceAll("XX-Name-XX", donor.getFullName());
			content = content.replaceAll("XX-Amt-XX", Utility.getFormattedAmt(donor.getDonationAmount()));			
			content = content.replaceAll("Address: XX-1-XX", donor.getAddress1()!=null?donor.getAddress1():"");			
			content = content.replaceAll("City: XX-2-XX", donor.getCity()!=null?donor.getCity():"");
			content = content.replaceAll("State: XX-3-XX", donor.getState()!=null?donor.getState():"");
			content = content.replaceAll("Zip: XX-4-XX", donor.getZip()!=null?donor.getZip():"");
			Files.write(Paths.get(sourceFolder+"/receipts/"+"/DonationReceipt-"+donor.getDonorId()+".rtf"), content.getBytes());
		}
	}
	
	private static String readAllBytes(String filePath) 
    {
        String content = "";
 
        try
        {
            content = new String ( Files.readAllBytes( Paths.get(filePath) ) );
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
 
        return content;
    }

}
