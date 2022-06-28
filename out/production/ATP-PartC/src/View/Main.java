package View;

//<?import View.MazeDisplayer?>
//<MazeDisplayer fx:id="mazeDisplayer" height="400.0" width="400.0" />

import ViewModel.MyViewModel;
import Model.MyModel;
import Model.IModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javafx.scene.media.Media;

import javax.swing.text.View;
import java.nio.file.Paths;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MyView.fxml"));
        Parent root = fxmlLoader.load();
        //Parent root = FXMLLoader.load(getClass().getResource("MyView.fxml"));
        primaryStage.setTitle("Maze App");
        Image image = new Image("/resources/Background/maze.png");
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
                        "'/resources/Background/cosmic.jpg'" +
                        "); " +
                        "-fx-background-size: cover;"
        );

        primaryStage.setScene(new Scene(root));
        primaryStage.show();


        IModel model = new MyModel();
        MyViewModel viewModel = new MyViewModel(model);
        MyViewController view = fxmlLoader.getController();
        view.setMyViewModel(viewModel);
    }



    public static void main(String[] args) {
        launch(args);
    }
}
