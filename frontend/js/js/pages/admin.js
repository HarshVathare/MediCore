import {check, logout} from "../utills/auth.js";
import {getDashboard, createDoctor, getUsers, getDoctors, deleteUser, deleteDoctor} from "../apis/adminApi.js";

window.logout = logout;
check();

async function load() {
    const s = (await getDashboard()).data;
    stats.innerHTML = `
Patients: ${s.totalPatients}<br>
Doctors: ${s.totalDocters}<br>
Appointments: ${s.totalAppointments}<br>
Today's Appointments: ${s.todayAppointments}
`;
}

window.createDoctor = async () => {
    await createDoctor({
        name: dname.value,
        email: demail.value,
        password: dpass.value,
        specialization: dspec.value,
        experianceInYears: 0, // Assuming default
        availibility_stutus: "AVAILABLE"
    });
    alert("Doctor Created");
};

load();