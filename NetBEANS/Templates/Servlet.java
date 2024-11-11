//Auteur : Maxime VALLET
//Version : 1.2


//Ce qui est entre crochets est à modifier ou retirer selon la situation


package [?];

[imports]

/**
 *
 * @author [?]
 * @version [?]
 */
@WebServlet(name = "[nomServlet]", urlPatterns = {"/[nomServlet]"}) // ATTENTION, RENOMMER LE SERVLET (refactor > rename) NE CHANGE PAS LE CONTENU DE CETTE LIGNE (donc il sera non joignable)
public class [nomServlet] extends HttpServlet {

    /**
     * [description]<br><br>
     * 
     * Variables à envoyer au servlet (POST)<br>
     * String [?]       &emsp;&emsp;        [description] <br>
     *                              ...
     * String Test       &emsp;&emsp;        BD à utiliser (true : test | false : sae_51) <br>
     * 
     * <br>
     * Variables renvoyées par le servlet (JSON)<br>
     * String erreur       &emsp;&emsp;        types d'erreur : [err1] | ... | accès refusé | champ(s) manquant (req) | none <br>
                                ...
     * String [?]       &emsp;&emsp;        [description] <br>
     * 
     * @param request       servlet request
     * @param response      servlet response
     * @throws      ServletException if a servlet-specific error occurs
     * @throws      IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //Type de la réponse
        response.setContentType("application/json;charset=UTF-8");
        
        DAO[?] DAO = new DAO[?]();
        
        //Récuperation du JSON envoyé
        BufferedReader reader = request.getReader();
        Gson gsonRequest = new Gson();
        
        //Convertion des données du JSON dans un objet Java
        JSON.GetJSONInfoUsers user = gsonRequest.fromJson(reader, JSON.GetJSONInfo[?].class);
        
        //Données envoyées par la requête
        String [?] = user.get[?]();
                    ...
        String [?] = user.get[?]();
        Boolean TestBoolean = Boolean.valueOf(user.getTest());

        //Données
        String rights = "Aucun";
        String jsonString = "";
        
        //Vérification du contenu envoyé
        if([?] == null | ... | [?] == null){
            jsonString = "{\"erreur\":\"champ(s) manquant (req)\"}";
        }
        else{
            if([?].equals("") | ... | [?].equals("")){
                jsonString = "{\"erreur\":\"champ(s) manquant (req)\"}";
            }
            else{
                //Récuppération des droits de l'utilisateur
                rights = DAO.getUserRightsFromToken(token, TestBoolean);
                
                //Récuppération des droits d'accès au servlet (Merci d'ajouter votre servlet à la BD => voir "README_Java.txt" dossier "/Serveur")
                String access = DAO.getServletRights("[Nom du servlet]", rights, false);

                if(access.equals("true")){

                    //Code ici

                }
                else{
                    jsonString = "{\"erreur\":\"accès refusé\"}";
                }
            }
            
        }
        
        //Envoi des données
        try (PrintWriter out = response.getWriter()) {
            out.print(jsonString);
            out.flush();
        }
    }

    //Ignorer ce qu'il y'a en dessous (rajouté par l'IDE)

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
