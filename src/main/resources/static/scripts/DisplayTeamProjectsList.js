async function fetchTeamProject() {
    try {
        const response = await fetch('/api/team/projects');
        if (!response.ok) {
            throw new Error('Сетевая ошибка');
        }
        try {
            const teamProjects = await response.json();
            displayTeamProjects(teamProjects);
        } catch {
            console.log('a');
        }
    } catch (error) {
        console.error('Ошибка при получении данных:', error);
    }
}

function displayTeamProjects(teamProjects) {
    if ((document.getElementById('role') != null && document.getElementById('role').textContent == 'Студент')
        || (document.getElementById('role') == null)) {
        
        if (document.getElementById('role') != null) {
            if (document.getElementById("projects") != null) 
                document.getElementById("projects").style = 'display: flex;';
            if (document.getElementById('projects-list') != null) 
                document.getElementById('projects-list').style = 'display: flex;';
        }

        const page = document.getElementById("projects");
        page.innerHTML = '';

        const allProjects = [].concat(teamProjects.current, teamProjects.refused, teamProjects.completed);

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
                'CANCELED': 'red'
            };
            const color = colorMap[filteredProjects[i].status];

            const statusMap = {
                'COMPLETED': 'сдано',
                'ON_WORK': 'в работе',
                'CANCELED': 'отказ'
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
}

document.getElementById('searchInput').addEventListener('input', () => {
    fetchTeamProject();
});

const checkboxes = document.querySelectorAll('input[name="status"]');
checkboxes.forEach(checkbox => {
    checkbox.addEventListener('change', fetchTeamProject);
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