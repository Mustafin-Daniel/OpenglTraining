package com.example.openglv2.glrenderers;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.pow;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.view.SurfaceHolder;

import com.example.openglv2.objects.Sphere;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class WavePendulum implements GLSurfaceView.Renderer {
    private float[] viewMatrix = new float[16];
    private float[] projectionMatrix = new float[16];
    private float[] lightModelMatrix = new float[16]; // Position of the light
    private float[] lightPosInWorldSpace = new float[4];
    private float[] lightPosInModelSpace = {0.0f,0.0f,0.0f,1.0f};
    private float[] lightPosInEyeSpace = new float[4];
    private float[] modelMatrix = new float[16];
    private float[] MVPMatrix = new float[16];
    private float[] MVMatrix = new float[16];
    //private float[] lightPos = {1.f,0.0f,0.0f}; //TODO Fix this currently its in the dark but still getting lighting
    //private float[] eyePos = {-1.5f,0.0f,0.0f};
    private float[] lightPos = {1.5f,0.0f,1.5f};
    private float[] eyePos = {2.0f,0.0f,1.0f};

    private float g = 24.79f;

    private int numSpheres = 12;
    Sphere[] spheres = new Sphere[numSpheres];
    float[] lengths = new float[numSpheres];

    float startTime;
    float maxVel = 10.0f;

    private ExecutorService threadManager;

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

        startTime = System.nanoTime();

        for (int i=0; i<numSpheres; i++) {
            spheres[i] = new Sphere(0.1f * i, 0.0f, -0.5f, 0.05f);
            lengths[i] = 0.9f-i*0.02f;
        }
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
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        Matrix.setIdentityM(lightModelMatrix,0);
        Matrix.translateM(lightModelMatrix,0, lightPos[0], lightPos[1], lightPos[2]);
        Matrix.multiplyMV(lightPosInWorldSpace,0,lightModelMatrix,0, lightPosInModelSpace, 0);
        Matrix.multiplyMV(lightPosInEyeSpace, 0, viewMatrix, 0, lightPosInWorldSpace, 0);


        for (int i=0; i<numSpheres; i++){
            modelMatrix = new float[16];
            MVPMatrix = new float[16];
            MVMatrix = new float[16];

            Matrix.setIdentityM(modelMatrix,0);
            Matrix.translateM(modelMatrix, 0,spheres[i].getCenter()[0],spheres[i].getCenter()[1],lengths[i]-spheres[i].getCenter()[2]);
            Matrix.rotateM(modelMatrix,0,getNextAngle(currentTime, lengths[i]),1,0,0);
            Matrix.translateM(modelMatrix,0,-spheres[i].getCenter()[0],-spheres[i].getCenter()[1],-lengths[i]+spheres[i].getCenter()[2]);

            Matrix.multiplyMM(MVPMatrix, 0, viewMatrix, 0, modelMatrix, 0);
            MVMatrix=MVPMatrix;
            Matrix.multiplyMM(MVPMatrix,0,projectionMatrix,0,MVPMatrix,0);
            spheres[i].draw(MVMatrix,MVPMatrix,lightPosInEyeSpace,new float[]{0.9f,0.45f,0.9f,1.0f});

        }
    }

    private float getNextAngle(long currentTime, float length){
        float elapsedTime = (currentTime - startTime)/1000000000;
        double period = 2*PI*pow(length/g,0.5f);
        float velocity = (float) (maxVel*cos(elapsedTime*2*PI/period));
        return velocity*length;
    }


}
