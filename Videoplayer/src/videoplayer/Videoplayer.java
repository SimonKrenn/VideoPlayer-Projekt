/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package videoplayer;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

/**
 *
 * @author schmizzle
 */
public class Videoplayer extends Application {

    @Override
    public void start(Stage primaryStage) {
        String video = "file:///Schule/vids/Sequenz.mp4";
        VBox toolbar = new VBox(1000);
        toolbar.setStyle("-fx-background-color: #444;");
        toolbar.setMinHeight(100);

        //Via Methode URL uebergeben
        Media media = new Media(video);
        MediaPlayer player = new MediaPlayer(media);
        MediaView view = new MediaView(player);

        
        view.setFitHeight(500.0);
        BorderPane root = new BorderPane();

        root.setCenter(view);
        root.setBottom(toolbar);
        player.play();
        Scene scene = new Scene(root, 1000, 700);

        primaryStage.setTitle("Player");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
