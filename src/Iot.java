package src;

public class Iot {

    private CentraleDeCommande centrale;

    public Iot(CentraleDeCommande centrale) {
        this.centrale = centrale;
    }

    public static void main(String[] args) {
        CentraleDeCommande cdc = CentraleDeCommande.getInstance();
        Iot main = new Iot(cdc);

        Capteur c1 = new Capteur("babla");
        Capteur c2 = new Capteur("blibli");

        TableauDeBord tab = new TableauDeBord();

        cdc.subscribe(c1);
        cdc.subscribe(c2);

        cdc.getDataSub();
        cdc.getDataSub();





    }
}
