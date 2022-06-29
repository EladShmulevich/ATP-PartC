package View;

import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.*;


public class MyViewController implements Initializable, IView, Observer {
    private MyViewModel myViewModel;
    private Maze maze;
    private boolean dragging = false;
    private double MouseX;
    private double MouseY;
    Properties properties;




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

        try{
            properties = new Properties();
            properties.load(new FileInputStream("./resources/config.properties"));
        }
        catch(Exception e){
            System.out.println("Properties file not found");
        }
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


        mazeDisplayer.drawMaze(myViewModel.getmaze());

        Main.startMusic.setAutoPlay(true);


    }


    public Properties getProperties(){
        return properties;
    }



    public void solveMaze(ActionEvent actionEvent) {
        if(myViewModel.getmaze() == null)
            {
            popAlert("Error", "No maze to solve!");}
        else{
            popAlert("Solve", "Solving maze...");

//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setContentText("Solving maze...");
//            alert.show();
            myViewModel.solveMaze(mazeDisplayer.getPlayerRow(), mazeDisplayer.getPlayerCol());
            mazeDisplayer.setSolution(myViewModel.getSolution());
        }


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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Are you sure that you want to exit?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            myViewModel.Exit();
            System.exit(0);
        }
    }

    public void mouseDragged(MouseEvent mouseEvent) {
        int rows = myViewModel.getmaze().getMaze().length;
        int cols = myViewModel.getmaze().getMaze()[0].length;
        if(myViewModel.getmaze() != null) {
            int Size = Math.max(rows,cols);
            MouseX = mouseDragHandler(Size, mazeDisplayer.getHeight(), rows, mouseEvent.getX(), mazeDisplayer.getWidth() /Size);
            MouseY = mouseDragHandler(Size, mazeDisplayer.getWidth(), cols, mouseEvent.getY(), mazeDisplayer.getHeight() /Size);
            myViewModel.movePlayer(MouseX, MouseY);
        }
    }

    private  double mouseDragHandler(int PaneSize, double canvasSize, int mazeSize,double mouseEvent,double temp){
        double cellSize=canvasSize/PaneSize;
        double start = (canvasSize / 2 - (cellSize * mazeSize / 2)) / cellSize ;
        return (int) (((mouseEvent) - start ) / temp);
    }

    public void mouseScroll(ScrollEvent scrollEvent) {
        if(scrollEvent.isControlDown()){
            double zoomDelta = 1.25;
            if(scrollEvent.getDeltaY()<=0){
                zoomDelta = 1/zoomDelta;
            }

            Scale scale = new Scale();
            scale.setX(BoardPane.getScaleX() * zoomDelta);
            scale.setY(BoardPane.getScaleY() * zoomDelta);
            scale.setPivotX(BoardPane.getScaleX());
            scale.setPivotY(BoardPane.getScaleY());
            BoardPane.getTransforms().add(scale);


        }
    }

    public void enterPropWindow(ActionEvent actionEvent) {
        Parent root;
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("View/FxmlProperties.fxml"));
            Scene scene = BoardPane.getScene();
            root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Properties");
            stage.setScene(new Scene(root, 390, 220));
            Image image = new Image("resources/Background/settings.png");
            stage.getIcons().add(image);


            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();

            root.setStyle(
                    "-fx-background-image: url(" +
                            "'/resources/Background/cosmic.jpg'" +
                            "); " +
                            "-fx-background-size: cover;"
            );



            stage.show();
            PropertiesController optionsController = fxmlLoader.getController();
            optionsController.setMyViewController(this);
            optionsController.setOptionStage(stage);
            optionsController.setMazeChoiceBox();
            optionsController.setSolveChoiceBox();
        }catch (IOException e){
            e.printStackTrace();
        }


    }

    public void saveMaze(ActionEvent actionEvent) {
        
    }

    public void HelpClickHandler(ActionEvent actionEvent) {
        Parent root;
        try {
            FXMLLoader fxmlLoader =new FXMLLoader(getClass().getClassLoader().getResource("View/Help.fxml"));
            Scene myScene= BoardPane.getScene();
            root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Help");
            stage.setScene(new Scene(root));

            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();

            root.setStyle(
                    "-fx-background-image: url(" +
                            "'resources/Background/helpScreen.png'" +
                            "); " +
                            "-fx-background-size: cover;"
            );



            stage.show();
//            PropertiesController helpController = fxmlLoader.getController();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void aboutClickHandler(ActionEvent actionEvent) {
        Parent root;
        try {
            FXMLLoader fxmlLoader =new FXMLLoader(getClass().getClassLoader().getResource("View/Help.fxml"));
            root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("About");
            stage.setScene(new Scene(root));


            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();

            root.setStyle(
                    "-fx-background-image: url(" +
                            "'resources/Background/helpScreen.png'" +
                            "); " +
                            "-fx-background-size: cover;"
            );



            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
}
