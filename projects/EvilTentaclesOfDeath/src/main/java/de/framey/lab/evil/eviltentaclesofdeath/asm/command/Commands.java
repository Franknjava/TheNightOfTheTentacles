package de.framey.lab.evil.eviltentaclesofdeath.asm.command;

import java.util.Stack;

import org.objectweb.asm.tree.AbstractInsnNode;

import de.framey.lab.evil.eviltentaclesofdeath.asm.FrameTable;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enumerates all supported commands and provides methods to identify them.
 *
 * @author Frank Meyfarth
 */
@Getter
@AllArgsConstructor
public enum Commands {

    LINE(new LineCommand()), //
    STATE(new StateCommand()), //
    STATIC_LABEL(new StaticLabelCommand()), //
    DYNAMIC_LABEL(new DynamicLabelCommand()), //
    STATIC_LINE_NUMBER_GOTO(new StaticLineNumberGotoCommand()), //
    STATIC_LABEL_GOTO(new StaticLabelGotoCommand()), //
    DYNAMIC_LINE_NUMBER_GOTO(new DynamicLineNumberGotoCommand()), //
    DYNAMIC_LABEL_GOTO(new DynamicLabelGotoCommand());

    private Command command;

    /**
     * Returns the command matching the given instruction type.
     *
     * @param instructionType
     *            type of instruction defined by the FLG flags
     * @return the command or NULL, if non matches the given type
     */
    public static Command getCommand(AbstractInsnNode ain, FrameTable frameTable, Stack<AbstractInsnNode> instructionStack) {
        for (Commands c : values()) {
            if (c.command.doesInstructionFit(ain, frameTable, instructionStack)) {
                return c.command;
            }
        }
        return null;
    }
}
