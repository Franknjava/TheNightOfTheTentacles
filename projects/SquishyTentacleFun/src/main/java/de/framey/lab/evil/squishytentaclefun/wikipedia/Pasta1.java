package de.framey.lab.evil.squishytentaclefun.wikipedia;

import de.framey.lab.evil.eviltentaclesofdeath.Tentacle;
import de.framey.lab.evil.squishytentaclefun.util.Basicified;

public class Pasta1 implements Tentacle, Basicified {

    public static void main(String[] args) throws Exception {
        new Pasta1().spaghetti();
    }

    // 10 i = 0
    // 20 i = i + 1
    // 30 PRINT i; " squared = "; i * i
    // 40 IF i >= 10 THEN GOTO 60
    // 50 GOTO 20
    // 60 PRINT "Program Fully Completed."
    // 70 END
    private void spaghetti() {
/* 10*/ int i = 0;
/* 20*/ i = i + 1;
/* 30*/ PRINT(i + " squared = " + i * i);
/* 40*/ if (i >= 10) GOTO(25);
/* 50*/ GOTO(21);
/* 60*/ PRINT("Program Fully Completed.");
/* 70*/ return;
    }
}
