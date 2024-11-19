package me.blueind.TensorFlow;

import org.bytedeco.tensorflow.TF_Tensor;

import java.nio.FloatBuffer;

import static org.bytedeco.tensorflow.global.tensorflow.TF_TensorData;

public class ResultInterpreter {
    public static String interpret(TF_Tensor outputTensor) {
        // Tensor 데이터 접근
        FloatBuffer buffer = TF_TensorData(outputTensor).asByteBuffer().asFloatBuffer();

        float[] probabilities = new float[2];
        buffer.get(probabilities);

        // 결과 해석
        return probabilities[0] > probabilities[1] ? "CAN" : "PLASTIC";
    }
}
