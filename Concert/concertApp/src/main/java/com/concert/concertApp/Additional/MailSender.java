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
        String to = reservation.getUser().getEmail();

        String from = "gtconcert@gmail.com";

        String host = "smtp.gmail.com";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");


        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(from));

            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));

            message.setSubject("Вашата резервация");
            
            message.setText(
                    "--------------------------Резервацията е на името на: "+ reservation.getUser().getFirstName() +" "+ reservation.getUser().getLastName() + "--------------------------"+"\n"
                            +"---------------------------------------{Брой билети : "+ reservation.getReservationTickets()+ " --------------------------"+"\n"
                   +"--------------------------------------- първоначална цена: " + Integer.parseInt(reservation.getReservationTickets())+"*"+ Double.parseDouble(reservation.getConcert().getPrice())+"="+Integer.parseInt(reservation.getReservationTickets())*Double.parseDouble(reservation.getConcert().getPrice())+"лв "+ "--------------------------"+"\n"
                            +"--------------------------------------- Отстъпка: " + reservation.getDiscount().getDiscountName()+" ("+reservation.getDiscount().getDiscountPercentage()+"%) "+"--------------------------"+"\n"
                            +"--------------------------------------- Крайна Цена: " +reservation.getReservationFinalPrice()+"лв }"+ "--------------------------"+"\n"
                            +"--------------------------------------- Концерт: " + reservation.getConcert().getTitle() +" "+ "--------------------------"+"\n"
                            +"--------------------------------------- Зала: " + reservation.getConcert().getHall().getConHallName() +" "+ "--------------------------"+"\n"
                            +"--------------------------------------- Ще Ви очакваме на : " + reservation.getConcert().getDate() +" "+ "--------------------------"+"\n"

            );
            Transport.send(message);
        } catch (Exception e) {
           throw new RuntimeException("Error accures while sending the email " + e.getMessage());
        }
    }
}

