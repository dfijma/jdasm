package net.fijma.classfile;

import java.io.IOException;
import java.io.InputStream;

public class ClassFileUtil {

    public static byte[] readBytes(InputStream is, int c) throws ClassFileException {
        var res = new byte[c];
        try {
            var rc = is.read(res);
            if (rc < c) throw new ClassFileException("unexpected EOF");
        } catch (IOException e) {
            throw new ClassFileException("error reading file", e);
        }
        return res;
    }

    public static long readSizedInteger(InputStream is, int c) throws ClassFileException {
        byte[] bytes = readBytes(is, c);
        return unsignedNumber(bytes, 0, bytes.length);
    }

    public static long unsignedNumber(byte[] data, int pos, int c) {
        var res = 0L;
        for (var i=0; i<c; ++i) {
            res = (res << 8) + (data[pos+i] & 0xFF);
        }
        return res;
    }

    public static byte signedByte(byte[] data, int pos) {
        // get igned byte
        return data[pos];
    }

    public static short signedShort(byte[] data, int pos) {
        return (short) unsignedNumber(data, pos, 2);
    }

    public static int signedInt(byte[] data, int pos) {
        return (int) unsignedNumber(data, pos, 4);
    }
}
