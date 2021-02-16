package com.company;

import java.util.*;

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
     * @param onFire - Set random open spot on fire if true, ignore if false.
     */
    public static void generateMaze(int dim, double den, boolean onFire) {
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

        //if maze needs to be on fire, select a random spot and set it on fire
        if(onFire) {
            int initFireI;
            int initFireJ;
            //randomly choose an i,j such that
            do {
                initFireI = (int)(Math.random() * maze.length);
                initFireJ = (int)(Math.random() * maze.length);
            } while(maze[initFireI][initFireJ] == 2 || (initFireI == 0 && initFireJ == 0) || (initFireI == maze.length - 1 && initFireJ == maze.length - 1));
            maze[initFireI][initFireJ] = 3;
        }
    }

    /*
    /**
     * Runs DFS recursively on the maze, marking each spot as visited as it goes
     * @param maze: the maze to traverse through
     * @param row: indexes the current row
     * @param col: indexes the current column
     */ /*
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
    }*/

    /**
     * DFS search for goal.
     * @return True if goal is reachable; false otherwise.
     */
    public static boolean DFSMaze() {
        //start by running dfs() on a copy of the maze
        int[][] mazeDFS = copyMaze();

        Stack<Index> stack = new Stack<Index>(); //initialize fringe in the form of a stack
        stack.push(new Index(0, 0, 0, null)); //start by pushing the source index

        while(!stack.isEmpty()) { //loop through until the stack is empty
            //pop current from the stack and step through its neighbors
            Index item = stack.pop();
            if(item.getRow() < 0 || item.getCol() < 0 || item.getRow() >= mazeDFS.length || item.getCol() >= mazeDFS[0].length) { //bound and visited check
                continue;
            }
            else if(mazeDFS[item.getRow()][item.getCol()] == 1) {
                continue;
            }

            mazeDFS[item.getRow()][item.getCol()] = 1;//mark popped index as visited

            //step through left, right, up, and down parts of maze
            stack.push(new Index(item.getRow(), item.getCol() + 1, item.getDistance() + 1, item)); //right
            stack.push(new Index(item.getRow() + 1, item.getCol(), item.getDistance() + 1, item)); //down
            stack.push(new Index(item.getRow(), item.getCol() - 1, item.getDistance() + 1, item)); //left
            stack.push(new Index(item.getRow() - 1, item.getCol(), item.getDistance() + 1, item)); //up

        }

        return (mazeDFS[mazeDFS.length - 1][mazeDFS[mazeDFS.length - 1].length - 1] == 1); //checking solely for if DFS reached the goal
    }

    public static ArrayList<Index> findShortestPath(Index[][] indexMaze) {
        ArrayList<Index> shortestPath = new ArrayList<Index>();
        shortestPath.add(indexMaze[indexMaze.length - 1][indexMaze[indexMaze.length - 1].length - 1]);

        int i = indexMaze.length - 1;
        int j = indexMaze[i].length - 1;
        while(i >= 0 && j >= 0) {
            //check if loop has reached start node
            //System.out.println(indexMaze[i][j]);
            if((i == 0 && j == 1) || (i == 1 && j == 0) || (i == 0 && j == 0)) {
                shortestPath.add(indexMaze[0][0]);
                break;
            }

            //check if left is within bounds
            if(j - 1 >= 0) {
                //check if left is not null
                if(indexMaze[i][j - 1] != null) {
                    //check if dist value of left is less than dist value at current index
                    if(indexMaze[i][j - 1].getDistance() < indexMaze[i][j].getDistance()) {
                        j --; //move to this index
                        shortestPath.add(indexMaze[i][j]);
                    }
                }
            }

            //check if up is within bounds
            if(i - 1 >= 0) {
                //check if up is not null
                if(indexMaze[i - 1][j] !=null) {
                    //check if dist value of up is less than dist value at current index
                    if(indexMaze[i - 1][j].getDistance() < indexMaze[i][j].getDistance()) {
                        i --; //move to this index
                        shortestPath.add(indexMaze[i][j]);
                    }
                }
            }

            //check if right is within bounds
            if(j + 1 < indexMaze[j].length) {
                //check if right is not null
                if(indexMaze[i][j + 1] != null) {
                    //check if dist value of right is less than dist value at current index
                    if(indexMaze[i][j + 1].getDistance() < indexMaze[i][j].getDistance()) {
                        j ++; //move to this index
                        shortestPath.add(indexMaze[i][j]);
                    }
                }
            }

            //check if down is within bounds
            if(i + 1 < indexMaze.length) {
                //check if down is not null
                if(indexMaze[i + 1][j] != null) {
                    //check if dist value of down is less than dist value at current index
                    if(indexMaze[i + 1][j].getDistance() < indexMaze[i][j].getDistance()) {
                        i ++; //move to this index
                        shortestPath.add(indexMaze[i][j]);
                    }
                }
            }

            //System.out.println(indexMaze[i][j]);
            //System.out.println(shortestPath);

            //loop should continue until starting node is reached
        }
        Collections.reverse(shortestPath);
        return shortestPath;    // Reached goal; return.
    }

    /**
     * BFS search for goal.
     * @return True if goal is reachable; false otherwise.
     * /TODO: Think about how we can utilize distance from source to backtrack from goal and obtain the shortest path.
     * /TODO: (ideas): HashMap that stores information on a particular spot in the maze, and points to a parent (seems wack tbh)
     * /TODO: (ideas): for every part of the maze that has a 1, have that spot in a parallel matrix be equal to the distance from the source
     */
    public static ArrayList<Index> BFSMaze() {
        int[][] mazeBFS = copyMaze();                   // Copy maze.
        Queue<Index> queue = new LinkedList<Index>();   // Define queue.
        mazeBFS[0][0] = 1;          // Mark (0, 0) explored.
        queue.add(new Index(0, 0, 0, null)); // Add index (0, 0) to queue.

        Index[][] indexMaze = new Index[maze.length][maze[0].length];
        indexMaze[0][0] = new Index(0, 0, 0, null);

        // While queue is not empty ...
        while(!queue.isEmpty()) {
            Index item = queue.remove(); // Dequeue next item.
            //System.out.println(item); // DEBUG
            // If item is (maze.length - 1, maze.length - 1) ...
            if(item.getRow() == mazeBFS.length - 1 && item.getCol() == mazeBFS.length - 1) {
                /*
                for(int i = 0; i < indexMaze.length; i ++) {
                    for(int j = 0; j < indexMaze[i].length; j ++) {
                        System.out.print(indexMaze[i][j] + " ");
                    }
                    System.out.println();
                }*/
                //printMaze(mazeBFS); // DEBUG

                return findShortestPath(indexMaze);
            }

            // For all neighbors adjacent to item ... (item can have 0 - 4 viable neighbors)
            // Check below item (col, row + 1).
            if(item.getRow() + 1 < mazeBFS.length) {    // Bound check.
                // If neighbor not marked explored ... (and not an obstacle)
                if(mazeBFS[item.getRow() + 1][item.getCol()] < 1 && !(mazeBFS[item.getRow() + 1][item.getCol()] > 1)) {
                    mazeBFS[item.getRow() + 1][item.getCol()] = 1;          // Mark explored ...
                    indexMaze[item.getRow() + 1][item.getCol()] = new Index(item.getRow() + 1, item.getCol(), item.getDistance() + 1, item);
                    queue.add(new Index(item.getRow() + 1, item.getCol(), item.getDistance() + 1, item)); // Add neighbor to queue.
                }
            }
            
            // Check right of item (col + 1, row).
            if(item.getCol() + 1 < mazeBFS.length) {    // Bound check.
                // If neighbor not marked explored ... (and not an obstacle)
                if(mazeBFS[item.getRow()][item.getCol() + 1] < 1 && !(mazeBFS[item.getRow()][item.getCol() + 1] > 1)) {
                    mazeBFS[item.getRow()][item.getCol() + 1] = 1;          // Mark explored ...
                    indexMaze[item.getRow()][item.getCol() + 1] = new Index(item.getRow(), item.getCol() + 1, item.getDistance() + 1, item);
                    queue.add(new Index(item.getRow(), item.getCol() + 1, item.getDistance() + 1, item)); // Add neighbor to queue.
                }
            }

            // Check above item (col, row - 1).
            if(item.getRow() - 1 >= 0) {    // Bound check.
                // If neighbor not marked explored ... (and not an obstacle)
                if(mazeBFS[item.getRow() - 1][item.getCol()] < 1 && !(mazeBFS[item.getRow() - 1][item.getCol()] > 1)) {
                    mazeBFS[item.getRow() - 1][item.getCol()] = 1;          // Mark explored ...
                    indexMaze[item.getRow() - 1][item.getCol()] = new Index(item.getRow() - 1, item.getCol(), item.getDistance() + 1, item);
                    queue.add(new Index(item.getRow() - 1, item.getCol(), item.getDistance() + 1, item)); // Add neighbor to queue.
                }
            }

            // Check left of item (col - 1, row).
            if(item.getCol() - 1 >= 0) {    // Bound check.
                // If neighbor not marked explored ... (and not an obstacle)
                if(mazeBFS[item.getRow()][item.getCol() - 1] < 1 && !(mazeBFS[item.getRow()][item.getCol() - 1] > 1)) {
                    mazeBFS[item.getRow()][item.getCol() - 1] = 1;          // Mark explored ...
                    indexMaze[item.getRow()][item.getCol() - 1] = new Index(item.getRow(), item.getCol() - 1, item.getDistance() + 1, item);
                    queue.add(new Index(item.getRow(), item.getCol() - 1, item.getDistance() + 1, item)); // Add neighbor to queue.
                }
            }
        }
        //printMaze(mazeBFS); // DEBUG
        return null;
    }

    /**
     * Helper method to check neighbors & add to queue.
     * @param item - Current item.
     * @param queue - The queue to add neighbors to.
     */
    private static void AStarCheckNeighbors(int[][] maze, Index[][] indexMaze, Index item, PriorityQueue<Index> queue) {
        Index goal = new Index(maze.length - 1, maze.length - 1, 0, null);

        // Check neighbor below item. (row + 1, col)
        if(item.getRow() + 1 < maze.length && maze[item.getRow() + 1][item.getCol()] == 0) {    // Bound check & not visited.
            maze[item.getRow() + 1][item.getCol()] = 1; // Mark visited.
            Index neighbor = new Index(item.getRow() + 1, item.getCol(), item.getDistance() + 1, item);
            indexMaze[neighbor.getRow()][neighbor.getCol()] = neighbor;
            neighbor.setScore(item.getDistance() + Index.distTwoPoints(neighbor, goal));
            queue.add(neighbor);
        }

        // Check neighbor right of item. (row, col + 1)
        if(item.getCol() + 1 < maze.length && maze[item.getRow()][item.getCol() + 1] == 0) {    // Bound check & not visited.
            maze[item.getRow()][item.getCol() + 1] = 1; // Mark visited.
            Index neighbor = new Index(item.getRow(), item.getCol() + 1, item.getDistance() + 1, item);
            indexMaze[neighbor.getRow()][neighbor.getCol()] = neighbor;
            neighbor.setScore(item.getDistance() + Index.distTwoPoints(neighbor, goal));
            queue.add(neighbor);
        }

        // Check neighbor above item. (row - 1, col)
        if(item.getRow() - 1 >= 0 && maze[item.getRow() - 1][item.getCol()] == 0) {    // Bound check & not visited.
            maze[item.getRow() - 1][item.getCol()] = 1; // Mark visited.
            Index neighbor = new Index(item.getRow() - 1, item.getCol(), item.getDistance() + 1, item);
            indexMaze[neighbor.getRow()][neighbor.getCol()] = neighbor;
            neighbor.setScore(item.getDistance() + Index.distTwoPoints(neighbor, goal));
            queue.add(neighbor);
        }

        // Check neighbor left of item. (row, col - 1)
        if(item.getCol() - 1 >= 0 && maze[item.getRow()][item.getCol() - 1] == 0) {    // Bound check & not visited.
            maze[item.getRow()][item.getCol() - 1] = 1; // Mark visited.
            Index neighbor = new Index(item.getRow(), item.getCol() - 1, item.getDistance() + 1, item);
            indexMaze[neighbor.getRow()][neighbor.getCol()] = neighbor;
            neighbor.setScore(item.getDistance() + Index.distTwoPoints(neighbor, goal));
            queue.add(neighbor);
        }
    }

    /**
     * A* search for goal. Priority queue is a min heap.
     * Priority queue items has priority f(n) = g(n) + h(n).
     * g(n) is distance travelled from start to n.
     * h(n) is an estimated distance from n to goal.
     * @return True if goal is reachable; false otherwise.
     */
    public static ArrayList<Index> AStarMaze() {
        int[][] mazeAStar = copyMaze();
        Index[][] indexMaze = new Index[mazeAStar.length][mazeAStar[0].length];
        PriorityQueue<Index> minHeap = new PriorityQueue<Index>();
        maze[0][0] = 1; // Mark start visited.
        indexMaze[0][0] = new Index(0, 0, 0, null);
        minHeap.add(new Index(0, 0, 0, null));  // Add start to queue.
        
        // While heap is not empty ...
        while(!(minHeap.isEmpty())) {
            Index item = minHeap.remove();  // Remove item from queue.

            // If goal ...
            if(item.getRow() == maze.length - 1 && item.getCol() == maze.length - 1) {
                //printMaze(mazeAStar); // DEBUG
                return findShortestPath(indexMaze);
            }

            // Check neighbors of item.
            AStarCheckNeighbors(mazeAStar, indexMaze, item, minHeap);
        }

        //printMaze(mazeAStar); // DEBUG
        return null;
    }

    /**
     * Helper method.
     * Count neighbors of given (x, y) that are currently on fire.
     * @param row - The row index of the current maze.
     * @param col - The column index of the current maze.
     * @return Number of neighbors that are on fire.
     */
    public static int countFireNeighbors(int row, int col) {
        int k = 0; //num of neighbors on fire

        if(row - 1 >= 0) { //check if left is in bounds
            if(maze[row - 1][col] == 3) { //check if left is on fire
                k ++;
            }
        }
        if(row + 1 < maze.length) { //check if right is in bounds
            if(maze[row + 1][col] == 3) { //check if right is on fire
                k ++;
            }
        }
        if(col - 1 >= 0) { //check if up is in bounds
            if(maze[row][col - 1] == 3) { //check if up is on fire
                k ++;
            }
        }
        if(col + 1 < maze[col].length) { //check if down is in bounds
            if(maze[row][col + 1] == 3) { //check if down is on fire
                k ++;
            }
        }

        return k;
    }

    /**
     * Advances fire one step in current maze.
     * The probability that a space will catch fire is defined as:
     * 1 - (1 - q)^k
     * @param q - Flammability rate; 0.0 < q < 1.0.
     */
    public static void advanceFireOneStep(int[][] maze, double q) {
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
        long startTime;
        long endTime;

        generateMaze(10, 0.3, true);

        /*
        System.out.println("Original maze:");
        printMaze(maze);
        System.out.println();*/

        /*
        System.out.println("Depth-First Search (DFS):");
        startTime = System.nanoTime();
        System.out.println(DFSMaze());
        endTime = System.nanoTime();
        System.out.println("Time elapsed: " + (endTime - startTime)/1000000000 + " s");
        System.out.println();


        System.out.println("Breadth-First Search (BFS):");
        startTime = System.nanoTime();
        System.out.println(BFSMaze());
        endTime = System.nanoTime();
        System.out.println("Time elapsed: " + (endTime - startTime)/1000000000 + " s");
        System.out.println();

        System.out.println("A* Search:");
        startTime = System.nanoTime();
        System.out.println(AStarMaze());
        endTime = System.nanoTime();
        System.out.println("Time elapsed: " + (endTime - startTime)/1000000000 + " s");*/

        //At the start, a randomly selected open spot in the maze is set to "on fire"
        printMaze(maze);
        System.out.println();
        advanceFireOneStep(maze, 0.3); //the fire spreads
        printMaze(maze);
    }
}