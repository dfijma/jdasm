package net.fijma.classfile.constantpool;

// TODO
public class InvokeDynamicInfo extends Info {

    final int bootstrapMethodAttrIndex;
    final int nameAndTypeIndex;

    public InvokeDynamicInfo(ConstantPool pool, int bootstrapMethodAttrIndex, int nameAndTypeIndex) {
        super(pool);
        this.bootstrapMethodAttrIndex = bootstrapMethodAttrIndex;
        this.nameAndTypeIndex = nameAndTypeIndex;
    }

    @Override
    public String toString() {  return "anInvokeDynamicInfo "; }
}
