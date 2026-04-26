import API from "./axios.js";

export const getDoctorProfile = () => API.get("/api/docters/profile");
export const updateDoctorProfile = d => API.put("/api/docters/profile", d);
export const updateAppointmentStatus = (id, d) => API.put(`/api/docters/appointments/${id}/status`, d);
export const createMedicalRecord = (appointmentId, d) => API.post(`/api/docters/${appointmentId}/medical-record`, d);
export const searchDoctors = () => API.get("/api/docters/search");
export const getMedicalRecord = patientId => API.get(`/api/docters/medical-record/${patientId}`);
export const getDoctorAppointments = () => API.get("/api/docters/appointments");