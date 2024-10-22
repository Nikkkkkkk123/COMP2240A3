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
        String[] test = "15 3 Process1.txt Process2.txt Process3.txt Process4.txt".split(" ");
        a3.run(test); // Call the run method
    }

    /*
     * Description: This method is used to run the program
     */
    public void run(String[] args) {
        getProcessInformation(args); // Call the getProcessInformation method

        // Run the FixedLTR algorithm
        FixedLRU fixedLTR = new FixedLRU(timeQuantum, "Fixed", fixedProcessList, frameSize); // Create a new FixedLTR
                                                                                             // object
        fixedLTR.run(); // Call the run method
        fixedLTR.printResults(); // Call the printResults method

        VariableLRU variableLTR = new VariableLRU(timeQuantum, "Variable", variableProcessList, frameSize); // Create a
                                                                                                             // new
                                                                                                            // VariableLTR
                                                                                                            // object
        variableLTR.run(); // Call the run method
        variableLTR.printResults(); // Call the printResults method

    }


    // make compatable with a single line in the process txt
    private void getProcessInformation(String[] arg) {
        // Take the input from the user. Get frame size, time quantum and all the
        // processors
        int processCount = 0; // Declare a variable of type int
        for (int i = 0; i < arg.length; i++) {
            if (i == 0) {
                frameSize = Integer.parseInt(arg[i]); // Set the frame size
            } else if (i == 1) {
                timeQuantum = Integer.parseInt(arg[i]); // Set the time quantum
            } else {
                try {
                    File inputFile = new File(arg[i]); // Create a new File object
                    Scanner scanner = new Scanner(inputFile); // Create a new Scanner object
                    int pageCount = 0; // Used to keep track of the number of pages. This ensures that each process
                                       // has less than or equal to 50 pages
    
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine(); // Read the next line from the file
                        String[] processInfo = line.split(";"); // Split the line by spaces
                        for (int j = 0; j < processInfo.length; j++) {
                            String[] processInfo2 = processInfo[j].trim().split(":");
                            if (processInfo2[0].equals("name")) {
                                processCount++; // Increment the process count
                                String processName = processInfo2[1]; // Get the process name
                                fixedProcessList.add(new Process(processName, processCount));
                                variableProcessList.add(new Process(processName, processCount));
                            } else if (processInfo2[0].equals("end")) {
                                // break;
                                break;
                            } 
                            else {
                                String page = processInfo2[1].trim(); // Get the page
                                fixedProcessList.get(fixedProcessList.size() - 1).addPage(Integer.parseInt(page));
                                variableProcessList.get(variableProcessList.size() - 1).addPage(Integer.parseInt(page));
                                pageCount++; // Increment the page count
                                if (pageCount > 50) {
                                    try {
                                        throw new Exception("You have exceeded the maximum allowed pages in a process being 50. Please use a process with a maximum or 50 pages and try again."); // Throw an exception
                                    } catch (Exception e) {
                                        System.out.println(e.getMessage()); // Print the error message
                                        System.exit(0); // Exit the program
                                    }
                                }
                            }
                        }
                    }

                    scanner.close(); // Close the scanner
                } catch (FileNotFoundException e) {
                    System.out.println("An error occurred."); // Print an error message
                    e.printStackTrace(); // Print the stack trace
                }
            }
        }
    }
}