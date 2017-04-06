package de.framey.lab.evil.eviltentaclesofdeath.util;

import java.util.Stack;

import org.testng.Assert;
import org.testng.annotations.Test;

import de.framey.lab.evil.eviltentaclesofdeath.util.StackUtil;

public class StackUtilTest {

    @Test
    public void pushAll() {
        Stack<String> source = new Stack<>();
        Stack<String> target = new Stack<>();
        source.push("alpha");
        source.push("beta");
        source.push("gamma");
        StackUtil.pushAll(source, target);
        Assert.assertEquals(target.pop(), "alpha");
        Assert.assertEquals(target.pop(), "beta");
        Assert.assertEquals(target.pop(), "gamma");
    }
}
