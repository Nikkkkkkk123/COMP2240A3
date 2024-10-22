import java.util.List;
import java.util.HashMap;

public class VariableLRU extends CPU {
    private HashMap<Integer, Frame> mainMemory;
    private int currentFrame;

    public VariableLRU(int timeQuantum, String allocationType, List<Process> processList, int frameSize) {
        super(timeQuantum, allocationType, processList);
        mainMemory = new HashMap<>();
        allocateFrames(processList, frameSize);

    }

    @Override
    protected void allocateFrames(List<Process> processList, int maxFrames) {
        for (int i = 1; i <= maxFrames; i++) {
            mainMemory.put(i, new Frame());
        }
        currentFrame = 1; // Sets the current frame equal to the first
    }

    @Override
    protected boolean searchForPage(int pid, int page) {
        for (int i = 1; i <= mainMemory.size(); i++) {
            if (mainMemory.get(i).getOwnerProcess() == pid && mainMemory.get(i).getPageNumber() == page) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void allocatePage(Process process, int page) {
        checkForEmptyFrames();
        mainMemory.get(currentFrame).setTimeAdded(currentTime);
        mainMemory.get(currentFrame).setOwnerProcess(process.getPid());
        mainMemory.get(currentFrame).setPageNumber(page);
        currentFrame++;
    }

    @Override
    protected void updatePage(int pid, int page, int time) {
        for (int i = 1; i <= mainMemory.size(); i++) {
            if (mainMemory.get(i).getOwnerProcess() == pid && mainMemory.get(i).getPageNumber() != -1) {
                mainMemory.get(i).setTimeAdded(time);
            }
        }
    }

    @Override
    protected void removeUsedFrames(Process process) {
        for (int i = 1; i <= mainMemory.size(); i++) {
            if (mainMemory.get(i).getOwnerProcess() == process.getPid()) {
                mainMemory.get(i).setOwnerProcess(-1);
                mainMemory.get(i).setPageNumber(-1);
                mainMemory.get(i).setTimeAdded(-1);
            }
        }
    }

    @Override
    protected void printResults() {
        System.out.println("\nLRU - Variable-Global Replacement:");
        System.out.printf("%-5s %-15s %-17s %-10s %s%n", "PID", "Process Name", "Turnaround Time", "# Faults",
                "Fault Times");
        for (Process process : processList) {
            System.out.printf("%-5d %-15s %-17d %-10d %s%n", process.getPid(), process.getProcessName(),
                    process.getTurnAroundTime(), process.getFaultTimes().size(), process.outputFaultTimes());
        }
    }

    private void checkForEmptyFrames() {
        int lowestTime = Integer.MAX_VALUE;
        for (int i = 1; i <= mainMemory.size(); i++) {
            if (mainMemory.get(i).getOwnerProcess() == -1) {
                currentFrame = i;
                return;
            } else if (mainMemory.get(i).getTimeAdded() < lowestTime) {
                if (mainMemory.get(i).getTimeAdded() == -1) {
                    continue;
                }
                lowestTime = mainMemory.get(i).getTimeAdded();
                currentFrame = i;
            }
        }
        if (lowestTime != -1) {
            return;
        }
        currentFrame = 1;

    }
}
