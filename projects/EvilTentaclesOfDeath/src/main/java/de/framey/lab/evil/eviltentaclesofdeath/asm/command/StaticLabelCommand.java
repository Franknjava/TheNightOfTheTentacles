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
 * Represents a LABEL command which defines a static jump target.
 * <p>
 * The calls to LABEL simply get removed, because JumpTable already registered all jump targets of the method.
 * </p>
 *
 * @author Frank Meyfarth
 */
public class StaticLabelCommand implements Command {

    private static final String METHOD    = "LABEL";
    private static final String SIGNATURE = AsmUtil.getMethodSignature(Tentacle.class, METHOD, String.class);

    @Override
    public boolean doesInstructionFit(AbstractInsnNode ain, FrameTable frameTable, Stack<AbstractInsnNode> instructionStack) {
        return AsmUtil.isMethodSignatureMatching(ain, METHOD, SIGNATURE)
                && AsmUtil.areParameterValuesStatic(AsmUtil.getParamStack(instructionStack, frameTable));
    }

    @Override
    public void injectInstructions(MethodNode mn, MethodInsnNode min, JumpTable jumpTable, FrameTable frameTable,
            Stack<AbstractInsnNode> instructionStack) {
        AsmUtil.removeCurrentMethodCall(instructionStack, frameTable);
    }
}
