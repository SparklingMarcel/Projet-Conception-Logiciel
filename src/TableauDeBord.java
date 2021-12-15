package src;

import java.util.ArrayList;

public class TableauDeBord {


    private CentraleDeCommande centraleMere ;

    public TableauDeBord(CentraleDeCommande cdc) {
        this.centraleMere = cdc;
        cdc.addTableau(this);
    }

    public void affichageRegu(Capteur c) {
        for(DataCapteur val: this.centraleMere.infoCapteur(c)) {
            System.out.println(val.getDate()+" "+val.getHour()+" "+val.getValeur());
        }
    }

    public void displayCapteurVal(double val) {
        System.out.println(val);
    }


    public void displayCourbe(ArrayList<DataCapteur> valCapteur) {

        for(DataCapteur val: valCapteur) {
            System.out.println(val.getDate()+" "+val.getHour()+" "+val.getValeur());
        }
    }

    public void displayHistogramme(ArrayList<DataCapteur> valCapteur) {
        for(DataCapteur val: valCapteur) {
            System.out.println(val.getDate()+" "+val.getHour()+" "+val.getValeur());
        }
    }
}
