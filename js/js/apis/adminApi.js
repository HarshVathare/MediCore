import API from "./axios.js";

export const getStats=()=>API.get("/admin/dashboard");
export const createDoc=d=>API.post("/admin/docter",d);