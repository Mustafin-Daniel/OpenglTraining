package com.example.openglv2;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.example.openglv2.glrenderers.TwoDPendulumRenderer;
import com.example.openglv2.glrenderers.SolarSystemRenderer;
import com.example.openglv2.glrenderers.SpinningCubeRenderer;
import com.example.openglv2.glrenderers.WavePendulum;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyGLSurfaceView extends GLSurfaceView {
    private final GLSurfaceView.Renderer renderer;
    public enum Function {
            SPINNING_CUBE,
            SOLAR_SYSTEM,
            PENDULUM,
            PENDULUM_WAVE
    };
    public MyGLSurfaceView(Context context, Function function) {
        super(context);

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);


        switch (function){
            case SPINNING_CUBE:
                renderer = new SpinningCubeRenderer();
                setRenderer(renderer);

            break;
            case SOLAR_SYSTEM:
                renderer = new SolarSystemRenderer();
                setRenderer(renderer);
                break;
            case PENDULUM:
                renderer = new TwoDPendulumRenderer();
                setRenderer(renderer);
                break;
            case PENDULUM_WAVE:
                renderer = new WavePendulum();
                setRenderer(renderer);
                break;
            default:
                renderer = new Renderer() {
                    @Override
                    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {}
                    @Override
                    public void onSurfaceChanged(GL10 gl10, int i, int i1) {}
                    @Override
                    public void onDrawFrame(GL10 gl10) {}
                };
                break;
        }

        //renderer = new SolarSystemRenderer();



        // Set the Renderer for drawing on the GLSurfaceView
        //setRenderer(renderer);
    }




    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        final int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

        // If the compilation failed, delete the shader.
        if (compileStatus[0] == 0)
        {
            GLES20.glDeleteShader(shader);
            shader = 0;
            throw new RuntimeException("Error creating shader.");
        }

        return shader;
    }
}
