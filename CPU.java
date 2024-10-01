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
        startReadyQueue();
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
                            break;
                        }
                        else {
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
                        if (allocationType.equals("Variable")) {
                            removeUsedFrames(currentProcess);
                        }
                        currentProcess.setTurnAroundTime(currentTime);
                    }
                }
            }
            else {
                currentTime++;
                if (!blockedQueue.isEmpty()) {
                    checkBlockedQueueTime(currentTime);
                }
            }
        }
    }

    private void startReadyQueue() {
        for (Process process : processList) {
            process.setStatus();
            readyQueue.add(process);
        }
    }

    private void checkBlockedQueueTime(int time) {
        Queue<Process> tempBlockedQueue = new LinkedList<>();
        tempBlockedQueue.addAll(blockedQueue);
        for (Process process : tempBlockedQueue) {
            if ((time - process.getBlockedTime()) >= 4) {
                process.setStatus();
                readyQueue.add(process);
                allocatePage(process, process.getPeekPage());
                blockedQueue.remove(process);
            }
        }
    }

    protected void removeUsedFrames(Process process) {
        throw new UnsupportedOperationException("Not supported.");
    }

    protected abstract void allocateFrames(List<Process> processList, int maxFrames);
    protected abstract boolean searchForPage(int pid, int page);
    protected abstract void allocatePage(Process process, int page);
    protected abstract void printResults();
}