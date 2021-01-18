package com.dua.finance;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
	
public static void main(String args[])
{	

	Set<String> set = listFilesUsingJavaIO("C:/Work/Personal/GIT-Repos/duaFinancial/reports/DUA-2020/Receipts/pdf/");
	
	List<String> list = new ArrayList<String>();
	for (String s : set) {
		s.substring(8,s.indexOf(".pdf"));
	    list.add(s);
	}
	
	sendMail(list);
	
}


public static void sendMail(List<String> emails)
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

    	
    	for(String email : emails)
    	{
    		//logger.info("Email - "+email);
    		if(!"reachvali@gmail.com".equals(email))
    		{
    			continue;
    		}
    		
	    	BodyPart messageBodyPart = new MimeBodyPart(); 
	    	messageBodyPart.setText("\n\n Assalamalekum, please find the attached donation receipt. \n\n Jazakallah Khair, \n\n Darul Uloom Austin, TX");
	    	
	    	Message message = new MimeMessage(session); 
	    	message.setFrom(new InternetAddress("finance@darululoomaustin.org"));
	    	message.setSubject("Donation Receipt - Darul Uloom Austin, TX - Year 2020"); 
	    	
	    	message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("reachvali@gmail.com")); 
	    	
	    	MimeBodyPart attachmentPart = new MimeBodyPart();
	    	attachmentPart.attachFile(new File("C:/Work/Personal/GIT-Repos/duaFinancial/reports/DUA-2020/Receipts/pdf/Receipt-"+email+".pdf"));
	    	
	    	Multipart multipart = new MimeMultipart();
	    	multipart.addBodyPart(messageBodyPart);
	    	multipart.addBodyPart(attachmentPart);
	
	    	message.setContent(multipart);
	        
	    	Transport.send(message);
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
