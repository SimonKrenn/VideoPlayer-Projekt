/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package videoplayer;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

/**
 *
 * @author schmizzle
 */
public class EngineTest {
    final Media media = new Media("file:///Users/schmizzle/Desktop/SlowMo.mp4");
    final MediaPlayer player = new MediaPlayer(media);
    final MediaView view = new MediaView(player);
    Button play = new Button("Play");
    Button stop = new Button("Stop");
    Button pause = new Button("Pause");
    Button test = new Button("Test");
    
    

    @Override
    public void start(Stage primaryStage) {
        play.setOnAction(this);
        stop.setOnAction(this);
        pause.setOnAction(this);
        
        
        
        
        HBox bottom = new HBox();
        
        BorderPane root = new BorderPane();
        root.setBottom(bottom);
        bottom.getChildren().addAll(play,pause,stop);
        root.getChildren().add(view);
        
        
        Scene scene = new Scene(root, 1920, 1080);
        view.setFitHeight(576);
        
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        scene.getStylesheets().add("styles.css");
        primaryStage.show();
 
}
    }
