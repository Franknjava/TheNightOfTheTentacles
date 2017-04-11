package de.framey.lab.evil.squishytentaclefun.blocksandlocals;

import de.framey.lab.evil.eviltentaclesofdeath.Tentacle;
import de.framey.lab.evil.squishytentaclefun.util.Basicified;

public class AmbiguousLocalVars implements Tentacle, Basicified {

    public static void main(String[] args) {
        new AmbiguousLocalVars();
    }

    private AmbiguousLocalVars() {
        doBadThings();
    }

    private void doBadThings() {
        int j = 0;
        GOTO(24);
        do {
            int x = 42;
            PRINT("%d: %d", LINE(), ++x);
        } while (j++ < 1);
        GOTO(27);
        int i = 666;
        PRINT("%d: %d", LINE(), i);
        GOTO(21);
        PRINT("%d: %d", LINE(), i);
    }
}
