package com.example.openglv2.glrenderers;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.pow;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.example.openglv2.objects.Sphere;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class TwoDPendulumRenderer implements GLSurfaceView.Renderer {
    private float[] viewMatrix = new float[16];
    private float[] projectionMatrix = new float[16];
    private float[] lightModelMatrix = new float[16]; // Position of the light
    private float[] lightPosInWorldSpace = new float[4];
    private float[] lightPosInModelSpace = {0.0f,0.0f,0.0f,1.0f};
    private float[] lightPosInEyeSpace = new float[4];
    private float[] modelMatrix = new float[16];
    private float[] MVPMatrix = new float[16];
    private float[] MVMatrix = new float[16];

    private float[] lightPos = {-4.0f,0.0f,0.0f};
    private float[] eyePos = {-4.0f,0.0f,0.0f};

    private float maxVel = 10.0f;
    private float[] attachToDot = {0.0f,0.0f,2.0f};
    private float[] startPos = {0.0f,0.0f,0.0f};
    private float[] center = startPos;

    private double g=9.8/10000;
    private double mass = 1;
    private float length =
            (float) pow(pow(attachToDot[0]-startPos[0],2)+pow(attachToDot[1]-startPos[1],2)+pow(attachToDot[2]-startPos[2],2),0.5);
    private float[] graphPos;

    private long startTime;
    Sphere sphere;
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
        sphere = new Sphere(startPos[0], startPos[1], startPos[2], 0.2f);

        startTime = System.nanoTime();
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
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
        long currentTime = System.nanoTime();
        long time = SystemClock.uptimeMillis() % 10000L;
        float angleInDegrees = (360.0f / 10000.0f) * ((int) time);

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        Matrix.setIdentityM(lightModelMatrix,0);
        Matrix.translateM(lightModelMatrix,0, lightPos[0], lightPos[1], lightPos[2]);
        //Matrix.rotateM(lightModelMatrix,0,angleInDegrees,0,1,1);
        //Matrix.translateM(lightModelMatrix, 0,2,0,0);

        Matrix.multiplyMV(lightPosInWorldSpace,0,lightModelMatrix,0, lightPosInModelSpace, 0);
        Matrix.multiplyMV(lightPosInEyeSpace, 0, viewMatrix, 0, lightPosInWorldSpace, 0);

        Matrix.setIdentityM(modelMatrix,0);
        Matrix.translateM(modelMatrix, 0,attachToDot[0],attachToDot[1],attachToDot[2]);
        Matrix.rotateM(modelMatrix,0,getNextAngle(currentTime),1,0,0);
        Matrix.translateM(modelMatrix,0,-attachToDot[0],-attachToDot[1],-attachToDot[2]);

        Matrix.multiplyMM(MVPMatrix, 0, viewMatrix, 0, modelMatrix, 0);
        MVMatrix=MVPMatrix;
        Matrix.multiplyMM(MVPMatrix,0,projectionMatrix,0,MVPMatrix,0);
        sphere.draw(MVMatrix,MVPMatrix,lightPosInEyeSpace,new float[]{0.9f,0.45f,0.9f,1.0f});
    }

    private float getNextAngle(long currentTime){
        long elapsedTime = currentTime-startTime;
        float seconds = elapsedTime/1000000000.0f;
        float velocity = maxVel*(float)cos(seconds*2*PI);
        return velocity*length;
    }
}
