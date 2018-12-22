package net.fijma;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit test for simple JDAsm.
 */
public class AppTest 
{


    @Test
    public void testCast() {
        long x = 0x7FFF_F8FF_FFFF_FFFFL;
        int y = (int)x;
        System.out.println(y);
    }

    @Test
    public void testNeq() {
        int x = 40;
        byte[] xs = new byte[x];
    }

    @Test
    public void testShort() {
        byte b1 = (byte) 0xFF;
        byte b2 = (byte) 0XFF;

        short s = (short) ((b1 << 8) | b2);

        System.out.println(s); // -1;

        int i = s;

        System.out.println(i); // -1?


    }
}
