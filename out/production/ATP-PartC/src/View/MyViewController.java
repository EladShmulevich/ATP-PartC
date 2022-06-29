package View;

import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;


public class MyViewController implements Initializable, IView, Observer {
    private MyViewModel myViewModel;
    private Maze maze;
    private boolean dragging = false;
    private double MouseX;
    private double MouseY;




    @FXML
    public TextField textField_mazeRows;
    @FXML
    public TextField textField_mazeColumns;
    @FXML
    public MazeDisplayer mazeDisplayer;
    @FXML
    public Label playerRow;
    @FXML
    public Label playerCol;
    @FXML
    public Pane BoardPane;

    private MediaPlayer mediaPlayer; //Media player


    StringProperty updatePlayerRow = new SimpleStringProperty();
    StringProperty updatePlayerCol = new SimpleStringProperty();


    public String getUpdatePlayerRow() {
        return updatePlayerRow.get();
    }

    public String getUpdatePlayerCol() {
        return updatePlayerCol.get();
    }

    public void setUpdatePlayerRow(int updatePlayerRow) {
        this.updatePlayerRow.set(updatePlayerRow + "");
    }

    public void setUpdatePlayerCol(int updatePlayerCol) {
        this.updatePlayerCol.set(updatePlayerCol + "");
    }


    public void setMyViewModel(MyViewModel myViewModel) {
        this.myViewModel = myViewModel;
//        this.myViewModel.addObserver(this);
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playerRow.textProperty().bind(updatePlayerRow);
        playerCol.textProperty().bind(updatePlayerCol);
    }

    public void generateMaze(ActionEvent actionEvent) {

        if (!textField_mazeRows.getText().matches("\\d*")) {
            textField_mazeRows.setText("10");
            popAlert("Error", "Numbers Only!");
        }

        if (!textField_mazeColumns.getText().matches("\\d*")) {
            textField_mazeColumns.setText("10");
            popAlert("Error", "Numbers Only!");
        }

        int rows = Integer.parseInt(textField_mazeRows.getText());
        int cols = Integer.parseInt(textField_mazeColumns.getText());

        myViewModel.generateMaze(rows, cols);

        mazeDisplayer.setRoundFirst(true);
        mazeDisplayer.requestFocus();
        mazeDisplayer.widthProperty().bind(BoardPane.widthProperty());
        mazeDisplayer.heightProperty().bind(BoardPane.heightProperty());

//        mazeDisplayer.drawMaze(myViewModel.getMaze());

        mazeDisplayer.drawMaze(myViewModel.getmaze());
        //mazeDisplayer.drawMaze();
//        mazeGenerated();

        //logger.info("Generate Maze button pressed");

    }




    public void solveMaze(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Solving maze...");
        alert.show();
        myViewModel.solveMaze(mazeDisplayer.getPlayerRow(), mazeDisplayer.getPlayerCol());
//        mazeDisplayer.ShowSolution(myViewModel.getSolution());
    }

    public void openFile(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Open maze");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Maze files (*.maze)", "*.maze"));
        fc.setInitialDirectory(new File("./resources"));
        File chosen = fc.showOpenDialog(null);
        //...
    }

    public void keyPressed(KeyEvent keyEvent) {
        myViewModel.movePlayer(keyEvent);
        keyEvent.consume();
    }

    public void setPlayerPosition(int row, int col) {
        mazeDisplayer.setPlayerPosition(row, col);
        setUpdatePlayerRow(row);
        setUpdatePlayerCol(col);
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }


    @Override
    public void update(Observable o, Object arg) {
        if ("generateMaze".equals(arg)) {
            int StartRow = myViewModel.getStartRow();
            int StartCol = myViewModel.getStartCol();
            try {
                setUpdatePlayerRow(StartRow);
                setUpdatePlayerCol(StartCol);
                mazeDisplayer.drawMaze(myViewModel.getmaze());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            //if we reached goal
            if (this.mazeDisplayer.getMaze() == myViewModel.getMaze()) {
                if (mazeDisplayer.getPlayerRow() == myViewModel.getPlayerRow() && mazeDisplayer.getPlayerCol() == myViewModel.getPlayerCol()) {
                    myViewModel.getSolution();
                }
                else {
                    setUpdatePlayerRow(myViewModel.getPlayerRow());
                    setUpdatePlayerCol(myViewModel.getPlayerCol());
                    this.mazeDisplayer.drawFinished(myViewModel.isSolved());
                    this.mazeDisplayer.setPlayerPosition(myViewModel.getPlayerRow(), myViewModel.getPlayerCol());

                    if (myViewModel.isSolved()) {
                        this.mazeDisplayer.setPlayerPosition(myViewModel.getPlayerRow(), myViewModel.getPlayerCol());
//                this.mazeDisplayer.drawFinishedMaze(bool);
                        this.mediaPlayer = new MediaPlayer(new Media(new File("./resources/music/applause-2.mp3").toURI().toString()));
                        this.mediaPlayer.play();
                    }

                }
            }
            else {
                this.mazeDisplayer.setMaze(myViewModel.getMaze());
                try{
                    setUpdatePlayerCol(myViewModel.getPlayerCol());
                    setUpdatePlayerRow(myViewModel.getPlayerRow());
                    mazeDisplayer.drawMaze(myViewModel.getmaze());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

        }

    }


    private void mazeSolved() {
        mazeDisplayer.setSolution(myViewModel.getSolution());
    }

    private void playerMoved() {
        setPlayerPosition(myViewModel.getPlayerRow(), myViewModel.getPlayerCol());
    }

    private void mazeGenerated() {
        mazeDisplayer.drawMaze(myViewModel.getmaze());
    }


    public MyViewModel getMyViewModel() {
        return myViewModel;
    }


    public void popAlert(String title, String message) {

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(300);
        window.setMinHeight(300);

        Label label = new Label();
        label.setText(message);
        Button closeButton = new Button("Close this window");
        closeButton.setOnAction(e -> window.close());

        VBox layout = new VBox(20);
        layout.getChildren().addAll(label, closeButton);
        layout.setAlignment(Pos.CENTER);

        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }


    public void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.show();
    }


    public void play(ActionEvent actionEvent) {
        Main.startMusic.setMute(false);
    }

    public void mute(ActionEvent actionEvent) {
        Main.startMusic.setMute(true);
    }

    public void exit() {
        myViewModel.Exit();
        System.exit(0);
    }

    public void mouseDragged(MouseEvent mouseEvent) {
        double cell_height = this.mazeDisplayer.getCellHeight();
        double cell_width = this.mazeDisplayer.getCellWidth();
        boolean stopDrag = (Math.abs(MouseX-mouseEvent.getX()) >= cell_width || Math.abs(MouseY-mouseEvent.getY()) >= cell_height);

        if(dragging && stopDrag){
            this.myViewModel.movePlayer(mouseEvent, MouseX, MouseY, mazeDisplayer.getCellHeight(), mazeDisplayer.getCellWidth());
        }

    }

    public void DragDetected(MouseEvent mouseEvent) {
        this.MouseX = mouseEvent.getX();
        this.MouseY = mouseEvent.getY();
        dragging = true;
    }


    public void MousePressed(MouseEvent mouseEvent) {
        MouseX = mouseEvent.getX();
        MouseY = mouseEvent.getY();

    }

    public void MouseReleased(MouseEvent mouseEvent) {
        dragging = false;
        MouseY = mouseEvent.getY();
        MouseX = mouseEvent.getX();
        mouseEvent.consume();
    }
}
