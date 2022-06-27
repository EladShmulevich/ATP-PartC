package Model;

import algorithms.mazeGenerators.Maze;

import java.util.Observer;

public interface IModel {


    public void assignObserver(Observer o);


    public void generateMaze(int rows, int cols);
    public Maze getMaze();




}
