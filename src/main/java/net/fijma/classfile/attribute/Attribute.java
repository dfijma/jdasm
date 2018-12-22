package net.fijma.classfile.attribute;

import net.fijma.classfile.ClassFileException;
import net.fijma.classfile.constantpool.ConstantPool;
import net.fijma.classfile.constantpool.Utf8Info;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import static net.fijma.classfile.ClassFileUtil.readSizedInteger;

public class Attribute {

    public final ConstantPool pool;

    Attribute(ConstantPool pool) {
        this.pool = pool;
    }

    public static Attribute read(ConstantPool pool, Utf8Info name, int attributeLength, InputStream is) throws ClassFileException {
        switch (name.value) {
            case "Code":
                return Code.read(pool, attributeLength, is);
            case "SourceFile":
                if (attributeLength != 2) throw new ClassFileException("attributelength of SourceFile should be 2");
                Utf8Info sourcefile = pool.get((int) readSizedInteger(is, 2), Utf8Info.class);
                return new SourceFile(pool, sourcefile);
            case "LineNumberTable":
                return LineNumberTable.read(pool, attributeLength, is);
            case "Signature" :
                return Signature.read(pool, attributeLength, is);
            case "LocalVariableTable":
                return LocalVariableTable.read(pool, attributeLength, is);
            case "LocalVariableTypeTable":
                return LocalVariableTypeTable.read(pool, attributeLength, is);
            case "StackMapTable":
                return StackMapTable.read(pool, attributeLength, is);
            case "Exceptions":
                return Exceptions.read(pool, attributeLength, is);
            case "NestMembers":
                return NestMembers.read(pool, attributeLength, is);
            case "InnerClasses":
                return InnerClasses.read(pool, attributeLength, is);
            case "BootstrapMethods":
                return BootstrapMethods.read(pool, attributeLength, is);
            default:
                throw new ClassFileException("not implemented attribute name " + name.value);
        }
    }

    public static Set<Attribute> readAttributes(ConstantPool pool, InputStream is) throws ClassFileException {
        int attributes = (int) readSizedInteger(is, 2);
        Set<Attribute> res = new HashSet<>();
        for (int a=0; a<attributes; ++a) {
            int attributeNameIndex = (int) readSizedInteger(is, 2);
            int attributeLength = (int) readSizedInteger(is, 4);
            Utf8Info name = pool.get(attributeNameIndex, Utf8Info.class);
            res.add(Attribute.read(pool, name, attributeLength, is));
        }
        return res;
    }

}
