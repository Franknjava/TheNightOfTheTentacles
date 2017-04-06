package de.framey.lab.evil.eviltentaclesofdeath.asm.command;

import java.util.Stack;

import org.objectweb.asm.tree.AbstractInsnNode;

import de.framey.lab.evil.eviltentaclesofdeath.Tentacle;
import de.framey.lab.evil.eviltentaclesofdeath.asm.AsmUtil;
import de.framey.lab.evil.eviltentaclesofdeath.asm.FrameTable;

/**
 * Represents a GOTO command which jumps to a static line number.
 *
 * @author Frank Meyfarth
 */
public class StaticLineNumberGotoCommand extends StaticGotoCommand {

    private static final String METHOD    = "GOTO";
    private static final String SIGNATURE = AsmUtil.getMethodSignature(Tentacle.class, METHOD, int.class);

    @Override
    public boolean doesInstructionFit(AbstractInsnNode ain, FrameTable frameTable, Stack<AbstractInsnNode> instructionStack) {
        return AsmUtil.isMethodSignatureMatching(ain, METHOD, SIGNATURE)
                && AsmUtil.areParameterValuesStatic(AsmUtil.getParamStack(instructionStack, frameTable));
    }
}
