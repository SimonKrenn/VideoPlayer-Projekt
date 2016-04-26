/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package videoplayer;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
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
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Krenn Simon, Hagn Sven, Audil Rowa
 */
public class Videoplayer extends Application implements EventHandler<ActionEvent> {
    Group root = new Group();
    MediaPlayer player;
    Button play = new Button("Play");
    Button pause = new Button("Pause");
    Button repeat = new Button("Reapet");
    private Slider volumeSlider;

    @Override

    public void start(final Stage stage) throws Exception {
        final Timeline slideIn = new Timeline();
        final Timeline slideOut = new Timeline();
        final VBox sliderBox = new VBox();
        final VBox slideAndViewBox = new VBox();
        final Slider slider = new Slider();
        
        Media media = new Media("file:///C:/Videos/GTA5.mp4");
        player = new MediaPlayer(media);
        MediaView view = new MediaView(player);
        
        BorderPane uiPlacer = new BorderPane();
        stage.setTitle("Snitch-Player");
        Group root = new Group();

        play.setOnAction(this);
        pause.setOnAction(this);
        repeat.setOnAction(this);
        
        view.setFitHeight(300.0);

        root.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                slideOut.play();
            }
        });
        root.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                slideIn.play();
            }
        });
        
        slideAndViewBox.setMaxWidth(view.getFitHeight());
        
        sliderBox.getChildren().addAll(slider);
        slideAndViewBox.getChildren().addAll(view, sliderBox);

        volumeSlider = new Slider();
        volumeSlider.setPrefWidth(70);
        //volumeSlider.setMaxWidth(Region.USE_PREF_SIZE);
        volumeSlider.setMinWidth(30);

        volumeSlider.valueProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable ov) {
                if (volumeSlider.isValueChanging()) {
                    player.setVolume(volumeSlider.getValue() / 100.0);
                }
            }
        });
        VBox buttons = new VBox();

        root.getChildren().add(view);
        root.getChildren().add(sliderBox);

        uiPlacer.setCenter(root);
        buttons.getChildren().addAll(play, pause, repeat, volumeSlider);
        uiPlacer.setBottom(buttons);

        Scene scene = new Scene(uiPlacer, 400, 400, Color.BLACK);
        stage.setScene(scene);
        stage.show();

        player.setOnReady(new Runnable() {
            @Override
            public void run() {
                int w = player.getMedia().getWidth();
                int h = player.getMedia().getHeight();

                stage.setMinWidth(w);
                stage.setMinHeight(h);

                sliderBox.setMinSize(w, 100);
                sliderBox.setTranslateY(h - 100);

                slider.setMin(0.0);
                slider.setValue(0.0);
                slider.setMax(player.getTotalDuration().toSeconds());

                slideOut.getKeyFrames().addAll(
                        new KeyFrame(new Duration(0),
                                new KeyValue(sliderBox.translateYProperty(), h - 100),
                                new KeyValue(sliderBox.opacityProperty(), 0.9)
                        ),
                        new KeyFrame(new Duration(300),
                                new KeyValue(sliderBox.translateYProperty(), h),
                                new KeyValue(sliderBox.opacityProperty(), 0.0)
                        )
                );
                slideIn.getKeyFrames().addAll(
                        new KeyFrame(new Duration(0),
                                new KeyValue(sliderBox.translateYProperty(), h),
                                new KeyValue(sliderBox.opacityProperty(), 0.0)
                        ),
                        new KeyFrame(new Duration(300),
                                new KeyValue(sliderBox.translateYProperty(), h - 100),
                                new KeyValue(sliderBox.opacityProperty(), 0.9)
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
        }
    }
}
