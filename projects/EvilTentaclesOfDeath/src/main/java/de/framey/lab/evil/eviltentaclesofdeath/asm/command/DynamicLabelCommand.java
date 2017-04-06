package de.framey.lab.evil.eviltentaclesofdeath.asm.command;

import java.util.Stack;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import de.framey.lab.evil.eviltentaclesofdeath.Tentacle;
import de.framey.lab.evil.eviltentaclesofdeath.asm.AsmUtil;
import de.framey.lab.evil.eviltentaclesofdeath.asm.FrameTable;
import de.framey.lab.evil.eviltentaclesofdeath.asm.JumpTable;

/**
 * Represents a LABEL command which defines a dynamic jump target.
 * <p>
 * The inject method throws an {@link IllegalArgumentException} to prevent usage of dynamic labels. This is because implementation of a goto
 * command which supports dynamic labels would be very difficult, because goto label implementation uses LOOKUPSWITCH which can only handle
 * static int keys.
 * </p>
 *
 * @author Frank Meyfarth
 */
public class DynamicLabelCommand implements Command {

    private static final String METHOD    = "LABEL";
    private static final String SIGNATURE = AsmUtil.getMethodSignature(Tentacle.class, METHOD, String.class);

    @Override
    public boolean doesInstructionFit(AbstractInsnNode ain, FrameTable frameTable, Stack<AbstractInsnNode> instructionStack) {
        return AsmUtil.isMethodSignatureMatching(ain, METHOD, SIGNATURE)
                && !AsmUtil.areParameterValuesStatic(AsmUtil.getParamStack(instructionStack, frameTable));
    }

    @Override
    public void injectInstructions(MethodNode mn, MethodInsnNode min, JumpTable jumpTable, FrameTable frameTable,
            Stack<AbstractInsnNode> instructionStack) {
        throw new IllegalArgumentException("Only static values allowed as LABEL!");
    }
}
