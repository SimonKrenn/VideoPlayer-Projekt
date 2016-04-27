/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package videoplayer;

import java.awt.Desktop;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Krenn Simon, Hagn Sven, Audil Rowa
 */
public class Videoplayer extends Application implements EventHandler<ActionEvent> {

    MediaPlayer player;
    String video;

    Button play = new Button("Play");
    Button pause = new Button("Pause");
    Button repeat = new Button("Repeat");
    Button playlist = new Button("PlayList");
    Slider volumeSlider;
    Desktop desktop = Desktop.getDesktop();
    //inside that is the mediaplayer
    VBox videos = new VBox();
    //inside that are the buttons and the sliders
    VBox uiElements = new VBox();
    javafx.geometry.Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
    final FileChooser fileChooser = new FileChooser();
    Stage mainStage;
    Media media;
    MediaView view;

    @Override

    public void start(final Stage stage) throws Exception {
        mainStage = stage;
        StackPane viewing = new StackPane();
        final Timeline slideIn = new Timeline();
        final Timeline slideOut = new Timeline();
        final Slider slider = new Slider();
        video = "C:/VIDEOS/CODaw.mp4";
        media = new Media("file:///" + video);
        player = new MediaPlayer(media);
        view = new MediaView(player);
        
        play.setOnAction(this);
        pause.setOnAction(this);
        repeat.setOnAction(this);
        playlist.setOnAction(this);

        viewing.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                slideOut.play();
            }
        });
        viewing.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                slideIn.play();
            }
        });

        volumeSlider = new Slider();
        volumeSlider.setValue(100);
        volumeSlider.setPrefWidth(70);
        volumeSlider.setMinWidth(30);
        volumeSlider.valueProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable ov) {
                if (volumeSlider.isValueChanging()) {
                    player.setVolume(volumeSlider.getValue() / 100.0);
                }
            }
        });

        HBox buttons = new HBox();
        HBox buttAndVol = new HBox();
        VBox sliderAndB = new VBox();
        VBox centeralignment = new VBox();
        centeralignment.setMinWidth(primaryScreenBounds.getWidth() / 2 - 250);
        System.out.println(media.getWidth());

        buttons.getChildren().addAll(pause, play, repeat, playlist);
        buttAndVol.getChildren().addAll(centeralignment, buttons, volumeSlider);
        sliderAndB.getChildren().addAll(slider, buttAndVol);

        videos.getChildren().addAll(view);
        uiElements.getChildren().addAll(sliderAndB);
        viewing.getChildren().addAll(videos, uiElements);

        Scene scene = new Scene(viewing, media.getWidth(), media.getHeight(), Color.BLACK);
        scene.getStylesheets().add("css/main.css");
        mainStage.setScene(scene);
        mainStage.show();

        player.setOnReady(new Runnable() {
            @Override
            public void run() {
                int w = player.getMedia().getWidth();
                int h = player.getMedia().getHeight();

                stage.setMinWidth(w);
                stage.setMinHeight(h);

                uiElements.setMinSize(w, 100);
                uiElements.setTranslateY(h - 100);

                slider.setMin(0.0);
                slider.setValue(0.0);
                slider.setMax(player.getTotalDuration().toSeconds());

                slideOut.getKeyFrames().addAll(
                        new KeyFrame(new Duration(0),
                                new KeyValue(uiElements.translateYProperty(), h - 100),
                                new KeyValue(uiElements.opacityProperty(), 0.9)
                        ),
                        new KeyFrame(new Duration(300),
                                new KeyValue(uiElements.translateYProperty(), h),
                                new KeyValue(uiElements.opacityProperty(), 0.0)
                        )
                );
                slideIn.getKeyFrames().addAll(
                        new KeyFrame(new Duration(0),
                                new KeyValue(uiElements.translateYProperty(), h),
                                new KeyValue(uiElements.opacityProperty(), 0.0)
                        ),
                        new KeyFrame(new Duration(300),
                                new KeyValue(uiElements.translateYProperty(), h - 100),
                                new KeyValue(uiElements.opacityProperty(), 0.9)
                        )
                );
            }
        });
        player.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observableValue, Duration duration, Duration current) {
                slider.setValue(current.toSeconds());
            }
        });
        slider.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                player.seek(Duration.seconds(slider.getValue()));
            }
        });
    }

    void updateValues() {
        if (volumeSlider != null) {
            Platform.runLater(new Runnable() {
                public void run() {
                    if (!volumeSlider.isValueChanging()) {
                        volumeSlider.setValue((int) Math.round(player.getVolume()
                                * 100));
                    }
                }
            });
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void handle(ActionEvent event) {
        if (event.getSource() == play) {
            player.play();
        } else if (event.getSource() == pause) {
            player.pause();
        } else if (event.getSource() == repeat) {
            player.stop();
            player.play();
        } else if (event.getSource() == playlist) {
            openPlaylistFolder();
            
        }
    }

    private void openPlaylistFolder() {
        configureFileChooser(fileChooser);
        List<File> list
                = fileChooser.showOpenMultipleDialog(mainStage);
        if (list != null) {
            for (File file : list) {
                openFile(file);
            }
        }
    }

    private static void configureFileChooser(final FileChooser fileChooser) {
        fileChooser.setTitle("View Pictures");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
    }

    private void openFile(File file) {
        video = file.toString();
        char[] swappingString = video.toCharArray();
        for (int i = 0; i < swappingString.length; i++) {
            if (swappingString[i] == '\\') {
                swappingString[i] = '/';
            }
        }
        video = new String(swappingString);
        System.out.println(video);
        player.stop();
        media = new Media("file:///" + video);
        player = new MediaPlayer(media);
         view.setMediaPlayer(player);
    }
}
