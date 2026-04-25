import API from "./axios.js";

export const getAppointments=()=>API.get("/docters/appointments");
export const updateStatus=(id,d)=>API.put(`/docters/appointments/${id}/status`,d);