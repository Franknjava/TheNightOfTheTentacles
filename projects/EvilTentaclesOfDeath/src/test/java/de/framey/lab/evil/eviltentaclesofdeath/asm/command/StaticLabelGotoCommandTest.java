package de.framey.lab.evil.eviltentaclesofdeath.asm.command;

import java.io.IOException;
import java.util.Stack;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import de.framey.lab.evil.eviltentaclesofdeath.asm.command.Commands;

public class StaticLabelGotoCommandTest extends AbstractCommandTest {

    @BeforeTest
    protected void loadCode() throws IOException {
        cmd = Commands.STATIC_LABEL_GOTO.getCommand();
        super.loadCode("gotoStringStatic");
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

    @Override
    protected void checkStack(Stack<AbstractInsnNode> stack) {
        Assert.assertTrue(stack.peek() instanceof JumpInsnNode);
    }
}
