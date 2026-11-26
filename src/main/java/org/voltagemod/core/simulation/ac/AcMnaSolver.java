package org.voltagemod.core.simulation.ac;

import org.voltagemod.core.physics.electrical.Complex;

public final class AcMnaSolver {
    private static final double EPSILON = 1e-12;

    private AcMnaSolver() {
    }

    public static Complex[] solve(Complex[][] a, Complex[] z) {
        if (a == null || z == null || a.length == 0 || a.length != z.length) {
            throw new IllegalArgumentException("Invalid matrix dimensions.");
        }
        int n = z.length;
        Complex[][] m = new Complex[n][n + 1];
        for (int i = 0; i < n; i++) {
            if (a[i] == null || a[i].length != n) throw new IllegalArgumentException("Invalid matrix dimensions.");
            for (int j = 0; j < n; j++) {
                Complex v = a[i][j];
                m[i][j] = v == null ? Complex.ZERO : v;
            }
            m[i][n] = z[i] == null ? Complex.ZERO : z[i];
        }

        for (int col = 0; col < n; col++) {
            int pivot = col;
            double maxMag = m[col][col].magnitude();
            for (int row = col + 1; row < n; row++) {
                double mag = m[row][col].magnitude();
                if (mag > maxMag) {
                    maxMag = mag;
                    pivot = row;
                }
            }
            if (maxMag <= EPSILON) {
                throw new ArithmeticException("Singular MNA matrix at column " + col);
            }
            if (pivot != col) {
                Complex[] tmp = m[col];
                m[col] = m[pivot];
                m[pivot] = tmp;
            }

            Complex pivotVal = m[col][col];
            for (int row = col + 1; row < n; row++) {
                Complex factor = m[row][col].divide(pivotVal);
                if (factor.getReal() == 0.0 && factor.getImaginary() == 0.0) continue;
                m[row][col] = Complex.ZERO;
                for (int j = col + 1; j <= n; j++) {
                    m[row][j] = m[row][j].subtract(factor.multiply(m[col][j]));
                }
            }
        }

        Complex[] x = new Complex[n];
        for (int i = n - 1; i >= 0; i--) {
            Complex sum = m[i][n];
            for (int j = i + 1; j < n; j++) {
                sum = sum.subtract(m[i][j].multiply(x[j]));
            }
            Complex diag = m[i][i];
            if (diag.magnitude() <= EPSILON) {
                throw new ArithmeticException("Singular MNA matrix at back-substitution row " + i);
            }
            x[i] = sum.divide(diag);
        }
        return x;
    }
}
