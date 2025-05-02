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
            username.textContent = (user.surname == 'null' ? '' : user.surname) + ' ' + (user.name == 'null' ? '' : user.name) + 
                ' ' + (user.patronymic == 'null' ? '' : user.patronymic);
        
            const profileButton = document.createElement('button');
            profileButton.textContent = 'Профиль';
            profileButton.onclick = () => {
                window.location.href = '/profile';
            };

            const logoutButton = document.createElement('button');
            logoutButton.textContent = 'Выйти';
            logoutButton.onclick = () => {
                window.location.href = '/logout';
            };

            profileButtonsContainer.appendChild(username);
            profileButtonsContainer.appendChild(profileButton);
            profileButtonsContainer.appendChild(logoutButton);

            displayUserInfo(user);
        } catch (error) {
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
    const name = (user.surname == 'null' ? '' : user.surname) + ' ' + (user.name == 'null' ? '' : user.name) + 
        ' ' + (user.patronymic == 'null' ? '' : user.patronymic);

    document.getElementById('name').textContent = name;
    document.getElementById('phone').textContent = 
        user.phoneNumber == 'null' ? 'не представлен' : user.phoneNumber;
    document.getElementById('mail').textContent = user.mail.includes('null') 
        ? 'не представлен' : user.mail;
}

document.addEventListener('DOMContentLoaded', fetchUser );