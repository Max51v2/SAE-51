document.addEventListener("TokenCheckFinished", (event) => {
    window.deleteUser = deleteUser;
    window.changePassword = changePassword;

    const addUserButton = document.getElementById("addUserButton");
    const addUserModal = document.getElementById("addUserModal");
    const closeModal = document.querySelector(".close-button");
    const addUserForm = document.getElementById("addUserForm");

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
                        <button class="button-54" onclick="deleteUser('${user.login}')">Supprimer</button>
                        <button class="button-54" onclick="changePassword('${user.login}')">Modifier MDP</button>
                    </td>
                `;
                userTableBody.appendChild(row);
            });
        });
    }

    // Afficher le modal
    addUserButton.addEventListener("click", () => {
        addUserModal.style.display = "block";
    });

    // Fermer le modal
    closeModal.addEventListener("click", () => {
        addUserModal.style.display = "none";
    });

    window.addEventListener("click", (event) => {
        if (event.target === addUserModal) {
            addUserModal.style.display = "none";
        }
    });

    // Soumettre le formulaire pour ajouter un utilisateur
    addUserForm.addEventListener("submit", (event) => {
        event.preventDefault();
        const formData = new FormData(addUserForm);
        const user = {
            nom: formData.get("nom"),
            prenom: formData.get("prenom"),
            login: formData.get("login"),
            droits: formData.get("droits"),
            token: token
        };

        fetch(`https://${window.ServerIP}:8443/SAE51/AddUser`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(user)
        })
        .then(response => response.json())
        .then(result => {
            if (result.erreur === "none") {
                alert("Utilisateur ajouté avec succès !");
                addUserModal.style.display = "none";
                fetchUsers(); // Met à jour le tableau
            } else {
                alert("Erreur lors de l'ajout de l'utilisateur.");
                console.error(result);
            }
        });
    });

    // Supprimer un utilisateur
    function deleteUser(login) {
        const UserLogin = sessionStorage.getItem('login');

        if (UserLogin === login) {
            alert('Vous ne pouvez pas supprimer votre propre compte');
        } else {
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
                    console.log(result);
                }
            });
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
                    console.log(result);
                }
            });
        }
    }

    // Initialisation
    fetchUsers();
});
