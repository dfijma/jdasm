package net.fijma.classfile.attribute;

import net.fijma.classfile.constantpool.ConstantPool;
import net.fijma.classfile.constantpool.Utf8Info;

public class SourceFile extends Attribute{

    public final Utf8Info sourcefile;

    public SourceFile(ConstantPool pool, Utf8Info sourcefile) {
        super(pool);
        this.sourcefile = sourcefile;
    }

    @Override
    public String toString() {
        return "SourceFile: " + sourcefile.value;
    }
}
