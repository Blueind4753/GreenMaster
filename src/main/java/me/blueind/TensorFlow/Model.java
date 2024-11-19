package me.blueind.TensorFlow;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.tensorflow.*;
import static org.bytedeco.tensorflow.global.tensorflow.*;

import org.bytedeco.opencv.opencv_core.Mat;
import me.blueind.OpenCV.ImageProcessor;

public class Model {
    private final TF_Session session;
    private final TF_Graph graph;
    private final TF_Status status;

    /**
     * TensorFlow 모델 초기화
     *
     * @param modelPath TensorFlow SavedModel 디렉토리 경로
     */
    public Model(String modelPath) {
        // TensorFlow 상태 객체 생성
        this.status = TF_NewStatus();
        this.graph = TF_NewGraph();

        // 세션 옵션 설정
        TF_SessionOptions sessionOptions = TF_NewSessionOptions();

        // 경로를 BytePointer로 변환 (Windows 경로 수정 포함)
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            modelPath = modelPath.replaceFirst("^/(.:/)", "$1");
        }
        BytePointer modelPathPointer = new BytePointer(modelPath);

        // SavedModel 로드
        this.session = TF_LoadSessionFromSavedModel(
                sessionOptions,
                null, // Run options
                modelPathPointer,
                new BytePointer("serve"), // 태그 이름
                1, // 태그 개수
                this.graph,
                null, // 메타 그래프 정보
                this.status
        );

        checkStatus("Failed to load the model");
    }

    /**
     * TensorFlow 모델 추론 수행
     *
     * @param image OpenCV Mat 이미지 (입력 데이터)
     * @return 예측된 클래스 이름 (e.g., "CAN" or "PLASTIC")
     */
    public String predict(Mat image) {
        // 이미지 전처리
        TF_Tensor inputTensor = ImageProcessor.preprocess(image, 224, 224);

        // 추론 수행
        TF_Tensor outputTensor = TensorProcessor.runInference(session, graph, inputTensor);

        // 결과 해석
        String result = ResultInterpreter.interpret(outputTensor);

        // 리소스 정리
        TF_DeleteTensor(inputTensor);
        TF_DeleteTensor(outputTensor);

        return result;
    }

    /**
     * TensorFlow 리소스 정리
     */
    public void close() {
        TF_DeleteSession(session, status);
        TF_DeleteGraph(graph);
        TF_DeleteStatus(status);
    }

    /**
     * TensorFlow 상태 확인
     *
     * @param errorMessage 오류 메시지
     */
    private void checkStatus(String errorMessage) {
        if (TF_GetCode(status) != TF_OK) {
            throw new RuntimeException(errorMessage + ": " + TF_Message(status).getString());
        }
    }
}
