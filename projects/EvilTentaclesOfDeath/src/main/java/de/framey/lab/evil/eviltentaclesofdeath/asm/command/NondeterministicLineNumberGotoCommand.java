package de.framey.lab.evil.eviltentaclesofdeath.asm.command;

import java.util.Stack;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import de.framey.lab.evil.eviltentaclesofdeath.Tentacle;
import de.framey.lab.evil.eviltentaclesofdeath.asm.AsmUtil;
import de.framey.lab.evil.eviltentaclesofdeath.asm.FrameTable;
import de.framey.lab.evil.eviltentaclesofdeath.asm.JumpTable;
import de.framey.lab.evil.eviltentaclesofdeath.asm.command.NondeterministicLineNumberGotoCommand.Key;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Represents the STATE command which returns the current method state.
 * <p>
 * Retrieves the current method state from the current stack map frame and puts it into an Array to provide it.
 * </p>
 *
 * @author Frank Meyfarth
 */
public class NondeterministicLineNumberGotoCommand implements GlobalCommand<Key> {

    @Getter
    @EqualsAndHashCode
    @AllArgsConstructor
    public static final class Key {
        private String signature;
    }

    private static final String METHOD    = "GOTO_ANY_ONE_OF";
    private static final String SIGNATURE = AsmUtil.getMethodSignature(Tentacle.class, METHOD);

    @Override
    public boolean doesInstructionFit(AbstractInsnNode ain, FrameTable frameTable, Stack<AbstractInsnNode> instructionStack) {
        return AsmUtil.isMethodSignatureMatching(ain, METHOD, SIGNATURE);
    }

    @Override
    public void injectInstructions(MethodNode mn, MethodInsnNode min, JumpTable jumpTable, FrameTable frameTable,
            Stack<AbstractInsnNode> instructionStack) {
        AsmUtil.removeCurrentMethodCall(instructionStack, frameTable);
        // TODO Call method from thread
    }

    @Override
    public Key getCommandKey(MethodNode mn) {
        return new Key(mn.signature);
    }

    @Override
    public void applyGlobalChanges(ClassNode cn, Key key) {
        // TODO Add wrapper and threadable method

    }
}
