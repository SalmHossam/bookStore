package entity;

public class RequestHistory {
    private int historyId;
    private int requestId;
    private String status;

    public RequestHistory(int historyId, String pending) {
    }

    public int getHistoryId() {
        return historyId;
    }

    public void setHistoryId(int historyId) {
        this.historyId = historyId;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public RequestHistory(int historyId, int requestId, String status) {
        this.historyId = historyId;
        this.requestId = requestId;
        this.status = status;
    }
}
