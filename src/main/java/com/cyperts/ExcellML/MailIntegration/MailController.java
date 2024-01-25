package com.cyperts.ExcellML.MailIntegration;

import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mail")
public class MailController {
	@Autowired
	MailIntegration mailIntegration;

	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("/sendEmail")	
	public String sendEmail(@RequestBody MailRequest mailRequest) {

		String from = mailRequest.getEmailFrom();

		String to = mailRequest.getEmailTo();

		RandomStringGenerator generator = new RandomStringGenerator.Builder().withinRange('0', '9').build();
		String generate = generator.generate(6);
		String subject = "Welcome to Excel- ML";
		String msg = "Dear user,\nYour OTP is: " + generate;
		mailRequest.setOtp(generate);
		System.out.println("OTP::" + generate);
		mailIntegration.sendMail(msg, subject, to, from, mailRequest);
		return "Mail sent successfullyy...!!!";
	}
}
