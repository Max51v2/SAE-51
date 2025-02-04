document.addEventListener("TokenCheckFinished", () => {
    const userLogin = sessionStorage.getItem("login");
    const token = sessionStorage.getItem("token");

    const newPasswordInput = document.getElementById("newPassword");
    const confirmPasswordInput = document.getElementById("confirmPassword");
    const errorMessage = document.getElementById("errorMessage");

    // Vérification en temps réel des mots de passe
    confirmPasswordInput.addEventListener("input", () => {
        if (newPasswordInput.value === confirmPasswordInput.value) {
            errorMessage.style.display = "none";
            confirmPasswordInput.style.borderColor = "#00cc66"; // Vert si OK
        } else {
            errorMessage.style.display = "block";
            confirmPasswordInput.style.borderColor = "#ff0000"; // Rouge si non OK
        }
    });

    document.getElementById("changePasswordForm").addEventListener("submit", (event) => {
        event.preventDefault();

        const newPassword = newPasswordInput.value;
        const confirmPassword = confirmPasswordInput.value;

        if (newPassword !== confirmPassword) {
            errorMessage.style.display = "block";
            return;
        }

        fetch(`https://${window.ServerIP}:8443/SAE51/SetPassword`, {
            method: "POST",
            headers: { "Content-Type": "application/json; charset=UTF-8" },
            body: JSON.stringify({ target: userLogin, password: newPassword, token: token, Test: false })
        })
        .then(response => response.json())
        .then(result => {
            if (result.erreur === "none") {
                alert("Mot de passe modifié avec succès !");
                document.getElementById("changePasswordForm").reset();
                confirmPasswordInput.style.borderColor = "#444"; // Réinitialisation de la bordure
            } else {
                alert("Erreur lors de la modification du mot de passe : " + result.erreur);
                console.error(result);
            }
        })
        .catch(error => {
            alert("Une erreur est survenue lors de la requête");
            console.error(error);
        });
    });
});
