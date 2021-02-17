# Compile & Run
To compile from command prompt terminal go to the project's source directory from the project directory:

    cd src
    javac com/company/Main.java

To run the program from the source directory:

    java com.company.Main

# Testing Notes
## Testing the Search Algorithms
When testing output of search with terminal, use the command:

    java com.company.Main > output.txt

This will send all terminal outputs to [overwrite] a text file to preserve line breaks when testing larger maze.

Open this text file in Notepad, ESPECIALLY when printing with the printMazeASCII method. It'll make more sense than trying to read it from the terminal. Make sure word wrap is off (look under Format > Word Wrap and make sure there is no check mark).

## Time elapsed: 0.000 s
Time elapsed is in seconds, and it tends to round to 0 in the conversion from nanoseconds to seconds, because the difference is insignificant. Try upping the dimensions of the maze to increase the time.

# Todo
1. Collect data for report.
2. Write report.
3. Write a main to accept user input.
    - It should take in maze dimension and density.
4. More testing.
    - Test time elapsed.
5. Submit by Friday, Februrary 19, 2021.

# Bugs & Issues
## Known
1. findShortestPath method causes infinite loop in A*.
    - Likely due to change #3.

## Fixed
1. DFS ignoring walls.

# Changelog
Most Recent → Oldest
## Wednesday, February 17, 2021
### 5:30 AM
1. Added this README (yay).
    - Added [Compile & Run](#Compile-&-Run).
    - Added [Testing Notes](#Testing-Notes).
    - Added [Todo](#Todo).
    - Added [Bugs & Issues](#Bugs-&-Issues).
    - Added [Changelog](#Changelog).
2. Changed Index's dist attribute type from int to double.
3. Fixed distance assignments for A*.
4. Fixed score assignments for A*.
5. Added earlier exit condition for DFS.
    - When goal is popped from the stack, it can exit.
6. Fix #1: Rewrote neighbor check of DFS to not ignore walls.
7. Added printMazeASCII method.
    - Same thing as printMaze, but it prints a maze that is friendlier to the human eyes.
    - See notes on [Testing the Search Algorithms](#Testing-Notes).
8. **[TEMP!]** Commented out advance fire outputs in main to test search outputs.
9. **[TEMP?]** Changed A* to return null instead of shortest path list with findShortestPath. 
    - See [Known Bugs & Issues](#Bugs-&-Issues).

### 3:00 PM
1. Shifted order of neighbors checked for DFS to prioritize bottom right.
    - Stack is last in, first out (LIFO), so: up → left → right → down.
2. Shifted order of neighbors checked for BFS to prioritize bottom right.
    - Queue is first in, first out (FIFO), so: down → right → left → up.
3. Organized a few methods and comments.

### 4:30 PM
1. Fixed score assignments for A* (again).
2. Formatted time elapsed to show 3 decimal places.