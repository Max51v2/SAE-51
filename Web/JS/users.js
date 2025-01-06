//Auteur(s) JS : Gabin PETITCOLAS
//Version : ?


document.addEventListener("TokenCheckFinished", (event) => {

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
        .catch(error => console.error("Erreur lors de la récupération des utilisateurs :", error));
    }

    // Supprimer un utilisateur
    function deleteUser(login) {
        fetch(`https://${window.ServerIP}:8443/SAE51/DeleteUser`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ login: login, token: token, Test: false })
        })
        .then(response => response.json())
        .then(result => {
            console.log(result);
            if (result.success) {
                alert("Utilisateur supprimé avec succès !");
                fetchUsers(); // Rafraîchit la liste des utilisateurs
            } else {
                alert("Erreur lors de la suppression de l'utilisateur.");
            }
        })
        .catch(error => console.error("Erreur lors de la suppression de l'utilisateur :", error));
    }

    // Changer le mot de passe d'un utilisateur
    function changePassword(login) {
        const newPassword = prompt(`Entrez le nouveau mot de passe pour ${login}:`);
        if (newPassword) {
            fetch(`https://${window.ServerIP}:8443/SAE51/UpdatePassword`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ login: login, password: newPassword, token: token, Test: false })
            })
            .then(response => response.json())
            .then(result => {
                console.log(result);
                if (result.success) {
                    alert("Mot de passe modifié avec succès !");
                } else {
                    alert("Erreur lors de la modification du mot de passe.");
                }
            })
            .catch(error => console.error("Erreur lors de la modification du mot de passe :", error));
        }
    }

    // Initialisation - récupérer le token et charger les utilisateurs
    function initialize() {
        fetch(`https://${window.ServerIP}:8443/SAE51/CheckPassword`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ login: "Admin1", password: "Admin", Test: false })
        })
        .then(response => response.json())
        .then(result => {
            token = result.token;
            fetchUsers(); // Récupère et affiche les utilisateurs après avoir obtenu le token
        })
        .catch(error => console.error("Erreur lors de l'authentification :", error));
    }

    // Exécute la fonction d'initialisation au chargement de la page
    window.onload = initialize;

})