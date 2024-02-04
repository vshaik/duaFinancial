package com.dua.finance;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class GmailSender {

    public static void main(String[] args) {
    	
    	System.setProperty("https.protocols", "TLSv1.2,TLSv1.3");
    	System.setProperty("javax.net.debug", "ssl:handshake");
    	System.setProperty("javax.net.debug", "ssl:handshake:verbose");

        final String username = "finance@darululoomaustin.org"; // Your Gmail email address
        final String password = "avfo jbhy exjv yvfw"; // Your Gmail password or App Password if 2FA is enabled

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // Use TLS
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587"); // TLS Port

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("finance@darululoomaustin.org")); // Sender's email (can be your Gmail)
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse("reachvali@yahoo.com")); // Receiver's email address
            message.setSubject("Test Mail from Java"); // Subject line
            message.setText("Dear Mail Crawler," + "\n\n No spam to my email, please!"); // Mail body

            Transport.send(message);

            System.out.println("Mail sent successfully!");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
