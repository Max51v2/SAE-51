//Auteur : Maxime VALLET


document.addEventListener("TokenCheckFinished", () => {
    //Récupération des infos utilisateur
    token = sessionStorage.getItem('token');
    droits = sessionStorage.getItem('droits');
    test = "false";
    
    // Fonction principale pour charger la liste des PCs
    const loadPcList = async () => {
        try {
            console.log("Chargement de la liste des PCs...");
            const response = await fetch(`https://${window.ServerIP}:8443/SAE51/ListPC`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ token: token, Test: test })
            });

            const data = await response.json();

            if (data.erreur) {
                console.error(`Erreur: ${data.erreur}`);
                alert(`Erreur: ${data.erreur}`);
                return;
            }

            // Nettoyer la table et afficher l'en-tête
            const tableBody = document.getElementById("pcTable");
            tableBody.innerHTML = ``;

            // Génération des lignes pour chaque PC
            data.forEach(pc => {
                tableBody.innerHTML += `
                    <th>${pc.id}</th>
                    <th>${pc.IP}</th>
                    <th>
                        <button class="button-control">Eteindre</button>
                        <button class="button-control">Redémarrer</button>
                        <button class="button-control">MAJ</button>
                    <th>
                `;
                
                tableBody.appendChild(row);
            });

            console.log("Liste des PCs chargée.");
        } catch (error) {
            console.error("Erreur lors du chargement :", error);
        }
    };

    loadPcList();
});