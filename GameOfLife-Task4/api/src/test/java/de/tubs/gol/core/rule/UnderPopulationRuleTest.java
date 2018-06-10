package de.tubs.gol.core.rule;

import de.tubs.gol.core.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Created by Tino on 17.01.2016.
 */
class UnderPopulationRuleTest {

    @Test
    void testUnderPopulationOfLivingCell() {
        Rule rule = new UnderPopulationRule();

        Assertions.assertTrue(rule.matches(Status.ALIVE, 0));
        Assertions.assertTrue(rule.matches(Status.ALIVE, 1));
        Assertions.assertFalse(rule.matches(Status.ALIVE, 2));
        Assertions.assertFalse(rule.matches(Status.ALIVE, 3));
    }
    
    @Test
    void testUnderPopulationOfDeadCell() {
        Rule rule = new UnderPopulationRule();

        Assertions.assertTrue(rule.matches(Status.DEAD, 0));
        Assertions.assertTrue(rule.matches(Status.DEAD, 1));
        Assertions.assertTrue(rule.matches(Status.DEAD, 2));
        Assertions.assertTrue(rule.matches(Status.DEAD, 3));
        Assertions.assertTrue(rule.matches(Status.DEAD, 4));
        Assertions.assertTrue(rule.matches(Status.DEAD, 5));
    }
}
