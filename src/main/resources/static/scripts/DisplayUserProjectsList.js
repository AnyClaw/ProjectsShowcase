async function fetchTeamProject() {
    try {
        const response = await fetch('/api/user/customer/projects');
        if (!response.ok) {
            throw new Error('Сетевая ошибка');
        }
        try {
            const teamProjects = await response.json();
            if (teamProjects.length == 0) throw new Error('Проектов нет');
            displayTeamProjects(teamProjects);
        } catch (error) {
            console.log(error);
            if (document.getElementById('role') != null) {
                if (document.getElementById("projects") != null) 
                    document.getElementById("projects").style = 'display: none;';
                if (document.getElementById('projects-list') != null) 
                    document.getElementById('projects-list').style = 'display: none;';
            }
        }
    } catch (error) {
        console.error('Ошибка при получении данных:', error);
    }
}

function displayTeamProjects(teamProjects) {
    if (document.getElementById('checkboxContainer').textContent == '') {
        document.getElementById('checkboxContainer').innerHTML = `
            <div>
                <input type="checkbox" name="status" value="COMPLETED" checked> Сдано
            </div>
            <div>
                <input type="checkbox" name="status" value="ON_WORK" checked> В работе
            </div>
            <div>
                <input type="checkbox" name="status" value="FREE" checked> Свободно
            </div>
            <div>
                <input type="checkbox" name="status" value="ON_VERIFICATION" checked> На верификации
            </div>
        `
    }

    const checkboxes = document.querySelectorAll('input[name="status"]');
    console.log(checkboxes);
    checkboxes.forEach(checkbox => {
        checkbox.addEventListener('change', fetchTeamProject);
    });

    const page = document.getElementById("projects");
    page.innerHTML = '';

    var allProjects = teamProjects;

    const selectedStatuses = Array.from(document.querySelectorAll('input[name="status"]:checked')).map(el => el.value);
    const searchQuery = document.getElementById('searchInput').value.toLowerCase();

    const filteredProjects = allProjects.filter(project => 
        selectedStatuses.includes(project.status) &&
        project.name.toLowerCase().includes(searchQuery)
    );

    for (let i = 0; i < filteredProjects.length; i++) {
        const colorMap = {
            'COMPLETED': 'green',
            'ON_WORK': 'blue',
            'CANCELED': 'red',
            'ON_VERIFICATION': 'blue',
            'FREE': 'green'
        };
        const color = colorMap[filteredProjects[i].status];

        const statusMap = {
            'COMPLETED': 'сдано',
            'ON_WORK': 'в работе',
            'CANCELED': 'отказ',
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

document.getElementById('searchInput').addEventListener('input', () => {
    fetchTeamProject();
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

document.addEventListener('DOMContentLoaded', fetchTeamProject);