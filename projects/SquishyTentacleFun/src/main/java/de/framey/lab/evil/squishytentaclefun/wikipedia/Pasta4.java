package de.framey.lab.evil.squishytentaclefun.wikipedia;

import de.framey.lab.evil.eviltentaclesofdeath.Tentacle;

public class Pasta4 implements Tentacle, Basicified {

    public static void main(String[] args) throws Exception {
        new Pasta4().spaghetti();
    }

    private void spaghetti() {
        int global = -1;
        for (int i = 0; i < 10; i++) {
            global = i;
            GOTO(19);
            NOP();
        }
        GOTO(21);
        PRINT("LOOP: " + global);
        GOTO(16);
        PRINT("DONE");
    }
}
