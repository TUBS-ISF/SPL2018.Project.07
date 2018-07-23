package de.tubs.gol;

import de.tubs.gol.core.Cell;
import de.tubs.gol.gui.GameOfLifeGuiController;

import java.util.Optional;

public aspect DrawCells {

    before(): execution(void de.tubs.gol.gui.GameOfLifeGuiController.initListener()) {
        GameOfLifeGuiController.canvas.setOnMousePressed(event -> {
            Optional<Cell> cellOnBoard = GameOfLifeGuiController.getCellOnBoard(event);
            if (cellOnBoard.isPresent()) {
                GameOfLifeGuiController.dragBool = GameOfLifeGuiController.board.cellIsAlive(cellOnBoard.get());
//                System.out.println("ENTERED ON: " + dragBool);
            }
        });
        GameOfLifeGuiController.canvas.setOnMouseDragged(event1 -> {
            if (event1.isPrimaryButtonDown()) {
                GameOfLifeGuiController.onCellClicked(event1, true);
            }
        });
    }

}
