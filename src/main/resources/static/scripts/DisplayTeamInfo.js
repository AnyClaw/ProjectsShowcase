async function fetchTeamInfo() {
    try {
        const response = await fetch('/api/team/info');
        if (!response.ok) {
            throw new Error('Сетевая ошибка');
        }
        const teamInfo = await response.json();
        displayTeamInfo(teamInfo);
    } catch (error) {
        console.error('Ошибка при получении данных:', error);
    }
}

function displayTeamInfo(teamInfo) {
    console.log(teamInfo);
    const teamTitle = document.getElementById('team_title');
    if (teamTitle)
        teamTitle.innerHTML = teamInfo.name; 

    document.getElementById('team').innerHTML = teamInfo.name;

    const teammatesList = document.getElementById('teammates');

    const teamlid = teamInfo.teamlid;
    teammatesList.innerHTML += `
        <div style="padding: 20px; cursor: pointer; font-size: 17px;">${
            teamlid.name + ' ' + teamlid.surname + ' ' + teamlid.patronymic
        }</div>
    `
    for (var i = 0; i < teamInfo.teammates.length; i++) {
        if (teamInfo.teammates[i].id != teamlid.id) {
            var post = teamInfo.teammates[i].name + ' ' + 
                teamInfo.teammates[i].surname + ' ' + teamInfo.teammates[i].patronymic;

            teammatesList.innerHTML += `
                <div style="padding: 20px; cursor: pointer; font-size: 17px;">${post}</div>
            `
        }
    }
}

document.getElementById('team').onclick = function() {
    window.location.href = '/team';
}

document.addEventListener('DOMContentLoaded', fetchTeamInfo);