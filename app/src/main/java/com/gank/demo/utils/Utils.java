package com.gank.demo.utils;

import android.view.View;

import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * Created by GankLi on 2016/7/4.
 */

public class Utils {

    public static byte[] inttobyte(int paramInt) {
        byte[] arrayOfByte = new byte[2];
        arrayOfByte[0] = (byte) (0xFF & paramInt >> 8);
        arrayOfByte[1] = (byte) (paramInt & 0xFF);
        return arrayOfByte;
    }

    public static byte[] uuidToByte(UUID uuid) {
        ByteBuffer buf = ByteBuffer.allocate(16);
        buf.putLong(uuid.getMostSignificantBits());
        buf.putLong(uuid.getLeastSignificantBits());
        return (byte[]) buf.array().clone();
    }
}
