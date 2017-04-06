package de.framey.lab.evil.squishytentaclefun.helloworld;

import java.math.BigDecimal;
import java.math.RoundingMode;

import de.framey.lab.evil.eviltentaclesofdeath.Tentacle;

/**
 * Simple, sinister hello world program to demonstrate the evil powers of tentacles.
 * 
 * @author Frank Meyfarth
 */
public class HelloWorld implements Tentacle {

    /**
     * Main method to start it all off.
     * @param args No args needed
     */
    public static void main(String[] args) {
        new HelloWorld().sayHello();
    }

    /**
     * Every great plan starts with a wierd formula. This is our HelloWorld formula.
     * @param x the current value of x
     * @return f(x)
     */
    private int f(int x) {
        BigDecimal bdx = BigDecimal.valueOf(x);
        return
          new BigDecimal("3882585.9999999999999999999999999999999999999999999428041500738804169558").add(
          new BigDecimal("-13924656.4417784999508650067271685255263125770596491290784190807657213985").multiply(bdx).add(
          new BigDecimal("21911753.7906848008661323700257934238607847764429053656464644755079084635").multiply(bdx.pow(2)).add(
          new BigDecimal("-20431557.1480175881632850584705648900385806312451289053940550122434691334").multiply(bdx.pow(3)).add(
          new BigDecimal("12805928.0712479169456490666407381631482967923977073778555199667826433437").multiply(bdx.pow(4)).add(
          new BigDecimal("-5786302.5736901255121124233486527331940740969069648297726896823031531123").multiply(bdx.pow(5)).add(
          new BigDecimal("1970216.8031444216891207010557219776710041951941180334365262054046968069").multiply(bdx.pow(6)).add(
          new BigDecimal("-521020.3150340978019731058690984224983833057515205145917489591281885714").multiply(bdx.pow(7)).add(
          new BigDecimal("109325.7790737148087029494871695537970278319092741952332626776990531654").multiply(bdx.pow(8)).add(
          new BigDecimal("-18485.7598691805509824970213292208757061364771115332626248471240648853").multiply(bdx.pow(9)).add(
          new BigDecimal("2547.0184654220933798685681331743871986306408635010038993327230203763").multiply(bdx.pow(10)).add(
          new BigDecimal("-288.1773474149205888338394317895171734649912752429376808632004400079").multiply(bdx.pow(11)).add(
          new BigDecimal("26.9044914545886665956306642266772671347520366813819955914903342987").multiply(bdx.pow(12)).add(
          new BigDecimal("-2.0772777563094335563971712716657757899095436729269752833151570127").multiply(bdx.pow(13)).add(
          new BigDecimal("0.1325915060575035453859797383214980706652272397912153505363193353").multiply(bdx.pow(14)).add(
          new BigDecimal("-0.0069748393881003940303026449830850603505344414053572995706673487").multiply(bdx.pow(15)).add(
          new BigDecimal("0.0003004706295151882787657262056075079226726716998545716723235452").multiply(bdx.pow(16)).add(
          new BigDecimal("-0.0000104910616768058352588255084232899227209708603912369093288079").multiply(bdx.pow(17)).add(
          new BigDecimal("2.922180839781618519039413323052704731302096173970639100287E-7").multiply(bdx.pow(18)).add(
          new BigDecimal("-6.3397953726983011360288370290985155427791914918707764810E-9").multiply(bdx.pow(19)).add(
          new BigDecimal("1.032371471693202930640509094026086701432776618319665065E-10").multiply(bdx.pow(20)).add(
          new BigDecimal("-1.1869163382463228067486665012744970394046918743865921E-12").multiply(bdx.pow(21)).add(
          new BigDecimal("8.5895958618189259318454021124897891143799198477536E-15").multiply(bdx.pow(22)).add(
          new BigDecimal("-2.94244742173568999517857600189463592362070872284E-17").multiply(bdx.pow(23)))))))))))))))))))))))))
          .setScale(0, RoundingMode.HALF_UP).intValue();
    }
    
    /**
     * The actual Hello World program code. 
     */
    private void sayHello() {
        int x = 1;
        GOTO(f(x++));
        System.out.print((char)(LINE()/2)); GOTO(f(x++));
        System.out.print((char) LINE()); GOTO(f(x++));
        System.out.print((char) LINE()); GOTO(f(x++));
        System.out.print((char) LINE()); GOTO(f(x++));
        System.out.print((char) LINE()); GOTO(f(x++));
        System.out.print((char) LINE()); GOTO(f(x++));
        System.out.print((char) LINE()); GOTO(f(x++));
        System.out.print((char) LINE()); GOTO(f(x++));
        System.out.print((char) LINE()); GOTO(f(x++));
        System.out.print((char) LINE()); GOTO(f(x++));
        System.out.print((char) LINE()); GOTO(f(x++));
        System.out.print((char) LINE()); GOTO(f(x++));
        System.out.print((char) LINE()); GOTO(f(x++));
        System.out.print((char) LINE()); GOTO(f(x++));
        System.out.print((char) LINE()); GOTO(f(x++));
        System.out.print((char) LINE()); GOTO(f(x++));
        System.out.print((char) LINE()); GOTO(f(x++));
        System.out.print((char) LINE()); GOTO(f(x++));
        System.out.print((char) LINE()); GOTO(f(x++));
        System.out.print((char) LINE()); GOTO(f(x++));
        System.out.print((char) LINE()); GOTO(f(x++));
        System.out.print((char) LINE()); GOTO(f(x++));
        System.out.print((char) LINE()); GOTO(f(x++));
        System.out.print((char) LINE()); GOTO(f(x++));
        System.out.print((char) LINE()); GOTO(f(x++));
        System.out.print((char) LINE()); GOTO(f(x++));
        System.out.print((char) LINE()); GOTO(f(x++));
        System.out.print((char)(LINE()/9)); GOTO(f(x++));
        return;
    }
}
