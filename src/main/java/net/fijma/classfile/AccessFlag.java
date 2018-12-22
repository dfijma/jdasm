package net.fijma.classfile;

public enum AccessFlag {

    PUBLIC(0x0001),
    PRIVATE(0x0002),
    PROTECTED(0x0004),
    STATIC(0x0008),
    FINAL(0x0010),
    SYNCRHORIZED (0x0020), // or SUPER_SPECIAL for class
    BRIDGE(0x0040), // for VOLATILE for fields
    VARARGS(0x0080), // or TRANSIENT for fields
    NATIVE(0x0100),
    INTERFACE (0x0200),
    ABSTRACT (0x0400),
    STRICT(0x800),
    SYNTHETIC (0x1000),
    ANNOTATION (0x2000),
    ENUM (0x4000);

    private final int x;

    AccessFlag(int x) {
        this.x = x;
    }

    boolean in(int x) {
        return (x & this.x) == this.x;
    }

    public static String flagsToString(int x) {
        StringBuilder sb = new StringBuilder();
        if (PUBLIC.in(x)) { sb.append("public "); }
        if (PRIVATE.in(x)) { sb.append("private "); }
        if (PROTECTED.in(x)) { sb.append("protected "); }
        if (STATIC.in(x)) { sb.append("static "); }
        if (FINAL.in(x)) { sb.append("final "); }
        if (SYNCRHORIZED.in(x)) { sb.append("special "); }
        if (BRIDGE.in(x)) { sb.append("bridge "); }
        if (VARARGS.in(x)) { sb.append("varargs "); }
        if (NATIVE.in(x)) { sb.append("native "); }
        if (INTERFACE.in(x)) { sb.append("interface "); }
        if (ABSTRACT.in(x)) { sb.append("abstract "); }
        if (STRICT.in(x)) { sb.append("strict "); }
        if (SYNTHETIC.in(x)) { sb.append("synthetic "); }
        if (ANNOTATION.in(x)) { sb.append("annotation "); }
        if (ENUM.in(x)) { sb.append("enum "); }
        return sb.toString();
    }

}

