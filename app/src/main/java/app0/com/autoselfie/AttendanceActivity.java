package app0.com.autoselfie;

//import android.app.Activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Rect;
import org.opencv.face.EigenFaceRecognizer;
import org.opencv.face.FisherFaceRecognizer;
import org.opencv.face.FaceRecognizer;
import org.opencv.face.FisherFaceRecognizer;
import org.opencv.face.LBPHFaceRecognizer;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class AttendanceActivity extends AppCompatActivity implements CvCameraViewListener2 {

    private static final String TAG = "Attendance";
    private File cascadeFile;
    private Mat mRgba, mGray, mRgbaT;
    private CamView mOpenCvCameraView;
    private DbHelper dbHelper;
    private boolean shouldFrameBeCaptured = false;
    private boolean isPerformingRecognition = false;
    FaceRecognizer recognizer;
    OpencvUtility opencvUtility;
    private int acceptableConfidenceLevel = 70;


    private TextView comment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        comment = findViewById(R.id.comment);
        dbHelper = new DbHelper(getApplicationContext());
        Toast.makeText(getApplicationContext(), "Look into the Camera screen for about a minnute \n You will be Prompted when to stop.", Toast.LENGTH_SHORT).show();


        mOpenCvCameraView = (CamView) findViewById(R.id.auto_selfie_activity_surface_view);
        mOpenCvCameraView.setCameraIndex(1);
        mOpenCvCameraView.setMaxFrameSize(600, 600);
        mOpenCvCameraView.setCvCameraViewListener(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                shouldFrameBeCaptured = true;
                Log.d(TAG, "Frame is currently being captured");
                Toast.makeText(getApplicationContext(), "Your image is being captured", Toast.LENGTH_SHORT).show();

            }
        }, 5000);

    }

    @Override
    public void onResume() {
        super.onResume();

        if (OpenCVLoader.initDebug()) {
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        } else {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, mLoaderCallback);
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        Log.d(TAG, "Started Camera View");
        mGray = new Mat();
        mRgba = new Mat();

    }


    public void onCameraViewStopped() {
        Log.d(TAG, "Stopped Camera View");
        mGray.release();
        mRgba.release();

    }


    //    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {


        if (mGray != null) {
            mGray.release();
        }

        if (mRgba != null) {
            mRgba.release();
        }

        if (mRgbaT != null) {
            mRgbaT.release();
        }


        mRgba = inputFrame.rgba();
        mGray = inputFrame.gray();

        if (shouldFrameBeCaptured) {
            captureFrames(mRgba, mGray);

        }


        return mRgba;
    }


    private void captureFrames(Mat mRgba, Mat mGray) {

        try {

            if (mRgba == null)
                return;

            Log.d(TAG, "checking on camera frame handler: ");

            Rect[] facesArray = opencvUtility.getRectsForDetectedFaces(mGray);

            if (facesArray.length == 1 && !isPerformingRecognition) {

                performRecognition(mRgba, mGray, facesArray);

            }


        } catch (Exception ex) {



            Log.e(TAG, ex.getMessage());

        }

    }

    private void performRecognition(Mat mRgba, Mat mGray, Rect[] facesArray) {
        isPerformingRecognition = true;


        Log.d(TAG, "Number of faces detected " + facesArray.length);

//            shouldFrameBeCaptured = false;
        Log.d(TAG, "getting mat");

        Mat c = opencvUtility.getMatForFace(mRgba, new Mat(mGray, facesArray[0]), facesArray[0]);

        ArrayList<UserModel> users = dbHelper.onGetUsersImages();

        Log.d(TAG, "Gotten users 2: " + users.size());
        Log.d(TAG, "Gotten users 2 images for first user: " + users.get(0).getImages().size());


        Log.d(TAG, "next");

        ArrayList<Mat> trainingImageData = new ArrayList<>();
        ArrayList<Integer> trainingImageLabel = new ArrayList<>();

        Log.d(TAG, "Traning Data");
        users.forEach(user -> {
            user.getImages().forEach(image -> {


                File file = new File(image);
                Mat imageMat = Imgcodecs.imread(file.toString());

                Imgproc.cvtColor(imageMat, imageMat, Imgproc.COLOR_BGR2GRAY);
                trainingImageData.add(imageMat);
                trainingImageLabel.add(user.getId());

                Log.d(TAG, "image height: " + imageMat.height());
            });
        });
//
//
        MatOfInt labelsMat = new MatOfInt();
        labelsMat.fromList(trainingImageLabel);


//
//            System.out.println("Starting training...");
        Log.d(TAG, "Starting training");
        if (recognizer != null) {

            Log.d(TAG, "start training rec");
            recognizer.train(trainingImageData, labelsMat);
            int[] outLabel = new int[1];
            double[] outConf = new double[1];
//                System.out.println("Starting Prediction...");
            Log.d(TAG, "Starting prediction");
            recognizer.predict(c, outLabel, outConf);
            Log.d(TAG, "***Predicted label is " + outLabel[0] + ".***");
            Log.d(TAG, "***Confidence value is " + outConf[0] + ".***");

            if (outConf[0] > acceptableConfidenceLevel) {
                Log.d(TAG, "updating register");
                dbHelper.onAddUserToAttendanceRegister((int) outLabel[0]);
            }

        } else {

            Log.d(TAG, "recognizer is null");
        }


        c.release();
        isPerformingRecognition = false;
    }

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            Log.d(TAG, "on manager connected. Status: " + status);
            if (status == LoaderCallbackInterface
                    .SUCCESS) {
                Toast.makeText(getApplicationContext(), "Open cv loaded", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "OpenCV loaded successfully");
                CascadeClassifier cascadeClassifier;
                try {
                    //load cascade file from application resources
                    InputStream is = getResources().openRawResource(R.raw.frontalface);
                    File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
                    cascadeFile = new File(cascadeDir, "frontalface.xml");
                    FileOutputStream os = new FileOutputStream(cascadeFile);

                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = is.read(buffer)) != -1) {
                        os.write(buffer, 0, bytesRead);
                    }
                    is.close();
                    os.close();

                    recognizer = LBPHFaceRecognizer.create();
//                    recognizer = EigenFaceRecognizer.create();
//                    FisherFaceRecognizer r = FisherFaceRecognizer.create();
                    cascadeClassifier = new CascadeClassifier(cascadeFile.getAbsolutePath());
                    if (cascadeClassifier.empty()) {
                        Log.e(TAG, "Failed to load cascade classifier");

                        cascadeClassifier = null;

                    } else {
                        Log.i(TAG, "Loaded cascade classifier from " + cascadeFile.getAbsolutePath());
                        opencvUtility = new OpencvUtility(cascadeClassifier);

                        cascadeDir.delete();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "Failed to load cascade. Exception thrown: " + e);
                }
                mOpenCvCameraView.enableView();
            } else {
                super.onManagerConnected(status);
            }
        }
    };


}
