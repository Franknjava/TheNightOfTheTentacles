package de.framey.lab.evil.eviltentaclesofdeath.asm;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import org.codehaus.plexus.util.StringOutputStream;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.LookupSwitchInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.MultiANewArrayInsnNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.util.Printer;
import org.objectweb.asm.util.TraceClassVisitor;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility class to print classes, methods and instructions from various sources to various targets.
 *
 * @author Frank Meyfarth
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AsmPrinter {

    /**
     * Prints the given method to System.out. The infoProvider can be used to prefix each line with additional information.
     *
     * @param mn
     *            the method to print
     * @param infoProvider
     *            can provide additional information to be printed for each instruction
     */
    public static void printMethod(MethodNode mn, Function<AbstractInsnNode, String> infoProvider) {
        printMethod(mn, infoProvider, System.out);
    }

    /**
     * Prints the given method to a String and returns it. The infoProvider can be used to prefix each line with additional information.
     *
     * @param mn
     *            the method to print
     * @param infoProvider
     *            can provide additional information to be printed for each instruction
     * @return the String representation of the method
     */
    public static String printMethodToString(MethodNode mn, Function<AbstractInsnNode, String> infoProvider) {
        StringOutputStream out = new StringOutputStream();
        printMethod(mn, infoProvider, new PrintStream(out));
        return out.toString();
    }

    /**
     * Prints the given method to a PrintStream. The infoProvider can be used to prefix each line with additional information.
     *
     * @param mn
     *            the method to print
     * @param infoProvider
     *            can provide additional information to be printed for each instruction
     * @param out
     *            the PrintStream to print to
     */
    @SuppressWarnings("unchecked")
    public static void printMethod(MethodNode mn, Function<AbstractInsnNode, String> infoProvider, PrintStream out) {
        out.println(mn.name + mn.desc);
        for (Iterator<AbstractInsnNode> iterator = mn.instructions.iterator(); iterator.hasNext();) {
            AbstractInsnNode ain = iterator.next();
            printInstruction(ain, infoProvider, out);
        }
        out.println();
    }

    /**
     * Prints a list of instructions to System.out. The infoProvider can be used to prefix each line with additional information.
     *
     * @param instructions
     *            the list of instructions
     * @param infoProvider
     *            can provide additional information to be printed for each instruction
     */
    public static void printInstructions(List<AbstractInsnNode> instructions, Function<AbstractInsnNode, String> infoProvider) {
        printInstructions(instructions, infoProvider, System.out);
    }

    /**
     * Prints a list of instructions to a String and returns it. The infoProvider can be used to prefix each line with additional
     * information.
     *
     * @param instructions
     *            the list of instructions
     * @param infoProvider
     *            can provide additional information to be printed for each instruction
     * @return the String representation of the instructions
     */
    public static String printInstructionsToString(List<AbstractInsnNode> instructions, Function<AbstractInsnNode, String> infoProvider) {
        StringOutputStream out = new StringOutputStream();
        printInstructions(instructions, infoProvider, new PrintStream(out));
        return out.toString();
    }

    /**
     * Prints a list of instructions to a PrintStream. The infoProvider can be used to prefix each line with additional information.
     *
     * @param instructions
     *            the list of instructions
     * @param infoProvider
     *            can provide additional information to be printed for each instruction
     * @param out
     *            the PrintStream to print to
     */
    public static void printInstructions(List<AbstractInsnNode> instructions, Function<AbstractInsnNode, String> infoProvider,
            PrintStream out) {
        for (Iterator<AbstractInsnNode> iterator = instructions.iterator(); iterator.hasNext();) {
            AbstractInsnNode ain = iterator.next();
            printInstruction(ain, infoProvider, out);
        }
        out.println();
    }

    /**
     * Prints a single instruction to System.out. The infoProvider can be used to prefix each line with additional information.
     *
     * @param ain
     *            the instruction to print
     * @param infoProvider
     *            can provide additional information to be printed for the instruction
     */
    public static void printInstruction(AbstractInsnNode ain, Function<AbstractInsnNode, String> infoProvider) {
        printInstruction(ain, infoProvider, System.out);
    }

    /**
     * Prints a single instruction to a String and returns it. The infoProvider can be used to prefix each line with additional information.
     *
     * @param instructions
     *            the instruction to print
     * @param infoProvider
     *            can provide additional information to be printed for the instruction
     * @return the String representation of the instruction
     */
    public static String printInstructionToString(AbstractInsnNode ain, Function<AbstractInsnNode, String> infoProvider) {
        StringOutputStream out = new StringOutputStream();
        printInstruction(ain, infoProvider, new PrintStream(out));
        return out.toString();
    }

    /**
     * Prints a single instruction to a PrintStream. The infoProvider can be used to prefix each line with additional information.
     *
     * @param instructions
     *            the instruction to print
     * @param infoProvider
     *            can provide additional information to be printed for the instruction
     * @param out
     *            the PrintStream to print to
     */
    @SuppressWarnings("unchecked")
    public static void printInstruction(AbstractInsnNode ain, Function<AbstractInsnNode, String> infoProvider, PrintStream out) {
        out.print(infoProvider.apply(ain));
        out.print("  ");
        if (ain.getOpcode() > -1) {
            out.print("  " + Printer.OPCODES[ain.getOpcode()] + " ");
        }
        switch (ain.getType()) {
        case AbstractInsnNode.LABEL:
            out.println("LABEL " + ((LabelNode) ain).getLabel());
            break;
        case AbstractInsnNode.LINE:
            out.println("LINE " + ((LineNumberNode) ain).line);
            break;
        case AbstractInsnNode.FIELD_INSN:
            out.println(((FieldInsnNode) ain).owner + "." + ((FieldInsnNode) ain).name + " " + ((FieldInsnNode) ain).desc);
            break;
        case AbstractInsnNode.VAR_INSN:
            out.println(((VarInsnNode) ain).var);
            break;
        case AbstractInsnNode.INT_INSN:
            out.println(((IntInsnNode) ain).operand);
            break;
        case AbstractInsnNode.LDC_INSN:
            out.println(((LdcInsnNode) ain).cst);
            break;
        case AbstractInsnNode.METHOD_INSN:
            out.println(((MethodInsnNode) ain).owner + "." + ((MethodInsnNode) ain).name + " " + ((MethodInsnNode) ain).desc);
            break;
        case AbstractInsnNode.JUMP_INSN:
            out.println(((JumpInsnNode) ain).label.getLabel());
            break;
        case AbstractInsnNode.FRAME:
            out.println("Local: " + ((FrameNode) ain).local + " Stack: " + ((FrameNode) ain).stack);
            break;
        case AbstractInsnNode.IINC_INSN:
            out.println(((IincInsnNode) ain).var + " " + ((IincInsnNode) ain).incr);
            break;
        case AbstractInsnNode.INVOKE_DYNAMIC_INSN:
            out.println(((InvokeDynamicInsnNode) ain).name + ((InvokeDynamicInsnNode) ain).desc);
            break;
        case AbstractInsnNode.LOOKUPSWITCH_INSN:
            out.println(
                    labelNodeToString(((LookupSwitchInsnNode) ain).dflt) + " Keys: " + ((LookupSwitchInsnNode) ain).keys + " Labels: "
                            + labelNodeListToString(((LookupSwitchInsnNode) ain).labels));
            break;
        case AbstractInsnNode.MULTIANEWARRAY_INSN:
            out.println(((MultiANewArrayInsnNode) ain).desc + " " + ((MultiANewArrayInsnNode) ain).dims);
            break;
        case AbstractInsnNode.TABLESWITCH_INSN:
            out.println(((TableSwitchInsnNode) ain).min + "-" + ((TableSwitchInsnNode) ain).max + " "
                    + labelNodeToString(((TableSwitchInsnNode) ain).dflt) + " Labels: "
                    + labelNodeListToString(((TableSwitchInsnNode) ain).labels));
            break;
        case AbstractInsnNode.TYPE_INSN:
            out.println(((TypeInsnNode) ain).desc);
            break;
        case AbstractInsnNode.INSN:
        default:
            out.println();
        }
    }

    /**
     * Prints the given bytecode to System.out.
     *
     * @param code
     *            the bytecode to print
     */
    public static void printClass(byte[] code) {
        printClass(code, System.out);
    }

    /**
     * Prints the given bytecode to a String and returns it.
     *
     * @param code
     *            the bytecode to print
     * @return the String representation of the class
     */
    public static String printClassToString(byte[] code) {
        StringOutputStream out = new StringOutputStream();
        printClass(code, new PrintStream(out));
        return out.toString();
    }

    /**
     * Prints the given bytecode to a PrintStream.
     *
     * @param code
     *            the bytecode to print
     * @param out
     *            the PrintStream to print to
     */
    public static void printClass(byte[] code, PrintStream out) {
        new ClassReader(code).accept(new TraceClassVisitor(new PrintWriter(new OutputStreamWriter(out))), 0);
    }

    /**
     * Prints the given class to System.out.
     *
     * @param clazz
     *            the class to print
     */
    public static void printClass(Class<?> clazz) throws IOException {
        printClass(clazz, System.out);
    }

    /**
     * Prints the given class to a String and returns it.
     *
     * @param clazz
     *            the class to print
     * @return the String representation of the class
     */
    public static String printClassToString(Class<?> clazz) throws IOException {
        StringOutputStream out = new StringOutputStream();
        printClass(clazz, new PrintStream(out));
        return out.toString();
    }

    /**
     * Prints the given class to a PrintStream.
     *
     * @param clazz
     *            the class to print
     * @param out
     *            the PrintStream to print to
     */
    public static void printClass(Class<?> clazz, PrintStream out) throws IOException {
        new ClassReader(AsmUtil.getClassAsStream(clazz)).accept(new TraceClassVisitor(new PrintWriter(new OutputStreamWriter(out))), 0);
    }

    /**
     * Generates a proper String representation of a {@link LabelNode}.
     *
     * @param labelNode
     *            the label to generate the String representation for
     * @return the String representation of the label
     */
    private static String labelNodeToString(LabelNode labelNode) {
        return labelNode.getLabel().toString();

    }

    /**
     * Generates a proper String representation of a List of {@link LabelNode}.
     *
     * @param labelNodes
     *            the labels to generate the String representation for
     * @return the String representation of the labels
     */
    private static String labelNodeListToString(List<LabelNode> labelNodes) {
        StringBuilder bob = new StringBuilder();
        bob.append("[");
        for (LabelNode ln : labelNodes) {
            bob.append(ln.getLabel().toString()).append(", ");
        }
        if (!labelNodes.isEmpty()) {
            bob.setLength(bob.length() - 2);
        }
        bob.append("]");
        return bob.toString();

    }
}
