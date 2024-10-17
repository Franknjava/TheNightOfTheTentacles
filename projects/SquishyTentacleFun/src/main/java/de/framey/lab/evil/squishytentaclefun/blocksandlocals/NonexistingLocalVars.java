package de.framey.lab.evil.squishytentaclefun.blocksandlocals;

import de.framey.lab.evil.eviltentaclesofdeath.Tentacle;
import de.framey.lab.evil.squishytentaclefun.util.Basicified;

public class NonexistingLocalVars implements Tentacle, Basicified {

    public static void main(String[] args) {
        new NonexistingLocalVars();
    }

    private NonexistingLocalVars() {
        doBadThings();
    }

    private void doBadThings() {
        GOTO(19);
        int i = 0;
        PRINT("%d: %d", LINE(), i);
    }
}
