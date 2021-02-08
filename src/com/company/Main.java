package com.company;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 * Let...
 * 0 be an empty space
 * 1 be an explored space
 * 2 be an obstacle
 * 3 be fire
 * 
 * Agent starts at (0, 0) and the goal is (dim - 1, dim - 1).
 * @author Aditya Dhawan, Annie Thach
 */
public class Main {
    private static int[][] maze;

    /**
     * Generate dim x dim maze given (dim)ension and (den)sity.
     * Density is 0.0 < p < 1.0; p is the probability the tile will be filled.
     * @param dim - Dimension of maze.
     * @param den - Obstacle density.
     */
    public static void generateMaze(int dim, double den) {
        maze = new int[dim][dim];
        Random random = new Random();

        for(int row = 0; row < maze.length; row++) {
            for(int col = 0; col < maze[row].length; col++) {
                // Exclude start and goal spaces.
                if(!((row == 0 && col == 0) || (row == dim - 1 && col == dim - 1)) 
                    && random.nextDouble() <= den) { // nextDouble() generates double betw. 0.0 & 1.0.
                        maze[row][col] = 2;
                }
                else {
                    maze[row][col] = 0;
                }
            }
        }
    }

    /**
     * Runs DFS recursively on the maze, marking each spot as visited as it goes
     * @param maze: the maze to traverse through
     * @param row: indexes the current row
     * @param col: indexes the current column
     */
    public static void DFS(int[][] maze, int row, int col) {
        //If we step out of bounds, or we reach something that has already been visited or is an obstacle, stop there
        if(row < 0 || col < 0 || row > maze.length - 1 || col > maze[0].length - 1) {
            return;
        }
        if(maze[row][col] == 1 || maze[row][col] == 2) {
            return;
        }

        maze[row][col] = 1;
        DFS(maze, row, col + 1); //step right
        DFS(maze, row, col - 1); //step left
        DFS(maze, row + 1, col); //step down
        DFS(maze, row - 1, col); //step up
    }

    /**
     * DFS search for goal.
     * @return True if goal is reachable; false otherwise.
     */
    public static boolean DFSMaze() {
        //start by running dfs() on a copy of the maze
        int[][] mazeDFS = copyMaze();
        DFS(mazeDFS, 0, 0);

        //once dfs() is complete, check to see if the goal has been visited
        return (mazeDFS[mazeDFS.length - 1][mazeDFS[0].length - 1] == 1);
    }

    /**
     * BFS search for goal.
     * @return True if goal is reachable; false otherwise.
     */
    public static boolean BFSMaze() {
        int[][] mazeBFS = copyMaze();                   // Copy maze.
        Queue<Index> queue = new LinkedList<Index>();   // Define queue.
        mazeBFS[0][0] = 1;          // Mark (0, 0) explored.
        queue.add(new Index(0, 0)); // Add index (0, 0) to queue.

        // While queue is not empty ...
        while(!queue.isEmpty()) {
            Index item = queue.remove(); // Dequeue next item.
            // If item is (maze.length - 1, maze.length - 1) ...
            if(item.getRow() == mazeBFS.length - 1 && item.getCol() == mazeBFS.length - 1) {
                printMaze(mazeBFS); // DEBUG
                return true;    // Reached goal; return.
            }

            // For all neighbors adjacent to item ... (item can have 0 - 4 viable neighbors)
            // Check below item (row + 1, col).
            if(item.getRow() + 1 < mazeBFS.length) {    // Bound check.
                // If neighbor not marked explored ...
                if(mazeBFS[item.getRow() + 1][item.getCol()] < 1 && !(mazeBFS[item.getRow() + 1][item.getCol()] > 1)) {
                    mazeBFS[item.getRow() + 1][item.getCol()] = 1;          // Mark explored ...
                    queue.add(new Index(item.getRow() + 1, item.getCol())); // Add neighbor to queue.
                }
            }
            
            // Check right of item (row, col + 1).
            if(item.getCol() + 1 < mazeBFS.length) {    // Bound check.
                // If neighbor not marked explored ...
                if(mazeBFS[item.getRow()][item.getCol() + 1] < 1 && !(mazeBFS[item.getRow()][item.getCol() + 1] > 1)) {
                    mazeBFS[item.getRow()][item.getCol() + 1] = 1;          // Mark explored ...
                    queue.add(new Index(item.getRow(), item.getCol() + 1)); // Add neighbor to queue.
                }
            }

            // Check above item (row - 1, col).
            if(item.getRow() - 1 >= 0) {    // Bound check.
                // If neighbor not marked explored ...
                if(mazeBFS[item.getRow() - 1][item.getCol()] < 1 && !(mazeBFS[item.getRow() - 1][item.getCol()] > 1)) {
                    mazeBFS[item.getRow() - 1][item.getCol()] = 1;          // Mark explored ...
                    queue.add(new Index(item.getRow() - 1, item.getCol())); // Add neighbor to queue.
                }
            }

            // Check left of item (row, col - 1).
            if(item.getCol() - 1 >= 0) {    // Bound check.
                // If neighbor not marked explored ...
                if(mazeBFS[item.getRow()][item.getCol() - 1] < 1 && !(mazeBFS[item.getRow()][item.getCol() - 1] > 1)) {
                    mazeBFS[item.getRow()][item.getCol() - 1] = 1;          // Mark explored ...
                    queue.add(new Index(item.getRow(), item.getCol() - 1)); // Add neighbor to queue.
                }
            }
        }
        printMaze(mazeBFS); // DEBUG
        return false;
    }

    /**
     * A* search for goal.
     * @return True if goal is reachable; false otherwise.
     */
    public boolean AStarMaze() {
        // TODO: Code here.
        return false;
    }

    /**
     * Helper method.
     * Count neighbors of given (x, y) that are currently on fire.
     * @param row - The row index of the current maze.
     * @param col - The column in dex of the current maze.
     * @return Number of neighbors that are on fire.
     */
    public static int countFireNeighbors(int row, int col) {
        // TODO: Code here.
        return 0;
    }

    /**
     * Advances fire one step in current maze.
     * The probability that a space will catch fire is defined as:
     * 1 - (1 - q)^k
     * @param q - Flammability rate; 0.0 < q < 1.0.
     * TODO: Finish coding helper countFireNeighbors.
     */
    public void advanceFireOneStep(double q) {
        Random random = new Random();

        for(int row = 0; row < maze.length; row++) {
            for(int col = 0; col < maze.length; col++) {
                // If (x, y) is not on fire or an obstacle...
                if(maze[row][col] != 3 && maze[row][col] != 2) {
                    // Count number of neighbors of (x, y) that are on fire.
                    // The higher the value, the likelier (x, y) will catch fire.
                    int k = countFireNeighbors(row, col);
                    double prob = 1 - Math.pow((1 - q), k);
                    
                    // Mark space on fire.
                    if(random.nextDouble() <= prob) {
                        maze[row][col] = 3;
                    }
                }
            }
        }
    }

    /**
     * Makes a copy of the maze.
     * @return Copy of maze.
     */
    public static int[][] copyMaze() {
        int[][] copy = new int[maze.length][maze.length];

        for(int row = 0; row < copy.length; row++) {
            for(int col = 0; col < copy[row].length; col++) {
                copy[row][col] = maze[row][col];
            }
        }

        return copy;
    }

    /**
     * Prints maze for debugging.
     * @param maze - Maze to print.
     */
    public static void printMaze(int[][] maze) {
        for(int row = 0; row < maze.length; row++) {
            for(int col = 0; col < maze[row].length; col++) {
                System.out.print(maze[row][col] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        generateMaze(10, 0.3);
        printMaze(maze);
        System.out.println();
        System.out.println(BFSMaze());
    }
}