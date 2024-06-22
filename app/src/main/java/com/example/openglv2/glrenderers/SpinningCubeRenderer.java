package com.example.openglv2.glrenderers;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.example.openglv2.objects.Cube;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class SpinningCubeRenderer implements GLSurfaceView.Renderer {
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
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        //GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        final float lookX = 0.0f;
        final float lookY = 0.0f;
        final float lookZ = 0.0f;
        //Which way is up for the eyeball
        final float upX = 0.0f;
        final float upY = 0.0f;
        final float upZ = 1.0f;

        Matrix.setLookAtM(viewMatrix, 0, eyePos[0], eyePos[1], eyePos[2], lookX, lookY, lookZ, upX, upY, upZ);
        cube = new Cube(0.0f,0.0f,0.0f,1.0f);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        // Set the OpenGL viewport to the same size as the surface.
        GLES20.glViewport(0, 0, width, height);

        // Create a new perspective projection matrix. The height will stay the same
        // while the width will vary as per aspect ratio.
        final float ratio = (float) width / height;
        final float left = -ratio;
        final float right = ratio;
        final float bottom = -1.0f;
        final float top = 1.0f;
        final float near = 1.0f;
        final float far = 10.0f;

        Matrix.frustumM(projectionMatrix, 0, left, right, bottom, top, near, far);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // Do a complete rotation every 10 seconds.
        long time = SystemClock.uptimeMillis() % 10000L;
        float angleInDegrees = (360.0f / 10000.0f) * ((int) time);

        Matrix.setIdentityM(lightModelMatrix,0);
        Matrix.translateM(lightModelMatrix,0, lightPos[0], lightPos[1], lightPos[2]);
        //Matrix.rotateM(lightModelMatrix,0,angleInDegrees,0,1,1);
        //Matrix.translateM(lightModelMatrix, 0,2,0,0);

        Matrix.multiplyMV(lightPosInWorldSpace,0,lightModelMatrix,0, lightPosInModelSpace, 0);
        Matrix.multiplyMV(lightPosInEyeSpace, 0, viewMatrix, 0, lightPosInWorldSpace, 0);

        Matrix.setIdentityM(modelMatrix,0);
        Matrix.translateM(modelMatrix, 0,0.0f,0.0f,0.0f);
        Matrix.rotateM(modelMatrix,0,angleInDegrees,1.0f,1.0f,-1.0f);

        Matrix.multiplyMM(MVPMatrix, 0, viewMatrix, 0, modelMatrix, 0);
        MVMatrix=MVPMatrix;
        Matrix.multiplyMM(MVPMatrix,0,projectionMatrix,0,MVPMatrix,0);
        cube.draw(MVMatrix,MVPMatrix,lightPosInEyeSpace);
    }


}
