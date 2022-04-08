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

import java.util.HashMap;


public class MainController {
    @FXML
    private Button btnSimulate;
    @FXML
    private Button btnInsert;
    @FXML
    private Button btnReset;
    @FXML
    private Button btnClear;
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
    private CheckBox experimentalCB;
    @FXML
    private Button btnAddData;
    @FXML
    private Button btnDeleteLast;



    private HashMap<Integer,SimulationPointsMap> maps;


    private Alert alert = new Alert(Alert.AlertType.ERROR, "Error occurred");

    public MainController() {
        maps = new HashMap<>();

    }

    public void simulate() {
        if (experimentalCB.isSelected()){
//            System.out.println("Selected experimental mode");
            experimental.run();
        } else {
            runnable.run();
        }
    }

    public void insert() {
        S.setAllParams(getDouble(tfC1.getText(), P.C1 + "", tfC1),
                getDouble(tfC2.getText(), P.C2 + "", tfC2),
                getDouble(tfC3.getText(), P.C3 + "", tfC3),
                getDouble(tfBeta.getText(), P.BetaDef + "", tfBeta),
                (int) getDouble(tfDistance.getText(), P.DistDef + "", tfDistance),
                (int) getDouble(tfTime.getText(), P.TimeDef + "",tfTime),
                getDouble(tfCurrInit.getText(), P.I_INITIAL + "", tfCurrInit),
                getDouble(tfVelocInit.getText(), P.V_INITIAL + "", tfVelocInit));
    }

    public void reset() {
        tfC1.setText(P.C1 + "");
        tfC2.setText(P.C2 + "");
        tfC3.setText(P.C3 + "");
        tfBeta.setText(P.BetaDef + "");
        tfDistance.setText(P.DistDef + "");
        tfTime.setText(P.TimeDef + "");
        tfCurrInit.setText(P.I_INITIAL + "");
        tfVelocInit.setText(P.V_INITIAL + "");
        insert();
    }

    public void addNewLinesToCharts(){
        chartHBox.getChildren().get(chartHBox.getChildren().size());

    }

    public void deleteLastLinesFromCharts(){

    }

    public void clear() {
        chartHBox.getChildren().removeAll(chartHBox.getChildren());
    }

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (chartHBox.getChildren().size() >= 3) {
                chartHBox.getChildren().remove(0);
            }
            SimulationPointsMap map = S.simulateVCPB(null);
            VBox vbox = ChartManager.getVboxSCharts(map, Param.TAU);
            if (vbox != null) {
                vbox.getChildren().add(createResultPane(map));
                chartHBox.getChildren().add(vbox);
            }
        }
    };

    private final Runnable experimental = new Runnable() {
        @Override
        public void run() {
            if (chartHBox.getChildren().size() >= 3) {
                chartHBox.getChildren().remove(0);
            }
            SimulationPointsMap betamap = S.createSimpleBetaMap(S.beta, S.distanceLimit, S.timeLimit);
            SimulationPointsMap map = S.simulateVSCB_AnD(betamap);
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
                {"C1:","" + S.c1,
                "Beta:","" + S.beta,
                "C(" + S.timeLimit + "):","" + C.roundAvoid(sp.getValue(Param.CURRENT), 3)},
                {"C2:","" + S.c2,
                "Dist:", "" + S.distanceLimit,
                "V(" + S.timeLimit + "):","" + C.roundAvoid(sp.getValue(Param.VELOCITY), 3)},
                {"C3:","" + S.c3,
                "Time:", "" + S.timeLimit,
                "P(" + S.timeLimit + "):", "" + C.roundAvoid(sp.getValue(Param.PATH), 3)},
                {"S(C):", "" + C.roundAvoid(integrate(map, P.K, Param.CURRENT),3),
                "S(V):", "" + C.roundAvoid(integrate(map, P.K, Param.VELOCITY),3),
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

    private double getDouble(String line, String defVal, TextField tf) {
        double ans = 0.0D;
        if (line != null) {
            try {
                ans = Double.parseDouble(line);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                ans = Double.parseDouble(defVal);
                tf.setText(ans+"");
                errorMessage("Invalid value entered. The value restored to default.");
            }
        }
        return ans;
    }

    private double integrate(SimulationPointsMap map, double step, Param param) {
        double result = 0.0D;
        int size = size=map.getMapSize();
        if (size!=0){
            for (int i = 0; size > i; i++) {
                result += map.getPointValue(i, param)*step;
            }
        }
        return result;
    }


    private void errorMessage(String line){
        alert.setContentText(line);
        if(!alert.isShowing()){
            alert.show();
        }
    }
}