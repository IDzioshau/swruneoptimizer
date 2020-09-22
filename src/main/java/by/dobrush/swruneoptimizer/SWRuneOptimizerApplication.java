package by.dobrush.swruneoptimizer;

import by.dobrush.swruneoptimizer.service.ParserService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class SWRuneOptimizerApplication extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Optimizer");
        primaryStage.setResizable(false);

        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = getClass().getResource("/views/main.fxml");
        loader.setLocation(xmlUrl);
        Parent root = loader.load();

        primaryStage.setScene(new Scene(root));

        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        ParserService.saveBuilds();
    }
}
