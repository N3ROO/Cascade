package fr.iut.cascade.game;

/**
 * Created by nero9 on 04/03/2018.
 */

public class Cell {
    private int line;
    private int column;
    private int color;

    public Cell(int column, int line, int color){
        this.column = column;
        this.line = line;
        this.color = color;
    }


    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }
}
