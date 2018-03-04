package fr.iut.cascade.game;

/**
 * Created by Lilian Gallon on 04/03/2018.
 *
 */

public class Cell {
    private int line;
    private int column;
    private int color;

    /**
     * Default constructor
     * @param column column of the cell
     * @param line line of the cell
     * @param color color of the cell
     */
    public Cell(int column, int line, int color){
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
