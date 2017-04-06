package de.framey.lab.evil.squishytentaclefun.helloworld.formula;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Arrays;

import org.apache.commons.math3.linear.Array2DRowFieldMatrix;
import org.apache.commons.math3.linear.ArrayFieldVector;
import org.apache.commons.math3.linear.FieldDecompositionSolver;
import org.apache.commons.math3.linear.FieldLUDecomposition;
import org.apache.commons.math3.linear.FieldMatrix;
import org.apache.commons.math3.linear.FieldVector;
import org.apache.commons.math3.util.BigReal;

public class SinisterFormulaGenerator {

    public static void main(String[] args) throws Exception {
        new SinisterFormulaGenerator("HELLO WORLD DOMINATION");
    }

    private SinisterFormulaGenerator(String evilMessage) {
        BigDecimal[][] matches = calculateMatchPoints(evilMessage);
        BigDecimal[][] matrix = calculateMatrix(matches[0]);
        BigDecimal[] coeff = calculateCoefficients(matrix, matches[1]);
        boolean isFitting = isFitting(coeff, matches);
        if (isFitting) {
            printFormula(coeff);
            printMethod(coeff);
        } else {
            System.out.println("ERROR: Unable to determine formula!");
        }
    }

    private void printMethod(BigDecimal[] coeff) {
        System.out.println("private int f(int x) {");
        System.out.println("  BigDecimal bdx = BigDecimal.valueOf(x);");
        System.out.println("  return");
        for (int i = 0; i < coeff.length; i++) {
            System.out.print("    new BigDecimal(\"");
            System.out.print(coeff[i]);
            System.out.print("\")");
            if (i > 0) {
                System.out.print(".multiply(bdx");
                if (i > 1) {
                    System.out.print(".pow(");
                    System.out.print(i);
                    System.out.print(")");
                }
                System.out.print(")");
            }
            if (i < coeff.length - 1) {
                System.out.println(".add(");
            }
        }
        for (int i = 0; i < coeff.length - 1; i++) {
            System.out.print(")");
        }
        System.out.println(".setScale(0, RoundingMode.HALF_UP).intValue();");
        System.out.println("}");
    }

    private void printFormula(BigDecimal[] coeff) {
        System.out.print("P(x) = ");
        for (int i = 0; i < coeff.length; i++) {
            if (i > 0) {
                if (coeff[i].signum() == -1) {
                    System.out.print("     - ");
                } else {
                    System.out.print("     + ");
                }
            }
            System.out.print(coeff[i].abs());
            if (i > 0) {
                System.out.print(" * x");
            }
            if (i > 1) {
                System.out.print("^");
                System.out.print(i);
            }
            System.out.println();
        }
    }

    private boolean isFitting(BigDecimal[] coeff, BigDecimal[][] matches) {
        boolean result = true;
        for (int i = 0; i < matches[0].length; i++) {
            BigDecimal x = matches[0][i];
            BigDecimal f = matches[1][i];
            BigDecimal y = BigDecimal.ZERO;
            for (int j = 0; j < coeff.length; j++) {
                y = y.add(coeff[j].multiply(x.pow(j)));
            }
            int fMatch = f.round(MathContext.DECIMAL128).intValue();
            int yMatch = y.round(MathContext.DECIMAL128).intValue();
            if (fMatch != yMatch) {
                result = false;
                break;
            }
        }
        return result;
    }

    private BigDecimal[][] calculateMatrix(BigDecimal[] points) {
        BigDecimal[][] matrix = new BigDecimal[points.length][points.length];
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points.length; j++) {
                matrix[i][j] = points[i].pow(j);
            }
        }
        return matrix;
    }

    private BigDecimal[][] calculateMatchPoints(String message) {
        int length = message.length() + 2;
        BigDecimal[][] matches = new BigDecimal[2][length];
        for (int i = 0; i < length - 2; i++) {
            matches[0][i] = BigDecimal.valueOf(i + 1);
            matches[1][i] = BigDecimal.valueOf(message.charAt(i) == 32 ? 64 : message.charAt(i));
        }
        matches[0][length - 2] = BigDecimal.valueOf(length - 1);
        matches[0][length - 1] = BigDecimal.valueOf(length);
        matches[1][length - 2] = BigDecimal.valueOf(91);
        matches[1][length - 1] = BigDecimal.valueOf(92);
        return matches;
    }

    // COMMONS MATH METHODS

    private BigDecimal[] calculateCoefficients(BigDecimal[][] matrix, BigDecimal[] target) {
        BigReal[][] m = Arrays.stream(matrix).map(row -> Arrays.stream(row).map(field -> new BigReal(field)).toArray(l -> new BigReal[l]))
                .toArray(l -> new BigReal[l][l]);
        BigReal[] t = Arrays.stream(target).map(field -> new BigReal(field)).toArray(l -> new BigReal[l]);
        BigReal[] c = calculateCoefficients(m, t);
        return Arrays.stream(c).map(field -> field.bigDecimalValue()).toArray(l -> new BigDecimal[l]);
    }

    private BigReal[] calculateCoefficients(BigReal[][] matrix, BigReal[] target) {
        FieldMatrix<BigReal> m = new Array2DRowFieldMatrix<>(matrix, false);
        FieldDecompositionSolver<BigReal> s = new FieldLUDecomposition<>(m).getSolver();
        FieldVector<BigReal> v = new ArrayFieldVector<>(target, false);
        FieldVector<BigReal> c = s.solve(v);
        return c.toArray();
    }
}
