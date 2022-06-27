package Model;

import algorithms.mazeGenerators.Maze;

public interface IModel {
    public void generateMaze(int rows, int cols);
    public Maze getMaze();




}
