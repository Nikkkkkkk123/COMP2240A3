/*
 * File Name: A3.java
 * Author: Nikkita Nichols (c3362623)
 * Course: COMP2240
 * Date Created: 2024/10/01
 * Last Updated: 2024/10/01
 * Description: 
 */

import java.util.Scanner; // Import the Scanner class
import java.io.File; // Import the File class
import java.io.FileNotFoundException; // Import the FileNotFoundException class
import java.util.ArrayList; // Import the ArrayList class
import java.util.List;

public class A3 {
    private List<Process> fixedProcessList = new ArrayList<Process>(); // Declare a variable of type ArrayList
    private List<Process> variableProcessList = new ArrayList<Process>(); // Declare a variable of type List
    private int timeQuantum; // Declare a variable of type int
    private int frameSize; // Declare a variable of type int

    public static void main(String[] args) {
        A3 a3 = new A3(); // Create an object of A3
        a3.run(args); // Call the run method
    }

    /*
     * Description: This method is used to run the program
     */
    public void run(String[] args) {
        String[] test = "15 3 Process1.txt Process2.txt Process3.txt Process4.txt".split(" "); // Create a test array
        getProcessInformation(test); // Call the getProcessInformation method

        // Run the FixedLTR algorithm
        FixedLTR fixedLTR = new FixedLTR(timeQuantum, "Fixed", fixedProcessList, frameSize); // Create a new FixedLTR object
        fixedLTR.run(); // Call the run method
        fixedLTR.printResults(); // Call the printResults method

    }

    private void getProcessInformation(String[] arg) {
        //Take the input from the user. Get frame size, time quantum and all the processors
        int processCount = 0; // Declare a variable of type int
        for (int i = 0; i < arg.length; i++) {
            if (i == 0) {
                frameSize = Integer.parseInt(arg[i]); // Set the frame size
            }
            else if (i == 1) {
                timeQuantum = Integer.parseInt(arg[i]); // Set the time quantum
            }
            else {
                try {
                    File inputFile = new File(arg[i]); // Create a new File object
                    Scanner scanner = new Scanner(inputFile); // Create a new Scanner object
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine(); // Read the next line from the file
                        String[] processInfo = line.split(" "); // Split the line by spaces
                        if (processInfo[0].equals("name:")) {
                            processCount++; // Increment the process count
                            String processName = processInfo[1].substring(0, (processInfo[1].length() - 1)); // Get the process name
                            fixedProcessList.add(new Process(processName, processCount)); 
                            variableProcessList.add(new Process(processName, processCount)); 
                        }
                        else if (!processInfo[0].equals("end;")) {
                            String page = processInfo[1]; // Get the page
                            page = page.substring(0, page.length() - 1); // Remove the last character
                            fixedProcessList.get(fixedProcessList.size() - 1).addPage(Integer.parseInt(page));
                            variableProcessList.get(variableProcessList.size() - 1).addPage(Integer.parseInt(page));
                        }
                    }
                    scanner.close(); // Close the scanner
                }
                catch (FileNotFoundException e) {
                    System.out.println("An error occurred."); // Print an error message
                    e.printStackTrace(); // Print the stack trace
                }
            }
        }
    }
}