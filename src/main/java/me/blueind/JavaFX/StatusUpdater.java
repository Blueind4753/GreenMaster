package me.blueind.JavaFX;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class StatusUpdater {
    private final Text statusText;

    public StatusUpdater(Stage primaryStage) {
        statusText = new Text("시스템 초기화 중...");
        StackPane root = new StackPane(statusText);
        Scene scene = new Scene(root, 800, 600);

        primaryStage.setTitle("TensorFlow Motor Control");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void update(String message) {
        Platform.runLater(() -> statusText.setText(message));
    }
}
