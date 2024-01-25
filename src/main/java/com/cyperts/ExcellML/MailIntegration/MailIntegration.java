package com.cyperts.ExcellML.MailIntegration;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MailIntegration {

	@Autowired
	MailRequest mailRequest;

	@Autowired
	MailRequestRepository mailRequestRepository;

	public String sendMail(String msg, String emailSubject, String emailTo, String emailFrom, MailRequest mailRequest) {

		// get the system properties
		Properties properties = System.getProperties();
		System.out.println("Properties :" + properties);
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.socketFactory.port", "465");
		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		properties.put("mail.smtp.socketFactory.fallback", "false");

		Session session = Session.getInstance(properties, new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("techinfoo26@gmail.com", "sdwc wsma eqnz ynle");
			}

		});
		session.setDebug(true);

		// 2.compose message [text , multi media]
		try {
			MimeMessage m = new MimeMessage(session);
			m.setFrom(emailFrom);

			// adding recipient to message
			m.addRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));
			// adding subject to message
			m.setSubject(emailSubject);
			// adding text to message

			m.setText(msg);
			// 3.Send the message using transport class

			Transport.send(m);
			System.out.println("Sent success............");

		} catch (MessagingException e) {
			e.printStackTrace();
		}
		mailRequest.setEmailFrom("techinfoo26@gmail.com");
		mailRequest.setEmailSubject(emailSubject);
		mailRequest.setEmailTo(emailTo);
		mailRequest.setMsg(msg);

		System.out.println("Mail Request Object Set:::::::::::::");
		mailRequestRepository.save(mailRequest);
		System.out.println(mailRequest.toString());
		return "Sent success............";
	}
}
