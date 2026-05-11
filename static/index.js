(function initAuthState() {
    const token    = localStorage.getItem('auth_token') || sessionStorage.getItem('auth_token');
    const navAuth  = document.getElementById('navAuthBtn');
    const navUser  = document.getElementById('navUser');
    const navName  = document.getElementById('navUsername');

    if (token) {
        try {
            const payload = JSON.parse(atob(token.split('.')[1]));
            const display = payload.sub || 'My Account';
            navName.textContent = display;
            navAuth.style.display = 'none';
            navUser.style.display = 'flex';
        } catch (e) {
            localStorage.removeItem('auth_token');
            sessionStorage.removeItem('auth_token');
        }
    }
})();

document.getElementById('btnLogout')?.addEventListener('click', () => {
    localStorage.removeItem('auth_token');
    sessionStorage.removeItem('auth_token');
    window.location.reload();
});

const hamburger = document.getElementById('hamburger');
const mainNav   = document.getElementById('mainNav');

hamburger?.addEventListener('click', () => {
    hamburger.classList.toggle('open');
    mainNav.classList.toggle('open');
});

mainNav?.querySelectorAll('a').forEach(link => {
    link.addEventListener('click', () => {
        hamburger.classList.remove('open');
        mainNav.classList.remove('open');
    });
});