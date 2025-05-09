let teammatesCounter = 0;
let isUsersAvaliable = false;
let teammatesId = [];

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

    fetch('/api/projects/request', {
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
    const post = document.getElementById(`teammate${teammatesCounter}`).value;
    
    if (post != '') {
        fetch(`../api/user/find/${post}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            if (!response.ok) throw new Error('Ошибка сети');
            return response.json();
        })
        .then(data => {
            console.log(data.id)
            const errorDiv = document.getElementById(`error${teammatesCounter}`);
            if (data.isExist === 'true' && !teammatesId.includes(data.id)) {
                errorDiv.textContent = '';
                isUsersAvaliable = true;
                teammatesId.push(data.id);

                const teamForm = document.getElementById('team-form');
        
                teamForm.insertAdjacentHTML('beforeend', `
                    <div style="width: 90%; margin-right: 5%;">
                        <textarea id="teammate${teammatesCounter + 1}" class="input medium" style="margin-bottom: 15px;"></textarea>
                    </div>
                `);

                const addButton = document.getElementById('add');
                teamForm.appendChild(addButton);

                teamForm.insertAdjacentHTML('beforeend', `
                    <div id="error${teammatesCounter + 1}" style="width: 100%;"></div>
                `)

                teammatesCounter++;
            }
            else {
                errorDiv.style.color = 'red';
                errorDiv.textContent = 'Пользователь не найден';
                isUsersAvaliable = false;
            }
        })
    }
}

function createTeam() {
    if (document.getElementById('name').value != '') {
        const data = {
            name: document.getElementById('name').value,
            ids: teammatesId
        }
        const jsonData = JSON.stringify(data);

        fetch('/api/team/create', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: jsonData
        })
        .then(data => {
            console.log('Успех:', data);
    
            alert('Команда создана! Перейти на главную страницу?');
            window.location.href = '/';
        })
        .catch(error => {
            console.error('Ошибка:', error);
        });
    }
    else alert('Введите название команды!')
}