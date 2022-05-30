package app0.com.autoselfie;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
//why PictureCallback class was deprecated
import android.util.AttributeSet;
import android.util.Log;

import org.opencv.android.JavaCameraView;

import java.io.FileOutputStream;
import java.io.IOException;

public class CamView extends CustomJavaCameraView implements PictureCallback {

    private static final String TAG = "AutoSelfie::camView";
    private String mPictureFileName;
    public CamView(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera){
        Log.i(TAG, "Saving a bitmap to file");
        // The camera preview was automatically stopped. Start it again.
        mCamera.startPreview();
        mCamera.setPreviewCallback(this);
//        mCamera.setDisplayOrientation(90);

        // Write the image in a file (in jpeg format)
        try{
            FileOutputStream fos = new FileOutputStream(mPictureFileName);
            fos.write(data);
            fos.close();
        }catch (IOException e){
            Log.e("PictureDemo", "Exception in photoCallback", e);
        }
    }

    public void takePicture(final String fileName){
        Log.i(TAG, "Taking picture");
        this.mPictureFileName = fileName;
        // PostView and jpeg are sent in the same buffers if the queue is not
        // empty when performing a capture.
        // Clear up buffers to avoid mCamera.takePicture to be stuck because
        // of memory issue
        mCamera.setPreviewCallback(null);
        //PictureCallback is implement by the current class
        mCamera.takePicture(null, null, this);
    }

}
