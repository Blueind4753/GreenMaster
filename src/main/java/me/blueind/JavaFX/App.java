package me.blueind.JavaFX;

import javafx.application.Application;
import javafx.stage.Stage;
import me.blueind.OpenCV.Camera;
import me.blueind.RaspberryPi.MovementExecutor;
import me.blueind.TensorFlow.Model;
import org.bytedeco.opencv.opencv_core.Mat;

public class App extends Application {
    private Camera camera;
    private Model model;
    private MovementExecutor movementExecutor;
    private StatusUpdater statusUpdater;

    @Override
    public void start(Stage primaryStage) {
        // UI 상태 표시기 초기화
        statusUpdater = new StatusUpdater(primaryStage);

        // 시스템 초기화
        new Thread(() -> {
            try {
                camera = new Camera();
                String modelPath = getClass().getClassLoader().getResource("saved_model").getPath();
                model = new Model(modelPath);
                movementExecutor = new MovementExecutor();

                while (true) {
                    Mat frame = camera.captureImage();
                    if (frame.empty()) {
                        statusUpdater.update("이미지 캡처 실패");
                        continue;
                    }

                    String prediction = model.predict(frame);
                    statusUpdater.update("예측 결과: " + prediction);

                    movementExecutor.execute(prediction);
                    Thread.sleep(2000);
                }
            } catch (Exception e) {
                e.printStackTrace();
                statusUpdater.update("에러 발생: " + e.getMessage());
            } finally {
                cleanup();
            }
        }).start();
    }

    private void cleanup() {
        if (camera != null) {
            camera.closeCamera();
        }
        if (movementExecutor != null) {
            movementExecutor.shutdown();
        }
        statusUpdater.update("시스템 종료");
    }

    @Override
    public void stop() throws Exception {
        cleanup();
        super.stop();
    }
}
