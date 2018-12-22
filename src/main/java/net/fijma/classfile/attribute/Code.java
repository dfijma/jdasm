package net.fijma.classfile.attribute;

import net.fijma.classfile.ClassFile;
import net.fijma.classfile.ClassFileException;
import net.fijma.classfile.ClassFileUtil;
import net.fijma.classfile.constantpool.ConstantPool;
import net.fijma.classfile.constantpool.Info;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Inherited;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static net.fijma.classfile.ClassFileUtil.*;

public class Code extends Attribute {

    private final int maxStack;
    private final int maxLocals;
    private final byte[] code;
    private final List<Object> exceptionTable;
    private final Set<Attribute> attributes;

    private Code(ConstantPool pool, int maxStack, int maxLocals, byte[] code) {
        super(pool);
        this.maxStack = maxStack;
        this.maxLocals = maxLocals;
        this.code = code;
        this.exceptionTable = new ArrayList<>();
        this.attributes = new HashSet<>();
    }

    public static Code read(ConstantPool pool, int attributeLength, InputStream is) throws ClassFileException {
        int maxStack = (int) readSizedInteger(is, 2);
        int maxLocals = (int) readSizedInteger(is, 2);
        int codeLength = (int) readSizedInteger(is, 4);
        byte code[] = new byte[codeLength];
        try {
            int rc = is.read(code);
            if (rc != codeLength) throw new ClassFileException("not enough data");

            Code res = new Code(pool, maxStack, maxLocals, code);

            int exceptionTableLength = (int) readSizedInteger(is, 2);
            for (int i = 0; i < exceptionTableLength; i++) {
                int startPc = (int) readSizedInteger(is, 2);
                int endPc = (int) readSizedInteger(is, 2);
                int handlerPc = (int) readSizedInteger(is, 2);
                int catchType = (int) readSizedInteger(is, 2);
                res.exceptionTable.add(new Object()); // TODO
            }

            Set<Attribute> attributes = readAttributes(pool, is);
            res.attributes.addAll(attributes);

            return res;
        } catch (IOException e) {
            throw new ClassFileException("cannot read data");
        }
    }

    @Override
    public String toString( ) {
        try {
            return "Code: " + code.length + " bytes, maxStack=" + maxStack +
                    ", maxLocals=" + maxLocals + ", exceptionTableEntries " + exceptionTable.size() +
                    ", #attributes=" + attributes.size() + "\n" + disasm();
        } catch (ClassFileException e) {
            throw new RuntimeException(e); // TODO
        }
    }

    private static abstract class Instruction {
        String name;
        Instruction(String name) { this.name = name; }
        abstract String formatOperands(ConstantPool pool, byte[] code, AtomicInteger pos) throws ClassFileException;
    }

    private static class SimpleInstruction extends Instruction {
        SimpleInstruction(String name) { super(name); }
        public String toString() { return name; }
        String formatOperands(ConstantPool pool, byte[] code, AtomicInteger pos) { return name; }
    }

    private static class InstructionByteVal extends Instruction {
        InstructionByteVal(String name) { super(name); }
        String formatOperands(ConstantPool pool, byte[] code, AtomicInteger pos) {
            byte value = ClassFileUtil.signedByte(code, pos.getAndAdd(1));
            return name + " " + value;
        }
    }

    private static class InstructionShortVal extends Instruction {
        InstructionShortVal(String name) { super(name); }
        String formatOperands(ConstantPool pool, byte[] code, AtomicInteger pos) {
            short value = ClassFileUtil.signedShort(code, pos.getAndAdd(2));
            return name + " " + value;
        }
    }

    private static class InstructionByteIndex extends Instruction {
        InstructionByteIndex(String name) { super(name); }
        String formatOperands(ConstantPool pool, byte[] code, AtomicInteger pos) throws ClassFileException {
            int index = (int) ClassFileUtil.unsignedNumber(code, pos.getAndAdd(1), 1);
            return name + " #" + index + " // " + pool.get(index, Info.class); // TODO check info type
        }
    }

    private static class InstructionShortIndex extends Instruction {
        InstructionShortIndex(String name) { super(name); }
        String formatOperands(ConstantPool pool, byte[] code, AtomicInteger pos) throws ClassFileException{
            int index = (int) ClassFileUtil.unsignedNumber(code, pos.getAndAdd(2), 2);
            return name + " #" + index + " // " + pool.get(index, Info.class); // TODO check info type
        }
    }

    private static class InstructionShortIndexAndCount extends Instruction {
        InstructionShortIndexAndCount(String name) { super(name); }
        String formatOperands(ConstantPool pool, byte[] code, AtomicInteger pos) throws ClassFileException {
            int index = (int) unsignedNumber(code, pos.getAndAdd(2), 2);
            long count = unsignedNumber(code, pos.getAndAdd(1), 1);
            long junk = unsignedNumber(code, pos.getAndAdd(1), 1);
            if (junk != 0) throw new RuntimeException("should be zero");
            return name + " #" + index + "," + count + " // " + pool.get(index, Info.class); // TODO check info type
        }
    }

    private static class InstructionShortIndexSpecial extends Instruction {
        InstructionShortIndexSpecial(String name) { super(name); }
        String formatOperands(ConstantPool pool, byte[] code, AtomicInteger pos) throws ClassFileException {
            int index = (int) ClassFileUtil.unsignedNumber(code, pos.getAndAdd(2), 2);
            long junk = ClassFileUtil.unsignedNumber(code, pos.getAndAdd(2), 2);
            if (junk != 0) throw new RuntimeException("should be zero");
            return name + " #" + index + " // " + pool.get(index, Info.class); // TODO check info type
        }
    }

    private static class InstructionVar extends Instruction {
        InstructionVar(String name) { super(name); }
        String formatOperands(ConstantPool pool, byte[] code, AtomicInteger pos) {
            long index = ClassFileUtil.unsignedNumber(code, pos.getAndAdd(1), 1);
            return name + " local_" + index;
        }
    }

    private static class InstructionVarWithByteVal extends Instruction {
        InstructionVarWithByteVal(String name) { super(name); }
        String formatOperands(ConstantPool pool, byte[] code, AtomicInteger pos) {
            long index = ClassFileUtil.unsignedNumber(code, pos.getAndAdd(1), 1);
            byte val = signedByte(code, pos.getAndAdd(1));
            return name + " local_" + index + "," + val;
        }
    }

    private static class InstructionOffset extends Instruction {
        InstructionOffset(String name) { super(name); }
        String formatOperands(ConstantPool pool, byte[] code, AtomicInteger pos) {
            int p = pos.getAndAdd(2);
            short offset = ClassFileUtil.signedShort(code, p);
            return name + " " + (offset+(p-1)) + " // relative=" + offset;
        }
    }

    private static class InstructionType extends Instruction {
        InstructionType(String name) { super(name); }
        String formatOperands(ConstantPool pool, byte[] code, AtomicInteger pos) {
            int tag = (int) ClassFileUtil.unsignedNumber(code, pos.getAndAdd(1), 1);
            String type;
            switch (tag) {
                case 4:
                    type = "boolean";
                    break;
                case 5:
                    type = "char";
                    break;
                case 6:
                    type = "float";
                    break;
                case 7:
                    type = "double";
                    break;
                case 8:
                    type = "byte";
                    break;
                case 9:
                    type = "short";
                    break;
                case 10:
                    type = "int";
                    break;
                case 11:
                    type = "long";
                    break;
                default:
                    throw new RuntimeException("invalid type tag");
            }
            return name + " " + type;
        }
    }

    private static Map<Integer, Instruction> instructionSet = new HashMap();

    static {
        instructionSet.put(3, new SimpleInstruction("iconst_0"));
        instructionSet.put(5, new SimpleInstruction("iconst_2"));
        instructionSet.put(6, new SimpleInstruction("iconst_3"));
        instructionSet.put(7, new SimpleInstruction("iconst_4"));
        instructionSet.put(8, new SimpleInstruction("iconst_5"));
        instructionSet.put(16, new InstructionByteVal("bipush"));
        instructionSet.put(17, new InstructionShortVal("sipush"));
        instructionSet.put(18, new InstructionByteIndex("ldc"));
        instructionSet.put(21, new InstructionVar("iload"));
        instructionSet.put(25, new InstructionVar("aload"));
        instructionSet.put(27, new SimpleInstruction("iload_1"));
        instructionSet.put(28, new SimpleInstruction("iload_2"));
        instructionSet.put(29, new SimpleInstruction("iload_3"));
        instructionSet.put(42, new SimpleInstruction("aload_0"));
        instructionSet.put(44, new SimpleInstruction("aload_2"));
        instructionSet.put(43, new SimpleInstruction("aload_1"));
        instructionSet.put(45, new SimpleInstruction("aload_3"));
        instructionSet.put(51, new SimpleInstruction("baload"));
        instructionSet.put(54, new InstructionVar("istore"));
        instructionSet.put(58, new InstructionVar("astore"));
        instructionSet.put(62, new SimpleInstruction("istore_3"));
        instructionSet.put(76, new SimpleInstruction("astore_1"));
        instructionSet.put(77, new SimpleInstruction("astore_2"));
        instructionSet.put(87, new SimpleInstruction("pop"));
        instructionSet.put(89, new SimpleInstruction("dup"));
        instructionSet.put(96, new SimpleInstruction("iadd"));
        instructionSet.put(104, new SimpleInstruction("imul"));
        instructionSet.put(126, new SimpleInstruction("iand"));
        instructionSet.put(132, new InstructionVarWithByteVal("iinc"));
        instructionSet.put(136, new SimpleInstruction("l2i"));
        instructionSet.put(159, new InstructionOffset("if_icmpeq"));
        instructionSet.put(162, new InstructionOffset("if_icmpge"));
        instructionSet.put(167, new InstructionOffset("goto"));
        instructionSet.put(172, new SimpleInstruction("ireturn"));
        instructionSet.put(176, new SimpleInstruction("areturn"));
        instructionSet.put(177, new SimpleInstruction("return"));
        instructionSet.put(178, new InstructionShortIndex("getstatic"));
        instructionSet.put(179, new InstructionShortIndex("putstatic"));
        instructionSet.put(180, new InstructionShortIndex("getfield"));
        instructionSet.put(181, new InstructionShortIndex("putfield"));
        instructionSet.put(182, new InstructionShortIndex("invokevirtual"));
        instructionSet.put(183, new InstructionShortIndex("invokespecial"));
        instructionSet.put(184, new InstructionShortIndex("invokestatic"));
        instructionSet.put(185, new InstructionShortIndexAndCount("invokeinterface"));
        instructionSet.put(186, new InstructionShortIndexSpecial("invokedynamic"));
        instructionSet.put(187, new InstructionShortIndex("new"));
        instructionSet.put(188, new InstructionType("newarray"));
        instructionSet.put(190, new SimpleInstruction("arraylength"));
        instructionSet.put(191, new SimpleInstruction("athrow"));
        instructionSet.put(192, new InstructionShortIndex("checkcast"));
        instructionSet.put(199, new InstructionOffset("ifnonnull"));
    }

    public String disasm() throws ClassFileException {
        StringBuilder sb = new StringBuilder();
        for (AtomicInteger pos=new AtomicInteger(0); pos.intValue() < code.length; ) {
            int p = pos.getAndIncrement();
            int opcode = code[p] & 0xFF;
            Instruction i = instructionSet.get(opcode);
            if (i == null) throw new RuntimeException("unknown opcode " + opcode);
            sb.append(p).append(": ").append(i.formatOperands(pool, code, pos)).append("\n");
        }
        return sb.toString();
    }
}
