package de.framey.lab.evil.eviltentaclesofdeath.asm.command;

import java.io.IOException;
import java.util.Stack;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.testng.Assert;

import de.framey.lab.evil.eviltentaclesofdeath.asm.AsmUtil;
import de.framey.lab.evil.eviltentaclesofdeath.asm.FrameTable;
import de.framey.lab.evil.eviltentaclesofdeath.asm.JumpTable;
import de.framey.lab.evil.eviltentaclesofdeath.asm.command.Command;
import de.framey.lab.evil.eviltentaclesofdeath.asm.command.Commands;
import de.framey.lab.evil.eviltentaclesofdeath.testhelper.TentacleVariantsIllegal;

public abstract class AbstractCommandTest {

    protected Command  cmd;

    private ClassNode  cn;
    private MethodNode mn;
    private JumpTable  jt;
    private FrameTable ft;

    protected void loadCode(String name) throws IOException {
        cn = AsmUtil.readClass(TentacleVariantsIllegal.class);
        mn = AsmUtil.getMethod(cn, name, "()V");
        jt = new JumpTable(mn);
        ft = new FrameTable(cn, mn);
    }

    public void doesInstructionFit() {
        Stack<AbstractInsnNode> stack = getInstructionStack(mn.instructions);
        AbstractInsnNode ain = stack.peek();
        for (Commands c : Commands.values()) {
            Assert.assertEquals(c.getCommand().doesInstructionFit(ain, ft, stack), cmd.getClass().equals(c.getCommand().getClass()));
        }
    }

    public void injectInstructions() {
        Stack<AbstractInsnNode> stack = getInstructionStack(mn.instructions);
        cmd.injectInstructions(mn, (MethodInsnNode) stack.peek(), jt, ft, stack);
        checkStack(stack);
    }

    protected abstract void checkStack(Stack<AbstractInsnNode> stack);

    private Stack<AbstractInsnNode> getInstructionStack(InsnList instructions) {
        Stack<AbstractInsnNode> stack = new Stack<>();
        for (int i = 0; i < instructions.size(); i++) {
            stack.push(instructions.get(i));
            if (instructions.get(i) instanceof MethodInsnNode) {
                break;
            }
        }
        return stack;
    }
}
