let user = null;

async function fetchUser () {
    try {
        const response = await fetch('/api/user/info');
        if (!response.ok) {
            throw new Error('Сетевая ошибка');
        }

        const profileButtonsContainer = document.getElementById('profilePopupButtons');
        profileButtonsContainer.innerHTML = '';
        
        try {
            user = await response.json();

            const username = document.createElement('label');
            username.textContent = user.name + " " + user.surname + " " + user.patronymic;
        
            const profileButton = document.createElement('button');
            profileButton.textContent = 'Профиль';
            profileButton.onclick = () => {
                window.location.href = '/profile';
            };

            const logoutButton = document.createElement('button');
            logoutButton.textContent = 'Выйти';
            logoutButton.onclick = () => {
                alert('Выход из системы');
            };

            profileButtonsContainer.appendChild(username);
            profileButtonsContainer.appendChild(profileButton);
            profileButtonsContainer.appendChild(logoutButton);
            console.log('dscxdsfer')

            displayUserInfo(user);
        } catch {
            const loginButton = document.createElement('button');
            loginButton.textContent = 'Войти';
            loginButton.onclick = () => {
                window.location.href = '/login';
            };

            profileButtonsContainer.appendChild(loginButton);
        }
    } catch (error) {
        console.error('Ошибка при получении данных:', error);
    }
}

const profileButton = document.getElementById("profile-button");
const showcaseButton = document.getElementById("showcase-button");
const profilePopup = document.getElementById("profilePopup");

profileButton.onclick = function(event) {
    profilePopup.style.display = profilePopup.style.display === "block" ? "none" : "block";
    event.stopPropagation(); 
}

showcaseButton.onclick = function() {
    window.location.href = '/';
}

window.onclick = function(event) {
    if (event.target !== profileButton && !profilePopup.contains(event.target)) {
        profilePopup.style.display = "none";
    }
}

function displayUserInfo(user) {
    // Заполняем элементы профиля
    document.getElementById('name').textContent = user.name + " " + user.surname + " " + user.patronymic; // Имя
    document.getElementById('phone').textContent = user.phone == null ? 'не представлен' : user.phone;
    document.getElementById('role').textContent = user.roles; // Роль
    document.getElementById('mail').textContent = user.mail; // Email
}

document.addEventListener('DOMContentLoaded', fetchUser );