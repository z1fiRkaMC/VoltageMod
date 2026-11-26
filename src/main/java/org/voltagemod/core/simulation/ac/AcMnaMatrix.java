package org.voltagemod.core.simulation.ac;

import org.voltagemod.core.physics.electrical.Complex;

public final class AcMnaMatrix {
    private final Complex[][] a;
    private final Complex[] z;

    public AcMnaMatrix(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Matrix size must be positive.");
        }
        this.a = new Complex[size][size];
        this.z = new Complex[size];
        for (int i = 0; i < size; i++) {
            z[i] = Complex.ZERO;
            for (int j = 0; j < size; j++) {
                a[i][j] = Complex.ZERO;
            }
        }
    }

    public int size() {
        return z.length;
    }

    public void add(int row, int col, Complex value) {
        requireIndex(row);
        requireIndex(col);
        requireValue(value);
        a[row][col] = a[row][col].add(value);
    }

    public void addRhs(int row, Complex value) {
        requireIndex(row);
        requireValue(value);
        z[row] = z[row].add(value);
    }

    public Complex get(int row, int col) {
        requireIndex(row);
        requireIndex(col);
        return a[row][col];
    }

    public Complex getRhs(int row) {
        requireIndex(row);
        return z[row];
    }

    public Complex[][] getA() {
        return a;
    }

    public Complex[] getZ() {
        return z;
    }

    private void requireIndex(int index) {
        if (index < 0 || index >= z.length) {
            throw new IndexOutOfBoundsException("Matrix index out of bounds: " + index);
        }
    }

    private static void requireValue(Complex value) {
        if (value == null) {
            throw new IllegalArgumentException("Matrix value cannot be null.");
        }
    }
}
