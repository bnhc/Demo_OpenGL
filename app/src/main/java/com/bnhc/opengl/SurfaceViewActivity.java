package com.bnhc.opengl;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by bnhc on 18-5-5.
 * SurfaceView
 */

public class SurfaceViewActivity extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    private class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

        public MySurfaceView(Context context) {
            super(context);
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {

        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }

        @Override
        public void run() {

        }
    }
}
