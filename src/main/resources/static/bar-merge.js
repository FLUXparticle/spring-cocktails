const buttons = document.querySelectorAll('[data-adopt]');
function adopt(button) { document.getElementById(button.dataset.adopt).value = button.dataset.value || ''; }
buttons.forEach(button => button.addEventListener('click', () => adopt(button)));
document.getElementById('adopt-all').addEventListener('click', () => buttons.forEach(adopt));
