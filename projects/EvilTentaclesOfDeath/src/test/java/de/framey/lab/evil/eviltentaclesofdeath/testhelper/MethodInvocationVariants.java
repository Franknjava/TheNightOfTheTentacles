package de.framey.lab.evil.eviltentaclesofdeath.testhelper;

public class MethodInvocationVariants extends Super implements Interface {

    private static final Super SUPER = new Super();

    public MethodInvocationVariants() {
    }

    @SuppressWarnings("unused")
    private void thisDefaultNP() {
        defaultTest();
    }

    @SuppressWarnings("unused")
    private void thisDefaultYP() {
        defaultTest(5);
    }

    @SuppressWarnings("unused")
    private void thisDefaultYPDyn() {
        int i = 5;
        defaultTest(i);
    }

    @SuppressWarnings("unused")
    private void superNP() {
        superTest();
    }

    @SuppressWarnings("unused")
    private void superYP() {
        superTest(5);
    }

    @SuppressWarnings("unused")
    private void superYPDyn() {
        int i = 5;
        superTest(i);
    }

    @SuppressWarnings("unused")
    private void thisNP() {
        thisTest();
    }

    @SuppressWarnings("unused")
    private void thisYP() {
        thisTest(5);
    }

    @SuppressWarnings("unused")
    private void thisYPDyn() {
        int i = 5;
        thisTest(i);
    }

    @SuppressWarnings("unused")
    private void staticNP() {
        MethodInvocationVariants.staticTest();
    }

    @SuppressWarnings("unused")
    private void staticYP() {
        MethodInvocationVariants.staticTest(5);
    }

    @SuppressWarnings("unused")
    private void staticYPDyn() {
        int i = 5;
        MethodInvocationVariants.staticTest(i);
    }

    @SuppressWarnings("unused")
    private void staticSuperNP() {
        SUPER.superTest();
    }

    @SuppressWarnings("unused")
    private void staticSuperYP() {
        SUPER.superTest(5);
    }

    @SuppressWarnings("unused")
    private void staticSuperYPDyn() {
        int i = 5;
        SUPER.superTest(i);
    }

    private void thisTest() {
    }

    private void thisTest(int x) {
    }

    private static void staticTest() {
    }

    private static void staticTest(int x) {
    }
}
