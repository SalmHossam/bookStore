package entity;

public class BorrowingRequest {
    private int requestId;
    private int borrowerId;
    private int lenderId;
    private int bookId;
    private String status;
    private User borrower;
    private User lender;
    private Book bookTitle;
    public BorrowingRequest(int requestId, String status, User borrower, User lender,Book bookTitle) {
        this.requestId = requestId;
        this.borrowerId = borrowerId;
        this.lenderId = lenderId;
        this.bookId = bookId;
        this.status = status;
        this.borrower = borrower;
        this.lender = lender;
        this.bookTitle=bookTitle;
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

    public User getBorrower() {
        return borrower;
    }

    public void setBorrower(User borrower) {
        this.borrower = borrower;
    }

    public User getLender() {
        return lender;
    }

    public void setLender(User lender) {
        this.lender = lender;
    }

    public Book getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(Book bookTitle) {
        this.bookTitle = bookTitle;
    }
}
