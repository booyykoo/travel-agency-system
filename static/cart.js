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

// ── Init ──
function show(id)  { const el = document.getElementById(id); if (el) el.style.display = 'block'; }
function hide(id)  { const el = document.getElementById(id); if (el) el.style.display = 'none';  }

if (!token) {
    hide('cartLoading');
    show('cartAuthWall');
} else {
    loadCart();
}

// ── Load cart from API ──
async function loadCart() {
    show('cartLoading');
    hide('cartEmpty');
    hide('cartContent');
    hide('cartError');

    try {
        const res = await fetch('/api/cart', {
            headers: { 'Authorization': `Bearer ${token}` }
        });

        if (res.status === 401) { window.location.href = '/login'; return; }
        if (!res.ok) throw new Error();

        const items = await res.json();
        hide('cartLoading');

        if (!items || items.length === 0) {
            show('cartEmpty');
        } else {
            renderCart(items);
            show('cartContent');
        }
    } catch (e) {
        hide('cartLoading');
        show('cartError');
    }
}

// ── Render cart items ──
function renderCart(items) {
    const container = document.getElementById('cartItems');
    container.innerHTML = '';

    let total = 0;

    items.forEach(item => {
        const price    = item.tour?.price || 0;
        const qty      = item.quantity || 1;
        const subtotal = price * qty;
        total += subtotal;

        const div = document.createElement('div');
        div.className = 'cart-item';
        div.innerHTML = `
            <div class="cart-item-img" style="background-image: url('${item.tour?.imageUrl || ''}')"></div>
            <div class="cart-item-info">
                <div class="cart-item-title">${item.tour?.title || item.tour?.destination || 'Tour'}</div>
                <div class="cart-item-destination">${item.tour?.destination || ''}</div>
                <div class="cart-item-qty">Qty: ${qty}</div>
            </div>
            <div class="cart-item-right">
                <div class="cart-item-price">$${subtotal.toLocaleString()}</div>
                <button class="btn-remove" data-id="${item.id}">Remove</button>
            </div>
        `;
        container.appendChild(div);
    });

    document.getElementById('cartSubtotal').textContent = `$${total.toLocaleString()}`;
    document.getElementById('cartTotal').textContent    = `$${total.toLocaleString()}`;

    // Remove buttons
    container.querySelectorAll('.btn-remove').forEach(btn => {
        btn.addEventListener('click', () => removeItem(btn.dataset.id, btn));
    });
}

// ── Remove item ──
async function removeItem(itemId, btn) {
    btn.disabled = true;
    btn.textContent = '...';

    try {
        const res = await fetch(`/api/cart/${itemId}`, {
            method:  'DELETE',
            headers: { 'Authorization': `Bearer ${token}` }
        });

        if (!res.ok) throw new Error();
        // Перезагружаем корзину после удаления
        loadCart();
    } catch (e) {
        btn.disabled = false;
        btn.textContent = 'Remove';
    }
}