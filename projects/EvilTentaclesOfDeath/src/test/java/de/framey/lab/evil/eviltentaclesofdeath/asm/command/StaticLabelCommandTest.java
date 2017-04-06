package de.framey.lab.evil.eviltentaclesofdeath.asm.command;

import java.io.IOException;
import java.util.Stack;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import de.framey.lab.evil.eviltentaclesofdeath.asm.command.Commands;

public class StaticLabelCommandTest extends AbstractCommandTest {

    @BeforeTest
    protected void loadCode() throws IOException {
        cmd = Commands.STATIC_LABEL.getCommand();
        super.loadCode("labelStatic");
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
        Assert.assertTrue(stack.peek() instanceof LineNumberNode);
    }
}
