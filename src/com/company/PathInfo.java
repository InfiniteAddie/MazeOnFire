package com.company;

import java.util.ArrayList;

/**
 * Class that holds information regarding the shortest path and the number of nodes explored by both BFS and A*.
 * @author Aditya Dhawan, Annie Thach
 */
public class PathInfo {
    private ArrayList<Index> shortestPath;
    private int numNodesExplored;

    /**
     * Constructor to store shortestPath ArrayList and numNodesExplored by the search algo.
     * @param shortestPath - The list containing the shortest path to take in order.
     * @param numNodesExplored - The number of nodes explored by the search algo.
     */
    public PathInfo(ArrayList<Index> shortestPath, int numNodesExplored) {
        this.shortestPath = shortestPath;
        this.numNodesExplored = numNodesExplored;
    }

    /**
     * Getter method for the shortest path.
     * @return the shortest path.
     */
    public ArrayList<Index> getShortestPath() {
        return shortestPath;
    }

    /**
     * Getter method for the number of nodes explored.
     * @return the number of nodes explored.
     */
    public int getNumNodesExplored() {
        return numNodesExplored;
    }

}