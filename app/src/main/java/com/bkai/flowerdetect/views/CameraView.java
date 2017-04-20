package com.bkai.flowerdetect.views;

/**
 * Created by marsch on 3/1/17.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Size;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;
import org.opencv.android.JavaCameraView;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class CameraView extends JavaCameraView implements PictureCallback, AutoFocusCallback {

    private String mPictureFileName;

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    private static boolean takingPicture = false;

    public List<String> getEffectList() {
        return mCamera.getParameters().getSupportedColorEffects();
    }

    public boolean isEffectSupported() {
        return (mCamera.getParameters().getColorEffect() != null);
    }

    public String getEffect() {
        return mCamera.getParameters().getColorEffect();
    }

    public void setEffect(String effect) {
        Camera.Parameters params = mCamera.getParameters();
        params.setColorEffect(effect);
        mCamera.setParameters(params);
    }

    public List<Size> getResolutionList() {
        return mCamera.getParameters().getSupportedPreviewSizes();
    }

    public void setResolution(Size resolution) {
        disconnectCamera();
        mMaxHeight = resolution.height;
        mMaxWidth = resolution.width;
        connectCamera(getWidth(), getHeight());
    }

    public Size getResolution() {
        return mCamera.getParameters().getPreviewSize();
    }

     public synchronized void takePicture(final String fileName,Camera.PreviewCallback callback) {
        this.mPictureFileName = fileName;
        // Postview and jpeg are sent in the same buffers if the queue is not empty when performing a capture.
        // Clear up buffers to avoid mCamera.takePicture to be stuck because of a memory issue
        mCamera.setPreviewCallback(callback);

        // PictureCallback is implemented by the current class
         mCamera.takePicture(null, null, this);
    }

    @Override
    public synchronized void onPictureTaken(byte[] data, Camera camera) {
        // The camera preview was automatically stopped. Start it again.
        mCamera.startPreview();
        mCamera.setPreviewCallback(this);
        // Write the image in a file (in jpeg format)
        try {
            FileOutputStream fos = new FileOutputStream(mPictureFileName);

            fos.write(data);
            fos.close();

        } catch (java.io.IOException e) {
            Log.e("PictureDemo", "Exception in photoCallback", e);
        }
    }

    public void showPreview(String fullPath){
        Intent showPicture = new Intent(getContext(), ShowPicture.class);
        showPicture.putExtra("img_path", fullPath);
        getContext().startActivity(showPicture);
    }

    @Override
    public void onAutoFocus(boolean b, Camera camera) {

    }

    public void focusOnTouch(MotionEvent event) {
        Rect focusRect = calculateTapArea(event.getRawX(), event.getRawY(), 1f);
        Rect meteringRect = calculateTapArea(event.getRawX(), event.getRawY(), 1.5f);

        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);

        if (parameters.getMaxNumFocusAreas() > 0) {
            List<Camera.Area> focusAreas = new ArrayList<Camera.Area>();
            focusAreas.add(new Camera.Area(focusRect, 1000));

            parameters.setFocusAreas(focusAreas);
        }

        if (parameters.getMaxNumMeteringAreas() > 0) {
            List<Camera.Area> meteringAreas = new ArrayList<Camera.Area>();
            meteringAreas.add(new Camera.Area(meteringRect, 1000));

            parameters.setMeteringAreas(meteringAreas);
        }
        mCamera.setParameters(parameters);
        mCamera.autoFocus(this);
    }

    private Rect calculateTapArea(float x, float y, float coefficient) {
        float focusAreaSize = 300;
        int areaSize = Float.valueOf(focusAreaSize * coefficient).intValue();

        int centerX = (int) (x / getResolution().width - 1000);
        int centerY = (int) (y / getResolution().height - 1000);

        int left = clamp(centerX - areaSize / 2, -1000, 1000);
        int top = clamp(centerY - areaSize / 2, -1000, 1000);

        RectF rectF = new RectF(left, top, left + areaSize, top + areaSize);

        return new Rect(Math.round(rectF.left), Math.round(rectF.top), Math.round(rectF.right), Math.round(rectF.bottom));
    }

    private int clamp(int x, int min, int max) {
        if (x > max) {
            return max;
        }
        if (x <min) {
            return min;
        }
        return x;
    }

    public void setFocusMode (Context item, int type){
        Camera.Parameters params = mCamera.getParameters();
        List<String> FocusModes = params.getSupportedFocusModes();

        switch (type){
            case 0:
                if (FocusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO))
                    params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                else
                    Toast.makeText(item, "Auto Mode not supported", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                if (FocusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO))
                    params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
                else
                    Toast.makeText(item, "Continuous Mode not supported", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                if (FocusModes.contains(Camera.Parameters.FOCUS_MODE_EDOF))
                    params.setFocusMode(Camera.Parameters.FOCUS_MODE_EDOF);
                else
                    Toast.makeText(item, "EDOF Mode not supported", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                if (FocusModes.contains(Camera.Parameters.FOCUS_MODE_FIXED))
                    params.setFocusMode(Camera.Parameters.FOCUS_MODE_FIXED);
                else
                    Toast.makeText(item, "Fixed Mode not supported", Toast.LENGTH_SHORT).show();
                break;
            case 4:
                if (FocusModes.contains(Camera.Parameters.FOCUS_MODE_INFINITY))
                    params.setFocusMode(Camera.Parameters.FOCUS_MODE_INFINITY);
                else
                    Toast.makeText(item, "Infinity Mode not supported", Toast.LENGTH_SHORT).show();
                break;
            case 5:
                if (FocusModes.contains(Camera.Parameters.FOCUS_MODE_MACRO))
                    params.setFocusMode(Camera.Parameters.FOCUS_MODE_MACRO);
                else
                    Toast.makeText(item, "Macro Mode not supported", Toast.LENGTH_SHORT).show();
                break;
        }

        mCamera.setParameters(params);
    }

    public void setFlashMode (Context item, int type){
        Camera.Parameters params = mCamera.getParameters();
        List<String> FlashModes = params.getSupportedFlashModes();

        switch (type){
            case 0:
                if (FlashModes.contains(Camera.Parameters.FLASH_MODE_AUTO))
                    params.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
                else
                    Toast.makeText(item, "Auto Mode not supported", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                if (FlashModes.contains(Camera.Parameters.FLASH_MODE_OFF))
                    params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                else
                    Toast.makeText(item, "Off Mode not supported", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                if (FlashModes.contains(Camera.Parameters.FLASH_MODE_ON))
                    params.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
                else
                    Toast.makeText(item, "On Mode not supported", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                if (FlashModes.contains(Camera.Parameters.FLASH_MODE_RED_EYE))
                    params.setFlashMode(Camera.Parameters.FLASH_MODE_RED_EYE);
                else
                    Toast.makeText(item, "Red Eye Mode not supported", Toast.LENGTH_SHORT).show();
                break;
            case 4:
                if (FlashModes.contains(Camera.Parameters.FLASH_MODE_TORCH))
                    params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                else
                    Toast.makeText(item, "Torch Mode not supported", Toast.LENGTH_SHORT).show();
                break;
        }

        mCamera.setParameters(params);
    }
}
