package com.concert.concertApp.Additional;

import com.concert.concertApp.entities.Reservation;
import org.springframework.http.ResponseEntity;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


public class MailSender {

   private static final String username = "gtconcert@gmail.com";
  private  static final String password = "123456789lni";

    public static void sendEmail(Reservation reservation) {
        // Add recipient
        String to = "luybomir2001@abv.bg";
        String toUserEmail = reservation.getUser().getEmail();
            // nelina.jeleva1@gmail.com
        //ageorgieva239@gmail.com
        // Add sender
        String from = "gtconcert@gmail.com";

        String host = "smtp.gmail.com";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        // Get the Session object
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            // Create a default MimeMessage object
            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(from));

            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));

            // Set Subject
            message.setSubject("Вашата резервация");

            // Put the content of your message
            message.setText("Резервацията е на името на : " + reservation.getUser().getFirstName() +" "+ reservation.getUser().getLastName() +"\n"
                    + "Заплатихте :"+reservation.getReservationFinalPrice()+"лв"+"\n"
                    +"Концерт :" + reservation.getConcert().getTitle() +"\n"
                    +"Зала " + reservation.getConcert().getHall().getConHallName() +"\n"
                   + "Ще Ви очакваме на " + reservation.getConcert().getDate()+"\n"
            );

            // Send message
            Transport.send(message);

         //   System.out.println("Sent message successfully....");

        } catch (Exception e) {
           throw new RuntimeException("Error accures while sending the email " + e.getMessage());
        }
    }
}

