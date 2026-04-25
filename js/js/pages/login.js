import {login} from "../api/authApi.js";
import {save} from "../utils/auth.js";

loginForm.onsubmit=async e=>{
e.preventDefault();
const res=await login({email:email.value,password:password.value});
save(res.data);
location.href="dashboard.html";
};