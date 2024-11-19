package me.blueind.JavaFX;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.MatOfByte;

import java.io.ByteArrayInputStream;

public class CameraViewer {
    private final ImageView imageView;

    public CameraViewer() {
        this.imageView = new ImageView();
        this.imageView.setFitWidth(800);
        this.imageView.setPreserveRatio(true);
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void updateView(Mat frame) {
        Image image = matToImage(frame);
        Platform.runLater(() -> imageView.setImage(image));
    }

    private Image matToImage(Mat frame) {
        MatOfByte buffer = new MatOfByte();
        Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2RGB);
        Imgcodecs.imencode(".png", frame, buffer);
        return new Image(new ByteArrayInputStream(buffer.toArray()));
    }
}
