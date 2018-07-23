package de.tubs.gol.core.board.fixed;

import de.tubs.gol.core.Cell;
import de.tubs.gol.core.Status;
import de.tubs.gol.core.board.Board;
import de.tubs.gol.core.rule.StatusCalculator;

import java.util.*;

/**
 * Created by Tino on 17.01.2016.
 */
public aspect FixedBoard implements Board {

    private static final int NEIGHBOUR_COUNT = 8;

    private final StatusCalculator calculator = new StatusCalculator();

    private final List<Cell> livingCells = new ArrayList<>();
    private final List<Cell> newLivingCells = new ArrayList<>();

    private final static long START_GENERATION = 1;
    private int height;
    private int width;
    private long currentGeneration;


    public boolean cellIsAlive(final Cell cell) {
        return livingCells.contains(cell);
    }

    public Board add(final Cell cell) {
        livingCells.add(cell);
        return this;
    }

    public Board remove(final Cell cell) {
        livingCells.remove(cell);
        return this;
    }

    public Board addAll(final Collection<Cell> cells) {
        livingCells.addAll(cells);
        return this;
    }

    public boolean isBounded() {
        return true;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Board clear() {
        livingCells.clear();
        currentGeneration = START_GENERATION;
        return this;
    }

    public List<Cell> getLivingCells() {
        return Collections.unmodifiableList(livingCells);
    }

    public int countCells() {
        return livingCells.size();
    }

    public long getCurrentGeneration() {
        return currentGeneration;
    }

    public FixedBoard() {
    }

    public FixedBoard(final int width, final int height) {
        this.width = width;
        this.height = height;
    }

    public FixedBoard(final long initialGeneration, final int width, final int height) {
        this(width, height);
        this.currentGeneration = (initialGeneration < START_GENERATION ? START_GENERATION : initialGeneration);
    }

    public void updateLivingCells() {
        livingCells.clear();
        livingCells.addAll(newLivingCells);
        newLivingCells.clear();
    }

    public void nextRound() {

        calculateNextStatusOfCells(Status.ALIVE, livingCells);
        calculateNextStatusOfCells(Status.DEAD, determineDeadNeighbourCells());

        updateLivingCells();

        currentGeneration++;
    }

    public void calculateNextStatusOfCells(final Status currentStatus, final Collection<Cell> cells) {
        for (Cell cell : cells) {

            final List<Cell> neighbours = determineLivingNeighbourCells(cell);
            final Status newStatus = calculator.calculateStatus(currentStatus, neighbours.size());

            switch (newStatus) {
                case ALIVE:
                    newLivingCells.add(cell);
                    break;
            }
        }
    }

    public List<Cell> determineLivingNeighbourCells(final Cell cell) {
        final List<Cell> livingNeighbours = new ArrayList<>(NEIGHBOUR_COUNT);
        for (Cell neighbour : getNeighbours(cell)) {
            if (cellIsAlive(neighbour)) {
                livingNeighbours.add(neighbour);
            }
        }
        return livingNeighbours;
    }

    public Set<Cell> determineDeadNeighbourCells() {
        final Set<Cell> deadNeighbours = new HashSet<>(livingCells.size());
        for (Cell cell : livingCells) {
            final List<Cell> neighbours = getNeighbours(cell);
            for (Cell neighbour : neighbours) {
                if (!cellIsAlive(neighbour)) {
                    deadNeighbours.add(neighbour);
                }
            }
        }
        return deadNeighbours;
    }

    public List<Cell> getNeighbours(final Cell cell) {
        final List<Cell> neighbours = new ArrayList<>(NEIGHBOUR_COUNT);

        for (int x = cell.getX() - 1; x <= cell.getX() + 1; x++) {
            for (int y = cell.getY() - 1; y <= cell.getY() + 1; y++) {
                if (!pointIsInBound(x, y)
                        || (x == cell.getX() && y == cell.getY())) {
                    continue;
                }

                neighbours.add(new Cell(x, y));
            }
        }

        return neighbours;
    }

    public boolean pointIsInBound(final int x, final int y) {
        return x >= 0 && x < getWidth()
                && y >= 0 && y < getHeight();
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
