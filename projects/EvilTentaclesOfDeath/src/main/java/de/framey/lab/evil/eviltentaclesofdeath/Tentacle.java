package de.framey.lab.evil.eviltentaclesofdeath;

/**
 * So here it is my precious little tentacle which makes your programcounter jump from here to there to everywhere, MUHAHAHAHA!
 * <p>
 * Simply attach this interface to any class which you like to enhance with nasty tentacles. You can use the tentacle methods right away,
 * although you need to summon Cthulhu with one of the tools from {@link de.framey.lab.evil.eviltentaclesofdeath.integration} in order to make them work.
 * </p>
 *
 * @author Frank Meyfarth
 */
public interface Tentacle {

    /**
     * Jumps to the given linenumber. Yes, this is the actual linenumber from your sourcecode starting at line one including comments, empty
     * lines, etc. Isn't this naughty? You'd better turn linenumbers on in your editor.
     * <p>
     * It is obvious that you cannot jump anywhere. First, you cannot jump out of the current method. This is simply not possible on
     * bytecode level. Second, there are destinations within the current method which do not make sense at all to jump at (e.g. multiline
     * instructions, empty lines, etc.). Did I say "sense"? We are insane using this anyway, aren't we? So, experiment where your tentacles
     * go.
     * </p>
     *
     * @param line
     *            the linenumber to go to
     */
    default void GOTO(int line) {
        throw new UnsupportedOperationException("Method should have been replaced by instrumentation: GOTO " + line);
    }

    /**
     * Well, this is for the faint hearted who are to afraid to jump to linenumbers. Named labels can be set using the
     * {@link #LABEL(String)} command.
     *
     * @param label
     *            the named target to go to
     */
    default void GOTO(String label) {
        throw new UnsupportedOperationException("Method should have been replaced by instrumentation: GOTO " + label);
    }

    /**
     * This command sets a named jump target to this line. If used with {@link #GOTO(String)}, the latter will always jump to the first
     * instruction of this line if used in multi-instruction line.
     * <p>
     * The parameter must be set static. Dynamic parameter setting by variable will cause an exceptional summoning ritual.
     * </p>
     *
     * @param label
     *            the name of the jump target.
     */
    default void LABEL(String label) {
        throw new UnsupportedOperationException("Method should have been replaced by instrumentation: LABEL " + label);
    }

    /**
     * This method return the current linenumber at runtime. This may come in handy for relative jumps and other funny stuff.
     *
     * @return the current linenumber.
     */
    default int LINE() {
        throw new UnsupportedOperationException("Method should have been replaced by instrumentation");
    }

    /**
     * This method return the current method state at runtime.
     *
     * @return the current method state.
     */
    default Object[][] STATE() {
        throw new UnsupportedOperationException("Method should have been replaced by instrumentation");
    }
}
