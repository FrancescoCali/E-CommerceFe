document.addEventListener('DOMContentLoaded', function() {
    const searchButton = document.querySelector('.search-button');
    const searchInput = document.querySelector('.search-input');
    const errorMessage = document.querySelector('.error-message');

    // Quando si clicca sul bottone della lente di ingrandimento
    searchButton.addEventListener('click', function(event) {
        // Previene che il clic sul bottone faccia chiudere il campo di ricerca
        event.stopPropagation();

        if (!searchInput.classList.contains('show')) {
            searchInput.classList.add('show');
            errorMessage.style.display = 'none';
        } else {
            if (searchInput.value.trim() === "") {
                errorMessage.style.display = 'block';
            } else {
                document.querySelector('.search-form').submit();
            }
        }
    });

    // Quando si clicca su qualsiasi altra parte dello schermo
    document.addEventListener('click', function(event) {
        // Verifica se il campo di input è visibile e se non si è cliccato su di esso o sul bottone della lente
        if (searchInput.classList.contains('show') && !searchInput.contains(event.target) && !searchButton.contains(event.target)) {
            searchInput.classList.remove('show');
            searchInput.value = "";  // Pulisce il campo di input
            errorMessage.style.display = 'none';
        }
    });

    // Previene la chiusura del campo di input se si clicca al suo interno
    searchInput.addEventListener('click', function(event) {
        event.stopPropagation();
    });
});

    document.addEventListener('DOMContentLoaded', function () {
        // Gestione della navigazione tra le pagine
        const prevButton = document.getElementById('prevButton');

        prevButton.addEventListener('click', function () {
            window.history.back(); // Torna indietro nella cronologia del browser
        });
    });