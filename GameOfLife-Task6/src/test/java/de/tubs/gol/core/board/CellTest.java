package de.tubs.gol.core.board;

import de.tubs.gol.core.Cell;
import org.junit.jupiter.api.Assertions;

import java.util.Collection;
import java.util.Objects;

/**
 * Created by Tino on 19.01.2016.
 */
abstract class CellTest {

    void assertSameCells(final Collection<Cell> collectionToTest, final Cell... expectedCells) {
        Objects.requireNonNull(collectionToTest, "Collection required");
        Objects.requireNonNull(expectedCells, "Cells required for assertion");

        Assertions.assertEquals(expectedCells.length, collectionToTest.size(), "Invalid Number of Cells");

        for (Cell expectedCell : expectedCells) {

            if (!collectionToTest.contains(expectedCell)) {
                Assertions.fail("Cell " + expectedCell + " not found in " + collectionToTest);
            }
        }
    }
}
