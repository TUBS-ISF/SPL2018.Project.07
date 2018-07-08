package de.tubs.gol.core.board;

import de.tubs.gol.core.Cell;
import de.tubs.gol.core.Status;

import java.util.*;

public interface Board {

    List<Cell> getNeighbours(final Cell cell);

    void updateLivingCells();

    void nextRound();

    void calculateNextStatusOfCells(final Status currentStatus, final Collection<Cell> cells);

    List<Cell> determineLivingNeighbourCells(final Cell cell);

    Set<Cell> determineDeadNeighbourCells();

    boolean cellIsAlive(Cell clickedCell);

    Board remove(Cell clickedCell);

    Board add(Cell clickedCell);

    int countCells();

    Board clear();

    long getCurrentGeneration();

    List<Cell> getLivingCells();

    Board addAll(Collection<Cell> collect);

    boolean isBounded();

    void setWidth(int width);
    void setHeight(int height);
}
