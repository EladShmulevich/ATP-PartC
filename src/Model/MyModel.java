package Model;

import Client.Client;
import Client.IClientStrategy;
import IO.MyDecompressorInputStream;
import Server.Server;
import algorithms.mazeGenerators.Maze;
import algorithms.search.SearchableMaze;
import algorithms.search.Solution;
import Server.ServerStrategySolveSearchProblem;
import Server.ServerStrategyGenerateMaze;
import javafx.beans.InvalidationListener;
import java.util.Observable;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Observer;

public class MyModel extends Observable implements IModel {
    private Maze maze;
    private SearchableMaze searchableMaze;
    private Solution solution;
    Server mazeGeneratingServer;
    Server solveServer;
    private int rowUser;
    private int colUser;
    private int rowGoal;
    private int colGoal;
    private boolean reachGoal;

    public MyModel() {
        maze = null;
        searchableMaze = null;
        rowUser = 0;
        colUser = 0;
        reachGoal=false;
        this.mazeGeneratingServer = new Server(5400,1000, new ServerStrategyGenerateMaze());
        this.solveServer = new Server(5401,1000,new ServerStrategySolveSearchProblem());
        mazeGeneratingServer.start();
        solveServer.start();
    }

    @Override
    public void assignObserver(Observer o) {
        this.addObserver(o);
    }

    @Override
    public void generateMaze(int rows, int cols) {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{rows, cols};
                        toServer.writeObject(mazeDimensions); //send maze dimensions to server
                        toServer.flush();
                        byte[] compressedMaze = (byte[]) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[rows*cols + 10000 /*CHANGE SIZE ACCORDING TO YOU MAZE SIZE*/]; //allocating byte[] for the decompressed maze -
                        is.read(decompressedMaze); //Fill decompressedMaze with bytes
                        maze = new Maze(decompressedMaze);
                        rowUser = maze.getStartPosition().getRowIndex();
                        colUser = maze.getStartPosition().getColumnIndex();
                        rowGoal = maze.getGoalPosition().getRowIndex();
                        colGoal = maze.getGoalPosition().getColumnIndex();
                        setMaze(maze);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }

    public void setMaze(Maze maze) {
        this.maze = maze;
    }

    public SearchableMaze getSearchableMaze() {
        return searchableMaze;
    }

    public void setSearchableMaze(SearchableMaze searchableMaze) {
        this.searchableMaze = searchableMaze;
    }

    public Solution getSolution() {
        return solution;
    }

    public void setSolution(Solution solution) {
        this.solution = solution;
    }


    public int getRowUser() {
        return rowUser;
    }

    public void setRowUser(int rowUser) {
        this.rowUser = rowUser;
    }

    public int getColUser() {
        return colUser;
    }

    public void setColUser(int colUser) {
        this.colUser = colUser;
    }

    public int getRowGoal() {
        return rowGoal;
    }

    public void setRowGoal(int rowGoal) {
        this.rowGoal = rowGoal;
    }

    public int getColGoal() {
        return colGoal;
    }

    public void setColGoal(int colGoal) {
        this.colGoal = colGoal;
    }

    public boolean isReachGoal() {
        return reachGoal;
    }

    public void setReachGoal(boolean reachGoal) {
        this.reachGoal = reachGoal;
    }

    @Override
    public Maze getMaze() {
        return this.maze;
    }



}
