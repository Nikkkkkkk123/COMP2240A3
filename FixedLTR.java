import java.util.List;
import java.util.HashMap;  

public class FixedLTR extends CPU{
    private HashMap<Integer, Frame> mainMemory;
    private HashMap<Integer, Integer> currentPage; // Stores the next available page for a process
    private HashMap<Integer, Integer> lastPage; // Stores the last page used by a process
    private HashMap<Integer, Integer> firstPage; // Stores the first page used by a process
    private int totalAllocatedFrames;

    public FixedLTR(int timeQuantum, String allocationType, List<Process> processList, int frameSize) {
        super(timeQuantum, allocationType, processList);
        mainMemory = new HashMap<>();
        currentPage = new HashMap<>();
        lastPage = new HashMap<>();
        firstPage = new HashMap<>();
        totalAllocatedFrames = Math.floorDiv(frameSize, processList.size());
        allocateFrames(processList, frameSize);
    }

    @Override
    protected void allocateFrames(List<Process> processList, int maxFrames) {
        int usedFrames = 0;
        for (Process process : processList) {
            for (int i = 0; i < totalAllocatedFrames; i++) {
                usedFrames++;
                mainMemory.put(usedFrames, new Frame(process.getPid()));
                if (!currentPage.containsKey(process.getPid())) {
                    currentPage.put(process.getPid(), usedFrames);
                    firstPage.put(process.getPid(), usedFrames);
                }
            }
            lastPage.put(process.getPid(), usedFrames);
        }
        while (mainMemory.size() < maxFrames) {
            usedFrames++;
            mainMemory.put(usedFrames, new Frame(-1));
        }
    }

    @Override
    protected boolean searchForPage(int pid, int page) {
        int process = pid;
        int previousProcess = getPreviousProcess(pid);
        int lastPageUsed = getLastPagePreviousProcess(previousProcess);
        for (int i = (lastPageUsed + 1); i <= lastPage.get(process); i++) {
            if (mainMemory.get(i).getPageNumber() == page) {
                return true;
            }
        }

        return false;
    }

    private int getPreviousProcess(int pid) {
        return pid - 1;
    }

    private int getLastPagePreviousProcess(int previousProcess) {
        if (previousProcess == 0) {
            return (firstPage.get(1) - 1);
        }
        return lastPage.get(previousProcess);
    }

    @Override
    protected void allocatePage(Process process, int page) {
        int pid = process.getPid();
        int nextPage = currentPage.get(pid);
        
        if (nextPage > lastPage.get(pid)) {
            nextPage = firstPage.get(pid);
        }
        mainMemory.get(nextPage).setPageNumber(page);
        currentPage.put(pid, nextPage + 1);
    }

    @Override
    protected void printResults() {
        System.out.println("LRU - Fixed-Local Replacement:");
        System.out.printf("%-5s %-15s %-17s %-10s %s%n", "PID", "Process Name", "Turnaround Time", "# Faults", "Fault Times");
        for (Process process : processList) {
            System.out.printf("%-5d %-15s %-17d %-10d %s%n", process.getPid() , process.getProcessName(), process.getTurnAroundTime() , process.getFaultTimes().size(), process.outputFaultTimes());
        }
        System.out.println();
        System.out.println("------------------------------------------------------------");
    }
}
