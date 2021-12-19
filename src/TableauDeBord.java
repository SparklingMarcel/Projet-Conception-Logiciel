package src;

import java.util.ArrayList;

public class TableauDeBord {



    public TableauDeBord() {
        CentraleDeCommande.getInstance().addTableau(this);
    }


    /**
     * Permet de mettre en forme les données d'un capteur pour un affichage en temps réel
     * @param valCapteur la liste des valeurs d'un capteur à mettre en forme
     * @return La mise en forme des valeurs du capteur
     */
    public String affichageTempsReel(ArrayList<DataCapteur> valCapteur) {
        StringBuilder total = new StringBuilder();
        if(valCapteur !=null) {
            for(DataCapteur val: valCapteur) {
                total.append(val+"\n");
            }

        }
        return total.toString();
    }

    /**
     * Permet de mettre en forme les données d'un capteur pour un affichage d'une courbe
     * @param valCapteur la liste des valeurs d'un capteur à mettre en forme
     * @return  La mise en forme des valeurs du capteur
     */
    public ArrayList<Double> displayCourbe(ArrayList<DataCapteur> valCapteur) {
        ArrayList<Double> total = new ArrayList();
        if(valCapteur != null) {
            for(DataCapteur val: valCapteur) {
                total.add(val.getValeur()) ;
            }

        }
        return total ;
    }

}
