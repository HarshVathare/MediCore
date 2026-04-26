import API from "./axios.js";

export const getPatientProfile = () => API.get("/api/patients/profile");
export const updatePatientProfile = d => API.put("/api/patients/profile", d);
export const changePatientPassword = d => API.put("/api/patients/change-password", d);
export const getPatientReport = id => API.get(`/api/patients/${id}/report`);
export const createPatientReport = (id, d) => API.post(`/api/patients/${id}/report`, d);
export const getPatientAppointments = () => API.get("/api/patients/appointments");
export const bookAppointment = d => API.post("/api/patients/appointments", d);
export const getDoctors = () => API.get("/api/patients/docters");
export const getDoctorById = id => API.get(`/api/patients/docters/${id}`);
export const cancelAppointment = id => API.delete(`/api/patients/appointments/${id}`);