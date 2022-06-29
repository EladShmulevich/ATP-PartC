package View;

import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import algorithms.mazeGenerators.Maze;
import algorithms.search.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class MazeDisplayer extends Canvas {
    private int[][] maze;
    private Solution solution;

    private Maze maze1;

    // player position:
    private int playerRow = 0;
    private int playerCol = 0;
    int row_goal, col_goal;
    boolean endGame = false;
    private boolean roundFirst = true;
    MyViewController controller;


    // wall and player images:
    StringProperty imageFileNameWall = new SimpleStringProperty();
    StringProperty imageFileNamePlayer = new SimpleStringProperty();
    StringProperty imageFileNameGoal = new SimpleStringProperty();

    StringProperty imageFileNameFinish = new SimpleStringProperty();




    public MazeDisplayer() {
        widthProperty().addListener(evt -> {
            draw();
        });
        heightProperty().addListener(evt -> {
            draw();
        });
    }


    public String getImageFileNameFinish() {
        return imageFileNameFinish.get();
    }
    public void setImageFileNameFinish(String imageFileNameFinish) {
        this.imageFileNameFinish.set(imageFileNameFinish);
    }

    public String getImageFileNameWall() {
        return imageFileNameWall.get();
    }
    public void setImageFileNameWall(String imageFileNameWall) {
        this.imageFileNameWall.set(imageFileNameWall);
    }

    public String getImageFileNamePlayer() {
        return imageFileNamePlayer.get();
    }



    public void setImageFileNamePlayer(String imageFileNamePlayer) {
        this.imageFileNamePlayer.set(imageFileNamePlayer);
    }


    public String getImageFileNameGoal() {
        return imageFileNameGoal.get();
    }

    public void setImageFileNameGoal(String imageFileNameGoal) {
        this.imageFileNameGoal.set(imageFileNameGoal);
    }

    public void setMaze(int[][] maze) {
        this.maze = maze;
    }

    public int[][] getMaze() {
        return maze;
    }

    public int getPlayerRow() {
        return playerRow;
    }

    public int getPlayerCol() {
        return playerCol;
    }

    public void setPlayerPosition(int row, int col) {
        this.playerRow = row;
        this.playerCol = col;
        draw();
    }

    public void setGoalPosition(int row, int col) {
        this.row_goal = row;
        this.col_goal = col;
    }


    public void setSolution(Solution solution) {
        this.solution = solution;
        draw();
    }


    public void resetFirstRound(boolean first) {
        this.roundFirst = first;
    }


    public void drawMaze(Maze maze) {
        this.maze1 = maze;
        if(this.roundFirst) {
            this.roundFirst = false;
            setPlayerPosition(maze.getStartPosition().getRowIndex(), maze.getStartPosition().getColumnIndex());
//            setPlayerPosition(controller.getMyViewModel().getStartRow(), controller.getMyViewModel().getStartCol());
        }
        this.endGame = false;
        setGoalPosition(maze.getGoalPosition().getRowIndex(), maze.getGoalPosition().getColumnIndex());
//        setGoalPosition(controller.getMyViewModel().getEndRow(), controller.getMyViewModel().getEndCol());
        draw();
    }


    private void draw() {
        if (maze1 != null) {
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            int rows = maze1.getMaze().length;
            int cols = maze1.getMaze()[0].length;

            double cellHeight = canvasHeight / rows;
            double cellWidth = canvasWidth / cols;

            ArrayList<AState> sol = null;
            if (solution != null){
                sol = solution.getSolutionPath();
            }

            GraphicsContext graphicsContext = getGraphicsContext2D();
            //clear the canvas:
            graphicsContext.clearRect(0, 0, canvasWidth, canvasHeight);

            drawMazeWalls(graphicsContext, cellHeight, cellWidth, rows, cols);

//                drawSolution(graphicsContext, cellHeight, cellWidth);
            drawSolution(graphicsContext, cellHeight, cellWidth, rows, cols, sol);

            Image finishImage = null;
            try {
                finishImage = new Image(new FileInputStream(getImageFileNameFinish()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            if(endGame && finishImage != null) {
                graphicsContext.drawImage(finishImage, 50, 50, 500, 500);

            }
            if(solution != null)
                solution = null;


            drawPlayer(graphicsContext, cellHeight, cellWidth);
            drawGoal(graphicsContext, cellHeight, cellWidth);
        }
    }




    private void drawSolution(GraphicsContext graphicsContext, double cellHeight, double cellWidth, int rows, int cols, ArrayList<AState> path) {
        // need to be implemented
        if(path != null)
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    AState state = new MazeState(0, new Position(i, j));
                    if(path.contains(state)) {
                        graphicsContext.setFill(Color.GREEN);
                        graphicsContext.fillRect(j * cellWidth, i * cellHeight, cellWidth, cellHeight);
                    }
                }
                }

    }


    private void drawMazeWalls(GraphicsContext graphicsContext, double cellHeight, double cellWidth, int rows, int cols) {
        graphicsContext.setFill(Color.RED);
        int[][] maze = maze1.getMaze();

        Image wallImage = null;
        try {
            wallImage = new Image(new FileInputStream(getImageFileNameWall()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no wall image file");
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (maze[i][j] == 1) {
                    //if it is a wall:
                    double w = j * cellWidth;
                    double h = i * cellHeight;
                    if (wallImage == null)
                        graphicsContext.fillRect(w, h, cellWidth, cellHeight);
                    else
                        graphicsContext.drawImage(wallImage, w, h, cellWidth, cellHeight);
                }
            }
        }
    }

    private void drawPlayer(GraphicsContext graphicsContext, double cellHeight, double cellWidth) {
        double x = getPlayerCol() * cellWidth;
        double y = getPlayerRow() * cellHeight;
        graphicsContext.setFill(Color.GREEN);

        Image playerImage = null;
        try {
            playerImage = new Image(new FileInputStream(getImageFileNamePlayer()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no player image file");
        }
        if (playerImage == null)
            graphicsContext.fillRect(x, y, cellWidth, cellHeight);
        else
            graphicsContext.drawImage(playerImage, x, y, cellWidth, cellHeight);
    }


    private void drawGoal(GraphicsContext graphicsContext, double cellHeight, double cellWidth) {
        double x = col_goal * cellWidth;
        double y = row_goal * cellHeight;
        graphicsContext.setFill(Color.BLUE);

        Image goalImage = null;
        try {
            goalImage = new Image(new FileInputStream(getImageFileNameGoal()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no goal image file");
        }
        if (goalImage == null)
            graphicsContext.fillRect(x, y, cellWidth, cellHeight);
        else
            graphicsContext.drawImage(goalImage, x, y, cellWidth, cellHeight);
    }

    public void drawFinished(boolean finished) {
        this.endGame = finished;
        draw();
    }

    @Override
    public double prefWidth(double height) {
        return getWidth();
    }

    @Override
    public double prefHeight(double width) {
        return getHeight();
    }

    public boolean isRoundFirst() {
        return roundFirst;
    }

    public void setRoundFirst(boolean roundFirst) {
        this.roundFirst = roundFirst;
    }

    public double getCellHeight() {
        double canvasHeight = getHeight();
        int mazeHeight = maze1.getMaze().length;
        double cellHeight = (canvasHeight) / mazeHeight;
        return cellHeight;
    }

    public double getCellWidth() {
        double canvasWidth = getWidth();
        int mazeWidth = maze1.getMaze()[0].length;
        double cellWidth = canvasWidth / mazeWidth;
        return cellWidth;
    }

    /*public void draw() {
        double canvasHeight = getHeight();
        double canvasWidth = getWidth();
        double rows = maze.getMaze().length;
        double cols = maze.getMaze()[0].length;
        int[][] mazeGrid = maze.getMaze();
        double cellHeight = canvasHeight / rows;
        double cellWidth = canvasWidth / cols;

        Image wall = new Image("/resources/Background/wall.png");
        Image solPath = new Image("/resources/Background/stardust.png");
        Image done = new Image("resources/Background/great-job.png");


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

    }*/

}
