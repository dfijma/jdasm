package net.fijma.classfile;

import net.fijma.classfile.attribute.Attribute;
import net.fijma.classfile.constantpool.Utf8Info;

import java.util.Set;

public class Method {

    public final int accessFlags;
    public final Utf8Info name;
    public final Utf8Info descriptor;
    public final Set<Attribute> attributes;

    public Method(int accessFlags, Utf8Info name, Utf8Info descriptor, Set<Attribute> attributes) {
        this.accessFlags = accessFlags;
        this.name = name;
        this.descriptor = descriptor;
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(AccessFlag.flagsToString(accessFlags) + name.value + "(" + descriptor.value + ") {\n");
        for (Attribute a : attributes) {
            sb.append("// ").append(a).append("\n");

        }
        sb.append("}\n");
        return  sb.toString();
    }

}
