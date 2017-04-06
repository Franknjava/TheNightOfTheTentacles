package de.framey.lab.evil.eviltentaclesofdeath.asm.command;

import java.util.Stack;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import de.framey.lab.evil.eviltentaclesofdeath.Tentacle;
import de.framey.lab.evil.eviltentaclesofdeath.asm.AsmUtil;
import de.framey.lab.evil.eviltentaclesofdeath.asm.FrameTable;
import de.framey.lab.evil.eviltentaclesofdeath.asm.JumpTable;

/**
 * Represents the LINE command which returns the current line number.
 * <p>
 * Retrieves the current line number from bytecode and adds an LDC statement to provide it.
 * </p>
 *
 * @author Frank Meyfarth
 */
public class LineCommand implements Command {

    private static final String METHOD    = "LINE";
    private static final String SIGNATURE = AsmUtil.getMethodSignature(Tentacle.class, METHOD);

    @Override
    public boolean doesInstructionFit(AbstractInsnNode ain, FrameTable frameTable, Stack<AbstractInsnNode> instructionStack) {
        return AsmUtil.isMethodSignatureMatching(ain, METHOD, SIGNATURE);
    }

    @Override
    public void injectInstructions(MethodNode mn, MethodInsnNode min, JumpTable jumpTable, FrameTable frameTable,
            Stack<AbstractInsnNode> instructionStack) {
        AsmUtil.removeCurrentMethodCall(instructionStack, frameTable);
        int line = (AsmUtil.findPreviousNode(min, LineNumberNode.class)).line;
        instructionStack.push(new LdcInsnNode(line));
    }
}
