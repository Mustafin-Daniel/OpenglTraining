package com.example.openglv2;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private GLSurfaceView gLView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activateList();
    }

    private void activateList(){
        setContentView(R.layout.project_list);
        Button spinningCubeBtn, solarSystemBtn, pendulumBtn, pendulumWaveBtn;
        spinningCubeBtn=findViewById(R.id.spinningcubebtn);
        solarSystemBtn=findViewById(R.id.solarsystembtn);
        pendulumBtn=findViewById(R.id.pendulumbtn);
        pendulumWaveBtn=findViewById(R.id.pendulumwavebtn);
        OnBackPressedCallback backPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                activateList();
            }
        };
        getOnBackPressedDispatcher().addCallback(MainActivity.this, backPressedCallback);

        spinningCubeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gLView = new MyGLSurfaceView(MainActivity.this,MyGLSurfaceView.Function.SPINNING_CUBE);
                setContentView(gLView);
            }
        });
        solarSystemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gLView = new MyGLSurfaceView(MainActivity.this, MyGLSurfaceView.Function.SOLAR_SYSTEM);
                setContentView(gLView);
            }
        });
        pendulumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gLView = new MyGLSurfaceView(MainActivity.this, MyGLSurfaceView.Function.PENDULUM);
                setContentView(gLView);
            }
        });
        pendulumWaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gLView = new MyGLSurfaceView(MainActivity.this, MyGLSurfaceView.Function.PENDULUM_WAVE);
                setContentView(gLView);
            }
        });

    }
}