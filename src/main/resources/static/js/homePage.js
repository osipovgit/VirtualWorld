function logout() {
    $.post('/logout').then(document.location = '/auth');
}