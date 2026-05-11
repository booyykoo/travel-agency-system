// ── Toggle password visibility ──
document.querySelectorAll('.toggle-pw').forEach(btn => {
    btn.addEventListener('click', () => {
        const input = document.getElementById(btn.dataset.target);
        input.type = input.type === 'password' ? 'text' : 'password';
        btn.textContent = input.type === 'password' ? '👁' : '🙈';
    });
});

// ── Validation helpers ──
const emailRe = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

function validateEmail(v)    { return emailRe.test(v.trim()); }
function validatePassword(v) { return v.length >= 6; }

// ── Field references ──
const emailInput = document.getElementById('email');
const pwInput    = document.getElementById('password');
const form       = document.getElementById('loginForm');
const submitBtn  = document.getElementById('submitBtn');

// ── Helpers ──
function setError(id, visible) {
    document.getElementById(id).classList.toggle('visible', visible);
}
function setInputState(input, state) {
    input.classList.remove('valid', 'invalid');
    if (state) input.classList.add(state);
}
function checkForm() {
    submitBtn.disabled = !(validateEmail(emailInput.value) && validatePassword(pwInput.value));
}

function showServerError(msg) {
    const el = document.getElementById('serverError');
    if (!el) return;
    el.textContent = '⚠ ' + msg;
    el.classList.add('visible');
}
function hideServerError() {
    const el = document.getElementById('serverError');
    if (el) el.classList.remove('visible');
}

// ── Email ──
emailInput.addEventListener('input', () => {
    const ok      = validateEmail(emailInput.value);
    const touched = emailInput.value.length > 0;
    setInputState(emailInput, touched ? (ok ? 'valid' : 'invalid') : '');
    setError('emailError', touched && !ok);
    checkForm();
});

// ── Password ──
pwInput.addEventListener('input', () => {
    const ok      = validatePassword(pwInput.value);
    const touched = pwInput.value.length > 0;
    setInputState(pwInput, touched ? (ok ? 'valid' : 'invalid') : '');
    setError('passwordError', touched && !ok);
    checkForm();
});

// ── Submit → POST /api/auth/login ──
form.addEventListener('submit', async (e) => {
    e.preventDefault();
    if (!validateEmail(emailInput.value) || !validatePassword(pwInput.value)) return;

    hideServerError();
    submitBtn.classList.add('loading');
    submitBtn.disabled = true;

    const payload = {
        email:    emailInput.value.trim(),
        password: pwInput.value
    };

    try {
        const res  = await fetch('/api/auth/login', {
            method:  'POST',
            headers: { 'Content-Type': 'application/json' },
            body:    JSON.stringify(payload)
        });
        const data = await res.json();

        if (res.ok && data.success) {
            // Сохраняем JWT токен
            const token = data.data?.token;
            if (token) {
                if (document.getElementById('rememberMe')?.checked) {
                    localStorage.setItem('auth_token', token);
                } else {
                    sessionStorage.setItem('auth_token', token);
                }
            }
            showSuccess();
            // Редирект на главную через 1.5 секунды
            setTimeout(() => { window.location.href = '/'; }, 1500);

        } else {
            submitBtn.classList.remove('loading');
            submitBtn.disabled = false;

            // Различаем ошибки по HTTP статусу
            if (res.status === 403) {
                // EMAIL_NOT_VERIFIED
                showServerError('Please verify your email before signing in. Check your inbox.');
            } else if (res.status === 401) {
                // INVALID_CREDENTIALS
                showServerError('Invalid email or password. Please try again.');
            } else {
                showServerError(data.message || 'Something went wrong. Please try again.');
            }
        }
    } catch (err) {
        submitBtn.classList.remove('loading');
        submitBtn.disabled = false;
        showServerError('Network error. Please check your connection and try again.');
    }
});

function showSuccess() {
    document.getElementById('formContent').classList.add('hidden');
    document.getElementById('successOverlay').classList.add('visible');
}