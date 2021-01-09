package com.dua.finance;

import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;

import com.opencsv.CSVReader;

public class TaxReceipts {
	
	public static final org.slf4j.Logger logger = LoggerFactory.getLogger(TaxReceipts.class);
	
	static Map<String, Donor> donorMap = new HashMap<String, Donor>();

	public static void main(String[] args) {

		readFromDonorBoxReport();
	}
	
	public static void readFromDonorBoxReport() 
	{
        String fileName = "C:/Work/Personal/GIT-Repos/duaFinancial/reports/DUA-2020/donorbox_darululoom-austin_from_2020-01-01_to_2020-12-31_cst.csv";
        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            List<String[]> r = reader.readAll();
            r.forEach(x -> System.out.println(Arrays.toString(x)));
            
            logger.info("total records: "+r.size());
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
	}

}
