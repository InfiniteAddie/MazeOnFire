package com.company;

/**
 * Index object to store indices for a space in the maze - for use in a stack.
 * @author Aditya Dhawan, Annie Thach
 */
public class Index {
    private int row;
    private int col;

    /**
     * Constructor method.
     * @param row - Row of index.
     * @param col - Column of index.
     */
    public Index(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * toString method for printing Index object.
     * @return String representation of Index object.
     */
    @Override
    public String toString() {
        return "(" + row + ", " + col + ")";
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
}