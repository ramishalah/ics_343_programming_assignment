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

        int result[][] = matrixMultiplicationFinal(a, b);
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


    public static int[][] matrixMultiplicationFinal(int[][] A, int[][] B) {

        return matrixMultiplication(
                A, B, 0, 0,
                0, 0, A.length);

    }


    public static int[][] matrixMultiplication(
            int[][] A, int[][] B, int rowA, int colA,
            int rowB, int colB, int size) {

        int[][] C = new int[size][size];

        if (size == 1)
            C[0][0] = A[rowA][colA] * B[rowB][colB];

        else {

            int newSize = size / 2;
            //C11
            sumMatrix(C,

                    matrixMultiplication(A, B, rowA, colA, rowB, colB, newSize),
                    matrixMultiplication(A, B, rowA, colA + newSize, rowB + newSize, colB, newSize),
                    0, 0);

            sumMatrix(C,

                    matrixMultiplication(A, B, rowA, colA, rowB, colB + newSize, newSize),
                    matrixMultiplication(A, B, rowA, colA + newSize, rowB + newSize, colB + newSize, newSize),
                    0, newSize);

            sumMatrix(C,

                    matrixMultiplication(A, B, rowA + newSize, colA, rowB, colB, newSize),
                    matrixMultiplication(A, B, rowA + newSize, colA + newSize, rowB + newSize, colB, newSize),
                    newSize, 0);

            sumMatrix(C,

                    matrixMultiplication(A, B, rowA + newSize, colA, rowB, colB + newSize, newSize),
                    matrixMultiplication(A, B, rowA + newSize, colA + newSize, rowB + newSize, colB + newSize, newSize),
                    newSize, newSize);
        }

        return C;

    }


    private static void sumMatrix(int[][] C, int[][] A, int[][] B, int rowC, int colC) {
        int n = A.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++)
                C[i + rowC][j + colC] = A[i][j] + B[i][j];
        }

    }
}
