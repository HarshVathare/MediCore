package com.withHarsh.MediCore.RabbitMQ;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageConsumer {

    private final SmtpEmailService emailService;

    public MessageConsumer(SmtpEmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = {"${rabbitmq.email.queue.name}"})
    public void consume(AppointmentEmailEventDTO event) {
        try {
            log.info("Received message -> {}", event);

            emailService.sendEmail(
                    event.getPatientEmail(),
                    "🏥 Appointment " + event.getStatus(),
                    event
            );

        } catch (Exception e) {
            log.error("Error consuming message", e);
        }
    }
}