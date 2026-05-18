// ── Auth state ──
const token = localStorage.getItem('auth_token') || sessionStorage.getItem('auth_token');

(function initAuthState() {
    const navAuth = document.getElementById('navAuthBtn');
    const navUser = document.getElementById('navUser');
    const navName = document.getElementById('navUsername');
    if (token) {
        try {
            const payload = JSON.parse(atob(token.split('.')[1]));
            navName.textContent = payload.sub || 'My Account';
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
    window.location.href = '/';
});

// ── Hamburger ──
const hamburger = document.getElementById('hamburger');
const mainNav   = document.getElementById('mainNav');
hamburger?.addEventListener('click', () => {
    hamburger.classList.toggle('open');
    mainNav.classList.toggle('open');
});

// ── Get tour ID from URL: /destinations/123 ──
const tourId = window.location.pathname.split('/').filter(Boolean).pop();

// ── Load tour details ──
async function loadTourDetails() {
    try {
        const res  = await fetch(`/api/tours/${tourId}`);

        if (res.status === 404) { showError(); return; }
        if (!res.ok) throw new Error();

        const tour = await res.json();
        renderTour(tour);
    } catch (e) {
        showError();
    }
}

function renderTour(tour) {
    document.title = `TravelGo - ${tour.title || tour.destination}`;

    // Image
    const img = document.getElementById('tourImage');
    if (tour.imageUrl) img.style.backgroundImage = `url(${tour.imageUrl})`;

    // Badge
    if (tour.duration) document.getElementById('tourDuration').textContent = tour.duration;

    // Info
    document.getElementById('tourDestination').textContent  = tour.destination || '';
    document.getElementById('tourTitle').textContent        = tour.title || tour.destination;
    document.getElementById('tourDescription').textContent  = tour.description || '';

    // Meta
    document.getElementById('metaDuration').textContent    = tour.duration    || '—';
    document.getElementById('metaDestination').textContent = tour.destination || '—';
    document.getElementById('metaPrice').textContent       = tour.price ? `from $${tour.price}` : '—';

    // Show/hide add to cart based on auth
    if (!token) {
        document.getElementById('btnAddCart').style.display  = 'none';
        document.getElementById('loginWarning').style.display = 'block';
    }

    document.getElementById('detailsLoading').style.display = 'none';
    document.getElementById('detailsContent').style.display = 'block';
}

function showError() {
    document.getElementById('detailsLoading').style.display = 'none';
    document.getElementById('detailsError').style.display   = 'flex';
}

// ── Add to cart ──
document.getElementById('btnAddCart')?.addEventListener('click', async () => {
    const btn      = document.getElementById('btnAddCart');
    const feedback = document.getElementById('cartFeedback');

    btn.classList.add('loading');
    btn.disabled = true;
    feedback.textContent = '';

    try {
        const res = await fetch('/api/cart/add', {
            method:  'POST',
            headers: {
                'Content-Type':  'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({ tourId: tourId, quantity: 1 })
        });

        if (res.status === 401) {
            window.location.href = '/login';
            return;
        }
        if (!res.ok) throw new Error();

        feedback.textContent = '✓ Added to cart!';
        btn.classList.remove('loading');
        btn.disabled = false;

        setTimeout(() => { feedback.textContent = ''; }, 3000);

    } catch (e) {
        feedback.style.color = '#e05252';
        feedback.textContent = '✕ Failed to add. Try again.';
        btn.classList.remove('loading');
        btn.disabled = false;
    }
});

loadTourDetails();