	// Aplica o tema salvo
  if (localStorage.getItem('darkMode') === 'enabled') {
    document.body.classList.add('dark-mode');
    document.querySelectorAll('.navbar, footer').forEach(e => e.classList.add('dark-mode'));
  }
  function toggleDarkMode() {
    const body = document.body;
    const btn = document.getElementById('toggleDarkBtn');

    body.classList.toggle('dark-mode');

    if (body.classList.contains('dark-mode')) {
      localStorage.setItem('darkMode', 'enabled');
      if (btn) btn.innerHTML = '<i class="bi bi-sun"></i> Modo Claro';
    } else {
      localStorage.setItem('darkMode', 'disabled');
      if (btn) btn.innerHTML = '<i class="bi bi-moon"></i> Modo Escuro';
    }
  }

  window.addEventListener('DOMContentLoaded', () => {
    const isDark = localStorage.getItem('darkMode') === 'enabled';
    const body = document.body;
    const btn = document.getElementById('toggleDarkBtn');

    if (isDark) {
      body.classList.add('dark-mode');
      if (btn) btn.innerHTML = '<i class="bi bi-sun"></i> Modo Claro';
    } else {
      if (btn) btn.innerHTML = '<i class="bi bi-moon"></i> Modo Escuro';
    }
  });
