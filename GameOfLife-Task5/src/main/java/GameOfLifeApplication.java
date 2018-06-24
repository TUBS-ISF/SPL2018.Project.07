import de.tubs.gol.gui.GameOfLifeGuiController;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Tino on 21.01.2016.
 */
public class GameOfLifeApplication extends Application {

    private static List<String> parameters;

    public static void main(String[] args) {
        parameters = Arrays.asList(args);
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        FXMLLoader fxml = new FXMLLoader(getClass().getResource("/de/tubs/gol/GameOfLifeGui.fxml"));
        Parent root = fxml.load();
        Scene scene = new Scene(root, 700, 500);

        GameOfLifeGuiController controller = fxml.getController();
        controller.setParameters(parameters);

        primaryStage.setTitle("Game of Life");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(scene.getWidth());
        primaryStage.setMinHeight(scene.getHeight());
        primaryStage.show();

        MenuButton btn = controller.getNewBtn();
        ObservableList<MenuItem> fields = btn.getItems();
    }

}
