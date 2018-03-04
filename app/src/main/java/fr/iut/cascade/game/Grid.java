package fr.iut.cascade.game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Lilian Gallon on 04/03/2018.
 *
 */

public class Grid extends View {
    private final int MIN_COLUMNS = 5;
    private final int MAX_COLUMNS = 25;
    private final int MIN_LINES = 5;
    private final int MAX_LINES = 25;

    private int grid_lines;
    private int grid_columns;
    private int cell_width;
    private int cell_height;
    private ArrayList<Cell> cells;
    private Paint paint;

    /**
     * Must use this constructor to put the view in the design.xml
     * @param context context
     */
    public Grid(Context context){
        super(context, null);

        this.paint = new Paint();
        this.paint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    /**
     * This is a sort of constructor lol
     * @param grid_columns number of columns of the grid
     * @param grid_lines number of lines of the grid
     * @param cells cell array list
     */
    public void init(int grid_columns, int grid_lines, ArrayList<Cell> cells){
        if(grid_columns < MIN_COLUMNS) throw new IllegalArgumentException("The number of columns is set to " + grid_columns + " but the minimal value is " + MIN_COLUMNS + ".");
        if(grid_columns > MAX_COLUMNS) throw new IllegalArgumentException("The number of columns is set to " + grid_columns + " but the maximal value is " + MAX_COLUMNS + ".");
        if(grid_lines < MIN_LINES) throw new IllegalArgumentException("The number of lines is set to " + grid_lines + " but the minimal value is " + MIN_LINES + ".");
        if(grid_lines > MAX_LINES) throw new IllegalArgumentException("The number of lines is set to " + grid_lines + " but the maximal value is " + MAX_LINES + ".");

        this.grid_columns = grid_columns;
        this.grid_lines = grid_lines;
        setCells(cells);

        this.paint = new Paint();
        this.paint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    /**
     * Must use this constructor to put the view in the design.xml
     * @param context context
     * @param attrs attrs
     */
    public Grid(Context context, AttributeSet attrs){
        super(context, attrs);
        this.paint = new Paint();
        this.paint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    /**
     * Must use this constructor to put the view in the design.xml
     * @param context context
     * @param attrs attrs
     * @param defStyle def style
     */
    public Grid(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        this.paint = new Paint();
        this.paint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    /**
     * Give the cells to the grid
     * @param cells cell array list
     */
    public void setCells(ArrayList<Cell> cells){
        // Verification
        int index = 0;
        boolean isValid = true;
        String errorMessage = "";
        while(index < cells.size() && isValid){
            int line = cells.get(index).getLine();
            int column = cells.get(index).getColumn();
            if(line >= grid_lines){
                errorMessage = "The line of the cell #" + index + " is set to " + line + " whereas the maximum is set at " + (this.grid_lines - 1) + ".";
                isValid = false;
            }else if(line < 0){
                errorMessage = "The line of the cell #" + index + " is set to " + line + " whereas the minimum is set at 0.";
                isValid = false;
            }else if(column >= grid_columns){
                errorMessage = "The column of the cell #" + index + " is set to " + column + " whereas the maximum is set at " + (this.grid_columns - 1) + ".";
                isValid = false;
            }else if(column < 0){
                errorMessage = "The column of the cell #" + index + " is set to " + column + " whereas the minimum is set at 0.";
                isValid = false;
            }else{
                index ++;
            }
        }
        if(!isValid) throw new IllegalArgumentException("The cells variable contains an error : " + errorMessage);

        this.cells = cells;
    }

    /**
     * It updates the dimensions of the grid, since the grid adapts itself to the view size
     */
    private void updateDimensions(){
        if (this.grid_columns > 0 || this.grid_lines > 0) {
            this.cell_width = getWidth() / grid_columns;
            this.cell_height = getHeight() / grid_lines;
            invalidate();
        }
    }

    /**
     * Find and returns the cell in the grid cell array list
     * @param column column of the cell
     * @param line line of the cell
     * @return return the cell or null if not found
     */
    private Cell getCell(int column, int line){
        boolean found = false;
        int index = 0;
        Cell cell = null;
        while(!found && index < this.cells.size()){
            cell = this.cells.get(index);
            if(cell.getColumn() == column && cell.getLine() == line)
                found = true;
            else
                index ++;
        }
        if(found) return cell;
        else return null;
    }

    /**
     * Called every time, we need to draw things here
     * @param canvas canvas
     */
    @Override
    protected void onDraw(Canvas canvas){
        if(cells != null) {
            for (Cell c : cells) {
                paint.setColor(c.getColor());
                float left = (float) (c.getColumn() * cell_width);
                float top = (float) (c.getLine() * cell_height);
                float right = (float) ((c.getColumn() + 1) * cell_width);
                float bottom = (float) ((c.getLine() + 1) * cell_height);
                canvas.drawRect(left, top, right, bottom, paint);
            }
        }
    }

    /**
     * Called whenever the size of the view is changed
     * @param width current width of the view
     * @param height current height of the view
     * @param old_width old width of the view
     * @param old_height old height of the view
     */
    @Override
    protected void onSizeChanged(int width, int height, int old_width, int old_height){
        super.onSizeChanged(width, height, old_width, old_height);
        updateDimensions();
    }

    /**
     * Called whenever there is an interaction with the view (touch)
     * @param event interaction event
     * @return true OTHERWISE we won't be able to use the method anymore
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int column = (int)(event.getX() / cell_width);
            int line = (int)(event.getY() / cell_height);

            Cell clicked_cell = getCell(column, line);
            if(clicked_cell != null){
                updateGrid(clicked_cell);
            }

            // If something changes and it needs to be reflected on screen, we need to call invalidate()
            invalidate();
        }

        return true;
    }

    /**
     * It updates the grid and the score of the player according to the cell he clicked
     * @param cell clicked cell
     */
    private void updateGrid(Cell cell){
        // TODO: Game algorithm

        // Update the score
        // ...

        // Update the grid
        // ...

        // Redraw
        invalidate();
    }
}
