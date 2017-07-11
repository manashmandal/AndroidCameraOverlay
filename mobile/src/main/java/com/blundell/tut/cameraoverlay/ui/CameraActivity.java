package com.blundell.tut.cameraoverlay.ui;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.view.View;

import com.blundell.tut.cameraoverlay.FromXML;
import com.blundell.tut.cameraoverlay.R;
import com.blundell.tut.cameraoverlay.ui.widget.CameraPreview;
import com.blundell.tut.cameraoverlay.util.Log;

import java.io.File;

import static com.blundell.tut.cameraoverlay.util.CameraHelper.cameraAvailable;
import static com.blundell.tut.cameraoverlay.util.CameraHelper.getCameraInstance;
import static com.blundell.tut.cameraoverlay.util.MediaHelper.getOutputMediaFile;
import static com.blundell.tut.cameraoverlay.util.MediaHelper.saveToFile;

/**
 * Takes a photo saves it to the SD card and returns the path of this photo to the calling Activity
 *
 * @author paul.blundell
 */
public class CameraActivity extends Activity implements PictureCallback {

    protected static final String EXTRA_IMAGE_PATH = "com.blundell.tut.cameraoverlay.ui.CameraActivity.EXTRA_IMAGE_PATH";

    private Camera camera;

    private static int WHICH_CAMERA = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        setResult(RESULT_CANCELED);

        int which_camera = getIntent().getIntExtra(MainActivity.WHICH_CAMERA, -1);

        // Saving the value
        this.WHICH_CAMERA = which_camera;

        Log.i("[Camera type] = " + String.valueOf(which_camera));

        // Camera may be in use by another activity or the system or not available at all
        camera = getCameraInstance(this.WHICH_CAMERA);
        if (cameraAvailable(camera)) {
            initCameraPreview();
        } else {
            finish();
        }
    }

    // Show the camera view on the activity
    private void initCameraPreview() {
        CameraPreview cameraPreview = (CameraPreview) findViewById(R.id.camera_preview);
        cameraPreview.init(camera);
    }

    @FromXML
    public void onCaptureClick(View button) {
        // Take a picture with a callback when the photo has been created
        // Here you can add callbacks if you want to give feedback when the picture is being taken
        camera.takePicture(null, null, this);
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        Log.d("Picture taken");
        String path = savePictureToFileSystem(data);
        setResult(path);
        finish();
    }

    private static String savePictureToFileSystem(byte[] data) {
        File file = getOutputMediaFile();
        saveToFile(data, file);
        return file.getAbsolutePath();
    }

    private void setResult(String path) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_IMAGE_PATH, path);
        setResult(RESULT_OK, intent);
    }

    // ALWAYS remember to release the camera when you are finished
    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    private void releaseCamera() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }
}
