package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.util.Observer;

public interface IModel {
    public void generateMaze(int rows, int cols);
    public void solveMaze(int row_User,int col_User);
    public void updatePlayerPositionKey(int direction);
    void setPlayerPosition(Position startPosition);
    void setGoalPosition(Position endPosition);
    public void assignObserver(Observer o);

    public void startServers();
    public void stopServers();
    public void refreshStrategies();
    public void saveMaze(File saveFile);
    public void loadMaze(File file);
    public void exit();


    //for later
    void updatePlayerPositionMouse(MouseEvent mouseEvent, double mouseX, double mouseY, double cellHeight, double cellWidth);


    public Solution getSolution();



    public Maze getMaze();

    public int[][] getGrid();




}
