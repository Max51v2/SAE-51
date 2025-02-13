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
const pcThresholds = document.getElementById("pcThresholds");
const backToListBtn1 = document.getElementById("backToList1");
const backToListBtn2 = document.getElementById("backToList2");
const backToListBtn3 = document.getElementById("backToList3");
const backToListBtn4 = document.getElementById("backToList4");
const refreshTresholds = document.getElementById("refreshTresholds");
const sendTresholds = document.getElementById("sendTresholds");
const staticInfoTable = document.querySelector("#staticInfoTable");
const rightsTable = document.querySelector("rightsTable");
const DynInfoTable2 = document.getElementById("DynInfoTable2");
date = "00000000"; //palceholder
time = "000000"; //placeholder
idPc = 0;
let token = sessionStorage.getItem('token');
let droits = sessionStorage.getItem('droits');

// SLIDERS ET INPUT SEUILS //
document.addEventListener("DOMContentLoaded", function() {

    //////// SLIDERS ////////

    //CPUUtilization
    var CPUUtilizationSlider = document.getElementById("CPUUtilizationSlider");
    var CPUUtilizationVal = document.getElementById("CPUUtilizationVal");
    CPUUtilizationSlider.oninput = function() {
        CPUUtilizationVal.innerHTML = `${this.value}%`;
    }

    //RAMUtilization
    var RAMUtilizationSlider = document.getElementById("RAMUtilizationSlider");
    var RAMUtilizationVal = document.getElementById("RAMUtilizationVal");
    RAMUtilizationSlider.oninput = function() {
        RAMUtilizationVal.innerHTML = `${this.value}%`;
    }

    //storageLoad
    var storageLoadSlider = document.getElementById("storageLoadSlider");
    var storageLoadVal = document.getElementById("storageLoadVal");
    storageLoadSlider.oninput = function() {
        storageLoadVal.innerHTML = `${this.value}%`;
    }

    //networkBandwidth
    var networkBandwidthSlider = document.getElementById("networkBandwidthSlider");
    var networkBandwidthVal = document.getElementById("networkBandwidthVal");
    networkBandwidthSlider.oninput = function() {
        networkBandwidthVal.innerHTML = `${this.value}%`;
    }

    //fanSpeed
    var fanSpeedSlider = document.getElementById("fanSpeedSlider");
    var fanSpeedVal = document.getElementById("fanSpeedVal");
    fanSpeedSlider.oninput = function() {
        fanSpeedVal.innerHTML = `>${this.value}RPM`;
    }


    //////// INPUT ////////

    //CPUTemp
    var CPUTempText = document.getElementById("CPUTempText");
    var CPUTempVal = document.getElementById("CPUTempVal");
    CPUTempText.oninput = function() {
        CPUTempVal.innerHTML = `${this.value}°C`;
    }

    //CPUConsumption
    var CPUConsumptionText = document.getElementById("CPUConsumptionText");
    var CPUConsumptionVal = document.getElementById("CPUConsumptionVal");
    CPUConsumptionText.oninput = function() {
        CPUConsumptionVal.innerHTML = `${this.value}W`;
    }

    //storageLeft
    var storageLeftText = document.getElementById("storageLeftText");
    var storageLeftVal = document.getElementById("storageLeftVal");
    storageLeftText.oninput = function() {
        storageLeftVal.innerHTML = `${this.value}Go`;
    }

    //storageTemp
    var storageTempText = document.getElementById("storageTempText");
    var storageTempVal = document.getElementById("storageTempVal");
    storageTempText.oninput = function() {
        storageTempVal.innerHTML = `${this.value}°C`;
    }

    //storageErrors
    var storageErrorsText = document.getElementById("storageErrorsText");
    var storageErrorsVal = document.getElementById("storageErrorsVal");
    storageErrorsText.oninput = function() {
        storageErrorsVal.innerHTML = `${this.value} Erreurs`;
    }

    //networkLatency
    var networkLatencyText = document.getElementById("networkLatencyText");
    var networkLatencyVal = document.getElementById("networkLatencyVal");
    networkLatencyText.oninput = function() {
        networkLatencyVal.innerHTML = `${this.value}ms`;
    }
});
///////////////////
    

// Fonction principale pour charger la liste des PCs
    const loadPcList = async () => {
        try {
            console.log("Chargement de la liste des PCs...");
            const response = await fetch(`https://${window.ServerIP}:8443/SAE51/ListPC`, {
                method: "POST",
                headers: { "Content-Type": "application/json; charset=UTF-8" },
                body: JSON.stringify({ token: token, test: test })
            });

            const data = await response.json();

            if (data.erreur) {
                if(data.erreur === "Pas de PC dans la BD"){
                    return;
                }
                else{
                    console.error(`Erreur: ${data.erreur}`);
                    alert(`Erreur: ${data.erreur}`);
                    return;
                }
            }
            else{
                console.log("Liste des chargée");
            }

            // Nettoyer la table et afficher l'en-tête
            tableBody.innerHTML = `
                <li class="table-header">
                    <div class="col col-1">ID</div>
                    <div class="col col-2">IP</div>
                    <div class="col col-3">Status</div>
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
                        <div class="col col-3" data-label="IP" id="Status${c}"></div>
                        <div class="col col-4" data-label="Actions" id="ActionsA${c}"></div>
                    `;
                }
                else{
                    row.innerHTML = `
                        <div class="col col-1" data-label="ID">${pc.id}</div>
                        <div class="col col-2" data-label="IP">${pc.IP}</div>
                        <div class="col col-3" data-label="IP" id="Status${c}"></div>
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
                headers: { "Content-Type": "application/json; charset=UTF-8" },
                body: JSON.stringify({ id: pcId, token: token, test: test })
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
        pcThresholds.style.display = "none";
    };


    // Fonction pour afficher les détails d'un PC
    const showPcDyn = async (pcId) => {
        if (!pcId) {
            alert("ID du PC non valide.");
            async function getDynInfo(pcId) {
                console.log(`Chargement des infos dyn (ID : ${pcId})`);
            
                try {
                    const response = await fetch(`https://${window.ServerIP}:8443/SAE51/ListPCDynInfo`, {
                        method: "POST",
                        headers: { "Content-Type": "application/json; charset=UTF-8" },
                        body: JSON.stringify({ id: pcId, token: token, test: test })
                    });
            
                    if (!response.ok) {
                        throw new Error(`Erreur HTTP ${response.status}`);
                    }
            
                    const data = await response.json();
                    console.log("Données reçues :", data);
            
                    if (data.erreur && data.erreur !== "none") {
                        console.error(`Erreur: ${data.erreur}`);
                        alert(`Erreur: ${data.erreur}`);
                        return;
                    }
            
                    console.log(`Infos dyn chargées`);
            
                    // Enregistrement du timestamp
                    const { date, time } = data;
            
                    // Remplissage du tableau avec les nouvelles données
                    fillDynInfoTable(data, date, time);
            
                } catch (error) {
                    console.error("Erreur lors du chargement des infos dynamiques :", error);
                    alert("Impossible de récupérer les informations dynamiques.");
                }
            }
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


    async function getDynInfo(pcId) {
        console.log(`Chargement des infos dyn (ID : ${pcId})`);
    
        try {
            const response = await fetch(`https://${window.ServerIP}:8443/SAE51/ListPCDynInfo`, {
                method: "POST",
                headers: { "Content-Type": "application/json; charset=UTF-8" },
                body: JSON.stringify({ id: pcId, token: token, test: test })
            });
    
            if (!response.ok) {
                throw new Error(`Erreur HTTP ${response.status}`);
            }
    
            const data = await response.json();
            console.log("Données reçues :", data);
    
            if (data.erreur && data.erreur !== "none") {
                console.error(`Erreur: ${data.erreur}`);
                alert(`Erreur: ${data.erreur}`);
                return;
            }
    
            console.log(`Infos dyn chargées`);
    
            // Enregistrement du timestamp
            const { date, time } = data;
    
            // Remplissage du tableau avec les nouvelles données
            fillDynInfoTable(data, date, time);
    
        } catch (error) {
            console.error("Erreur lors du chargement des infos dynamiques :", error);
            alert("Impossible de récupérer les informations dynamiques.");
        }
    }
    
    const fillDynInfoTable = (data, date, time) => {
        let tableContent = ``;
    
        // Section CPU
        if (data.CPU && data.CPU.length > 0) {
            const cpu = data.CPU[0];
            tableContent += `
                <tr><td colspan="2"><b>CPU</b></td></tr>
                <tr><td>Utilisation CPU</td><td>${cpu.CPUUtilization || "N/A"}%</td></tr>
                <tr><td>Température CPU</td><td>${cpu.CPUTemp || "N/A"}°C</td></tr>
                <tr><td>Consommation CPU</td><td>${cpu.CPUConsumption || "N/A"}W</td></tr>
            `;
        }
    
        // Section RAM
        if (data.RAM && data.RAM.length > 0) {
            const ram = data.RAM[0];
            tableContent += `
                <tr><td colspan="2"><b>RAM</b></td></tr>
                <tr><td>Utilisation RAM</td><td>${ram.RAMUtilization || "N/A"}%</td></tr>
            `;
        }
    
        // Section Network
        if (data.Network && data.Network.length > 0) {
            data.Network.forEach((network) => {
                tableContent += `
                    <tr><td colspan="2"><b>${network.networkName || "N/A"}</b></td></tr>
                    <tr><td>Latence Réseau</td><td>${network.networkLatency || "N/A"} ms</td></tr>
                    <tr><td>Bande Passante Réseau</td><td>${network.networkBandwidth || "N/A"}%</td></tr>
                `;
            });
        }
    
        // Section Storage
        if (data.Storage && data.Storage.length > 0) {
            data.Storage.forEach((storage) => {
                tableContent += `
                    <tr><td colspan="2"><b>${storage.storageName || "N/A"}</b></td></tr>
                    <tr><td>Utilisation Stockage</td><td>${storage.storageLoad || "N/A"}%</td></tr>
                    <tr><td>Espace Restant</td><td>${storage.storageLeft || "N/A"} Go</td></tr>
                    <tr><td>Température Stockage</td><td>${storage.storageTemp || "N/A"}°C</td></tr>
                    <tr><td>Erreurs Stockage</td><td>${storage.storageErrors || "N/A"} Erreurs</td></tr>
                `;
            });
        }
    
        // Section Fans
        tableContent += `<tr><td colspan="2"><b>Ventilateurs</b></td></tr>`;
        if (data.Fans && data.Fans.length > 0) {
            data.Fans.forEach((fan, index) => {
                tableContent += `<tr><td>Vitesse Ventilateur ${index + 1}</td><td>${fan.fanSpeed || "N/A"}RPM</td></tr>`;
            });
        }
    
        DynInfoTable2.innerHTML = tableContent;
    
        // Gestion de l'affichage des sections
        mainContent.style.display = "none";
        pcDetailsPage.style.display = "none";
        pcDynPage.style.display = "block";
        pcRights.style.display = "none";
        pcThresholds.style.display = "none";
    
        // Ajout de la date de dernier rafraîchissement
        if (date.length === 8 && time.length === 6) {
            const jour = date.substring(6, 8);
            const mois = date.substring(4, 6);
            const annee = date.substring(0, 4);
            const heure = time.substring(0, 2);
            const minute = time.substring(2, 4);
            const seconde = time.substring(4, 6);
    
            document.getElementById('RefreshText').innerHTML = `Enregistré à ${heure}:${minute}:${seconde} le ${jour}/${mois}/${annee}`;
        } else {
            document.getElementById('RefreshText').innerHTML = "Données de date/heure incorrectes";
        }
    };
    


    //Vérifie s'il faut actualiser les données dynamiques
    async function checkDynInfoRefresh(pcId){
        refreshIntervall = 5;

        //Tant que le tableau dyn est affiché
        while (DynRefreshMap.get(1)){
            c2=0;

            while(c2 < refreshIntervall && DynRefreshMap.get(1)){
                //DOM refresh txt
                document.getElementById('RefreshCount').innerHTML=`Rafraîchissement dans ${refreshIntervall - c2}s`;

                await Wait(1000);

                c2 = c2+1;
            }

            if(DynRefreshMap.get(1)){
                console.log(`Refresh infos dyn (ID : ${pcId})`);

                document.getElementById('RefreshCount').innerHTML=`Rafraîchissement...`;

                //On vérifie si le serveur a de nouvelles infos à distribuer
                const response = await fetch(`https://${window.ServerIP}:8443/SAE51/IsDynamicInfoUpToDate`, {
                    method: "POST",
                    headers: { "Content-Type": "application/json; charset=UTF-8" },
                    body: JSON.stringify({ id: pcId, date: date, time: time, token: token, test: test })
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
        pcThresholds.style.display = "none";
    });


    // Fonction pour retourner à la liste principale
    backToListBtn2.addEventListener("click", () => {
        mainContent.style.display = "block";
        pcDetailsPage.style.display = "none";
        pcDynPage.style.display = "none";
        pcRights.style.display = "none";
        pcThresholds.style.display = "none";
    });


    // Fonction pour retourner à la liste principale
    backToListBtn3.addEventListener("click", () => {
        //Arrêt de l'act des données dyn en arrière plan
        DynRefreshMap.set(1, false);

        mainContent.style.display = "block";
        pcDetailsPage.style.display = "none";
        pcDynPage.style.display = "none";
        pcRights.style.display = "none";
        pcThresholds.style.display = "none";
    });


    // Fonction pour retourner à la liste principale
    function backToList4(){
        mainContent.style.display = "block";
        pcDetailsPage.style.display = "none";
        pcDynPage.style.display = "none";
        pcRights.style.display = "none";
        pcThresholds.style.display = "none";
    }
        
    


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
        } else if (event.target.classList.contains("tresholdBtn")) {
            const pcId = event.target.getAttribute("data-id");
            console.log(`Bouton Modif seuils (ID: ${pcId})`);
            document.getElementById('pcThresholds2').innerHTML=`
                <button id="refreshTresholds" type="button" class="back-button" onClick="getTresholds('${pcId}')">Reset</button>
                <button id="sendTresholds" type="button" class="back-button" onClick="setThreshold('${pcId}')">Envoyer</button>
                <button id="backToList4" type="button" class="back-button" onClick="backToList4()">Retour à la Liste</button>
            `;
            getTresholds(pcId);
        }
    });


    // Fonction pour supprimer un PC
    const deletePc = async (pcId) => {
        try {
            const response = await fetch(`https://${window.ServerIP}:8443/SAE51/DeletePC`, {
                method: "POST",
                headers: { "Content-Type": "application/json; charset=UTF-8" },
                body: JSON.stringify({ token:token, id: pcId, test: test })
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
            headers: { "Content-Type": "application/json; charset=UTF-8" },
            body: JSON.stringify({ token: token, test: test })
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
                StatusID.innerHTML = `🟢 On`;
            }
            else{
                StatusID.innerHTML = `🔴 Off`;
            }
            
            //Remplissage des bouttons pour un Admin
            if(document.getElementById("ActionsA"+c)){

                ActionsID = document.getElementById("ActionsA"+c);

                if(pc.status === "En Ligne"){
                    ActionsID.innerHTML = `
                        <button class="button-25 viewBtn" data-id="${pc.id}">Infos statiques</button>
                        <button class="button-25 viewDynBtn" data-id="${pc.id}">Infos dynamiques</button>
                        <button class="rightsBtn" data-id="${pc.id}">Droits d'accès</button>
                        <button class="tresholdBtn" data-id="${pc.id}">Seuils</button>
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
                        <button class="tresholdBtn" data-id="${pc.id}">Seuils</button>
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
                headers: { "Content-Type": "application/json; charset=UTF-8" },
                body: JSON.stringify({ id: pcId, hasAccess: "true", token: token, test: test })
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
            pcThresholds.style.display = "none";

            fillRightsTableAllowed(data, pcId);

            console.log(`Chargement des utilisateurs ne possédant pas les droits (ID : ${pcId})`);
            response = await fetch(`https://${window.ServerIP}:8443/SAE51/ListUsersWithAccess`, {
                method: "POST",
                headers: { "Content-Type": "application/json; charset=UTF-8" },
                body: JSON.stringify({ id: pcId, hasAccess: "false", token: token, test: test })
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
    async function addUser(login, idPc){
        console.log(`Ajout des droits à \"${login}\" (ID : ${idPc})`);

        response = await fetch(`https://${window.ServerIP}:8443/SAE51/AddUserToPC`, {
            method: "POST",
            headers: { "Content-Type": "application/json; charset=UTF-8" },
            body: JSON.stringify({ id: idPc, login: login, token: token, test: test })
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
        getRights(idPc);
    }


    //Retrait des droits d'accès au pc pour un utilisateur défini
    async function deleteUser(login, idPc) {
        console.log(`Retrait des droits à \"${login}\" (ID : ${idPc})`);
        response = await fetch(`https://${window.ServerIP}:8443/SAE51/DeleteUserFromPC`, {
            method: "POST",
            headers: { "Content-Type": "application/json; charset=UTF-8" },
            body: JSON.stringify({ id: idPc, login: login, token: token, test: test })
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
        getRights(idPc);
    }


    //Récupère les seuils
    async function getTresholds(idPc){
        await fetchTresholds(idPc);

        //Ajout des valeurs des Sliders
        CPUUtilizationVal.innerHTML = `${CPUUtilizationSlider.value}%`;
        RAMUtilizationVal.innerHTML = `${RAMUtilizationSlider.value}%`;
        storageLoadVal.innerHTML = `${storageLoadSlider.value}%`;
        networkBandwidthVal.innerHTML = `${networkBandwidthSlider.value}%`;
        fanSpeedVal.innerHTML = `>${fanSpeedSlider.value}RPM`;
        
        //Ajout des valeurs des inputs
        CPUTempVal.innerHTML = `${CPUTempText.value}°C`;
        CPUConsumptionVal.innerHTML = `${CPUConsumptionText.value}W`;
        storageLeftVal.innerHTML = `${storageLeftText.value}Go`;
        storageTempVal.innerHTML = `${storageTempText.value}°C`;
        storageErrorsVal.innerHTML = `${storageErrorsText.value} Erreurs`;
        networkLatencyVal.innerHTML = `${networkLatencyText.value}ms`;

        //Affichage du tableau
        mainContent.style.display = "none";
        pcDetailsPage.style.display = "none";
        pcDynPage.style.display = "none";
        pcRights.style.display = "none";
        pcThresholds.style.display = "block";
    }


    async function fetchTresholds(idPc){
        console.log(`Récupération des seuils (ID : ${idPc})`);
        response = await fetch(`https://${window.ServerIP}:8443/SAE51/GetPCThresholds`, {
            method: "POST",
            headers: { "Content-Type": "application/json; charset=UTF-8" },
            body: JSON.stringify({ id: idPc, token: token, test: test })
        });

        data = await response.json();

        if(data.erreur === "Pas d'informations dans la table"){
            //On laise les valeurs par défaut dans la page
            return
        }
        else if (data.erreur !== "none") {
            console.error(`Erreur: ${data.erreur}`);
            alert(`Erreur: ${data.erreur}`);
            return;
        }

        //Ajout des valeurs des Sliders
        CPUUtilizationSlider.value = data.CPUUtilization;
        RAMUtilizationSlider.value = data.RAMUtilization;
        storageLoadSlider.value = data.storageLoad;
        networkBandwidthSlider.value = data.networkBandwith;
        fanSpeedSlider.value = data.fanSpeed;
        
        //Ajout des valeurs des inputs
        CPUTempText.value = data.CPUTemp;
        CPUConsumptionText.value = data.CPUConsumption;
        storageLeftText.value = data.storageLeft;
        storageTempText.value = data.storageTemp;
        storageErrorsText.value = data.storageErrors;
        networkLatencyText.value = data.networkLatency;
    }


    async function setThreshold(idPc){
        //Récupération des valeurs des Sliders
        CPUUtilization = CPUUtilizationSlider.value;
        RAMUtilization = RAMUtilizationSlider.value;
        storageLoad = storageLoadSlider.value;
        networkBandwidth = networkBandwidthSlider.value;
        fanSpeed = fanSpeedSlider.value;
        
        //Récupération des valeurs des inputs
        CPUTemp = CPUTempText.value;
        CPUConsumption = CPUConsumptionText.value;
        storageLeft = storageLeftText.value;
        storageTemp = storageTempText.value;
        storageErrors = storageErrorsText.value;
        networkLatency = networkLatencyText.value;

        console.log(`Envoi des seuils (ID : ${idPc})`);
        response = await fetch(`https://${window.ServerIP}:8443/SAE51/SetThresholds`, {
            method: "POST",
            headers: { "Content-Type": "application/json; charset=UTF-8" },
            body: JSON.stringify({ CPUUtilization: CPUUtilization, RAMUtilization: RAMUtilization,
                storageLoad: storageLoad, networkBandwith: networkBandwidth, 
                fanSpeed: fanSpeed, CPUTemp: CPUTemp, CPUConsumption: CPUConsumption, storageLeft: storageLeft, 
                storageTemp: storageTemp, storageErrors: storageErrors, networkLatency: networkLatency, id: idPc, token: token, test: test })
        });

        data = await response.json();

        if (data.erreur !== "none") {
            console.error(`Erreur: ${data.erreur}`);
            alert(`Erreur: ${data.erreur}`);
            return;
        }
    }

document.addEventListener("TokenCheckFinished", () => {
    //Récupération des infos utilisateur
    token = sessionStorage.getItem('token');
    droits = sessionStorage.getItem('droits');

    //Afichage de la liste des PC
    loadPcList();
});