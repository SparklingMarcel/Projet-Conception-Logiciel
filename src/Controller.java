package src;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Controller {

    @FXML
    public ScrollPane tab1;
    @FXML
    public ScrollPane tab2;
    @FXML
    public ScrollPane tab3;
    @FXML
    public ComboBox comboTab1;
    @FXML
    public ComboBox comboTab2;
    @FXML
    public ComboBox comboTab3;
    @FXML
    public ComboBox methodTab1;
    @FXML
    public ComboBox methodTab2;
    @FXML
    public ComboBox methodTab3;
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

    public void ajoutCapteur() {
        Capteur c = new Capteur(UUID.randomUUID().toString());
        CentraleDeCommande.getInstance().subscribe(c);
        capteurRef.put(c.getRef(), c);

        for (ComboBox box : capteurCombo) {
            box.getItems().add(c.getRef());
        }

    }

    /*public void consulter(TableauDeBord tab, Capteur c, String methode) {
        if (c != null && methode != null) {
            Platform.runLater(new Runnable() {
                String value = "";
                Text txt = tableau.get(tab);

                public void run() {


                    if (methode.equals("courbe")) {
                        displayCourbe(tab, c);

                    } else if (methode.equals("histogramme")) {
                        CentraleDeCommande.getInstance().displayHistogramme(tab, c);
                    } else {
                        scrollpane.get(tab).setContent(tableau.get(tab));
                        value = CentraleDeCommande.getInstance().displayTempsReel(tab, c);
                        txt.setText(value);
                    }
                }
            });
        }
    } */

    public void affichageReel(TableauDeBord tab, Capteur c) {

        String value = CentraleDeCommande.getInstance().displayTempsReel(tab, c);
        tableau.get(tab).setText(value);
    }

    public LineChart<Number, Number> displayCourbe(TableauDeBord tab, Capteur c) {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();

        //creating the chart
        final LineChart<Number, Number> lineChart =
                new LineChart<Number, Number>(xAxis, yAxis);

        lineChart.setAnimated(false);
        lineChart.setTitle("Evolution des valeurs du capteur");
        XYChart.Series<Number,Number> series = new XYChart.Series<Number,Number>();
        series.setName("Over the time");
        ArrayList<Double> total = CentraleDeCommande.getInstance().displayCourbe(tab, c);
        int i = 0;
        for (Double d : total) {
            series.getData().add(new XYChart.Data<Number,Number>(i, d));
            i++;
        }
        xAxis.setLabel("Valeur du capteur au cours des" + i * 5 + "dernières secondes");
        lineChart.getData().add(series);
        scrollpane.get(tab).setContent(lineChart);
        return lineChart;

    }

    public void updateCourbe(TableauDeBord tab, Capteur c, LineChart linechart) {

        ArrayList<Double> total = CentraleDeCommande.getInstance().displayCourbe(tab, c);
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
            box.getItems().add("histogramme");
            box.getItems().add("courbe");
            box.getItems().add("Temps reel");
        }

        tab1.setContent(text1);
        tab2.setContent(text2);
        tab3.setContent(text3);

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
                                LineChart l = displayCourbe(box1, capteurRef.get(box2[0].getValue()));
                                timeline =
                                        new Timeline(new KeyFrame(Duration.millis(5000), e -> updateCourbe(box1, capteurRef.get(box2[0].getValue()),l)));
                                timeline.setCycleCount(Animation.INDEFINITE); // loop forever
                                timeline.play();
                            } else if (box2[1].getValue().equals("Temps reel")) {
                                scrollpane.get(box1).setContent(tableau.get(box1));
                                timeline =
                                        new Timeline(new KeyFrame(Duration.millis(5000), e -> affichageReel(box1, capteurRef.get(box2[0].getValue()))));
                                timeline.setCycleCount(Animation.INDEFINITE); // loop forever
                                timeline.play();
                            } else {

                            }
                        }
                    }
                });
            }
        }
    }


}
