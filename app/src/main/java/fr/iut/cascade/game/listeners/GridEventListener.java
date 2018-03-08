package fr.iut.cascade.game.listeners;

import fr.iut.cascade.game.Grid;

/**
 * Created by nero9 on 08/03/2018.
 */

public interface GridEventListener {

    /**
     * Called whenever the score changes
     * @param grid the grid that called the event
     */
    void scoreChanged(Grid grid);
}
