package com.dua.finance;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sun.mail.smtp.SMTPTransport;

public class EmailUtilityUsingOAuth {

	public static final Logger logger = LogManager.getLogger(EmailUtility.class);

	public static void main(String args[]) {

		try {

			if (args.length == 0) {
				logger.error("please provide a valid source folder!");
				System.exit(0);
			}

			System.setProperty("https.protocols", "TLSv1.2,TLSv1.3");
			System.setProperty("https.cipherSuites",
					"TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384,TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384");
			System.setProperty("javax.net.debug", "ssl:handshake");
			//System.setProperty("mail.pop3s.ssl.protocols", "TLSv1.2");
			System.setProperty("mail.imaps.ssl.protocols", "TLSv1.2");


			String sourceFolder = args[0];
			Properties appProps = Utility.getConfigProperties(sourceFolder + "/config/config.properties");
			List<Donor> donors = Utility.beanBuilder(Paths.get(sourceFolder + "/" + AppConstants.FINAL_REPORT));
			sendMail(donors, sourceFolder, appProps);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void sendMail(List<Donor> donors, String sourceFolder, Properties appProps) {
		Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.port", "587");
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.starttls.enable", "true");
		prop.put("mail.smtp.starttls.required", "true");

		Session session = Session.getInstance(prop);

		try {

			for (Donor donor : donors) {

				if (donor.getEmail().contains("@temp.com")) {
					continue;
				}

				BodyPart messageBodyPart = new MimeBodyPart();
				messageBodyPart.setText("\n\n Assalamalekum, please find the attached donation receipt. "
						+ "\n\n If you have any questions please contact us at " + appProps.getProperty("email")
						+ " \n\n Jazakallah Khair, \n\n " + appProps.getProperty("org"));

				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(appProps.getProperty("email")));
				message.setSubject("Donation Receipt - " + appProps.getProperty("org") + " - Year "
						+ appProps.getProperty("year"));

				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(donor.getEmail()));

				MimeBodyPart attachmentPart = new MimeBodyPart();

				File receipt = new File(
						sourceFolder + "/Receipts/Receipt-" + donor.getFullName() + "-" + donor.getDonorId() + ".pdf");
				File sentReceipt = new File(sourceFolder + "/Receipts/sent/Receipt-" + donor.getFullName() + "-"
						+ donor.getDonorId() + ".pdf");
				File receiptToDelete = new File(
						sourceFolder + "/Receipts/Receipt-" + donor.getFullName() + "-" + donor.getDonorId() + ".rtf");
				attachmentPart.attachFile(receipt);

				Multipart multipart = new MimeMultipart();
				multipart.addBodyPart(messageBodyPart);
				multipart.addBodyPart(attachmentPart);

				message.setContent(multipart);

				SMTPTransport transport = (SMTPTransport) session.getTransport("smtp");
				transport.connect("smtp.gmail.com", "finance@darululoomaustin.org", "Hafiz2020$");

				// Use the access token for authentication
				transport.issueCommand("AUTH XOAUTH2 " + generateOAuth2String("finance@darululoomaustin.org",
						"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", false), 235);

				try {
					if (receipt.exists()) {
						Transport.send(message);
						// move the receipt to sent folder
						Files.move(receipt.toPath(), sentReceipt.toPath(), StandardCopyOption.REPLACE_EXISTING);
						logger.info("{} sent to {}", receipt.getName(), donor.getEmail());

						if (receiptToDelete.exists()) {
							receiptToDelete.deleteOnExit();
						} else {
							logger.info("{} doesn't exist", receiptToDelete.getName());
						}
					} else {
						logger.info("{} doesn't exist", receipt.getName());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			System.out.println("Done");

		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String generateOAuth2String(String email, String accessToken, boolean base64Encode) {
		String authString = "user=" + email + "\001auth=Bearer " + accessToken + "\001\001";
		return base64Encode ? Base64.getEncoder().encodeToString(authString.getBytes()) : authString;
	}

	public static Set<String> listFilesUsingJavaIO(String dir) {
		return Stream.of(new File(dir).listFiles()).filter(file -> !file.isDirectory()).map(File::getName)
				.collect(Collectors.toSet());
	}

}
