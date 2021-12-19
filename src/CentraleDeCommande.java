package src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CentraleDeCommande {


    private static CentraleDeCommande instance = new CentraleDeCommande(); // Singleton de la centrale
    private HashMap<Subscriber, ArrayList<DataCapteur>> allDataCapteur = new HashMap<>() ; // Liste des capteurs , et pour chaque capteurs toutes les données qu'il a envoyées
    private ArrayList<Subscriber> capteurSubbed = new ArrayList<>() ; // Liste des capteurs abonnés à la centrale
    private ArrayList<TableauDeBord> listTabDeBord = new ArrayList<>(); // Liste des tableaux de bord de la centrale


    /**
     * le constructeur de la centrale permet de lancer le thread de mise à jour
     */
    private CentraleDeCommande() {
        workCentrale();
    }

    /**
     * mise en place du singeleton
     * @return le singleton CentraleDeCommande
     */
    public static CentraleDeCommande getInstance() {
        if (instance == null) {
            instance = new CentraleDeCommande();
        }
        return instance;
    }

    /**
     * permet d'abonner des capteurs à la centrale
     * @param s le capteur qu'on ajoute à la centrale
     */
    public void subscribe(Subscriber s) {
        this.capteurSubbed.add(s);

        this.allDataCapteur.put(s,new ArrayList<DataCapteur>(){
            {
                add(s.getData());
            }
        });

    }

    /**
     * permet d'ajouter les données d'un capteur à la centrale
     * @param c le capteur
     * @param data les données du capteur
     */
    public void addData(Capteur c,DataCapteur data) {
        this.allDataCapteur.get(c).add(data);
    }

    public DataCapteur getSubData(Subscriber s) {
        return s.getData();
    }

    /**
     * permet de récupérer toutes les données d'un capteur
     * @param s le capteur
     * @return la liste de ses donnes sous formes de String
     */
    public String getAllSubData(Subscriber s) {
        StringBuilder total = new StringBuilder();
        for(DataCapteur val: this.allDataCapteur.get(s)) {
            total.append(val);
            total.append("\n");
        }
        return total.toString();
    }

    /**
     * Permet de récupérer les dates des données d'un capteur
     * @param s Le capteur en question
     * @return la liste des dates des données du capteur sous forme de Liste de String
     */

    public ArrayList<String> getSubDate(Subscriber s) {
        ArrayList<String> total = new ArrayList<>();
        for(DataCapteur val: this.allDataCapteur.get(s)) {
            total.add(val.getDate());
        }
        return total;
    }

    /**
     * Permet de récupérer les données d'un capteur sur une période donnée
     * @param s Le capteur
     * @param date1 la date de début des données
     * @param date2 la date de fin des données
     * @return la liste des données du capteur c entre ces 2 dates
     */
    public String getSubDataPeriode(Subscriber s,String date1, String date2) {
        StringBuilder total = new StringBuilder();
        for(DataCapteur val: this.allDataCapteur.get(s)) {
            if(val.getDate().compareTo(date2)<=0 && val.getDate().compareTo(date1)>=0) {
                total.append(val);
                total.append("\n");
            }
        }
        return total.toString();
    }

    /**
     * permet de désabonner un capteur
     * @param s le captuer à désabonner
     */
    public void unsubscribe(Subscriber s) {
        this.capteurSubbed.remove(s);
        this.allDataCapteur.remove(s);
    }


    /**
     * Permet de lancer le thread de la centrale qui demande à chaque capteur d'envoyer leurs informations à la centrale toutes les 30 secondes
     */
    public void workCentrale() {

        Runnable helloRunnable = new Runnable() {
            public void run() {
                for (Subscriber s : capteurSubbed) {
                    s.workSub();
                }
            }
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(helloRunnable, 0, 30, TimeUnit.SECONDS);
    }

    public void addTableau(TableauDeBord tab) {
        this.listTabDeBord.add(tab);
    }


    /**
     * Permet de récupérer les données d'un capteur mise en forme pour l'affichage d'une courbe
     * @param tab le tableau sur lequel sera affiché la courbe
     * @param c le capteur dont on veut les données
     * @return Les données du capteur misent en forme par le tableau
     */
    public ArrayList<Double> displayCourbe(TableauDeBord tab, Capteur c){
        return tab.displayCourbe(this.allDataCapteur.get(c));
    }

    /**
     * Permet de récupérer les données d'un capteur mise en forme pour un affichage en temps réel
     * @param tab le tableau sur lequel seront affichées les données
     * @param c le capteur dont on veut les informations
     * @return les informations du capteur misent en forme par le tableau
     */
    public String displayTempsReel(TableauDeBord tab,Capteur c) {
        return tab.affichageTempsReel(this.allDataCapteur.get(c));
    }


    /**
     * retourne les données d'un capteur
     * @param c le capteur
     * @return les données DataCapteur du capteur
     */
    public ArrayList<DataCapteur> infoCapteur(Capteur c) {
        return this.allDataCapteur.get(c);
    }

    public HashMap<Subscriber, ArrayList<DataCapteur>> getAllDataCapteur() {
        return allDataCapteur;
    }


}
