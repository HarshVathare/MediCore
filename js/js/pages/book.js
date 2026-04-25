// js/pages/patient.js
import { getDoctors, bookAppointment } from "../api/patientApi.js";

async function loadDoctors() {
  const res = await getDoctors();
  const select = document.getElementById("doctorSelect");

  res.data.forEach((doc) => {
    const option = document.createElement("option");
    option.value = doc.id;
    option.textContent = `${doc.name} (${doc.specialization})`;
    select.appendChild(option);
  });
}

document.getElementById("bookBtn").addEventListener("click", async () => {
  const data = {
    docter_Id: document.getElementById("doctorSelect").value,
    appointment_Date: document.getElementById("date").value,
  };

  await bookAppointment(data);
  alert("Appointment booked!");
});

loadDoctors();

import { isLoggedIn } from "../utils/auth.js";

if (!isLoggedIn()) {
  window.location.href = "login.html";
}