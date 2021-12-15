package src;

import java.time.LocalDate;
import java.util.Date;

public class DataCapteur {
    private LocalDate date;
    private int hour ;
    private String refCapteur;
    private double valeur;

    public DataCapteur(LocalDate now, int heure, String ref, double val) {
        this.date = now;
        this.hour = heure;
        this.refCapteur = ref ;
        this.valeur = val ;
    }

    public double getValeur() {
        return valeur;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getHour() {
        return hour;
    }

    public String getRefCapteur() {
        return refCapteur;
    }
}
