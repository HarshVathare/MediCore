package com.withHarsh.MediCore.RabbitMQ;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentEmailEventDTO {

    private Long appointmentId;
    private String patientName;
    private String patientEmail;
    private String doctorName;
    private String appointmentTime;
    private String status;
}



// package com.withHarsh.MediCore.RabbitMQ;

// import lombok.AllArgsConstructor;
// import lombok.Data;
// import lombok.NoArgsConstructor;

// @Data
// @AllArgsConstructor
// @NoArgsConstructor
// public class AppointmentEmailEventDTO {

//     private Long appointmentId;
//     private String patientName;
//     private String patientEmail;
//     private String doctorName;
//     private String appointmentTime;
//     private String status; // PENDING / CONFIRMED / CANCELLED
// }
