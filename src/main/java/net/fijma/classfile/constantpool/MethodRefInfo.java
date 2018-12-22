package net.fijma.classfile.constantpool;

public class MethodRefInfo extends RefInfo {

    public MethodRefInfo(ConstantPool pool, int classIndex, int nameAndTypeIndex) {
        super(pool, classIndex, nameAndTypeIndex);
    }

    @Override
    public String toString() {
        return "Method " + super.toString();
    }
}
