package entity;

public class BorrowingRequest {
    private int requestId;
    private int borrowerId;
    private int lenderId;
    private int bookId;
    private String status;

    public BorrowingRequest(int requestId, int borrowerId, int lenderId, int bookId, String status) {
        this.requestId = requestId;
        this.borrowerId = borrowerId;
        this.lenderId = lenderId;
        this.bookId = bookId;
        this.status = status;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(int borrowerId) {
        this.borrowerId = borrowerId;
    }

    public int getLenderId() {
        return lenderId;
    }

    public void setLenderId(int lenderId) {
        this.lenderId = lenderId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
