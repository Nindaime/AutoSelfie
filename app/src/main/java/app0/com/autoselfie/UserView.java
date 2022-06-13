package app0.com.autoselfie;



import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class UserView extends AppCompatActivity implements CvCameraViewListener2 {
    Handler handler;
    private static final String TAG = "UserView";
    private File cascadeFile;
    private CascadeClassifier cascadeClassifier;
    private Mat mRgba, mGray;
    private CamView mOpenCvCameraView;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager recyclerViewLayoutManager;
    LinearLayoutManager horizontalLayout;
    Adapter adapter;
    HashMap<Integer, String> images;
    ArrayList<Integer> imagesId;
    private String username;
    private String email;
    private int id;
    private DbHelper dbHelper;
    private boolean shouldFrameBeCaptured;
    private OpencvUtility opencvUtility;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_view);


        dbHelper = new DbHelper(getApplicationContext());

        shouldFrameBeCaptured = false;

        Bundle bundle = getIntent().getExtras();

        email = bundle.getString("email");
        id = bundle.getInt("id");
        String firstName = bundle.getString("firstName");
        String lastName = bundle.getString("lastName");

        username = firstName + lastName;
        Log.d(TAG, "User id for storing the user in file system is: " + username);

        mOpenCvCameraView = (CamView) findViewById(R.id.auto_selfie_activity_surface_view);
        mOpenCvCameraView.setCameraIndex(1);
        mOpenCvCameraView.setMaxFrameSize(600, 600);
        mOpenCvCameraView.setCvCameraViewListener(this);


        handler = new Handler();

        images = new HashMap<>();
        imagesId = new ArrayList<>();

        recyclerView
                = (RecyclerView) findViewById(
                R.id.recyclerView);
        recyclerViewLayoutManager
                = new LinearLayoutManager(
                getApplicationContext());


        adapter = new Adapter(imagesId, images, dbHelper);


        // Set Horizontal Layout Manager
        // for Recycler view
        horizontalLayout
                = new LinearLayoutManager(
                UserView.this,
                LinearLayoutManager.HORIZONTAL,
                false);

        recyclerView.setLayoutManager(horizontalLayout);


        // Set adapter on recycler view
        recyclerView.setAdapter(adapter);


        new Handler().postDelayed(() -> {
            shouldFrameBeCaptured = true;
            Log.d(TAG, "Frame is currently being captured");
            Toast.makeText(getApplicationContext(), "Your image is being captured", Toast.LENGTH_SHORT).show();

        }, 5000);


        new Handler().postDelayed(() -> {
            shouldFrameBeCaptured = false;
            Log.d(TAG, "Frame is not being captured.");
            Toast.makeText(getApplicationContext(), "Your images have been captured successfully.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }, 35000);


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

        if (shouldFrameBeCaptured) {
            captureFrames(mRgbaR, mGrayR);
        }


        return mRgbaR;
    }

    private void captureFrames(Mat mRgba, Mat mGray) {
        Log.d(TAG, "checking on camera frame handler: ");



        Rect[] facesArray = opencvUtility.getRectsForDetectedFaces(mGray);


        Log.d(TAG, "Number of faces: " + facesArray.length);
        if (facesArray.length == 1) {

            Mat c = opencvUtility.getMatForFace(mRgba, new Mat(mGray, facesArray[0]), facesArray[0]);


            File path = getApplicationContext().getExternalFilesDir(null);

//            Log.d(TAG, "this is a file5: "+file5);

            String filename = username + System.currentTimeMillis() / 1000L + ".jpg";
            File file = new File(path, filename);
            boolean bool;
            String imagePath = file.toString();

            try {

                bool = Imgcodecs.imwrite(imagePath, c);
                c.release();


                if (bool) {

                    long imagedId = dbHelper.onAddUserImage(id, imagePath);


                    images.put((int) imagedId, imagePath);
                    imagesId.add((int) imagedId);
                    ArrayList<Integer> newImagesId = new ArrayList<>();

                    Iterator<Integer> iterator = imagesId.iterator();

                    while (iterator.hasNext()) {
                        //Add the object clones
                        newImagesId.add((Integer) iterator.next());
                    }

                    imagesId = null;
                    imagesId = newImagesId;


                    Log.i(TAG, "SUCCESS writing image to external storage " + imagePath);
                } else
                    Log.i(TAG, "Fail writing image to external storage " + imagePath);

            } catch (Exception ex) {

                Log.d(TAG, ex.getMessage());
            }


        }
    }


    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            Log.d(TAG, "on manager connected. Status: " + status);
            if (status == LoaderCallbackInterface
                    .SUCCESS) {
                Log.i(TAG, "OpenCV loaded successfully");
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

                    //initialize the Cascade Classifier object using the trained cascade file
                    cascadeClassifier = new CascadeClassifier(cascadeFile.getAbsolutePath());
                    if (cascadeClassifier.empty()) {
                        Log.e(TAG, "Failed to load cascade classifier");

                        cascadeClassifier = null;
                    } else {
                        Log.i(TAG, "Loaded cascade classifier from " + cascadeFile.getAbsolutePath());
                        opencvUtility = new OpencvUtility(cascadeClassifier);
                        cascadeDir.delete();
                    }


                } catch (IOException e) {
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
