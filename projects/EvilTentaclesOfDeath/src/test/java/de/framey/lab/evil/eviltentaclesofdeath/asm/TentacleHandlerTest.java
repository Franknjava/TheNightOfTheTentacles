package de.framey.lab.evil.eviltentaclesofdeath.asm;

import java.io.IOException;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.LookupSwitchInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import de.framey.lab.evil.eviltentaclesofdeath.asm.AsmUtil;
import de.framey.lab.evil.eviltentaclesofdeath.asm.TentacleHandler;
import de.framey.lab.evil.eviltentaclesofdeath.testhelper.TentacleVariants;
import junit.framework.Assert;

public class TentacleHandlerTest {

    private byte[]    code;
    private byte[]    evil;
    private ClassNode clazz;

    @BeforeTest
    private void loadCode() throws IOException {
        code = AsmUtil.writeClass(AsmUtil.readClass(TentacleVariants.class));
        evil = TentacleHandler.transform(code);
        clazz = AsmUtil.readClass(evil);
    }

    //line()V
    //  LABEL L2070529722
    //  LINE 8
    //    LDC 8
    //    POP
    //  LABEL L1188753216
    //  LINE 9
    //    RETURN
    //  LABEL L317986356
    @Test
    public void transformLineTest() {
        MethodNode mn = AsmUtil.getMethod(clazz, "line", "()V");
        Assert.assertEquals(Opcodes.LDC, mn.instructions.get(2).getOpcode());
        Assert.assertEquals(11, ((LdcInsnNode) mn.instructions.get(2)).cst);
        Assert.assertEquals(Opcodes.POP, mn.instructions.get(3).getOpcode());
        Assert.assertEquals(Opcodes.RETURN, mn.instructions.get(6).getOpcode());
    }

    //label()V
    //  LABEL L1629604310
    //  LINE 12
    //  LINE 13
    //    RETURN
    //  LABEL L142555199
    @Test
    public void transformLabelTest() {
        MethodNode mn = AsmUtil.getMethod(clazz, "label", "()V");
        Assert.assertEquals(Opcodes.RETURN, mn.instructions.get(3).getOpcode());
    }

    //gotoIntStatic()V
    //  LABEL L317986356
    //  LINE 16
    //  Local: null Stack: null
    //    GOTO L317986356
    //  LABEL L331510866
    //  LINE 17
    //  Local: [] Stack: [java/lang/Throwable]
    //    ATHROW
    //  LABEL L640363654
    @Test
    public void transformGotoIntStaticTest() {
        MethodNode mn = AsmUtil.getMethod(clazz, "gotoIntStatic", "()V");
        Assert.assertEquals(Opcodes.GOTO, mn.instructions.get(3).getOpcode());
        Assert.assertEquals(((LabelNode) mn.instructions.get(0)).getLabel().toString(),
                ((JumpInsnNode) mn.instructions.get(3)).label.getLabel().toString());
        Assert.assertEquals(Opcodes.ATHROW, mn.instructions.get(7).getOpcode());
    }

    // 0   LABEL L660017404
    // 1   LINE 23
    // 2   Local: null Stack: null
    // 3     BIPUSH 24
    // 4     ISTORE 1
    // 5   LABEL L1381965390
    // 6   LINE 24
    // 7   Local: [1] Stack: null
    // 8     ILOAD 1
    // 9     TABLESWITCH 23-25 L1979313356 Labels: [L660017404, L1381965390, L1386883398]
    //10   LABEL L1979313356
    //11   Local: null Stack: null
    //12     NEW java/lang/IllegalArgumentException
    //13     DUP
    //14     LDC Illegal jump target!
    //15     INVOKESPECIAL java/lang/IllegalArgumentException.<init> (Ljava/lang/String;)V
    //16     ATHROW
    //17   LABEL L1386883398
    //18   LINE 25
    //19   Local: null Stack: null
    //20     RETURN
    //21   LABEL L1306854175
    @Test
    public void transformGotoIntDynamicTest() {
        MethodNode mn = AsmUtil.getMethod(clazz, "gotoIntDynamic", "()V");
        Assert.assertEquals(Opcodes.BIPUSH, mn.instructions.get(3).getOpcode());
        Assert.assertEquals(24, ((IntInsnNode) mn.instructions.get(3)).operand);
        Assert.assertEquals(Opcodes.ISTORE, mn.instructions.get(4).getOpcode());
        Assert.assertEquals(1, ((VarInsnNode) mn.instructions.get(4)).var);
        Assert.assertEquals(Opcodes.ILOAD, mn.instructions.get(8).getOpcode());
        Assert.assertEquals(1, ((VarInsnNode) mn.instructions.get(8)).var);
        Assert.assertEquals(Opcodes.TABLESWITCH, mn.instructions.get(9).getOpcode());
        Assert.assertEquals(23, ((TableSwitchInsnNode) mn.instructions.get(9)).min);
        Assert.assertEquals(25, ((TableSwitchInsnNode) mn.instructions.get(9)).max);
        Assert.assertEquals(3, ((TableSwitchInsnNode) mn.instructions.get(9)).labels.size());
        Assert.assertEquals(((LabelNode) mn.instructions.get(0)).getLabel().toString(),
                ((LabelNode) ((TableSwitchInsnNode) mn.instructions.get(9)).labels.get(0)).getLabel().toString());
        Assert.assertEquals(((LabelNode) mn.instructions.get(5)).getLabel().toString(),
                ((LabelNode) ((TableSwitchInsnNode) mn.instructions.get(9)).labels.get(1)).getLabel().toString());
        Assert.assertEquals(((LabelNode) mn.instructions.get(17)).getLabel().toString(),
                ((LabelNode) ((TableSwitchInsnNode) mn.instructions.get(9)).labels.get(2)).getLabel().toString());
        Assert.assertEquals(((LabelNode) mn.instructions.get(10)).getLabel().toString(),
                ((TableSwitchInsnNode) mn.instructions.get(9)).dflt.getLabel().toString());
        Assert.assertEquals(Opcodes.NEW, mn.instructions.get(12).getOpcode());
        Assert.assertEquals("java/lang/IllegalArgumentException", ((TypeInsnNode) mn.instructions.get(12)).desc);
        Assert.assertEquals(Opcodes.DUP, mn.instructions.get(13).getOpcode());
        Assert.assertEquals(Opcodes.LDC, mn.instructions.get(14).getOpcode());
        Assert.assertEquals("Illegal jump target!", ((LdcInsnNode) mn.instructions.get(14)).cst);
        Assert.assertEquals(Opcodes.INVOKESPECIAL, mn.instructions.get(15).getOpcode());
        Assert.assertEquals("java/lang/IllegalArgumentException", ((MethodInsnNode) mn.instructions.get(15)).owner);
        Assert.assertEquals("<init>", ((MethodInsnNode) mn.instructions.get(15)).name);
        Assert.assertEquals("(Ljava/lang/String;)V", ((MethodInsnNode) mn.instructions.get(15)).desc);
        Assert.assertEquals(false, ((MethodInsnNode) mn.instructions.get(15)).itf);
        Assert.assertEquals(Opcodes.ATHROW, mn.instructions.get(16).getOpcode());
        Assert.assertEquals(Opcodes.RETURN, mn.instructions.get(20).getOpcode());
    }

    //gotoStringStatic()V
    //  LABEL L1665404403
    //  LINE 25
    //    GOTO L988458918
    //  LABEL L988458918
    //  LINE 26
    //  LINE 27
    //  Local: null Stack: null
    //    RETURN
    //  LABEL L1990451863
    @Test
    public void transformGotoStringStaticTest() {
        MethodNode mn = AsmUtil.getMethod(clazz, "gotoStringStatic", "()V");
        Assert.assertEquals(Opcodes.GOTO, mn.instructions.get(2).getOpcode());
        Assert.assertEquals(((LabelNode) mn.instructions.get(3)).getLabel().toString(),
                ((JumpInsnNode) mn.instructions.get(2)).label.getLabel().toString());
        Assert.assertEquals(Opcodes.RETURN, mn.instructions.get(7).getOpcode());
    }

    //   gotoStringDynamic()V
    // 0   LABEL L704024720
    // 1   LINE 33
    // 2     LDC label
    // 3     ASTORE 1
    // 4   LABEL L1452012306
    // 5   LINE 34
    // 6     ALOAD 1
    // 7     DUP
    // 8    ASTORE 2
    // 9     INVOKEVIRTUAL java/lang/String.hashCode ()I
    //10     LOOKUPSWITCH L211968962 Keys: [102727412] Labels: [L1486566962]
    //11   LABEL L1486566962
    //12   Local: [java/lang/String, java/lang/String] Stack: null
    //13     ALOAD 2
    //14     LDC label
    //15     INVOKEVIRTUAL java/lang/String.equals (Ljava/lang/Object;)Z
    //16     IFNE L1173643169
    //17     GOTO L211968962
    //18   LABEL L211968962
    //19   Local: null Stack: null
    //20     NEW java/lang/IllegalArgumentException
    //21     DUP
    //22     LDC Illegal jump target!
    //23     INVOKESPECIAL java/lang/IllegalArgumentException.<init> (Ljava/lang/String;)V
    //24     ATHROW
    //25   LABEL L1173643169
    //26   LINE 35
    //27   LINE 36
    //28   Local: null Stack: null
    //29     RETURN
    //30   LABEL L1282287470
    @Test
    public void transformGotoStringDynamicTest() {
        MethodNode mn = AsmUtil.getMethod(clazz, "gotoStringDynamic", "()V");
        Assert.assertEquals(Opcodes.LDC, mn.instructions.get(2).getOpcode());
        Assert.assertEquals("label", ((LdcInsnNode) mn.instructions.get(2)).cst);
        Assert.assertEquals(Opcodes.ASTORE, mn.instructions.get(3).getOpcode());
        Assert.assertEquals(1, ((VarInsnNode) mn.instructions.get(3)).var);
        Assert.assertEquals(Opcodes.ALOAD, mn.instructions.get(6).getOpcode());
        Assert.assertEquals(1, ((VarInsnNode) mn.instructions.get(6)).var);
        Assert.assertEquals(Opcodes.DUP, mn.instructions.get(7).getOpcode());
        Assert.assertEquals(Opcodes.ASTORE, mn.instructions.get(8).getOpcode());
        Assert.assertEquals(2, ((VarInsnNode) mn.instructions.get(8)).var);
        Assert.assertEquals(Opcodes.INVOKEVIRTUAL, mn.instructions.get(9).getOpcode());
        Assert.assertEquals("java/lang/String", ((MethodInsnNode) mn.instructions.get(9)).owner);
        Assert.assertEquals("hashCode", ((MethodInsnNode) mn.instructions.get(9)).name);
        Assert.assertEquals("()I", ((MethodInsnNode) mn.instructions.get(9)).desc);
        Assert.assertEquals(Opcodes.LOOKUPSWITCH, mn.instructions.get(10).getOpcode());
        Assert.assertEquals(1, ((LookupSwitchInsnNode) mn.instructions.get(10)).keys.size());
        Assert.assertEquals(1, ((LookupSwitchInsnNode) mn.instructions.get(10)).labels.size());
        Assert.assertEquals(((LabelNode) mn.instructions.get(18)).getLabel().toString(),
                ((LookupSwitchInsnNode) mn.instructions.get(10)).dflt.getLabel().toString());
        Assert.assertEquals("label".hashCode(), ((LookupSwitchInsnNode) mn.instructions.get(10)).keys.get(0));
        Assert.assertEquals(((LabelNode) mn.instructions.get(11)).getLabel().toString(),
                ((LabelNode) ((LookupSwitchInsnNode) mn.instructions.get(10)).labels.get(0)).getLabel().toString());
        Assert.assertEquals(Opcodes.ALOAD, mn.instructions.get(13).getOpcode());
        Assert.assertEquals(2, ((VarInsnNode) mn.instructions.get(13)).var);
        Assert.assertEquals(Opcodes.LDC, mn.instructions.get(14).getOpcode());
        Assert.assertEquals("label", ((LdcInsnNode) mn.instructions.get(14)).cst);
        Assert.assertEquals(Opcodes.INVOKEVIRTUAL, mn.instructions.get(15).getOpcode());
        Assert.assertEquals("java/lang/String", ((MethodInsnNode) mn.instructions.get(15)).owner);
        Assert.assertEquals("equals", ((MethodInsnNode) mn.instructions.get(15)).name);
        Assert.assertEquals("(Ljava/lang/Object;)Z", ((MethodInsnNode) mn.instructions.get(15)).desc);
        Assert.assertEquals(Opcodes.IFNE, mn.instructions.get(16).getOpcode());
        Assert.assertEquals(((JumpInsnNode) mn.instructions.get(16)).label.getLabel().toString(),
                ((LabelNode) mn.instructions.get(25)).getLabel().toString());
        Assert.assertEquals(Opcodes.GOTO, mn.instructions.get(17).getOpcode());
        Assert.assertEquals(((JumpInsnNode) mn.instructions.get(17)).label.getLabel().toString(),
                ((LabelNode) mn.instructions.get(18)).getLabel().toString());
        Assert.assertEquals(Opcodes.NEW, mn.instructions.get(20).getOpcode());
        Assert.assertEquals("java/lang/IllegalArgumentException", ((TypeInsnNode) mn.instructions.get(20)).desc);
        Assert.assertEquals(Opcodes.DUP, mn.instructions.get(21).getOpcode());
        Assert.assertEquals(Opcodes.LDC, mn.instructions.get(22).getOpcode());
        Assert.assertEquals("Illegal jump target!", ((LdcInsnNode) mn.instructions.get(22)).cst);
        Assert.assertEquals(Opcodes.INVOKESPECIAL, mn.instructions.get(23).getOpcode());
        Assert.assertEquals("java/lang/IllegalArgumentException", ((MethodInsnNode) mn.instructions.get(23)).owner);
        Assert.assertEquals("<init>", ((MethodInsnNode) mn.instructions.get(23)).name);
        Assert.assertEquals("(Ljava/lang/String;)V", ((MethodInsnNode) mn.instructions.get(23)).desc);
        Assert.assertEquals(false, ((MethodInsnNode) mn.instructions.get(23)).itf);
        Assert.assertEquals(Opcodes.ATHROW, mn.instructions.get(24).getOpcode());
        Assert.assertEquals(Opcodes.RETURN, mn.instructions.get(29).getOpcode());
    }
}
