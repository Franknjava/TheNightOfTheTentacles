package de.framey.lab.evil.eviltentaclesofdeath.asm.command;

import java.io.IOException;
import java.util.Stack;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import de.framey.lab.evil.eviltentaclesofdeath.asm.AsmUtil;
import de.framey.lab.evil.eviltentaclesofdeath.asm.FrameTable;
import de.framey.lab.evil.eviltentaclesofdeath.asm.command.Command;
import de.framey.lab.evil.eviltentaclesofdeath.asm.command.Commands;
import de.framey.lab.evil.eviltentaclesofdeath.asm.command.DynamicLabelCommand;
import de.framey.lab.evil.eviltentaclesofdeath.asm.command.DynamicLabelGotoCommand;
import de.framey.lab.evil.eviltentaclesofdeath.asm.command.DynamicLineNumberGotoCommand;
import de.framey.lab.evil.eviltentaclesofdeath.asm.command.LineCommand;
import de.framey.lab.evil.eviltentaclesofdeath.asm.command.StaticLabelCommand;
import de.framey.lab.evil.eviltentaclesofdeath.asm.command.StaticLabelGotoCommand;
import de.framey.lab.evil.eviltentaclesofdeath.asm.command.StaticLineNumberGotoCommand;
import de.framey.lab.evil.eviltentaclesofdeath.testhelper.TentacleVariantsIllegal;

public class CommandsTest {

    private ClassNode clazz;

    @BeforeTest
    private void loadCode() throws IOException {
        clazz = AsmUtil.readClass(TentacleVariantsIllegal.class);
    }

    @Test
    public void getCommandLine() {
        checkCommandType("line", LineCommand.class);
    }

    @Test
    public void getCommandLabelStatic() {
        checkCommandType("labelStatic", StaticLabelCommand.class);
    }

    @Test
    public void getCommandLabelDynamic() {
        checkCommandType("labelDynamic", DynamicLabelCommand.class);
    }

    @Test
    public void getCommandLineNumberGotoStatic() {
        checkCommandType("gotoIntStatic", StaticLineNumberGotoCommand.class);
    }

    @Test
    public void getCommandLineNumberGotoDynamic() {
        checkCommandType("gotoIntDynamic", DynamicLineNumberGotoCommand.class);
    }

    @Test
    public void getCommandLabelGotoStatic() {
        checkCommandType("gotoStringStatic", StaticLabelGotoCommand.class);
    }

    @Test
    public void getCommandLabelGotoDynamic() {
        checkCommandType("gotoStringDynamic", DynamicLabelGotoCommand.class);
    }

    private void checkCommandType(String name, Class<? extends Command> command) {
        MethodNode mn = AsmUtil.getMethod(clazz, name, "()V");
        FrameTable ft = new FrameTable(clazz, mn);
        Stack<AbstractInsnNode> istack = getInstructionStack(mn.instructions);
        Command cmd = Commands.getCommand(istack.peek(), ft, istack);
        Assert.assertEquals(command, cmd.getClass());
    }

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
