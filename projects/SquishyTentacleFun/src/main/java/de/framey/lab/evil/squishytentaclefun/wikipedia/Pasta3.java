package de.framey.lab.evil.squishytentaclefun.wikipedia;

import de.framey.lab.evil.eviltentaclesofdeath.Tentacle;

public class Pasta3 implements Tentacle, Basicified {

    public static void main(String[] args) throws Exception {
        new Pasta3().spaghetti();
    }

    //  10 FOR ia = 1 TO 10
    //  20 IF ia = 5 THEN
    //  30 FOR ib = 1 TO 10
    //  40 PRINT "LOOP:";ia;" SUB LOOP:";ib
    //  50 IF ib = 8 THEN GOTO 80
    //  60 NEXT ib
    //  70 END IF
    //  80 PRINT "SUB LOOP:";ia;" END"
    //  90 NEXT ia
    // 100 END
    private void spaghetti() {
/* 10*/ for (int ia = 1; ia <= 10; ia++) {
/* 20*/     if (ia == 5) {
/* 30*/         for (int ib = 1; ib <= 10; ib++) {
/* 40*/             PRINT("LOOP:" + ia + " SUB LOOP:" + ib);
/* 50*/             if (ib == 8) GOTO(29);
/* 60*/         }
/* 70*/     }
/* 80*/     PRINT("SUB LOOP:" + ia + " END");
/* 90*/ }
/*100*/ return;
    }
}
