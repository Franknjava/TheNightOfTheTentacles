package de.framey.lab.evil.eviltentaclesofdeath.asm;

import java.util.Iterator;
import java.util.List;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.testng.Assert;
import org.testng.annotations.Test;

import de.framey.lab.evil.eviltentaclesofdeath.asm.AsmPrinter;
import de.framey.lab.evil.eviltentaclesofdeath.asm.AsmUtil;
import de.framey.lab.evil.eviltentaclesofdeath.asm.FrameTable;
import de.framey.lab.evil.eviltentaclesofdeath.testhelper.MethodInvocationVariants;

public class FrameTableTest {

    @SuppressWarnings("unchecked")
    private int printMethod(String name) throws Exception {
        int size = -1;
        ClassNode cn = AsmUtil.readClass(MethodInvocationVariants.class);
        for (MethodNode mn : (List<MethodNode>) cn.methods) {
            if (name.equals(mn.name)) {
                System.out.println(mn.name);
                FrameTable ft = new FrameTable(cn, mn);
                for (Iterator<AbstractInsnNode> iterator = mn.instructions.iterator(); iterator.hasNext();) {
                    AbstractInsnNode ain = iterator.next();
                    Integer s = ft.getParameterSize(ain);
                    StringBuilder bob = new StringBuilder();
                    bob.append(String.format("%12s ", ft.getInstructionFrame(ain)));
                    bob.append((s == null) ? " -" : String.format("%2d", s));
                    bob.append(String.format("%15s", ain.getClass().getSimpleName()));
                    AsmPrinter.printInstruction(ain, (i) -> bob.toString());
                    if (ain instanceof MethodInsnNode) {
                        size = s;
                    }
                }
                System.out.println();
            }
        }
        return size;
    }

    @Test
    public void thisDefaultNPTest() throws Exception {
        Assert.assertEquals(printMethod("thisDefaultNP"), 0);
    }

    @Test
    public void thisDefaultYPTest() throws Exception {
        Assert.assertEquals(printMethod("thisDefaultYP"), 1);
    }

    @Test
    public void thisDefaultYPDynTest() throws Exception {
        Assert.assertEquals(printMethod("thisDefaultYPDyn"), 1);
    }

    @Test
    public void superNPTest() throws Exception {
        Assert.assertEquals(printMethod("superNP"), 0);
    }

    @Test
    public void superYPTest() throws Exception {
        Assert.assertEquals(printMethod("superYP"), 1);
    }

    @Test
    public void superYPDynTest() throws Exception {
        Assert.assertEquals(printMethod("superYPDyn"), 1);
    }

    @Test
    public void thisNPTest() throws Exception {
        Assert.assertEquals(printMethod("thisNP"), 0);
    }

    @Test
    public void thisYPTest() throws Exception {
        Assert.assertEquals(printMethod("thisYP"), 1);
    }

    @Test
    public void thisYPDynTest() throws Exception {
        Assert.assertEquals(printMethod("thisYPDyn"), 1);
    }

    @Test
    public void staticNPTest() throws Exception {
        Assert.assertEquals(printMethod("staticNP"), 0);
    }

    @Test
    public void staticYPTest() throws Exception {
        Assert.assertEquals(printMethod("staticYP"), 1);
    }

    @Test
    public void staticYPDynTest() throws Exception {
        Assert.assertEquals(printMethod("staticYPDyn"), 1);
    }

    @Test
    public void staticSuperNPTest() throws Exception {
        Assert.assertEquals(printMethod("staticSuperNP"), 0);
    }

    @Test
    public void staticSuperYPTest() throws Exception {
        Assert.assertEquals(printMethod("staticSuperYP"), 1);
    }

    @Test
    public void staticSuperYPDynTest() throws Exception {
        Assert.assertEquals(printMethod("staticSuperYPDyn"), 1);
    }
}
