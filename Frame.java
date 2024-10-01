class Frame {
    private int ownerProcess;
    private int pageNumber;

    public Frame(int ownerProcess) {
        this.ownerProcess = ownerProcess;
        pageNumber = -1;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageNumber() {
        return pageNumber;
    }
}