package net.fijma.classfile;

import net.fijma.classfile.attribute.Attribute;
import net.fijma.classfile.constantpool.ClassInfo;
import net.fijma.classfile.constantpool.ConstantPool;
import net.fijma.classfile.constantpool.Utf8Info;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static net.fijma.classfile.ClassFileUtil.readBytes;
import static net.fijma.classfile.ClassFileUtil.readSizedInteger;
import static net.fijma.classfile.attribute.Attribute.readAttributes;

public class ClassFile {

    public static final Version SUPPORTED_VERSION = new Version(55,0);

    public final ConstantPool pool;
    public final int accessFlags;
    public final ClassInfo thisClass;
    public final ClassInfo superClass;
    public final Version version;

    public final Set<Method> methods;
    public final List<ClassInfo> interfaces;
    public final Set<Field> fields;
    public final Set<Attribute> attributes;

    public ClassFile(Version version, ConstantPool pool, int accessFlags, ClassInfo thisClass, ClassInfo superClass) {
        this.version = version;
        this.pool = pool;
        this.accessFlags = accessFlags;
        this.thisClass = thisClass;
        this.superClass = superClass;
        this.methods = new HashSet<>();
        this.fields = new HashSet<>();
        this.attributes = new HashSet<>();
        this.interfaces = new ArrayList<>();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(AccessFlag.flagsToString(accessFlags));
        sb.append(thisClass.getName()).append(" extends ").append(superClass.getName());
        for (ClassInfo i : interfaces) {
            sb.append(" implements ").append(i.getName()).append(" ");
        }
        sb.append(" {\n");
        for (Attribute a : attributes) {
            sb.append("// ").
                append(a).
                append("\n");
        }
        for (Field f : fields) {
            sb.append("    ");
            sb.append(f);
            sb.append("\n");
        }
        for (Method m : methods) {
            sb.append("    ");
            sb.append(m.toString());
            sb.append("\n");
        }
        sb.append("}\n");
        return sb.toString();
    }

    public static ClassFile read(InputStream is) throws ClassFileException {

        // basic stuff, magic bytes and version
        readMagic(is);
        Version version = readVersion(is);

        // once we have at least a valid contantpool and a class name and a super class name, create a ClassFile representing the file being read
        ConstantPool pool = ConstantPool.read(is);
        int accessFlags = (int)readSizedInteger(is, 2);
        ClassInfo thisClass = readClass(pool, is);
        ClassInfo superClass = readClass(pool, is);
        ClassFile classFile = new ClassFile(version, pool, accessFlags, thisClass, superClass);

        // body: interface, fields, methods and classFile level attributes
        classFile.readInterfaces(is);
        classFile.readFieldsOrMethods(is, true); // fields
        classFile.readFieldsOrMethods(is, false); // methods
        Set<Attribute> attributes = Attribute.readAttributes(pool, is);
        classFile.attributes.addAll(attributes);

        // and now the input stream should exhausted
        try {
            int b = is.read();
            if (b>=0) throw new ClassFileException("extra data");
        } catch (IOException e) {
            throw new ClassFileException("cannot read");
        }
        return classFile;
    }

    private static void readMagic(InputStream is) throws ClassFileException {
        // 0xCAFEBABE
        byte[] magic = readBytes(is, 4);
        if (magic[0] !=  (byte) 0xCA || magic[1] != (byte) 0xFE || magic [2] != (byte) 0xBA || magic[3] != (byte) 0xBE) {
            throw new ClassFileException("No cafe, Babe");
        }
    }

    private static Version readVersion(InputStream is) throws ClassFileException {
        // u2 minor u2 major
        int minor = (int) readSizedInteger(is, 2);
        int major = (int) readSizedInteger(is, 2);
        Version res = new Version(major, minor);
        if (res.compareTo(SUPPORTED_VERSION) > 0) {
            throw new ClassFileException(res + "should be updated for version > 52.0");
        }
        return res;
    }

    private static ClassInfo readClass(ConstantPool pool, InputStream is) throws ClassFileException {
        long thisClass = readSizedInteger(is, 2);
        return pool.get((int)thisClass, ClassInfo.class);
    }

    private void readInterfaces(InputStream is) throws ClassFileException {
        int interfaces = (int)readSizedInteger(is, 2);
        for (int i=0; i<interfaces; ++i) {
            int ifaceIndex = (int) readSizedInteger(is, 2);
            ClassInfo iface = pool.get(ifaceIndex, ClassInfo.class);
            this.interfaces.add(iface);
        }
    }

    private void readFieldsOrMethods(InputStream is, boolean fs) throws ClassFileException {
        long fieldsCount = readSizedInteger(is, 2);
        for (int i=0; i<fieldsCount; ++i) {
            int accessFlags = (int) readSizedInteger(is, 2);
            int nameIndex = (int) readSizedInteger(is, 2);
            int descriptorIndex = (int) readSizedInteger(is, 2);

            Utf8Info name = pool.get(nameIndex, Utf8Info.class);
            Utf8Info descriptor = pool.get(descriptorIndex, Utf8Info.class);

            Set<Attribute> attributes = readAttributes(pool, is);
            if (fs) {
                fields.add(new Field(accessFlags, name, descriptor, attributes));
            } else {
                methods.add(new Method(accessFlags, name, descriptor, attributes));
            }
        }
    }


}
