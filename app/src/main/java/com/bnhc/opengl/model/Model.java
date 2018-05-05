package com.bnhc.opengl.model;



import com.bnhc.opengl.util.Util;

import java.nio.FloatBuffer;

/**
 * Created by bnhc on 10/31/17.
 */

public class Model {
    private int facetCount;
    private float[] verts;
    private float[] vnorms;
    private short[] remarks;

    //vers to Buffer
    private FloatBuffer verBuffer;
    private FloatBuffer vnormBuffer;
    //save the max int the x,y,z
    public float maxX, minX;
    public float maxY, minY;
    public float maxZ, minZ;

    // FIXME: 10/31/17
    public Point getCenterPoint() {
        float centorX = minX + (maxX - minX) / 2;
        float centorY = minY + (maxY - minY) / 2;
        float centorZ = minZ + (maxZ - maxZ) / 2;
        return new Point(centorX,  centorY,  centorZ);
    }

    public float getR() {
        float x = (maxX - minX);
        float y = (maxY - minY);
        float max = (maxZ - minZ);
        if (max < x) {
            max = x;
        }
        if (max < y) {
            max = y;
        }
        return max;
    }

    public void setVerts(float[] verts) {
        this.verts = verts;
        verBuffer = Util.floatToBuffer(verts);
    }

    public void setVnorms(float[] vnorms) {
        this.vnorms = vnorms;
        vnormBuffer = Util.floatToBuffer(vnorms);
    }

    public int getFacetCount() {
        return facetCount;
    }

    public void setFacetCount(int facetCount) {
        this.facetCount = facetCount;
    }

    public float[] getVerts() {
        return verts;
    }

    public float[] getVnorms() {
        return vnorms;
    }

    public short[] getRemarks() {
        return remarks;
    }

    public void setRemarks(short[] remarks) {
        this.remarks = remarks;
    }

    public FloatBuffer getVerBuffer() {
        return verBuffer;
    }

    public void setVerBuffer(FloatBuffer verBuffer) {
        this.verBuffer = verBuffer;
    }

    public FloatBuffer getVnormBuffer() {
        return vnormBuffer;
    }

    public void setVnormBuffer(FloatBuffer vnormBuffer) {
        this.vnormBuffer = vnormBuffer;
    }
}
