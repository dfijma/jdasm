package net.fijma.classfile.constantpool;

import org.apache.commons.lang.StringEscapeUtils;

public class Utf8Info extends Info {

    public final String value;

    public Utf8Info(ConstantPool pool, String info) {
        super(pool);
        this.value = info;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public String toTaggedString() {
        return "Utf8 " +  "\"" + StringEscapeUtils.escapeJava(value) + "\"";
    }

}
