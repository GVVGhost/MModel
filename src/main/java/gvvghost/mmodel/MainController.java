package gvvghost.mmodel;

import gvvghost.mmodel.Charts.ChartManager;
import gvvghost.mmodel.calc.C;
import gvvghost.mmodel.calc.S;
import gvvghost.mmodel.dataClasses.SimulationPoint;
import gvvghost.mmodel.dataClasses.SimulationPointsMap;
import gvvghost.mmodel.parameters.P;
import gvvghost.mmodel.parameters.Param;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MainController {
    @FXML
    private HBox chartHBox;
    @FXML
    private TextField tfC1;
    @FXML
    private TextField tfC2;
    @FXML
    private TextField tfC3;
    @FXML
    private TextField tfBeta;
    @FXML
    private TextField tfDistance;
    @FXML
    private TextField tfTime;
    @FXML
    private TextField tfCurrInit;
    @FXML
    private TextField tfVelocInit;
    @FXML
    private CheckBox varRS;

    private final Alert alert = new Alert(Alert.AlertType.ERROR, "Error occurred");

    public void insert() {
        S.c1 = getFromTF(tfC1, P.C1);
        S.c2 = getFromTF(tfC2, P.C2);
        S.c3 = getFromTF(tfC3, P.C3);
        S.beta = getFromTF(tfBeta, P.betaDef);
        S.iInit = getFromTF(tfCurrInit, P.I_INITIAL);
        S.vInit = getFromTF(tfVelocInit, P.V_INITIAL);
        S.timeLimit = (int) getFromTF(tfTime, P.timeDef);
        S.distanceLimit = (int) getFromTF(tfDistance, P.distDef);
    }

    public void reset() {
        tfC1.setText(P.C1 + "");
        tfC2.setText(P.C2 + "");
        tfC3.setText(P.C3 + "");
        tfBeta.setText(P.betaDef + "");
        tfTime.setText(P.timeDef + "");
        tfDistance.setText(P.distDef + "");
        tfCurrInit.setText(P.I_INITIAL + "");
        tfVelocInit.setText(P.V_INITIAL + "");
        insert();
    }

    public void clear() {
        chartHBox.getChildren().removeAll(chartHBox.getChildren());
    }

    public void simulate() {
        runnable.run();
        /*if (varRS.isSelected()) variableSlopeSim.run();
        else constantSlopeSim.run();*/
    }

    private final Runnable constantSlopeSim = new Runnable() {
        @Override
        public void run() {
            if (chartHBox.getChildren().size() > 2) chartHBox.getChildren().remove(0);
            SimulationPointsMap map = S.simulateVCPB();
            VBox vbox = ChartManager.getVboxSCharts(map, Param.TAU);
            if (vbox != null) {
                vbox.getChildren().add(createResultPane(map));
                chartHBox.getChildren().add(vbox);
            }
        }
    };

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (chartHBox.getChildren().size() > 2) chartHBox.getChildren().remove(0);
            SimulationPointsMap map;
            if (!varRS.isSelected()) map = S.simulateVSCB_AnD(null);
            else map = S.simulateVSCB_AnD(S.createSimpleBetaMap(S.beta, S.distanceLimit, S.timeLimit));
            VBox vbox = ChartManager.getVboxSCharts(map, Param.TAU);
            if (vbox != null) {
                vbox.getChildren().add(createResultPane(map));
                chartHBox.getChildren().add(vbox);
            }
        }
    };

    private GridPane createResultPane(SimulationPointsMap map){
        SimulationPoint sp = map.getPoint(map.getMapSize() - 1);
        GridPane pane = new GridPane();
        String[][] strings = {
                {"C1:","" + S.c1, "Beta:","" + S.beta, "C(" + S.timeLimit + "):",
                    "" + C.round(sp.getValue(Param.CURRENT), 3)},
                {"C2:","" + S.c2, "Dist:", "" + S.distanceLimit, "V(" + S.timeLimit + "):",
                    "" + C.round(sp.getValue(Param.VELOCITY), 3)},
                {"C3:","" + S.c3, "Time:", "" + S.timeLimit, "P(" + S.timeLimit + "):",
                    "" + C.round(sp.getValue(Param.PATH), 3)},
                {"S(C):", "" + C.round(integrate(map, Param.CURRENT),3),
                    "S(V):", "" + C.round(integrate(map, Param.VELOCITY),3),
                    "--", "--"}
        };

        for (int r = 0; r < strings.length; r++){
            for (int c = 0; c < strings[r].length; c+=2) {
                Label l = new Label(strings[r][c]);
                l.setMinWidth(60);
                l.setAlignment(Pos.BASELINE_RIGHT);
                l.setPadding(new Insets(0, 5, 0, 0));
                TextField tf = new TextField(strings[r][c+1]);
                tf.setEditable(false);
                tf.setAlignment(Pos.BASELINE_CENTER);
                pane.add(l, c, r);
                pane.add(tf, c+1, r);
            }
        }
        pane.setPadding(new Insets(0, 5, 0, 5));
        return pane;
    }

    private double getFromTF(TextField fromTextField, double defValue){
        return getDouble(fromTextField.getText(), String.valueOf(defValue), fromTextField);
    }

    private double getDouble(String newLine, String defaultLine, TextField tf) {
        double ans = 0.0D;
//        if (newLine == null) return ans;
        try {
            ans = Double.parseDouble(newLine);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            ans = Double.parseDouble(defaultLine);
            tf.setText(String.valueOf(ans));
            errorMessage("Invalid value entered. Restore defaults.");
        }
        return ans;
    }

    private double integrate(SimulationPointsMap map, Param param) {
        double result = 0.0D;
        for (int i = 0; i < map.getMapSize(); i++) result += map.getPointValue(i, param);
        return result * P.K;
    }

    private void errorMessage(String line){
        alert.setContentText(line);
        if(!alert.isShowing()) alert.show();
    }
}