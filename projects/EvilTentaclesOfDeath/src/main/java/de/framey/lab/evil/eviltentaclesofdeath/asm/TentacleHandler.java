package de.framey.lab.evil.eviltentaclesofdeath.asm;

import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import de.framey.lab.evil.eviltentaclesofdeath.Tentacle;
import de.framey.lab.evil.eviltentaclesofdeath.asm.command.Command;
import de.framey.lab.evil.eviltentaclesofdeath.asm.command.Commands;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * This handles all of your tiny squishy tentacles and brings them to life.
 * <p>
 * The handler takes the bytes of a class and searches all methods for supported tentacle commands. Just to replace them witch something
 * different, something better, something to change the world forever, MUHAHAHAHA! To make the world a liitle bit more cthulhish and evil.
 * </p>
 *
 * @author Frank Meyfarth
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TentacleHandler {

    /** Typename of tentacle enabled classes. */
    private static final String TYP_TENTACLE = Type.getInternalName(Tentacle.class);

    /**
     * Transforms the given bytes of a class into a class with functioning tentacles, if the class is of type {@link Tentacle}.
     *
     * @param code
     *            the bytecode to transform
     * @return the transformed bytecode
     */
    @SuppressWarnings("unchecked")
    public static byte[] transform(byte[] code) {
        ClassNode cn = AsmUtil.readClass(code);

        if (!cn.interfaces.contains(TYP_TENTACLE)) {
            return code;
        }

        for (MethodNode mn : (List<MethodNode>) cn.methods) {
            JumpTable jumpTable = new JumpTable(mn);
            FrameTable frameTable = new FrameTable(cn, mn);
            transformMethod(mn, jumpTable, frameTable);
        }

        cn.interfaces.remove(TYP_TENTACLE);

        return AsmUtil.writeClass(cn);
    }

    /**
     * Checks all instructions of the given method and replaces the dummy calls of {@link Tentacle} with their real sinister actions.
     *
     * @param mn
     *            the method to transform
     * @param jumpTable
     *            the jump table to use to calculate jump targets
     * @param frameTable
     *            the frame table to use to isolate method parameter stacks
     */
    @SuppressWarnings("unchecked")
    private static void transformMethod(MethodNode mn, JumpTable jumpTable, FrameTable frameTable) {
        Stack<AbstractInsnNode> intructionStack = new Stack<>();
        for (Iterator<AbstractInsnNode> iterator = mn.instructions.iterator(); iterator.hasNext();) {
            AbstractInsnNode ain = iterator.next();
            intructionStack.push(ain);
            transformLatestInstruction(mn, ain, jumpTable, frameTable, intructionStack);
        }
        mn.instructions = AsmUtil.toInsnList(intructionStack);
    }

    /**
     * Replaces the current instruction with its real sinister action, if it is a supported {@link Tentacle} command.
     *
     * @param mn
     *            the current method
     * @param ain
     *            the current instruction
     * @param jumpTable
     *            the jump table to use to calculate jump targets
     * @param frameTable
     *            the frame table to use to isolate method parameter stacks
     * @param instructionStack
     *            the transformed instruction stack built so far
     */
    private static void transformLatestInstruction(MethodNode mn, AbstractInsnNode ain, JumpTable jumpTable, FrameTable frameTable,
            Stack<AbstractInsnNode> instructionStack) {

        Command cmd = Commands.getCommand(ain, frameTable, instructionStack);

        if (cmd != null) {
            cmd.injectInstructions(mn, (MethodInsnNode) ain, jumpTable, frameTable, instructionStack);
        }
    }
}
