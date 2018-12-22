package net.fijma.classfile.constantpool;

public class ClassInfo extends Info {

    public final int nameIndex;

    public ClassInfo(ConstantPool pool, int nameIndex) {
        super(pool);
        this.nameIndex = nameIndex;
    }

    public Utf8Info getName() { return get(nameIndex, Utf8Info.class); }

    @Override
    public String toString()  {
        return getName().toString();
    }

    @Override
    public String toTaggedString() {
        return "Class " + toString();
    }

}
