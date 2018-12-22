package net.fijma.classfile.constantpool;

import net.fijma.classfile.ClassFileException;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static net.fijma.classfile.ClassFileUtil.readBytes;
import static net.fijma.classfile.ClassFileUtil.readSizedInteger;

public class ConstantPool {

    private Map<Integer, Info> contantPoolInfo = new HashMap<>();

    private ConstantPool() {}

    public static ConstantPool read(InputStream is) throws ClassFileException {
        ConstantPool res = new ConstantPool();
        int count = (int)readSizedInteger(is, 2);
        for (int i=1; i<count; ++i) {
            Info info = res.readConstantPoolEntry(is);
            res.contantPoolInfo.put(i, info);
        }
        return res;
    }

    public <T extends Info> T get(int index, Class<T> type) throws ClassFileException {
        Info i = contantPoolInfo.get(index);
        if (i == null) throw new ClassFileException("no constantpool at index: " + index);
        try {
            return type.cast(i);
        } catch (ClassCastException e) {
            throw new ClassFileException("constantpool at index:" + index + " is not a " + type.getName());
        }
     }

    private Info readConstantPoolEntry(InputStream is) throws ClassFileException {
        byte[] entryTypes = readBytes(is, 1);
        switch (entryTypes[0]) {
            case 1:
                return readUtf8Info(is);
            case 7:
                return readClassInfo(is);
            case 8:
                return readStringInfo(is);
            case 9:
                return readFieldrefInfo(is);
            case 10:
                return readMethodRefInfo(is);
            case 11:
                return readInterfaceMethodrefInfo(is);
            case 12:
                return readNameAndTypeInfo(is);
            case 15:
                return readMethodHandleInfo(is);
            case 18:
                return readInvokeDynamicInfo(is);
        }
        throw new ClassFileException("unknown/non-implemented constant pool entry: " + entryTypes[0]);
    }

    private MethodRefInfo readMethodRefInfo(InputStream is) throws ClassFileException {
        long classIndex = readSizedInteger(is, 2);
        long nameAndTypeIndex = readSizedInteger(is, 2);
        return new MethodRefInfo(this, (int) classIndex, (int)nameAndTypeIndex);
    }

    private FieldRefInfo readFieldrefInfo(InputStream is) throws ClassFileException {
        long classIndex = readSizedInteger(is, 2);
        long nameAndTypeIndex = readSizedInteger(is, 2);
        return new FieldRefInfo(this, (int) classIndex, (int)nameAndTypeIndex);
    }

    private InterfaceMethodRefInfo readInterfaceMethodrefInfo(InputStream is) throws ClassFileException {
        long classIndex = readSizedInteger(is, 2);
        long nameAndTypeIndex = readSizedInteger(is, 2);
        return new InterfaceMethodRefInfo(this, (int) classIndex, (int)nameAndTypeIndex);
    }

    private StringInfo readStringInfo(InputStream is) throws  ClassFileException {
        long stringIndex = readSizedInteger(is, 2);
        return new StringInfo(this, (int)stringIndex);
    }

    private ClassInfo readClassInfo(InputStream is) throws ClassFileException {
        long nameIndex = readSizedInteger(is,2 );
        return new ClassInfo(this, (int)nameIndex);
    }

    private Utf8Info readUtf8Info(InputStream is) throws ClassFileException {
        long length = readSizedInteger(is, 2);
        byte[] bytes = readBytes(is, (int) length);
        String val = new String(bytes, Charset.forName("UTF-8"));
        return new Utf8Info(this, val);
    }

    private NameAndTypeInfo readNameAndTypeInfo(InputStream is) throws ClassFileException {
        long nameIndex = readSizedInteger(is, 2);
        long descriptorIndex = readSizedInteger(is, 2);
        return new NameAndTypeInfo(this, (int) nameIndex, (int)descriptorIndex);
    }

    private InvokeDynamicInfo readInvokeDynamicInfo(InputStream is) throws ClassFileException {
        int bootstrapMethodAttrIndex = (int) readSizedInteger(is ,2);
        int nameAndTypeIndex = (int) readSizedInteger(is, 2);
        return new InvokeDynamicInfo(this, bootstrapMethodAttrIndex, nameAndTypeIndex );
    }

    private MethodHandleInfo readMethodHandleInfo(InputStream is) throws ClassFileException {
        int referenceKind = (int) readSizedInteger(is ,1);
        int referenceIndex = (int) readSizedInteger(is , 2);
        return new MethodHandleInfo(this, referenceKind, referenceIndex);
    }

    @Override
    public String toString() {
        return contantPoolInfo.entrySet().stream().
                map(e -> e.getKey() + ": " + e.getValue().toTaggedString()).
                collect(Collectors.joining("\n"));
    }
}
