(() => {
    'use strict';
    const admin = document.body.dataset.admin === 'true';
    const status = document.getElementById('login-status');
    function csrfToken() {
        const cookie = document.cookie.split('; ').find(value => value.startsWith('XSRF-TOKEN='));
        return cookie ? decodeURIComponent(cookie.slice('XSRF-TOKEN='.length)) : '';
    }
    async function request(path, options = {}) {
        const response = await fetch(path, {credentials: 'same-origin', ...options});
        if (!response.ok) throw new Error(response.status === 403 ? 'Zugriff oder CSRF-Prüfung abgewiesen (403).' : 'Anfrage fehlgeschlagen (' + response.status + ').');
        return response.json();
    }
    async function refresh() {
        if (admin) {
            const data = await request('/security-demo/notes/all');
            // DELIBERATELY UNSAFE sink for the exercise: stored HTML is interpreted.
            // SOLUTION A: server-side HtmlUtils.htmlEscape (escape-notes=true).
            // SOLUTION B: build DOM nodes and set each cell.textContent = text.
            document.getElementById('notes-output').innerHTML = '<table><thead><tr><th>Benutzer</th><th>Notiz</th></tr></thead><tbody>' +
                Object.entries(data).map(([username, text]) => `<tr><td>${username}</td><td>${text}</td></tr>`).join('') + '</tbody></table>';
            document.getElementById('admin-section').hidden = false;
        } else {
            const data = await request('/security-demo/notes/me');
            document.getElementById('note').value = data.text;
            document.getElementById('user-section').hidden = false;
        }
    }
    document.getElementById('login-form').addEventListener('submit', async event => {
        event.preventDefault();
        try {
            await request('/security-demo/session');
            const response = await fetch('/login', {method: 'POST', credentials: 'same-origin',
                headers: {'Content-Type': 'application/x-www-form-urlencoded', 'X-XSRF-TOKEN': csrfToken()},
                body: new URLSearchParams({username: document.getElementById('username').value, password: document.getElementById('password').value})});
            if (!response.ok || new URL(response.url).searchParams.has('error')) throw new Error('Anmeldung fehlgeschlagen.');
            const session = await request('/security-demo/session');
            if (!session.authenticated) throw new Error('Anmeldung fehlgeschlagen.');
            status.textContent = 'Angemeldet als ' + session.username + '.';
            await refresh();
        } catch (error) { status.textContent = error.message; }
    });
    const save = document.getElementById('save-note');
    if (save) save.addEventListener('click', async () => {
        try {
            await request('/security-demo/session');
            await request('/security-demo/notes/me', {method: 'POST', headers: {'Content-Type': 'application/json', 'X-XSRF-TOKEN': csrfToken()},
                body: JSON.stringify({text: document.getElementById('note').value})});
            document.getElementById('save-status').textContent = 'Notiz gespeichert.';
        } catch (error) { document.getElementById('save-status').textContent = error.message; }
    });
    const refreshButton = document.getElementById('refresh-notes');
    if (refreshButton) refreshButton.addEventListener('click', () => refresh().catch(error => { status.textContent = error.message; }));
    request('/security-demo/config').then(config => {
        document.getElementById('mode').textContent = `CSRF: ${config.csrfEnabled ? 'an' : 'aus'}, CSP: ${config.cspEnabled ? 'an' : 'aus'}, HTML-Escaping: ${config.escapeNotes ? 'an' : 'aus'}, CORS: ${config.allowAnyOrigin ? 'alle Origins erlaubt' : 'fremde Origins gesperrt'}`;
        document.getElementById('attacker-link').href = config.attackerOrigin + '/evil/csrf-attack.html';
        document.getElementById('loot-link').href = config.attackerOrigin + '/evil/loot';
    }).catch(error => { status.textContent = error.message; });
})();
