package com.dua.finance;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UnPaidTuitionReport {
    public static void main(String[] args) throws IOException {
        // Load the spreadsheets
        Workbook workbook1 = new XSSFWorkbook(new FileInputStream("C:\\work\\Non-Profit-Donations\\DUA\\Muntazim\\reports\\DUA Parents.xlsx"));
        Workbook workbook2 = new XSSFWorkbook(new FileInputStream("C:\\work\\Non-Profit-Donations\\DUA\\Muntazim\\reports\\DUA Operations.xlsx"));

        // Assume data is in the first sheet
        Sheet sheet1 = workbook1.getSheetAt(0);
        Sheet sheet2 = workbook2.getSheetAt(0);

        // Map to hold <student_parent, phone_email> pairs from spreadsheet1
        Map<String, String[]> dataMap = new HashMap<>();

        // Read data from spreadsheet1 into the map (Skipping header row)
        for (int i = 1; i <= sheet1.getLastRowNum(); i++) {
            Row row = sheet1.getRow(i);
            String studentParentKey = normalizeKey(row.getCell(0).getStringCellValue(), row.getCell(1).getStringCellValue());
            String[] phoneEmail = {
                    row.getCell(2).getStringCellValue(),
                    row.getCell(3).getStringCellValue()
            };
            dataMap.put(studentParentKey, phoneEmail);
        }

        // Write phone and email data to spreadsheet2 (Assuming appropriate columns exist, skipping header row)
        for (int i = 1; i <= sheet2.getLastRowNum(); i++) {
            Row row = sheet2.getRow(i);
            String studentParentKey = normalizeKey(row.getCell(0).getStringCellValue(), row.getCell(1).getStringCellValue());
            
            // If the map contains data for this student-parent pair, write it to the sheet
            if (dataMap.containsKey(studentParentKey)) {
                String[] phoneEmail = dataMap.get(studentParentKey);
                row.createCell(3).setCellValue(phoneEmail[0]);  // Phone
                row.createCell(4).setCellValue(phoneEmail[1]);  // Email
            }
        }

        // Save the modified spreadsheet2
        try (FileOutputStream fileOut = new FileOutputStream("C:\\work\\Non-Profit-Donations\\DUA\\Muntazim\\reports\\merged_spreadsheet2.xlsx")) {
            workbook2.write(fileOut);
        }

        // Close the workbooks
        workbook1.close();
        workbook2.close();
    }
    
    private static String normalizeKey(String student, String parent) {
        return (student.trim().replaceAll(" +", " ") + "_" + parent.trim().replaceAll(" +", " ")).toLowerCase();
    }
}
