package de.framey.lab.evil.eviltentaclesofdeath.asm;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Stack;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import de.framey.lab.evil.eviltentaclesofdeath.asm.AsmPrinter;
import de.framey.lab.evil.eviltentaclesofdeath.asm.AsmUtil;
import de.framey.lab.evil.eviltentaclesofdeath.asm.FrameTable;
import de.framey.lab.evil.eviltentaclesofdeath.testhelper.Simple;

public class AsmUtilTest {

    private byte[]     code;
    private ClassNode  clazz;
    private MethodNode method;

    @BeforeTest
    private void loadCode() throws IOException {
        code = AsmUtil.writeClass(AsmUtil.readClass(Simple.class));
        clazz = AsmUtil.readClass(Simple.class);
        method = (MethodNode) clazz.methods.get(1);
    }

    @Test
    public void areParameterValuesStatic() {
        Stack<AbstractInsnNode> stack = new Stack<>();
        AsmPrinter.printMethod(method, (i) -> "");
        Assert.assertFalse(AsmUtil.areParameterValuesStatic(stack));
        stack.push(method.instructions.get(8));
        stack.push(method.instructions.get(9));
        stack.push(method.instructions.get(10));
        Assert.assertFalse(AsmUtil.areParameterValuesStatic(stack));
        stack.clear();
        stack.push(new LdcInsnNode("value"));
        stack.push(new IntInsnNode(Opcodes.ISTORE, 911));
        Assert.assertTrue(AsmUtil.areParameterValuesStatic(stack));
    }

    @Test
    public void findFirstNode() {
        LineNumberNode node = AsmUtil.findFirstNode(method, LineNumberNode.class);
        Assert.assertEquals(node.line, 11);
    }

    @Test
    public void findLastNode() {
        LineNumberNode node = AsmUtil.findLastNode(method, LineNumberNode.class);
        Assert.assertEquals(node.line, 13);
    }

    @Test
    public void findNextNode() {
        LineNumberNode node = AsmUtil.findNextNode(AsmUtil.findFirstNode(method, LineNumberNode.class).getNext(), LineNumberNode.class);
        Assert.assertEquals(node.line, 12);
    }

    @Test
    public void findPreviousNode() {
        LineNumberNode node = AsmUtil.findPreviousNode(AsmUtil.findLastNode(method, LineNumberNode.class).getPrevious(),
                LineNumberNode.class);
        Assert.assertEquals(node.line, 12);
    }

    @Test
    public void getClassAsStream() throws IOException {
        try (InputStream is1 = AsmUtil.getClassAsStream(Simple.class)) {
            try (InputStream is2 = Simple.class.getClassLoader()
                    .getResourceAsStream(Simple.class.getName().replace(".", "/") + ".class")) {
                int b1 = -1, b2 = -1;
                while ((b1 = is1.read()) != -1 | (b2 = is2.read()) != -1) {
                    Assert.assertEquals(b1, b2);
                }
            }
        }
    }

    @Test
    public void getMethod() {
        Assert.assertEquals(method, AsmUtil.getMethod(clazz, "simpleMethod", "()V"));
    }

    @Test
    public void getMethodSignature() {
        Assert.assertEquals("()V", AsmUtil.getMethodSignature(Simple.class, "simpleMethod"));
        Assert.assertEquals("(IJ)Ljava/lang/String;", AsmUtil.getMethodSignature(Simple.class, "complexMethod", int.class, long.class));
        Assert.assertNull(AsmUtil.getMethodSignature(Simple.class, "nonExistingMethod"));
    }

    @Test
    public void getParamStack() {
        FrameTable frameTable = new FrameTable(clazz, method);
        Stack<AbstractInsnNode> stack = new Stack<>();
        for (int i = 0; i < method.instructions.size(); i++) {
            stack.push(method.instructions.get(i));
            if (method.instructions.get(i) instanceof MethodInsnNode) {
                break;
            }
        }
        Stack<AbstractInsnNode> params = AsmUtil.getParamStack(stack, frameTable);
        Assert.assertEquals(params.size(), 4);
        Assert.assertEquals(stack.size(), 12);
    }

    @Test
    public void getStaticParameterValues() {
        Stack<AbstractInsnNode> stack = new Stack<>();
        stack.push(new LdcInsnNode("value"));
        stack.push(new IntInsnNode(Opcodes.ISTORE, 911));
        List<Object> params = AsmUtil.getStaticParameterValues(stack);
        Assert.assertEquals(params.size(), 2);
        Assert.assertEquals(params.get(0), "value");
        Assert.assertEquals(params.get(1), 911);
    }

    @Test
    public void isMethodSignatureMatching() {
        AbstractInsnNode i1 = method.instructions.get(11);
        AbstractInsnNode i2 = method.instructions.get(6);
        Assert.assertTrue(AsmUtil.isMethodSignatureMatching(i1, "println", "(I)V"));
        Assert.assertFalse(AsmUtil.isMethodSignatureMatching(i1, "hashCode", "()I"));
        Assert.assertFalse(AsmUtil.isMethodSignatureMatching(i2, "println", "(I)V"));
    }

    @Test
    public void readClassbyte() {
        ClassNode cn = AsmUtil.readClass(code);
        Assert.assertEquals(cn.methods.size(), 3);
        Assert.assertEquals(((MethodNode) cn.methods.get(0)).name, "<init>");
        Assert.assertEquals(((MethodNode) cn.methods.get(1)).name, "simpleMethod");
        Assert.assertEquals(((MethodNode) cn.methods.get(2)).name, "complexMethod");
        Assert.assertEquals(((MethodNode) cn.methods.get(0)).instructions.size(), 13);
        Assert.assertEquals(((MethodNode) cn.methods.get(1)).instructions.size(), 16);
        Assert.assertEquals(((MethodNode) cn.methods.get(2)).instructions.size(), 5);
    }

    @Test
    public void readClassClass() throws IOException {
        ClassNode cn = AsmUtil.readClass(Simple.class);
        Assert.assertEquals(cn.methods.size(), 3);
        Assert.assertEquals(((MethodNode) cn.methods.get(0)).name, "<init>");
        Assert.assertEquals(((MethodNode) cn.methods.get(1)).name, "simpleMethod");
        Assert.assertEquals(((MethodNode) cn.methods.get(2)).name, "complexMethod");
        Assert.assertEquals(((MethodNode) cn.methods.get(0)).instructions.size(), 13);
        Assert.assertEquals(((MethodNode) cn.methods.get(1)).instructions.size(), 16);
        Assert.assertEquals(((MethodNode) cn.methods.get(2)).instructions.size(), 5);
    }

    @Test
    public void removeCurrentMethodCall() {
        FrameTable frameTable = new FrameTable(clazz, method);
        Stack<AbstractInsnNode> stack = new Stack<>();
        for (int i = 0; i < method.instructions.size(); i++) {
            stack.push(method.instructions.get(i));
            if (method.instructions.get(i) instanceof MethodInsnNode) {
                break;
            }
        }
        Stack<AbstractInsnNode> params = AsmUtil.removeCurrentMethodCall(stack, frameTable);
        Assert.assertEquals(params.size(), 4);
        Assert.assertEquals(stack.size(), 6);
    }

    @Test
    public void toInsnList() {
        Stack<AbstractInsnNode> stack = new Stack<>();
        for (int i = 0; i < method.instructions.size(); i++) {
            stack.push(method.instructions.get(i));
        }
        InsnList list = AsmUtil.toInsnList(stack);
        Assert.assertEquals(list.size(), method.instructions.size());
        for (int i = 0; i < method.instructions.size(); i++) {
            Assert.assertEquals(list.get(i), method.instructions.get(i));
        }
    }

    @Test
    public void writeClass() throws IOException {
        ClassNode cn = AsmUtil.readClass(AsmUtil.writeClass(clazz));
        Assert.assertEquals(cn.methods.size(), 3);
        Assert.assertEquals(((MethodNode) cn.methods.get(0)).name, "<init>");
        Assert.assertEquals(((MethodNode) cn.methods.get(1)).name, "simpleMethod");
        Assert.assertEquals(((MethodNode) cn.methods.get(2)).name, "complexMethod");
        Assert.assertEquals(((MethodNode) cn.methods.get(0)).instructions.size(), 13);
        Assert.assertEquals(((MethodNode) cn.methods.get(1)).instructions.size(), 16);
        Assert.assertEquals(((MethodNode) cn.methods.get(2)).instructions.size(), 5);
    }
}
