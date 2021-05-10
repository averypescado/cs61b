package game2048;

import java.util.Formatter;
import java.util.Observable;


/** The state of a game of 2048.
 *  @author TODO: YOUR NAME HERE
 */
public class Model extends Observable {
    /** Current contents of the board. */
    private Board board;
    /** Current score. */
    private int score;
    /** Maximum score so far.  Updated when game ends. */
    private int maxScore;
    /** True iff game is ended. */
    private boolean gameOver;

    /* Coordinate System: column C, row R of the board (where row 0,
     * column 0 is the lower-left corner of the board) will correspond
     * to board.tile(c, r).  Be careful! It works like (x, y) coordinates.
     */

    /** Largest piece value. */
    public static final int MAX_PIECE = 2048;

    /** A new 2048 game on a board of size SIZE with no pieces
     *  and score 0. */
    public Model(int size) {
        board = new Board(size);
        score = maxScore = 0;
        gameOver = false;
    }

    /** A new 2048 game where RAWVALUES contain the values of the tiles
     * (0 if null). VALUES is indexed by (row, col) with (0, 0) corresponding
     * to the bottom-left corner. Used for testing purposes. */
    public Model(int[][] rawValues, int score, int maxScore, boolean gameOver) {
        int size = rawValues.length;
        board = new Board(rawValues, score);
        this.score = score;
        this.maxScore = maxScore;
        this.gameOver = gameOver;
    }

    /** Return the current Tile at (COL, ROW), where 0 <= ROW < size(),
     *  0 <= COL < size(). Returns null if there is no tile there.
     *  Used for testing. Should be deprecated and removed.
     *  */
    public Tile tile(int col, int row) {
        return board.tile(col, row);
    }

    /** Return the number of squares on one side of the board.
     *  Used for testing. Should be deprecated and removed. */
    public int size() {
        return board.size();
    }

    /** Return true iff the game is over (there are no moves, or
     *  there is a tile with value 2048 on the board). */
    public boolean gameOver() {
        checkGameOver();
        if (gameOver) {
            maxScore = Math.max(score, maxScore);
        }
        return gameOver;
    }

    /** Return the current score. */
    public int score() {
        return score;
    }

    /** Return the current maximum game score (updated at end of game). */
    public int maxScore() {
        return maxScore;
    }

    /** Clear the board to empty and reset the score. */
    public void clear() {
        score = 0;
        gameOver = false;
        board.clear();
        setChanged();
    }

    /** Add TILE to the board. There must be no Tile currently at the
     *  same position. */
    public void addTile(Tile tile) {
        board.addTile(tile);
        checkGameOver();
        setChanged();
    }

    /** Tilt the board toward SIDE. Return true iff this changes the board.
     *
     * 1. If two Tile objects are adjacent in the direction of motion and have
     *    the same value, they are merged into one Tile of twice the original
     *    value and that new value is added to the score instance variable
     * 2. A tile that is the result of a merge will not merge again on that
     *    tilt. So each move, every tile will only ever be part of at most one
     *    merge (perhaps zero).
     * 3. When three adjacent tiles in the direction of motion have the same
     *    value, then the leading two tiles in the direction of motion merge,
     *    and the trailing tile does not.
     * */
    public boolean tilt(Side side) {
        boolean changed;
        changed = false;

        // TODO: Modify this.board (and perhaps this.score) to account
        // for the tilt to the Side SIDE. If the board changed, set the
        // changed local variable to true.

        //Get all the second row
        board.setViewingPerspective(side);

        for (int column=0;column<board.size();column++) {
            int[] current_column = get_column(column);
            boolean[] merged = new boolean[4];
            for (int i = 2; i >= 0; i--) {
                int[] above = get_above(column,i);
                int current_value= current_column[i];
                Tile t=board.tile(column,i);
                if (i==2){
                    if (above[0]==0 && current_value!=0){
                        board.move(column,3,t);
                        current_column = get_column(column);
                        changed=true;
                    }
                    else if (above[0]==current_value && current_value!=0){
                        this.score+=current_value*2;
                        merged[0]=true;
                        board.move(column,3,t);
                        current_column = get_column(column);
                        changed=true;
                    }
                }
                else if  (i==1){
                    if (above[0]==0 && above[1]==0 && current_value!=0){
                        board.move(column,3,t);
                        current_column = get_column(column);
                        changed=true;
                    }
                    else if (above[0]==0 && above[1]==current_value && current_value!=0 && merged[0]==false){
                        this.score+=current_value*2;
                        merged[0]=true;
                        board.move(column,3,t);
                        current_column = get_column(column);
                        changed=true;
                    }
                    else if (above[0]==0 && above[1]==current_value && current_value!=0 && merged[0]==true){
                        board.move(column,2,t);
                        current_column = get_column(column);
                        changed=true;
                    }

                    else if (above[0]==0 && above[1]!=current_value && current_value!=0)
                    {
                        board.move(column,2,t);
                        current_column = get_column(column);
                        changed=true;
                    }

                    else if (above[0]==current_value && above[1]!=current_value && current_value!=0 && merged[1]==false)
                    {
                        this.score+=current_value*2;
                        merged[1]=true;
                        board.move(column,2,t);
                        current_column = get_column(column);
                        changed=true;
                    }


                }
                else if (i==0){
                    int[] zero=new int[] {0,0,0};
                    if (above[0]==0 && above[1]==0 && above[2]==0 && current_value!=0){
                        board.move(column,3,t);
                        current_column = get_column(column);
                        changed=true;

                    }
                    else if (above[0]==0 && above[1]==0 && above[2]!=current_value && current_value!=0){
                        board.move(column,2,t);
                        current_column = get_column(column);
                        changed=true;
                    }
                    else if (above[0]==0 && above[1]==0 && above[2]==current_value && current_value!=0 && merged[0]==false){
                        merged[0]=true;
                        this.score+=current_value*2;
                        board.move(column,3,t);
                        current_column = get_column(column);
                        changed=true;
                    }

                    else if (above[0]==0 && above[1]==0 && above[2]==current_value && current_value!=0 && merged[0]==true){
                        board.move(column,2,t);
                        current_column = get_column(column);
                        changed=true;
                    }

                    else if (above[0]==0 && above[1]==current_value && above[2]!=current_value && current_value!=0 && merged[1]==false){
                        merged[1]=true;
                        this.score+=current_value*2;
                        board.move(column,2,t);
                        current_column = get_column(column);
                        changed=true;
                    }
                    else if (above[0]==0 && above[1]==current_value && above[2]!=current_value && current_value!=0 && merged[1]==true){
                        board.move(column,1,t);
                        current_column = get_column(column);
                        changed=true;
                    }

                    else if (above[0]==current_value && above[1] !=current_value && above[2] !=current_value && current_value!=0 && merged[2]==false){
                        merged[2]=true;
                        this.score+=current_value*2;
                        board.move(column,1,t);
                        changed=true;
                    }
                    else if  (above[0]==0 && above[1] !=current_value && above[2] !=current_value && current_value!=0){
                        board.move(column,1,t);
                        current_column = get_column(column);
                        changed=true;
                    }

                }
            }
        }

        board.setViewingPerspective(Side.NORTH);
        checkGameOver();
        if (changed) {
            setChanged();
        }
        return changed;
    }


    public int[] get_row(int row_number) {
        int[] array = new int[4];
        for (int i=0; i < board.size();i++){
            if (board.tile(i,row_number)==null){
                array[i]=0;
            }
            else{
                array[i]=board.tile(i,row_number).value();
            }
        }
        return array;
    }

    public int[] get_column(int column_number) {
        int[] array = new int[4];
        for (int i=0; i < board.size();i++){
            if (board.tile(column_number,i)==null){
                array[i]=0;
            }
            else{
                array[i]=board.tile(column_number,i).value();
            }
        }
        return array;
    }

    public int[] get_above(int column, int row ){
        int size=3-row;
        int[] array = new int[size];
        int start=row+1;
        for (int j=start; j < board.size();j++){
            int location=j-row-1;
            if (board.tile(column,j)==null){
                array[location]=0;
            }
            else{
                array[location]=board.tile(column,j).value();
            }
        }
        return array;

    }


    /** Checks if the game is over and sets the gameOver variable
     *  appropriately.
     */
    private void checkGameOver() {
        gameOver = checkGameOver(board);
    }

    /** Determine whether game is over. */
    private static boolean checkGameOver(Board b) {
        return maxTileExists(b) || !atLeastOneMoveExists(b);
    }

    /** Returns true if at least one space on the Board is empty.
     *  Empty spaces are stored as null.
     * */
    public static boolean emptySpaceExists(Board b) {
        int x =0;
        int y =0;
        while (x < b.size()){
            if (b.tile(x,y)==null) {
                return true;
            }
            if (y < b.size()) {
                y+=1;
            }
            if (y >=b.size()){
                y=0;
                x+=1;
            }
        }

        return false;
    }

    /**
     * Returns true if any tile is equal to the maximum valid value.
     * Maximum valid value is given by MAX_PIECE. Note that
     * given a Tile object t, we get its value with t.value().
     */
    public static boolean maxTileExists(Board b) {
        int x =0;
        int y=0;
        while (x < b.size()){
                if (b.tile(x,y) != null) {
                    if (b.tile(x,y).value()==2048){
                        return true;
                    }}
                if (y < b.size()){
                    y+=1;
                }
                if (y>=b.size()) {
                    y=0;
                    x+=1;
                }
            }
            return false;
        }

    /**
     * Returns true if there are any valid moves on the board.
     * There are two ways that there can be valid moves:
     * 1. There is at least one empty space on the board.
     * 2. There are two adjacent tiles with the same value.
     */
    public static boolean atLeastOneMoveExists(Board b) {
        if (emptySpaceExists(b)) {
            return true;
        }
        int x =0;
        int y=1;
        while (x < b.size()){
            if (b.tile(x,y) != null) {
                if (b.tile(x,y).value()==b.tile(x,y-1).value()){
                    return true;
                }}
            if (y < b.size()){
                y+=1;
            }
            if (y>=b.size()) {
                y=1;
                x+=1;
            }
        }
        x =1;
        y=0;
        while (y < b.size()){
            if (b.tile(x,y) != null) {
                if (b.tile(x,y).value()==b.tile(x-1,y).value()){
                    return true;
                }}
            if (x < b.size()){
                x+=1;
            }
            if (x>=b.size()) {
                x=1;
                y+=1;
            }
        }


        return false;

    }


    @Override
     /** Returns the model as a string, used for debugging. */
    public String toString() {
        Formatter out = new Formatter();
        out.format("%n[%n");
        for (int row = size() - 1; row >= 0; row -= 1) {
            for (int col = 0; col < size(); col += 1) {
                if (tile(col, row) == null) {
                    out.format("|    ");
                } else {
                    out.format("|%4d", tile(col, row).value());
                }
            }
            out.format("|%n");
        }
        String over = gameOver() ? "over" : "not over";
        out.format("] %d (max: %d) (game is %s) %n", score(), maxScore(), over);
        return out.toString();
    }

    @Override
    /** Returns whether two models are equal. */
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (getClass() != o.getClass()) {
            return false;
        } else {
            return toString().equals(o.toString());
        }
    }

    @Override
    /** Returns hash code of Model’s string. */
    public int hashCode() {
        return toString().hashCode();
    }
}
