package de.tubs.gol;

import de.tubs.gol.gui.GameOfLifeGuiController;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;

public aspect Speed {

    before(): execution(void de.tubs.gol.gui.GameOfLifeGuiController.initBindings()) {
        Slider durationSlider = new Slider(1D, 1000D, 100D);
        durationSlider.setBlockIncrement(1.0);
        durationSlider.setMajorTickUnit(100.0);
        durationSlider.setShowTickLabels(true);

        GameOfLifeGuiController.gridPane.add(durationSlider, 0, 3);

        durationSlider.valueProperty().bindBidirectional(GameOfLifeGuiController.timer.stepDurationProperty());
    }

}
