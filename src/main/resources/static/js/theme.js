document.addEventListener('DOMContentLoaded', () => {
  const root=document.documentElement;
  const button=document.querySelector('[data-theme-toggle]');
  let mode='light';
  try { mode=localStorage.getItem('smartcity360-ui-theme')==='dark'?'dark':'light'; } catch(ignore) {}
  const apply=()=>{
    root.dataset.theme=mode;
    if(button){
      button.setAttribute('aria-label',mode==='dark'?'Switch to light theme':'Switch to dark theme');
      button.setAttribute('title',mode==='dark'?'Switch to light theme':'Switch to dark theme');
      button.querySelector('span').textContent=mode==='dark'?'☀':'☾';
    }
  };
  apply();
  button?.addEventListener('click',()=>{
    mode=mode==='light'?'dark':'light';
    try {localStorage.setItem('smartcity360-ui-theme',mode);}catch(ignore){}
    apply();
  });
});
