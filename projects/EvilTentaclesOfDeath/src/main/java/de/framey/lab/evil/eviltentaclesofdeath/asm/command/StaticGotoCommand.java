package de.framey.lab.evil.eviltentaclesofdeath.asm.command;

import java.util.Stack;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import de.framey.lab.evil.eviltentaclesofdeath.asm.AsmUtil;
import de.framey.lab.evil.eviltentaclesofdeath.asm.FrameTable;
import de.framey.lab.evil.eviltentaclesofdeath.asm.JumpTable;

/**
 * An abstract super class which provides injection of static GOTO commands. *
 * <p>
 * Retrieves the static jump target label from the methods jump table and adds a GOTO instruction.
 * </p>
 *
 * @author Frank Meyfarth
 */
public abstract class StaticGotoCommand implements Command {

    @Override
    public void injectInstructions(MethodNode mn, MethodInsnNode min, JumpTable jumpTable, FrameTable frameTable,
            Stack<AbstractInsnNode> instructionStack) {
        Stack<AbstractInsnNode> paramStack = AsmUtil.removeCurrentMethodCall(instructionStack, frameTable);
        Object target = AsmUtil.getStaticParameterValues(paramStack).get(0);
        LabelNode label = jumpTable.get(target);
        instructionStack.push(new JumpInsnNode(Opcodes.GOTO, label));
    }
}
