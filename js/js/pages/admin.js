import {check,logout} from "../utils/auth.js";
import {getStats,createDoc} from "../api/adminApi.js";

window.logout=logout;
check();

async function load(){
const s=(await getStats()).data;
stats.innerHTML=`
Patients:${s.totalPatients}<br>
Doctors:${s.totalDocters}<br>
Appointments:${s.totalAppointments}
`;
}

window.createDoctor=async ()=>{
await createDoc({
name:dname.value,
email:demail.value,
password:dpass.value,
specialization:dspec.value
});
alert("Doctor Created");
};

load();