package net.fijma.classfile.constantpool;

public class InterfaceMethodRefInfo extends RefInfo {

    public InterfaceMethodRefInfo(ConstantPool pool, int classIndex, int nameAndTypeIndex) {
        super(pool, classIndex, nameAndTypeIndex);
    }

    @Override
    public String toString() {
        return "InterfaceMethod " + super.toString();
    }
}