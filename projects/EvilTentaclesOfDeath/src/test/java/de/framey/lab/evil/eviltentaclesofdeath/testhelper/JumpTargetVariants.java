package de.framey.lab.evil.eviltentaclesofdeath.testhelper;

import de.framey.lab.evil.eviltentaclesofdeath.Tentacle;

public class JumpTargetVariants implements Tentacle {

    public JumpTargetVariants() {
    }

    @SuppressWarnings("unused")
    private void lineNumberTestMethod() {
        System.out.println("First line!");
        System.out.println("Inbetween line!");
        System.out.println("Last line!");
    }

    @SuppressWarnings("unused")
    private void labelTestMethod() {
        LABEL("first");
        System.out.println("First line!");
        LABEL("inbetween");
        System.out.println("Inbetween line!");
        LABEL("last");
        System.out.println("Last line!");
    }
}
