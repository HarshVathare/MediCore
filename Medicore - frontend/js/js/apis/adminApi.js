import API from "./axios.js";

export const createDoctor = d => API.post("/api/admin/docter", d);
export const getUsers = () => API.get("/api/admin/users");
export const getDoctors = () => API.get("/api/admin/docters");
export const getDashboard = () => API.get("/api/admin/dashboard");
export const deleteUser = id => API.delete(`/api/admin/user/${id}`);
export const deleteDoctor = id => API.delete(`/api/admin/docter/${id}`);