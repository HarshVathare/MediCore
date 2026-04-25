import API from "./axios.js";
export const login=d=>API.post("/auth/login",d);
export const register=d=>API.post("/auth/register",d);