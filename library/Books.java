package library;

public class Books {

    private int book_id;
    private String author;
    private String title;
    private String subject;
    private int quantity;

    Books() {
        this.book_id = -1;
        this.author = " ";
        this.title = " ";
        this.subject = " ";
        this.quantity = 0;
    }

    Books(int book_id, String author, String title, String subject, int quantity) {
        this.book_id = book_id;
        this.author = author;
        this.title = title;
        this.subject = subject;
        this.quantity = quantity;
    }

    public void SetBookId(int book_id) { this.book_id = book_id; }
    
    public void SetAuthor(String author) {
        this.author = author;
        new dbConnectivity().ChangeBookInfo(book_id, author, 2);
    }

    public void SetTitle(String title) {
        this.title = title;
        new dbConnectivity().ChangeBookInfo(book_id, title, 1);
    }

    public void SetQuantity(int quantity) {
        this.quantity = quantity;
        new dbConnectivity().UpdateBookQuantity(this.quantity, this.book_id);
    }

    public void SetSubject(String subject) {
        this.subject = subject;
        new dbConnectivity().ChangeBookInfo(book_id, subject, 3);
    }

    public String GetTitle() { return new dbConnectivity().GetTitleofBook(this.book_id); }
    public String GetAuthor() { return new dbConnectivity().GetAuthorofBook(this.book_id); }
    public int GetBookId() { return this.book_id; }
    public String GetSubject() { return new dbConnectivity().GetSubjectofBook(this.book_id); }
    public int GetQuantity() { return new dbConnectivity().GetQuantityofBook(this.book_id); }

    public boolean ChekcAvailability(int book_id) {
        return new dbConnectivity().GetQuantityofBook(this.book_id) > 0;
    }

    public void DecreaseQuantity() {
        if (quantity > 0) {
            this.quantity = this.quantity - 1;
            new dbConnectivity().UpdateBookQuantity(this.quantity, book_id);
        }
    }

    public void IncreaseQuantity() {
        this.quantity = this.quantity + 1;
        new dbConnectivity().UpdateBookQuantity(this.quantity, book_id);
    }

    public String PrintInformation() {
        boolean available = ChekcAvailability(this.book_id);
        String status = available ? "Available" : "Out of Stock";
        
        return String.format("[Book ID: %d] - %s (Author: %s) | %s | Qty: %d | %s", 
                this.book_id, this.title, this.author, this.subject, this.quantity, status);
    }
}