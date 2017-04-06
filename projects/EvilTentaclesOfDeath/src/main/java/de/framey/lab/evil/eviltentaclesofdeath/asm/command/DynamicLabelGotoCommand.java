package de.framey.lab.evil.eviltentaclesofdeath.asm.command;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.LookupSwitchInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import de.framey.lab.evil.eviltentaclesofdeath.Tentacle;
import de.framey.lab.evil.eviltentaclesofdeath.asm.AsmUtil;
import de.framey.lab.evil.eviltentaclesofdeath.asm.FrameTable;
import de.framey.lab.evil.eviltentaclesofdeath.asm.JumpTable;
import de.framey.lab.evil.eviltentaclesofdeath.util.StackUtil;

/**
 * Represents a GOTO command which jumps to a dynamic label.
 * <p>
 * Adds a LOOKUPSWITCH instruction to switch for label strings. The switch is done on hashcodes of the strings. Afterwards equals is called
 * on the strings. This is the same procedure used on Java switch statements. Default target is the last line.
 * </p>
 * TODO: Adjust according to text file!!!
 *
 * @author Frank Meyfarth
 */
public class DynamicLabelGotoCommand implements Command {

    private static final String METHOD       = "GOTO";
    private static final String SIGNATURE    = AsmUtil.getMethodSignature(Tentacle.class, METHOD, String.class);

    private static final String MTH_HASHCODE = "hashCode";
    private static final String MTH_EQUALS   = "equals";

    private static final String SGN_EQUALS   = AsmUtil.getMethodSignature(String.class, MTH_EQUALS, Object.class);
    private static final String SGN_HASHCODE = AsmUtil.getMethodSignature(String.class, MTH_HASHCODE);
    private static final String SGN_STRING   = Type.getType(String.class).getDescriptor();

    private static final String TYP_STRING   = Type.getInternalName(String.class);

    private static final String PFX_TEMP_VAR = "TEMP$";

    @Override
    public boolean doesInstructionFit(AbstractInsnNode ain, FrameTable frameTable, Stack<AbstractInsnNode> instructionStack) {
        return AsmUtil.isMethodSignatureMatching(ain, METHOD, SIGNATURE)
                && !AsmUtil.areParameterValuesStatic(AsmUtil.getParamStack(instructionStack, frameTable));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void injectInstructions(MethodNode mn, MethodInsnNode min, JumpTable jumpTable, FrameTable frameTable,
            Stack<AbstractInsnNode> instructionStack) {
        LabelNode defaultSwitchTarget = new LabelNode();
        int[] hopByKeys = generateHopByKeys(jumpTable.getLabelHashCodes());
        LabelNode[] hopByTargets = generateHopByTargets(jumpTable.getLabelHashCodes());
        int maxLocals = mn.localVariables.size();
        mn.localVariables.add(
                new LocalVariableNode(
                        PFX_TEMP_VAR + maxLocals,
                        SGN_STRING, null,
                        jumpTable.getFirstTarget(),
                        jumpTable.getLastTarget(),
                        maxLocals));

        StackUtil.pushAll(AsmUtil.removeCurrentMethodCall(instructionStack, frameTable), instructionStack);
        instructionStack.push(new InsnNode(Opcodes.DUP));
        instructionStack.push(new IntInsnNode(Opcodes.ASTORE, maxLocals));
        instructionStack.push(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, TYP_STRING, MTH_HASHCODE, SGN_HASHCODE, false));
        instructionStack.push(
                new LookupSwitchInsnNode(
                        defaultSwitchTarget,
                        hopByKeys,
                        hopByTargets));
        int i = 0;
        for (Entry<Integer, Set<String>> labelHashCode : jumpTable.getLabelHashCodes().entrySet()) {
            instructionStack.push(hopByTargets[i++]);
            for (String label : labelHashCode.getValue()) {
                instructionStack.push(new IntInsnNode(Opcodes.ALOAD, maxLocals));
                instructionStack.push(new LdcInsnNode(label));
                instructionStack.push(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, TYP_STRING, MTH_EQUALS, SGN_EQUALS, false));
                instructionStack.push(new JumpInsnNode(Opcodes.IFNE, jumpTable.get(label)));
            }
            instructionStack.push(new JumpInsnNode(Opcodes.GOTO, defaultSwitchTarget));
        }
        instructionStack.push(defaultSwitchTarget);
        AsmUtil.addThrowExceptionToStack(instructionStack, IllegalArgumentException.class, "Illegal jump target!");
    }

    private int[] generateHopByKeys(Map<Integer, Set<String>> labelHashCodes) {
        int[] keys = new int[labelHashCodes.size()];
        int i = 0;
        for (int k : labelHashCodes.keySet()) {
            keys[i++] = k;
        }
        return keys;
    }

    private LabelNode[] generateHopByTargets(Map<Integer, Set<String>> labelHashCodes) {
        LabelNode[] targets = new LabelNode[labelHashCodes.size()];
        for (int i = 0; i < labelHashCodes.size(); i++) {
            targets[i] = new LabelNode();
        }
        return targets;
    }
}
