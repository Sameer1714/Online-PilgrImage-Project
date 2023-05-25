package com.mytrip.service;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.mytrip.entities.Email;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender javaMailSender;

	public void sendEmail(Email email) {
		try {

			MimeMessage mimeMessage = javaMailSender.createMimeMessage();

			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

			helper.setTo(email.getTo());
			helper.setSubject(email.getSubject());
			helper.setText(email.getMessage(), true);

			javaMailSender.send(mimeMessage);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
