package net.fijma.classfile.constantpool;

// TODO
public class MethodHandleInfo extends Info {

    public final int referenceKind;
    public final int referenceIndex;

    public MethodHandleInfo(ConstantPool pool, int referenceKind, int referenceIndex) {
        super(pool);
        this.referenceKind = referenceKind;
        this.referenceIndex = referenceIndex;
    }

    @Override
    public String toString() {
        return "aMethodHandleInfo";
    }
}
