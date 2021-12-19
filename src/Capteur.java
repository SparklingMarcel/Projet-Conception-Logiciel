package src;


import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Capteur implements Subscriber {


    private final String ref ;
    private double valeur ;


    public Capteur(String r) {
        this.ref = r ;
        this.valeur = new Random().nextInt(55);
        this.updateCapteur(this);
    }


    public String getRef() {
        return ref;
    }


    /**
     * Permet de mettre à jour le capteur avec son propre thread
     * Le capteur envoie soit ses données toutes les 20 secondes, soit quand il considère que sa valeur est trop haute ( > 50 )
     * @param c capteur qui s'actualise
     */
    public void updateCapteur(Capteur c) {

        Runnable capteurRunnable = new Runnable() {
            int i = 0;
            public void run() {
                i++;
                c.valeur = new Random().nextInt(55);
                if(i>20) {
                    c.addData();
                    i=0;
                }

                else if(c.valeur>50) {
                    c.addData();
                }
            }
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(capteurRunnable, 0, 1, TimeUnit.SECONDS);
    }

    /**
     * Permet d'ajouter les données du capteur dans la centrale
     */
    public void addData() {
        CentraleDeCommande.getInstance().addData(this,this.getData());
    }

    /**
     * WorkSub est appelé par la centrale pour demander à ses capteurs ( subscriber ) d'envoyer leurs données
     */
    @Override
    public void workSub() {
        this.addData();
    }

    /**
     *
     * @return  permet de récupérer les données du capteur
     */
    @Override
    public DataCapteur getData() {
        return new DataCapteur(LocalDateTime.now(),this.ref,this.valeur);
    }

    public double getValeur() {
        return valeur;
    }

    @Override
    public String toString() {
        return ref ;
    }
}
