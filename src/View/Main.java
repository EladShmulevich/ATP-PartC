package View;

//<?import View.MazeDisplayer?>
//<MazeDisplayer fx:id="mazeDisplayer" height="400.0" width="400.0" />

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javafx.scene.media.Media;

import java.io.File;
import java.nio.file.Paths;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("MyView.fxml"));
        primaryStage.setTitle("Maze App");
        Image image = new Image("/resources/Backround/maze.png");
        primaryStage.getIcons().add(image);

        /** set the app size to the screen size**/
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());

//        StackPane root1 = new StackPane();
        root.setStyle(
                "-fx-background-image: url(" +
                        "'/resources/Backround/cosmic.jpg'" +
                        "); " +
                        "-fx-background-size: cover;"
        );

        primaryStage.setScene(new Scene(root));
        primaryStage.show();


//        music();



    }
    MediaPlayer mediaPlayer;
    public void music(){
        String s = "Star_Wars_Main_Theme_Song.mp3";
        Media m = new Media(Paths.get(s).toUri().toString());

        mediaPlayer = new MediaPlayer(m);
        mediaPlayer.play();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
