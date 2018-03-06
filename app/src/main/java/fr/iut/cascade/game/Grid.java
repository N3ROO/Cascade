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

    private int grid_lines = -1;
    private int grid_columns = -1;
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
     */
    public void init(int grid_columns, int grid_lines){
        if(grid_columns < MIN_COLUMNS) throw new IllegalArgumentException("The number of columns is set to " + grid_columns + " but the minimal value is " + MIN_COLUMNS + ".");
        if(grid_columns > MAX_COLUMNS) throw new IllegalArgumentException("The number of columns is set to " + grid_columns + " but the maximal value is " + MAX_COLUMNS + ".");
        if(grid_lines < MIN_LINES) throw new IllegalArgumentException("The number of lines is set to " + grid_lines + " but the minimal value is " + MIN_LINES + ".");
        if(grid_lines > MAX_LINES) throw new IllegalArgumentException("The number of lines is set to " + grid_lines + " but the maximal value is " + MAX_LINES + ".");

        this.grid_columns = grid_columns;
        this.grid_lines = grid_lines;

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.BLUE);
        colors.add(Color.RED);
        colors.add(Color.YELLOW);
        colors.add(Color.GREEN);
        resetCells(colors);

        this.paint = new Paint();
        this.paint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    /**
     *  It set gives a brand new cells array list to the grid with random colors
     * @param colors ArrayList of the random colors that we want for the grid
     */
    public void resetCells(ArrayList<Integer> colors){
        if(grid_lines == -1 || grid_columns == -1 ) throw new IllegalStateException("You should call init() before trying to initialise the cells");
        ArrayList<Cell> cells = new ArrayList<>();
        for(int line = 0 ; line < grid_lines ; ++line){
            for(int column = 0 ; column < grid_columns ; ++column){
                int random = (int) (Math.random() * colors.size());
                int color = colors.get(random);
                Cell cell = new Cell(column, line, color);
                cells.add(cell);
            }
        }
        this.cells = cells;
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
     * Gives a 2D array of the cells
     * @return cells matrix
     */
    private Cell[][] getGridMatrix(){
        Cell[][] cells = new Cell[grid_columns][grid_lines];

        for(Cell c : this.cells){
            cells[c.getColumn()][c.getLine()] = c;
        }

        return cells;
    }

    private void setGridMatrix(Cell[][] cells){
        ArrayList<Cell> new_cells = new ArrayList<>();

        for(int column = 0 ; column < grid_columns ; column ++) {
            for (int line = 0; line < grid_lines; line++) {
                Cell current_cell = cells[column][line];
                if(current_cell != null)
                    new_cells.add(current_cell);
            }
        }

        setCells(new_cells);
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
     * It removes all the defined cells from the grid
     * @param cells cells array to remove
     */
    private void removeCells(ArrayList<Cell> cells){
        this.cells.removeAll(cells);
    }


    /**
     * Find and returns the cell in the grid cell array list
     * It is frequently used and need to be optimized
     * @param column column of the cell
     * @param line line of the cell
     * @return return the cell or null if not found
     */
    private Cell getCell(int column, int line){
        if(column < 0 || column >= grid_columns || line < 0 || line >= grid_lines) return null;
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
     * Returns if the cell has at least a neighbor that has the same color
     * @param cell cell
     * @return has a same color neighbor
     */
    private boolean hasSameColorNeighbor(Cell cell){
        Cell up_cell = getCell(cell.getColumn(), cell.getLine() - 1);
        Cell down_cell = getCell(cell.getColumn(), cell.getLine() + 1);
        Cell right_cell = getCell(cell.getColumn() + 1, cell.getLine());
        Cell left_cell = getCell(cell.getColumn() - 1, cell.getLine());

        if(up_cell != null)
            if(up_cell.getColor() == cell.getColor())
                return true;
        if(down_cell != null)
            if(down_cell.getColor() == cell.getColor())
                return true;
        if(right_cell != null)
            if(right_cell.getColor() == cell.getColor())
                return true;
        if(left_cell != null)
            if (left_cell.getColor() == cell.getColor())
                return true;
        return false;
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
        // TODO: Finish game algorithm ez pz lmn sqz

        // Check if the cell has at least one neighbor with the same
        if(!hasSameColorNeighbor(cell)) return;

        // Get all the cells that will be deleted
        ArrayList<Cell> cells = getSameColorAndNeighborsCells(cell, cell.getColor(), new ArrayList<Cell>());

        // Remove the cells from the grid
        removeCells(cells);

        // Get those cells down if there is space
        applyGravity();

        // Get those cells left if there is space

        // Update the size of the grid

        // Update the score
        // int number_of_cells = cells.size();
        // Do something with it

        // Redraw
        invalidate();
    }

    /**
     * Recursive method that return an ArrayList containing all the cells that have the same color
     * as the "cell", and that are neighbor of it
     * You can see it as a 4-children tree if the 4 children : up_cell, down_cell, right_cell, left_cell
     * We use the container tree member to insure that we won't run an infinite loop
     * @param cell cell
     * @param color color
     * @param tree_members members of the tree
     * @return array list
     */
    private ArrayList<Cell> getSameColorAndNeighborsCells(Cell cell, int color, ArrayList<Cell> tree_members){
        ArrayList<Cell> cells_found = new ArrayList<>();

        // Get all the neighbors
        Cell up_cell = getCell(cell.getColumn(), cell.getLine() - 1);
        Cell down_cell = getCell(cell.getColumn(), cell.getLine() + 1);
        Cell right_cell = getCell(cell.getColumn() + 1, cell.getLine());
        Cell left_cell = getCell(cell.getColumn() - 1, cell.getLine());

        // Get the children of the cell father
        ArrayList<Cell> children = new ArrayList<>();
        if(up_cell != null)
            if(up_cell.getColor() == color && !tree_members.contains(up_cell)) {
                children.add(up_cell);
                tree_members.add(up_cell);
            }
        if(down_cell != null)
            if(down_cell.getColor() == color && !tree_members.contains(down_cell)) {
                children.add(down_cell);
                tree_members.add(down_cell);
            }
        if(right_cell != null)
            if(right_cell.getColor() == color && !tree_members.contains(right_cell)) {
                children.add(right_cell);
                tree_members.add(right_cell);
            }
        if(left_cell != null)
            if(left_cell.getColor() == color && !tree_members.contains(left_cell)) {
                children.add(left_cell);
                tree_members.add(left_cell);
            }

        for (Cell child : children) {
            cells_found.addAll(getSameColorAndNeighborsCells(child, color, tree_members));
        }

        cells_found.add(cell);

        return cells_found;
    }

    /**
     * Applies the down gravity for the cells
     */
    private void applyGravity(){

        // 1. We get the matrix
        Cell cells[][] = getGridMatrix();

        // 2. We will iterate over each line to apply the down gravity
        for(int column = 0 ; column < grid_columns ; column ++) {
            int void_counter = 1;
            for (int line = grid_lines - 2; line >= 0; line--) {
                Cell current_cell = cells[column][line];
                // If the cell that is down is empty
                if(cells[column][line + 1] == null && current_cell != null){
                    System.out.println(column + " " + line);
                    current_cell.setLine(current_cell.getLine() + void_counter);
                    cells[column][line + void_counter] = current_cell;
                    cells[column][line] = null;
                    line = line + (void_counter - 1);
                    void_counter = 1;
                }else if(cells[column][line + 1] == null && current_cell == null){
                    void_counter ++;
                }
            }
        }

        setGridMatrix(cells);
    }


}
