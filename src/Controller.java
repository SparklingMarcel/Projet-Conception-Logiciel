package src;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.*;
import java.util.*;

/**
 * Controller permettant de gérer la vue
 */
public class Controller {

    // tab1, tab2, tab3 = les 3 scrollPane permettant d'afficher les tableaux
    @FXML
    public ScrollPane tab1;
    @FXML
    public ScrollPane tab2;
    @FXML
    public ScrollPane tab3;
    // comboTab1, comboTab2, comboTab3 = Les 3 menus déroulants permettant de choisir le capteur qu'on veut regarder pour chaque tableau
    @FXML
    public ComboBox comboTab1;
    @FXML
    public ComboBox comboTab2;
    @FXML
    public ComboBox comboTab3;
    // methodTab1, methodTab2, methodTab3 = les 3 menus déroulants permettant de choisir le mode d'affichage pour chaque tableau
    @FXML
    public ComboBox methodTab1;
    @FXML
    public ComboBox methodTab2;
    @FXML
    public ComboBox methodTab3;
    @FXML
    public Button rapButton; // Bouton permettant de générer le rapport
    @FXML
    public ComboBox genRapport; // menu déroulant choisissant le capteur pour la génération du rapport
    @FXML
    public ComboBox periode1; // date de début des données de la génération du rapport
    @FXML
    public ComboBox periode2; // date de fin des données de la génération du rapport
    @FXML
    public TextField xtt; // lis les inputs de l'utilisateur pour choisir le nom du capteur

    private HashMap<TableauDeBord, ComboBox[]> allCombo = new HashMap<>();
    private ComboBox[] capteurCombo;
    private HashMap<String, Capteur> capteurRef = new HashMap<>();
    private ComboBox[] methodCombo;
    private HashMap<TableauDeBord, Text> tableau = new HashMap<>();
    private HashMap<TableauDeBord, ScrollPane> scrollpane = new HashMap<>();
    private Text text1 = new Text();
    private Text text2 = new Text();
    private Text text3 = new Text();
    private TableauDeBord bord1 = new TableauDeBord();
    private TableauDeBord bord2 = new TableauDeBord();
    private TableauDeBord bord3 = new TableauDeBord();


    /**
     * Permet d'ajouter un capteur à la centrale, ainsi que d'ajouter les capteurs dans les menu déroulant (comboBox ) de l'interface graphique
     */
    public void ajoutCapteur() {
        if(!xtt.getText().equals("")) {
            Capteur c = new Capteur(xtt.getText()+" - " +UUID.randomUUID());
            CentraleDeCommande.getInstance().subscribe(c);
            capteurRef.put(c.getRef(), c);
            genRapport.getItems().add(c.getRef());

            for (ComboBox box : capteurCombo) {
                box.getItems().add(c.getRef());
            }

        }

    }

    /**
     * Permet de générer un rapport des données d'un capteur choisi sur la période choisie
     */
    public void generateRapport() {
        if(genRapport.getValue()!= null) {
            File selectedFile;
            final FileChooser chooser = new FileChooser();


            selectedFile = chooser.showSaveDialog(null);
            if(selectedFile == null ){
                return ;
            }
            String total = CentraleDeCommande.getInstance().getSubDataPeriode(capteurRef.get(genRapport.getValue()),periode1.getValue().toString(),periode2.getValue().toString());

            try (FileWriter bo = new FileWriter(selectedFile)) {
                bo.write(total);

            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }


    /**
     * permet d'afficher les données d'un capteur en temps réel
     * @param tab Le tableau sur lequel doivent être affiché les informations
     * @param c le capteur dont on veut afficher les informations
     */
    public void affichageReel(TableauDeBord tab, Capteur c) {

        String value = CentraleDeCommande.getInstance().displayTempsReel(tab, c);
        tableau.get(tab).setText(value);
    }


    /**
     * Permet de mettre en forme la courbe initial dans un tableau donné
     * @param tab Le tableau dans lequel on veut voir affiché la courbe
     * @param c Le capteur dont les données seront affichées dans le tableau
     * @return La courbe sous forme d'un objet linechart
     */
    public LineChart<Number, Number> setupCourbe(TableauDeBord tab, Capteur c) {

        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();

        //creating the chart
        final LineChart<Number, Number> lineChart =
                new LineChart<Number, Number>(xAxis, yAxis);

        lineChart.setAnimated(false);
        lineChart.setTitle("Evolution des valeurs du capteur");
        XYChart.Series<Number,Number> series = new XYChart.Series<Number,Number>();
        series.setName("Over the time");
        xAxis.setLabel("Valeur du capteur ");
        lineChart.getData().add(series);
        scrollpane.get(tab).setContent(lineChart);
        return lineChart;

    }

    /**
     * Permet de mettre en place des observers sur de multiples objets de l'interface graphique
     * Cela permet de mettre à jour des informations quand l'utilisateur change des données d'un des menus déroulant ( comboBox )
     * Cela permet par exemple de changer le type d'affichage en temps réel, ou de changer le capteur qu'un tableau observe
     */
    public void setupChangeListener() {
        for (Map.Entry<TableauDeBord, ComboBox[]> entry : allCombo.entrySet()) {
            TableauDeBord box1 = entry.getKey();
            ComboBox[] box2 = entry.getValue();
            for (int i = 0; i < 2; i++) {
                box2[i].valueProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue ov, String t, String t1) {
                        if (box2[1].getValue() != null) {
                            Timeline timeline;
                            if (box2[1].getValue().equals("courbe")) {
                                LineChart l = setupCourbe(box1, capteurRef.get(box2[0].getValue()));
                                timeline =
                                        new Timeline(new KeyFrame(Duration.millis(1000), e -> updateCourbe(box1, capteurRef.get(box2[0].getValue()),l)));
                                timeline.setCycleCount(Animation.INDEFINITE); // loop forever
                                timeline.play();
                            } else if (box2[1].getValue().equals("Temps reel")) {
                                scrollpane.get(box1).setContent(tableau.get(box1));
                                timeline =
                                        new Timeline(new KeyFrame(Duration.millis(1000), e -> affichageReel(box1, capteurRef.get(box2[0].getValue()))));
                                timeline.setCycleCount(Animation.INDEFINITE); // loop forever
                                timeline.play();
                            }
                        }
                    }
                });
            }
        }

        genRapport.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                periode1.getItems().clear();
                periode2.getItems().clear();
                ArrayList<String> dataDate = CentraleDeCommande.getInstance().getSubDate(capteurRef.get(t1));
                periode1.getItems().addAll(dataDate);
                periode2.setDisable(true);
                periode2.getItems().addAll(dataDate);

            }
        });

        periode1.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                periode2.setDisable(false);

                periode2.getItems().clear();
                ArrayList<String> dataDate = CentraleDeCommande.getInstance().getSubDate(capteurRef.get(genRapport.getValue()));
                for(String s : dataDate) {
                    if(s.compareTo(String.valueOf(t1))>=1) {
                        periode2.getItems().add(s);
                    }
                }
            }
        });
    }

    /**
     * Permet de mettre à jour la courbe en temps réel
     * @param tab le tableau sur lequel on met à jour la courbe
     * @param c le capteur dont les informations sont affichées
     * @param linechart l'objet représentant la courbe
     */
    public void updateCourbe(TableauDeBord tab, Capteur c, LineChart linechart) {

        ArrayList<Double> total = CentraleDeCommande.getInstance().displayCourbe(tab, c);
        if(!total.isEmpty()) {
            XYChart.Series<Number,Number> series = new XYChart.Series<Number,Number>();
            int i = 0;
            for (Double d : total) {
                XYChart.Data<Number,Number> chart = new XYChart.Data<Number,Number>(i,d);
                series.getData().add(new XYChart.Data(i, d));
                i++;
            }
            series.setName("Over the time");
            linechart.getData().clear();
            linechart.getData().add(series);
        }



    }

    /**
     * Permet de mettre en place toutes les liaisons entre les différents objets de l'interface graphique
     */
    @FXML
    public void initialize() {


        allCombo.put(bord1, new ComboBox[]{comboTab1, methodTab1});
        allCombo.put(bord2, new ComboBox[]{comboTab2, methodTab2});
        allCombo.put(bord3, new ComboBox[]{comboTab3, methodTab3});

        capteurCombo = new ComboBox[]{comboTab1, comboTab2, comboTab3};
        methodCombo = new ComboBox[]{methodTab1, methodTab2, methodTab3};
        tableau.put(bord1, text1);
        tableau.put(bord2, text2);
        tableau.put(bord3, text3);

        scrollpane.put(bord1, tab1);
        scrollpane.put(bord2, tab2);
        scrollpane.put(bord3, tab3);


        for (ComboBox box : methodCombo) {
            box.getItems().add("courbe");
            box.getItems().add("Temps reel");
        }

        tab1.setContent(text1);
        tab2.setContent(text2);
        tab3.setContent(text3);
        setupChangeListener();


    }


}
