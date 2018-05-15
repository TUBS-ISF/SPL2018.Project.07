package de.tubs.gol.core.rule;

import de.tubs.gol.core.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Created by Tino on 23.01.2016.
 */
class StatusCalculatorTest {

    @Test
    void testConditionsForLivingCells() {

        StatusCalculator calculator = new StatusCalculator();

        Assertions.assertEquals(Status.DEAD, calculator.calculateStatus(Status.ALIVE, 0));
        Assertions.assertEquals(Status.DEAD, calculator.calculateStatus(Status.ALIVE, 1));
        Assertions.assertEquals(Status.ALIVE, calculator.calculateStatus(Status.ALIVE, 2));
        Assertions.assertEquals(Status.ALIVE, calculator.calculateStatus(Status.ALIVE, 3));
        Assertions.assertEquals(Status.DEAD, calculator.calculateStatus(Status.ALIVE, 4));
        Assertions.assertEquals(Status.DEAD, calculator.calculateStatus(Status.ALIVE, 5));
        Assertions.assertEquals(Status.DEAD, calculator.calculateStatus(Status.ALIVE, 6));
        Assertions.assertEquals(Status.DEAD, calculator.calculateStatus(Status.ALIVE, 7));
    }

    @Test
    void testConditionsForDeadCells() {

        StatusCalculator calculator = new StatusCalculator();

        Assertions.assertEquals(Status.DEAD, calculator.calculateStatus(Status.DEAD, 0));
        Assertions.assertEquals(Status.DEAD, calculator.calculateStatus(Status.DEAD, 1));
        Assertions.assertEquals(Status.DEAD, calculator.calculateStatus(Status.DEAD, 2));
        Assertions.assertEquals(Status.ALIVE, calculator.calculateStatus(Status.DEAD, 3));
        Assertions.assertEquals(Status.DEAD, calculator.calculateStatus(Status.DEAD, 4));
        Assertions.assertEquals(Status.DEAD, calculator.calculateStatus(Status.DEAD, 5));
        Assertions.assertEquals(Status.DEAD, calculator.calculateStatus(Status.DEAD, 6));
        Assertions.assertEquals(Status.DEAD, calculator.calculateStatus(Status.DEAD, 7));
    }
}
