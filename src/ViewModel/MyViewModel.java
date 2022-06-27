package ViewModel;

import Model.IModel;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;

import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {

    private IModel model;
    private Maze maze;
    private int rowUser;
    private int colUser;

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
                this.maze = model.getMaze();
            }
            else{
                Maze maze = model.getMaze();
                if(maze == this.maze){
                    this.rowUser = model.getRowUser();
                    this.colUser = model.getColUser();
                }
                this.maze = maze;
            }
            setChanged();
            notifyObservers();

        }

    }

    public void solveMaze(){
        this.model.solveMaze();
    }

    public Solution getSolution(){
        return this.model.getSolution();
    }
}
