package com.bnhc.opengl.renderer;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;

import com.bnhc.opengl.model.Model;
import com.bnhc.opengl.model.Point;
import com.bnhc.opengl.util.STLReader;


import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by bnhc on 11/2/17.
 */

public class GLRenderer implements GLSurfaceView.Renderer {
    private Model model;
    private Point mCenterPoint;
    private Point eye = new Point(0, 0, -3);
    private Point up = new Point(0, 1, 0);
    private Point center = new Point(0, 0, 0);
    private float mScalef = 1;
    private float mDegree = 0;

    public GLRenderer(Context context)  {
        model = new STLReader().pareBinStlInAsset(context, "test.stl");
        if(model==null) return;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glClearDepthf(1.0f);
        gl.glDepthFunc(GL10.GL_LEQUAL);
        gl.glShadeModel(GL10.GL_SMOOTH);
        float r = model.getR();
        mScalef = 0.5f/r;
        mCenterPoint = model.getCenterPoint();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0,0,width,height);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        GLU.gluPerspective(gl,45.0f,width/height,1f,100f);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT|GL10.GL_DEPTH_BITS);
        gl.glLoadIdentity();
        GLU.gluLookAt(gl,eye.x,eye.y,eye.z,center.x,center.y,center.z,up.z,up.y,up.z);
        gl.glScalef(mScalef,mScalef,mScalef);
        gl.glTranslatef(-mCenterPoint.x,-mCenterPoint.y,-mCenterPoint.z);

        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glNormalPointer(GL10.GL_FLOAT,0,model.getVnormBuffer());
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
    }

    public void rotate(float degree){
        this.mDegree = degree;
    }
}
