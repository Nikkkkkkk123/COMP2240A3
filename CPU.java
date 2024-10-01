/*
 * File Name: CPU.java
 * Author: Nikkita Nichols (c3362623)
 * Course: COMP2240
 * Date Created: 2024/10/01
 * Last Updated: 2024/10/01
 * Description: 
 */

import java.util.LinkedList;
import java.util.Queue;
import java.util.List;

public abstract class CPU {
    protected int currentTime;
    protected String allocationType; // Used to specify the allocation type (Fixed, Variable)
    protected int timeQuantum;
    protected Process currentProcess;
    protected Queue<Process> readyQueue;
    protected Queue<Process> blockedQueue;
    protected List<Process> processList;

    /*
     * Description: This method is used to run the CPU
     * Parameters: None
     * Returns: None
     */
    public CPU(int timeQuantum, String allocationType, List<Process> processList) {
        readyQueue = new LinkedList<Process>();
        blockedQueue = new LinkedList<Process>();
        currentTime = 0;
        this.timeQuantum = timeQuantum;
        this.allocationType = allocationType;
        this.processList = processList;
    }

    public void run () {
        startReadyQueue(currentTime);
        while (!readyQueue.isEmpty() || !blockedQueue.isEmpty()) {
            if (!readyQueue.isEmpty()) {
                currentProcess = readyQueue.poll();
                int page = currentProcess.getPeekPage();
                if (!searchForPage(currentProcess.getPid(), page)) {
                    currentProcess.setStatus();
                    currentProcess.setBlockedTime(currentTime);
                    currentProcess.addFaultTime(currentTime);
                    blockedQueue.add(currentProcess);
                }
                else {
                    int simulationTime = 0;
                    while (currentProcess.getStatus().equals("Ready") && simulationTime < timeQuantum) {
                        if (currentProcess.getPagesQueue().isEmpty()) {
                            break;
                        }
                        else if (!searchForPage(currentProcess.getPid(), currentProcess.getPeekPage())) {
                            currentProcess.setStatus();
                            currentProcess.setBlockedTime(currentTime);
                            currentProcess.addFaultTime(currentTime);
                            // Something goes wrong attime 26 (actually being 27 because of the incrementation)
                            System.out.println(currentProcess.getPid() + " " + currentTime);
                            break;
                        }
                        else {
                            //System.out.println("Process: " +currentProcess.getPid()+ " Page: " + currentProcess.getPage() + " Time: " + currentTime);
                            currentProcess.getPage(); // Remove the page from the queue
                            if (currentProcess.getPagesQueue().isEmpty()) {
                                currentTime++;
                                currentProcess.setEmpty();
                                checkBlockedQueueTime(currentTime);
                                break;
                            }
                            currentTime++;
                            checkBlockedQueueTime(currentTime);
                            simulationTime++;
                        }
                    }
                    if (currentProcess.getStatus().equals("Blocked")) {
                        checkBlockedQueueTime(currentTime);
                        blockedQueue.add(currentProcess);
                    }
                    else if (currentProcess.getStatus().equals("Ready")) {
                        readyQueue.add(currentProcess);
                    }
                    else if (currentProcess.getStatus().equals("Empty")) {
                        currentProcess.setTurnAroundTime(currentTime);
                    }
                }
            }
            else {
                currentTime++;
                if (!blockedQueue.isEmpty()) {
                    Queue<Process> tempQueue = new LinkedList<Process>();
                    tempQueue.addAll(blockedQueue);
                    for (Process process : tempQueue) {
                        if ((currentTime - process.getBlockedTime()) == 4) {
                            process.setStatus();
                            allocatePage(process, process.getPeekPage());
                            readyQueue.add(blockedQueue.poll());
                        }
                    }
                }
            }
        }
    }

    protected abstract void startReadyQueue(int time);
    protected abstract boolean searchForPage(int pid, int page);
    protected abstract void allocatePage(Process process, int page);
    protected abstract void checkBlockedQueueTime(int time);
    protected abstract void printResults();
}