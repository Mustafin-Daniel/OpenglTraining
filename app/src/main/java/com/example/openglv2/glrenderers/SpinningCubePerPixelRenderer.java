package com.example.openglv2.glrenderers;

import android.opengl.GLSurfaceView;

import com.example.openglv2.objects.Cube;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class SpinningCubePerPixelRenderer implements GLSurfaceView.Renderer {
    private float[] viewMatrix = new float[16];
    private float[] projectionMatrix = new float[16];
    private float[] lightModelMatrix = new float[16]; // Position of the light
    private float[] lightPosInWorldSpace = new float[4];
    private float[] lightPosInModelSpace = {0.0f,0.0f,0.0f,1.0f};
    private float[] lightPosInEyeSpace = new float[4];
    private float[] modelMatrix = new float[16];
    private float[] MVPMatrix = new float[16];
    private float[] MVMatrix = new float[16];
    private float[] lightPos = {3.0f,0.0f,1.0f};
    private float[] eyePos = {3.0f,0.0f,1.0f};

    private Cube cube;
    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {

    }

    @Override
    public void onDrawFrame(GL10 gl10) {

    }
}
