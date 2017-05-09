package de.framey.lab.evil.eviltentaclesofdeath.asm;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import de.framey.lab.evil.eviltentaclesofdeath.Tentacle;
import de.framey.lab.evil.eviltentaclesofdeath.asm.command.Command;
import de.framey.lab.evil.eviltentaclesofdeath.asm.command.Commands;
import de.framey.lab.evil.eviltentaclesofdeath.asm.command.GlobalCommand;
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

        Map<Object, GlobalCommand<?>> globalCommands = new HashMap<>();

        for (MethodNode mn : (List<MethodNode>) cn.methods) {
            JumpTable jumpTable = new JumpTable(mn);
            FrameTable frameTable = new FrameTable(cn, mn);
            transformMethod(globalCommands, mn, jumpTable, frameTable);
        }

        for (Entry<Object, GlobalCommand<?>> gc : globalCommands.entrySet()) {
            gc.getValue().applyGlobalChangesRaw(cn, gc.getKey());
        }
        
        cn.interfaces.remove(TYP_TENTACLE);

        return AsmUtil.writeClass(cn);
    }

    /**
     * Checks all instructions of the given method and replaces the dummy calls of {@link Tentacle} with their real sinister actions.
     * 
     * @param globalCommands
     *            a map of commands which apply global changes to the class
     * @param mn
     *            the method to transform
     * @param jumpTable
     *            the jump table to use to calculate jump targets
     * @param frameTable
     *            the frame table to use to isolate method parameter stacks
     */
    @SuppressWarnings("unchecked")
    private static void transformMethod(Map<Object, GlobalCommand<?>> globalCommands, MethodNode mn, JumpTable jumpTable,
            FrameTable frameTable) {
        Stack<AbstractInsnNode> intructionStack = new Stack<>();
        for (Iterator<AbstractInsnNode> iterator = mn.instructions.iterator(); iterator.hasNext();) {
            AbstractInsnNode ain = iterator.next();
            intructionStack.push(ain);
            transformLatestInstruction(globalCommands, mn, ain, jumpTable, frameTable, intructionStack);
        }
        mn.instructions = AsmUtil.toInsnList(intructionStack);
    }

    /**
     * Replaces the current instruction with its real sinister action, if it is a supported {@link Tentacle} command.
     * 
     * @param globalCommands
     *            a map of commands which apply global changes to the class
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
    private static void transformLatestInstruction(Map<Object, GlobalCommand<?>> globalCommands, MethodNode mn, AbstractInsnNode ain,
            JumpTable jumpTable,
            FrameTable frameTable, Stack<AbstractInsnNode> instructionStack) {

        Command cmd = Commands.getCommand(ain, frameTable, instructionStack);

        if (cmd != null) {
            cmd.injectInstructions(mn, (MethodInsnNode) ain, jumpTable, frameTable, instructionStack);

            if (cmd instanceof GlobalCommand) {
                globalCommands.put(((GlobalCommand<?>) cmd).getCommandKey(mn), (GlobalCommand<?>) cmd);
            }
        }
    }
}
