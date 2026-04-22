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


    public void sendEmailForVerifyAccount(String to, String subject, String verificationLink) {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("medicore.owner@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);

            String htmlContent = """
            <html>
            <body style="font-family: Arial, sans-serif; background-color:#f4f6f8; padding:20px;">

                <div style="max-width:600px; margin:auto; background:white; padding:20px; border-radius:10px;">

                    <div style="text-align:center;">
                        <img src='cid:logoImage' width='120'/>
                        <h2 style="color:#2c7be5;">Verify Your Account</h2>
                    </div>

                    <p>Dear User,</p>

                    <p>Welcome to <b>MediCore</b> 🏥</p>

                    <p>Please click the button below to verify your email address:</p>

                    <div style="text-align:center; margin:30px 0;">
                        <a href="%s" 
                           style="background-color:#28a745; color:white; padding:12px 20px; 
                                  text-decoration:none; border-radius:5px; font-weight:bold;">
                            Verify Account
                        </a>
                    </div>

                    <p>If the button doesn’t work, copy and paste this link:</p>
                    <p style="word-break:break-all;">%s</p>

                    <p>This link will expire in <b>24 hours</b>.</p>

                    <hr>

                    <p style="font-size:12px; color:gray;">
                        If you did not create this account, you can ignore this email.
                    </p>

                    <p>Best Regards,<br><b>MediCore Team 💙</b></p>

                </div>

            </body>
            </html>
            """.formatted(verificationLink, verificationLink);

            helper.setText(htmlContent, true);

            ClassPathResource image = new ClassPathResource("static/Medicore.png");
            helper.addInline("logoImage", image);

            mailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendEmailForForgotPassword(String to, String subject, String resetLink) {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("medicore.owner@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);

            String htmlContent = """
        <html>
        <body style="font-family: Arial, sans-serif; background-color:#f4f6f8; padding:20px;">

            <div style="max-width:600px; margin:auto; background:white; padding:20px; border-radius:10px;">

                <div style="text-align:center;">
                    <img src='cid:logoImage' width='120'/>
                    <h2 style="color:#dc3545;">Reset Your Password</h2>
                </div>

                <p>Dear User,</p>

                <p>We received a request to reset your password for your <b>MediCore</b> 🏥 account.</p>

                <p>Click the button below to reset your password:</p>

                <div style="text-align:center; margin:30px 0;">
                    <a href="%s" 
                       style="background-color:#dc3545; color:white; padding:12px 20px; 
                              text-decoration:none; border-radius:5px; font-weight:bold;">
                        Reset Password
                    </a>
                </div>

                <p>If the button doesn’t work, copy and paste this link:</p>
                <p style="word-break:break-all;">%s</p>

                <p>This link will expire in <b>15–30 minutes</b> for security reasons.</p>

                <hr>

                <p style="font-size:12px; color:gray;">
                    If you did not request a password reset, you can safely ignore this email.
                </p>

                <p>Best Regards,<br><b>MediCore Team 💙</b></p>

            </div>

        </body>
        </html>
        """.formatted(resetLink, resetLink);

            helper.setText(htmlContent, true);

            ClassPathResource image = new ClassPathResource("static/Medicore.png");
            helper.addInline("logoImage", image);

            mailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}