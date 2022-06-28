package ViewModel;

import Model.IModel;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.scene.input.KeyEvent;

import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {

    private IModel model;

    public MyViewModel(IModel model) {
        this.model = model;
        this.model.assignObserver(this);
        //lalalalalala
    }

    public void update(Observable o, Object arg) {
        setChanged();
        notifyObservers();
    }

    public int[][] getMaze() {
        return model.getGrid();
    }

    public int getPlayerRow(){
        return model.getRowUser();
    }

    public int getPlayerCol(){ return model.getColUser();}

    public Solution getSolution(){
        return this.model.getSolution();
    }

    public void generateMaze(int row, int col) {
        this.model.generateMaze(row, col);
        //this.model.stopServers();
    }


    public void movePlayer(KeyEvent keyEvent){
        int direction;
        switch (keyEvent.getCode()){
            case NUMPAD8 -> direction = 1; //UP
            case NUMPAD2 -> direction = 2; //DOWN
            case NUMPAD6 -> direction = 3; //RIGHT;
            case NUMPAD4 -> direction = 4; //LEFT
            case NUMPAD9 -> direction = 5; //UP_RIGHT
            case NUMPAD7 -> direction = 6; //UP_LEFT
            case NUMPAD3 -> direction = 7; //DOWN_RIGHT
            case NUMPAD1 -> direction = 8; //DOWN_LEFT

            default -> {
                // no need to move the player...
                return;
            }
        }
        model.updatePlayerPositionKey(direction);
    }

    public void solveMaze(){
        this.model.solveMaze(model.getRowUser(),model.getColUser());
    }


}
