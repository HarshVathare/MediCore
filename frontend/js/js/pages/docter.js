import {check, logout} from "../utills/auth.js";
import {getDoctorAppointments, updateAppointmentStatus, createMedicalRecord} from "../apis/docterApi.js";

window.logout = logout;
check();

async function load() {
    const data = (await getDoctorAppointments()).data;

    appointments.innerHTML = "";
    data.forEach(a => {
        appointments.innerHTML += `
<div>
${a.name} - ${a.status}
<button onclick="update(${a.appointment_Id})">Approve</button>
<button onclick="addRecord(${a.appointment_Id})">Add Medical Record</button>
</div>`;
    });
}

window.update = async id => {
    await updateAppointmentStatus(id, { appointment_status: "APPROVED" });
    load();
};

window.addRecord = async id => {
    // Simple prompt for medical record
    const diagnosis = prompt("Diagnosis:");
    const prescription = prompt("Prescription:");
    const notes = prompt("Notes:");
    if (diagnosis && prescription && notes) {
        await createMedicalRecord(id, { diagnosis, prescription, notes });
        alert("Medical record added");
    }
};

load();