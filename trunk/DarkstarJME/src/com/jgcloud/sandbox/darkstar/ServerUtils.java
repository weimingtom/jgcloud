package com.jgcloud.sandbox.darkstar;

import com.jgcloud.sandbox.jme.PlayerDetails;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import java.nio.ByteBuffer;

public class ServerUtils {

    public static void main(String[] args) throws Exception {
        ByteBuffer bb = encodePlayerDetailsByteBufferMessage(new PlayerDetails("Richard Hawkes", new Vector3f(1f,2f,3f), new Quaternion(1f,2f,3f,4f), System.currentTimeMillis()));
        System.out.println(bb.array().length);
    }

    public static ByteBuffer vector3fAsByteBuffer(Vector3f v3f) {
        ByteBuffer bb = ByteBuffer.allocate(12);
        bb.putFloat(v3f.x);
        bb.putFloat(v3f.y);
        bb.putFloat(v3f.z);
        bb.rewind();
        return bb;
    }


    public static ByteBuffer quaternionAsByteBuffer(Quaternion q) {
        ByteBuffer bb = ByteBuffer.allocate(16);
        bb.putFloat(q.x);
        bb.putFloat(q.y);
        bb.putFloat(q.z);
        bb.putFloat(q.w);
        bb.rewind();
        return bb;
    }

    public static ByteBuffer encodePlayerDetailsByteBufferMessage(PlayerDetails pd) throws Exception {
        byte[] playerNameAsByteArray = pd.getPlayerName().getBytes(DarkstarConstants.MESSAGE_CHARSET);
        byte playerNameAsByteArraySize = (byte)playerNameAsByteArray.length;

        ByteBuffer bb = ByteBuffer.allocate(37 + playerNameAsByteArray.length);
        bb.put(playerNameAsByteArraySize); // 1
        bb.put(playerNameAsByteArray); // ? bytes (depends on player name length)
        bb.putLong(pd.getLastUpdateTimeMillis()); // 8 bytes
        bb.put(vector3fAsByteBuffer(pd.getLocation())); // 12 bytes
        bb.put(quaternionAsByteBuffer(pd.getRotation())); // 16 bytes

        bb.rewind();
        return bb;
    }

    private PlayerDetails decodePlayerDetailsByteBufferMessage(ByteBuffer message) {
        message.rewind(); // Not necessary, but just in case.

        // Because the player name string is an unknown length, the first byte
        // contains the length of the string. From that we create a byte array
        // and store the characters. Finally, we can create a string from this
        // byte array !
        byte playerNameLength = message.get(); // Get the length of the playerName string.
        byte[] playerNameByteArray = new byte[playerNameLength]; // Create an array of the size of the playerName string.
        message.get(playerNameByteArray); // Load the characters in to the byte array.
        String playerName = new String(playerNameByteArray);

        long time = message.getLong();
        Vector3f location = new Vector3f(message.getFloat(), message.getFloat(), message.getFloat());
        Quaternion rotation = new Quaternion(message.getFloat(), message.getFloat(), message.getFloat(), message.getFloat());

        PlayerDetails pd = new PlayerDetails(playerName, location, rotation, time);

        return pd;
    }
}
