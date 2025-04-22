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
    console.log(projectInfo);
}

document.addEventListener('DOMContentLoaded', fetchProjectInfo);