package fr.iut.cascade.game.listeners;

import android.view.MotionEvent;

import fr.iut.cascade.game.Cell;
import fr.iut.cascade.game.Grid;


/**
 * This file is part of Cascade.
 *
 * Cascade is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Cascade is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Cascade. If not, see <http://www.gnu.org/licenses/>.
 * Author(s) : Lilian Gallon (N3RO)
 */

public interface GridEventListener {

    /**
     * Called when the score changes
     * @param grid the grid that called the event
     * @param score_increment the value in which the score was incremented
     */
    void onScoreChanged(Grid grid, int score_increment);

    /**
     * Called when the game finishes
     * @param grid the grid that called the event
     */
    void onGameFinished(Grid grid);

    /**
     * Called when the grid is touched.
     * We could use the view default one, but this one has the clicked_cell parameter
     * @param clicked_cell clicked_cell (null if none)
     * @param cell_width clicked_cell width
     * @param cell_height clicked_cell height
     * @param has_destroyed_cells has destroyed cells
     * @param motionEvent motion event
     */
    void onTouchEvent(Cell clicked_cell, float cell_width, float cell_height, boolean has_destroyed_cells, MotionEvent motionEvent);
}
