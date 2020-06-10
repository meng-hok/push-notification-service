package com.kosign.push.helper;

import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;

public class MailHelper {

    static private String SMTP_HOST = "smtp.kosign.com.kh";
    static private Integer SMTP_PORT = 25;
    static private String FROM_ADDRESS = "noreply@bnkcmfi.com";
    static private String FROM_NAME = "BNK";

    static {
        //TODO: read from config resource
//        if(JexSystemConfig.get("wabooks/mail", "smtpHost") != null) SMTP_HOST = JexSystemConfig.get("wabooks/mail", "smtpHost");
//        if(JexSystemConfig.get("wabooks/mail", "smtpPort") != null) SMTP_HOST = JexSystemConfig.get("wabooks/mail", "smtpPort");
//        if(JexSystemConfig.get("wabooks/mail", "fromAddress") != null) FROM_ADDRESS = JexSystemConfig.get("wabooks/mail", "fromAddress");
//        if(JexSystemConfig.get("wabooks/mail", "fromName") != null) FROM_NAME = JexSystemConfig.get("wabooks/mail", "fromName");
    }

    /*
    private static MultiPartEmail createMultiPartEmail() throws Exception {
        MultiPartEmail email = new MultiPartEmail();
        email.setHostName(SMTP_HOST);
        //email.setAuthentication(USERNAME, PASSWORD);
        email.setDebug(true);
        email.setSmtpPort(25);
        //email.setSSLOnConnect(true);
        email.setFrom(FROM_ADDRESS, FROM_NAME);
        return email;
    }

    private static SimpleEmail createSimpleEmail() throws Exception {
        SimpleEmail email = new SimpleEmail();
        email.setHostName(SMTP_HOST);
        //email.setAuthentication(USERNAME, PASSWORD);
        email.setDebug(true);
        email.setSmtpPort(25);
        //email.setSSLOnConnect(true);
        email.setFrom(FROM_ADDRESS, FROM_NAME);
        return email;
    }
    */

    public static void send(Email email) {
		Mailer mailer = MailerBuilder
				.withSMTPServerHost(SMTP_HOST)
				.withSMTPServerPort(SMTP_PORT)
//				.async()
				.buildMailer();

		email = EmailBuilder.copying(email)
				.from(FROM_NAME, FROM_ADDRESS)
				.buildEmail();

		mailer.sendMail(email);
    }
}
