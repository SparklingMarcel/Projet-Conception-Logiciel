package src;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class DataCapteur {
    private String date;
    private String refCapteur;
    private double valeur;

    public DataCapteur(LocalDateTime now, String ref, double val) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        this.date = dtf.format(now);;

        this.refCapteur = ref ;
        this.valeur = val ;
    }

    public double getValeur() {
        return valeur;
    }

    public String getDate() {
        return date;
    }


    public String getRefCapteur() {
        return refCapteur;
    }

    @Override
    public String toString() {
        return "date=" + date +
                ", valeur=" + valeur ;
    }
}
