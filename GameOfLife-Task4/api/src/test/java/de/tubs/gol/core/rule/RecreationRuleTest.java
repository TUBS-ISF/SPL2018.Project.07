package de.tubs.gol.core.rule;

import de.tubs.gol.core.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Created by Tino on 17.01.2016.
 */
class RecreationRuleTest {

    @Test
    void testCreateNewCell() {
        Rule rule = new RecreationRule();

        Assertions.assertTrue(rule.matches(Status.DEAD, 3));
    }
    @Test
    void testNoRecreateCell() {
        Rule rule = new RecreationRule();

        Assertions.assertFalse(rule.matches(Status.DEAD, 0));
        Assertions.assertFalse(rule.matches(Status.DEAD, 1));
        Assertions.assertFalse(rule.matches(Status.DEAD, 2));
        Assertions.assertFalse(rule.matches(Status.DEAD, 4));

        Assertions.assertFalse(rule.matches(Status.ALIVE, 3));
    }
}
