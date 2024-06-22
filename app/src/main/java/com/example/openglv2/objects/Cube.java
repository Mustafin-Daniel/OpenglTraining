package com.example.openglv2.objects;

import android.opengl.GLES20;

import com.example.openglv2.MyGLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL;

public class Cube {
    private float[] coords;
    private float[] normals;

    private FloatBuffer vertexBuffer;
    private FloatBuffer normalBuffer;

    private int NUM_OF_COORDS = 3;
    private int mProgram;

    public Cube(float cx, float cy, float cz, float len){
        coords = new float[]{
                // In the YZ plane
                cx + len / 2, cy + len / 2, cz + len / 2,
                cx + len / 2, cy - len / 2, cz + len / 2,
                cx + len / 2, cy - len / 2, cz - len / 2,
                cx + len / 2, cy + len / 2, cz + len / 2,
                cx + len / 2, cy + len / 2, cz - len / 2,
                cx + len / 2, cy - len / 2, cz - len / 2,
                // In the XY plane
                cx + len / 2, cy + len / 2, cz + len / 2,
                cx + len / 2, cy - len / 2, cz + len / 2,
                cx - len / 2, cy - len / 2, cz + len / 2,
                cx + len / 2, cy + len / 2, cz + len / 2,
                cx - len / 2, cy + len / 2, cz + len / 2,
                cx - len / 2, cy - len / 2, cz + len / 2,
                // In the XZ plane
                cx + len / 2, cy + len / 2, cz + len / 2,
                cx - len / 2, cy + len / 2, cz + len / 2,
                cx - len / 2, cy + len / 2, cz - len / 2,
                cx + len / 2, cy + len / 2, cz + len / 2,
                cx + len / 2, cy + len / 2, cz - len / 2,
                cx - len / 2, cy + len / 2, cz - len / 2,
                // In the YZ plane (this time x is -)
                cx - len / 2, cy + len / 2, cz + len / 2,
                cx - len / 2, cy - len / 2, cz + len / 2,
                cx - len / 2, cy - len / 2, cz - len / 2,
                cx - len / 2, cy + len / 2, cz + len / 2,
                cx - len / 2, cy + len / 2, cz - len / 2,
                cx - len / 2, cy - len / 2, cz - len / 2,
                // In the XY plane (this time z is -)
                cx + len / 2, cy + len / 2, cz - len / 2,
                cx + len / 2, cy - len / 2, cz - len / 2,
                cx - len / 2, cy - len / 2, cz - len / 2,
                cx + len / 2, cy + len / 2, cz - len / 2,
                cx - len / 2, cy + len / 2, cz - len / 2,
                cx - len / 2, cy - len / 2, cz - len / 2,
                // In the XZ plane (this time y is -)
                cx + len / 2, cy - len / 2, cz + len / 2,
                cx - len / 2, cy - len / 2, cz + len / 2,
                cx - len / 2, cy - len / 2, cz - len / 2,
                cx + len / 2, cy - len / 2, cz + len / 2,
                cx + len / 2, cy - len / 2, cz - len / 2,
                cx - len / 2, cy - len / 2, cz - len / 2,
        };

        normals = new float[]{
                1.0f,0.0f,0.0f,   1.0f,0.0f,0.0f,   1.0f,0.0f,0.0f,   1.0f,0.0f,0.0f,   1.0f,0.0f,0.0f,   1.0f,0.0f,0.0f,
                0.0f,0.0f,1.0f,   0.0f,0.0f,1.0f,   0.0f,0.0f,1.0f,   0.0f,0.0f,1.0f,   0.0f,0.0f,1.0f,   0.0f,0.0f,1.0f,
                0.0f,1.0f,0.0f,   0.0f,1.0f,0.0f,   0.0f,1.0f,0.0f,   0.0f,1.0f,0.0f,   0.0f,1.0f,0.0f,   0.0f,1.0f,0.0f,
                -1.0f,0.0f,0.0f,  -1.0f,0.0f,0.0f,  -1.0f,0.0f,0.0f,  -1.0f,0.0f,0.0f,  -1.0f,0.0f,0.0f,  -1.0f,0.0f,0.0f,
                0.0f,0.0f,-1.0f,  0.0f,0.0f,-1.0f,  0.0f,0.0f,-1.0f,  0.0f,0.0f,-1.0f,  0.0f,0.0f,-1.0f,  0.0f,0.0f,-1.0f,
                0.0f,-1.0f,0.0f,  0.0f,-1.0f,0.0f,  0.0f,-1.0f,0.0f,  0.0f,-1.0f,0.0f,  0.0f,-1.0f,0.0f,  0.0f,-1.0f,0.0f
        };


        vertexBuffer = ByteBuffer.allocateDirect(coords.length*4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertexBuffer.put(coords).position(0);

        normalBuffer = ByteBuffer.allocateDirect(normals.length*4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        normalBuffer.put(normals).position(0);

        final int vertexShaderHandle = MyGLSurfaceView.loadShader(GLES20.GL_VERTEX_SHADER,vertexShader);
        final int fragmentShaderHandle = MyGLSurfaceView.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader);

        mProgram = GLES20.glCreateProgram();

        GLES20.glAttachShader(mProgram, vertexShaderHandle);
        GLES20.glAttachShader(mProgram, fragmentShaderHandle);

        String[] attributes = {"a_Position", "a_Normal"};
        for (int i=0; i<attributes.length; i++)
            GLES20.glBindAttribLocation(mProgram, i, attributes[i]);

        GLES20.glLinkProgram(mProgram);
        GLES20.glEnable(mProgram);
    }

    private int MVPMatrixHandle;
    private int MVMatrixHandle;
    private int lightPosHandle;
    private int positionHandle;
    private int colorHandle;
    private int normalHandle;
    public void draw(float[] mvMatrix, float[] mvpMatrix, float[] lightPosInEyeSpace){
        GLES20.glUseProgram(mProgram);

        MVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "u_MVPMatrix");
        MVMatrixHandle = GLES20.glGetUniformLocation(mProgram, "u_MVMatrix");
        lightPosHandle = GLES20.glGetUniformLocation(mProgram, "u_LightPos");
        positionHandle = GLES20.glGetAttribLocation(mProgram, "a_Position");
        colorHandle = GLES20.glGetUniformLocation(mProgram, "a_Color");
        normalHandle = GLES20.glGetAttribLocation(mProgram, "a_Normal");

        GLES20.glVertexAttribPointer(positionHandle,NUM_OF_COORDS,GLES20.GL_FLOAT,false,0,vertexBuffer);
        GLES20.glEnableVertexAttribArray(positionHandle);

        GLES20.glVertexAttribPointer(normalHandle,NUM_OF_COORDS, GLES20.GL_FLOAT,false,0,normalBuffer);
        GLES20.glEnableVertexAttribArray(normalHandle);

        GLES20.glUniformMatrix4fv(MVMatrixHandle,1,false,mvMatrix,0);
        GLES20.glUniformMatrix4fv(MVPMatrixHandle,1,false,mvpMatrix,0);
        GLES20.glUniform3f(lightPosHandle,lightPosInEyeSpace[0],lightPosInEyeSpace[1],lightPosInEyeSpace[2]);
        float color[] = {1.0f,0.2f,0.2f,1.0f};
        GLES20.glUniform4f(colorHandle,color[0],color[1],color[2],color[3]);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,coords.length/NUM_OF_COORDS);
    }

    private final String vertexShader =
            "uniform mat4 u_MVPMatrix;      \n"		// A constant representing the combined model/view/projection matrix.
            + "uniform mat4 u_MVMatrix;       \n"		// A constant representing the combined model/view matrix.
            + "uniform vec3 u_LightPos;       \n"	    // The position of the light in eye space.

            + "attribute vec4 a_Position;     \n"		// Per-vertex position information we will pass in.
            + "uniform vec4 a_Color;        \n"		// Per-vertex color information we will pass in.
            + "attribute vec3 a_Normal;       \n"		// Per-vertex normal information we will pass in.

            + "varying vec4 v_Color;          \n"		// This will be passed into the fragment shader.

            + "void main()                    \n" 	// The entry point for our vertex shader.
            + "{                              \n"
            // Transform the vertex into eye space.
            + "   vec3 modelViewVertex = vec3(u_MVMatrix * a_Position);              \n"
            // Transform the normal's orientation into eye space.
            + "   vec3 modelViewNormal = vec3(u_MVMatrix * vec4(a_Normal, 0.0));     \n"
            // Will be used for attenuation.
            + "   float distance = length(u_LightPos - modelViewVertex);             \n"
            // Get a lighting direction vector from the light to the vertex.
            + "   vec3 lightVector = normalize(u_LightPos - modelViewVertex);        \n"
            // Calculate the dot product of the light vector and vertex normal. If the normal and light vector are
            // pointing in the same direction then it will get max illumination.
            + "   float diffuse = max(dot(modelViewNormal, lightVector), 0.1);       \n"
            // Attenuate the light based on distance.
            + "   diffuse = diffuse * (1.0 / (1.0 + (0.25 * distance * distance)));  \n"

            // Multiply the color by the illumination level. It will be interpolated across the triangle.
            + "   v_Color = a_Color * diffuse;                                       \n"
            // gl_Position is a special variable used to store the final position.
            // Multiply the vertex by the matrix to get the final point in normalized screen coordinates.
            + "   gl_Position = u_MVPMatrix * a_Position;                            \n"
            + "}                                                                     \n";

    private final String fragmentShader =
            "precision mediump float;       \n"		// Set the default precision to medium. We don't need as high of a
                    // precision in the fragment shader.
                    + "varying vec4 v_Color;          \n"		// This is the color from the vertex shader interpolated across the
                    // triangle per fragment.
                    + "void main()                    \n"		// The entry point for our fragment shader.
                    + "{                              \n"
                    + "   gl_FragColor = v_Color;     \n"		// Pass the color directly through the pipeline.
                    + "}                              \n";

}
