import API from "./axios.js";

export const getProfile=()=>API.get("/patients/profile");
export const getDoctors=()=>API.get("/patients/docters");
export const book=d=>API.post("/patients/appointments",d);
export const getApp=()=>API.get("/patients/appointments");
export const del=id=>API.delete(`/patients/appointments/${id}`);