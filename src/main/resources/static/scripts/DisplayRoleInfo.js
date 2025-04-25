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
    document.getElementById('team').textContent = roleInfo.teamName;
}

document.getElementById('team').onclick = function() {
    if (document.getElementById('team').textContent != 'Вы пока не состоите в команде') {
        window.location.href = '/team';
    }
}

document.addEventListener('DOMContentLoaded', fetchRoleInfo);