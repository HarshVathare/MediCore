export const save=(d)=>localStorage.setItem("accessToken",d.accessToken);
export const logout=()=>{localStorage.clear();location.href="login.html"};
export const check=()=>{if(!localStorage.getItem("accessToken"))location.href="login.html"};