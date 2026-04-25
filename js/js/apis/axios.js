import axios from "https://cdn.jsdelivr.net/npm/axios@1.6.7/+esm";

const API=axios.create({baseURL:"http://localhost:8080/api"});

API.interceptors.request.use(c=>{
const t=localStorage.getItem("accessToken");
if(t) c.headers.Authorization=`Bearer ${t}`;
return c;
});

export default API;