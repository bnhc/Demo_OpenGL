package com.bnhc.opengl;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "OpenGL";
    private boolean supportEs2;
    private GLSurfaceView glSurfaceView;

    @SuppressLint("ShowToast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkSupportGL();
        if (supportEs2) {
            glSurfaceView = new GLSurfaceView(getApplicationContext());
            glSurfaceView.setRenderer(new MyGLRenderer());
            setContentView(glSurfaceView);
        } else {
            setContentView(R.layout.activity_main);
            Toast.makeText(getApplication(), "This Device Not Support OpenGL", Toast.LENGTH_SHORT);
        }
    }

    /**
     * Check the device support the OpenGL
     */
    private void checkSupportGL() {
        ActivityManager activityMananger = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        assert activityMananger != null;
        ConfigurationInfo configuration = activityMananger.getDeviceConfigurationInfo();
        Log.d(TAG, "[onCreate()][checkSupportGL()]" + configuration.reqGlEsVersion);
        supportEs2 = configuration.reqGlEsVersion >= 0x2000;
        boolean isEmulator = Build.FINGERPRINT.startsWith("generic") || Build.FINGERPRINT.startsWith("unknow") || Build.MODEL.contains("google_sdk") || Build.MODEL.contains("Emulator") || Build.MODEL.contains("Android SDK build for x86");
        supportEs2 = supportEs2 || isEmulator;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (glSurfaceView != null) glSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (glSurfaceView != null) glSurfaceView.onPause();
    }

    class MyGLRenderer implements GLSurfaceView.Renderer {

        //Let Show Triangle
        private float[] mTriangleArray = {
                0f, 1f, 0f,
                -1f, -1f, 0f,
                1f, -1f, 0f
        };
        //Color
        private float[] mColor = new float[]{
                1, 1, 0, 1,
                0, 1, 1, 1,
                1, 0, 1, 1
        };
        

        //We need NIOBuffer,use in Direct Memory
        private FloatBuffer mTriangleBuffer;
        private FloatBuffer mColorBuffer;

        public MyGLRenderer() {
            //Init Buffer  1 Float = 4 Byte = 32Bit
            ByteBuffer bb = ByteBuffer.allocateDirect(mTriangleArray.length * 4);
            bb.order(ByteOrder.nativeOrder());
            mTriangleBuffer = bb.asFloatBuffer();
            mTriangleBuffer.put(mTriangleArray);
            mTriangleBuffer.position(0);

            //Color
            ByteBuffer bb2 = ByteBuffer.allocateDirect(mColor.length * 4);
            bb2.order(ByteOrder.nativeOrder());
            mColorBuffer = bb2.asFloatBuffer();
            mColorBuffer.put(mColor);
            mColorBuffer.position(0);
        }

        @Override
        public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
            gl10.glClearColor(1f, 1f, 1f, 1f);
        }

        @Override
        public void onSurfaceChanged(GL10 gl10, int i, int i1) {
            float ratio = (float) i / i1;
            gl10.glViewport(0, 0, i, i1);
            gl10.glMatrixMode(GL10.GL_PROJECTION);
            gl10.glLoadIdentity();
            gl10.glFrustumf(-ratio, ratio, -1, 1, 1, 10);
            gl10.glMatrixMode(GL10.GL_MODELVIEW);
            gl10.glLoadIdentity();
        }

        @Override
        public void onDrawFrame(GL10 gl10) {
            gl10.glClear(GL10.GL_COLOR_BUFFER_BIT);
            gl10.glLoadIdentity();

            gl10.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            gl10.glEnableClientState(GL10.GL_COLOR_ARRAY);

            gl10.glTranslatef(0f, 0.0f, -2.0f);
            gl10.glVertexPointer(3, GL10.GL_FLOAT, 0, mTriangleBuffer);
            gl10.glColorPointer(4, GL10.GL_FLOAT, 0, mColorBuffer);
            gl10.glDrawArrays(GL10.GL_TRIANGLES, 0, 3);

            gl10.glDisableClientState(GL10.GL_COLOR_ARRAY);
            gl10.glDisableClientState(GL10.GL_VERTEX_ARRAY);
            gl10.glFinish();
        }
    }
}
