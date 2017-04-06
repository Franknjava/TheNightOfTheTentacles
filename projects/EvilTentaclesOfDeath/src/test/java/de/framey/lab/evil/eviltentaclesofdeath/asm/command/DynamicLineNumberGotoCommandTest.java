package de.framey.lab.evil.eviltentaclesofdeath.asm.command;

import java.io.IOException;
import java.util.Stack;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import de.framey.lab.evil.eviltentaclesofdeath.asm.command.Commands;

public class DynamicLineNumberGotoCommandTest extends AbstractCommandTest {

    @BeforeTest
    protected void loadCode() throws IOException {
        cmd = Commands.DYNAMIC_LINE_NUMBER_GOTO.getCommand();
        super.loadCode("gotoIntDynamic");
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

    //    LABEL L1988859660
    //    LINE 28
    //      BIPUSH 29
    //      ISTORE 1
    //    LABEL L1514160588
    //    LINE 29
    //      ILOAD 1
    //      TABLESWITCH 28-30 L22756955 Labels: [L1988859660, L1514160588, L1640639994]
    //    LABEL L22756955
    //      NEW java/lang/IllegalArgumentException
    //      DUP
    //      LDC Illegal jump target!
    //      INVOKESPECIAL java/lang/IllegalArgumentException.<init> (Ljava/lang/String;)V
    //      ATHROW
    @Override
    protected void checkStack(Stack<AbstractInsnNode> stack) {
        Assert.assertTrue(stack.pop().getOpcode() == Opcodes.ATHROW);
        Assert.assertTrue(stack.pop().getOpcode() == Opcodes.INVOKESPECIAL);
        Assert.assertTrue(stack.pop().getOpcode() == Opcodes.LDC);
        Assert.assertTrue(stack.pop().getOpcode() == Opcodes.DUP);
        Assert.assertTrue(stack.pop().getOpcode() == Opcodes.NEW);
        Assert.assertTrue(stack.pop() instanceof LabelNode);
        Assert.assertTrue(stack.pop().getOpcode() == Opcodes.TABLESWITCH);
    }
}
