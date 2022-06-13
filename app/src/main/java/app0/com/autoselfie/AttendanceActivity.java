package app0.com.autoselfie;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Rect;
import org.opencv.face.FaceRecognizer;
import org.opencv.face.LBPHFaceRecognizer;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;


public class AttendanceActivity extends AppCompatActivity {

    private static final String TAG = "AttendanceActivity";
    private File cascadeFile;
    private Mat mRgba, mGray;
    private CamView mOpenCvCameraView;
    private DbHelper dbHelper;
    private boolean shouldFrameBeCaptured;
    private boolean isPerformingRecognition = false;
    FaceRecognizer recognizer;
    OpencvUtility opencvUtility;
    private int acceptableConfidenceLevel = 80;
    private int scheduleEntryId;
    private String courseCode;
    private final Semaphore semaphore = new Semaphore(1);
    private int count = 1;
    ArrayList<UserModel> users = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        Bundle bundle = getIntent().getExtras();
        scheduleEntryId = bundle.getInt("scheduleEntryId");
        courseCode = bundle.getString("courseCode");

        dbHelper = new DbHelper(getApplicationContext());
        shouldFrameBeCaptured = false;
        Toast.makeText(getApplicationContext(), "Please look into the camera.", Toast.LENGTH_SHORT).show();


        mOpenCvCameraView = findViewById(R.id.auto_selfie_activity_surface_view);
        mOpenCvCameraView.setCameraIndex(1);
        mOpenCvCameraView.setMaxFrameSize(600, 600);


        mOpenCvCameraView.setCvCameraViewListener(new CvCameraViewListener2() {
            @Override
            public void onCameraViewStarted(int width, int height) {
                Log.d(TAG, "Started Camera View");
                mGray = new Mat();
                mRgba = new Mat();
            }

            @Override
            public void onCameraViewStopped() {
                Log.d(TAG, "Stopped Camera View");
                mGray.release();
                mRgba.release();


            }

            @Override
            public Mat onCameraFrame(CvCameraViewFrame inputFrame) {


                if (mGray != null) {
                    mGray.release();
                }

                if (mRgba != null) {
                    mRgba.release();
                }


                mRgba = inputFrame.rgba();
                mGray = inputFrame.gray();

                Mat mRgbaT = new Mat();
                Mat mGrayT = new Mat();
                Mat mRgbaR = new Mat();
                Mat mGrayR = new Mat();


                Core.transpose(mRgba, mRgbaT);
                Core.transpose(mGray, mGrayT);

                Core.flip(mRgbaT, mRgbaR, -1);
                Core.flip(mGrayT, mGrayR, -1);

                Imgproc.resize(mRgbaR, mRgbaR, mRgba.size(), 0, 0, 0);
                Imgproc.resize(mGrayR, mGrayR, mGray.size(), 0, 0, 0);

                mRgbaT.release();
                mGrayT.release();

                new ThreadWithSemaphore(semaphore, mRgbaR, mGrayR).start();


                return mRgbaR;


            }
        });

        new Handler().postDelayed(() -> {
            shouldFrameBeCaptured = true;
            Log.d(TAG, "Frame is currently being captured");
            Toast.makeText(getApplicationContext(), "Your image is being captured", Toast.LENGTH_SHORT).show();

        }, 5000);

        users = dbHelper.onGetUsersImages();

        Log.d(TAG, "Number of users: " + users.size());


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

    private void captureFrames(Mat mRgba, Mat mGray) {

        try {

            if (mRgba == null)
                return;


            Log.d(TAG, "No face detected height " + mGray.height());
            Rect[] facesArray = opencvUtility.getRectsForDetectedFaces(mGray);
            Log.d(TAG, "No face detected " + facesArray.length);

            if (facesArray.length == 1) {
                Log.d(TAG, "FAce detected");
                performRecognition(mRgba, mGray, facesArray);

            } else {
                Log.d(TAG, "No face detected");
            }


        } catch (Exception ex) {


            Log.e(TAG, ex.getMessage());

        }

    }

    private void performRecognition(Mat mRgba, Mat mGray, Rect[] facesArray) {
        try {


            Mat face = opencvUtility.getMatForFace(mRgba, new Mat(mGray, facesArray[0]), facesArray[0]);

            ArrayList<UserModel> users = dbHelper.onGetUsersImages();
            ArrayList<Mat> trainingImageData = new ArrayList<>();
            ArrayList<Integer> trainingImageLabel = new ArrayList<>();

            Log.d(TAG, "Training Data");
            users.forEach(user -> {
                user.getImages().forEach(image -> {


                    File file = new File(image);
                    Mat imageMat = Imgcodecs.imread(file.toString());

                    Imgproc.cvtColor(imageMat, imageMat, Imgproc.COLOR_BGR2GRAY);
                    trainingImageData.add(imageMat);
                    trainingImageLabel.add(user.getId());

                });
            });

            MatOfInt labelsMat = new MatOfInt();
            labelsMat.fromList(trainingImageLabel);
            Log.d(TAG, "Starting training");

            if (recognizer != null) {

                recognizer.train(trainingImageData, labelsMat);
                int[] outLabel = new int[1];
                double[] outConf = new double[1];
                Log.d(TAG, "Starting prediction");
                recognizer.predict(face, outLabel, outConf);

                int studentId = outLabel[0];
                double confidenceLevel = (double) outConf[0];
                if (confidenceLevel < acceptableConfidenceLevel) {

                    Log.d(TAG, "updating register");

                    if (dbHelper.onAddUserToAttendanceRegister(studentId, scheduleEntryId, confidenceLevel)) {
                        Log.d(TAG, "Success");

                        String nameOfStudent =  dbHelper.onGetStudentName(studentId);

                        this.runOnUiThread(() -> {
                            Toast.makeText(getApplicationContext(), String.format("Hi %s. Welcome to %s", nameOfStudent, courseCode ) , Toast.LENGTH_SHORT).show();

                        });
                    } else {
                        Log.d(TAG, "Failed");
                    }
                }

            } else {

                Log.d(TAG, "recognizer is null");
            }


            face.release();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

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


    class ThreadWithSemaphore extends Thread {

        Semaphore semaphore;
        Mat mRgbaR, mGrayR;

        public ThreadWithSemaphore(Semaphore semaphore, Mat mRgbaR, Mat mGrayR) {
            this.semaphore = semaphore;
            this.mGrayR = mGrayR;
            this.mRgbaR = mRgbaR;
        }

        public void run() {

            boolean acquireLock = semaphore.tryAcquire();


            if (acquireLock) {
                while (!Thread.interrupted()) {
                    captureFrames(mRgbaR, mGrayR);
                }

                semaphore.release();
            }

            Thread.interrupted();
        }
    }

}
