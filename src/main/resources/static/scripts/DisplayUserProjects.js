document.getElementById('toggleCheckboxes').addEventListener('click', (event) => {
    event.stopPropagation();
    const checkboxContainer = document.getElementById('checkboxContainer');
    
    if (checkboxContainer.style.display === 'none' || checkboxContainer.style.display === '') {
        checkboxContainer.style.display = 'block';
    } else {
        checkboxContainer.style.display = 'none';
    }
});