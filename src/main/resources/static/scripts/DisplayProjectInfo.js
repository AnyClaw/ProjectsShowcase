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

        var affiliation;
        if (userInfo != null) {
            const response2 = await fetch(`/api/affiliation/${userInfo.id}/${projectId}`)
            if (!response2.ok) {
                throw new Error('Сетевая ошибка');
            }

            affiliation = await response2.json();
        }
        else affiliation = false;

        displayProjectInfo(projectInfo, userInfo, affiliation);
        
    } catch (error) {
        console.error('Ошибка при получении данных:', error);
    }
}

function displayProjectInfo(projectInfo, userInfo, affiliation) {
    document.getElementById('name').textContent = projectInfo.name;
    document.getElementById('status').textContent = projectInfo.status;
    document.getElementById('type').textContent = projectInfo.type;
    document.getElementById('department').textContent = projectInfo.department;
    document.getElementById('goal').textContent = projectInfo.goal;
    document.getElementById('barrier').textContent = projectInfo.barrier;
    document.getElementById('decisions').textContent = projectInfo.decisions;
    document.getElementById('customer').textContent = projectInfo.customer.name;

    const buttonsSection = document.getElementById('buttons_section');
    if (userInfo != null) {
        if (userInfo.role == 'ROLE_STUDENT') {
            buttonsSection.innerHTML = `<button class="yellow-button button-right">Забронировать</button>`;
        }
        else {
            if (affiliation) {
                buttonsSection.innerHTML = `<button class="yellow-button button-right">Редактировать</button>`;
            }
        }
    }
    else buttonsSection.innerHTML = `<button class="yellow-button button-right">Забронировать</button>`;
}

document.addEventListener('DOMContentLoaded', fetchProjectInfo);