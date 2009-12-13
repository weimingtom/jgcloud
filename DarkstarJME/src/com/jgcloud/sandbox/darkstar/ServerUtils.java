package com.jgcloud.sandbox.darkstar;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import java.nio.ByteBuffer;

public class ServerUtils {

    public static void main(String[] args) throws Exception {
        ByteBuffer bb = createPlayerLocationByteBufferMessage("Richard Hawkes", System.currentTimeMillis(), new Vector3f(1f,2f,3f), new Quaternion(1f,2f,3f,4f));
        System.out.println(bb.array().length);
    }

    public static byte[] vector3fAsByteBuffer(Vector3f v3f) {
        ByteBuffer bb = ByteBuffer.allocate(12);
        bb.putFloat(v3f.x);
        bb.putFloat(v3f.y);
        bb.putFloat(v3f.z);
        System.out.println("vsize=" + bb.array().length);
        return bb.array();
    }

    /**
     * PROBLEM: I'd like to return the ByteBuffer instance, but it seems that
     * you can't "put" this in to another ByteBuffer without converting it back
     * to a byte[] first. ie the put(ByteBufer bb) isn't working properly??
     * 
     * @param q
     * @return
     */
    public static byte[] quaternionAsByteBuffer(Quaternion q) {
        ByteBuffer bb = ByteBuffer.allocate(16);
        bb.putFloat(q.x);
        bb.putFloat(q.y);
        bb.putFloat(q.z);
        bb.putFloat(q.w);
        System.out.println("qsize=" + bb.array().length);
        return bb.array();
    }

    public static ByteBuffer createPlayerLocationByteBufferMessage(String playerName, long time, Vector3f location, Quaternion rotation) throws Exception {
        byte[] playerNameAsByteArray = playerName.getBytes(DarkstarConstants.MESSAGE_CHARSET);
        byte playerNameAsByteArraySize = (byte)playerNameAsByteArray.length;

        ByteBuffer bb = ByteBuffer.allocate(37 + playerNameAsByteArray.length);
        bb.put(playerNameAsByteArraySize); // 1
        System.out.println("1 " + bb.position());
        bb.put(playerNameAsByteArray); // ?
        System.out.println("2 " + bb.position());
        bb.putLong(time); // 8
        System.out.println("3 " + bb.position());
        bb.put(vector3fAsByteBuffer(location)); // 12
        System.out.println("4 " + bb.position());
        bb.put(quaternionAsByteBuffer(rotation)); // 16
        System.out.println("5 " + bb.position());

        return bb;
    }
}
