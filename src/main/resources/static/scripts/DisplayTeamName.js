async function fetchTeamName() {
    try {
        const response = await fetch('/api/team/getName');
        if (!response.ok) {
            throw new Error('Сетевая ошибка');
        }
        const teamName = await response.json();
        displayTeamName(teamName);
    } catch (error) {
        console.error('Ошибка при получении данных:', error);
    }
}

function displayTeamName(teamName) {
    document.getElementById('team').innerHTML = teamName.name;
}

document.getElementById('team').onclick = function() {
    if (document.getElementById('team').textContent != 'Вы пока не состоите в команде') {
        window.location.href = '/team';
    }
}

document.addEventListener('DOMContentLoaded', fetchTeamName);