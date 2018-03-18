package fr.iut.cascade.game;

import java.io.Serializable;

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

public class Cell implements Serializable{
    private int line;
    private int column;
    private int color;

    private float moving_line;
    private float moving_column;

    /**
     * Default constructor
     * @param column column of the cell
     * @param line line of the cell
     * @param color color of the cell
     */
    Cell(int column, int line, int color){
        this.column = column;
        this.line = line;
        this.color = color;
        this.moving_column = column;
        this.moving_line = line;
    }

    /**
     * Method used to get the color of the cell
     * @return the color of the cell
     */
    public int getColor() {
        return color;
    }

    /**
     * Package-private method used to set the color of the cell
     * @param color new color of the cell
     */
    void setColor(int color) {
        this.color = color;
    }

    /**
     * Method used to get the line of the cell
     * @return line of the cell
     */
    int getLine() {
        return line;
    }

    /**
     * Package-private method used to set the line of the cell
     * @param line new line of the cell
     */
    void setLine(int line) {
        this.line = line;
    }

    /**
     * Method used to get the column of the cell
     * @return the column of the cell
     */
    int getColumn() {
        return column;
    }

    /**
     * Package-private method used to set the column of the cell
     * @param column new column of the cell
     */
    void setColumn(int column) {
        this.column = column;
    }

    /**
     * Method used to know if the cell is moving
     * @return if the cell is moving or not
     */
    boolean isMoving(){
        return this.moving_column != this.column || this.moving_line != this.line;
    }

    /**
     * Package-private method used to get the column of the cell (moving)
     * @return the column of the cell
     */
    float getMovingLine() {
        return moving_line;
    }

    /**
     * Package-private method used to get the column of the cell (moving)
     * @return the column of the cell
     */
    float getMovingColumn() {
        return moving_column;
    }

    /**
     * Package-private method which moves the cell with the desired increment
     * AT THE MOMENT IT DOES NOT WORK FOR LEFT TO RIGHT AND BOTTOM TO TOP MOVEMENTS
     * (and we don't care for the moment)
     *
     * If we want the cell to bounce, remove "- increment" or "+ increment" in the if statement.
     * Explanations :
     *  The animation speed (between 0 and 1) should be a multiple of 1 to prevent
     *  the cells to bounce when the animation is finished.
     *  The bounce is cause because sometimes, the value isn't exactly, for example 0.2
     *  but 0.20000000000051202005 and so on. This causes the cell to exceed the final position.
     *  The program detects it and put the cell at its right place and this causes the bounce.
     *
     * The program here detects if the cell will exceed the final position, so it puts it at
     * the right place before going over its final position. And since the shift is small, we don't
     * see the cell doing strange moves when finishing the movement.
     *
     * @param increment between 0 excluded and 1 included (no animation for 1)
     */
    void move(float increment){
        // For the right to left movement, it means that we moved too far, we have to
        // synchronize the two values (we finished the animation)
        if(moving_column - increment <= column)
            moving_column = column;
        else
            moving_column -= increment;

        // Same thing but for the top to bottom movement
        if(moving_line + increment >= line)
            moving_line = line;
        else
            moving_line += increment;
    }
}
