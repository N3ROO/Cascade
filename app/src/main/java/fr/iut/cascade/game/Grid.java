package fr.iut.cascade.game;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.plattysoft.leonids.ParticleSystem;

import java.io.Serializable;
import java.util.ArrayList;

import fr.iut.cascade.R;
import fr.iut.cascade.game.listeners.GridEventListener;

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

public class Grid extends View implements Serializable {


    GridEventListener gridEventListener;

    private int grid_lines;
    private int grid_columns;
    private float cell_width;
    private float cell_height;
    private int difficulty;
    private ArrayList<Cell> cells;
    private Paint paint;
    private int score;
    private Toast informationToast;

    private Activity activity;

    // stats
    private int best_combo;
    private int total_clicks;

    private static final int GREEN = Color.rgb(140, 230, 65);
    private static final int YELLOW = Color.rgb(240, 225, 0);
    private static final int AQUA = Color.rgb(0,240,200);
    private static final int ORANGE = Color.rgb(255, 180, 0);


    private float animation_speed;

    /**
     * Must use this constructor to put the view in the design.xml
     * @param context context
     */
    public Grid(Context context){
        super(context, null);

        this.paint = new Paint();
        this.paint.setStyle(Paint.Style.FILL_AND_STROKE);

        this.activity = (Activity) context;
    }

    /**
     * This is a sort of constructor lol
     * @param difficulty the level difficulty
     */
    public void init(int difficulty){
        int grid_lines;
        int grid_columns;
        switch (difficulty){
            case 1:
                grid_columns = 10;
                grid_lines = 10;
                this.animation_speed = 0.1f;
                break;
            case 2:
                grid_columns = 15;
                grid_lines = 15;
                this.animation_speed = 0.1f;
                break;
            case 3:
                grid_columns = 20;
                grid_lines = 20;
                this.animation_speed = 0.2f;
                break;
            case 4:
                grid_columns = 25;
                grid_lines = 25;
                this.animation_speed = 0.5f;
                break;
            default:
                throw new IllegalArgumentException("The difficulty value is set to " + difficulty + " but has to be between 1 and 4 included");
        }

        this.grid_columns = grid_columns;
        this.grid_lines = grid_lines;

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(AQUA);
        colors.add(YELLOW);
        colors.add(ORANGE);
        colors.add(GREEN);
        resetCells(colors);

        this.paint = new Paint();
        this.paint.setStyle(Paint.Style.FILL_AND_STROKE);

        this.score = 0;
        this.difficulty = difficulty;

        this.best_combo = 0;
        this.total_clicks = 0;

    }

    /**
     * Resets the entire game
     */
    public void reset(){
        init(this.difficulty);
        invalidate();
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
        this.activity = (Activity) context;
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
        this.activity = (Activity) context;
    }

    /**
     * Sets the gridEventListener
     * @param gridEventListener event listener
     */
    public void setGridEventListener(GridEventListener gridEventListener){
        this.gridEventListener = gridEventListener;
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

    /**
     * Updates the current grid cells with a 2D matrix
     * @param cells 2D matrix
     */
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
     * Accessor on the current score
     * @return the current score
     */
    public int getScore(){
        return this.score;
    }

    /**
     * It updates the dimensions of the grid, since the grid adapts itself to the view size
     */
    private void updateDimensions(){
        if (this.grid_columns > 0 && this.grid_lines > 0) {
            this.cell_width = (float) getWidth() / (float) grid_columns;
            this.cell_height = (float) getHeight() / (float) grid_lines;
            invalidate();
        }
    }

    /*
     * It updates the grid dimensions (grid_columns, grid_lines) to, then resize the grid.
     * For the moment it is unused because it isn't a user-friendly thing. It updates the size of the
     * cells so there is no animation anymore, but only automatic resize. The user could not understand
     * what happens. => The animation is better !
     * The code is cool so I let that method in case if we need it one day.
     *
    public void updateGridDimensions(){
        int min_lines = Integer.MAX_VALUE;
        int max_column = 0;
        for(Cell c : cells){
            int line = c.getLine();
            int column = c.getColumn() + 1;
            if(line < min_lines){
                min_lines = line;
            }
            if(column > max_column){
                max_column = column;
            }
        }

        // If the new size isn't the same as before we move the cells and change the dimensions variables
        if(min_lines != 0 || this.grid_columns != max_column){
            for(Cell c : cells){
                if(min_lines != 0){
                    c.setLine(c.getLine() - min_lines);
                }
                if(this.grid_columns != max_column){
                    c.setColumn(c.getColumn() + (this.grid_columns-(max_column+1)));
                }
            }
        }

        this.grid_lines = this.grid_lines - min_lines;
        this.grid_columns = max_column;
        updateDimensions();
    }
     */

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
                paint.setAlpha(210);
                float left,top,right,bottom;

                if(c.isMoving()){
                    left =  c.getMovingColumn() * cell_width;
                    top =  c.getMovingLine() * cell_height;
                    right =  (c.getMovingColumn() + 1f) * cell_width;
                    bottom =  (c.getMovingLine() + 1f) * cell_height;
                    c.move(animation_speed);
                    invalidate();
                }else{
                    left =  ((float) c.getColumn() * cell_width);
                    top =  ((float) c.getLine() * cell_height);
                    right =  ((float) (c.getColumn() + 1) * cell_width);
                    bottom =  ((float) (c.getLine() + 1) * cell_height);
                }

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
            int particle_resource = R.drawable.star_white;
            if(clicked_cell != null){

                int color = clicked_cell.getColor();
                if(color == AQUA)  particle_resource = R.drawable.star_aqua;
                if(color == ORANGE)  particle_resource = R.drawable.star_orange;
                if(color == YELLOW)  particle_resource = R.drawable.star_yellow;
                if(color == GREEN)  particle_resource = R.drawable.star_green;

                updateGrid(clicked_cell);
            }

            // Particle image shift
            final float x_shift =  20 / 2;
            final float y_shift = - 22 / 2;

            ParticleSystem particles = new ParticleSystem(this.activity, 50, particle_resource, 100);
            particles.setSpeedRange(0.2f, 0.5f);
            particles.setAcceleration(0.0005f, 90);
            particles.emit((int)(event.getX() + x_shift) , (int)(event.getY() + cell_height + y_shift), 50, 100);




            // If something changes and it needs to be reflected on screen, we need to call invalidate()
            invalidate();
        }

        return true;
    }

    /**
     * Updates the score
     */
    private void updateScore(int score_increment){
        sendInformation(Integer.toString(score_increment));
        this.score += score_increment;
        // Send the scoreChanged event
        if(this.gridEventListener != null)
            this.gridEventListener.onScoreChanged(this);
    }

    /**
     * It sends an information at the screen.
     * It prevents sending multiple information at the same time by removing
     * the last one and keeping the one sent.
     * @param information the information to display
     */
    private void sendInformation(String information){
        if (informationToast != null) {
            informationToast.cancel();
        }
        informationToast = Toast.makeText(getContext(), information, Toast.LENGTH_SHORT);
        informationToast.show();
    }

    /**
     * It updates the grid and the score of the player according to the cell he clicked
     * @param cell clicked cell
     */
    private void updateGrid(Cell cell){

        // 1. Check if the cell has at least one neighbor with the same color
        if(!hasSameColorNeighbor(cell)) return;

        // 2. Get all the cells that will be deleted
        ArrayList<Cell> cells = getSameColorAndNeighborsCells(cell, cell.getColor(), new ArrayList<Cell>());

        // 3. Remove the cells from the grid
        removeCells(cells);

        // 4. Get those cells down if there is space
        applyDownGravity();

        // 5. Get those cells left if there is space
        applyLeftGravity();

        // 6. Update the size of the grid
        // It is working, but it is actually not a user-friendly thing.
        // It seems better to animate the cells when it drops instead of
        // resizing the grid every time.
        // updateGridDimensions();

        // 7. Update the score
        int number_of_removed_cells = cells.size();
        int score_increment = calculateScoreIncrement(number_of_removed_cells);
        updateScore(score_increment);

        // 9. Check if there it is still possible to play
        // in this case, remove 10 * number of cells remaining to the score
        // and we send the end game event
        if(isGameFinished()){
            if(this.cells.size() == 0) updateScore(500);
            else updateScore(-this.cells.size() * 10);
            gridEventListener.onGameFinished(this);
        }

        // 8. Update the stats
        total_clicks ++;
        if(this.best_combo < number_of_removed_cells){
            this.best_combo = number_of_removed_cells;
        }

        // 9. Redraw
        invalidate();
    }

    /**
     * Calculates the score increment according to the number of removed cells
     * @param number_of_removed_cells number of removed cells
     * @return score increment
     */
    private int calculateScoreIncrement(int number_of_removed_cells){
        int score_increment = 0;
        if(number_of_removed_cells == 2){
            score_increment = 5;
        }else if(number_of_removed_cells > 2 && number_of_removed_cells <= 4){
            score_increment = number_of_removed_cells * 10;
        }else if(number_of_removed_cells > 4 && number_of_removed_cells <= 6){
            score_increment = number_of_removed_cells * 15;
        }else if(number_of_removed_cells > 6 && number_of_removed_cells <= 8){
            score_increment = number_of_removed_cells * 20;
        }else if(number_of_removed_cells > 8){
            score_increment = number_of_removed_cells * 30;
        }
        return score_increment;
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
    private void applyDownGravity(){

        // 1. We get the matrix
        Cell cells[][] = getGridMatrix();

        // 2. We will iterate over each line to apply the down gravity
        for(int column = 0 ; column < grid_columns ; column ++) {
            int void_counter = 1;
            for (int line = grid_lines - 2; line >= 0; line--) {
                Cell current_cell = cells[column][line];
                // If the cell that is down is empty
                if(cells[column][line + 1] == null && current_cell != null){
                    // Move the cell because there is a hole at the bottom
                    current_cell.setLine(current_cell.getLine() + void_counter);
                    cells[column][line + void_counter] = current_cell;
                    cells[column][line] = null;
                    // Reset
                    line = line + (void_counter - 1);

                    void_counter = 1;
                }else if(cells[column][line + 1] == null && current_cell == null){
                    // Big hole :o
                    void_counter ++;
                }
            }
        }

        setGridMatrix(cells);
    }

    /**
     * Applies the left gravity for the cells
     *
     * Algorithm explanation :
     * First, it counts the number of void columns at the left of each column and then
     * we move each column according to the void column at its left.
     *
     * Example: there are 2 void columns in the left of the third column, we say to the
     * third column that there is 2 void columns at its left, and it will move it to 2
     * columns at its left.
     */
    private void applyLeftGravity(){
        // 1. Get the matrix
        Cell cells[][] = getGridMatrix();

        // 2. Initiate the void counter
        // We don't need to count the number of void cells at the left of the very left column
        // SO DO NOT CHECK left_void_counter[0] it will be always 0
        int left_void_counter[] = new int[grid_columns];
        for(int i = 0 ; i < left_void_counter.length ; ++ i)
            left_void_counter[i] = 0;

        // 3. Count the number of void columns at the left of each movable column
        for(int column = 0 ; column < grid_columns ; ++ column){
            int line = 0;
            boolean empty = true;
            while(line < grid_lines && empty){
                Cell cell = cells[column][line];
                if(cell != null){
                    empty = false;
                }else{
                    ++ line;
                }
            }

            if(empty){
                if(column < grid_columns)
                    for (int col = (column + 1); col < left_void_counter.length; ++col)
                        left_void_counter[col]++;

            }
        }

        // 4. Now move each column
        for(int column = 1 ; column < grid_columns ; ++ column){
            if(left_void_counter[column] != 0){
                for(int line = 0 ; line < grid_lines ; ++ line){
                    Cell cell = cells[column][line];
                    if(cell != null){
                        int shift = left_void_counter[column];
                        cell.setColumn(cell.getColumn() - shift);
                        cells[column - shift][line] = cell;
                        cells[column][line] = null;
                    }
                }
            }
        }

        // 5. Apply the matrix to the grid
        setGridMatrix(cells);
    }

    /**
     * It says if there are remaining solutions in the grid
     * @return true if there is at least one remaining solution
     */
    private boolean isGameFinished(){
        boolean has_remaining_cells = false;
        int index = 0;
        while(!has_remaining_cells && index < this.cells.size()){
            Cell cell = this.cells.get(index);
            if(hasSameColorNeighbor(cell))
                has_remaining_cells = true;
            else
                ++ index;
        }
        return !has_remaining_cells;
    }

    /**
     * Accessor on the current difficulty
     * @return the current difficulty
     */
    public int getDifficulty() {
        return difficulty;
    }

    /**
     * Called when the application is killed
     * => save stuff
     * @return Parcelable
     */
    @Override
    public Parcelable onSaveInstanceState()
    {
        Bundle bundle = new Bundle();
        bundle.putParcelable("superState", super.onSaveInstanceState());
        bundle.putSerializable("cells", this.cells);
        return bundle;
    }

    /**
     * Called when the application is restored
     * => load stuff
     * @param state state
     */
    @SuppressWarnings("unchecked")
    @Override
    public void onRestoreInstanceState(Parcelable state)
    {
        if (state instanceof Bundle)
        {
            Bundle bundle = (Bundle) state;
            this.cells = (ArrayList<Cell>) bundle.getSerializable("cells");
            state = bundle.getParcelable("superState");
        }
        super.onRestoreInstanceState(state);
    }

    /**
     * Getter on the total of clicks
     * @return the number of clicks
     */
    public int getTotalClicks() {
        return total_clicks;
    }

    /**
     * Getter on the best combo
     * @return the best number of cells removed at the same time
     */
    public int getBestCombo() {
        return best_combo;
    }

    /**
     * Gives the value in score of the best combo
     * @return the value of the best combo
     */
    public int getBestComboScore(){
        return calculateScoreIncrement(this.best_combo);
    }

    /*
     * Sets the animation speed
     * Needs to be between 0 excluded and 1 included
     * We recommend a value between 0.05 and 0.3 otherwise it's too fast
     * @param animation_speed
     *
     * Removed for the moment because it sounds better that the grid has to handle
     * the animation by itself. At the moment, the animation speed changes according
     * to the level.
     *
    public void setAnimationSpeed(int animation_speed) {
        if(animation_speed <= 1 && animation_speed > 0)
            this.animation_speed = animation_speed;
    }*/
}