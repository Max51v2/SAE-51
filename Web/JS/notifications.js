document.addEventListener("TokenCheckFinished", async function () {
    console.log("alert.js chargé !");
    const notificationList = document.getElementById("notificationList");

    if (!notificationList) {
        console.error("Erreur : #notificationList introuvable !");
        return;
    }

    const token = sessionStorage.getItem("token");
    if (!token) {
        console.warn("Aucun token trouvé !");
        notificationList.innerHTML = "<p class='error'>Accès refusé. Veuillez vous reconnecter.</p>";
        return;
    }

    // Fonction pour charger les notifications
    async function loadNotifications() {
        try {
            response = await fetch(`https://${window.ServerIP}:8443/SAE51/GetNotifications`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ token: token, Test: "false" })
            });

            console.log("Réponse du serveur reçue");
            if (!response.ok) {
                throw new Error(`Erreur HTTP : ${response.status}`);
            }

            const data = await response.json();
            console.log("Données JSON reçues");

            notificationList.innerHTML = ""; // Nettoie les notifications précédentes

            if (data.erreur) {
                console.warn("Erreur reçue du serveur :", data.erreur);
                notificationList.innerHTML = `<p class='error'>Erreur : ${data.erreur}</p>`;
            } else if (!Array.isArray(data) || data.length === 0) {
                console.log("Aucune notification reçue.");
                notificationList.innerHTML = `<p class='notification'>Aucune nouvelle notification.</p>`;
            } else {
                data.forEach(notification => displayNotification(notification));
            }
        } catch (error) {
            console.error("Erreur lors du chargement des notifications :", error);
            notificationList.innerHTML = "<p class='error'>Une erreur est survenue lors du chargement des notifications.</p>";
        }
    }

    // Charger immédiatement les notifications
    loadNotifications();

    // Actualiser les notifications toutes les 30 secondes
    setInterval(loadNotifications, 30000);
});

function displayNotification(notification) {
    const notificationList = document.getElementById("notificationList");
    if (!notificationList) {
        console.error("Erreur : #notificationList introuvable !");
        return;
    }

    const notifElement = document.createElement("div"); 
    notifElement.classList.add("notification");
    notifElement.innerHTML = `
        <h3>${notification.description}</h3>
        <p>${notification.content}</p>
        <span class="notif-date">${formatDate(notification.date)}</span>
    `;
    notificationList.appendChild(notifElement);
}

function formatDate(dateStr) {
    if (!dateStr || dateStr.length < 15) {
        return "Date inconnue";
    }
    const year = dateStr.substring(0, 4);
    const month = dateStr.substring(4, 6);
    const day = dateStr.substring(6, 8);
    const hour = dateStr.substring(9, 11);
    const minute = dateStr.substring(11, 13);
    const second = dateStr.substring(13, 15);
    return `${day}/${month}/${year} ${hour}:${minute}:${second}`;
}
