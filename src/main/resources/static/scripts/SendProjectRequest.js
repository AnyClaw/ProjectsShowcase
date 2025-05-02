let teammatesCounter = 0;

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

        alert('Заявка отправлена! Перейти на главную страницу?');
        window.location.href = '/';
    })
    .catch(error => {
        console.error('Ошибка:', error);
    });
}

function addTeammate() {
    document.getElementById(`add`).remove();
    document.getElementById('team-form').innerHTML += `
        <div style="width: 90%; margin-right: 5%; ">
            <textarea id="teammate${++teammatesCounter}" type="text" class="input medium" style="margin-bottom: 15px;"></textarea>
        </div>
        <img src="..\\images\\Add_Icon.png" class="add-button" id="add" onclick="addTeammate()" style="margin-bottom: 15px;">
    `;
};