package ViewModel;

import Model.IModel;
import algorithms.mazeGenerators.Maze;

public class MyViewModel {

    private IModel model;
    private Maze maze;

    public MyViewModel(IModel model) {
        this.model = model;
        this.maze = null;
    }

    public void generateMaze(int row, int col) {
        this.model.generateMaze(row, col);
    }
    public Maze getMaze() {
        return maze;
    }


}
