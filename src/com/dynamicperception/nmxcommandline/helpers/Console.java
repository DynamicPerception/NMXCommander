package com.dynamicperception.nmxcommandline.helpers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Console {

    /**
     * Convenience print method
     * 
     * @param output
     *            Object to be printed to console
     */
    public static void p(Object output) {
        System.out.print(output);
    }

    /**
     * Convenience print method with new line
     * 
     * @param output
     *            Object to be printed to console
     */
    public static void pln(Object output) {
        System.out.println(output);
    }

    /**
     * Gets string input from user in console
     * 
     * @return The input as a string
     */
    public static String getString() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            String s = br.readLine();
            return s;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "FAIL";
        }
    }

    /**
     * Gets integer input from user in console
     * 
     * @return The input as an integer
     */
    public static int getInteger() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            int i = Integer.parseInt(br.readLine());
            return i;
        } catch (NumberFormatException | IOException nfe) {
            System.err.println("Invalid Format!");
            return Consts.ERROR;
        }
    }

    /**
     * Prints the contents of a file in the console
     * 
     * @param filePath
     *            The path of the file to be printed
     */
    public static void printFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.err.println("Attempted to print file from invalid path!!!");
        }
    }
}
