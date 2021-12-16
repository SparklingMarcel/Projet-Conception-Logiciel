package src;

import java.util.ArrayList;
import java.util.HashMap;

public class CentraleDeCommande {


    private static CentraleDeCommande instance = new CentraleDeCommande();
    private HashMap<Subscriber, ArrayList<DataCapteur>> allDataCapteur = new HashMap<>() ;
    private ArrayList<Subscriber> capteurSubbed = new ArrayList<>() ;
    private ArrayList<TableauDeBord> listTabDeBord = new ArrayList<>();





    private CentraleDeCommande() {

    }

    public static CentraleDeCommande getInstance() {
        if (instance == null) {
            instance = new CentraleDeCommande();
        }
        return instance;
    }
    public void subscribe(Subscriber s) {
        this.capteurSubbed.add(s);
        this.allDataCapteur.put(s,new ArrayList<DataCapteur>());
    }


    public void addData(Capteur c,DataCapteur data) {
        this.allDataCapteur.get(c).add(data);
    }

    public void unsubscribe(Subscriber s) {
        this.capteurSubbed.remove(s);
        this.allDataCapteur.remove(s);
    }

    public void workSub() {
        for (Subscriber s : this.capteurSubbed){
            s.workSub();
        }
    }

    public void addTableau(TableauDeBord tab) {
        this.listTabDeBord.add(tab);
    }

    public void getDataSub() {
        for (Subscriber s : this.capteurSubbed){
            this.allDataCapteur.get(s).add(s.getData());
        }
    }

    public ArrayList<Double> displayCourbe(TableauDeBord tab, Capteur c){
        return tab.displayCourbe(this.allDataCapteur.get(c));
    }

    public String displayTempsReel(TableauDeBord tab,Capteur c) {
        return tab.affichageTempsReel(this.allDataCapteur.get(c));
    }

    public void displayHistogramme(TableauDeBord tab, Capteur c){
        tab.displayHistogramme(this.allDataCapteur.get(c));
    }

    public ArrayList<DataCapteur> infoCapteur(Capteur c) {
        return this.allDataCapteur.get(c);
    }

    public HashMap<Subscriber, ArrayList<DataCapteur>> getAllDataCapteur() {
        return allDataCapteur;
    }


}
