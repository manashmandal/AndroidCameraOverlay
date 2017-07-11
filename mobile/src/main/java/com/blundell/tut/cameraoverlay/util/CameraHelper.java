package com.blundell.tut.cameraoverlay.util;

import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;

import com.blundell.tut.cameraoverlay.ui.MainActivity;
import android.hardware.Camera.CameraInfo;

/**
 * Used to make camera use in the tutorial a bit more obvious
 * in a production environment you wouldn't make these calls static
 * as you have no way to mock them for testing
 *
 * @author paul.blundell
 */
public class CameraHelper {

    public static boolean cameraAvailable(Camera camera) {
        return camera != null;
    }

    private static int getFrontCameraId() {
        CameraInfo ci = new CameraInfo();
        for (int i = 0 ; i < Camera.getNumberOfCameras(); i++) {
            Camera.getCameraInfo(i, ci);
            if (ci.facing == CameraInfo.CAMERA_FACING_FRONT) return i;
        }
        return -1; // No front-facing camera found
    }

    private static int getBackCameraId(){
        CameraInfo ci = new CameraInfo();
        for (int i = 0 ; i < Camera.getNumberOfCameras(); i++) {
            Camera.getCameraInfo(i, ci);
            if (ci.facing == CameraInfo.CAMERA_FACING_BACK) return i;
        }
        return -1; // No front-facing camera found
    }


    public static Camera getCameraInstance(int which_cam) {
        int camID = -1;


        if (which_cam == MainActivity.FRONT_CAMERA){
            camID = CameraHelper.getFrontCameraId();
        } else if (which_cam == MainActivity.BACK_CAMERA) {
            camID = CameraHelper.getBackCameraId();
        }

        Camera c = null;
        try {
            c = Camera.open(camID);
//            c = Camera.open(which_cam);
        } catch (Exception e) {
            // Camera is not available or doesn't exist
            Log.d("getCamera failed", e);
        }
        return c;
    }

}
