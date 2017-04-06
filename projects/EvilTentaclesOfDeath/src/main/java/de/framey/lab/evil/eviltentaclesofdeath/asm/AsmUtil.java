package de.framey.lab.evil.eviltentaclesofdeath.asm;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;

/**
 * This utility class provides convinience methods for common operations on Java bytecode.
 *
 * @author Frank Meyfarth
 */
@Log
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AsmUtil {

    /**
     * Retrieves a named method from of a class.
     *
     * @param cn
     *            the class to get the method from
     * @param name
     *            the name of the method
     * @param desc
     *            the signature description of the method
     * @return the mothod node or NULL, if no match was found
     */
    @SuppressWarnings("unchecked")
    public static MethodNode getMethod(ClassNode cn, String name, String desc) {
        for (MethodNode mn : (List<MethodNode>) cn.methods) {
            if (mn.name.equals(name) && mn.desc.equals(desc)) {
                return mn;
            }
        }
        return null;
    }

    /**
     * Gets the {@link InputStream} for the given class file from its {@link ClassLoader}.
     *
     * @param clazz
     *            the class to get the Stream for
     * @return the INputStream of the class file
     */
    public static InputStream getClassAsStream(Class<?> clazz) {
        ClassLoader loader = clazz.getClassLoader();
        if (loader == null) {
            loader = ClassLoader.getSystemClassLoader();
        }
        return loader.getResourceAsStream(clazz.getName().replace('.', '/') + ".class");
    }

    /**
     * Finds a certain type of instruction node in the chain of instructions starting at the given instruction and searching up.
     *
     * @param current
     *            the instruction to start the search from
     * @param type
     *            the type of instruction to find
     * @return the instruction found or NULL if none was found
     */
    @SuppressWarnings("unchecked")
    public static <T extends AbstractInsnNode> T findPreviousNode(AbstractInsnNode current, Class<T> type) {
        AbstractInsnNode ain = current;
        while (ain != null && !type.isAssignableFrom(ain.getClass())) {
            ain = ain.getPrevious();
        }
        return (T) ain;
    }

    /**
     * Finds a certain type of instruction node in the chain of instructions starting at the given instruction and searching down.
     *
     * @param current
     *            the instruction to start the search from
     * @param type
     *            the type of instruction to find
     * @return the instruction found or NULL if none was found
     */
    @SuppressWarnings("unchecked")
    public static <T extends AbstractInsnNode> T findNextNode(AbstractInsnNode current, Class<T> type) {
        AbstractInsnNode ain = current;
        while (ain != null && !type.isAssignableFrom(ain.getClass())) {
            ain = ain.getNext();
        }
        return (T) ain;
    }

    /**
     * Finds a certain type of instruction node in the chain of instructions starting at the given instruction and searching from the start
     * of the method.
     *
     * @param mn
     *            the method to search
     * @param type
     *            the type of instruction to find
     * @return the instruction found or NULL if none was found
     */
    public static <T extends AbstractInsnNode> T findFirstNode(MethodNode mn, Class<T> type) {
        return findNextNode(mn.instructions.getFirst(), type);
    }

    /**
     * Finds a certain type of instruction node in the chain of instructions starting at the given instruction and searching from the end of
     * the method.
     *
     * @param mn
     *            the method to search
     * @param type
     *            the type of instruction to find
     * @return the instruction found or NULL if none was found
     */
    public static <T extends AbstractInsnNode> T findLastNode(MethodNode mn, Class<T> type) {
        return findPreviousNode(mn.instructions.getLast(), type);
    }

    /**
     * Writes the given class to a byte array.
     *
     * @param cn
     *            the class to write
     * @return the byte array containing the bytecode of the class
     */
    public static byte[] writeClass(ClassNode cn) {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        cn.accept(cw);
        return cw.toByteArray();
    }

    /**
     * Reads a class from the given byte array.
     *
     * @param code
     *            the bytecode to read
     * @return the class represented by the given bytecode
     */
    public static ClassNode readClass(byte[] code) {
        ClassReader cr = new ClassReader(code);
        ClassNode cn = new ClassNode();
        cr.accept(cn, 0);
        return cn;
    }

    /**
     * Reads a class from the given class object.
     *
     * @param clazz
     *            the class object to read
     * @return the class represented by the given class object
     */
    public static ClassNode readClass(Class<?> clazz) throws IOException {
        ClassReader cr = new ClassReader(getClassAsStream(clazz));
        ClassNode cn = new ClassNode();
        cr.accept(cn, 0);
        return cn;
    }

    /**
     * Converts a {@link Stack} of instructions into a {@link InsnList}.
     *
     * @param intructionStack
     *            the instruction stack to convert
     * @return the {@link InsnList} representing the instructions from the stack
     */
    public static InsnList toInsnList(Stack<AbstractInsnNode> intructionStack) {
        InsnList newInstructions = new InsnList();
        for (AbstractInsnNode ain : intructionStack) {
            newInstructions.add(ain);
        }
        return newInstructions;
    }

    /**
     * Generates the String representation of a method signature.
     *
     * @param clazz
     *            the class which contains the method
     * @param method
     *            the name of the method
     * @param parameters
     *            the types of the method parameters
     * @return the String representation of the method
     */
    public static String getMethodSignature(final Class<?> clazz, final String method, final Class<?>... parameters) {
        try {
            ClassReader cr = new ClassReader(getClassAsStream(clazz));
            List<String> sgn = new ArrayList<>(1);
            cr.accept(new ClassVisitor(Opcodes.ASM5) {
                @Override
                public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                    if (method.equals(name)) {
                        boolean parametersMatch = true;
                        Type[] parameterTypes = Type.getArgumentTypes(desc);
                        if (parameterTypes.length == parameters.length) {
                            for (int i = 0; i < parameterTypes.length; i++) {
                                if (!parameterTypes[i].getClassName().equals(parameters[i].getName())) {
                                    parametersMatch = false;
                                    break;
                                }
                            }
                        } else {
                            parametersMatch = false;
                        }
                        if (parametersMatch) {
                            sgn.add(desc);
                        }
                    }
                    return super.visitMethod(access, name, desc, signature, exceptions);
                }
            }, 0);
            return (sgn.isEmpty()) ? null : sgn.get(0);
        } catch (IOException e) {
            throw new IllegalArgumentException("Class cannot be read: " + clazz.getName(), e);
        }
    }

    /**
     * Removes the current method call from the instruction stack. This has an effect only, if the top element of the stack is a method
     * instruction. The method call is removed including all instructions putting parameter values to the operand stack. These are actually
     * return as a parameter instruction stack for further use.
     *
     * @param instructionStack
     *            the current stack to remove the method call from
     * @param frameTable
     *            the frame table used to determine the number of instructions belonging to this method call
     * @return the stack containing the instructions providing the methods parameters
     */
    public static Stack<AbstractInsnNode> removeCurrentMethodCall(Stack<AbstractInsnNode> instructionStack, FrameTable frameTable) {
        Stack<AbstractInsnNode> paramStack = new Stack<>();
        if (!instructionStack.isEmpty() && instructionStack.peek().getType() == AbstractInsnNode.METHOD_INSN) {
            int paramSize = frameTable.getParameterSize(instructionStack.peek());
            log.info(String.format("Removing %d parameter instructions", paramSize));
            instructionStack.pop();
            for (int i = 0; i < paramSize; i++) {
                paramStack.push(instructionStack.pop());
            }
            instructionStack.pop();
        }
        return paramStack;
    }

    /**
     * Retrieves the instructions providing the paramenters of the current method call without removing the current method call from the
     * instruction stack. This has an effect only, if the top element of the stack is a method instruction.
     *
     * @param instructionStack
     *            the current stack to retrieve the parameter instructions from
     * @param frameTable
     *            the frame table used to determine the number of instructions belonging to this method call
     * @return the stack containing the instructions providing the methods parameters
     */
    @SuppressWarnings("unchecked")
    public static Stack<AbstractInsnNode> getParamStack(Stack<AbstractInsnNode> instructionStack, FrameTable frameTable) {
        return removeCurrentMethodCall((Stack<AbstractInsnNode>) instructionStack.clone(), frameTable);
    }

    /**
     * If the current method call has only static parameters, this method returns a list of those values. If there are non static parameter
     * an {@link IllegalArgumentException} will be thrown.
     *
     * @param paramStack
     *            the instruction stack containing the parameter instructions
     * @return the list of values
     */
    @SuppressWarnings("unchecked")
    public static List<Object> getStaticParameterValues(Stack<AbstractInsnNode> paramStack) {
        Stack<AbstractInsnNode> paramStackCopy = (Stack<AbstractInsnNode>) paramStack.clone();
        List<Object> result = new ArrayList<>();
        while (!paramStackCopy.isEmpty()) {
            AbstractInsnNode ain = paramStackCopy.pop();
            if (ain instanceof IntInsnNode) {
                result.add(0, ((IntInsnNode) ain).operand);
            } else if (ain instanceof LdcInsnNode) {
                result.add(0, ((LdcInsnNode) ain).cst);
            } else {
                throw new IllegalArgumentException("Unknown static parameter: " + ain.getClass().getName());
            }
        }
        return result;
    }

    /**
     * Returns TRUE, if all parameters on the parameter stack are static.
     *
     * @param paramStack
     *            the instruction stack containing the parameter instructions
     * @return TRUE, if all parameters on the parameter stack are static
     */
    @SuppressWarnings("unchecked")
    public static boolean areParameterValuesStatic(Stack<AbstractInsnNode> paramStack) {
        Stack<AbstractInsnNode> paramStackCopy = (Stack<AbstractInsnNode>) paramStack.clone();
        boolean result = !paramStackCopy.isEmpty();
        while (!paramStackCopy.isEmpty()) {
            AbstractInsnNode ain = paramStackCopy.pop();
            if (!(ain instanceof IntInsnNode) && !(ain instanceof LdcInsnNode)) {
                result = false;
                break;
            }
        }
        return result;
    }

    /**
     * Returns TRUE, if the given instruction is a method call which matches the given method name and signature.
     *
     * @param instruction
     *            the instruction to check
     * @param name
     *            the name of the method
     * @param signature
     *            the signature description of the method
     * @return TRUE, if the given instruction is a method call which matches the given method name and signature
     */
    public static boolean isMethodSignatureMatching(AbstractInsnNode instruction, String name, String signature) {
        boolean result = false;
        if (instruction != null && instruction.getType() == AbstractInsnNode.METHOD_INSN) {
            MethodInsnNode meth = (MethodInsnNode) instruction;
            result = meth.name.equals(name) && meth.desc.equals(signature);
        }
        return result;
    }

    public static void addThrowExceptionToStack(Stack<AbstractInsnNode> instructions, Class<? extends Exception> ex, String msg) {
        try {
            String exType = Type.getInternalName(ex);
            Constructor<RuntimeException> constructor = RuntimeException.class.getConstructor(String.class);

            instructions.push(new TypeInsnNode(Opcodes.NEW, exType));
            instructions.push(new InsnNode(Opcodes.DUP));
            instructions.push(new LdcInsnNode(msg));
            instructions.push(new MethodInsnNode(
                    Opcodes.INVOKESPECIAL,
                    exType,
                    "<init>",
                    Type.getType(constructor).getDescriptor(),
                    false));
            instructions.push(new InsnNode(Opcodes.ATHROW));
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
