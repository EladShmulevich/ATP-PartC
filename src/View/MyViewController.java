package View;

import ViewModel.MyViewModel;
import algorithms.mazeGenerators.MyMazeGenerator;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import  java.util.ResourceBundle;


public class MyViewController implements Initializable, IView {
    //public MyMazeGenerator generator;
    public TextField textField_mazeRows;
    public TextField textField_mazeColumns;
    public MazeDisplayer mazeDisplayer;
    private MyViewModel myViewModel;

//    private boolean changedSettings = false;


    public void generateMaze(ActionEvent actionEvent) {

        if (!textField_mazeRows.getText().matches("\\d*")) {
            textField_mazeRows.setText("10");
            popAlert("Error", "Numbers Only!");
        }
            int rows = Integer.parseInt(textField_mazeRows.getText());

        if(!textField_mazeColumns.getText().matches("\\d*")) {
            textField_mazeColumns.setText("10");
            popAlert("Error", "Numbers Only!");
        }
            int cols = Integer.parseInt(textField_mazeColumns.getText());
            myViewModel.generateMaze(rows,cols);


            //int[][] maze = generator.generateRandomMaze(rows, cols);

            //mazeDisplayer.drawMaze(maze);

        }


        public void solveMaze (ActionEvent actionEvent){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Solving maze...");
            alert.show();
        }

    public void popAlert (String title, String message ){

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

    public void mouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
