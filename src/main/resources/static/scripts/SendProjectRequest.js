function submitRequest() {
    const data = {
        name: document.getElementById('name').value,
        department: document.getElementById('department').value,
        status: 'ON_VERIFICATION',
        goal: document.getElementById('goal').value,
        barrier: document.getElementById('barrier').value,
        decisions: document.getElementById('decisions').value,
        type: document.getElementById('type').value,
        keywords: document.getElementById('keywords').value,
        customer: user
    };

    const jsonData = JSON.stringify(data);

    fetch('/api/project/request', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: jsonData
    })
    .then(data => {
        console.log('Успех:', data);

        const toMainPage = confirm('Заявка отправлена! Перейти на главную страницу?');
        window.location.href = '/';
    })
    .catch(error => {
        console.error('Ошибка:', error);
    });
}
