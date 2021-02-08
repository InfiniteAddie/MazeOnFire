package com.company;

/**
 * Index object to store indices for a space in the maze - for use in a stack.
 * @author Aditya Dhawan, Annie Thach
 */
public class Index {
    private int row;
    private int col;
    private double distanceFromGoal;

    /**
     * Constructor method.
     * @param row - Row of index.
     * @param col - Column of index.
     */
    public Index(int row, int col) {
        this.row = row;
        this.col = col;
        this.distanceFromGoal = 0;
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
     * Setter method for distance from goal.
     * @param distanceFromGoal - The distance from goal.
     */
    public void setDistanceFromGoal(double distanceFromGoal) {
        this.distanceFromGoal = distanceFromGoal;
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
    public double getDistanceFromGoal() {
        return distanceFromGoal;
    }
}