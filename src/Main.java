import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class Main {
    private char[][] maze;
    private int rows;
    private int columns;

    public Main(String filePath) {
        try {
            Scanner scanner = new Scanner(new File(filePath));

            // Read the maze content
            StringBuilder mazeContent = new StringBuilder();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                mazeContent.append(line);
                mazeContent.append("\n");
            }

            scanner.close();

            // Determine the dimensions of the maze
            rows = mazeContent.toString().trim().split("\n").length;
            columns = mazeContent.indexOf("\n");

            // Initialize the maze array
            maze = new char[rows][columns];

            // Fill the maze array with the content
            int index = 0;
            for (int i = 0; i<rows; i++){
                for (int j = 0; j<columns; j++){
                    maze[i][j] = mazeContent.charAt(index++);
                }
                index++; // Skip the new line characters
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void solveMaze() {
        int startX = -1;
        int startY = -1;

        // Find the starting point in the maze
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (maze[i][j] == 'S') {
                    startX = i;
                    startY = j;
                    break;
                }
            }
            if (startX != -1 && startY != -1){
                break;
            }
        }

        if (startX == -1 || startY == -1) {
            System.out.println("Starting point not found in the maze.");
            return;
        }

        // Call the stack-based function to solve the maze
        boolean[][] visited = new boolean[rows][columns];
        boolean found = solveMazeWithStack(startX, startY, visited);

        if (found) {
            System.out.println("Maze solved! Here is the pathway:");
            printMaze(visited);
        } else {
            System.out.println("Sorry no pathway found to reach the end point try another pathway.");
        }
    }

    public boolean solveMazeWithStack(int startX, int startY, boolean[][] visited) {
        stackMaze<Point> stack = new stackMaze<>();
        stack.push(new Point(startX, startY));

        while (!stack.isEmpty()) {
            Point currentPoint = stack.pop();
            int x = currentPoint.x;
            int y = currentPoint.y;

            // Check if the current position is out of bounds or a wall
            if (x < 0 || x >= rows || y < 0 || y >= columns || maze[x][y] == '#')
                continue;

            // Check if the current position is the end point
            if (maze[x][y] == 'E')
                return true;

            // Check if the current position has already been visited
            if (visited[x][y])
                continue;

            visited[x][y] = true;

            // Push neighboring positions onto the stack
            stack.push(new Point(x - 1, y)); // Up
            stack.push(new Point(x + 1, y)); // Down
            stack.push(new Point(x, y - 1)); // Left
            stack.push(new Point(x, y + 1)); // Right
        }

        return false;
    }

    private void printMaze(boolean[][] visited) {
        boolean startFound = false;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (!startFound && maze[i][j] == 'S') {
                    System.out.print("S ");
                    startFound = true;
                } else if (visited[i][j]) {
                    System.out.print("* ");
                } else {
                    System.out.print(maze[i][j] + " ");
                }
            }
            System.out.println();
        }
    }

    private static class Point {
        int x;
        int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public static void main(String[] args) {
        String filePath = "Maze.txt"; //Path of maze text file
        Main mazeSolver = new Main(filePath);
        mazeSolver.solveMaze();
    }
}
