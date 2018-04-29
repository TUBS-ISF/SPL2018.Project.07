package de.tubs.gol.core.rule;

import de.tubs.gol.core.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Created by Tino on 17.01.2016.
 */
class SurvivalRuleTest {

    @Test
    void testSurvivalOfCell() {
        Rule rule = new SurvivalRule();

        Assertions.assertTrue(rule.matches(Status.ALIVE, 2));
        Assertions.assertTrue(rule.matches(Status.ALIVE, 3));
    }
    @Test
    void testNoMatch() {
        Rule rule = new SurvivalRule();

        Assertions.assertFalse(rule.matches(Status.ALIVE, 0));
        Assertions.assertFalse(rule.matches(Status.ALIVE, 1));
        Assertions.assertFalse(rule.matches(Status.ALIVE, 4));

        Assertions.assertFalse(rule.matches(Status.DEAD, 2));
        Assertions.assertFalse(rule.matches(Status.DEAD, 3));
    }
}
