package com.eoss.appengine.helper;

import com.eoss.sendgrid.Sendgrid;

public class SendEmail {

	public void send(String text, String subject, String html, String sendTo, String from) {
		Sendgrid mail = new Sendgrid(System.getenv("senGridUser"),System.getenv("senGridPass"));
		mail.setTo(sendTo).setFrom(from).setText(text).setSubject(subject).setHtml(html);
		mail.send();
	}
}
