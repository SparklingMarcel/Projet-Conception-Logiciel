package src;

import java.util.ArrayList;

public class TableauDeBord {


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
