package com.company;

/**
 * Index object to store indices for a space in the maze - for use in a stack.
 * @author Aditya Dhawan, Annie Thach
 */
public class Index {
    private int row;
    private int col;
    private double weight;

    /**
     * Constructor method.
     * @param row - Row of index.
     * @param col - Column of index.
     */
    public Index(int row, int col, double weight) {
        this.row = row;
        this.col = col;
        this.weight = weight;
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
     * @param weight - The distance from goal.
     */
    public void setWeight(double weight) {
        this.weight = weight;
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
    public double getWeight() {
        return weight;
    }
}