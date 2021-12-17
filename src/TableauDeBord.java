package src;

import java.util.ArrayList;

public class TableauDeBord {


    private CentraleDeCommande centraleMere ;

    public TableauDeBord() {
        this.centraleMere = CentraleDeCommande.getInstance();
        CentraleDeCommande.getInstance().addTableau(this);
    }

    public void affichageRegu(Capteur c) {
        for(DataCapteur val: this.centraleMere.infoCapteur(c)) {
            System.out.println(val+"\n");
        }
    }

    public String affichageTempsReel(ArrayList<DataCapteur> valCapteur) {
        StringBuilder total = new StringBuilder();
        if(valCapteur !=null) {
            for(DataCapteur val: valCapteur) {
                total.append(val+"\n");
            }

        }
        return total.toString();
    }


    public ArrayList<Double> displayCourbe(ArrayList<DataCapteur> valCapteur) {
        ArrayList<Double> total = new ArrayList();
        if(valCapteur != null) {
            for(DataCapteur val: valCapteur) {
                total.add(val.getValeur()) ;
            }

        }
        return total ;
    }

    public void displayHistogramme(ArrayList<DataCapteur> valCapteur) {
        for(DataCapteur val: valCapteur) {

        }
    }
}
