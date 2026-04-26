import {register, googleRegister, microsoftRegister} from "../apis/authApi.js";

regForm.onsubmit=async e=>{
e.preventDefault();
await register({
name:name.value,
email:email.value,
password:password.value,
age:age.value,
gender:gender.value,
medicalHistory:medicalHistory.value
});
alert("Registered");
location.href="login.html";
};

// Google OAuth for Register
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
        await googleRegister({ token: response.credential });
        alert("Registered with Google");
        location.href="login.html";
    } catch (err) {
        alert("Google registration failed");
    }
}

// Microsoft OAuth for Register
const msalConfig = {
    auth: {
        clientId: 'YOUR_MICROSOFT_CLIENT_ID', // Replace with actual client ID
        authority: 'https://login.microsoftonline.com/common',
        redirectUri: window.location.origin + '/register.html',
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
        await microsoftRegister({ token: response.accessToken });
        alert("Registered with Microsoft");
        location.href="login.html";
    } catch (err) {
        alert("Microsoft registration failed");
        console.error(err);
    }
};