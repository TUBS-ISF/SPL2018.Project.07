package de.tubs.gol.core.board;

import de.tubs.gol.core.Cell;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Created by Tino on 17.01.2016.
 */
class FixedBoardTest extends CellTest {

    @Test
    void testWithinBounds() {

        FixedBoard board = new FixedBoard(10, 5);

        Assertions.assertTrue(board.pointIsInBound(0, 0));
        Assertions.assertTrue(board.pointIsInBound(9, 0));
        Assertions.assertTrue(board.pointIsInBound(0, 4));
        Assertions.assertTrue(board.pointIsInBound(9, 4));
    }

    @Test
    void testNotInBounds() {

        FixedBoard board = new FixedBoard(10, 5);

        Assertions.assertFalse(board.pointIsInBound(-1, -1));
        Assertions.assertFalse(board.pointIsInBound(10, -1));
        Assertions.assertFalse(board.pointIsInBound(-1, 5));
        Assertions.assertFalse(board.pointIsInBound(10, 5));
    }

    @Test
    void testGetNeighbours() {

        FixedBoard board = new FixedBoard(10, 5);

        Cell cell = new Cell(3, 3);
        Cell cellWest = new Cell(2, 3);
        Cell cellNorthWest = new Cell(2, 2);
        Cell cellNorth = new Cell(3, 2);

        board.add(cell).add(cellWest).add(cellNorthWest).add(cellNorth);

        // Do not add dead Cells to Board
        Cell cellNorthEast = new Cell(4, 2);
        Cell cellEast = new Cell(4, 3);
        Cell cellSouthEast = new Cell(4, 4);
        Cell cellSouth = new Cell(3, 4);
        Cell cellSouthWest = new Cell(2, 4);

        List<Cell> neighbours = board.getNeighbours(cell);
        assertSameCells(neighbours,
                cellWest, cellNorthWest, cellNorth, cellNorthEast, cellEast, cellSouthEast, cellSouth, cellSouthWest);
    }
}
