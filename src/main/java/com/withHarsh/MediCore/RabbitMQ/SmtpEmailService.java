package com.withHarsh.MediCore.RabbitMQ;

import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class SmtpEmailService {

    private final JavaMailSender mailSender;

    public SmtpEmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String subject, AppointmentEmailEventDTO event) {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("medicore.owner@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);

            String htmlContent = """
                <html>
                <body style="font-family: Arial;">

                    <div style="text-align:center;">
                        <img src='cid:logoImage' width='150'/>
                        <h2 style="color:%s;">Appointment %s ..!</h2>
                    </div>

                    <p>Dear %s,</p>

                    <p>Your appointment is <b>%s</b>.</p>

                    <h3>🩺 Appointment Details:</h3>
                    <ul>
                        <li><b>Appointment ID:</b> %s</li>
                        <li><b>Doctor:</b> %s</li>
                        <li><b>Date & Time:</b> %s</li>
                        <li><b>Status:</b> %s</li>
                    </ul>

                    <p>Thank you for choosing <b>MediCore</b> 💙</p>

                    <br>
                    <p>Best Regards,<br>MediCore Team</p>

                </body>
                </html>
                """.formatted(
                    getColor(event.getStatus()),
                    event.getStatus(),
                    event.getPatientName(),
                    event.getStatus(),
                    event.getAppointmentId(),
                    event.getDoctorName(),
                    event.getAppointmentTime(),
                    event.getStatus()
            );

            helper.setText(htmlContent, true);

            ClassPathResource image = new ClassPathResource("static/Medicore.png");
            helper.addInline("logoImage", image);

            mailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getColor(String status) {
        return switch (status) {
            case "CONFIRMED" -> "green";
            case "CANCELLED" -> "red";
            default -> "orange";
        };
    }
}