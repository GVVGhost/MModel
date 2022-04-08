package gvvghost.mmodel;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MainApplication extends Application {
    public static final Rectangle2D screenBounds = Screen.getPrimary().getBounds();

    @Override
    public void start(Stage stage) throws IOException {
        URL fxmlLocation = getClass().getResource("main-view.fxml");
        System.out.println(fxmlLocation);
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Simulation");
        stage.setScene(scene);
        stage.setMinWidth(screenBounds.getWidth()/2);
        stage.setMinHeight(screenBounds.getHeight()/2);
        stage.setWidth(screenBounds.getWidth()*0.8);
        stage.setHeight(screenBounds.getHeight()*0.8);
        stage.setMaxWidth(screenBounds.getWidth());
        stage.setMaxHeight(screenBounds.getHeight());
        scene.getStylesheets().add("gvvghost/mmodel/chart.css");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}