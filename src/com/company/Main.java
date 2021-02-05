package com.company;
import java.util.Random;

public class Main {
    private static int[][] maze;

    public static void generateMaze(int dim, float den) {
        maze = new int[dim][dim];
        Random random = new Random();

        for(int row = 0; row < maze.length; row++) {
            for(int col = 0; col < maze[row].length; col++) {
                // Exclude start and goal spaces.
                if(!((row == 0 && col == 0) || (row == dim - 1 && col == dim - 1)) 
                    && random.nextFloat() <= den) { // nextFloat() generates float betw. 0.0 & 1.0.
                        maze[row][col] = 2;
                }
                else {
                    maze[row][col] = 0;
                }
            }
        }
    }

    public static void printMaze() {
        for(int i = 0; i < maze.length; i++) {
            for(int j = 0; j < maze[i].length; j++) {
                System.out.print(maze[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
    // write your code here
        generateMaze(10, (float)0.5);
        printMaze();
    }
}