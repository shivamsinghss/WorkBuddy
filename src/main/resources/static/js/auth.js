/* ===== WorkBuddy Auth (shared across pages) ===== */
const AUTH_KEY  = 'workbuddy_user';
const API_BASE  = 'https://workbuddy-gdzp.onrender.com';

function getUser() {
    try { return JSON.parse(localStorage.getItem(AUTH_KEY)); } catch(e) { return null; }
}

function setUser(user) {
    localStorage.setItem(AUTH_KEY, JSON.stringify(user));
}

function signOut() {
    localStorage.removeItem(AUTH_KEY);
    location.reload();
}

function getInitials(name) {
    return (name || '?').split(' ').filter(Boolean).map(n => n[0]).join('').toUpperCase().substring(0, 2);
}

/* ---- Modal ---- */
function openSignInModal() {
    document.getElementById('auth-modal').classList.add('open');
    showStep('step-provider');
}

function closeSignInModal() {
    document.getElementById('auth-modal').classList.remove('open');
}

function showStep(id) {
    document.querySelectorAll('.modal-step').forEach(s => s.style.display = 'none');
    document.getElementById(id).style.display = 'block';
}

function signInWithGoogle() {
    showStep('step-loading');
    document.getElementById('loading-text').textContent = 'Connecting to Google...';
    setTimeout(() => {
        setUser({ name: 'Google User', email: 'user@gmail.com', avatar: 'GU', provider: 'Google' });
        location.reload();
    }, 1400);
}

function signInWithApple() {
    showStep('step-loading');
    document.getElementById('loading-text').textContent = 'Connecting to Apple...';
    setTimeout(() => {
        setUser({ name: 'Apple User', email: 'user@icloud.com', avatar: 'AU', provider: 'Apple' });
        location.reload();
    }, 1400);
}

function showEmailStep() { showStep('step-email'); }
function showPhoneStep()  { showStep('step-phone'); }
function showProviderStep() { showStep('step-provider'); }

function submitEmailSignIn() {
    const usernameEl = document.getElementById('email-username');
    const passEl     = document.getElementById('email-password');
    let valid = true;
    [usernameEl, passEl].forEach(el => {
        el.classList.remove('error');
        if (!el.value.trim()) { el.classList.add('error'); valid = false; }
    });
    if (!valid) return;

    showStep('step-loading');
    document.getElementById('loading-text').textContent = 'Waking up server… this may take up to 50 seconds on first request.';

    fetch(API_BASE + '/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username: usernameEl.value.trim(), password: passEl.value.trim() }),
        signal: AbortSignal.timeout(90000)
    })
    .then(r => r.json())
    .then(data => {
        if (data.success) {
            setUser({
                id:         data.id,
                name:       data.name,
                email:      data.email,
                role:       data.role,
                avatar:     data.avatar,
                department: data.department,
                position:   data.position,
                provider:   'Email'
            });
            closeSignInModal();
            location.reload();
        } else {
            showStep('step-email');
            usernameEl.classList.add('error');
            passEl.classList.add('error');
            alert(data.message || 'Login failed');
        }
    })
    .catch(() => {
        showStep('step-email');
        alert('Server is taking too long to respond. Please wait a moment and try again.');
    });
}

function submitPhoneSignIn() {
    const nameEl  = document.getElementById('phone-name');
    const phoneEl = document.getElementById('phone-number');
    let valid = true;
    [nameEl, phoneEl].forEach(el => {
        el.classList.remove('error');
        if (!el.value.trim()) { el.classList.add('error'); valid = false; }
    });
    if (!valid) return;
    setUser({ name: nameEl.value.trim(), phone: phoneEl.value.trim(),
              avatar: getInitials(nameEl.value.trim()), provider: 'Phone' });
    closeSignInModal();
    location.reload();
}

/* ---- Navbar auth init ---- */
function initNavAuth() {
    const user      = getUser();
    const btnSignin = document.getElementById('btn-signin');
    const profSec   = document.getElementById('profile-section');

    if (user) {
        btnSignin.style.display = 'none';
        profSec.style.display   = 'flex';
        document.getElementById('nav-avatar').textContent    = user.avatar;
        document.getElementById('nav-name').textContent      = user.name;
        document.getElementById('nav-full-name').textContent = user.name;
        document.getElementById('nav-email').textContent     = user.email || user.phone || '';
    } else {
        btnSignin.style.display = 'flex';
        profSec.style.display   = 'none';
    }

    /* Profile dropdown */
    const profileBtn      = document.getElementById('profile-btn');
    const profileDropdown = document.getElementById('profile-dropdown');
    if (profileBtn) {
        profileBtn.addEventListener('click', function(e) {
            e.stopPropagation();
            const open = profileDropdown.classList.toggle('open');
            profileBtn.classList.toggle('open', open);
        });
        profileDropdown.addEventListener('click', e => e.stopPropagation());
        document.addEventListener('click', () => {
            profileDropdown.classList.remove('open');
            profileBtn.classList.remove('open');
        });
    }
}

/* ---- Modal overlay click to close ---- */
document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('auth-modal').addEventListener('click', function(e) {
        if (e.target === this) closeSignInModal();
    });
});
