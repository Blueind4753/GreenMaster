package me.blueind.OpenCV;

import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.javacpp.FloatPointer;
import org.bytedeco.tensorflow.TF_Tensor;
import static org.bytedeco.tensorflow.global.tensorflow.*;

public class ImageProcessor {
    public static TF_Tensor preprocess(Mat image, int height, int width) {
        // OpenCV 크기 조정
        Mat resizedImage = new Mat();
        opencv_imgproc.resize(image, resizedImage, new org.bytedeco.opencv.opencv_core.Size(width, height));

        // BytePointer에서 데이터를 읽어와 float 배열로 변환
        byte[] byteArray = new byte[height * width * 3]; // 3 채널 (RGB)
        resizedImage.data().get(byteArray);

        FloatPointer floatPointer = new FloatPointer(height * width * 3);
        for (int i = 0; i < byteArray.length; i++) {
            floatPointer.put(i, (byteArray[i] & 0xFF) / 255.0f); // 정규화
        }

        // TensorFlow 텐서 생성
        return TF_NewTensor(
                TF_FLOAT,
                new long[]{1, height, width, 3}, // 텐서 크기
                Long.BYTES,
                floatPointer,
                floatPointer.capacity() * Float.BYTES,
                null, // deallocator
                null  // deallocator argument
        );
    }
}
