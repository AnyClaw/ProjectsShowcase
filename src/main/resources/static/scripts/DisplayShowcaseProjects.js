async function fetchProjects() {
    try {
        const response = await fetch('/api/projects/active');
        if (!response.ok) {
            throw new Error('Сетевая ошибка');
        }
        const projects = await response.json();
        displayProjects(projects);
    } catch (error) {
        console.error('Ошибка при получении данных:', error);
    }
}

function displayProjects(projects) {
    const page = document.getElementById("projects");
    page.innerHTML = '';

    const selectedStatuses = Array.from(document.querySelectorAll('input[name="status"]:checked')).map(el => el.value);
    const searchQuery = document.getElementById('searchInput').value.toLowerCase();

    const filteredProjects = projects.filter(project => 
        selectedStatuses.includes(project.status) &&
        project.name.toLowerCase().includes(searchQuery)
    );

    for (let i = 0; i < filteredProjects.length; i++) {
        const colorMap = {
            'FREE': 'green',
            'ON_WORK': 'red',
            'COMPLETED': 'blue'
        };
        const color = colorMap[filteredProjects[i].status];

        const statusMap = {
            'FREE': 'свободно',
            'ON_WORK': 'в работе',
            'COMPLETED': 'готово'
        };
        const status = statusMap[filteredProjects[i].status];

        page.innerHTML += `
            <div class="content">
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
            </div>
        `;
    }
}

document.getElementById('searchInput').addEventListener('input', () => {
    fetchProjects();
});

document.querySelectorAll('input[name="status"]').forEach(input => {
    input.addEventListener('change', () => {
        fetchProjects();
    });
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

document.addEventListener('click', (event) => {
    const checkboxContainer = document.getElementById('checkboxContainer');
    const toggleButton = document.getElementById('toggleCheckboxes');
    if (!checkboxContainer.contains(event.target) && !toggleButton.contains(event.target)) {
        checkboxContainer.style.display = 'none';
    }
});

document.addEventListener('DOMContentLoaded', fetchProjects);
