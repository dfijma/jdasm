package net.fijma.classfile.constantpool;

public class NameAndTypeInfo extends Info {

    public final int nameIndex;
    public final int descriptorIndex;

    public NameAndTypeInfo(ConstantPool pool, int nameIndex, int descriptorIndex) {
        super(pool);
        this.nameIndex = nameIndex;
        this.descriptorIndex = descriptorIndex;
    }

    public Utf8Info getName() {
        return get(nameIndex, Utf8Info.class);
    }

    public Utf8Info getDescriptor() {
        return get(descriptorIndex, Utf8Info.class);
    }

    @Override
    public String toString() {
        return getName() + ":" + getDescriptor();
    }

    @Override
    public String toTaggedString() {
        return "NameAndType " + toString();
    }

}
