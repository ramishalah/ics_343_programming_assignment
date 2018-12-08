package com.company;

import java.io.*;
import java.util.Scanner;

public class Main {
    static int value;

    public static void main(String[] args) throws IOException {
        Scanner keyboardScanner = new Scanner(System.in);

        System.out.println("Enter the input file name");
        // reading the input text file from the user
        String inputFileName = keyboardScanner.nextLine();

        System.out.println("Enter the output file name");
        // reading the output text file from the user
        String outputFileName = keyboardScanner.nextLine();


        System.out.println("Enter matrices dimension:");
        // reading the input dimension from the user
        int dimension = keyboardScanner.nextInt();


        // extracting the dimensions of the matrices from the file name
//        int dimension = Integer.parseInt(inputFileName.substring(7, 9));

        // getting the file from the user
        Scanner fileScanner = new Scanner(new File(inputFileName));

        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName));

        // initializing the arrays
        int firstMatrix[][] = new int[(int) Math.pow(2, dimension)][(int) Math.pow(2, dimension)];
        int secondMatrix[][] = new int[(int) Math.pow(2, dimension)][(int) Math.pow(2, dimension)];

        int count = 0;

        // reading the matrices and storing it in the 'a' and 'b'
        for (int i = 0; i < firstMatrix.length; i++) {
            for (int j = 0; j < firstMatrix.length; j++) {
                firstMatrix[i][j] = fileScanner.nextInt();
            }
        }
        for (int i = 0; i < secondMatrix.length; i++) {
            for (int j = 0; j < secondMatrix.length; j++) {
                secondMatrix[i][j] = fileScanner.nextInt();
            }
        }
        int result[][] = new int[firstMatrix.length][firstMatrix.length];

        while(true){

            System.out.println("Choose the algorithm that you want, 1- iterative 2- classical recursive 3- strassan with base=1 4- strassan with base > 1");
            int algorithmselected = keyboardScanner.nextInt();

            // initializing the timers
            long before = 0, after = 0;
            long totalTime = 0;


            switch (algorithmselected) {
                case 1:
                    before = System.nanoTime();
                    result = matrixMultiplicationFinal(firstMatrix, secondMatrix, firstMatrix.length);
                    after = System.nanoTime();
                    break;
                case 2:
                    before = System.nanoTime();
                    result = divideAndConquer(firstMatrix, secondMatrix, firstMatrix.length);
                    after = System.nanoTime();
                    break;
                case 3:
                    before = System.nanoTime();
                    result = strassen(firstMatrix, secondMatrix, firstMatrix.length);
                    after = System.nanoTime();
                    break;
                case 4:
                    System.out.println("Enter base case number power of two:");
                    value = keyboardScanner.nextInt();
                    before = System.nanoTime();
                    result = strassenWithDifferentBaseCase(firstMatrix, secondMatrix, firstMatrix.length);
                    after = System.nanoTime();
                    break;
            }
            // total time
            totalTime = after - before;
            System.out.println("The total time is: " + (totalTime / Math.pow(10, 9)) + " s");

            writer.flush();
            for (int i = 0; i < secondMatrix.length; i++) {
                for (int j = 0; j < secondMatrix.length; j++) {
                    count++;
                    writer.append(result[i][j] + " ");
                    if (count % Math.pow(2, dimension) == 0) {
                        writer.append("\n");
                    }
                }
            }


            System.out.println("Do you want to do it again? yes = 1, no = 0");
            int choice = keyboardScanner.nextInt();

            if(choice == 0) {
                break;
            }
        }



    }

    public static int[][] matrixMultiplicationFinal(int[][] firstMatrix, int[][] secondMatrix , int size) {

        return matrixMultiplication(firstMatrix, secondMatrix, size);
    }

    public static int[][] matrixMultiplication(int[][] firstMatrix, int[][] secondMatrix, int size) {

        int[][] C = new int[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                C[i][j] = 0;
            }
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    C[i][j] += firstMatrix[i][k] * secondMatrix[k][j];
                }
            }
        }
        return C;
    }


    public static int[][] divideAndConquer(int[][] firstMatrix, int[][] secondMatrix, int size) {
        int[][] resultMatrix = new int[size][size];

        if (size == 1) {
            resultMatrix[0][0] = firstMatrix[0][0] * secondMatrix[0][0];
            return resultMatrix;
        } else {
            int[][] A11 = new int[size / 2][size / 2];
            int[][] A12 = new int[size / 2][size / 2];
            int[][] A21 = new int[size / 2][size / 2];
            int[][] A22 = new int[size / 2][size / 2];
            int[][] B11 = new int[size / 2][size / 2];
            int[][] B12 = new int[size / 2][size / 2];
            int[][] B21 = new int[size / 2][size / 2];
            int[][] B22 = new int[size / 2][size / 2];

            // getting the sub-matrices
            getMatrix(firstMatrix, A11, 0, 0);
            getMatrix(firstMatrix, A12, 0, size / 2);
            getMatrix(firstMatrix, A21, size / 2, 0);
            getMatrix(firstMatrix, A22, size / 2, size / 2);
            getMatrix(secondMatrix, B11, 0, 0);
            getMatrix(secondMatrix, B12, 0, size / 2);
            getMatrix(secondMatrix, B21, size / 2, 0);
            getMatrix(secondMatrix, B22, size / 2, size / 2);

            int[][] C11 = add(divideAndConquer(A11, B11, size / 2), divideAndConquer(A12, B21, size / 2), size / 2);
            int[][] C12 = add(divideAndConquer(A11, B12, size / 2), divideAndConquer(A12, B22, size / 2), size / 2);
            int[][] C21 = add(divideAndConquer(A21, B11, size / 2), divideAndConquer(A22, B21, size / 2), size / 2);
            int[][] C22 = add(divideAndConquer(A21, B12, size / 2), divideAndConquer(A22, B22, size / 2), size / 2);


            // combining the results
            combine(C11, resultMatrix, 0, 0);
            combine(C12, resultMatrix, 0, size / 2);
            combine(C21, resultMatrix, size / 2, 0);
            combine(C22, resultMatrix, size / 2, size / 2);
        }

        return resultMatrix;
    }

    public static int[][] strassenWithDifferentBaseCase(int[][] firstMatrix, int[][] secondMatrix, int size) {
        int[][] resultMatrix = new int[size][size];
        strassenCoreWithDifferentBaseCase(firstMatrix, secondMatrix, resultMatrix, size);
        return resultMatrix;
    }

    public static void strassenCoreWithDifferentBaseCase(int[][] firstMatrix, int[][] secondMatrix, int[][] resultMatrix, int size) {
        if (size == value)
            for (int i = 0; i < value; i++) {
                for (int j = 0; j < value; j++) {
                    resultMatrix[i][j] = 0;
                    for (int k = 0; k < value; k++) {
                        resultMatrix[i][j] += firstMatrix[i][k] * firstMatrix[k][j];
                    }
                }
            }
        else {
            // initializing the sub-matrices
            int[][] A11 = new int[size / 2][size / 2];
            int[][] A12 = new int[size / 2][size / 2];
            int[][] A21 = new int[size / 2][size / 2];
            int[][] A22 = new int[size / 2][size / 2];
            int[][] B11 = new int[size / 2][size / 2];
            int[][] B12 = new int[size / 2][size / 2];
            int[][] B21 = new int[size / 2][size / 2];
            int[][] B22 = new int[size / 2][size / 2];


            // initializing the D1 and stuff ..
            int[][] D1 = new int[size / 2][size / 2];
            int[][] D2 = new int[size / 2][size / 2];
            int[][] D3 = new int[size / 2][size / 2];
            int[][] D4 = new int[size / 2][size / 2];
            int[][] D5 = new int[size / 2][size / 2];
            int[][] D6 = new int[size / 2][size / 2];
            int[][] D7 = new int[size / 2][size / 2];

            getMatrix(firstMatrix, A11, 0, 0);
            getMatrix(firstMatrix, A12, 0, size / 2);
            getMatrix(firstMatrix, A21, size / 2, 0);
            getMatrix(firstMatrix, A22, size / 2, size / 2);
            getMatrix(secondMatrix, B11, 0, 0);
            getMatrix(secondMatrix, B12, 0, size / 2);
            getMatrix(secondMatrix, B21, size / 2, 0);
            getMatrix(secondMatrix, B22, size / 2, size / 2);

            //D1
            strassenCoreWithDifferentBaseCase(add(A11, A22, size / 2), add(B11, B22, size / 2), D1, size / 2);
            //D2
            strassenCoreWithDifferentBaseCase(add(A21, A22, size / 2), B11, D2, size / 2);
            //D3
            strassenCoreWithDifferentBaseCase(A11, subtract(B12, B22, size / 2), D3, size / 2);
            //D4
            strassenCoreWithDifferentBaseCase(A22, subtract(B21, B11, size / 2), D4, size / 2);
            //D5
            strassenCoreWithDifferentBaseCase(add(A11, A12, size / 2), B22, D5, size / 2);
            //D6
            strassenCoreWithDifferentBaseCase(subtract(A21, A11, size / 2), add(B11, B12, size / 2), D6, size / 2);
            //D7
            strassenCoreWithDifferentBaseCase(subtract(A12, A22, size / 2), add(B21, B22, size / 2), D7, size / 2);

            //C11
            int[][] C11 = add(subtract(add(D1, D4, D1.length), D5, D5.length), D7, D7.length);
            //C12
            int[][] C12 = add(D3, D5, D3.length);
            //C21
            int[][] C21 = add(D2, D4, D2.length);
            //C22
            int[][] C22 = add(subtract(add(D1, D3, D1.length), D2, D2.length), D6, D6.length);


            // now we are combining the results of all matrices to the result Matrix
            combine(C11, resultMatrix, 0, 0);
            combine(C12, resultMatrix, 0, size / 2);
            combine(C21, resultMatrix, size / 2, 0);
            combine(C22, resultMatrix, size / 2, size / 2);
        }
    }


    public static int[][] strassen(int[][] firstMatrix, int[][] secondMatrix, int size) {
        int[][] resultMatrix = new int[size][size];
        strassenCore(firstMatrix, secondMatrix, resultMatrix, size);
        return resultMatrix;
    }

    public static void strassenCore(int[][] firstMatrix, int[][] secondMatrix, int[][] resultMatrix, int size) {

        if (size == 1) {

            resultMatrix[0][0] = firstMatrix[0][0] * secondMatrix[0][0];

        } else {
            // initializing the sub-matrices
            int[][] A11 = new int[size / 2][size / 2];
            int[][] A12 = new int[size / 2][size / 2];
            int[][] A21 = new int[size / 2][size / 2];
            int[][] A22 = new int[size / 2][size / 2];
            int[][] B11 = new int[size / 2][size / 2];
            int[][] B12 = new int[size / 2][size / 2];
            int[][] B21 = new int[size / 2][size / 2];
            int[][] B22 = new int[size / 2][size / 2];


            // initializing the D1 and stuff ..
            int[][] D1 = new int[size / 2][size / 2];
            int[][] D2 = new int[size / 2][size / 2];
            int[][] D3 = new int[size / 2][size / 2];
            int[][] D4 = new int[size / 2][size / 2];
            int[][] D5 = new int[size / 2][size / 2];
            int[][] D6 = new int[size / 2][size / 2];
            int[][] D7 = new int[size / 2][size / 2];

            getMatrix(firstMatrix, A11, 0, 0);
            getMatrix(firstMatrix, A12, 0, size / 2);
            getMatrix(firstMatrix, A21, size / 2, 0);
            getMatrix(firstMatrix, A22, size / 2, size / 2);
            getMatrix(secondMatrix, B11, 0, 0);
            getMatrix(secondMatrix, B12, 0, size / 2);
            getMatrix(secondMatrix, B21, size / 2, 0);
            getMatrix(secondMatrix, B22, size / 2, size / 2);

            //D1
            strassenCore(add(A11, A22, size / 2), add(B11, B22, size / 2), D1, size / 2);
            //D2
            strassenCore(add(A21, A22, size / 2), B11, D2, size / 2);
            //D3
            strassenCore(A11, subtract(B12, B22, size / 2), D3, size / 2);
            //D4
            strassenCore(A22, subtract(B21, B11, size / 2), D4, size / 2);
            //D5
            strassenCore(add(A11, A12, size / 2), B22, D5, size / 2);
            //D6
            strassenCore(subtract(A21, A11, size / 2), add(B11, B12, size / 2), D6, size / 2);
            //D7
            strassenCore(subtract(A12, A22, size / 2), add(B21, B22, size / 2), D7, size / 2);

            //C11
            int[][] C11 = add(subtract(add(D1, D4, D1.length), D5, D5.length), D7, D7.length);
            //C12
            int[][] C12 = add(D3, D5, D3.length);
            //C21
            int[][] C21 = add(D2, D4, D2.length);
            //C22
            int[][] C22 = add(subtract(add(D1, D3, D1.length), D2, D2.length), D6, D6.length);


            // now we are combining the results of all matrices to the result Matrix
            combine(C11, resultMatrix, 0, 0);
            combine(C12, resultMatrix, 0, size / 2);
            combine(C21, resultMatrix, size / 2, 0);
            combine(C22, resultMatrix, size / 2, size / 2);
        }
    }

    private static void combine(int[][] mat,
                                int[][] newMat, int firstLocation, int secondLocation) {

        int temp = secondLocation;

        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat.length; j++) {
                newMat[firstLocation][temp++] = mat[i][j];
            }
            temp = secondLocation;
            firstLocation++;
        }
    }


    private static void getMatrix(int[][] mat,
                                  int[][] newMat, int firstLocation, int secondLocation) {

        int temp = secondLocation;
        for (int i = 0; i < newMat.length; i++) {
            for (int j = 0; j < newMat.length; j++) {
                newMat[i][j] = mat[firstLocation][temp++];
            }
            temp = secondLocation;
            firstLocation++;
        }
    }


    private static int[][] add(int[][] firstMatrix, int[][] secondMatrix, int size) {

        int[][] resultMatrix = new int[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                resultMatrix[i][j] = firstMatrix[i][j] + secondMatrix[i][j];
            }
        }
        return resultMatrix;
    }


    private static int[][] subtract(int[][] firstMatrix, int[][] secondMatrix, int size) {

        int[][] resultMatrix = new int[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                resultMatrix[i][j] = firstMatrix[i][j] - secondMatrix[i][j];
            }
        }
        return resultMatrix;
    }

}
