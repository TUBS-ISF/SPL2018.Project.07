package de.tubs.gol.core.rule;

import de.tubs.gol.core.Status;

/**
 * Created by Tino on 17.01.2016.
 */
interface Rule {

    boolean matches(Status currentStatus, int neighbourCount);
    Status getStatus();

}
