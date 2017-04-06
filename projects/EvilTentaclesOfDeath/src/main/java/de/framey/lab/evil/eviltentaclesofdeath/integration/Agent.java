package de.framey.lab.evil.eviltentaclesofdeath.integration;

import java.lang.instrument.Instrumentation;

import de.framey.lab.evil.eviltentaclesofdeath.asm.TentacleHandler;

/**
 * Simple Java Agent which adds a lambda delegator to {@link java.lang.instrument.Instrumentation} which delegates class file transformation
 * to {@link TentacleHandler#transform(byte[])}.
 * <p>
 * This agent is especially usefull during implementation and for testing purposes. It does all the bytecode bloodbath on the fly on class
 * loading. It cann simply be added at program start like this:
 * <p>
 * <code>
 * java -javaagent:<PATH TO THE TENTACLE JAR>/tentacle.jar ...
 * </code>
 * </p>
 * You can even put the following line into your eclipse.ini file to make tentacle roam around your favourite IDE immediately:
 * <p>
 * <code>
 * -javaagent:<PATH TO THE TENTACLE JAR>/tentacle.jar
 * </code>
 * </p>
 * </p>
 *
 * @author Frank Meyfarth
 */
public class Agent {

    /**
     * Implementation of agentmain as described in {@link java.lang.instrument}. Simply adds delegation to
     * {@link java.lang.instrument.Instrumentation}. Actual class file transformatione is done by {@link TentacleHandler}.
     *
     * @param agentArgs
     *            see {@link java.lang.instrument}
     * @param instrumentation
     *            see {@link java.lang.instrument}
     * @throws Throwable
     *             see {@link java.lang.instrument}
     */
    public static void agentmain(String agentArgs, Instrumentation instrumentation) throws Throwable {
        instrumentation.addTransformer((loader, className, classBeingRedefined, protectionDomain, classfileBuffer) -> {
            return TentacleHandler.transform(classfileBuffer);
        });
    }

    /**
     * Implementation of premain as described in {@link java.lang.instrument}. Simply adds delegation to
     * {@link java.lang.instrument.Instrumentation}. Actual class file transformatione is done by {@link TentacleHandler}.
     *
     * @param agentArgs
     *            see {@link java.lang.instrument}
     * @param instrumentation
     *            see {@link java.lang.instrument}
     * @throws Throwable
     *             see {@link java.lang.instrument}
     */
    public static void premain(String agentArgs, Instrumentation instrumentation) throws Throwable {
        instrumentation.addTransformer((loader, className, classBeingRedefined, protectionDomain, classfileBuffer) -> {
            return TentacleHandler.transform(classfileBuffer);
        });
    }
}
