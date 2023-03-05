package com.jwtauth.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class MailService {

	@Autowired
	private TemplateEngine templateEngine;

	@Autowired
	private JavaMailSender emailSender;

	public boolean sendHTMLMail(Context ctx, String htmlTemplateName, String toAddress, String subject) {

		final String htmlContent = this.templateEngine.process("html/" + htmlTemplateName + ".html", ctx);

		final MimeMessage mimeMessage = this.emailSender.createMimeMessage();
		final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");

		try {
			message.setFrom("no-reply@localhost.com");
			message.setTo(toAddress);
			message.setSubject(subject);
			message.setText(htmlContent, true);
		} catch (MessagingException e) {
			log.error("Unable to send mail {}", e);
		}

		this.emailSender.send(mimeMessage);
		return true;
	}

}
