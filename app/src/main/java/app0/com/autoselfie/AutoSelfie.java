package app0.com.autoselfie;

//import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import org.opencv.android.BaseLoaderCallback;
//import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


public class AutoSelfie extends AppCompatActivity implements CvCameraViewListener2 {
    Handler handler;
    private static final String TAG = "AutoSelfie::camView";
    private File cascadeFile;
    private CascadeClassifier cascadeClassifier;
    private Mat mRgba, mGray;
    private CamView mOpenCvCameraView;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view);

        mOpenCvCameraView = (CamView) findViewById(R.id.auto_selfie_activity_surface_view);
        mOpenCvCameraView.setCameraIndex(1);
        mOpenCvCameraView.setCvCameraViewListener(this);

        handler = new Handler();
    }

    @Override
    public void onResume(){
        super.onResume();
        if(OpenCVLoader.initDebug()){
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }else{
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, mLoaderCallback);
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height){
        mGray = new Mat();
        mRgba = new Mat();
    }

    public void onCameraViewStopped(){
        mGray.release();
        mRgba.release();
    }

    int mAbsoluteFaceSize = 0, mRelativeFaceSize = 100;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public Mat onCameraFrame(CvCameraViewFrame inputFrame){
        //Flip around the Y axis
        Core.flip(inputFrame.rgba(), mRgba, 1);
        Core.flip(inputFrame.gray(), mGray, 1);

        if(mAbsoluteFaceSize == 0){
            int height = mGray.rows();
            if(Math.round(height * mRelativeFaceSize) > 0) {
                mAbsoluteFaceSize = Math.round(height * mRelativeFaceSize);
            }
        }



        MatOfRect closedHands = new MatOfRect();
        if(cascadeClassifier != null)
            cascadeClassifier.detectMultiScale(mGray, closedHands, 1.1,
                    2, 2, new Size(mAbsoluteFaceSize, mAbsoluteFaceSize), new Size());

        Rect[] facesArray = closedHands.toArray();
        Map<Integer, Integer> rectBuckts = new LinkedHashMap<>();
        Map<Integer, Rect> rectCue = new HashMap<>();
        for(int i = 0; i < facesArray.length; i++)
        {
            Imgproc.rectangle(mRgba, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 255, 0, 255), 3);

            Point quatnizedTL = new Point(((int)(facesArray[i].tl().x/100)) * 100,((int)(facesArray[i].tl().y/100))*100);

            Point quatnizedBR = new Point(((int)(facesArray[i].br().x/100))*100, ((int)(facesArray[i].br().y/100))*100);

            int bucktID = quatnizedTL.hashCode()+quatnizedBR.hashCode()*2;
            if(rectBuckts.containsKey(bucktID))
            {
                rectBuckts.put(bucktID, rectBuckts.get(bucktID)+1);
                rectCue.put(bucktID, new Rect(quatnizedTL, quatnizedBR));
            }else{
                rectBuckts.put(bucktID, 1);
            }
        }

        int maxDetections = 0;
        int maxDetectionsKey = 0;

        for(Map.Entry<Integer, Integer> e : rectBuckts.entrySet())
        {
            if(e.getValue() > maxDetections)
            {
                maxDetections = e.getValue();
                maxDetectionsKey = e.getKey();
            }
        }

        if(maxDetections > 5)
        {
            Imgproc.rectangle(mRgba, rectCue.get(maxDetectionsKey).tl(),
                    rectCue.get(maxDetectionsKey).br(), new Scalar(0, 0, 0, 255), 3);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            String currentDateandTime = sdf.format(new Date());
            String fileName = Environment.getExternalStorageDirectory().getPath() + "/sample_picture_" + currentDateandTime + ".jpg";

            mOpenCvCameraView.takePicture(fileName);

            Message msg = handler.obtainMessage();
            msg.arg1 = 1;
            Bundle b = new Bundle();
            b.putString("msg", fileName + " saved");
            msg.setData(b);
            handler.sendMessage(msg);
            rectBuckts.clear();
        }
//        mRgba = inputFrame.rgba();
        return mRgba;
    }

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this){
        @Override
        public void onManagerConnected(int status){
            switch (status){
                case LoaderCallbackInterface
                        .SUCCESS:{
                    Log.i(TAG, "OpenCV loaded successfully");
                    try{
                        //load cascade file from application resources
                        InputStream is = getResources().openRawResource(R.raw.frontalface);
                        File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
                        cascadeFile = new File(cascadeDir, "frontalface.xml");
                        FileOutputStream os = new FileOutputStream(cascadeFile);

                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while((bytesRead = is.read(buffer)) != -1){
                            os.write(buffer, 0, bytesRead);
                        }
                        is.close();os.close();

                        //initialize the Cascade Classifier object using the trained cascade file
                        cascadeClassifier = new CascadeClassifier(cascadeFile.getAbsolutePath());
                        if(cascadeClassifier.empty()){
                            Log.e(TAG, "Failed to load cascade classifier");
                            cascadeClassifier = null;
                        }else
                            Log.i(TAG, "Loaded cascade classifier from "+cascadeFile.getAbsolutePath());

                        cascadeDir.delete();

                    }catch(IOException e){
                        e.printStackTrace();
                        Log.e(TAG, "Failed to load cascade. Exception thrown: "+ e);
                    }
                    mOpenCvCameraView.enableView();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };



}
