class Frame {
    private int ownerProcess;
    private int pageNumber;
    private int timeAdded;

    /*
     * Description: Default constructor for the Frame class
     * Parameters: None
     * Returns: none
     */
    public Frame() {
        ownerProcess = -1;
        pageNumber = -1;
        timeAdded = -1;
    }

    /*
     * Description: Constructor for the Frame class
     * Parameters: ownerProcess
     * Returns: none
     */
    public Frame(int ownerProcess) {
        this.ownerProcess = ownerProcess;
        pageNumber = -1;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void setOwnerProcess(int ownerProcess) {
        this.ownerProcess = ownerProcess;
    }

    public int getOwnerProcess() {
        return ownerProcess;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setTimeAdded(int timeAdded) {
        this.timeAdded = timeAdded;
    }

    public int getTimeAdded() {
        return timeAdded;
    }
}