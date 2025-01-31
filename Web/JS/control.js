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
            c=0;
            data.forEach(pc => {
                tableBody.innerHTML += `
                    <th>${pc.id}</th>
                    <th>${pc.IP}</th>
                    <th id="Status${c}"></th>
                    <th id="Actions${c}"><th>
                `;

                c = c+1;
            });

            loadPCStatus();

            console.log("Liste des PCs chargée.");
        } catch (error) {
            console.error("Erreur lors du chargement :", error);
        }
    };



    //Ajout du status des PC
    async function loadPCStatus(){
        response = await fetch(`https://${window.ServerIP}:8443/SAE51/ListPCStatus`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ token: token, Test: test })
        });

        data = await response.json();

        if (data.erreur) {
            console.error(`Erreur: ${data.erreur}`);
            alert(`Erreur: ${data.erreur}`);
            return;
        }

        c=0;
        //Pas besoin de check l'ordre des id car les servlets ListPC et ListPCStatus renvoient leurs info triées par id ASC
        data.forEach(pc => {
            StatusID = document.getElementById("Status"+c);

            StatusID.innerHTML = `${pc.status}`;

            ActionsID = document.getElementById("Actions"+c);

            if(pc.status === "En Ligne"){
                ActionsID.innerHTML = `
                    <button class="button-control" onclick="">Éteindre</button>
                    <button class="button-control" onclick="">Redémarrer</button>
                    <button class="button-control" onclick="">MAJ</button>
                `;
            }
            else{
                ActionsID.innerHTML = `
                    <button class="buttonForbidden">Éteindre</button>
                    <button class="buttonForbidden">Redémarrer</button>
                    <button class="buttonForbidden">MAJ</button>
                `;
            }

            c = c+1;
        });

        
    }


    loadPcList();
});