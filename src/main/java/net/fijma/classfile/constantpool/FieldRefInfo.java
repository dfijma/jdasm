package net.fijma.classfile.constantpool;

public class FieldRefInfo extends RefInfo {

    public FieldRefInfo(ConstantPool pool, int classIndex, int nameAndTypeIndex) {
        super(pool, classIndex, nameAndTypeIndex);
    }

    @Override
    public String toString() {
        return "Field " + super.toString();
    }
}
