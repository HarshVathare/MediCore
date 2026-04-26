import {login, googleLogin, microsoftLogin} from "../apis/authApi.js";
import {save} from "../utills/auth.js";

loginForm.onsubmit=async e=>{
e.preventDefault();
const res=await login({email:email.value,password:password.value});
save(res.data);
location.href="dashboard.html";
};

// Forgot Password
forgotPassword.onclick = () => {
    // Implement forgot password logic, e.g., redirect to forgot password page or show modal
    alert("Forgot password functionality to be implemented");
};

// Google OAuth
window.onload = function() {
    google.accounts.id.initialize({
        client_id: 'YOUR_GOOGLE_CLIENT_ID', // Replace with actual client ID
        callback: handleGoogleCredentialResponse
    });
    // Removed renderButton, using custom button
};

googleSignInButton.onclick = () => {
    google.accounts.id.prompt();
};

async function handleGoogleCredentialResponse(response) {
    try {
        const res = await googleLogin({ token: response.credential });
        save(res.data);
        location.href="dashboard.html";
    } catch (err) {
        error.textContent = "Google login failed";
    }
}

// Microsoft OAuth
const msalConfig = {
    auth: {
        clientId: 'YOUR_MICROSOFT_CLIENT_ID', // Replace with actual client ID
        authority: 'https://login.microsoftonline.com/common',
        redirectUri: window.location.origin + '/login.html',
    },
    cache: {
        cacheLocation: 'sessionStorage',
        storeAuthStateInCookie: false,
    },
};

const msalInstance = new msal.PublicClientApplication(msalConfig);

microsoftSignInButton.onclick = async () => {
    try {
        const response = await msalInstance.loginPopup({
            scopes: ['user.read']
        });
        const res = await microsoftLogin({ token: response.accessToken });
        save(res.data);
        location.href="dashboard.html";
    } catch (err) {
        error.textContent = "Microsoft login failed";
        console.error(err);
    }
};