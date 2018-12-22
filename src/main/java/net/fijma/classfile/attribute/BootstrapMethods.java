package net.fijma.classfile.attribute;

import net.fijma.classfile.ClassFileException;
import net.fijma.classfile.constantpool.ConstantPool;

import java.io.IOException;
import java.io.InputStream;

public class BootstrapMethods extends Attribute {

    // TODO
    public BootstrapMethods(ConstantPool pool) {
        super(pool);
    }

    public static BootstrapMethods read(ConstantPool pool, int attributeLength, InputStream is) throws ClassFileException {
        try {
            byte[] info = new byte[attributeLength];
            int rc = is.read(info);
            if (rc != attributeLength) throw new ClassFileException("not enouhg data");
        } catch (IOException e) {
            throw new ClassFileException("error reading lInenumbertable");
        }
        return new BootstrapMethods(pool);
    }

    @Override
    public String toString() {
        return "aBootstrapMethods";
    }
}
