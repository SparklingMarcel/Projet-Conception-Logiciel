package src;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Capteur implements Subscriber {

    private CentraleDeCommande centralMere;
    private final String ref ;
    private double valeur ;


    public Capteur(String r, CentraleDeCommande centrale) {
        this.ref = r ;
        this.valeur = 0;
        this.centralMere = centrale;
        this.updateCapteur(this);
    }

    public String getRef() {
        return ref;
    }

    public void updateCapteur(Capteur c) {
        Runnable helloRunnable = new Runnable() {
            public void run() {
                c.valeur = new Random().nextInt(55);
                if(c.valeur>50) {
                    c.addData();
                }
            }
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(helloRunnable, 0, 1, TimeUnit.SECONDS);
    }

    public void addData() {
        this.centralMere.addData(this,this.getData());
    }

    @Override
    public void workSub() {

    }

    @Override
    public DataCapteur getData() {
        return new DataCapteur(LocalDate.now(), LocalDateTime.now().getHour(),this.ref,this.valeur);
    }

    public double getValeur() {
        return valeur;
    }
}
