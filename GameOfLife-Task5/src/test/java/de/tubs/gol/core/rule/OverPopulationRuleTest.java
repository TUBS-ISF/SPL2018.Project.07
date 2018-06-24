package de.tubs.gol.core.rule;

import de.tubs.gol.core.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Created by Tino on 17.01.2016.
 */
public class OverPopulationRuleTest {

    @Test
    public void testOverPopulationOfLivingCell() {
        Rule rule = new OverPopulationRule();

        Assertions.assertFalse(rule.matches(Status.ALIVE, 2));
        Assertions.assertFalse(rule.matches(Status.ALIVE, 3));
        Assertions.assertTrue(rule.matches(Status.ALIVE, 4));
        Assertions.assertTrue(rule.matches(Status.ALIVE, 5));
    }

    @Test
    public void testOverPopulationOfDeadCell() {
        Rule rule = new OverPopulationRule();

        Assertions.assertFalse(rule.matches(Status.DEAD, 2));
        Assertions.assertFalse(rule.matches(Status.DEAD, 3));
        Assertions.assertTrue(rule.matches(Status.DEAD, 4));
        Assertions.assertTrue(rule.matches(Status.DEAD, 5));
    }
}
