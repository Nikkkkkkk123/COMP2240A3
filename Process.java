import java.util.Queue;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

public class Process {
    private String processName;
    private int pid;
    private int blockedTime;
    private Queue<Integer> pagesQueue;
    private List<Integer> faultTimes;
    private String status;
    private int turnAroundTime;

    public Process(String processName, int pid) {
        this.processName = processName;
        this.pid = pid;
        status = "Blocked";
        blockedTime = 0;
        pagesQueue = new LinkedList<Integer>();
        faultTimes = new ArrayList<Integer>();
    }

    public void addPage(int page) {
        pagesQueue.add(page);
    }

    public void addFaultTime(int time) {
        faultTimes.add(time);
    }

    public List<Integer> getFaultTimes() {
        return faultTimes;
    }

    public int getPid() {
        return pid;
    }

    public Integer getPage() {
        return pagesQueue.poll();
    }

    public Integer getPeekPage() {
        return pagesQueue.peek();
    }

    public String getProcessName() {
        return processName;
    }

    public String getStatus() {
        return status;
    }

    public int getBlockedTime() {
        return blockedTime;
    }

    public void setBlockedTime(int time) {
        blockedTime = time;
    }

    public void setStatus() {
        if (status.equals("Blocked")) {
            status = "Ready";
        } else {
            status = "Blocked";
        }
    }

    public void setEmpty() {
        status = "Empty";
    }

    public Queue<Integer> getPagesQueue() {
        return pagesQueue;
    }

    public String outputFaultTimes() {
        String output = "{";
        for (int i = 0; i < faultTimes.size(); i++) {
            if ((i + 1) == faultTimes.size()) {
                output += faultTimes.get(i);
                break;
            }
            output += faultTimes.get(i) + ", ";
        }
        output += "}";
        return output;
    }

    public void setTurnAroundTime(int time) {
        turnAroundTime = time;
    }

    public int getTurnAroundTime() {
        return turnAroundTime;
    }
}
