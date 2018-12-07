package com.company;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.io.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        Scanner keyboardScanner = new Scanner(System.in);

        System.out.println("Enter the input file name");
        // reading the input text file from the user
        String inputFileName = keyboardScanner.nextLine();

        System.out.println("Enter the output file name");
        // reading the output text file from the user
        String outputFileName = keyboardScanner.nextLine();



        // extracting the dimensions of the matrices from the file name
        int dimension = Integer.parseInt(inputFileName.substring(7, 9));

        System.out.println(dimension);

        // getting the file from the user
        Scanner fileScanner = new Scanner(new File(inputFileName));

        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName));

        long startTime, endTime;
        long totalTimeC = 0;
        // initializing the arrays
        int a[][] = new int[(int) Math.pow(2, dimension)][(int) Math.pow(2, dimension)];
        int b[][] = new int[(int) Math.pow(2, dimension)][(int) Math.pow(2, dimension)];

        int count = 0;

        // reading the matrices and storing it in the 'a' and 'b'
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a.length; j++) {
                a[i][j] = fileScanner.nextInt();
            }
        }
        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < b.length; j++) {
                b[i][j] = fileScanner.nextInt();
            }
        }



        startTime = System.nanoTime();
        int result[][] = strassenMM(a, b, a.length);
        //        int result[][] = strassenMM(a, b, a.length);
        endTime = System.nanoTime();
        totalTimeC = endTime - startTime;
        System.out.println("The total time is: " + (totalTimeC / Math.pow(10, 9)) + " seconds");

        writer.flush();
        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < b.length; j++) {
               count++;
               writer.append(result[i][j] + " ");
               if(count % Math.pow(2, dimension) == 0) {
                   writer.append("\n");
               }
            }
        }

    }

    /**
     * Will perform divide and conquer matrix multiplication by recursively
     * calling itself on smaller matrices made up of 1/4 of the original matrix
     *
     * @param A
     *            One matrix to be multiplied
     * @param B
     *            Another matrix to be multiplied
     * @param n
     *            the size of the matrix
     * @return a new array C which is the result of the matrix multiplication
     */
    public static int[][] divideAndConquerMM(int[][] A, int[][] B, int n) {
        int[][] C = new int[n][n];

        if (n == 1) {
            C[0][0] = A[0][0] * B[0][0];
            return C;
        } else {
            int[][] A11 = new int[n / 2][n / 2];
            int[][] A12 = new int[n / 2][n / 2];
            int[][] A21 = new int[n / 2][n / 2];
            int[][] A22 = new int[n / 2][n / 2];
            int[][] B11 = new int[n / 2][n / 2];
            int[][] B12 = new int[n / 2][n / 2];
            int[][] B21 = new int[n / 2][n / 2];
            int[][] B22 = new int[n / 2][n / 2];

            deconstructMatrix(A, A11, 0, 0);
            deconstructMatrix(A, A12, 0, n / 2);
            deconstructMatrix(A, A21, n / 2, 0);
            deconstructMatrix(A, A22, n / 2, n / 2);
            deconstructMatrix(B, B11, 0, 0);
            deconstructMatrix(B, B12, 0, n / 2);
            deconstructMatrix(B, B21, n / 2, 0);
            deconstructMatrix(B, B22, n / 2, n / 2);

            int[][] C11 = addMatrix(divideAndConquerMM(A11, B11, n / 2),
                    divideAndConquerMM(A12, B21, n / 2), n / 2);
            int[][] C12 = addMatrix(divideAndConquerMM(A11, B12, n / 2),
                    divideAndConquerMM(A12, B22, n / 2), n / 2);
            int[][] C21 = addMatrix(divideAndConquerMM(A21, B11, n / 2),
                    divideAndConquerMM(A22, B21, n / 2), n / 2);
            int[][] C22 = addMatrix(divideAndConquerMM(A21, B12, n / 2),
                    divideAndConquerMM(A22, B22, n / 2), n / 2);

            constructMatrix(C11, C, 0, 0);
            constructMatrix(C12, C, 0, n / 2);
            constructMatrix(C21, C, n / 2, 0);
            constructMatrix(C22, C, n / 2, n / 2);
        }

        return C;
    }


//    public static int[][] matrixMultiplicationFinal(int[][] A, int[][] B) {
//
//        return matrixMultiplication(
//                A, B, 0, 0,
//                0, 0, A.length);
//
//    }


//    public static int[][] matrixMultiplication(int[][] A, int[][] B, int rowA, int colA, int rowB, int colB, int size) {
//
//        int[][] C = new int[size][size];
//
//        if (size == 1)
//            C[0][0] = A[rowA][colA] * B[rowB][colB];
//
//        else {
//
//            int newSize = size / 2;
//            //C11
//            sumMatrix(C,
//
//                    matrixMultiplication(A, B, rowA, colA, rowB, colB, newSize),
//                    matrixMultiplication(A, B, rowA, colA + newSize, rowB + newSize, colB, newSize),
//                    0, 0);
//
//            sumMatrix(C,
//
//                    matrixMultiplication(A, B, rowA, colA, rowB, colB + newSize, newSize),
//                    matrixMultiplication(A, B, rowA, colA + newSize, rowB + newSize, colB + newSize, newSize),
//                    0, newSize);
//
//            sumMatrix(C,
//
//                    matrixMultiplication(A, B, rowA + newSize, colA, rowB, colB, newSize),
//                    matrixMultiplication(A, B, rowA + newSize, colA + newSize, rowB + newSize, colB, newSize),
//                    newSize, 0);
//
//            sumMatrix(C,
//
//                    matrixMultiplication(A, B, rowA + newSize, colA, rowB, colB + newSize, newSize),
//                    matrixMultiplication(A, B, rowA + newSize, colA + newSize, rowB + newSize, colB + newSize, newSize),
//                    newSize, newSize);
//        }
//
//        return C;
//
//    }

    /**
     * Will use the strassenMMHelper method to multiply the two matrices
     *
     * @param A
     *            One matrix to be multiplied
     * @param B
     *            Another matrix to be multiplied
     * @param n
     *            the size of the matrix
     * @return a new array C which is the result of the matrix multiplication
     */
    public static int[][] strassenMM(int[][] A, int[][] B, int n) {
        int[][] C = new int[n][n];
        strassenMMHelper(A, B, C, n);
        return C;
    }

    /**
     * Creates 7 new matrices P - V, based on Strassen's algorithm which will be
     * used to find the matrix C, which is the result of the multiplication of A
     * and B.
     *
     * @param A
     *            One matrix to be multiplied
     * @param B
     *            Another matrix to be multiplied
     * @param C
     *            the result of the matrix multiplication
     * @param n
     *            the size of the matrix
     */
    public static void strassenMMHelper(int[][] A, int[][] B, int[][] C, int n) {

        if (n == 2) {
            C[0][0] = (A[0][0] * B[0][0]) + (A[0][1] * B[1][0]);
            C[0][1] = (A[0][0] * B[0][1]) + (A[0][1] * B[1][1]);
            C[1][0] = (A[1][0] * B[0][0]) + (A[1][1] * B[1][0]);
            C[1][1] = (A[1][0] * B[0][1]) + (A[1][1] * B[1][1]);
        } else {
            int[][] A11 = new int[n / 2][n / 2];
            int[][] A12 = new int[n / 2][n / 2];
            int[][] A21 = new int[n / 2][n / 2];
            int[][] A22 = new int[n / 2][n / 2];
            int[][] B11 = new int[n / 2][n / 2];
            int[][] B12 = new int[n / 2][n / 2];
            int[][] B21 = new int[n / 2][n / 2];
            int[][] B22 = new int[n / 2][n / 2];

            int[][] P = new int[n / 2][n / 2];
            int[][] Q = new int[n / 2][n / 2];
            int[][] R = new int[n / 2][n / 2];
            int[][] S = new int[n / 2][n / 2];
            int[][] T = new int[n / 2][n / 2];
            int[][] U = new int[n / 2][n / 2];
            int[][] V = new int[n / 2][n / 2];

            deconstructMatrix(A, A11, 0, 0);
            deconstructMatrix(A, A12, 0, n / 2);
            deconstructMatrix(A, A21, n / 2, 0);
            deconstructMatrix(A, A22, n / 2, n / 2);
            deconstructMatrix(B, B11, 0, 0);
            deconstructMatrix(B, B12, 0, n / 2);
            deconstructMatrix(B, B21, n / 2, 0);
            deconstructMatrix(B, B22, n / 2, n / 2);

            strassenMMHelper(addMatrix(A11, A22, n / 2),
                    addMatrix(B11, B22, n / 2), P, n / 2);
            strassenMMHelper(addMatrix(A21, A22, n / 2), B11, Q, n / 2);
            strassenMMHelper(A11, subtractMatrix(B12, B22, n / 2), R, n / 2);
            strassenMMHelper(A22, subtractMatrix(B21, B11, n / 2), S, n / 2);
            strassenMMHelper(addMatrix(A11, A12, n / 2), B22, T, n / 2);
            strassenMMHelper(subtractMatrix(A21, A11, n / 2),
                    addMatrix(B11, B12, n / 2), U, n / 2);
            strassenMMHelper(subtractMatrix(A12, A22, n / 2),
                    addMatrix(B21, B22, n / 2), V, n / 2);

            int[][] C11 = addMatrix(
                    subtractMatrix(addMatrix(P, S, P.length), T, T.length), V,
                    V.length);
            int[][] C12 = addMatrix(R, T, R.length);
            int[][] C21 = addMatrix(Q, S, Q.length);
            int[][] C22 = addMatrix(
                    subtractMatrix(addMatrix(P, R, P.length), Q, Q.length), U,
                    U.length);

            constructMatrix(C11, C, 0, 0);
            constructMatrix(C12, C, 0, n / 2);
            constructMatrix(C21, C, n / 2, 0);
            constructMatrix(C22, C, n / 2, n / 2);
        }
    }
    /**
     * Creates a new matrix based off of part of another matrix
     *
     * @param initialMatrix
     *            the initial matrix
     * @param newMatrix
     *            the new matrix created from the initial matrix
     * @param a
     *            the initial row position of initialMatrix used when creating
     *            newMatrix
     * @param b
     *            the initial column position of initialMatrix used when
     *            creating newMatrix
     */
    private static void constructMatrix(int[][] initialMatrix,
                                        int[][] newMatrix, int a, int b) {

        int y = b;

        for (int i = 0; i < initialMatrix.length; i++) {
            for (int j = 0; j < initialMatrix.length; j++) {
                newMatrix[a][y++] = initialMatrix[i][j];
            }
            y = b;
            a++;
        }
    }

    /**
     * Adds two matrices together
     *
     * @param A
     *            One matrix to be added
     * @param B
     *            Another matrix to be added
     * @param n
     *            the size of the matrix
     * @return a new array C which is the result of the matrix addition
     */
    private static int[][] addMatrix(int[][] A, int[][] B, int n) {

        int[][] C = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                C[i][j] = A[i][j] + B[i][j];
            }
        }
        return C;
    }

    /**
     * Subtracts two matrices
     *
     * @param A
     *            One matrix to be subtracted
     * @param B
     *            Another matrix to be subtracted
     * @param n
     *            the size of the matrix
     * @return a new array C which is the result of the matrix subtraction
     */
    private static int[][] subtractMatrix(int[][] A, int[][] B, int n) {

        int[][] C = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                C[i][j] = A[i][j] - B[i][j];
            }
        }
        return C;
    }

    /**
     * Creates a new matrix based off of part of another matrix
     *
     * @param initialMatrix
     *            the initial matrix
     * @param newMatrix
     *            the new matrix created from the initial matrix
     * @param a
     *            the initial row position of initialMatrix used when creating
     *            newMatrix
     * @param b
     *            the initial column position of initialMatrix used when
     *            creating newMatrix
     */
    private static void deconstructMatrix(int[][] initialMatrix,
                                          int[][] newMatrix, int a, int b) {

        int y = b;
        for (int i = 0; i < newMatrix.length; i++) {
            for (int j = 0; j < newMatrix.length; j++) {
                newMatrix[i][j] = initialMatrix[a][y++];
            }
            y = b;
            a++;
        }
    }

//    private static void sumMatrix(int[][] C, int[][] A, int[][] B, int rowC, int colC) {
//        int n = A.length;
//        for (int i = 0; i < n; i++) {
//            for (int j = 0; j < n; j++)
//                C[i + rowC][j + colC] = A[i][j] + B[i][j];
//        }
//
//    }
}
