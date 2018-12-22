package net.fijma.classfile.constantpool;

import org.apache.commons.lang.StringEscapeUtils;

public class StringInfo extends Info {

    public final int stringIndex;

    public StringInfo(ConstantPool pool, int stringIndex) {
        super(pool);
        this.stringIndex = stringIndex;
    }

    public Utf8Info getString() {
        return get(stringIndex, Utf8Info.class);
    }

    @Override
    public String toString() {
        return getString().toString();
    }

    @Override
    public String toTaggedString() {
        return "String " + "\"" + StringEscapeUtils.escapeJava(getString().value) + "\"";
    }

}
