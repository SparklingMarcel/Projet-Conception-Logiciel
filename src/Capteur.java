package src;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;


public class Capteur implements Subscriber {

    private CentraleDeCommande centralMere;
    private final String ref ;
    private double valeur ;


    public Capteur(String r, CentraleDeCommande centrale) {
        this.ref = r ;
        this.valeur = 0;
        this.centralMere = centrale;
    }

    public String getRef() {
        return ref;
    }


    public void addData() {
        this.centralMere.addData(this,this.getData());
    }

    @Override
    public void workSub() {

    }

    @Override
    public DataCapteur getData() {
        this.valeur = new Random().nextInt(30);
        return new DataCapteur(LocalDate.now(), LocalDateTime.now().getHour(),this.ref,this.valeur);
    }

    public double getValeur() {
        return valeur;
    }
}
