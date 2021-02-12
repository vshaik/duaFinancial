package com.dua.finance;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
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

import org.slf4j.LoggerFactory;

public class EmailUtility {

	public static final org.slf4j.Logger logger = LoggerFactory.getLogger(EmailUtility.class);
	static final String FINAL_REPORT = "ConsolidatedList.csv";
	
public static void main(String args[])
{
	
	try {
		
		if(args.length == 0)
		{
			logger.error("please provide a valid source folder!");
			System.exit(0);
		}
		String sourceFolder = args[0];		
		List<Donor> donors = Utility.beanBuilder(Paths.get(sourceFolder+"/"+FINAL_REPORT));		
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
                    return new PasswordAuthentication("finance@darululoomaustin.org", "nrncicpqgqchljrz");
                }
            });

    try {
    	
    	for(Donor donor : donors)
    	{	
	    	BodyPart messageBodyPart = new MimeBodyPart(); 
	    	messageBodyPart.setText("\n\n Assalamalekum, please find the attached donation receipt. "
	    			+ "\n\n If you have any questions please contact us at finance@darululoomaustin.org \n\n Jazakallah Khair, \n\n Darul Uloom Austin, TX");
	    	
	    	Message message = new MimeMessage(session); 
	    	message.setFrom(new InternetAddress("finance@darululoomaustin.org"));
	    	message.setSubject("Donation Receipt - Darul Uloom Austin, TX - Year 2020"); 
	    	
	    	message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(donor.getEmail())); 
	    	
	    	MimeBodyPart attachmentPart = new MimeBodyPart();
	    	attachmentPart.attachFile(new File(sourceFolder+"/Receipts/pdf/DonationReceipt-"+donor.getDonorId()+".pdf"));
	    	
	    	Multipart multipart = new MimeMultipart();
	    	multipart.addBodyPart(messageBodyPart);
	    	multipart.addBodyPart(attachmentPart);
	
	    	message.setContent(multipart);
	        
	    	Transport.send(message);
	    	
	    	logger.info("Sent receipt to "+donor.getEmail());
	    	
	    	//update Email sent status
	    	
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
