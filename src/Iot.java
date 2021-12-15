package src;

public class Iot {

    private CentraleDeCommande centrale;

    public Iot(CentraleDeCommande centrale) {
        this.centrale = centrale;
    }

    public static void main(String[] args) {
        CentraleDeCommande cdc = new CentraleDeCommande();
        Iot main = new Iot(cdc);

        Capteur c1 = new Capteur("babla",cdc);
        Capteur c2 = new Capteur("blibli",cdc);

        TableauDeBord tab = new TableauDeBord();

        cdc.subscribe(c1);
        cdc.subscribe(c2);

        cdc.getDataSub();
        cdc.addTableau(tab);
        cdc.displayCourbe(tab,c1);
    }
}
