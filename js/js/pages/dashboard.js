import {check,logout} from "../utils/auth.js";
import {getProfile,getDoctors,book,getApp,del} from "../api/patientApi.js";

window.logout=logout;
check();

async function init(){
const p=(await getProfile()).data;
profile.innerHTML=`${p.name} (${p.email})`;

const docs=(await getDoctors()).data;
docs.forEach(d=>{
doctorSelect.innerHTML+=`<option value=${d.id}>${d.name}</option>`;
});

bookBtn.onclick=async ()=>{
await book({docter_Id:doctorSelect.value,appointment_Date:date.value});
loadApp();
};

loadApp();
}

async function loadApp(){
const a=(await getApp()).data;
appointments.innerHTML="";
a.forEach(x=>{
appointments.innerHTML+=`
<div>
${x.docter?.name} - ${x.status}
<button onclick="cancel(${x.appointment_Id})">X</button>
</div>`;
});
}

window.cancel=async id=>{
await del(id);
loadApp();
};

init();