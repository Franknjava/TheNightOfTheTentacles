package de.framey.lab.evil.eviltentaclesofdeath;

import org.testng.annotations.Test;

import de.framey.lab.evil.eviltentaclesofdeath.Tentacle;

public class TentacleTest implements Tentacle {

    @Test(expectedExceptions = { UnsupportedOperationException.class })
    public void testGOTOint() {
        GOTO(17);
    }

    @Test(expectedExceptions = { UnsupportedOperationException.class })
    public void testGOTOString() {
        GOTO("TEST");
    }

    @Test(expectedExceptions = { UnsupportedOperationException.class })
    public void testLABEL() {
        LABEL("TEST");
    }

    @Test(expectedExceptions = { UnsupportedOperationException.class })
    public void testLINE() {
        LINE();
    }
}
