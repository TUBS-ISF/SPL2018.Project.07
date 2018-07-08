package de.tubs.gol.core.board.torus;

import de.tubs.gol.core.Cell;
import de.tubs.gol.core.Status;
import de.tubs.gol.core.board.Board;
import de.tubs.gol.core.rule.StatusCalculator;

import java.util.*;

/**
 * Created by Tino on 17.01.2016.
 */
public class TorusBoard implements Board {

    public static final int NEIGHBOUR_COUNT = 8;

    protected final StatusCalculator calculator = new StatusCalculator();

    protected final List<Cell> livingCells = new ArrayList<>();
    protected final List<Cell> newLivingCells = new ArrayList<>();

    protected final static long START_GENERATION = 1;
    protected long currentGeneration;
    private int width;
    private int height;

    public TorusBoard() {
        this.currentGeneration = START_GENERATION;
    }

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

    public TorusBoard(final int width, final int height) {
        this.width = width;
        this.height = height;
    }

    public TorusBoard(final long initialGeneration, final int width, final int height) {
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
                if (x == cell.getX() && y == cell.getY()) {
                    continue;
                }

                neighbours.add(new Cell(translateX(x), translateY(y)));
            }
        }

        return neighbours;
    }

    public int translateX(final int x) {
        if (x < 0) {
            return getWidth() - 1;
        }
        if (x >= getWidth()) {
            return 0;
        }
        return x;
    }

    public int translateY(final int y) {
        if (y < 0) {
            return getHeight() - 1;
        }
        if (y >= getHeight()) {
            return 0;
        }
        return y;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
