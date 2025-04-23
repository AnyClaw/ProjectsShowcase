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
        displayProjectInfo(projectInfo);
        
    } catch (error) {
        console.error('Ошибка при получении данных:', error);
    }
}

function displayProjectInfo(projectInfo) {
    document.getElementById('name').textContent = projectInfo.name;
    document.getElementById('status').textContent = projectInfo.status;
    document.getElementById('type').textContent = projectInfo.type;
    document.getElementById('department').textContent = projectInfo.department;
    document.getElementById('goal').textContent = projectInfo.goal;
    document.getElementById('barrier').textContent = projectInfo.barrier;
    document.getElementById('decisions').textContent = projectInfo.decisions;
    document.getElementById('customer').textContent = projectInfo.customer.name;
}

document.addEventListener('DOMContentLoaded', fetchProjectInfo);