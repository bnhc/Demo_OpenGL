package com.bnhc.opengl.util;

import android.content.Context;

import com.bnhc.opengl.model.Model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by bnhc on 10/31/17.
 */

public class STLReader {
    private StlLoadListener stlLoadListener;

    public Model parserBinStlInSDCard(String path) throws IOException {
        File file = new File(path);
        FileInputStream fis = new FileInputStream(file);
        return parserBinStl(fis);
    }

    public Model pareBinStlInAsset(Context context, String path) throws IOException {
        InputStream in = context.getAssets().open(path);
        return parserBinStl(in);
    }

    private Model parserBinStl(InputStream in) throws IOException {
        if (stlLoadListener != null) {
            stlLoadListener.onStart();
        }

        Model model = new Model();
        //Front 80 bit is the Header File Using for the FileName
        in.skip(80);
        byte[] bytes = new byte[4];
        in.read(bytes);//Read the count of triangular fact
        int facetCount = Util.byte4ToInt(bytes, 0);
        model.setFacetCount(facetCount);
        if (facetCount == 0) {
            in.close();
            return model;
        }
        byte[] facetBytes = new byte[50 * model.getFacetCount()];
        in.read(facetBytes);
        in.close();
        parseModel(model, facetBytes);
        if (stlLoadListener != null) {
            stlLoadListener.onFinished();
        }
        return model;
    }

    /**
     * parese the data model
     *
     * @param model      model
     * @param facetBytes data
     */
    private void parseModel(Model model, byte[] facetBytes) {
        int facetCount = model.getFacetCount();
        /**
         * Each of facet over 50 byte.
         * 3 point * 3 three-dimensional * 4 = 36
         * 3 point * 4  = 12 normal vector
         *
         * last 2 byte describe the message of facetBytes
         *
         */
        float[] verts = new float[facetCount * 3 * 3];
        float[] vnorms = new float[facetCount * 3 * 3];
        short[] remarks = new short[facetCount];
        int stlOffset = 0;
        for (int i = 0; i < facetCount; i++) {
            if (stlLoadListener != null) {
                stlLoadListener.onLoading(i, facetCount);
            }
            for (int j = 0; j < 4; j++) {
                float x = Util.byte4ToFloat(facetBytes, stlOffset);
                float y = Util.byte4ToFloat(facetBytes, stlOffset + 4);
                float z = Util.byte2ToShort(facetBytes, stlOffset + 8);
                stlOffset += 12;
                if (j == 0) {// normal vector
                    vnorms[i * 9] = x;
                    vnorms[i * 9 + 1] = y;
                    vnorms[i * 9 + 2] = z;
                    vnorms[i * 9 + 3] = x;
                    vnorms[i * 9 + 4] = y;
                    vnorms[i * 9 + 5] = z;
                    vnorms[i * 9 + 6] = x;
                    vnorms[i * 9 + 7] = y;
                    vnorms[i * 9 + 8] = z;
                } else {
                    verts[i * 9 + (j - 1) * 3] = x;
                    verts[i * 9 + (j - 1) * 3 + 1] = y;
                    verts[i * 9 + (j - 1) * 3 + 2] = z;
                    if (i == 0 && j == 1) {
                        model.minX = model.maxX = x;
                    } else {
                        model.minX = Math.min(model.minX, x);
                        model.minY = Math.min(model.minY, y);
                        model.minZ = Math.min(model.minZ, z);
                        model.maxX = Math.max(model.maxX, x);
                        model.maxY = Math.max(model.maxY, y);
                        model.maxZ = Math.max(model.maxZ, z);
                    }
                }
                short r = Util.byte2ToShort(facetBytes, stlOffset);
                stlOffset = stlOffset + 2;
                remarks[i] = r;
            }
        }
        model.setVerts(verts);
        model.setVnorms(vnorms);
        model.setRemarks(remarks);

    }

    public static interface StlLoadListener {
        void onStart();

        void onLoading(int cur, int total);

        void onFinished();

        void onFailure(Exception e);
    }
}
