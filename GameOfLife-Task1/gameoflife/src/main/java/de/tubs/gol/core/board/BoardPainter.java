package de.tubs.gol.core.board;

import de.tubs.gol.core.Cell;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

import java.util.Optional;

/**
 * Created by Tino on 28.01.2016.
 */
public interface BoardPainter {

    void paint(GraphicsContext gc);

    Point2D getPosOnBoard(double mouseX, double mouseY);
    Optional<Cell> getCellAt(Point2D pointOnBoard);

    void setViewPortX(int x);
    void setViewPortY(int y);
    int getViewPortX();
    int getViewPortY();

    DoubleProperty viewPortWidthProperty();
    DoubleProperty viewPortHeightProperty();
    IntegerProperty cellWidthProperty();
}
