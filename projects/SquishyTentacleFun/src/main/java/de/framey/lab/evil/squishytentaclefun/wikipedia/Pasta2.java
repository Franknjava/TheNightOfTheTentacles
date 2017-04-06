package de.framey.lab.evil.squishytentaclefun.wikipedia;

import java.io.IOException;

import de.framey.lab.evil.eviltentaclesofdeath.Tentacle;

public class Pasta2 implements Tentacle, Basicified {

    public static void main(String[] args) throws Exception {
        new Pasta2().spaghetti();
    }

    //  10 CLS
    //  20 i = 0
    //  30 i = i + 1
    //  40 PRINT i; " squared = "; i * i
    //  50 IF i >= 10 THEN GOTO 70
    //  60 GOTO 30
    //  70 PRINT "Program Completed."
    //  80 INPUT "Do it Again (j)"; sel$
    //  90 IF sel$ = "j" THEN GOTO 10
    // 100 END
    private void spaghetti() throws IOException {
        int i, sel$;
/* 10*/ CLS();
/* 20*/ i = 0;
/* 30*/ i = i + 1;
/* 40*/ PRINT(i + " squared = " + i * i);
/* 50*/ if (i >= 10) GOTO(31);
/* 60*/ GOTO(27);
/* 70*/ PRINT("Program Fully Completed.");
/* 80*/ sel$ = INPUT("Do it Again (j): ");
/* 90*/ if (sel$ == 106) GOTO(25); // char 106 is 'j'
/*100*/ return;
    }
}
