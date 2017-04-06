package de.framey.lab.evil.eviltentaclesofdeath.util;

import java.util.Stack;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Holds utility methods for working with the {@link Stack} class.
 *
 * @author Frank Meyfarth
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StackUtil {

    /**
     * Pushes all values from one stack to another. This operation will reverse element order. The source stack will be empty after copy
     * operation.
     * 
     * @param source
     *            the stack to copy from
     * @param target
     *            the stack to copy to
     */
    public static final <T> void pushAll(Stack<T> source, Stack<T> target) {
        while (!source.isEmpty()) {
            target.push(source.pop());
        }
    }
}
