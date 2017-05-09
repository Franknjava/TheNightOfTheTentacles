package de.framey.lab.evil.eviltentaclesofdeath.asm.command;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

/**
 * Describes a command which has a global class impact.
 *
 * @param <T>
 *            The type of the object identifying this command
 * 
 * @author Frank Meyfarth
 */
public interface GlobalCommand<T> extends Command {

    /**
     * Generates and returns which identifies the command in a certain context.
     * 
     * @param mn
     *            the method from which this command has been called
     * @return the key of this command in the given method context
     */
    T getCommandKey(MethodNode mn);

    /**
     * Applies the neccessary changes to the class.
     * 
     * @param cn
     *            the class which uses this command
     * @param key
     *            of this command in the given method context
     */
    void applyGlobalChanges(ClassNode cn, T key);

    /**
     * Applies the neccessary changes to the class. The raw key will be cast to T.
     * 
     * @param cn
     *            the class which uses this command
     * @param key
     *            of this command in the given method context
     */
    @SuppressWarnings("unchecked")
    default void applyGlobalChangesRaw(ClassNode cn, Object key) {
        applyGlobalChanges(cn, (T) key); 
    }
}
