package de.framey.lab.evil.eviltentaclesofdeath.asm;

import java.util.IdentityHashMap;
import java.util.Map;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.*;

/**
 * For seperating the bytecode used for generation of the parameters for a method call from the method call itself, it is necessary to
 * determine how many instructions before the actual method call are used to push the method parameters to the operand stack. This can be
 * alot, because parameter declarations can contain complex instructions such as method calls themself.
 * <p>
 * For each instruction of the given method the number of instructions used for building up the parameter stack is stored.
 * </p>
 *
 * @author Frank Meyfarth
 */
public class FrameTable {

    /** Maps any instruction of the method to the number of instructions used to build up its parameter stack. */
    private final Map<AbstractInsnNode, Integer>                parameterSizes = new IdentityHashMap<>();

    /** Maps any instruction of the method to the frame calculated by the {@link Analyzer}. */
    private final Map<AbstractInsnNode, Frame<? extends Value>> insnFrames     = new IdentityHashMap<>();

    /**
     * Builds up the {@link FrameTable} for the given method of the given class.
     *
     * @param cn
     *            the class node within the method resides
     * @param mn
     *            the method node to analyse
     */
    public FrameTable(ClassNode cn, MethodNode mn) {
        try {
            Analyzer<BasicValue> a = new Analyzer<>(new BasicInterpreter());
            Frame<? extends Value>[] frames = a.analyze(cn.name, mn);
            for (int i = 1; i < frames.length - 1; i++) {
                int ref = i - 1;
                for (int j = i - 1; (j > 0) && (frames[j].getStackSize() > frames[i].getStackSize()); j--) {
                    ref = j - 1;
                }
                insnFrames.put(mn.instructions.get(i - 1), frames[i]);
                if (frames[i].getStackSize() <= frames[i - 1].getStackSize()) {
                    int size = (mn.instructions.get(i - 1).getOpcode() == Opcodes.INVOKESTATIC) ? i - ref - 1 : i - ref - 2;
                    parameterSizes.put(mn.instructions.get(i - 1), size);
                }
            }
        } catch (AnalyzerException e) {
            throw new IllegalStateException("Unable to analyze method bytecode: " + cn.name + "." + mn.name + mn.desc, e);
        }
    }

    /**
     * Retrieves the {@link Frame} calculated by the {@link Analyzer} for this instruction.
     *
     * @param ain
     *            the instruction to get the {@link Frame} for
     * @return the {@link Frame} for this instruction
     */
    public Frame<? extends Value> getInstructionFrame(AbstractInsnNode ain) {
        return insnFrames.get(ain);
    }

    /**
     * Retrieves the number of instructions used to build up the parameter stack of the given instruction.
     *
     * @param ain
     *            the instruction to get the parameter stack size for
     * @return the number of instructions used to build up the parameter stack of the given instruction
     */
    public Integer getParameterSize(AbstractInsnNode ain) {
        return parameterSizes.get(ain);
    }
}
