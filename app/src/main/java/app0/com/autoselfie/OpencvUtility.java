package app0.com.autoselfie;

import android.util.Log;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

public class OpencvUtility {

    int mAbsoluteFaceSize = 0, mRelativeFaceSize = 100;
    CascadeClassifier cascadeClassifier;

    public OpencvUtility(CascadeClassifier cascadeClassifier) {
        this.cascadeClassifier = cascadeClassifier;
    }

    public Mat getMatForFace(Mat mRgba, Mat croppedImage1, Rect roi) {
        Mat croppedImage = croppedImage1;

        Imgproc.rectangle(mRgba, roi.tl(), roi.br(), new Scalar(0, 255, 0), 2);


        return croppedImage.clone();
    }


    public Rect[] getRectsForDetectedFaces(Mat mGray) {
        int height = mGray.rows();

        if (mAbsoluteFaceSize == 0) {


            if (Math.round(height * 0.2f) > 0) {
                mAbsoluteFaceSize = Math.round(height * 0.01f);
            }
        }

        MatOfRect detectedFaces = new MatOfRect();


        Imgproc.equalizeHist(mGray, mGray);


        if (cascadeClassifier != null) {


            cascadeClassifier.detectMultiScale(mGray, detectedFaces, 1.1,
                    2, 2, new Size(mAbsoluteFaceSize, mAbsoluteFaceSize), new Size(height, height));

//            cascadeClassifier.detectMultiScale(mGray, detectedFaces);


        }


        Rect[] facesArray = detectedFaces.toArray();
        return facesArray;
    }
}
