package com.dua.finance;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EmailUtility {

	public static final Logger logger = LogManager.getLogger(EmailUtility.class);
	
public static void main(String args[])
{
	
	try {
		
		if(args.length == 0)
		{
			logger.error("please provide a valid source folder!");
			System.exit(0);
		}
		String sourceFolder = args[0];		
		List<Donor> donors = Utility.beanBuilder(Paths.get(sourceFolder + "/" + AppConstants.FINAL_REPORT));		
		sendMail(donors, sourceFolder);
		
	} catch (Exception e) {
		e.printStackTrace();
	}
		
}

public static void sendMail(List<Donor> donors, String sourceFolder)
{
	Properties prop = new Properties();
    prop.put("mail.smtp.host", "smtp.gmail.com");
    prop.put("mail.smtp.port", "465");
    prop.put("mail.smtp.auth", "true");
    prop.put("mail.smtp.socketFactory.port", "465");
    prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

    
	Session session = Session.getInstance(prop,
            new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(AppConstants.EMAIL, AppConstants.PASSWORD);
                }
            });

    try {
    	
    	for(Donor donor : donors)
    	{
    		
    		if(donor.getEmail().contains("@temp.com"))
    		{
    			continue;
    		}
    		
	    	BodyPart messageBodyPart = new MimeBodyPart(); 
	    	messageBodyPart.setText("\n\n Assalamalekum, please find the attached donation receipt. "
	    			+ "\n\n If you have any questions please contact us at "+AppConstants.EMAIL+" \n\n Jazakallah Khair, \n\n "+ AppConstants.ORG);
	    	
	    	Message message = new MimeMessage(session); 
	    	message.setFrom(new InternetAddress(AppConstants.EMAIL));
	    	message.setSubject("Donation Receipt - "+AppConstants.ORG+" - Year 2020"); 
	    	
	    	message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(donor.getEmail())); 
	    	
	    	MimeBodyPart attachmentPart = new MimeBodyPart();
	    	
	    	File receipt = new File(sourceFolder+"/Receipts/Receipt-"+donor.getFullName()+"-"+donor.getDonorId()+".pdf");
	    	File sentReceipt = new File(sourceFolder+"/Receipts/sent/Receipt-"+donor.getFullName()+"-"+donor.getDonorId()+".pdf");
	    	File receiptToDelete = new File(sourceFolder+"/Receipts/Receipt-"+donor.getFullName()+"-"+donor.getDonorId()+".rtf");
	    	attachmentPart.attachFile(receipt);
	    	
	    	Multipart multipart = new MimeMultipart();
	    	multipart.addBodyPart(messageBodyPart);
	    	multipart.addBodyPart(attachmentPart);
	
	    	message.setContent(multipart);
	        
	    	try {
	    		if(receipt.exists()) {
	    			Transport.send(message);
	    			//move the receipt to sent folder
	    	    	Files.move(receipt.toPath(), sentReceipt.toPath(), StandardCopyOption.REPLACE_EXISTING);
	    	    	logger.info("{} sent to {}", receipt.getName(), donor.getEmail());
	    		}
	    		else
	    		{
	    			logger.info("{} doesn't exist",receipt.getName());
	    		}
	    	}
	    	catch(Exception e)
	    	{
	    		e.printStackTrace();
	    	}
	    	
	    	
	    	if(receiptToDelete.exists()) {
	    		receiptToDelete.deleteOnExit();
	    	}
	    	else {
	    		logger.info("{} doesn't exist",receiptToDelete.getName());
	    	}
	    	
    	}
        System.out.println("Done");

    } catch (MessagingException e) {
        e.printStackTrace();
    } catch (IOException e) {
		e.printStackTrace();
	}
}

public static Set<String> listFilesUsingJavaIO(String dir) {
    return Stream.of(new File(dir).listFiles())
      .filter(file -> !file.isDirectory())
      .map(File::getName)
      .collect(Collectors.toSet());
}
	
}
