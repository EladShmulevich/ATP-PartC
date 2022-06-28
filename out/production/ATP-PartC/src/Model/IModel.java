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
    void updatePlayerPositionMouse(MouseEvent mouseEvent, double mouseX, double mouseY, double cellHeight, double cellWidth);
    void setPlayerPosition(Position startPosition);
    void setGoalPosition(Position endPosition);
    public void assignObserver(Observer o);

    public void stopServers();
    public void startServers();
    public void saveMaze(File saveFile);
    public void loadMaze(Object file);
    public void exit();
    public Solution getSolution();

    public Maze getMaze();
    public int[][] getGrid();

    int getRowUser();
    int getColUser();
    int getGoalRow();
    int getGoalCol();
}
