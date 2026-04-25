import {check,logout} from "../utils/auth.js";
import {getAppointments,updateStatus} from "../api/doctorApi.js";

window.logout=logout;
check();

async function load(){
const data=(await getAppointments()).data;

appointments.innerHTML="";
data.forEach(a=>{
appointments.innerHTML+=`
<div>
${a.name} - ${a.status}
<button onclick="update(${a.appointment_Id})">Approve</button>
</div>`;
});
}

window.update=async id=>{
await updateStatus(id,{appointment_status:"APPROVED"});
load();
};

load();