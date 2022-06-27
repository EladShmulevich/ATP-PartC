package ViewModel;

import Model.IModel;
import algorithms.mazeGenerators.Maze;

import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {

    private IModel model;
    private Maze maze;

    public MyViewModel(IModel model) {
        this.model = model;
        this.model.assignObserver(this);
    }

    public Maze getMaze() {
        return maze;
    }

    public void setMaze(Maze maze) {
        this.maze = maze;
    }

    public void generateMaze(int row, int col) {
        this.model.generateMaze(row, col);
    }


    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof IModel){
            if(this.maze == null){

            }
        }

    }
}
