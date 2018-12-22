package net.fijma.classfile;

public class Version implements Comparable<Version> {

    public final int major;
    public final int minor;

    public Version(int major, int minor) {
        this.major = major;
        this.minor = minor;
    }

    @Override
    public String toString() {
        return major + "." + minor;
    }

    @Override
    public int compareTo(Version other) {
        if (this.major < other.major) return -1;
        if (this.major > other.major) return 1;
        return Long.compare(this.minor, other.minor);
    }

}
