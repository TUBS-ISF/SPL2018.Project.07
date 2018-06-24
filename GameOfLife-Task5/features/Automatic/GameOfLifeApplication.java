import de.tubs.gol.gui.GameOfLifeGuiController;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Tino on 21.01.2016.
 */
public class GameOfLifeApplication extends Application {

    @Override
    public void start(final Stage primaryStage) throws Exception {
        original();

        MenuItem endlessField = new MenuItem("Endless Field");
        endlessField.setOnAction(event -> newEndlessBoard());
        fields.add(endlessField);

        original();
    }

}
