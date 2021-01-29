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
	static final String FINAL_REPORT = "C:/git-repos/duaFinancial/reports/DUA-2020/ConsolidatedList.csv";
	static final String RECEIPT_TEMPLATE = "C:/git-repos/duaFinancial/reports/DUA-2020/DUA Tax Receipt Letter 2020.rtf";
	static final String RECEIPT_FOLDER = "C:/git-repos/duaFinancial/reports/DUA-2020/Receipts";

	public static void main(String[] args) {
		
		try {
			print();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void print() throws Exception
	{
		List<Donor> donors = beanBuilderExample(Paths.get(FINAL_REPORT), Donor.class);
		logger.info("Donors list size: "+donors.size());
		String templateContent = readAllBytesJava7(RECEIPT_TEMPLATE);
		for(Donor donor : donors) {
			String content = templateContent.replaceAll("XX-Name-XX", donor.getFullName());
			content = content.replaceAll("XX-Amt-XX", Utility.getFormattedAmt(donor.getDonationAmount()));			
			content = content.replaceAll("Address: XX-1-XX", donor.getAddress1()!=null?donor.getAddress1():"");			
			content = content.replaceAll("City: XX-2-XX", donor.getCity()!=null?donor.getCity():"");
			content = content.replaceAll("State: XX-3-XX", donor.getState()!=null?donor.getState():"");
			content = content.replaceAll("Zip: XX-4-XX", donor.getZip()!=null?donor.getZip():"");
			Files.write(Paths.get(RECEIPT_FOLDER+"/DonationReceipt-"+donor.getDonorId()+".rtf"), content.getBytes());
		}
	}
	
	private static String readAllBytesJava7(String filePath) 
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
	
	 public static List<Donor> beanBuilderExample(Path path, Class clazz) throws Exception {
	     ColumnPositionMappingStrategy ms = new ColumnPositionMappingStrategy();
	     ms.setType(clazz);

	     Reader reader = Files.newBufferedReader(path);
	     CsvToBean cb = new CsvToBeanBuilder(reader)
	       .withType(clazz)
	       .withMappingStrategy(ms)
	       .build();

	    List<Donor> objects = cb.parse();
	    reader.close();
	    return objects;
	}

}
