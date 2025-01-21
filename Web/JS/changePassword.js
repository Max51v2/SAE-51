document.addEventListener("TokenCheckFinished", () => {
    // Récupération des informations utilisateur
    const userLogin = sessionStorage.getItem("login");
    const token = sessionStorage.getItem("token");

    // Gestion de la soumission du formulaire
    document.getElementById("changePasswordForm").addEventListener("submit", (event) => {
        event.preventDefault();

        const newPassword = document.getElementById("newPassword").value;

        if (!newPassword) {
            alert("Veuillez entrer un nouveau mot de passe.");
            return;
        }

        // Requête pour modifier le mot de passe
        fetch(`https://${window.ServerIP}:8443/SAE51/SetPassword`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ target: userLogin, password: newPassword, token: token, Test: false })
        })
        .then(response => response.json())
        .then(result => {
            if (result.erreur === "none") {
                alert("Mot de passe modifié avec succès !");
                document.getElementById("changePasswordForm").reset(); // Réinitialise le formulaire
            } else {
                alert("Erreur lors de la modification du mot de passe. : "+response.erreur);
                console.error(result);
            }
        })
        .catch(error => {
            alert("Une erreur est survenue lors de la requête");
            console.error(error);
        });
    });
});
