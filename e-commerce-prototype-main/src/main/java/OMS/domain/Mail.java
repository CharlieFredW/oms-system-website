package OMS.domain;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class Mail {
    private final String email;
    private final String subject;
    private final String message;

    public Mail(String email, String subject, String message) {
        this.email = email;
        this.subject = subject;
        this.message = message;
    }

    public boolean sendMail() {
        String username = "arneelectronics@yeetmail.dk";
        String password = "arneelectronics";

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "send.one.com");
        prop.put("mail.smtp.port", "465");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.socketFactory.port", "465");
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        Session session = Session.getInstance(prop,
                new jakarta.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(username));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            msg.setSubject(subject);
            msg.setText(message);

            try {
                Transport.send(msg);
                return true;
            } catch (Exception e) {
//                e.printStackTrace();
                System.out.println("Email failed");
                return false;
            }
        } catch (MessagingException e) {
            System.out.println("Messaging exception");
//            e.printStackTrace();
            return false;
        }
    }

    public String getEmail() {
        return email;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return message;
    }


}
