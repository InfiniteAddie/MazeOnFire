package com.company;

import java.util.Comparator;

/**
 * Index object to store indices for a space in the maze - for use in a stack.
 * @author Aditya Dhawan, Annie Thach
 */
public class Index implements Comparable<Index>, Comparator<Index> {
    private int row;
    private int col;
    private int dist;
    private Index parent;
    private double score;

    /**
     * Constructor used to represent coordinates in the maze itself.
     * @param row - Row of index.
     * @param col - Column of index.
     */
    public Index(int row, int col, int dist, Index parent) {
        this.row = row;
        this.col = col;
        this.dist = dist;
        this.parent = parent;
        this.score = dist;
    }

    /**
     * Constructor used to represent the agent that steps through the maze.
     * @param row
     * @param col
     */
    public Index(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * toString method for printing Index object in (col, row) to match (x, y).
     * @return String representation of Index object.
     */
    @Override
    public String toString() {
        return "(" + row + ", " + col + ")";
        //return "(" + dist + ")"; //debug
    }

    /**
     * Compare method for 2 index objects.
     * @param obj - Index object.
     * @return -1 if score is less than input,
                0 if score is equal to input,
                1 if score is greater than input.
     */
    public int compareTo(Index obj) {
        if(this.score == obj.score) {
            return 0;
        } else if (this.score > obj.score) {
            return 1;
        } else {
            return -1;
        }
    }

    /**
     * Method to make the agent move down one step by incrementing the row.
     */
    public void stepDown() {
        row ++;
    }

    /**
     * Method to make the agent move up one step by decrementing the row.
     */
    public void stepUp() {
        row --;
    }

    /**
     * Method to make the agent move right one step by incrementing the col.
     */
    public void stepRight() {
        col ++;
    }

    /**
     * Method to make the agent move left one step by decrementing the col.
     */
    public void stepLeft() {
        col --;
    }

    /**
     * Compare method for priority queue's comparator.
     * @param a - Item 1.
     * @param b - Item 2.
     * @return (See compareTo method.)
     */
    @Override
    public int compare(Index a, Index b) {
        return a.compareTo(b);
    }

    /**
     * Calculate the distance between 2 points based off the Euclidean distance metric.
     * d((x_1, y_1), (x_2, y_2)) = sqrt((x_1 - x_2)^2 + (y_1 - y_2)^2)
     * @param p - A point.
     * @param q - Another point.
     * @return The Euclidean distance between p and q.
     */
    public static double distTwoPoints(Index p, Index q) {
        return Math.sqrt(Math.pow((p.getCol() - q.getCol()), 2) + Math.pow((p.getRow() - q.getRow()), 2));
    }

    // DEBUG
    public static void main(String[] args) {
        System.out.println(distTwoPoints(new Index(-1, 2, -1, null), new Index(2, -2, -1, null)));
    }

    /**
     * Setter method for distance from goal.
     * @param dist - The distance from goal.
     */
    public void setDist(int dist) {
        this.dist = dist;
    }

    /**
     * Setter method for score.
     * @param score - The score.
     */
    public void setScore(double score) {
        this.score = score;
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

    /**
     * Getter method for score.
     * @return Score.
     */
    public double getScore() {
        return score;
    }
}
