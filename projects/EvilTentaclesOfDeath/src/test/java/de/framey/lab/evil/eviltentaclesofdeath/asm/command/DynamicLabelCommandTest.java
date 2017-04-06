package de.framey.lab.evil.eviltentaclesofdeath.asm.command;

import java.io.IOException;
import java.util.Stack;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import de.framey.lab.evil.eviltentaclesofdeath.asm.command.Commands;

public class DynamicLabelCommandTest extends AbstractCommandTest {

    @BeforeTest
    protected void loadCode() throws IOException {
        cmd = Commands.DYNAMIC_LABEL.getCommand();
        super.loadCode("labelDynamic");
    }

    @Override
    @Test
    public void doesInstructionFit() {
        super.doesInstructionFit();
    }

    @Override
    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void injectInstructions() {
        super.injectInstructions();
    }

    @Override
    protected void checkStack(Stack<AbstractInsnNode> stack) {
        Assert.fail("This method should not be called, because dynamic labels are not allowed!");
    }
}
