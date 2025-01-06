//Auteur(s) JS : Gabin PETITCOLAS
//Version : ?





document.addEventListener("TokenCheckFinished", (event) => {

    window.deleteUser = deleteUser;
    window.changePassword = changePassword;

    // Récupérer la liste des utilisateurs et remplir le tableau
    function fetchUsers() {
        fetch(`https://${window.ServerIP}:8443/SAE51/ListUsers`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ token: token, Test: false })
        })
        .then(response => response.json())
        .then(users => {
            const userTableBody = document.getElementById("userTableBody");
            userTableBody.innerHTML = ""; // Vide le tableau avant d'ajouter les nouvelles données

            users.forEach(user => {
                const row = document.createElement("tr");
                row.innerHTML = `
                    <td>${user.nom}</td>
                    <td>${user.prenom}</td>
                    <td>${user.login}</td>
                    <td>${user.droits}</td>
                    <td>
                        <button class="button delete-btn" onclick="deleteUser('${user.login}')">Supprimer</button>
                        <button class="button password-btn" onclick="changePassword('${user.login}')">Modifier MDP</button>
                    </td>
                `;
                userTableBody.appendChild(row);
            });
        })
    }

    // Supprimer un utilisateur
    function deleteUser(login) {
        UserLogin = sessionStorage.getItem('login');

        if(UserLogin === login){
            alert('Vous ne pouvez pas supprimer votre propre compte')
        }
        else{
            fetch(`https://${window.ServerIP}:8443/SAE51/DeleteUser`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ login: login, token: token, Test: false })
            })
            .then(response => response.json())
            .then(result => {
                if (result.erreur === "none") {
                    alert("Utilisateur supprimé avec succès !");
                    fetchUsers(); // Rafraîchit la liste des utilisateurs
                } else {
                    alert("Erreur lors de la suppression de l'utilisateur.");
                    console.log(result)
                }
            })
        }
    }

    // Changer le mot de passe d'un utilisateur
    function changePassword(login) {
        const newPassword = prompt(`Entrez le nouveau mot de passe pour ${login}:`);
        if (newPassword) {
            fetch(`https://${window.ServerIP}:8443/SAE51/SetPassword`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ target: login, password: newPassword, token: token, Test: false })
            })
            .then(response => response.json())
            .then(result => {
                if (result.erreur === "none") {
                    alert("Mot de passe modifié avec succès !");
                } else {
                    alert("Erreur lors de la modification du mot de passe.");
                    console.log(result)
                }
            })
        }
    }

    // Initialisation - récupérer le token et charger les utilisateurs
    function initialize() {
        fetchUsers(); // Récupère et affiche les utilisateurs
    }

    // Exécute la fonction d'initialisation au chargement de la page
    window.onload = initialize();

})