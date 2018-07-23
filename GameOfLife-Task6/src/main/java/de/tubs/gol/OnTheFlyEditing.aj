package de.tubs.gol;

import de.tubs.gol.gui.GameOfLifeGuiController;

public aspect OnTheFlyEditing {

    before(): execution(void de.tubs.gol.gui.GameOfLifeGuiController.initListener()) {
        GameOfLifeGuiController.canvas.setOnMouseClicked(event2 -> GameOfLifeGuiController.onCellClicked(event2, false));
        GameOfLifeGuiController.canvas.setOnMouseReleased(event -> {
            GameOfLifeGuiController.dragBool = false;
//            System.out.println("RELEASE");
    });
}
