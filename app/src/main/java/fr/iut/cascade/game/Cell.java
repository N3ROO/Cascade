package fr.iut.cascade.game;

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

class Cell {
    private int line;
    private int column;
    private int color;

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
    }

    /**
     * Package-private method used to get the color of the cell
     * @return the color of the cell
     */
    int getColor() {
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
     * Package-private method used to get the line of the cell
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
     * Package-private method used to get the column of the cell
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
}
