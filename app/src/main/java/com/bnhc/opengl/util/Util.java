package com.bnhc.opengl.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by bnhc on 10/31/17.
 */

public class Util {

    public static FloatBuffer floatToBuffer(float[] verts) {
        ByteBuffer tmp = ByteBuffer.allocate(verts.length * 4);
        tmp.order(ByteOrder.nativeOrder());
        FloatBuffer buffer = tmp.asFloatBuffer();
        buffer.put(verts);
        buffer.position(0);
        return buffer;
    }

    /**
     * byte 2 int(The length:4)
     *
     * @param bytes byte[]
     * @param i     offset
     * @return int
     */
    public static int byte4ToInt(byte[] bytes, int i) {
        int b3 = bytes[i + 3] & 0xFF;
        int b2 = bytes[i + 2] & 0xFF;
        int b1 = bytes[i + 1] & 0xFF;
        int b0 = bytes[i + 0] & 0xFF;
        return (b3 << 24) | (b2 << 16) | (b1 << 8) | b0;
    }

    /**
     * byte 2 short(The length
     * @param bytes
     * @param i
     * @return
     */
    public static short byte2ToShort(byte[] bytes, int i) {
        int b1 = bytes[1 + i] & 0xFF;
        int b0 = bytes[0 + i] & 0xFF;
        return (short) ((b1 << 8) | b0);
    }

    public static float byte4ToFloat(byte[] bytes, int i) {
        return Float.intBitsToFloat(byte4ToInt(bytes, i));
    }
}
