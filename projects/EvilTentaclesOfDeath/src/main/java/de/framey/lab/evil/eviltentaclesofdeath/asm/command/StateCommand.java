package de.framey.lab.evil.eviltentaclesofdeath.asm.command;

import java.util.Stack;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.analysis.BasicValue;
import org.objectweb.asm.tree.analysis.Frame;

import de.framey.lab.evil.eviltentaclesofdeath.Tentacle;
import de.framey.lab.evil.eviltentaclesofdeath.asm.AsmUtil;
import de.framey.lab.evil.eviltentaclesofdeath.asm.FrameTable;
import de.framey.lab.evil.eviltentaclesofdeath.asm.JumpTable;

/**
 * Represents the STATE command which returns the current method state.
 * <p>
 * Retrieves the current method state from the current stack map frame and puts it into an Array to provide it.
 * </p>
 *
 * @author Frank Meyfarth
 */
public class StateCommand implements Command {

    private static final String METHOD    = "STATE";
    private static final String SIGNATURE = AsmUtil.getMethodSignature(Tentacle.class, METHOD);

    @Override
    public boolean doesInstructionFit(AbstractInsnNode ain, FrameTable frameTable, Stack<AbstractInsnNode> instructionStack) {
        return AsmUtil.isMethodSignatureMatching(ain, METHOD, SIGNATURE);
    }

    @Override
    public void injectInstructions(MethodNode mn, MethodInsnNode min, JumpTable jumpTable, FrameTable frameTable,
            Stack<AbstractInsnNode> instructionStack) {
        Frame frame = frameTable.getInstructionFrame(instructionStack.peek());
        AsmUtil.removeCurrentMethodCall(instructionStack, frameTable);
        instructionStack.push(new LdcInsnNode(frame.getLocals()));
        instructionStack.push(new TypeInsnNode(Opcodes.ANEWARRAY, Type.getInternalName(Object[].class)));
        for (int i = 0; i < frame.getLocals(); i++) {
            Type localType = ((BasicValue) frame.getLocal(i)).getType();
            if (localType != null) {
                instructionStack.push(new InsnNode(Opcodes.DUP));
                instructionStack.push(new LdcInsnNode(i));
                instructionStack.push(new LdcInsnNode(2));
                instructionStack.push(new TypeInsnNode(Opcodes.ANEWARRAY, Type.getInternalName(Object.class)));
                instructionStack.push(new InsnNode(Opcodes.DUP));
                instructionStack.push(new LdcInsnNode(0));
                instructionStack.push(new LdcInsnNode(localType.getDescriptor()));
                instructionStack.push(new InsnNode(Opcodes.AASTORE));
                instructionStack.push(new InsnNode(Opcodes.DUP));
                instructionStack.push(new LdcInsnNode(1));
                pushVariableValue(instructionStack, i, localType);
                instructionStack.push(new InsnNode(Opcodes.AASTORE));
                instructionStack.push(new InsnNode(Opcodes.AASTORE));
            }
        }
    }

    private void pushVariableValue(Stack<AbstractInsnNode> instructionStack, int i, Type localType) {
        switch (localType.toString()) {
        case "I":
            pushWithAutoBoxing(instructionStack, i, Opcodes.ILOAD, Integer.class, int.class);
            break;
        case "J":
            pushWithAutoBoxing(instructionStack, i, Opcodes.LLOAD, Long.class, long.class);
            break;
        case "F":
            pushWithAutoBoxing(instructionStack, i, Opcodes.FLOAD, Float.class, float.class);
            break;
        case "D":
            pushWithAutoBoxing(instructionStack, i, Opcodes.DLOAD, Double.class, double.class);
            break;
        default:
            instructionStack.push(new IntInsnNode(Opcodes.ALOAD, i));
            break;
        }
    }

    private void pushWithAutoBoxing(Stack<AbstractInsnNode> instructionStack, int index, int opcode, Class<?> boxingClass,
            Class<?> primitiveClass) {
        try {
            instructionStack.push(new IntInsnNode(opcode, index));
            instructionStack.push(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(boxingClass), "valueOf",
                    Type.getMethodDescriptor(boxingClass.getMethod("valueOf", primitiveClass)), false));
        } catch (NoSuchMethodException ex) {
            throw new IllegalStateException("Autoboxing method not found for: " + boxingClass.getSimpleName(), ex);
        }
    }
}
