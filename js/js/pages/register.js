import {register} from "../api/authApi.js";

regForm.onsubmit=async e=>{
e.preventDefault();
await register({
name:name.value,
email:email.value,
password:password.value,
age:age.value,
gender:gender.value
});
alert("Registered");
location.href="login.html";
};