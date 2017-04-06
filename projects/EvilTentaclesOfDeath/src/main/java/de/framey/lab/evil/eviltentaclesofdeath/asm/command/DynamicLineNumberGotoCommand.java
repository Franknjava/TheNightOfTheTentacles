package de.framey.lab.evil.eviltentaclesofdeath.asm.command;

import java.util.Stack;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;

import de.framey.lab.evil.eviltentaclesofdeath.Tentacle;
import de.framey.lab.evil.eviltentaclesofdeath.asm.AsmUtil;
import de.framey.lab.evil.eviltentaclesofdeath.asm.FrameTable;
import de.framey.lab.evil.eviltentaclesofdeath.asm.JumpTable;
import de.framey.lab.evil.eviltentaclesofdeath.util.StackUtil;

/**
 * Represents a GOTO command which jumps to a dynamic line number.
 * <p>
 * Adds a TABLESWITCH instruction with a key range from methods first to methods last line number. Default target is the last line.
 * </p>
 * TODO: Default should throw IA Ex.
 *
 * @author Frank Meyfarth
 */
public class DynamicLineNumberGotoCommand implements Command {

    private static final String METHOD    = "GOTO";
    private static final String SIGNATURE = AsmUtil.getMethodSignature(Tentacle.class, METHOD, int.class);

    @Override
    public boolean doesInstructionFit(AbstractInsnNode ain, FrameTable frameTable, Stack<AbstractInsnNode> instructionStack) {
        return AsmUtil.isMethodSignatureMatching(ain, METHOD, SIGNATURE)
                && !AsmUtil.areParameterValuesStatic(AsmUtil.getParamStack(instructionStack, frameTable));
    }

    @Override
    public void injectInstructions(MethodNode mn, MethodInsnNode min, JumpTable jumpTable, FrameTable frameTable,
            Stack<AbstractInsnNode> instructionStack) {
        LabelNode defaultSwitchTarget = new LabelNode();
        StackUtil.pushAll(AsmUtil.removeCurrentMethodCall(instructionStack, frameTable), instructionStack);
        instructionStack.push(
                new TableSwitchInsnNode(
                        jumpTable.getFirstLineNumber(),
                        jumpTable.getLastLineNumber(),
                        defaultSwitchTarget,
                        jumpTable.getLineNumberSwitchTargets()));
        instructionStack.push(defaultSwitchTarget);
        AsmUtil.addThrowExceptionToStack(instructionStack, IllegalArgumentException.class, "Illegal jump target!");
    }
}
