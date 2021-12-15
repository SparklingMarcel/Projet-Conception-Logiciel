package src;

import java.util.ArrayList;
import java.util.HashMap;

public class CentraleDeCommande {

    private HashMap<Subscriber, ArrayList<DataCapteur>> allDataCapteur = new HashMap<>() ;
    private ArrayList<Subscriber> capteurSubbed = new ArrayList<>() ;
    private ArrayList<TableauDeBord> listTabDeBord = new ArrayList<>();




    public void subscribe(Subscriber s) {
        this.capteurSubbed.add(s);
        this.allDataCapteur.put(s,new ArrayList<DataCapteur>());
    }


    public void addData(Subscriber s) {
        this.allDataCapteur.get(s).add(s.getData());
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

    public void displayCourbe(TableauDeBord tab, Capteur c){
        tab.displayCourbe(this.allDataCapteur.get(c));
    }
}
