package net.fijma.classfile.constantpool;

import net.fijma.classfile.ClassFileException;

public abstract class Info {

    public final ConstantPool pool;

    public Info(ConstantPool pool) {
        this.pool = pool;
    }

    public <T extends Info> T get(int index, Class<T> type) {
        try {
            return pool.get(index, type);
        } catch (ClassFileException e) {
            throw new RuntimeException(e);
        }
    }

    public String toTaggedString() { return toString(); }

}
