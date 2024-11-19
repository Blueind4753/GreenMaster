package me.blueind.TensorFlow;

import org.bytedeco.tensorflow.*;
import static org.bytedeco.tensorflow.global.tensorflow.*;

public class TensorProcessor {
    public static TF_Tensor runInference(TF_Session session, TF_Graph graph, TF_Tensor inputTensor) {
        // 입력 및 출력 초기화
        TF_Output inputOp = new TF_Output();
        inputOp.oper(TF_GraphOperationByName(graph, "serving_default_input_tensor"));
        inputOp.index(0);

        TF_Output outputOp = new TF_Output();
        outputOp.oper(TF_GraphOperationByName(graph, "serving_default_output_tensor"));
        outputOp.index(0);

        // 출력 텐서 초기화
        TF_Tensor outputTensor = new TF_Tensor();

        // 세션 실행
        TF_SessionRun(
                session,
                null, // run options
                inputOp, // 입력 이름
                inputTensor, // 입력 텐서
                1, // 입력 개수
                outputOp, // 출력 이름
                outputTensor, // 출력 텐서
                1, // 출력 개수
                null, // target operations
                0, // target 개수
                null, // run metadata
                TF_NewStatus()
        );

        return outputTensor;
    }
}
