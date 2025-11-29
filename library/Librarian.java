package library;

import java.util.ArrayList;

public class Librarian extends Staff {

    Librarian(int user_id, String user_name, char gender) {
        super(user_id, user_name, gender);
    }

    public void AddBook(ArrayList<Books> BooksList, String NewAuthor, String NewTitle, String NewSubject, int quantity) {
        int book_id = 1;

        if (BooksList != null && !BooksList.isEmpty()) {
            Books LastBook = BooksList.get(BooksList.size() - 1);
            book_id = LastBook.GetBookId() + 1;
        }

        Books NewBook = new Books(book_id, NewTitle, NewAuthor, NewSubject, quantity);
        new dbConnectivity().AddNewBook(book_id, NewTitle, NewAuthor, NewSubject, quantity);
        BooksList.add(NewBook);
    }

    public boolean DeleteBook(ArrayList<Books> BooksList, int book_id) {
        boolean deleted = false;
        Books ToDelete = null;
        dbConnectivity db = new dbConnectivity();
        
        boolean result = db.DeleteABook(book_id);
        
        if (result) {
            for (Books B : BooksList) {
                if (B.GetBookId() == book_id) {
                    ToDelete = B;
                    break;
                }
            }
            if (ToDelete != null) {
                BooksList.remove(ToDelete);
                deleted = true;
            }
        }
        return deleted;
    }

    public void ChangeInfo(ArrayList<Books> BooksList, int book_id, int command, String NewInfo, int Quantity) {
        for (Books b : BooksList) {
            if (b.GetBookId() == book_id) {
                if (command == 1) b.SetTitle(NewInfo);
                else if (command == 2) b.SetAuthor(NewInfo);
                else if (command == 3) b.SetSubject(NewInfo);
                else if (command == 4) b.SetQuantity(Quantity);
                break;
            }
        }
    }
}