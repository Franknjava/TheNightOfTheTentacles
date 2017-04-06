package de.framey.lab.evil.eviltentaclesofdeath.asm;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.plexus.util.StringOutputStream;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import de.framey.lab.evil.eviltentaclesofdeath.testhelper.Simple;

public class AsmPrinterTest {

    private static final String    SIMPLE_TEXT      = "// class version 52.0 (52)\n" +
            "// access flags 0x21\n" +
            "public class de/framey/lab/evil/eviltentaclesofdeath/testhelper/Simple {\n" +
            "\n" +
            "  // compiled from: Simple.java\n" +
            "\n" +
            "  // access flags 0x2\n" +
            "  private I x\n" +
            "\n" +
            "  // access flags 0x1\n" +
            "  public <init>()V\n" +
            "   L0\n" +
            "    LINENUMBER 7 L0\n" +
            "    ALOAD 0\n" +
            "    INVOKESPECIAL java/lang/Object.<init> ()V\n" +
            "   L1\n" +
            "    LINENUMBER 5 L1\n" +
            "    ALOAD 0\n" +
            "    ICONST_5\n" +
            "    PUTFIELD de/framey/lab/evil/eviltentaclesofdeath/testhelper/Simple.x : I\n" +
            "   L2\n" +
            "    LINENUMBER 8 L2\n" +
            "    RETURN\n" +
            "   L3\n" +
            "    LOCALVARIABLE this Lde/framey/lab/evil/eviltentaclesofdeath/testhelper/Simple; L0 L3 0\n" +
            "    MAXSTACK = 2\n" +
            "    MAXLOCALS = 1\n" +
            "\n" +
            "  // access flags 0x1\n" +
            "  public simpleMethod()V\n" +
            "   L0\n" +
            "    LINENUMBER 11 L0\n" +
            "    BIPUSH 7\n" +
            "    ISTORE 1\n" +
            "   L1\n" +
            "    LINENUMBER 12 L1\n" +
            "    GETSTATIC java/lang/System.out : Ljava/io/PrintStream;\n" +
            "    ALOAD 0\n" +
            "    GETFIELD de/framey/lab/evil/eviltentaclesofdeath/testhelper/Simple.x : I\n" +
            "    ILOAD 1\n" +
            "    IMUL\n" +
            "    INVOKEVIRTUAL java/io/PrintStream.println (I)V\n" +
            "   L2\n" +
            "    LINENUMBER 13 L2\n" +
            "    RETURN\n" +
            "   L3\n" +
            "    LOCALVARIABLE this Lde/framey/lab/evil/eviltentaclesofdeath/testhelper/Simple; L0 L3 0\n" +
            "    LOCALVARIABLE y I L1 L3 1\n" +
            "    MAXSTACK = 3\n" +
            "    MAXLOCALS = 2\n" +
            "\n" +
            "  // access flags 0x1\n" +
            "  public complexMethod(IJ)Ljava/lang/String;\n" +
            "   L0\n" +
            "    LINENUMBER 16 L0\n" +
            "    LDC \"OK\"\n" +
            "    ARETURN\n" +
            "   L1\n" +
            "    LOCALVARIABLE this Lde/framey/lab/evil/eviltentaclesofdeath/testhelper/Simple; L0 L1 0\n" +
            "    LOCALVARIABLE a I L0 L1 1\n" +
            "    LOCALVARIABLE c J L0 L1 2\n" +
            "    MAXSTACK = 1\n" +
            "    MAXLOCALS = 4\n" +
            "}\n";

    private static final String    METHOD_TEXT      = "simpleMethod()V\n" +
            " -1    LABEL *\n" +
            " -1    LINE 11\n" +
            " 16      BIPUSH 7\n" +
            " 54      ISTORE 1\n" +
            " -1    LABEL *\n" +
            " -1    LINE 12\n" +
            "178      GETSTATIC java/lang/System.out Ljava/io/PrintStream;\n" +
            " 25      ALOAD 0\n" +
            "180      GETFIELD de/framey/lab/evil/eviltentaclesofdeath/testhelper/Simple.x I\n" +
            " 21      ILOAD 1\n" +
            "104      IMUL \n" +
            "182      INVOKEVIRTUAL java/io/PrintStream.println (I)V\n" +
            " -1    LABEL *\n" +
            " -1    LINE 13\n" +
            "177      RETURN \n" +
            " -1    LABEL *\n\n";

    private static final String    INSTRUCTION_TEXT = "178      GETSTATIC java/lang/System.out Ljava/io/PrintStream;\n";

    private byte[]                 code;
    private MethodNode             method;
    private List<AbstractInsnNode> instructions;

    @BeforeTest
    private void loadCode() throws IOException {
        code = AsmUtil.writeClass(AsmUtil.readClass(Simple.class));
        method = (MethodNode) AsmUtil.readClass(Simple.class).methods.get(1);
        instructions = new ArrayList<>();
        for (int i = 0; i < method.instructions.size(); i++) {
            instructions.add(method.instructions.get(i));
        }
    }

    @Test
    public void printClassbyte() {
        PrintStream out = System.out;
        StringOutputStream sout = new StringOutputStream();
        System.setOut(new PrintStream(sout));
        AsmPrinter.printClass(code);
        String prt = sout.toString();
        System.setOut(out);
        Assert.assertEquals(SIMPLE_TEXT, prt);
    }

    @Test
    public void printClassbytePrintStream() {
        StringOutputStream sout = new StringOutputStream();
        AsmPrinter.printClass(code, new PrintStream(sout));
        String prt = sout.toString();
        Assert.assertEquals(SIMPLE_TEXT, prt);
    }

    @Test
    public void printClassClass() throws IOException {
        PrintStream out = System.out;
        StringOutputStream sout = new StringOutputStream();
        System.setOut(new PrintStream(sout));
        AsmPrinter.printClass(Simple.class);
        String prt = sout.toString();
        System.setOut(out);
        Assert.assertEquals(SIMPLE_TEXT, prt);
    }

    @Test
    public void printClassClassPrintStream() throws IOException {
        StringOutputStream sout = new StringOutputStream();
        AsmPrinter.printClass(Simple.class, new PrintStream(sout));
        String prt = sout.toString();
        Assert.assertEquals(SIMPLE_TEXT, prt);
    }

    @Test
    public void printClassToStringbyte() {
        String prt = AsmPrinter.printClassToString(code);
        Assert.assertEquals(SIMPLE_TEXT, prt);
    }

    @Test
    public void printClassToStringClass() throws IOException {
        String prt = AsmPrinter.printClassToString(Simple.class);
        Assert.assertEquals(SIMPLE_TEXT, prt);
    }

    @Test
    public void printInstructionAbstractInsnNodeFunctionAbstractInsnNodeString() {
        PrintStream out = System.out;
        StringOutputStream sout = new StringOutputStream();
        System.setOut(new PrintStream(sout));
        AsmPrinter.printInstruction(instructions.get(6), (ain) -> {
            return String.format("%3d  ", ain.getOpcode());
        });
        String prt = sout.toString().replaceAll("L[0-9]+", "*");
        System.setOut(out);
        Assert.assertEquals(INSTRUCTION_TEXT, prt);
    }

    @Test
    @SuppressWarnings("resource")
    public void printInstructionAbstractInsnNodeFunctionAbstractInsnNodeStringPrintStream() {
        StringOutputStream sout = new StringOutputStream();
        AsmPrinter.printInstruction(instructions.get(6), (ain) -> {
            return String.format("%3d  ", ain.getOpcode());
        }, new PrintStream(sout));
        String prt = sout.toString().replaceAll("L[0-9]+", "*");
        Assert.assertEquals(INSTRUCTION_TEXT, prt);
    }

    @Test
    public void printInstructionToString() {
        String prt = AsmPrinter.printInstructionToString(instructions.get(6), (ain) -> {
            return String.format("%3d  ", ain.getOpcode());
        }).replaceAll("L[0-9]+", "*");
        Assert.assertEquals(INSTRUCTION_TEXT, prt);
    }

    @Test
    public void printInstructionsListAbstractInsnNodeFunctionAbstractInsnNodeString() {
        PrintStream out = System.out;
        StringOutputStream sout = new StringOutputStream();
        System.setOut(new PrintStream(sout));
        AsmPrinter.printInstructions(instructions, (ain) -> {
            return String.format("%3d  ", ain.getOpcode());
        });
        String prt = sout.toString().replaceAll("L[0-9]+", "*");
        System.setOut(out);
        Assert.assertEquals(METHOD_TEXT.substring(16), prt);
    }

    @Test
    @SuppressWarnings("resource")
    public void printInstructionsListAbstractInsnNodeFunctionAbstractInsnNodeStringPrintStream() {
        StringOutputStream sout = new StringOutputStream();
        AsmPrinter.printInstructions(instructions, (ain) -> {
            return String.format("%3d  ", ain.getOpcode());
        }, new PrintStream(sout));
        String prt = sout.toString().replaceAll("L[0-9]+", "*");
        Assert.assertEquals(METHOD_TEXT.substring(16), prt);
    }

    @Test
    public void printInstructionsToString() {
        String prt = AsmPrinter.printInstructionsToString(instructions, (ain) -> {
            return String.format("%3d  ", ain.getOpcode());
        }).replaceAll("L[0-9]+", "*");
        Assert.assertEquals(METHOD_TEXT.substring(16), prt);
    }

    @Test
    public void printMethodMethodNodeFunctionAbstractInsnNodeString() {
        PrintStream out = System.out;
        StringOutputStream sout = new StringOutputStream();
        System.setOut(new PrintStream(sout));
        AsmPrinter.printMethod(method, (ain) -> {
            return String.format("%3d  ", ain.getOpcode());
        });
        String prt = sout.toString().replaceAll("L[0-9]+", "*");
        System.setOut(out);
        Assert.assertEquals(METHOD_TEXT, prt);
    }

    @Test
    @SuppressWarnings("resource")
    public void printMethodMethodNodeFunctionAbstractInsnNodeStringPrintStream() {
        StringOutputStream sout = new StringOutputStream();
        AsmPrinter.printMethod(method, (ain) -> {
            return String.format("%3d  ", ain.getOpcode());
        }, new PrintStream(sout));
        String prt = sout.toString().replaceAll("L[0-9]+", "*");
        Assert.assertEquals(METHOD_TEXT, prt);
    }

    @Test
    public void printMethodToString() {
        String prt = AsmPrinter.printMethodToString(method, (ain) -> {
            return String.format("%3d  ", ain.getOpcode());
        }).replaceAll("L[0-9]+", "*");
        Assert.assertEquals(METHOD_TEXT, prt);
    }
}
