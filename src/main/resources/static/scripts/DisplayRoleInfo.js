async function fetchRoleInfo() {
    try {
        const response = await fetch('/api/role/info');
        if (!response.ok) {
            throw new Error('Сетевая ошибка');
        }
        const roleInfo = await response.json();
        if (roleInfo.role == 'Студент') {
            displayTeamName(roleInfo);
        }

        document.getElementById('role').textContent = roleInfo.role;

    } catch (error) {
        console.error('Ошибка при получении данных:', error);
    }
}

function displayTeamName(roleInfo) {
    document.getElementById('team_section').style = 'display: grid';

    if (roleInfo.teamName != 'Вы пока не состоите в команде')
        document.getElementById('team').textContent = roleInfo.teamName;
    else {
        const newTeamButton = document.createElement('button');
        newTeamButton.textContent = 'Создать команду';
        Object.assign(newTeamButton.style, {
            backgroundColor: 'rgb(223, 223, 223)',
            padding: '10px 20px',
            fontSize: '25px',
            border: 'none',
            borderRadius: '5px',
            cursor: 'pointer'
        });
        document.getElementById('team').appendChild(newTeamButton);
    }
}

document.getElementById('team').onclick = function() {
    if (document.getElementById('team').textContent != 'Создать команду') {
        window.location.href = '/team';
    }
    else {
        window.location.href = '/team/create'
    }
}

document.addEventListener('DOMContentLoaded', fetchRoleInfo);