package View;

import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.Solution;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.search.*;

import java.util.ArrayList;

public class MazeDisplayer extends Canvas {
    Maze maze;
    int row_user, col_user = 0;
    int row_goal, col_goal;
    Solution solution = null;
    boolean endGame = false;

    public void drawMaze(int[][] maze) {

    }

    public void draw() {
        double canvasHeight = getHeight();
        double canvasWidth = getWidth();
        double rows = maze.getMaze().length;
        double cols = maze.getMaze()[0].length;
        int[][] mazeGrid = maze.getMaze();
        double cellHeight = canvasHeight / rows;
        double cellWidth = canvasWidth / cols;

        Image wall = new Image("/resources/Backround/wall.png");
        Image solPath = new Image("/resources/Backround/stardust.png");
        Image done = new Image("resources/Backround/great-job.png");


        GraphicsContext graphicsContext = getGraphicsContext2D();
        graphicsContext.clearRect(0, 0, canvasWidth, canvasHeight);
        graphicsContext.setFill(Color.BLACK);

        ArrayList<AState> path = null;
        if (solution != null)
            path = solution.getSolutionPath();


        double x,y;
        // draw the maze
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (mazeGrid[i][j] == 1) {
                    x= i * cellHeight;
                    y = j * cellWidth;
                    if(wall != null){
                        graphicsContext.drawImage(wall, y, x, cellHeight, cellWidth);
                    }
                    else {
                        graphicsContext.fillRect(x, y, canvasHeight, canvasWidth);
                    }
                }

                if(solution != null){
                    AState p  = new MazeState(0, new Position(i,j));
                    assert path != null;
                    if(path.contains(p)){
                        graphicsContext.drawImage(solPath, j * cellWidth, i * cellHeight, canvasHeight, canvasWidth);
                    }
                }
            }
        }

            }


}
