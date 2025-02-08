document.addEventListener("TokenCheckFinished", function() {
    const logTableBody = document.querySelector("#logTable tbody");
    const filterBtn = document.getElementById("filterBtn");
    const errorMsg = document.getElementById("errorMsg");

    const token = sessionStorage.getItem("token");
    const test = "false";

    // Fonction pour récupérer et afficher les logs
    async function fetchLogs(beginDate = "", endDate = "", logLevel = "All") {
        try {
            errorMsg.textContent = "";
            logTableBody.innerHTML = "<tr><td colspan='6'>Chargement...</td></tr>";

            // Vérifier que tous les champs requis sont présents
            if (!token) {
                throw new Error("Token manquant");
            }

            // Obtenir la date et l'heure actuelles pour la date de fin
            const currentDate = new Date();
            const requestDate = formatDate(currentDate);

            // Si la date de début n'est pas fournie, afficher un message d'erreur et arrêter
            if (!beginDate) {
                throw new Error("Date de début manquante");
            }

            const bodyData = {
                logLevelReq: logLevel,
                token: token,
                test: test,
                beginDate: formatDate(new Date(beginDate)),
                endDate: requestDate // Utiliser la date actuelle pour la date de fin
            };

            console.log("Corps de la requête:", bodyData); // Debug: Afficher le corps de la requête

            const response = await fetch(`https://${window.ServerIP}:8443/SAE51/GetLogs`, {
                method: "POST",
                headers: { "Content-Type": "application/json; charset=UTF-8" },
                body: JSON.stringify(bodyData)
            });

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            const data = await response.json();
            logTableBody.innerHTML = "";

            if (data.erreur) {
                errorMsg.textContent = `Erreur: ${data.erreur}`;
                return;
            }

            data.forEach(log => {
                const row = `<tr>
                    <td>${log.id}</td>
                    <td>${log.date}</td>
                    <td>${log.login}</td>
                    <td>${log.ip}</td>
                    <td>${log.servlet}</td>
                    <td class="${log.erreur !== "none" ? "log-error" : "log-info"}">${log.erreur}</td>
                </tr>`;
                logTableBody.innerHTML += row;
            });

        } catch (error) {
            errorMsg.textContent = `Erreur lors du chargement des logs: ${error.message}`;
            console.error(error);
        }
    }

    // Fonction pour formater la date selon AAAAMMJJ_hhmmss
    function formatDate(date) {
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        const hours = String(date.getHours()).padStart(2, '0');
        const minutes = String(date.getMinutes()).padStart(2, '0');
        const seconds = String(date.getSeconds()).padStart(2, '0');
        return `${year}${month}${day}_${hours}${minutes}${seconds}`;
    }

    // Appliquer un filtre sur les logs
    filterBtn.addEventListener("click", function() {
        const beginDate = document.getElementById("beginDate").value;
        const logLevel = document.getElementById("logLevel").value;

        // Log des valeurs des dates pour le débogage
        console.log("Begin Date:", beginDate);

        if (!beginDate) {
            errorMsg.textContent = "Veuillez sélectionner une date de début.";
            return;
        }

        fetchLogs(beginDate, "", logLevel);
    });
});