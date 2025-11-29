package library;

import java.util.*;
import java.text.SimpleDateFormat;

public class Loan {

    private int loanId;
    private Date issue_date;
    private Date due_date;
    private Date return_date;
    private Users borrower;
    private Books borrowed_book;
    private String fine_status;
    private boolean returned_status;

    Loan() {
        loanId = -1;
        Calendar calendar = Calendar.getInstance();
        issue_date = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 7);
        due_date = calendar.getTime();
        this.return_date = due_date;
        returned_status = false;
        fine_status = "no fine";
    }
    
    Loan(int id ) {
        loanId = id;
        Calendar calendar = Calendar.getInstance();
        issue_date = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, 7);
        due_date = calendar.getTime();
        this.return_date = due_date;
        returned_status = false;
        fine_status = "no fine";
    }

    Loan(int id, int user_id, int borrowedbook_id, boolean returned_status, String fine_status, Date issue_date, Date due_date, Date return_date) {
        dbConnectivity db = new dbConnectivity();
        this.loanId = id;
        this.issue_date = issue_date;
        this.return_date = return_date;
        this.due_date = due_date;
        this.fine_status = fine_status;
        this.borrower = db.GetaBorrowerObjectByUserId(user_id);
        this.borrowed_book = db.GetaBookbyId(borrowedbook_id);
        this.returned_status = returned_status;
    }

    Loan(int id , Users borrower, Books borrowed_book, String finestatus, boolean returned_status, Date issue_date, Date due_date, Date return_date) {
        this.loanId =id; 
        this.issue_date = issue_date;
        this.return_date = return_date;
        this.due_date = due_date;
        this.fine_status = finestatus;
        this.borrower = borrower;
        this.borrowed_book = borrowed_book;
        this.returned_status = returned_status;
    }

    public int GetaBookId() { return this.borrowed_book.GetBookId(); }
    public String GetFineStatus() { return fine_status; }
    public int GetLoanId() { return this.loanId; }
    public Date getReturnDate() { return this.return_date; }
    public Date getDueDate() { return this.due_date; }
    public Date getIssueDate() { return this.issue_date; }
    public Books GetaBook() { return new dbConnectivity().GetLoanedBook(this.loanId); }
    public int GetaBorrowerId() { return this.borrower.GetId(); }
    public Users Getborrower() { return this.borrower; }
    public boolean GetStatus() { return this.returned_status; }

    public void SetReturnedDate(Date Ret_date) {
        this.return_date = Ret_date;
        new dbConnectivity().SetLoanReturnedDate(this.loanId, Ret_date);
    }

    public void SetaBook(Books NewBook) {
        this.borrowed_book = NewBook;
        new dbConnectivity().SetLoanedBook(this.loanId, this.borrowed_book.GetBookId());
    }

    public void SetaBorrower(Users Loanee) {
        this.borrower = Loanee;
        new dbConnectivity().SetLoaneeObject(this.loanId, this.borrower.GetId());
    }

    public void SetReturnStatus(boolean status) {
        this.returned_status = status;
        new dbConnectivity().SetReturnStatus(this.loanId, status);
    }

    public void SetLoan(Loan Update) {
        this.borrowed_book = Update.borrowed_book;
        this.borrower = Update.borrower;
        this.due_date = Update.due_date;
        this.issue_date = Update.issue_date;
        this.fine_status = Update.fine_status;
        this.return_date = Update.return_date;
        this.returned_status = Update.returned_status;
        new dbConnectivity().SetLoan(this.loanId, Update);
    }

    public void SetFineStatus(String status) {
        this.fine_status = status;
        new dbConnectivity().SetLoanFineStatus(loanId, status);
    }

    public double CalculateFine() {
        if (return_date != null && return_date.after(due_date)) {
            long difference = (return_date.getTime() - due_date.getTime()) / 86400000;
            return 30.0 * Math.abs(difference);
        }
        return 0.0;
    }

    String PrintLoanInfo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String sIssue = (issue_date != null) ? sdf.format(issue_date) : "N/A";
        String sDue = (due_date != null) ? sdf.format(due_date) : "N/A";
        String sReturn = (return_date != null) ? sdf.format(return_date) : "Not Returned";
        String status = returned_status ? "[RETURNED]" : "[ON LOAN]";
        
        String bookInfo = borrowed_book.PrintInformation(); 

        return String.format("  + %s (Loan ID: %d)\n    %s\n    Issue Date: %s  |  Due Date: %s  |  Return Date: %s\n    Fine Status: %s\n",
                status, loanId, bookInfo, sIssue, sDue, sReturn, fine_status);
    }
}