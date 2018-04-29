package de.tubs.gol;

import de.tubs.gol.javafx.gui.GameOfLifeGuiController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Tino on 21.01.2016.
 */
public class GameOfLifeApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        FXMLLoader fxml = new FXMLLoader(getClass().getResource("/de/tubs/gol/GameOfLifeGui.fxml"));
        Parent root = fxml.load();
        Scene scene = new Scene(root, 700, 500);

        GameOfLifeGuiController controller = fxml.getController();

//        controller.paint();

        primaryStage.setTitle("Game of Life");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(scene.getWidth());
        primaryStage.setMinHeight(scene.getHeight());
        primaryStage.show();
    }
}
