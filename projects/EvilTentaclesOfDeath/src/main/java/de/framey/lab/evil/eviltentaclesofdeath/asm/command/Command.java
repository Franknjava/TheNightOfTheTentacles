package de.framey.lab.evil.eviltentaclesofdeath.asm.command;

import java.util.Stack;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import de.framey.lab.evil.eviltentaclesofdeath.asm.FrameTable;
import de.framey.lab.evil.eviltentaclesofdeath.asm.JumpTable;

/**
 * Describes a command which corresponds to a certain default method call and replaces it by its bytecode. Each command must have a unique
 * instruction type, identifying the method call instruction it replaces.
 *
 * @author Frank Meyfarth
 */
public interface Command {

    /**
     * Returns TRUE, if the instruction fits this command.
     *
     * @param ain
     *            the method instruction to derive the type from
     * @param frameTable
     *            the methods frame table
     * @param instructionStack
     *            the current instruction stack
     * @return TRUE, if the instruction fits this command
     */
    boolean doesInstructionFit(AbstractInsnNode ain, FrameTable frameTable, Stack<AbstractInsnNode> instructionStack);

    /**
     * Replaces the given method instruction in the given method with the bytecode representing this command.
     *
     * @param mn
     *            the method to inject the command to
     * @param min
     *            the default method call to replace
     * @param jumpTable
     *            the methods jump table
     * @param frameTable
     *            the methods frame table
     * @param instructionStack
     *            the current instruction stack
     */
    void injectInstructions(MethodNode mn, MethodInsnNode min, JumpTable jumpTable, FrameTable frameTable,
            Stack<AbstractInsnNode> instructionStack);

}
