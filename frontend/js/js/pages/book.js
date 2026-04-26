import { getDoctors, bookAppointment } from "../apis/patientApi.js";
import { check } from "../utills/auth.js";

check();

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
  window.location.href = "login.html";
}