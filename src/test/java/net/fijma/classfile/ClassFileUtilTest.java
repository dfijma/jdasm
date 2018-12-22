package net.fijma.classfile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ClassFileUtilTest {

    @Test
    public void testByte() {
        byte[] test     = new byte[] {  -1, 127, -128, 0, 42, -42};
        long[] signed   = new long[] {  -1, 127, -128, 0, 42, -42};
        long[] unsigned = new long[] { 255, 127,  128, 0, 42, 214};

        for (int i=0; i<test.length; ++i) {
            assertEquals(signed[i], ClassFileUtil.signedByte(test, i));
            assertEquals(unsigned[i], ClassFileUtil.unsignedNumber(test, i, 1));
        }
    }

    @Test
    public void testShort() {
        assertEquals(65535, ClassFileUtil.unsignedNumber(new byte[] { -1, -1 }, 0, 2));
        assertEquals(32768, ClassFileUtil.unsignedNumber(new byte [] { -128, 0} , 0, 2));
        assertEquals(4242, ClassFileUtil.unsignedNumber(new byte[] { 16 , -110 }, 0, 2));
        assertEquals(0, ClassFileUtil.unsignedNumber(new byte[] { 0, 0 }, 0, 2));
        assertEquals(-1, ClassFileUtil.signedShort(new byte[] { -1, -1 }, 0));
        assertEquals(-32768, ClassFileUtil.signedShort(new byte [] { -128, 0} , 0));
        assertEquals(4242, ClassFileUtil.signedShort(new byte[] { 16 , -110 }, 0));
        assertEquals(0, ClassFileUtil.signedShort(new byte[] { 0, 0 }, 0));
    }

    @Test
    public void testInt() {
        byte[][] intTest = new byte[][] {{ -1, -1, -1, -1}, {127, -1, -1, -1}};
        long[] signed    = new long[]   { -1L             , 2147483647 };
        long[] unsigned  = new long[]   { 4294967295L     , 2147483647 };
        for (int i=0; i<intTest.length; ++i) {
            assertEquals(unsigned[i], ClassFileUtil.unsignedNumber(intTest[i], 0, 4));
            assertEquals(signed[i], ClassFileUtil.signedInt(intTest[i], 0));
        }
    }
}
