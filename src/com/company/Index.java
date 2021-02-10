package com.company;

/**
 * Index object to store indices for a space in the maze - for use in a stack.
 * @author Aditya Dhawan, Annie Thach
 */
public class Index {
    private int row;
    private int col;
    private int dist;
    private Index parent;

    /**
     * Constructor method.
     * @param row - Row of index.
     * @param col - Column of index.
     */
    public Index(int row, int col, int dist, Index parent) {
        this.row = row;
        this.col = col;
        this.dist = dist;
        this.parent = parent;
    }

    /**
     * toString method for printing Index object in (col, row) to match (x, y).
     * @return String representation of Index object.
     */
    @Override
    public String toString() {
        return "(" + col + ", " + row + ")";
    }

    /**
     * Setter method for distance from goal.
     * @param dist - The distance from goal.
     */
    public void setDist(int dist) {
        this.dist = dist;
    }

    /**
     * Getter method for row of index.
     * @return Row of index.
     */
    public int getRow() {
        return row;
    }

    /**
     * Getter method for column of index.
     * @return Column of index.
     */
    public int getCol() {
        return col;
    }

    /**
     * Getter method for distance from goal.
     * @return Distance from goal.
     */
    public int getDistance() {
        return dist;
    }

    /**
     * Getter method for parent of index.
     * @return Parent index of index.
     */
    public Index getParent() {
        return parent;
    }
}