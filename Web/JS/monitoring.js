//Auteurs : Gabin PETITCOLAS et Maxime VALLET


//Map pour rafraichir les données dyn
let DynRefreshMap = new Map();

//Données
window.deleteUser = deleteUser;
window.addUser = addUser;
const test = "false";
const tableBody = document.querySelector("#pcList");
const mainContent = document.getElementById("MainTable");
const pcDetailsPage = document.getElementById("pcDetailsPage");
const pcDynPage = document.getElementById("pcDynPage");
const pcRights = document.getElementById("pcRights");
const backToListBtn1 = document.getElementById("backToList1");
const backToListBtn2 = document.getElementById("backToList2");
const backToListBtn3 = document.getElementById("backToList3");
const staticInfoTable = document.querySelector("#staticInfoTable");
const rightsTable = document.querySelector("rightsTable");
date = "00000000"; //palceholder
time = "000000"; //placeholder
    

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
            else{
                console.log("Liste des chargée");
            }

            // Nettoyer la table et afficher l'en-tête
            tableBody.innerHTML = `
                <li class="table-header">
                    <div class="col col-1">ID</div>
                    <div class="col col-2">IP</div>
                    <div class="col col-2">Status</div>
                    <div class="col col-4">Actions</div>
                </li>
            `;

            // Génération des lignes pour chaque PC
            c=0;
            data.forEach(pc => {
                const row = document.createElement("li");
                row.className = "table-row";

                //On affiche le boutton des droits d'accès que si c'est un admin
                if(droits === "Admin"){
                    row.innerHTML = `
                        <div class="col col-1" data-label="ID">${pc.id}</div>
                        <div class="col col-2" data-label="IP">${pc.IP}</div>
                        <div class="col col-2" data-label="IP" id="Status${c}"></div>
                        <div class="col col-4" data-label="Actions" id="ActionsA${c}"></div>
                    `;
                }
                else{
                    row.innerHTML = `
                        <div class="col col-1" data-label="ID">${pc.id}</div>
                        <div class="col col-2" data-label="IP">${pc.IP}</div>
                        <div class="col col-2" data-label="IP" id="Status${c}"></div>
                        <div class="col col-4" data-label="Actions" id="ActionsU${c}">
                            <button class="button-25 viewBtn" data-id="${pc.id}">Infos statiques</button>
                            <button class="button-25 viewDynBtn" data-id="${pc.id}">Infos dynamiques</button>
                        </div>
                    `;
                }
                
                tableBody.appendChild(row);

                c = c+1;
            });

            loadPCStatus();
        } catch (error) {
            console.error("Erreur lors du chargement :", error);
        }
    };


    // Fonction pour afficher les détails d'un PC
    const showPcDetails = async (pcId) => {
        if (!pcId) {
            alert("ID du PC non valide.");
            return;
        }

        try {
            console.log(`Chargement des infos statiques (ID : ${pcId})`);
            const response = await fetch(`https://${window.ServerIP}:8443/SAE51/ListPCStaticInfo`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ id: pcId, token: token, Test: test })
            });

            const data = await response.json();

            if (data.erreur && data.erreur !== "none") {
                console.error(`Erreur: ${data.erreur}`);
                alert(`Erreur: ${data.erreur}`);
                return;
            }
            else{
                console.log(`Infos statiques chargées`);
            }

            fillStaticInfoTable(data);

        } catch (error) {
            console.error("Erreur lors du chargement des infos :", error);
        }
    };


    // Remplir le tableau avec les droits des utilisateur
    const fillStaticInfoTable = (data) => {
        staticInfoTable.innerHTML = `
            <tr><td>ID</td><td>${data.id}</td></tr>
            <tr><td>Modèle CPU</td><td>${data.cpu_model}</td></tr>
            <tr><td>Nombre de Coeurs</td><td>${data.cores}</td></tr>
            <tr><td>Nombre de Threads</td><td>${data.threads}</td></tr>
            <tr><td>Fréquence Max CPU</td><td>${data.maximum_frequency} GHz</td></tr>
            <tr><td>Quantité RAM</td><td>${data.ram_quantity} Mo</td></tr>
            <tr><td>Nombre de Barrettes RAM</td><td>${data.dimm_quantity}</td></tr>
            <tr><td>Vitesse RAM</td><td>${data.dimm_speed} MT/s</td></tr>
            <tr><td>Nombre de Périphériques de Stockage</td><td>${data.storage_device_number}</td></tr>
            <tr><td>Capacité de Stockage</td><td>${data.storage_space} Go</td></tr>
            <tr><td>Nombre d'Interfaces Réseau</td><td>${data.network_int_number}</td></tr>
            <tr><td>Vitesse des Interfaces Réseau</td><td>${data.network_int_speed} Mo/s</td></tr>
            <tr><td>Système d'Exploitation</td><td>${data.os}</td></tr>
            <tr><td>Version OS</td><td>${data.version}</td></tr>
        `;
        
        // Cache la liste principale et affiche les détails
        mainContent.style.display = "none";
        pcDetailsPage.style.display = "block";
        pcDynPage.style.display = "none";
        pcRights.style.display = "none";
    };

    // Fonction pour afficher les détails d'un PC
    const showPcDyn = async (pcId) => {
        if (!pcId) {
            alert("ID du PC non valide.");
            return;
        }

        try {
            await getDynInfo(pcId);

            //Lancement du rafraichissement des données en arrière plan
            DynRefreshMap.set(1, true);
            checkDynInfoRefresh(pcId);

        } catch (error) {
            console.error("Erreur lors du chargement des infos :", error);
        }
    };

    //récupère les infos dyn
    async function getDynInfo(pcId){

        console.log(`Chargement des infos dyn (ID : ${pcId})`);
        const response = await fetch(`https://${window.ServerIP}:8443/SAE51/ListPCDynInfo`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ id: pcId, token: token, Test: test })
        });

        const data = await response.json();

        if (data.erreur && data.erreur !== "none") {
            console.error(`Erreur: ${data.erreur}`);
            alert(`Erreur: ${data.erreur}`);
            return;
        }
        else{
            console.log(`Infos dyn chargées`);

            //Enregistrement du timestamp
            date = data.date;
            time = data.time;
        }

        //Remplissage intial du tableau
        fillDynInfoTable(data);
    }

    // Remplir le tableau avec les droits des utilisateur
    const fillDynInfoTable = (data) => {
        DynInfoTable.innerHTML = `
            <tr><td>Coucou</td><td>salut</td></tr>
        `;
        
        // Cache la liste principale et affiche les détails
        mainContent.style.display = "none";
        pcDetailsPage.style.display = "none";
        pcDynPage.style.display = "block";
        pcRights.style.display = "none";

        //Ajout de la date de dernier rafraichissement
        jour = date.substring(6,8);
        mois = date.substring(4,6);
        annee = date.substring(0,4);
        heure = time.substring(0,2);
        minute = time.substring(2,4);
        seconde = time.substring(4,6);
        document.getElementById('RefreshText').innerHTML=`Enregistré à ${heure}:${minute}:${seconde} le ${jour}/${mois}/${annee}`;
    };

    //Vérifie s'il faut actualiser les données dynamiques
    async function checkDynInfoRefresh(pcId){
        refreshIntervall = 10;

        //Tant que le tableau dyn est affiché
        while (DynRefreshMap.get(1)){
            c2=0;

            while(c2 < refreshIntervall && DynRefreshMap.get(1)){
                //DOM refresh txt
                document.getElementById('RefreshCount').innerHTML=`Rafraîchis dans ${refreshIntervall - c2}s`;

                await Wait(1000);

                c2 = c2+1;
            }

            if(DynRefreshMap.get(1)){
                console.log(`Refresh infos dyn (ID : ${pcId})`);

                document.getElementById('RefreshCount').innerHTML=`Rafraîchissement...`;

                //On vérifie si le serveur a de nouvelles infos à distribuer
                const response = await fetch(`https://${window.ServerIP}:8443/SAE51/IsDynamicInfoUpToDate`, {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({ id: pcId, date: date, time: time, token: token, Test: test })
                });

                const data = await response.json();

                if (data.erreur && data.erreur !== "none") {
                    console.error(`Erreur: ${data.erreur}`);
                    alert(`Erreur: ${data.erreur}`);
                }
                else{
                    //S'il y'a de nouvelles infos, on les récupèrrent => tableau
                    if(data.isUpToDate === "false"){
                        await getDynInfo(pcId);

                        document.getElementById('RefreshCount').innerHTML=`Récupération des données...`;

                        await Wait(2000);
                    }
                    else{
                        console.log(`Infos dyn déjà à jour (ID : ${pcId})`);

                        document.getElementById('RefreshCount').innerHTML=`À jour`;

                        await Wait(2000);
                    }
                }
            }
        }
    }

    //Fonction sleep
    function Wait(ms) {
        return new Promise(resolve => setTimeout(resolve, ms));
    }

    // Fonction pour retourner à la liste principale
    backToListBtn1.addEventListener("click", () => {
        mainContent.style.display = "block";
        pcDetailsPage.style.display = "none";
        pcDynPage.style.display = "none";
        pcRights.style.display = "none";
    });

    // Fonction pour retourner à la liste principale
    backToListBtn2.addEventListener("click", () => {
        mainContent.style.display = "block";
        pcDetailsPage.style.display = "none";
        pcDynPage.style.display = "none";
        pcRights.style.display = "none";
    });

    // Fonction pour retourner à la liste principale
    backToListBtn3.addEventListener("click", () => {
        //Arrêt de l'act des données dyn en arrière plan
        DynRefreshMap.set(1, false);

        mainContent.style.display = "block";
        pcDetailsPage.style.display = "none";
        pcDynPage.style.display = "none";
        pcRights.style.display = "none";
    });

    // Gestion des événements via délégation
    document.addEventListener("click", (event) => {
        if (event.target.classList.contains("viewBtn")) {
            const pcId = event.target.getAttribute("data-id");
            console.log(`Bouton infos statiques cliqué (ID : ${pcId})`);
            showPcDetails(pcId);
        }
        else if (event.target.classList.contains("viewDynBtn")) {
            const pcId = event.target.getAttribute("data-id");
            console.log(`Bouton infos dynamiques cliqué (ID: ${pcId})`);
            showPcDyn(pcId);
        } else if (event.target.classList.contains("deleteBtn")) {
            const pcId = event.target.getAttribute("data-id");
            console.log(`Bouton Supprimer cliqué (ID: ${pcId})`);
            deletePc(pcId);
        } else if (event.target.classList.contains("rightsBtn")) {
            const pcId = event.target.getAttribute("data-id");
            console.log(`Bouton Modif droits (ID: ${pcId})`);
            getRights(pcId);
        }
    });

    // Fonction pour supprimer un PC
    const deletePc = async (pcId) => {
        try {
            const response = await fetch(`https://${window.ServerIP}:8443/SAE51/DeletePC`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ token:token, id: pcId, Test: test })
            });

            const result = await response.json();
            if (result.erreur) {
                console.error(`Erreur: ${result.erreur}`);
                alert(`Erreur: ${result.erreur}`);
            } else {
                loadPcList();

                console.log(`PC supprimé (ID : ${pcId})`);
            }
        } catch (error) {
            console.error("Erreur lors de la suppression :", error);
        }
    };


    //Ajout du status des PC
    async function loadPCStatus(){
        console.log(`Chargement du status des PC`);

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
        else{
            console.log(`Liste chargée`);
        }

        c=0;
        //Pas besoin de check l'ordre des id car les servlets ListPC et ListPCStatus renvoient leurs info triées par id ASC
        data.forEach(pc => {
            StatusID = document.getElementById("Status"+c);

            if(pc.status === "En Ligne"){
                StatusID.innerHTML = `🟢 ${pc.status}`;
            }
            else{
                StatusID.innerHTML = `🔴 ${pc.status}`;
            }
            
            //Remplissage des bouttons pour un Admin
            if(document.getElementById("ActionsA"+c)){

                ActionsID = document.getElementById("ActionsA"+c);

                if(pc.status === "En Ligne"){
                    ActionsID.innerHTML = `
                        <button class="button-25 viewBtn" data-id="${pc.id}">Infos statiques</button>
                        <button class="button-25 viewDynBtn" data-id="${pc.id}">Infos dynamiques</button>
                        <button class="rightsBtn" data-id="${pc.id}">Droits d'accès</button>
                        <button class="deleteBtn" data-id="${pc.id}">Supprimer</button>
                    `;
                }
                else{
                    ActionsID.innerHTML = `
                        <button class="button-25 viewBtn" data-id="${pc.id}">Infos statiques</button>
                        <span title="PC Hors Ligne">
                            <button class="buttonForbidden" data-id="${pc.id}">Infos dynamiques</button>
                        </span>
                        <button class="rightsBtn" data-id="${pc.id}">Droits d'accès</button>
                        <button class="deleteBtn" data-id="${pc.id}">Supprimer</button>
                    `;
                }
            }
            else{
                ActionsID = document.getElementById("ActionsU"+c);

                if(pc.status === "En Ligne"){
                    ActionsID.innerHTML = `
                        <button class="button-25 viewBtn" data-id="${pc.id}">Infos statiques</button>
                        <button class="button-25 viewDynBtn" data-id="${pc.id}">Infos dynamiques</button>
                    `;
                }
                else{
                    ActionsID.innerHTML = `
                        <button class="button-25 viewBtn" data-id="${pc.id}">Infos statiques</button>
                        <span title="PC Hors Ligne">
                            <button class="buttonForbidden" data-id="${pc.id}">Infos dynamiques</button>
                        </span>
                    `;
                }
            }

            

            c = c+1;
        });

        
    }


    // Fonction ajouter les droits des utilisateurs sur une machine
    const getRights = async (pcId) => {
        if (!pcId) {
            alert("ID du PC non valide.");
            return;
        }

        try {
            console.log(`Chargement des utilisateurs possédant les droits (ID : ${pcId})`);
            response = await fetch(`https://${window.ServerIP}:8443/SAE51/ListUsersWithAccess`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ id: pcId, hasAccess: "true", token: token, Test: test })
            });

            data = await response.json();

            if (data.erreur) {
                console.error(`Erreur: ${data.erreur}`);
                alert(`Erreur: ${data.erreur}`);
                return;
            }
            else{
                console.log(`Utilisateurs possédant les droits chargé`);
            }

            // Cache la liste principale et affiche les détails
            mainContent.style.display = "none";
            pcDetailsPage.style.display = "none";
            pcRights.style.display = "block";

            fillRightsTableAllowed(data, pcId);

            console.log(`Chargement des utilisateurs ne possédant pas les droits (ID : ${pcId})`);
            response = await fetch(`https://${window.ServerIP}:8443/SAE51/ListUsersWithAccess`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ id: pcId, hasAccess: "false", token: token, Test: test })
            });

            data = await response.json();

            if (data.erreur) {
                console.error(`Erreur: ${data.erreur}`);
                alert(`Erreur: ${data.erreur}`);
                return;
            }
            else{
                console.log(`Utilisateurs ne possédant pas les droits chargé`);
            }

            fillRightsTableForbidden(data, pcId);

        } catch (error) {
            console.error("Erreur lors du chargement des infos :", error);
        }
    };


    // Remplir le tableau avec les infos reçues
    const fillRightsTableAllowed = (data, pcId) => {

        const userTableBody = document.getElementById("allowed");

        if(data !== undefined){
            userTableBody.innerHTML = ""; // Vide le tableau avant d'ajouter les nouvelles données

            data.forEach(user => {
                if(user.canBeDeleted === "false"){
                    userTableBody.innerHTML += `
                    <span title="Le rôle Admin donne accès à tous les PC">
                        <button 
                            style="background: none; 
                                    border: none; 
                                    padding: 0; 
                                    display: flex; 
                                    margin-right: 20px;
                                    margin-left: 20px;
                                    padding-top: 8px;
                                    align-items: center;"">
                            <span style="background: url('./images/locked.webp') no-repeat center; 
                                        background-size: contain; 
                                        width: 25px; 
                                        height: 25px; 
                                        display: inline-block;
                                        margin-right: 10px;"></span>
                            <span style="font-size: 15px;">${user.user}</span>
                        </button>
                    </span>
                        <br>
                    `;
                }
                else{
                    userTableBody.innerHTML += `
                        <button 
                            style="background: none; 
                                    border: none; 
                                    padding: 0; 
                                    cursor: pointer; 
                                    display: flex; 
                                    margin-right: 20px;
                                    margin-left: 20px;
                                    padding-top: 8px;
                                    align-items: center;" 
                            onClick="deleteUser('${user.user}', '${pcId}')">
                            <span style="background: url('./images/trash.webp') no-repeat center; 
                                        background-size: contain; 
                                        width: 25px; 
                                        height: 25px; 
                                        display: inline-block;
                                        margin-right: 10px;"></span>
                            <span style="font-size: 15px;">${user.user}</span>
                        </button>
                        <br>
                    `;
                }
            });
        }
    };


    // Remplir le tableau avec les infos reçues
    const fillRightsTableForbidden = (data, pcId) => {

        const userTableBody = document.getElementById("forbidden");

        if(data !== undefined){
            userTableBody.innerHTML = ""; // Vide le tableau avant d'ajouter les nouvelles données

            data.forEach(user => {
                userTableBody.innerHTML += `
                    <button 
                        style="background: none; 
                                border: none; 
                                padding: 0; 
                                cursor: pointer; 
                                display: flex; 
                                margin-right: 20px;
                                margin-left: 20px;
                                padding-top: 8px;
                                align-items: center;" 
                        onClick="addUser('${user.user}', '${pcId}')">
                        <span style="background: url('./images/plus.webp') no-repeat center; 
                                    background-size: contain; 
                                    width: 25px; 
                                    height: 25px; 
                                    display: inline-block;
                                    margin-right: 10px;"></span>
                        <span style="font-size: 15px;">${user.user}</span>
                    </button>
                    <br>
                `;
            });
        }
    };


    //Ajout des droits d'accès au pc pour un utilisateur défini
    async function addUser(login, idPC){
        console.log(`Ajout des droits à \"${login}\" (ID : ${idPC})`);

        response = await fetch(`https://${window.ServerIP}:8443/SAE51/AddUserToPC`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ id: idPC, login: login, token: token, Test: test })
        });

        data = await response.json();

        if (data.erreur !== "none") {
            console.error(`Erreur: ${data.erreur}`);
            alert(`Erreur: ${data.erreur}`);
            return;
        }
        else{
            console.log(`Droits ajoutés à \"${login}\"`);
        }

        //Actualisation des droits
        getRights(idPC);
    }


    //Retrait des droits d'accès au pc pour un utilisateur défini
    async function deleteUser(login, idPC) {
        console.log(`Retrait des droits à \"${login}\" (ID : ${idPC})`);
        response = await fetch(`https://${window.ServerIP}:8443/SAE51/DeleteUserFromPC`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ id: idPC, login: login, token: token, Test: test })
        });

        data = await response.json();

        if (data.erreur !== "none") {
            console.error(`Erreur: ${data.erreur}`);
            alert(`Erreur: ${data.erreur}`);
            return;
        }
        else{
            console.log(`Droits retirés à \"${login}\"`);
        }

        //Actualisation des droits
        getRights(idPC);
    }



document.addEventListener("TokenCheckFinished", () => {
    //Récupération des infos utilisateur
    token = sessionStorage.getItem('token');
    droits = sessionStorage.getItem('droits');
    
    //Afichage de la liste des PC
    loadPcList();
});