package de.tubs.gol.gui;

import de.tubs.gol.core.board.BoardPainter;
import de.tubs.gol.core.board.BoardPainterFactory;
import de.tubs.gol.persistence.ResourceFigure;
import de.tubs.gol.core.Cell;
import de.tubs.gol.core.board.Board;
import de.tubs.gol.core.board.EndlessBoard;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Tino on 06.03.2016.
 */
public class DirectionDialogGuiController implements Initializable {

    @FXML private Canvas canvas;
    @FXML private Button leftRightBtn;
    @FXML private Button upDownBtn;

    private Board board;
    private BoardPainter boardPainter;

    private ResourceFigure figure;

    private void initBoard() {
        board = new EndlessBoard();
        boardPainter = new BoardPainterFactory().build(board, canvas.getWidth(), canvas.getHeight(), false);
        boardPainter.setViewPortX(0);
        boardPainter.setViewPortY(0);
        boardPainter.viewPortWidthProperty().bind(canvas.widthProperty());
        boardPainter.viewPortHeightProperty().bind(canvas.heightProperty());
    }

    public void setFigure(final ResourceFigure figureToEdit) {
        figure = copy(figureToEdit);
        refreshFigure();
    }

    @FXML private void mirrorLeftRight() {
        final ResourceFigure mirroredFigure = new ResourceFigure(figure.getName());

        for (Cell cell : figure.getCells()) {
            final Cell mirroredXCell = new Cell(-cell.getX(), cell.getY());
            mirroredFigure.getCells().add(mirroredXCell);
        }

        figure = mirroredFigure;

        refreshFigure();
    }

    @FXML private void mirrorUpDown() {
        final ResourceFigure mirroredFigure = new ResourceFigure(figure.getName());

        for (Cell cell : figure.getCells()) {
            final Cell mirroredXCell = new Cell(cell.getX(), -cell.getY());
            mirroredFigure.getCells().add(mirroredXCell);
        }

        figure = mirroredFigure;

        refreshFigure();
    }

    private int findMinX(final List<Cell> cells) {
        int minX = 0;
        for (Cell cell : cells) {
            minX = Math.min(minX, cell.getX());
        }
        return minX;
    }

    private int findMinY(final List<Cell> cells) {
        int minY = 0;
        for (Cell cell : cells) {
            minY = Math.min(minY, cell.getY());
        }
        return minY;
    }

    private void refreshFigure() {
        board.clear();
        board.addAll(figure.getCells());

        final int minX = findMinX(figure.getCells());
        final int minY = findMinY(figure.getCells());
        boardPainter.setViewPortX(minX);
        boardPainter.setViewPortY(minY);

        paint();
    }

    public void paint() {

        final GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(Color.GRAY);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        boardPainter.paint(gc);
    }

    private ResourceFigure copy(final ResourceFigure figure) {
        final ResourceFigure copy = new ResourceFigure(figure.getName());
        copy.getCells().addAll(figure.getCells());
        return copy;
    }

    public ResourceFigure getCurrentResourceFigure() {

        final List<Cell> normalisedCells = moveToPositionZero(figure.getCells());
        figure.getCells().clear();
        figure.getCells().addAll(normalisedCells);

        return figure;
    }

    private List<Cell> moveToPositionZero(final List<Cell> cells) {
        int minX = findMinX(figure.getCells());
        int minY = findMinY(figure.getCells());

        minX = (minX > 0 ? 0 : minX);
        minY = (minY > 0 ? 0 : minY);

        final List<Cell> normalisedCells = new ArrayList<>(cells.size());
        for (Cell cell : figure.getCells()) {
            final Cell normalizedCell = new Cell(cell.getX() + Math.abs(minX),
                    cell.getY() + Math.abs(minY));
            normalisedCells.add(normalizedCell);
        }

        return normalisedCells;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initBoard();
    }
}
