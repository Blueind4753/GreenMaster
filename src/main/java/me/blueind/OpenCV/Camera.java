package me.blueind.OpenCV;

import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;

public class Camera {
    private final VideoCapture videoCapture;

    public Camera() {
        videoCapture = new VideoCapture(0); // 기본 카메라 열기
    }

    public Mat captureImage() {
        Mat frame = new Mat();
        if (videoCapture.grab()) {
            videoCapture.retrieve(frame);
        }
        return frame;
    }

    public void closeCamera() {
        if (videoCapture.isOpened()) {
            videoCapture.release();
        }
    }
}
