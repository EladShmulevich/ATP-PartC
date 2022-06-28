package View;

//<?import View.MazeDisplayer?>
//<MazeDisplayer fx:id="mazeDisplayer" height="400.0" width="400.0" />

import ViewModel.MyViewModel;
import Model.MyModel;
import Model.IModel;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javafx.scene.media.Media;
import javafx.stage.WindowEvent;

import javax.swing.text.View;
import java.nio.file.Paths;
import java.util.Optional;

public class Main extends Application {

    public static IModel model;
    public  static MyViewModel viewModel;
    public static MyViewController myViewController;
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


        model = new MyModel();
        viewModel = new MyViewModel(model);
        myViewController = fxmlLoader.getController();
        myViewController.setMyViewModel(viewModel);




        //close the program if the user click on the close button
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent windowEvent) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText("Are you sure that you want to exit?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    model.exit();
                } else {
                    windowEvent.consume();
                }
            }
        });
    }







    public static void main(String[] args) {
        launch(args);
    }

}
