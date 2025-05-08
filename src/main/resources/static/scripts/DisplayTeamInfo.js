async function fetchTeamInfo() {
    try {
        const response = await fetch('/api/team/info');
        if (!response.ok) {
            throw new Error('Сетевая ошибка');
        }
        const teamInfo = await response.json();
        displayTeamInfo(teamInfo);
        displayTeamProjects(teamInfo);
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
    teammatesList.innerHTML = '';

    const teamlid = teamInfo.teamlid;
    teammatesList.innerHTML += `
        <div style="padding: 20px; cursor: pointer; font-size: 17px;">${
            teamlid.surname + ' ' + teamlid.name + ' ' + teamlid.patronymic
        }</div>
    `
    for (var i = 0; i < teamInfo.teammates.length; i++) {
        if (teamInfo.teammates[i].id != teamlid.id) {
            var post = teamInfo.teammates[i].surname + ' ' + 
                teamInfo.teammates[i].name + ' ' + teamInfo.teammates[i].patronymic;

            teammatesList.innerHTML += `
                <div style="padding: 20px; cursor: pointer; font-size: 17px;">${post}</div>
            `
        }
    }
}

function displayTeamProjects(teamInfo) {
    const checkboxes = document.querySelectorAll('input[name="status"]');
    console.log(checkboxes);
    checkboxes.forEach(checkbox => {
        checkbox.addEventListener('change', fetchTeamInfo);
    });

    const page = document.getElementById("projects");
    page.innerHTML = '';

    var allProjects = [].concat(teamInfo.currentProject, teamInfo.refusedProjects, teamInfo.completedProjects);
    console.log(allProjects);

    const selectedStatuses = Array.from(document.querySelectorAll('input[name="status"]:checked')).map(el => el.value);
    const searchQuery = document.getElementById('searchInput').value.toLowerCase();

    const filteredProjects = allProjects.filter(project => 
        project != null && selectedStatuses.includes(project.status) &&    
        project.name.toLowerCase().includes(searchQuery)
    );

    for (let i = 0; i < filteredProjects.length; i++) {
        const colorMap = {
            'COMPLETED': 'green',
            'ON_WORK': 'red',
            'ON_VERIFICATION': 'blue',
            'FREE': 'green'
        };
        const color = colorMap[filteredProjects[i].status];

        const statusMap = {
            'COMPLETED': 'сдано',
            'ON_WORK': 'в работе',
            'ON_VERIFICATION': 'на верификации',
            'FREE': 'свободно'
        };
        const status = statusMap[filteredProjects[i].status];

        const card = document.createElement('div');
        card.className = 'content';
        card.innerHTML += `
            <ul class="card-info">
                <li class="card-section header_text">
                    ${filteredProjects[i].name}
                </li>
                <li class="card-section center-y">
                    ${filteredProjects[i].type}
                </li>
                <li class="card-section center-y">
                    Статус:&nbsp;<span style="color: ${color};">${status}</span>
                </li>
                <li class="card-section center-y">
                    <span class="description_text">
                        Цель: ${filteredProjects[i].goal}
                    </span>
                </li>
                <li class="card-section center-y">
                    <button class="yellow-button">Подробнее</button>
                </li>
            </ul>
        `;

        card.addEventListener('click', () => {
            window.location.href = `/project/info/${filteredProjects[i].id}`;
        });

        page.appendChild(card);
    }
}

document.getElementById('team').onclick = function() {
    window.location.href = '/team';
}

document.getElementById('searchInput').addEventListener('input', () => {
    fetchTeamInfo();
});

document.getElementById('toggleCheckboxes').addEventListener('click', (event) => {
    event.stopPropagation();
    const checkboxContainer = document.getElementById('checkboxContainer');
    
    if (checkboxContainer.style.display === 'none' || checkboxContainer.style.display === '') {
        checkboxContainer.style.display = 'block';
    } else {
        checkboxContainer.style.display = 'none';
    }
});

document.addEventListener('DOMContentLoaded', fetchTeamInfo);