package de.framey.lab.evil.eviltentaclesofdeath.testhelper;

import de.framey.lab.evil.eviltentaclesofdeath.Tentacle;

public class TentacleVariantsIllegal implements Tentacle {

    public TentacleVariantsIllegal() {
    }

    public void line() {
        LINE();
    }

    public void labelStatic() {
        LABEL("label");
    }

    public void labelDynamic() {
        String x = "label";
        LABEL(x);
    }

    public void gotoIntStatic() {
        GOTO(24);
    }

    public void gotoIntDynamic() {
        int i = 29;
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

    public void unsupportedCommand() {
        System.out.println("Dagon");
    }
}
