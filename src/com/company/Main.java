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

    /**
     * Helper method to find shortest path through a maze.
     * @param indexMaze - Maze of indices to trace back through.
     * @return A list of indices that is part of the shortest path.
     */
    public static ArrayList<Index> findShortestPath(Index[][] indexMaze) {
        ArrayList<Index> shortestPath = new ArrayList<Index>();
        shortestPath.add(indexMaze[indexMaze.length - 1][indexMaze[indexMaze.length - 1].length - 1]);

        int i = indexMaze.length - 1;
        int j = indexMaze[i].length - 1;
        while(i >= 0 && j >= 0) {
            //check if loop has reached start node
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
    public static boolean DFSMaze(boolean print) {
        int[][] mazeDFS = copyMaze();   // Copy maze.
        Stack<Index> stack = new Stack<Index>(); // Initialize fringe in the form of a stack.
        stack.push(new Index(0, 0, 0, null)); // Start by pushing the source index.

        while(!stack.isEmpty()) { // While stack is not empty ...
            Index item = stack.pop();   // Current item.
            mazeDFS[item.getRow()][item.getCol()] = 1; // Mark visited.

            // If goal ...
            if(item.getRow() == maze.length - 1 && item.getCol() == maze.length - 1) {
                if(print) {
                    printMaze(mazeDFS);
                }
                return true;
            }

            // Check neighbors of item.
            // Check above item (row - 1, col).
            if(item.getRow() - 1 >= 0) {   // Bound check.
                if(mazeDFS[item.getRow() - 1][item.getCol()] == 0) {    // Check space type.
                    stack.push(new Index(item.getRow() - 1, item.getCol(), item.getDistance() + 1, item));
                }
            }

            // Check left of item (row, col - 1).
            if(item.getCol() - 1 >= 0) {   // Bound check.
                if(mazeDFS[item.getRow()][item.getCol() - 1] == 0) {    // Check space type.
                    stack.push(new Index(item.getRow(), item.getCol() - 1, item.getDistance() + 1, item));
                }
            }

            // Check right of item (row, col + 1).
            if(item.getCol() + 1 < maze.length) {   // Bound check.
                if(mazeDFS[item.getRow()][item.getCol() + 1] == 0) {    // Check space type.
                    stack.push(new Index(item.getRow(), item.getCol() + 1, item.getDistance() + 1, item));
                }
            }

            // Check below item (row + 1, col).
            if(item.getRow() + 1 < maze.length) {   // Bound check.
                if(mazeDFS[item.getRow() + 1][item.getCol()] == 0) {    // Check space type.
                    stack.push(new Index(item.getRow() + 1, item.getCol(), item.getDistance() + 1, item));
                }
            }
        }

        if(print) {
            printMaze(mazeDFS);
        }
        return false;
    }

    /**
     * Helper method for strategy.
     * @param agent - Location of agent.
     * @return Path information.
     */
    public static PathInfo BFSFromPosition(Index agent) {
        int[][] mazeBFS = copyMaze();
        Queue<Index> queue = new LinkedList<Index>();
        mazeBFS[agent.getRow()][agent.getCol()] = 1;
        queue.add(new Index(agent.getRow(), agent.getCol(), 0, null));

        Index[][] indexMaze = new Index[mazeBFS.length][mazeBFS.length];
        indexMaze[agent.getRow()][agent.getCol()] = new Index(agent.getRow(), agent.getCol(), 0, null);

        while(!queue.isEmpty()) {
            Index item = queue.remove();

            //if goal...
            if(item.getRow() == mazeBFS.length - 1 && item.getCol() == mazeBFS.length - 1) {
                ArrayList<Index> shortestPath = new ArrayList<Index>();
                Index current = indexMaze[mazeBFS.length - 1][mazeBFS.length - 1];

                //Determine the exact route of the shortest path.
                while(current.getRow() != agent.getRow() || current.getCol() != agent.getCol()) {
                    shortestPath.add(current);
                    current = current.getParent();
                }
                shortestPath.add(indexMaze[agent.getRow()][agent.getCol()]);
                Collections.reverse(shortestPath);

                //Determine the number of nodes visited by BFS.
                int numNodesExplored = 0;
                for(int i = 0; i < mazeBFS.length; i ++) {
                    for(int j = 0; j < mazeBFS.length; j ++) {
                        if(mazeBFS[i][j] == 1) {
                            numNodesExplored ++;
                        }
                    }
                }
                // System.out.println("# Nodes Explored: " + numNodesExplored); //Use for extracting data for BFS

                return new PathInfo(shortestPath, numNodesExplored);
            }

            // For all neighbors adjacent to item ... (item can have 0 - 4 viable neighbors)
            // Check below item (row + 1, col).
            if(item.getRow() + 1 < mazeBFS.length) {    // Bound check.
                // If neighbor not marked explored ... (and not an obstacle)
                if(mazeBFS[item.getRow() + 1][item.getCol()] < 1 && !(mazeBFS[item.getRow() + 1][item.getCol()] > 1)) {
                    mazeBFS[item.getRow() + 1][item.getCol()] = 1;          // Mark explored ...
                    indexMaze[item.getRow() + 1][item.getCol()] = new Index(item.getRow() + 1, item.getCol(), item.getDistance() + 1, item);
                    queue.add(new Index(item.getRow() + 1, item.getCol(), item.getDistance() + 1, item)); // Add neighbor to queue.
                }
            }

            // Check right of item (row, col + 1).
            if(item.getCol() + 1 < mazeBFS.length) {    // Bound check.
                // If neighbor not marked explored ... (and not an obstacle)
                if(mazeBFS[item.getRow()][item.getCol() + 1] < 1 && !(mazeBFS[item.getRow()][item.getCol() + 1] > 1)) {
                    mazeBFS[item.getRow()][item.getCol() + 1] = 1;          // Mark explored ...
                    indexMaze[item.getRow()][item.getCol() + 1] = new Index(item.getRow(), item.getCol() + 1, item.getDistance() + 1, item);
                    queue.add(new Index(item.getRow(), item.getCol() + 1, item.getDistance() + 1, item)); // Add neighbor to queue.
                }
            }

            // Check left of item (row, col - 1).
            if(item.getCol() - 1 >= 0) {    // Bound check.
                // If neighbor not marked explored ... (and not an obstacle)
                if(mazeBFS[item.getRow()][item.getCol() - 1] < 1 && !(mazeBFS[item.getRow()][item.getCol() - 1] > 1)) {
                    mazeBFS[item.getRow()][item.getCol() - 1] = 1;          // Mark explored ...
                    indexMaze[item.getRow()][item.getCol() - 1] = new Index(item.getRow(), item.getCol() - 1, item.getDistance() + 1, item);
                    queue.add(new Index(item.getRow(), item.getCol() - 1, item.getDistance() + 1, item)); // Add neighbor to queue.
                }
            }

            // Check above item (row - 1, col).
            if(item.getRow() - 1 >= 0) {    // Bound check.
                // If neighbor not marked explored ... (and not an obstacle)
                if(mazeBFS[item.getRow() - 1][item.getCol()] < 1 && !(mazeBFS[item.getRow() - 1][item.getCol()] > 1)) {
                    mazeBFS[item.getRow() - 1][item.getCol()] = 1;          // Mark explored ...
                    indexMaze[item.getRow() - 1][item.getCol()] = new Index(item.getRow() - 1, item.getCol(), item.getDistance() + 1, item);
                    queue.add(new Index(item.getRow() - 1, item.getCol(), item.getDistance() + 1, item)); // Add neighbor to queue.
                }
            }
        }

        int numNodesExplored = 0;
        for(int i = 0; i < mazeBFS.length; i ++) {
            for(int j = 0; j < mazeBFS.length; j ++) {
                if(mazeBFS[i][j] == 1) {
                    numNodesExplored ++;
                }
            }
        }
        return new PathInfo(null, numNodesExplored);
    }

    /**
     * BFS search for goal.
     * @return True if goal is reachable; false otherwise.
     * /TODO: Think about how we can utilize distance from source to backtrack from goal and obtain the shortest path.
     * /TODO: (ideas): HashMap that stores information on a particular spot in the maze, and points to a parent (seems wack tbh)
     * /TODO: (ideas): for every part of the maze that has a 1, have that spot in a parallel matrix be equal to the distance from the source
     */
    public static PathInfo BFSMaze(boolean print) {
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
                //printMazeASCII(mazeBFS); // DEBUG

                //Count the number of nodes explored by BFS
                
                int numNodesExplored = 0;
                for(int i = 0; i < mazeBFS.length; i ++) {
                    for(int j = 0; j < mazeBFS.length; j ++) {
                        if(mazeBFS[i][j] == 1) {
                            numNodesExplored ++;
                        }
                    }
                }
                //System.out.println("# Nodes Explored: " + numNodesExplored); //Use for extracting data for BFS

                if(print) {
                    printMaze(mazeBFS);
                }
                return new PathInfo(findShortestPath(indexMaze), numNodesExplored);
            }

            // For all neighbors adjacent to item ... (item can have 0 - 4 viable neighbors)
            // Check below item (row + 1, col).
            if(item.getRow() + 1 < mazeBFS.length) {    // Bound check.
                // If neighbor not marked explored ... (and not an obstacle)
                if(mazeBFS[item.getRow() + 1][item.getCol()] < 1 && !(mazeBFS[item.getRow() + 1][item.getCol()] > 1)) {
                    mazeBFS[item.getRow() + 1][item.getCol()] = 1;          // Mark explored ...
                    indexMaze[item.getRow() + 1][item.getCol()] = new Index(item.getRow() + 1, item.getCol(), item.getDistance() + 1, item);
                    queue.add(new Index(item.getRow() + 1, item.getCol(), item.getDistance() + 1, item)); // Add neighbor to queue.
                }
            }
            
            // Check right of item (row, col + 1).
            if(item.getCol() + 1 < mazeBFS.length) {    // Bound check.
                // If neighbor not marked explored ... (and not an obstacle)
                if(mazeBFS[item.getRow()][item.getCol() + 1] < 1 && !(mazeBFS[item.getRow()][item.getCol() + 1] > 1)) {
                    mazeBFS[item.getRow()][item.getCol() + 1] = 1;          // Mark explored ...
                    indexMaze[item.getRow()][item.getCol() + 1] = new Index(item.getRow(), item.getCol() + 1, item.getDistance() + 1, item);
                    queue.add(new Index(item.getRow(), item.getCol() + 1, item.getDistance() + 1, item)); // Add neighbor to queue.
                }
            }

            // Check left of item (row, col - 1).
            if(item.getCol() - 1 >= 0) {    // Bound check.
                // If neighbor not marked explored ... (and not an obstacle)
                if(mazeBFS[item.getRow()][item.getCol() - 1] < 1 && !(mazeBFS[item.getRow()][item.getCol() - 1] > 1)) {
                    mazeBFS[item.getRow()][item.getCol() - 1] = 1;          // Mark explored ...
                    indexMaze[item.getRow()][item.getCol() - 1] = new Index(item.getRow(), item.getCol() - 1, item.getDistance() + 1, item);
                    queue.add(new Index(item.getRow(), item.getCol() - 1, item.getDistance() + 1, item)); // Add neighbor to queue.
                }
            }

            // Check above item (row - 1, col).
            if(item.getRow() - 1 >= 0) {    // Bound check.
                // If neighbor not marked explored ... (and not an obstacle)
                if(mazeBFS[item.getRow() - 1][item.getCol()] < 1 && !(mazeBFS[item.getRow() - 1][item.getCol()] > 1)) {
                    mazeBFS[item.getRow() - 1][item.getCol()] = 1;          // Mark explored ...
                    indexMaze[item.getRow() - 1][item.getCol()] = new Index(item.getRow() - 1, item.getCol(), item.getDistance() + 1, item);
                    queue.add(new Index(item.getRow() - 1, item.getCol(), item.getDistance() + 1, item)); // Add neighbor to queue.
                }
            }
        }
        //printMaze(mazeBFS); // DEBUG
        int numNodesExplored = 0;
        for(int i = 0; i < mazeBFS.length; i ++) {
            for(int j = 0; j < mazeBFS.length; j ++) {
                if(mazeBFS[i][j] == 1) {
                    numNodesExplored ++;
                }
            }
        }
        //System.out.println("# Nodes Explored: " + numNodesExplored); //Use for extracting data for BFS

        if(print) {
            printMaze(mazeBFS);
        }
        return new PathInfo(null, numNodesExplored);
    }

    /**
     * Helper method to check neighbors & add to queue.
     * @param item - Current item.
     * @param queue - The queue to add neighbors to.
     */
    private static void AStarCheckNeighbors(int[][] maze, Index[][] indexMaze, Index item, PriorityQueue<Index> queue) {
        Index start = new Index(0, 0, 0, null);
        Index goal = new Index(maze.length - 1, maze.length - 1, 0, null);

        // Check neighbor below item. (row + 1, col)
        if(item.getRow() + 1 < maze.length && maze[item.getRow() + 1][item.getCol()] == 0) {    // Bound check & not visited.
            maze[item.getRow() + 1][item.getCol()] = 1; // Mark visited.
            Index neighbor = new Index(item.getRow() + 1, item.getCol(), 0, item);
            neighbor.setDist(Index.distTwoPoints(start, neighbor)); // Set Euclidean distance.
            neighbor.setScore(Index.distTwoPoints(item, neighbor) + Index.distTwoPoints(neighbor, goal)); // f(n) = g(n) + h(n)
            queue.add(neighbor);

            indexMaze[neighbor.getRow()][neighbor.getCol()] = neighbor;
        }

        // Check neighbor right of item. (row, col + 1)
        if(item.getCol() + 1 < maze.length && maze[item.getRow()][item.getCol() + 1] == 0) {    // Bound check & not visited.
            maze[item.getRow()][item.getCol() + 1] = 1; // Mark visited.
            Index neighbor = new Index(item.getRow(), item.getCol() + 1, 0, item);
            neighbor.setDist(Index.distTwoPoints(start, neighbor)); // Set Euclidean distance.
            neighbor.setScore(Index.distTwoPoints(item, neighbor) + Index.distTwoPoints(neighbor, goal)); // f(n) = g(n) + h(n)
            queue.add(neighbor);

            indexMaze[neighbor.getRow()][neighbor.getCol()] = neighbor;
        }

        // Check neighbor left of item. (row, col - 1)
        if(item.getCol() - 1 >= 0 && maze[item.getRow()][item.getCol() - 1] == 0) {    // Bound check & not visited.
            maze[item.getRow()][item.getCol() - 1] = 1; // Mark visited.
            Index neighbor = new Index(item.getRow(), item.getCol() - 1, 0, item);
            neighbor.setDist(Index.distTwoPoints(start, neighbor)); // Set Euclidean distance.
            neighbor.setScore(Index.distTwoPoints(item, neighbor) + Index.distTwoPoints(neighbor, goal)); // f(n) = g(n) + h(n)
            queue.add(neighbor);

            indexMaze[neighbor.getRow()][neighbor.getCol()] = neighbor;
        }

        // Check neighbor above item. (row - 1, col)
        if(item.getRow() - 1 >= 0 && maze[item.getRow() - 1][item.getCol()] == 0) {    // Bound check & not visited.
            maze[item.getRow() - 1][item.getCol()] = 1; // Mark visited.
            Index neighbor = new Index(item.getRow() - 1, item.getCol(), 0, item);
            neighbor.setDist(Index.distTwoPoints(start, neighbor)); // Set Euclidean distance.
            neighbor.setScore(Index.distTwoPoints(item, neighbor) + Index.distTwoPoints(neighbor, goal)); // f(n) = g(n) + h(n)
            queue.add(neighbor);

            indexMaze[neighbor.getRow()][neighbor.getCol()] = neighbor;
        }
    }

    /**
     * A* search for goal. Priority queue is a min heap.
     * Priority queue items has priority f(n) = g(n) + h(n).
     * g(n) is distance travelled from start to n.
     * h(n) is an estimated distance from n to goal.
     * @return True if goal is reachable; false otherwise.
     */
    public static PathInfo AStarMaze(boolean print) {
        int[][] mazeAStar = copyMaze();
        Index[][] indexMaze = new Index[mazeAStar.length][mazeAStar[0].length];
        PriorityQueue<Index> minHeap = new PriorityQueue<Index>();
        mazeAStar[0][0] = 1; // Mark start visited.
        indexMaze[0][0] = new Index(0, 0, 0, null);
        minHeap.add(new Index(0, 0, 0, null));  // Add start to queue.
        
        // While heap is not empty ...
        while(!(minHeap.isEmpty())) {
            Index item = minHeap.remove();  // Remove item from queue.

            // If goal ...
            if(item.getRow() == mazeAStar.length - 1 && item.getCol() == mazeAStar.length - 1) {
                //printMazeASCII(mazeAStar); // DEBUG
                ArrayList<Index> shortestPath = new ArrayList<Index>();
                Index current = indexMaze[mazeAStar.length - 1][mazeAStar.length - 1];

                //Determine the exact route of the shortest path.
                while(current.getRow() != 0 || current.getCol() != 0) {
                    shortestPath.add(current);
                    current = current.getParent();
                }
                shortestPath.add(indexMaze[0][0]);
                Collections.reverse(shortestPath);

                //Determine the number of nodes visited by A*.
                int numNodesExplored = 0;
                for(int i = 0; i < mazeAStar.length; i ++) {
                    for(int j = 0; j < mazeAStar.length; j ++) {
                        if(mazeAStar[i][j] == 1) {
                            numNodesExplored ++;
                        }
                    }
                }
                // System.out.println("# Nodes Explored: " + numNodesExplored); //Use for extracting data for A*

                if(print) {
                    printMaze(mazeAStar);
                }
                return new PathInfo(shortestPath, numNodesExplored);
            }

            // Check neighbors of item.
            AStarCheckNeighbors(mazeAStar, indexMaze, item, minHeap);
        }

        //printMaze(mazeAStar); // DEBUG
        int numNodesExplored = 0;
        for(int i = 0; i < mazeAStar.length; i ++) {
            for(int j = 0; j < mazeAStar.length; j ++) {
                if(mazeAStar[i][j] == 1) {
                    numNodesExplored ++;
                }
            }
        }
        //System.out.println("# Nodes Explored: " + numNodesExplored); //Use for extracting data for A*

        if(print) {
            printMaze(mazeAStar);
        }
        return new PathInfo(null, numNodesExplored);
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

    /**
     * Basically prints a more readable maze.
     * @param maze - Maze to print.
     */
    public static void printMazeASCII(int[][] maze) {
        for(int row = 0; row < maze.length; row++) {
            for(int col = 0; col < maze[row].length; col++) {
                
                if(maze[row][col] == 0) {
                    System.out.print("\u2591\u2591");
                } else if(maze[row][col] == 1) {
                    System.out.print("\u2593\u2593");
                } else if(maze[row][col] == 2) {
                    System.out.print("\u2588\u2588");
                } else {    // Everything else for now.
                    System.out.print(maze[row][col] + " ");
                }
            }
            System.out.println();
        }
    }

    /**
     * Method that implements Strategy One of stepping through the maze.
     * @param q - The flammability.
     * @return true if the agent reaches the end, false otherwise.
     */
    public static boolean stratOne(double q) {
        /*  STRATEGY ONE  */
        //At the start of the maze, wherever the fire is, solve for the shortest path from upper left to lower right,
        // and follow it until the agent exits the maze or burns

        //Generate a solvable maze.
        PathInfo pathInfo;
        do {
            generateMaze(100, 0.3, true);
            pathInfo = BFSMaze(false);
        } while(pathInfo.getShortestPath() == null);
        //printMaze(maze); //debug

        //have the agent start at the starting point, and have them step through.
        Index agent = new Index(0, 0);
        int i = 1;
        while(agent.getRow() != maze.length - 1 || agent.getCol() != maze.length - 1) {
            Index nextSpot = pathInfo.getShortestPath().get(i); //object that obtains the next spot to move to in the shortestPath
            agent.setRow(nextSpot.getRow()); //agent steps to nextSpot's row
            agent.setCol(nextSpot.getCol()); //agent steps to nextSpot's col
            //System.out.println("Agent has stepped to: " + agent); //debug

            if(maze[agent.getRow()][agent.getCol()] == 3) { //check if the spot the agent just moved to is on fire
                //System.out.println("Agent has stepped in fire."); //debug
                //printMaze(maze); //debug
                return false; //the task is over once the agent steps into fire
            }
            advanceFireOneStep(maze, q); //agent has stepped once, now the fire has to advance once
            i ++;
        }

        //System.out.println("Agent has reached the end."); //debug
        //printMaze(maze); //debug
        return true;
    }

    /**
     * Method that implements Strategy Two of stepping through the maze.
     * @param q - The flammability.
     * @return true if the agent reaches the end, false otherwise.
     */
    public static boolean stratTwo(double q) {
        // At every time step, re-compute the shortest path from the agent’s current position to the goal position, based on  the  current  state  of  the  maze  and  the  fire.
        // Follow  this  new  path  one  time  step,  then  re-compute.
        // This strategy constantly re-adjusts its plan based on the evolution of the fire.
        // If the agent gets trapped with no path to the goal, it dies.

        //Generate a solvable maze.
        PathInfo pathInfo;
        do {
            generateMaze(100, 0.3, true);
            pathInfo = BFSMaze(false);
        } while(pathInfo.getShortestPath() == null);

        Index agent = new Index(0, 0);
        while(agent.getRow() != maze.length - 1 || agent.getCol() != maze.length - 1) {
            Index nextSpot = pathInfo.getShortestPath().get(1);
            agent.setRow(nextSpot.getRow());
            agent.setCol(nextSpot.getCol());

            if(maze[agent.getRow()][agent.getCol()] == 3) {
                //System.out.println("Agent has stepped in fire."); //debug
                return false;
            }

            //Determine after the fire if maze is still solvable
            advanceFireOneStep(maze, q);
            PathInfo newPath = BFSFromPosition(agent);
            if(newPath.getShortestPath() == null) {
                //System.out.println("Maze no longer solvable."); //debug
                return false;
            }
            pathInfo = newPath;
        }

        //System.out.println("Agent has exited the maze."); //debug
        return true;
    }

    /**
     * Method that implements Strategy 3 of stepping through the maze.
     * @param q - The flammability.
     * @return true if the agent reaches the end, false otherwise.
     */
    public static boolean stratThree(double q) {
        // TODO
        return false;
    }

    public static void main(String[] args) {
        long startTime;
        long endTime;

        generateMaze(10, 0.3, false);   // Generate a maze by default.

        Scanner sc = new Scanner(System.in);

        String help = "Commands\nq | quit: quit the program\n" 
            + "g: generate new maze\np | print: print maze\nr: a reference for values printed by 'p' or 'print'\n"
            + "pr: print readable version of maze\ndfs: run DFS on maze\n"
            + "bfs: run BFS on maze\na | a*: run A* on maze\n"
            + "strat1: run strategy 1 on maze\nstrat2: run strategy 2 on maze\nstrat3: run strategy 3 on maze\n"
            + "h | help: show this list again\nh [g|p|print|pr] | help [g|p|print|pr]: get more info on command";

        System.out.println(help + "\n");

        while(true) {
            String command = sc.nextLine();
            if(command.equalsIgnoreCase("q") || command.equalsIgnoreCase("quit")) { // Quit program.
                break;
            } else if(command.equalsIgnoreCase("g")) {  // Generate new maze.
                // Default values.
                int dim = 10;
                double p = 0.3;

                try {
                    System.out.print("Enter maze dimension greater than 0 (integer): ");
                    dim = sc.nextInt();
                    sc.nextLine();

                    while(dim < 1) {
                        System.out.print("Enter maze dimension greater than 0 (integer): ");
                        dim = sc.nextInt();
                        sc.nextLine();
                    }

                    System.out.print("Enter obstacle density between 0.0 and 1.0 (double): ");
                    p = sc.nextDouble();
                    sc.nextLine();

                    while(p < 0.0 || p > 1.0) {
                        System.out.print("Enter obstacle density between 0.0 and 1.0 (double): ");
                        p = sc.nextDouble();
                        sc.nextLine();
                    }

                    generateMaze(dim, p, false);
                } catch (Exception e) {
                    System.out.println("Please enter numbers only!");
                }
                System.out.println();
            } else if(command.equalsIgnoreCase("p") || command.equalsIgnoreCase("print")) { // Print maze.
                System.out.println("Original maze:");
                printMaze(maze);
                System.out.println();
            } else if(command.equalsIgnoreCase("r")) {
                System.out.println("0: Free space\n1: Explored space\n2: Maze wall/obstacle\n3: Fire\n");
            } else if(command.equalsIgnoreCase("pr")) { // Print maze (readable).
                System.out.println("Original maze:");
                printMazeASCII(maze);
                System.out.println();
            } else if(command.equalsIgnoreCase("dfs")) {    // DFS
                System.out.println("Depth-First Search (DFS):");
                System.out.println("Print result? (y/n): ");
                String answer = sc.nextLine();

                startTime = System.nanoTime();
                DFSMaze(answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("yes"));
                endTime = System.nanoTime();

                System.out.println(String.format("Time elapsed: %.3f s", (float)(endTime - startTime)/1000000000));
                System.out.println();
            } else if(command.equalsIgnoreCase("bfs")) {    // BFS
                System.out.println("Breadth-First Search (BFS):");
                System.out.println("Print result? (y/n): ");
                String answer = sc.nextLine();

                startTime = System.nanoTime();
                BFSMaze(answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("yes"));
                endTime = System.nanoTime();

                System.out.println(String.format("Time elapsed: %.3f s", (float)(endTime - startTime)/1000000000));
                System.out.println();
            } else if(command.equalsIgnoreCase("a") || command.equalsIgnoreCase("a*")) {    // A*
                System.out.println("A* Search:");
                System.out.println("Print result? (y/n): ");
                String answer = sc.nextLine();

                startTime = System.nanoTime();
                AStarMaze(answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("yes"));
                endTime = System.nanoTime();

                System.out.println(String.format("Time elapsed: %.3f s", (float)(endTime - startTime)/1000000000));
                System.out.println();
            } else if(command.equalsIgnoreCase("strat1") || command.equalsIgnoreCase("strat2") || command.equalsIgnoreCase("strat2")) { // Strategies
                try {
                    System.out.print("Enter flammability rate between 0.0 and 1.0 (double): ");
                    double q = sc.nextDouble();
                    sc.nextLine();
    
                    while(q < 0.0 || q > 1.0) {
                        System.out.print("Enter flammability rate between 0.0 and 1.0 (double): ");
                        q = sc.nextDouble();
                        sc.nextLine();
                    }
    
                    if(command.equalsIgnoreCase("strat1")) {
                        stratOne(q);
                    } else if(command.equalsIgnoreCase("strat2")) {
                        stratTwo(q);
                    } else if(command.equalsIgnoreCase("strat3")) {
                        stratThree(q);
                    }
                } catch (Exception e) {
                    System.out.println("Please enter numbers only!");
                }
                System.out.println();
            } else if(command.equalsIgnoreCase("h") || command.equalsIgnoreCase("help")) {  // Help
                System.out.println(help + "\n");
            } else if(command.equalsIgnoreCase("h g") || command.equalsIgnoreCase("help g")) {
                System.out.println("By default, this program pre-generates a 10x10 maze with obstacle density p = 0.3.");
                System.out.println("This command will prompt you for a dimension (integers > 0 only) and an obstacle density (numbers and decimals between 0.0 and 1.0, inclusive).\n");
            } else if(command.equalsIgnoreCase("h p") || command.equalsIgnoreCase("help p") || command.equalsIgnoreCase("help p") || command.equalsIgnoreCase("help print")) {
                System.out.println("This command will print the maze that has been generated by the program in its default/original state.");
                System.out.println("The numbers indicates the type of space. Type 'r' for a reference on the values.");
            } else if(command.equalsIgnoreCase("h pr") || command.equalsIgnoreCase("help pr")) {
                System.out.println("This command will print the maze as something more graphical than the regular print, using special ASCII characters to represent the spaces.");
                System.out.println("ASCII characters may not be represented properly in the terminal or IDE depending on the font. Consider outputting the console outputs to a text file to see.");
            } else {    // Invalid commands
                System.out.println("Invalid command! Type 'h' or 'help' for a list of valid commands.\nType 'q' or 'quit' to quit the program.\n");
            }
        }

        sc.close();

        //At the start, a randomly selected open spot in the maze is set to "on fire"
        /*
        printMaze(maze);
        System.out.println();
        advanceFireOneStep(maze, 0.3); //the fire spreads
        printMaze(maze);*/

        /* DFS Plot */
        /*
        for(double i = 0.1; i <= 1.0; i += 0.1) {
            int numSuccess = 0;
            int numAttempts = 0;
            for(int j = 0; j < 100; j ++) {
                generateMaze(750, i, false);
                boolean reachable = DFSMaze();
                if(reachable) {
                    System.out.println(true);
                    numSuccess ++;
                }
                else {
                    System.out.println(false);
                }
                numAttempts ++;
            }
            System.out.println("--For p = " + i + ", P[G reachable] = " + ((double)numSuccess / (double)numAttempts) + "\n");
        }
         */

        /* BFS Plot and A* Plot */
        //Number of nodes explored by BFS vs. obstacle density p
        /*
        for(double i = 0.1; i <= 1.0; i += 0.1) {
            double avg = 0;
            System.out.println("--For p = " + i);
            for(int j = 0; j < 100; j ++) {
                generateMaze(750, i, false);
                PathInfo pathInfo = BFSMaze();
                avg += pathInfo.getNumNodesExplored();
            }
            avg = avg / 100.0;
            System.out.println("Average # Nodes Explored: " + avg + "\n");
        }
        */
        /*
        for(double i = 0.1; i <= 1.0; i += 0.1) {
            double avg = 0;
            System.out.println("--For p = " + i);
            for(int j = 0; j < 100; j ++) {
                generateMaze(750, i, false);
                PathInfo pathInfo = AStarMaze();
                avg += pathInfo.getNumNodesExplored();
            }
            avg = avg / 100.0;
            System.out.println("Average # Nodes Explored: " + avg + "\n");
        }
        */

        /* Strat One Plot */
        /*
        //Plot, for Strategy 1, 2, and 3, a graph of 'average strategy success rate’ vs ‘flammability q’ at p= 0.3.
        for(double q = 0.1; q <= 1.0; q += 0.1) {
            double avgSuccess = 0;
            System.out.println("--For q = " + q);
            for(int j = 0; j < 100; j ++) {
                boolean attempt = stratOne(q); //Implement strat one and see whether the agent reached or not
                System.out.println(attempt);
                if(attempt) {
                    avgSuccess += 1.0;
                }
            }
            avgSuccess = avgSuccess / 100.0;
            System.out.println("Average Success: " + avgSuccess + "\n");
        }
        */
        /* Strat Two Plot */
        /*
        for(double q = 0.1; q <= 1.0; q += 0.1) {
            double avgSuccess = 0;
            System.out.println("--For q = " + q);
            for(int j = 0; j < 100; j ++) {
                boolean attempt = stratTwo(q);
                System.out.println(attempt);
                if(attempt) {
                    avgSuccess += 1.0;
                }
            }
            avgSuccess = avgSuccess / 100.0;
            System.out.println("Average Success: " + avgSuccess + "\n");
        }
        */
    }
}