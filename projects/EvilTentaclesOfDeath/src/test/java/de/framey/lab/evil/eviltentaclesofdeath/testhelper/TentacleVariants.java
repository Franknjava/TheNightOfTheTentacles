package de.framey.lab.evil.eviltentaclesofdeath.testhelper;

import de.framey.lab.evil.eviltentaclesofdeath.Tentacle;

public class TentacleVariants implements Tentacle {

    public TentacleVariants() {
    }

    public void line() {
        LINE();
    }

    public void label() {
        LABEL("label");
    }

    public void gotoIntStatic() {
        GOTO(19);
    }

    public void gotoIntDynamic() {
        int i = 24;
        GOTO(i);
    }

    public void gotoStringStatic() {
        GOTO("label");
        LABEL("label");
    }

    public void gotoStringDynamic() {
        String target = "label";
        GOTO(target);
        LABEL("label");
    }
}
