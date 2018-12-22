package net.fijma.classfile.attribute;

import net.fijma.classfile.ClassFileException;
import net.fijma.classfile.constantpool.ConstantPool;

import java.io.IOException;
import java.io.InputStream;

public class LineNumberTable extends Attribute {

    // TODO
    public LineNumberTable(ConstantPool pool) {
        super(pool);
    }

    public static LineNumberTable read(ConstantPool pool, int attributeLength, InputStream is) throws ClassFileException {
        try {
            byte[] info = new byte[attributeLength];
            int rc = is.read(info);
            if (rc != attributeLength) throw new ClassFileException("not enouhg data");
        } catch (IOException e) {
            throw new ClassFileException("error reading lInenumbertable");
        }
        return new LineNumberTable(pool);
    }

    @Override
    public String toString() {
        return "aLineNumberTable";
    }


}
