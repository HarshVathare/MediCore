import {check, logout} from "../utills/auth.js";
import {getPatientProfile, updatePatientProfile, getDoctors, bookAppointment, getPatientAppointments, cancelAppointment, changePatientPassword} from "../apis/patientApi.js";

window.logout = logout;
check();

const profileDetails = document.getElementById("profileDetails");
const profileUpdateContainer = document.getElementById("profileUpdateContainer");
const patientNamePreview = document.getElementById("patientNamePreview");
const appointmentCount = document.getElementById("appointmentCount");
const doctorList = document.getElementById("doctorList");
const doctorSelect = document.getElementById("doctorSelect");
const appointmentsContainer = document.getElementById("appointments");
const date = document.getElementById("date");
const bookForm = document.getElementById("bookForm");
const passwordForm = document.getElementById("passwordForm");
const oldPassword = document.getElementById("oldPassword");
const newPassword = document.getElementById("newPassword");
const sidebarNav = document.querySelector(".sidebar-nav");
const contentSections = document.querySelectorAll(".content-section");

let currentSection = "profile-section";

// Function to show a section
function showSection(sectionId) {
    contentSections.forEach(section => {
        section.classList.remove("active");
    });
    const targetSection = document.getElementById(sectionId);
    if (targetSection) {
        targetSection.classList.add("active");
    }
    currentSection = sectionId;

    // Update active menu item
    document.querySelectorAll(".sidebar-nav a").forEach(link => {
        link.classList.remove("active");
        if (link.getAttribute("href") === `#${sectionId}`) {
            link.classList.add("active");
        }
    });
}

// Expose showSection to global scope for onclick handlers
window.showSection = showSection;

// Menu click handler
sidebarNav.addEventListener("click", e => {
    if (e.target.tagName === "A") {
        e.preventDefault();
        const sectionId = e.target.getAttribute("href").substring(1);
        showSection(sectionId);
    }
});

async function init() {
    const apiProfile = (await getPatientProfile()).data;
    const tokenProfile = getTokenProfile();
    renderProfile(apiProfile, tokenProfile);
    await loadDoctors();
    await loadAppointments();

    bookForm.addEventListener("submit", async e => {
        e.preventDefault();
        await bookAppointment({ docter_Id: doctorSelect.value, appointment_Date: date.value });
        await loadAppointments();
        alert("Appointment booked successfully.");
    });

    passwordForm.addEventListener("submit", async e => {
        e.preventDefault();
        await changePatientPassword({ oldPassword: oldPassword.value, newPassword: newPassword.value });
        passwordForm.reset();
        alert("Password updated successfully.");
    });

    // Show default section
    showSection(currentSection);
}

function parseJwt(token) {
    try {
        const base64Url = token.split('.')[1] || '';
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(atob(base64).split('').map(c => '%'+('00'+c.charCodeAt(0).toString(16)).slice(-2)).join(''));
        return JSON.parse(jsonPayload);
    } catch (error) {
        return {};
    }
}

function getTokenProfile() {
    const token = localStorage.getItem('accessToken');
    if (!token) return {};
    const payload = parseJwt(token);
    return {
        name: payload.name || payload.fullName || payload.username || '',
        email: payload.email || payload.sub || '',
        age: payload.age || payload.patientAge || '',
        gender: payload.gender || '',
        medicalRecord: payload.medicalRecord || payload.medical_record || ''
    };
}

function renderProfile(apiProfile, tokenProfile) {
    const profile = {
        name: tokenProfile.name || apiProfile.name || 'Unknown',
        email: tokenProfile.email || apiProfile.email || 'Unknown',
        age: tokenProfile.age || apiProfile.age || 'Unknown',
        gender: tokenProfile.gender || apiProfile.gender || 'Unknown',
        medicalRecord: tokenProfile.medicalRecord || apiProfile.medicalRecord || 'No records available.',
        role: apiProfile.role || 'Patient',
        createdAt: apiProfile.createdAt || ''
    };

    patientNamePreview.textContent = profile.name;
    profileDetails.innerHTML = `
        <div class="profile-row"><span>Name</span><strong>${profile.name}</strong></div>
        <div class="profile-row"><span>Email</span><strong>${profile.email}</strong></div>
        <div class="profile-row"><span>Age</span><strong>${profile.age}</strong></div>
        <div class="profile-row"><span>Gender</span><strong>${profile.gender}</strong></div>
        <div class="profile-row"><span>Role</span><strong>${profile.role}</strong></div>
        <div class="profile-row"><span>Joined</span><strong>${profile.createdAt ? new Date(profile.createdAt).toLocaleDateString() : 'Unknown'}</strong></div>
        <div class="profile-row full-row"><span>Medical Record</span><p>${profile.medicalRecord}</p></div>
    `;

    renderProfileUpdateForm(profile, tokenProfile);
}

function renderProfileUpdateForm(profile, tokenProfile) {
    const missingFields = [
        { key: 'name', label: 'Name', value: tokenProfile.name || profile.name },
        { key: 'email', label: 'Email', value: tokenProfile.email || profile.email },
        { key: 'age', label: 'Age', value: tokenProfile.age || profile.age },
        { key: 'gender', label: 'Gender', value: tokenProfile.gender || profile.gender },
        { key: 'medicalRecord', label: 'Medical Record', value: tokenProfile.medicalRecord || profile.medicalRecord }
    ].filter(field => !field.value || field.value === 'Unknown' || field.value === 'No records available.');

    if (!profileUpdateContainer) return;

    if (!missingFields.length) {
        profileUpdateContainer.innerHTML = `
            <div class="profile-update-card">
                <p>All profile fields are complete and read-only from your JWT token.</p>
            </div>
        `;
        return;
    }

    const inputs = missingFields.map(field => `
        <label for="${field.key}">${field.label}</label>
        <input id="${field.key}" name="${field.key}" placeholder="Enter ${field.label}" />
    `).join('');

    profileUpdateContainer.innerHTML = `
        <div class="profile-update-card">
            <h3>Complete missing profile fields</h3>
            <p>Only fields that are missing from your JWT can be added here.</p>
            <form id="profileUpdateForm">
                ${inputs}
                <button type="submit" class="primary-btn">Save Missing Fields</button>
            </form>
        </div>
    `;

    const profileUpdateForm = document.getElementById('profileUpdateForm');
    profileUpdateForm.addEventListener('submit', async e => {
        e.preventDefault();
        const updatePayload = {};
        missingFields.forEach(field => {
            const input = document.getElementById(field.key);
            if (input && input.value.trim()) {
                updatePayload[field.key] = input.value.trim();
            }
        });

        if (!Object.keys(updatePayload).length) {
            return alert('Please enter values for the missing fields before saving.');
        }

        await updatePatientProfile(updatePayload);
        alert('Missing profile fields saved successfully.');
        const refreshedProfile = (await getPatientProfile()).data;
        renderProfile(refreshedProfile, getTokenProfile());
    });
}

async function loadDoctors() {
    const docs = (await getDoctors()).data;
    doctorList.innerHTML = "";
    doctorSelect.innerHTML = "";

    docs.forEach(doc => {
        doctorSelect.innerHTML += `<option value=${doc.id}>${doc.name} (${doc.specialization})</option>`;
        doctorList.innerHTML += `
            <article class="doctor-card">
                <h4>${doc.name}</h4>
                <p>${doc.specialization}</p>
                <div class="doctor-meta">
                    <span>${doc.experianceInYears} yrs exp</span>
                    <span>${doc.availibility_stutus || "Available"}</span>
                </div>
            </article>
        `;
    });
}

async function loadAppointments() {
    const appointments = (await getPatientAppointments()).data;
    appointmentCount.textContent = appointments.length;
    appointmentsContainer.innerHTML = "";

    if (!appointments.length) {
        appointmentsContainer.innerHTML = `<div class="empty-state">No upcoming appointments yet.</div>`;
        return;
    }

    appointments.forEach(item => {
        appointmentsContainer.innerHTML += `
            <article class="appointment-card">
                <div>
                    <h4>${item.docter?.name || "Doctor"}</h4>
                    <p>${new Date(item.appointment_Date).toLocaleDateString()}</p>
                    <span class="status ${item.status.toLowerCase()}">${item.status}</span>
                </div>
                <button class="link-btn" onclick="cancelAppointmentHandler(${item.appointment_Id})">Cancel</button>
            </article>
        `;
    });
}

window.cancelAppointmentHandler = async id => {
    await cancelAppointment(id);
    await loadAppointments();
};

init();