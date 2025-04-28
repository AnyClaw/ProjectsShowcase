async function fetchProjectInfo() {
    try {
        const url = new URL(window.location.href);
        const pathSegments = url.pathname.split('/');
        const projectId = pathSegments[pathSegments.length - 1];

        const response = await fetch(`/api/project/info/${projectId}`);
        if (!response.ok) {
            throw new Error('Сетевая ошибка');
        }

        const projectInfo = await response.json();

        const response1 = await fetch("/api/user/info");
        if (!response1.ok) {
            throw new Error('Сетевая ошибка');
        }
        var userInfo;
        try {
            userInfo = await response1.json();
        } catch {
            userInfo = null;
        }

        var affiliation, team;
        if (userInfo != null) {
            const response2 = await fetch(`/api/affiliation/${userInfo.id}/${projectId}`);
            if (!response2.ok) {
                throw new Error('Сетевая ошибка');
            }

            affiliation = await response2.json();

            const response3 = await fetch(`/api/team/info`);
            if (!response3.ok) {
                throw new Error('Сетевая ошибка');
            }
            
            try {
                team = await response3.json();
            } catch (error) {
                console.log(error);
            }
        }
        else affiliation = false;

        displayProjectInfo(projectInfo, userInfo, affiliation, projectId, team);
        
    } catch (error) {
        console.error('Ошибка при получении данных:', error);
    }
}

function displayProjectInfo(projectInfo, userInfo, affiliation, projectId, team) {
    const colorMap = {
        'COMPLETED': 'gray',
        'ON_WORK': 'red',
        'ON_VERIFICATION': 'blue',
        'FREE': 'green'
    };
    const color = colorMap[projectInfo.status];

    const statusMap = {
        'COMPLETED': 'сдано',
        'ON_WORK': 'в работе',
        'ON_VERIFICATION': 'на верификации',
        'FREE': 'свободно'
    };
    const status = statusMap[projectInfo.status];

    document.getElementById('name').textContent = projectInfo.name;

    document.getElementById('status').textContent = status;
    document.getElementById('status').style = `color: ${color}`;

    document.getElementById('type').textContent = projectInfo.type;
    document.getElementById('department').textContent = projectInfo.department;
    document.getElementById('goal').textContent = projectInfo.goal;
    document.getElementById('barrier').textContent = projectInfo.barrier;
    document.getElementById('decisions').textContent = projectInfo.decisions;
    document.getElementById('customer').textContent = projectInfo.customer.name;

    const buttonsSection = document.getElementById('buttons_section');
    buttonsSection.innerHTML = '';

    if (userInfo != null) {
        if (userInfo.role == 'ROLE_STUDENT' && status == 'свободно') {
            
            const book = document.createElement('button');
            book.className = 'yellow-button button-right';
            book.textContent = 'Забронировать';
            book.id = 'book-button';
            book.addEventListener('click', () => {
                fetch(`/book/project/${projectId}`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    }
                })
                .then(response => {
                    if (!response.ok) throw new Error('Ошибка сети');
                    return response.json();
                })
                .then(data => {
                    const response = data["Ответ"];
                    alert(response);
                    fetchProjectInfo();
                })
                .catch(error => {
                    console.error('Ошибка:', error);
                });
            });
            buttonsSection.appendChild(book);
        }
        else if (userInfo.role == 'ROLE_STUDENT' && status == 'в работе') {
            if (team != null && projectInfo.id == team.currentProject.id) {
                const book = document.createElement('button');
                book.className = 'yellow-button button-right';
                book.textContent = 'Завершить';
                book.id = 'book-button';
                book.addEventListener('click', () => {
                    fetch(`/team/finish`, {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        }
                    })
                    .then(response => {
                        if (!response.ok) throw new Error('Ошибка сети');
                        alert('Проект завершён!');
                        fetchProjectInfo();
                    })
                    .catch(error => {
                        console.error('Ошибка:', error);
                    });
                });
                buttonsSection.appendChild(book);
            }
        }
        else if (affiliation) {
            buttonsSection.innerHTML = `<button class="yellow-button button-right">Редактировать</button>`;
        }
    }
    else if (status == 'свободно') {
        const book = document.createElement('button');
        book.className = 'yellow-button button-right';
        book.textContent = 'Забронировать';
        book.id = 'book-button';
        book.addEventListener('click', () => {window.location.href = '/login';});
        buttonsSection.appendChild(book);
    }
}



document.addEventListener('DOMContentLoaded', fetchProjectInfo);