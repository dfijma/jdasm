package net.fijma.classfile.constantpool;

public abstract class RefInfo extends Info {

    public final int classIndex;
    public final int nameAndTypeIndex;

    public RefInfo(ConstantPool pool, int classIndex, int nameAndTypeIndex) {
        super(pool);
        this.classIndex = classIndex;
        this.nameAndTypeIndex = nameAndTypeIndex;
    }

    public ClassInfo getClazz() {
        return get(classIndex, ClassInfo.class);
    }

    public NameAndTypeInfo getNameAndType() {
        return get(nameAndTypeIndex, NameAndTypeInfo.class);
    }

    @Override
    public String toString() {
        return getClazz().getName() + "." + getNameAndType().toString();
    }

}
