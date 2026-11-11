document.addEventListener('DOMContentLoaded', () => {
  const toggle = document.querySelector('[data-nav-toggle]');
  const scrim = document.querySelector('[data-nav-close]');
  if (!toggle) return;
  const setOpen = (open) => {
    document.body.classList.toggle('nav-is-open', open);
    toggle.setAttribute('aria-expanded', String(open));
    toggle.setAttribute('aria-label', open ? 'Close navigation' : 'Open navigation');
  };
  toggle.addEventListener('click', () => setOpen(!document.body.classList.contains('nav-is-open')));
  if (scrim) scrim.addEventListener('click', () => setOpen(false));
  document.addEventListener('keydown', (event) => {
    if (event.key === 'Escape') setOpen(false);
  });
  document.querySelectorAll('.side-nav a').forEach((link) => {
    link.addEventListener('click', () => setOpen(false));
  });
});
