package gvvghost.mmodel.Charts;

import gvvghost.mmodel.MainApplication;
import gvvghost.mmodel.dataClasses.SimulationPointsMap;
import gvvghost.mmodel.dataClasses.Value;
import gvvghost.mmodel.parameters.P;
import gvvghost.mmodel.parameters.Param;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.Chart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.WritableImage;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.VBox;

public class ChartManager {
    public static VBox getVboxSCharts(SimulationPointsMap map,
                               Param xAxisTitle) {
        if (map.getMapSize() == 0) return null;
        VBox vBox = new VBox();
        vBox.setPrefSize(MainApplication.screenBounds.getWidth()*0.3, MainApplication.screenBounds.getHeight()*0.7);
        for (Value value : map.getMap().get(0).getValues()) {
            vBox.getChildren().add(createLineChart(map, value.getParam(), xAxisTitle));
        }
        return vBox;
    }

    public static LineChart createLineChart(SimulationPointsMap map, Param yAxis, Param xAxis){
        NumberAxis xNAx = new NumberAxis();
        NumberAxis yNAx = new NumberAxis();
        xNAx.setLabel(xAxis.getName());
        yNAx.setLabel(yAxis.getName());
        LineChart lineChart = new LineChart(xNAx, yNAx);
        lineChart.getData().add(createXYChartSeries(map, yAxis, xAxis));
        lineChart.setLegendVisible(false);
        setChartSaveOption(lineChart);
        return lineChart;
    }

    //new method for exprm mode
    public static LineChart createMultiLineChart(SimulationPointsMap[] maps, Param yAxis, Param xAxis){
        NumberAxis xNAx = new NumberAxis();
        NumberAxis yNAx = new NumberAxis();
        xNAx.setLabel(xAxis.getName());
        yNAx.setLabel(yAxis.getName());
        LineChart lineChart = new LineChart(xNAx, yNAx);
        for (SimulationPointsMap map : maps){
            lineChart.getData().add(createXYChartSeries(map, yAxis, xAxis));
        }
        lineChart.setLegendVisible(false);
        setChartSaveOption(lineChart);
        return lineChart;
    }

    //new method for exprm mode
    /*public static LineChart addToLineChart(){

    }*/


    public static XYChart.Series createXYChartSeries(SimulationPointsMap map,
                                               Param yAxis,
                                               Param xAxis){
        XYChart.Series series = new XYChart.Series();
        series.setName(yAxis.getName());
        for(int i : map.getMap().keySet()){
            if(Param.TAU.equals(xAxis)){
                series.getData().add(new XYChart.Data(i* P.K, map.getPointValue(i, yAxis)));
            } else {
                series.getData().add(new XYChart.Data(i, map.getPointValue(i, yAxis)));
            }
        }
        return series;
    }

    private static void setChartSaveOption(Chart chart){
        ContextMenu contextMenu = new ContextMenu();
        MenuItem menuItem = new MenuItem("Copy as png");
        menuItem.setOnAction(event -> {
            WritableImage image = chart.snapshot(new SnapshotParameters(), null);
            final ClipboardContent content = new ClipboardContent();
            content.putImage(image);
            Clipboard.getSystemClipboard().setContent(content);
        });
        contextMenu.getItems().add(menuItem);
        chart.setOnMouseClicked(event -> contextMenu.show(chart, event.getScreenX(), event.getScreenY()));
    }
}