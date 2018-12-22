package net.fijma.classfile;

public class ClassFileException extends Exception {

    public ClassFileException(String s) {
        super(s);
    }

    public ClassFileException(String s, Exception reason) {
        super(s, reason);
    }
}
