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
const phoneRe = /^[+]?[\d\s\-().]{7,20}$/;

function validateEmail(v)    { return emailRe.test(v.trim()); }
function validatePhone(v)    { return v === '' || phoneRe.test(v.trim()); }
function validatePasswordRules(v) {
    return {
        len:  v.length >= 8,
        num:  /\d/.test(v),
        spec: /[^a-zA-Z0-9]/.test(v)
    };
}
function passwordValid(v) {
    const r = validatePasswordRules(v);
    return r.len && r.num && r.spec;
}
function confirmValid() { return pw.value === cpw.value; }

// ── Field references ──
const email     = document.getElementById('email');
const phone     = document.getElementById('phone');
const pw        = document.getElementById('password');
const cpw       = document.getElementById('confirmPassword');
const form      = document.getElementById('registerForm');
const submitBtn = document.getElementById('submitBtn');

// ── Helpers ──
function setError(id, visible) {
    document.getElementById(id).classList.toggle('visible', visible);
}
function setInputState(input, state) {
    input.classList.remove('valid', 'invalid');
    if (state) input.classList.add(state);
}
function checkForm() {
    const ok =
        validateEmail(email.value) &&
        validatePhone(phone.value) &&
        passwordValid(pw.value) &&
        confirmValid();
    submitBtn.disabled = !ok;
}

// ── Server error banner ──
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
email.addEventListener('input', () => {
    const ok = validateEmail(email.value);
    const touched = email.value.length > 0;
    setInputState(email, touched ? (ok ? 'valid' : 'invalid') : '');
    setError('emailError', touched && !ok);
    checkForm();
});

// ── Phone ──
phone.addEventListener('input', () => {
    const ok = validatePhone(phone.value);
    setInputState(phone, phone.value.length > 0 ? (ok ? 'valid' : 'invalid') : '');
    setError('phoneError', phone.value.length > 0 && !ok);
    checkForm();
});

// ── Password strength ──
const bars   = ['sb1','sb2','sb3','sb4'].map(id => document.getElementById(id));
const label  = document.getElementById('pwLabel');
const colors = ['#e05252','#f5a623','#6B8E23','#3a6b0a'];
const labels = ['Weak','Fair','Good','Strong'];

function updateStrength(v) {
    const r = validatePasswordRules(v);
    let score = 0;
    if (r.len)  score++;
    if (r.num)  score++;
    if (r.spec) score++;
    if (v.length >= 12) score++;
    score = Math.min(score, 4);
    bars.forEach((b, i) => {
        b.style.background = i < score ? colors[score - 1] : '#e0e0e0';
    });
    label.textContent = score > 0 ? labels[score - 1] : '';
    label.style.color = score > 0 ? colors[score - 1] : '#aaa';
}

function updateRules(v) {
    const r = validatePasswordRules(v);
    document.getElementById('ruleLen').classList.toggle('passed', r.len);
    document.getElementById('ruleNum').classList.toggle('passed', r.num);
    document.getElementById('ruleSpec').classList.toggle('passed', r.spec);
}

pw.addEventListener('focus', () => {
    document.getElementById('pwStrength').style.display = 'block';
    document.getElementById('pwRules').style.display    = 'flex';
});

pw.addEventListener('input', () => {
    const ok = passwordValid(pw.value);
    const touched = pw.value.length > 0;
    setInputState(pw, touched ? (ok ? 'valid' : 'invalid') : '');
    setError('passwordError', touched && !ok);
    updateStrength(pw.value);
    updateRules(pw.value);
    if (cpw.value.length > 0) {
        const match = confirmValid();
        setInputState(cpw, match ? 'valid' : 'invalid');
        setError('confirmPasswordError', !match);
    }
    checkForm();
});

// ── Confirm password ──
cpw.addEventListener('input', () => {
    const touched = cpw.value.length > 0;
    const match   = confirmValid();
    setInputState(cpw, touched ? (match ? 'valid' : 'invalid') : '');
    setError('confirmPasswordError', touched && !match);
    checkForm();
});

// ── Submit → POST /api/auth/register ──
form.addEventListener('submit', async (e) => {
    e.preventDefault();
    if (!validateEmail(email.value) || !passwordValid(pw.value) || !confirmValid()) return;

    hideServerError();
    submitBtn.classList.add('loading');
    submitBtn.disabled = true;

    const payload = {
        email:     email.value.trim(),
        password:  pw.value,
        firstName: document.getElementById('firstName').value.trim(),
        lastName:  document.getElementById('lastName').value.trim(),
        phone:     phone.value.trim()
    };

    try {
        const res  = await fetch('/api/auth/register', {
            method:  'POST',
            headers: { 'Content-Type': 'application/json' },
            body:    JSON.stringify(payload)
        });
        const data = await res.json();

        if (res.ok && data.success) {
            showSuccess();
        } else {
            // 409 — email already exists; 400 — bad data
            submitBtn.classList.remove('loading');
            submitBtn.disabled = false;
            showServerError(data.message || 'Registration failed. Please try again.');
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