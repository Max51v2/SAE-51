document.addEventListener("DOMContentLoaded", () => {
    const token = "userToken"; // Assurez-vous que ce token est valide
    const test = true;

    const tableBody = document.querySelector("#pcList");
    const mainContent = document.getElementById("mainContent");
    const pcDetailsPage = document.getElementById("pcDetailsPage");
    const backToListBtn = document.getElementById("backToList");
    const staticInfoTable = document.querySelector("#staticInfoTable tbody");

    // Fonction principale pour charger la liste des PCs
    const loadPcList = async () => {
        try {
            console.log("Chargement de la liste des PCs...");
            const response = await fetch(`https://${window.ServerIP}:8443/SAE51/ListPc`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ token:token, Test: test })
            });

            const data = await response.json();

            if (data.erreur) {
                console.error(`Erreur: ${data.erreur}`);
                alert(`Erreur: ${data.erreur}`);
                return;
            }

            // Nettoyer la table et afficher l'en-tête
            tableBody.innerHTML = `
                <li class="table-header">
                    <div class="col col-1">ID</div>
                    <div class="col col-2">IP</div>
                    <div class="col col-3">Statut</div>
                    <div class="col col-4">Actions</div>
                </li>
            `;

            // Vérification si la liste est vide
            if (data.length === 0) {
                addTestPcToList();
                return;
            }

            // Génération des lignes pour chaque PC
            data.forEach(pc => {
                const row = document.createElement("li");
                row.className = "table-row";
                row.innerHTML = `
                    <div class="col col-1" data-label="ID">${pc.id}</div>
                    <div class="col col-2" data-label="IP">${pc.IP}</div>
                    <div class="col col-3" data-label="Statut">${pc.status || "Inconnu"}</div>
                    <div class="col col-4" data-label="Actions">
                        <button class="deleteBtn" data-id="${pc.id}">Supprimer</button>
                        <button class="button-25 viewBtn" data-id="${pc.id}">Voir</button>
                    </div>
                `;
                tableBody.appendChild(row);
            });

            console.log("Liste des PCs chargée.");
        } catch (error) {
            console.error("Erreur lors du chargement :", error);
        }
    };

    // Ajouter un PC de test à la liste
    const addTestPcToList = () => {
        const testPc = {
            id: 1,
            IP: "192.168.0.1",
            status: "En ligne"
        };

        const row = document.createElement("li");
        row.className = "table-row";
        row.innerHTML = `
            <div class="col col-1" data-label="ID">${testPc.id}</div>
            <div class="col col-2" data-label="IP">${testPc.IP}</div>
            <div class="col col-3" data-label="Statut">${testPc.status}</div>
            <div class="col col-4" data-label="Actions">
                <button class="deleteBtn" data-id="${testPc.id}">Supprimer</button>
                <button class="button-25 viewBtn" data-id="${testPc.id}">Voir</button>
            </div>
        `;
        tableBody.appendChild(row);
    };

    // Fonction pour afficher les détails d'un PC
    const showPcDetails = async (pcId) => {
        if (!pcId) {
            alert("ID du PC non valide.");
            return;
        }

        try {
            console.log(`Chargement des détails pour le PC ID: ${pcId}`);
            const response = await fetch(`https://${window.ServerIP}:8443/SAE51/ListPCStaticInfo`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ id: pcId, token:token, Test: test })
            });

            const data = await response.json();

            if (data.erreur && data.erreur !== "none") {
                console.error(`Erreur: ${data.erreur}`);
                alert(`Erreur: ${data.erreur}`);
                return;
            }

            fillStaticInfoTable(data);

        } catch (error) {
            console.error("Erreur lors du chargement des infos :", error);
        }
    };

    // Remplir le tableau avec les infos reçues
    const fillStaticInfoTable = (data) => {
        staticInfoTable.innerHTML = `
            <tr><td>ID</td><td>${data.id}</td></tr>
            <tr><td>Modèle CPU</td><td>${data.cpu_model}</td></tr>
            <tr><td>Nombre de Coeurs</td><td>${data.cores}</td></tr>
            <tr><td>Nombre de Threads</td><td>${data.threads}</td></tr>
            <tr><td>Fréquence Max CPU</td><td>${data.maximum_frequency}</td></tr>
            <tr><td>Quantité RAM</td><td>${data.ram_quantity}</td></tr>
            <tr><td>Nombre de Barrettes RAM</td><td>${data.dimm_quantity}</td></tr>
            <tr><td>Vitesse RAM</td><td>${data.dimm_speed}</td></tr>
            <tr><td>Nombre de Périphériques de Stockage</td><td>${data.storage_device_number}</td></tr>
            <tr><td>Capacité de Stockage</td><td>${data.storage_space}</td></tr>
            <tr><td>Nombre d'Interfaces Réseau</td><td>${data.network_int_number}</td></tr>
            <tr><td>Vitesse des Interfaces Réseau</td><td>${data.network_int_speed}</td></tr>
            <tr><td>Système d'Exploitation</td><td>${data.os}</td></tr>
            <tr><td>Version OS</td><td>${data.version}</td></tr>
        `;

        console.log("Détails du PC chargés et affichés.");
        
        // Cache la liste principale et affiche les détails
        mainContent.style.display = "none";
        pcDetailsPage.style.display = "block";
    };

    // Fonction pour retourner à la liste principale
    backToListBtn.addEventListener("click", () => {
        mainContent.style.display = "block";
        pcDetailsPage.style.display = "none";
    });

    // Gestion des événements via délégation
    document.addEventListener("click", (event) => {
        if (event.target.classList.contains("viewBtn")) {
            const pcId = event.target.getAttribute("data-id");
            console.log(`Bouton Voir cliqué pour le PC ID: ${pcId}`);
            showPcDetails(pcId);
        } else if (event.target.classList.contains("deleteBtn")) {
            const pcId = event.target.getAttribute("data-id");
            console.log(`Bouton Supprimer cliqué pour le PC ID: ${pcId}`);
            deletePc(pcId);
        }
    });

    // Fonction pour supprimer un PC
    const deletePc = async (pcId) => {
        try {
            const response = await fetch(`https://${window.ServerIP}:8443/SAE51/DeletePc`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ token:token, pcId })
            });

            const result = await response.json();
            if (result.erreur) {
                console.error(`Erreur: ${result.erreur}`);
                alert(`Erreur: ${result.erreur}`);
            } else {
                alert("PC supprimé avec succès");
                loadPcList();
            }
        } catch (error) {
            console.error("Erreur lors de la suppression :", error);
        }
    };

    loadPcList();
});