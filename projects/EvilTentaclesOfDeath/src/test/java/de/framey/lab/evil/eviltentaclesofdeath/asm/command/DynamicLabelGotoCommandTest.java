package de.framey.lab.evil.eviltentaclesofdeath.asm.command;

import java.io.IOException;
import java.util.Stack;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LookupSwitchInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import de.framey.lab.evil.eviltentaclesofdeath.asm.AsmPrinter;
import de.framey.lab.evil.eviltentaclesofdeath.asm.command.Commands;

public class DynamicLabelGotoCommandTest extends AbstractCommandTest {

    @BeforeTest
    protected void loadCode() throws IOException {
        cmd = Commands.DYNAMIC_LABEL_GOTO.getCommand();
        super.loadCode("gotoStringDynamic");
    }

    @Override
    @Test
    public void doesInstructionFit() {
        super.doesInstructionFit();
    }

    @Override
    @Test()
    public void injectInstructions() {
        super.injectInstructions();
    }

    //    LABEL L1159114532
    //    LINE 38
    //      LDC label
    //      ASTORE 1
    //    LABEL L1256728724
    //    LINE 39
    //      ALOAD 1
    //      DUP
    //      ASTORE 2
    //      INVOKEVIRTUAL java/lang/String.hashCode ()I
    //      LOOKUPSWITCH L1412925683 Keys: [102727412] Labels: [L1832580921]
    //    LABEL L1832580921
    //      ALOAD 2
    //      LDC label
    //      INVOKEVIRTUAL java/lang/String.equals (Ljava/lang/Object;)Z
    //      IFNE L497359413
    //      GOTO L1412925683
    //    LABEL L1412925683
    //      NEW java/lang/IllegalArgumentException
    //      DUP
    //      LDC Illegal jump target!
    //      INVOKESPECIAL java/lang/IllegalArgumentException.<init> (Ljava/lang/String;)V
    //      ATHROW
    @Override
    protected void checkStack(Stack<AbstractInsnNode> stack) {
        AsmPrinter.printInstructions(stack, i -> "");
        Stack<AbstractInsnNode> buffer = new Stack<>();
        for (AbstractInsnNode ain : stack) {
            if (ain instanceof LookupSwitchInsnNode) {
                Assert.assertTrue(buffer.pop() instanceof MethodInsnNode);
                Assert.assertTrue(buffer.pop().getOpcode() == Opcodes.ASTORE);
                Assert.assertTrue(buffer.pop().getOpcode() == Opcodes.DUP);
                Assert.assertTrue(buffer.pop().getOpcode() == Opcodes.ALOAD);
                break;
            } else {
                buffer.push(ain);
            }
        }
        Assert.assertTrue(stack.pop().getOpcode() == Opcodes.ATHROW);
        Assert.assertTrue(stack.pop().getOpcode() == Opcodes.INVOKESPECIAL);
        Assert.assertTrue(stack.pop().getOpcode() == Opcodes.LDC);
        Assert.assertTrue(stack.pop().getOpcode() == Opcodes.DUP);
        Assert.assertTrue(stack.pop().getOpcode() == Opcodes.NEW);
        Assert.assertTrue(stack.pop() instanceof LabelNode);
        Assert.assertTrue(stack.pop().getOpcode() == Opcodes.GOTO);
        Assert.assertTrue(stack.pop().getOpcode() == Opcodes.IFNE);
        Assert.assertTrue(stack.pop().getOpcode() == Opcodes.INVOKEVIRTUAL);
        Assert.assertTrue(stack.pop().getOpcode() == Opcodes.LDC);
        Assert.assertTrue(stack.pop().getOpcode() == Opcodes.ALOAD);
        Assert.assertTrue(stack.pop() instanceof LabelNode);
        Assert.assertTrue(stack.pop().getOpcode() == Opcodes.LOOKUPSWITCH);
    }
}
