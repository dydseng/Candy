import java.util.*;

public class Location {
    private int row;
    private int col;
    public Location(int row, int col) {
        this.row = row;
        this.col = col;
    }
    
    /* Getter and setter methods. */
    public int getRow() {
        return row;
    }
    
    public int getCol() {
        return col;
    }
    
    public Location getRight() {
        return new Location(row, col + 1);
    }
    
    public Location getLeft() {
        return new Location(row, col - 1);
    }
    
    public Location getUp() {
        return new Location(row + 1, col);
    }
    
    public Location getBelow() {
        return new Location(row - 1, col);
    }
    
    public boolean isAdjacentTo(Location other) {
        return (getRight().equals(other) || getLeft().equals(other) || getUp().equals(other) || getBelow().equals(other));
    }
    
    public boolean equals(Object other) {
        Location temp = (Location)other;
        return this.row == temp.row && this.col == temp.col;
    }
}