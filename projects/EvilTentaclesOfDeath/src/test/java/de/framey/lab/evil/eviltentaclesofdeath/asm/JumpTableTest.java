package de.framey.lab.evil.eviltentaclesofdeath.asm;

import java.util.List;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodNode;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import de.framey.lab.evil.eviltentaclesofdeath.asm.AsmUtil;
import de.framey.lab.evil.eviltentaclesofdeath.asm.JumpTable;
import de.framey.lab.evil.eviltentaclesofdeath.testhelper.JumpTargetVariants;
import junit.framework.Assert;

public class JumpTableTest {

    private ClassNode cn;

    @BeforeTest
    private void initCLassNode() throws Exception {
        cn = AsmUtil.readClass(JumpTargetVariants.class);
    }

    @SuppressWarnings("unchecked")
    private MethodNode getMethodNode(String name) {
        for (MethodNode mn : (List<MethodNode>) cn.methods) {
            if (name.equals(mn.name)) {
                return mn;
            }
        }
        return null;
    }

    @Test
    public void getLineNumberValid() {
        JumpTable jt = new JumpTable(getMethodNode("lineNumberTestMethod"));
        Assert.assertNotNull(jt.get(12));
        Assert.assertNotNull(jt.get(13));
        Assert.assertNotNull(jt.get(14));
        Assert.assertNotNull(jt.get(15));
    }

    @Test
    public void getLabelValid() {
        JumpTable jt = new JumpTable(getMethodNode("labelTestMethod"));
        Assert.assertNotNull(jt.get("first"));
        Assert.assertNotNull(jt.get("inbetween"));
        Assert.assertNotNull(jt.get("last"));
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void getLineNumberInvalid() {
        JumpTable jt = new JumpTable(getMethodNode("lineNumberTestMethod"));
        jt.get(8);
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void getLabelInvalid() {
        JumpTable jt = new JumpTable(getMethodNode("labelTestMethod"));
        jt.get("fhtagn");
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void getTypeInvalid() {
        JumpTable jt = new JumpTable(getMethodNode("labelTestMethod"));
        jt.get(new JumpTargetVariants());
    }

    @Test
    public void getDefaultSwitchTarget() {
        MethodNode mn = getMethodNode("lineNumberTestMethod");
        JumpTable jt = new JumpTable(mn);
        Assert.assertEquals(jt.getLastTarget(), jt.getLastTarget());
    }

    @Test
    public void getFirstLineNumber() {
        JumpTable jt = new JumpTable(getMethodNode("lineNumberTestMethod"));
        Assert.assertEquals(12, jt.getFirstLineNumber());
    }

    @Test
    public void getFirstTarget() {
        MethodNode mn = getMethodNode("lineNumberTestMethod");
        JumpTable jt = new JumpTable(mn);
        Assert.assertEquals(mn.instructions.getFirst(), jt.getFirstTarget());
    }

    @Test
    public void getLastLineNumber() {
        JumpTable jt = new JumpTable(getMethodNode("lineNumberTestMethod"));
        Assert.assertEquals(15, jt.getLastLineNumber());
    }

    @Test
    public void getLastTarget() {
        MethodNode mn = getMethodNode("lineNumberTestMethod");
        JumpTable jt = new JumpTable(mn);
        Assert.assertEquals(AsmUtil.findPreviousNode(mn.instructions.getLast().getPrevious(), LabelNode.class), jt.getLastTarget());
    }

    @Test
    public void getLineNumberSwitchTargets() {
        MethodNode mn = getMethodNode("lineNumberTestMethod");
        JumpTable jt = new JumpTable(mn);
        LabelNode[] lns = jt.getLineNumberSwitchTargets();
        Assert.assertEquals(4, lns.length);
        AbstractInsnNode current = mn.instructions.getFirst();
        for (LabelNode ln : lns) {
            current = AsmUtil.findNextNode(current, LabelNode.class);
            Assert.assertEquals(ln, current);
            current = current.getNext();
        }
    }
}
