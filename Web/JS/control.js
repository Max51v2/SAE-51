//Auteur : Maxime VALLET


// Récupération des infos utilisateur
token = sessionStorage.getItem('token');
droits = sessionStorage.getItem('droits');
test = "false";
window.doAction = doAction;


//Map pour suivre l'état des spinners par ligne
let runningMap = new Map();

// Modification de l'état d'une machine
async function doAction(message, id, line) {
    //Animation : on démarre le spinner pour cette ligne
    runningMap.set(line, true);
    spinner(line);

    //Attente de la réponse du serveur
    let response = await fetch(`https://${window.ServerIP}:8443/SAE51/ChangePCState`, {
        method: "POST",
        headers: { "Content-Type": "application/json; charset=UTF-8" },
        body: JSON.stringify({ token: token, id: id, message: message, test: test })
    });

    let data = await response.json();

    //Fin animation  + délais act serveur
    await Wait(1000);
    runningMap.set(line, false);

    //Act status
    loadPCStatus();

    if (data.erreur !== "none") {
        console.error(`Erreur: ${data.erreur}`);
        alert(`Erreur: ${data.erreur}`);
        return;
    }
}


//Animation
async function spinner(line) {
    //Désactivation des bouttons
    ActionsID = document.getElementById("Actions"+line);
        
    ActionsID.innerHTML = `
        <button class="buttonForbidden">Éteindre</button>
        <button class="buttonForbidden">Redémarrer</button>
        <button class="buttonForbidden">MAJ</button>
    `;
    
    //Remplacement du status par le spinner
    let statusElement = document.getElementById("Status" + line);

    let charList = ["⠇", "⠋", "⠙", "⠸", "⠴", "⠦"];
    let c2 = 0;

    while (runningMap.get(line)) {
        statusElement.innerHTML = `${charList[c2]} Chargement`;

        await Wait(200);

        c2 = (c2 + 1) % charList.length;
    }

    //Activation des bouttons
    ActionsID.innerHTML = `
        <button class="button-control" onclick="doAction('shutdown', ${pc.id}, ${c})">Éteindre</button>
        <button class="button-control" onclick="doAction('restart', ${pc.id}, ${c})">Redémarrer</button>
        <button class="button-control" onclick="doAction('update', ${pc.id}, ${c})">MAJ</button>
    `;
}


//Fonction sleep
function Wait(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}


//Ajout du status des PC
async function loadPCStatus(){
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

        ActionsID = document.getElementById("Actions"+c);

        if(pc.status === "En Ligne"){
            ActionsID.innerHTML = `
                <button class="button-control" onclick="doAction('shutdown', ${pc.id}, ${c})">Éteindre</button>
                <button class="button-control" onclick="doAction('restart', ${pc.id}, ${c})">Redémarrer</button>
                <button class="button-control" onclick="doAction('update', ${pc.id}, ${c})">MAJ</button>
            `;
        }
        else{
            ActionsID.innerHTML = `
                <span title="PC Hors Ligne">
                    <button class="buttonForbidden">Éteindre</button>
                </span>
                <span title="PC Hors Ligne">
                    <button class="buttonForbidden">Redémarrer</button>
                </span>
                <span title="PC Hors Ligne">
                    <button class="buttonForbidden">MAJ</button>
                </span>
            `;
        }

        c = c+1;
    });
}



document.addEventListener("TokenCheckFinished", () => {
    
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
    

    loadPcList();
});