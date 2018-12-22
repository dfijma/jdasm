package net.fijma.classfile.attribute;

import net.fijma.classfile.ClassFileException;
import net.fijma.classfile.constantpool.ConstantPool;

import java.io.IOException;
import java.io.InputStream;

public class LocalVariableTable extends Attribute {

    // TODO
    public LocalVariableTable(ConstantPool pool) {
        super(pool);
    }

    public static LocalVariableTable read(ConstantPool pool, int attributeLength, InputStream is) throws ClassFileException {
        try {
            byte[] info = new byte[attributeLength];
            int rc = is.read(info);
            if (rc != attributeLength) throw new ClassFileException("not enouhg data");
        } catch (IOException e) {
            throw new ClassFileException("error reading lInenumbertable");
        }
        return new LocalVariableTable(pool);
    }

    @Override
    public String toString() {
        return "aLocalVariableTable";
    }
}
