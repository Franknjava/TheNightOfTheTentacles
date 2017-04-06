package de.framey.lab.evil.eviltentaclesofdeath.asm;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import de.framey.lab.evil.eviltentaclesofdeath.Tentacle;
import de.framey.lab.evil.eviltentaclesofdeath.util.DefaultableTreeMap;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * The {@link JumpTable} of a method contains all information needed to determine where a certain GOTO statement needs to jump to.
 * <p>
 * This information includes:
 * <ul>
 * <li>A linenumber jumptable which maps linenumbers to actual jumptargets representd by {@link LabelNode} objects.</li>
 * <li>A label jumptable which maps named targets set by {@link Tentacle#LABEL(String)} commands to actual jumptargets representd by
 * {@link LabelNode} objects.</li>
 * <li>A default jumptarget which is used, if the target of a dynamic jump does not exists</li>
 * <li>The first line number of this method</li>
 * <li>The last line number of this method</li>
 * <li>Information needed to build up switch tables for dynamig gotos</li>
 * </ul>
 * </p>
 *
 * @author Frank Meyfarth
 */
@Getter
public class JumpTable {

    /** Maps linenumbers to {@link LabelNode} objects. */
    @Getter(AccessLevel.PRIVATE)
    private NavigableMap<Integer, LabelNode>         lineNumberJumpTable = new TreeMap<>();

    /** Maps {@link Tentacle#LABEL(String)} values to {@link LabelNode} objects. */
    @Getter(AccessLevel.PRIVATE)
    private NavigableMap<String, LabelNode>          labelJumpTable      = new TreeMap<>();

    /** Maps hashcodes to label names. */
    private DefaultableTreeMap<Integer, Set<String>> labelHashCodes      = new DefaultableTreeMap<>();

    /** The first linenumber of the method. */
    private int                                      firstLineNumber;

    /** The last linenumber of the method. */
    private int                                      lastLineNumber;

    /** The first jumptarget of the method. */
    private LabelNode                                firstTarget;

    /** The last jumptarget of the method. */
    private LabelNode                                lastTarget;

    /** The {@link LabelNode} objects corresponding to each line of the method. */
    private LabelNode[]                              lineNumberSwitchTargets;

    /**
     * Creates a {@link JumpTable} for the given method containing all necessary data.
     *
     * @param mn
     *            the method node to generate the {@link JumpTable} for
     */
    public JumpTable(MethodNode mn) {
        fillJumpTables(mn);
        fillSwitchTables(mn);
    }

    /**
     * Collects all the data needed for linenumber and label jumptable. These are used for static jumps.
     *
     * @param mn
     *            the method node to collect the data for
     */
    @SuppressWarnings("unchecked")
    private void fillJumpTables(MethodNode mn) {
        LabelNode label = null;

        for (Iterator<AbstractInsnNode> iterator = mn.instructions.iterator(); iterator.hasNext();) {
            AbstractInsnNode ain = iterator.next();
            switch (ain.getType()) {
            case AbstractInsnNode.LABEL:
                label = (LabelNode) ain;
                break;
            case AbstractInsnNode.LINE:
                lineNumberJumpTable.put(((LineNumberNode) ain).line, label);
                break;
            case AbstractInsnNode.METHOD_INSN:
                if ("LABEL".equals(((MethodInsnNode) ain).name)) { // Sind die schon weg???
                    String labelName = AsmUtil.findPreviousNode(ain, LdcInsnNode.class).cst.toString();
                    Set<String> labelNames = labelHashCodes.get(labelName.hashCode(), () -> new TreeSet<>());
                    labelNames.add(labelName);
                    labelJumpTable.put(labelName, label);
                }
                break;
            }
        }
    }

    /**
     * Convenience method to provide a {@link LabelNode} for a given jump target regardless of its type. If the type is neither int nor
     * String or it does not exist within this method an {@link IllegalArgumentException} will be thrown.
     *
     * @param target
     *            the target to get the matching {@link LabelNode} for
     * @return the {@link LabelNode} matching the target, if it exists
     * @throws IllegalArgumentException
     *             if the target is neither int nor String or it does not exist
     */
    public LabelNode get(Object target) {
        LabelNode label = null;
        if (target instanceof Integer) {
            label = lineNumberJumpTable.get(target);
        } else if (target instanceof String) {
            label = labelJumpTable.get(target);
        } else {
            throw new IllegalArgumentException("Unsupported jump target type: " + target.getClass().getName());
        }
        if (label == null) {
            throw new IllegalArgumentException("Goto target does not exist: " + target);
        }
        return label;
    }

    /**
     * Collects all the data needed for table and lookup switches. These are used for dynamic jumps.
     *
     * @param mn
     *            the method node to collect the data for
     */
    private void fillSwitchTables(MethodNode mn) {
        firstLineNumber = lineNumberJumpTable.firstKey();
        lastLineNumber = lineNumberJumpTable.lastKey();
        firstTarget = lineNumberJumpTable.firstEntry().getValue();
        lastTarget = lineNumberJumpTable.lastEntry().getValue();
        lineNumberSwitchTargets = new LabelNode[lastLineNumber - firstLineNumber + 1];

        for (int i = firstLineNumber; i <= lastLineNumber; i++) {
            int index = i - firstLineNumber;
            lineNumberSwitchTargets[index] = lineNumberJumpTable.get(i);
            if (lineNumberSwitchTargets[index] == null) {
                Entry<Integer, LabelNode> e = lineNumberJumpTable.higherEntry(i);
                if (e != null) {
                    lineNumberSwitchTargets[index] = e.getValue();
                }
            }
        }
    }
}
