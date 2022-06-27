package Model;

import Client.Client;
import Client.IClientStrategy;
import IO.MyDecompressorInputStream;
import Server.Server;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.SearchableMaze;
import algorithms.search.Solution;
import Server.ServerStrategySolveSearchProblem;
import Server.ServerStrategyGenerateMaze;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import java.util.Observable;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Observer;

public class MyModel extends Observable implements IModel {
    private Maze maze;
    private Solution solution;
    Server mazeGeneratingServer;
    Server solveServer;
    Position UserPosition;
    Position GoalPosition;
    private boolean reachGoal;
    private boolean serverRunning;

    public MyModel() {
        this.maze = null;
        this.solution = null;
        this.reachGoal = false;
        this.serverRunning = false;
        this.UserPosition = null;
        this.GoalPosition = null;
        this.mazeGeneratingServer = null;
        this.solveServer = null;
    }


    @Override
    public void startServers() {
        this.mazeGeneratingServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        this.solveServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
        this.mazeGeneratingServer.start();
        this.solveServer.start();
        this.serverRunning = true;
    }

    public void stopServers() {
        this.mazeGeneratingServer.stop();
        this.solveServer.stop();
        serverRunning = false;
    }

    @Override
    public void assignObserver(Observer o) {
        this.addObserver(o);
    }

    @Override
    public void setGoalPosition(Position endPosition) {
        this.GoalPosition = endPosition;
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
                        byte[] decompressedMaze = new byte[rows * cols + 12 /*CHANGE SIZE ACCORDING TO YOU MAZE SIZE*/]; //allocating byte[] for the decompressed maze -
                        is.read(decompressedMaze); //Fill decompressedMaze with bytes
                        maze = new Maze(decompressedMaze);
                        setPlayerPosition(maze.getStartPosition());
                        setGoalPosition(maze.getGoalPosition());
                    } catch (Exception e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Error generating maze");
                        alert.show();
                    }
                }
            });
            client.communicateWithServer();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void updatePlayerPositionKey(int direction) {
        int currentRow = UserPosition.getRowIndex();
        int currentCol = UserPosition.getColumnIndex();
        int totalRows = maze.getMaze().length;
        int totalCols = maze.getMaze()[0].length;
        switch (direction) {
            //UP
            case 1:
                if (currentRow - 1 >= 0 && this.maze.getMaze()[currentRow - 1][currentCol] != 1) {
                    this.setPlayerPosition(new Position(currentRow - 1, currentCol));
                }
                break;
            //DOWN
            case 2:
                if (currentRow + 1 < totalRows && this.maze.getMaze()[currentRow + 1][currentCol] != 1) {
                    this.setPlayerPosition(new Position(currentRow + 1, currentCol));
                }

                break;
            //RIGHT
            case 3:
                if (currentCol + 1 < totalCols && this.maze.getMaze()[currentRow][currentCol + 1] != 1) {
                    this.setPlayerPosition(new Position(currentRow, currentCol + 1));
                }
                break;
            //LEFT
            case 4:
                if (currentCol - 1 >= 0 && this.maze.getMaze()[currentRow][currentCol - 1] != 1) {
                    this.setPlayerPosition(new Position(currentRow, currentCol - 1));
                }
                break;
            case 5: //UP-Right
                if (isDiagonalAccessible(currentRow, currentCol, totalRows, totalCols, 5)) {
                    this.setPlayerPosition(new Position(currentRow - 1, currentCol + 1));
                }
                break;
            case 6: //UP-LEFT
                if (isDiagonalAccessible(currentRow, currentCol, totalRows, totalCols, 6)) {
                    this.setPlayerPosition(new Position(currentRow - 1, currentCol - 1));
                }
                break;
            case 7: //DOWN-RIGHT
                if (isDiagonalAccessible(currentRow, currentCol, totalRows, totalCols, 7)) {
                    this.setPlayerPosition(new Position(currentRow + 1, currentCol + 1));
                }
                break;

            case 8://Down-left
                if (isDiagonalAccessible(currentRow, currentCol, totalRows, totalCols, 8)) {
                    this.setPlayerPosition(new Position(currentRow + 1, currentCol - 1));
                }
                break;
        }
    }

    private boolean isDiagonalAccessible(int currRow, int currCol, int totalRows, int totalCols, int diagonalNumber) {
        switch (diagonalNumber) {
            case 5: //UP-RIGHT
                if (currRow - 1 >= 0 && currCol + 1 < totalRows && maze.getMaze()[currRow - 1][currCol + 1] != 1) {
                    return true;
                }

                break;
            case 6: //UP-LEFT
                if (currRow - 1 >= 0 && currCol - 1 >= 0 && maze.getMaze()[currRow - 1][currCol - 1] != 1) {
                    return true;
                }
                break;

            case 7: //DOWN-RIGHT
                if (currRow + 1 < totalRows && currCol + 1 < totalCols && maze.getMaze()[currRow + 1][currCol + 1] != 1) {
                    return true;
                }
                break;
            case 8://DOWN-LEFT
                if (currRow + 1 < totalRows && currCol - 1 >= 0 && maze.getMaze()[currRow + 1][currCol - 1] != 1) {
                    return true;
                }
                break;
        }
        return false;
    }

    @Override
    public void updatePlayerPositionMouse(MouseEvent mouseEvent, double mouseX, double mouseY, double cellHeight, double cellWidth) {
        if (this.maze != null) {
            //DOWN
            if (mouseEvent.getY() > mouseY && Math.abs(mouseEvent.getY() - mouseY) >= cellHeight) {
                //DOWN-RIGHT
                if (mouseEvent.getX() > mouseX && Math.abs(mouseEvent.getX() - mouseX) >= cellWidth) {
                    updatePlayerPositionKey(7);
                }
                //DOWN-LEFT
                else if (mouseEvent.getX() < mouseX && Math.abs(mouseEvent.getX() - mouseX) >= cellWidth) {
                    updatePlayerPositionKey(8);
                }
                //DOWN
                else {
                    updatePlayerPositionKey(2);
                }
                return;
            }
            //Checks RIGHT/LEFT
            else {
                if (mouseEvent.getX() > mouseX && Math.abs(mouseEvent.getX() - mouseX) >= cellWidth) {
                    updatePlayerPositionKey(3);
                } else if (mouseEvent.getX() < mouseX && Math.abs(mouseEvent.getX() - mouseX) >= cellWidth) {
                    updatePlayerPositionKey(4);
                }
            }
        }
        //Checks UP
        else if (mouseEvent.getY() < mouseY && Math.abs(mouseEvent.getY() - mouseY) >= cellHeight) {

            //UP-RIGHT
            if (mouseEvent.getX() > mouseX && Math.abs(mouseEvent.getX() - mouseX) >= cellWidth) {
                updatePlayerPositionKey(5);
            }
            //UP-LEFT
            else if (mouseEvent.getX() < mouseX && Math.abs(mouseEvent.getX() - mouseX) >= cellWidth) {
                updatePlayerPositionKey(6);
            }
            //UP
            else {
                updatePlayerPositionKey(1);
            }
        }
    }

    @Override
    public void setPlayerPosition(Position startPosition) {
        this.UserPosition = startPosition;
        if (startPosition.equals(GoalPosition)) {
            this.reachGoal = true;
            setChanged();
            notifyObservers("Finish");
        } else {
            this.reachGoal = false;
            setChanged();
            notifyObservers(startPosition);
        }

    }


    public Solution getSolution() {
        return solution;
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

    public void setMaze(Maze maze) {
        this.maze = maze;
    }

    @Override
    public int[][] getGrid() {
        return maze.getMaze();
    }

    @Override
    public void solveMaze(int row_User, int col_User) {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, (IClientStrategy) (inFromServer, outToServer) -> {
                try {
                    ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                    ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                    toServer.flush();

                    toServer.writeObject(getMaze());
                    toServer.flush();

                    //updates this.solution to be mazeSolution
                    solution = (Solution) fromServer.readObject();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            client.communicateWithServer();
            setChanged();
            notifyObservers();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void saveMaze(File saveFile) {
        FileChooser fileChooser = new FileChooser();

        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try {
                File newFile = new File(file.getAbsolutePath());
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(newFile));
                byte[] byteMaze = this.getMaze().toByteArray();

                out.writeObject(maze);
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                Alert a = new Alert((Alert.AlertType.ERROR));
                a.setContentText("The Server had a problem with the file");
                a.show();
            }
        }

    }

    @Override
    public void loadMaze(Object file) {
        Maze loadedMaze = new Maze((byte[]) file);
        setPlayerPosition(loadedMaze.getStartPosition());
        setMaze(loadedMaze);
    }

    @Override
    public void exit() {
        stopServers();
    }
}
