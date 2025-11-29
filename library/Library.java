package library;

import java.util.*;
import java.io.*;

public class Library {
    
    // Database connectivity instance
    dbConnectivity db;
    
    // Static lists to hold loaded data
    public static ArrayList<Users> UsersList;
    public static ArrayList<Books> BooksList;
    public static ArrayList<Librarian> LibrarianList;
    public static ArrayList<Clerk> ClerkList;
    public static ArrayList<Loan> LoanList;

    // Constructor: Loads all data from database on startup
    Library() {
        db = new dbConnectivity();
        
        // Load data from DB into memory
        UsersList = db.LoadAllBorrowers();
        BooksList = db.LoadAllBooks();
        LibrarianList = db.LoadAllLibrarians();
        ClerkList = db.LoadAllClerk();
        LoanList = db.LoadLoanList();
        
        System.out.println("--- Loading Data ---");
        System.out.println("Borrowers loaded: " + UsersList.size());
        System.out.println("Books loaded: " + BooksList.size());
        System.out.println("Librarians loaded: " + LibrarianList.size());
        System.out.println("Clerks loaded: " + ClerkList.size());
        System.out.println("Loans loaded: " + LoanList.size());
        System.out.println("--- System Ready ---");
    }
    
    // --- Login Validation ---
    public boolean validateLogin(String role, int id, String password) {
        if (db == null) {
            db = new dbConnectivity();
        }
        return db.validateLogin(role, id, password);
    }
    
    // --- Existence Checks (Legacy, kept for reference) ---
    boolean IsBorrowerPresent(int id) {
        for (Users U : UsersList) {
            if (U.GetId() == id) return true;
        }
        return false;
    } 

    boolean IsClerkPresent(int id) {
        for (Clerk C : ClerkList) {
            if (C.GetId() == id) return true;
        }
        return false;
    } 

    boolean IsLibrarianPresent(int id) {
        for (Librarian L : LibrarianList) {
            if (L.GetId() == id) return true;
        }
        return false;
    } 

    // --- CLERK FUNCTIONS ---

    // Fixed: Now searches for the target user instead of displaying Clerk's own info
    String CheckLoanofUser(int user_id, int clerk_id) {
        for (Users u : UsersList) {
            if (u.GetId() == user_id) {
                return u.ViewInformation(LoanList, user_id);
            }
        }
        return "User ID not found: " + user_id;
    }

    ArrayList<Books> ClerkSearchBookbyTitle(String title, int clerk_id) {
        for (Clerk C : ClerkList) {
            if (C.GetId() == clerk_id) return C.SearchBookbyTitle(title);
        }
        return new ArrayList<>();
    }

    ArrayList<Books> ClerkSearchBookbyAuthor(String author, int clerk_id) {
        for (Clerk C : ClerkList) {
            if (C.GetId() == clerk_id) return C.SearchBookbyAuthor(author);
        }
        return new ArrayList<>();
    }

    ArrayList<Books> ClerkSearchBookbySubject(String subject, int clerk_id) {
        for (Clerk C : ClerkList) {
            if (C.GetId() == clerk_id) return C.SearchBookbySubject(subject);
        }
        return new ArrayList<>();
    }

    boolean AddNewBorrower(String borrower_name, char gender, String tel_num, String address, int clerk_id) {
        for (Clerk C : ClerkList) {
            if (C.GetId() == clerk_id) {
                return C.AddaBorrower(UsersList, borrower_name, gender, tel_num, address);
            }
        }
        return false;
    }

    Boolean ClerkUpdatingInfo(String Info, int choice, int clerk_id, int user_id) {
        for (Clerk C : ClerkList) {
            if (C.GetId() == clerk_id) {
                return C.UpdatePerosnalInformation(UsersList, Info, choice, user_id);
            }
        }
        return false;
    }

    void ClerkRecordFine(int user_id, int clerk_id) {
        for (Clerk C : ClerkList) {
            if (C.GetId() == clerk_id) {
                C.PayFine(user_id, UsersList, LoanList);
            }
        }
    }

    void ClerkCheckInItem(String ret_date, int book_id, int borrower_id, int clerk_id) {
        for (Clerk C : ClerkList) {
            if (C.GetId() == clerk_id) {
                for (Users U : UsersList) {
                    if (borrower_id == U.GetId()) {
                        C.CheckInItem(ret_date, book_id, U, BooksList, LoanList);
                    }
                }
            }
        }
    }

    // Fixed: Added empty list check to avoid IndexOutOfBoundsException
    void ClerkCheckOutItem(int book_id, int borrower_id, int clerk_id) {
        for (Clerk C : ClerkList) {
            if (C.GetId() == clerk_id) {
                for (Users U : UsersList) {
                    if (borrower_id == U.GetId()) {
                        int index = 1;
                        if (!LoanList.isEmpty()) {
                            Loan L = LoanList.get(LoanList.size() - 1);
                            index = L.GetLoanId() + 1;
                        }
                        Loan LoanObj = new Loan(index);
                        C.CheckOutItem(book_id, U, BooksList, LoanObj, LoanList);
                    }
                }
            }
        }
    }

    void ClerkRenewItem(int book_id, int option, int clerk_id) {
        for (Clerk C : ClerkList) {
            if (C.GetId() == clerk_id) {
                C.RenewItem(book_id, BooksList, option);
            }
        }
    }

    // --- LIBRARIAN FUNCTIONS ---

    // Fixed: Now searches for the target user
    String LibrarianCheckLoanofUser(int user_id, int lib_id) {
        for (Users u : UsersList) {
            if (u.GetId() == user_id) {
                return u.ViewInformation(LoanList, user_id);
            }
        }
        return "User ID not found: " + user_id;
    }

    ArrayList<Books> LibrarianSearchBookbyTitle(String title, int lib_id) {
        for (Librarian L : LibrarianList) {
            if (L.GetId() == lib_id) return L.SearchBookbyTitle(title);
        }
        return new ArrayList<>();
    }

    ArrayList<Books> LibrarianSearchBookbyAuthor(String author, int lib_id) {
        for (Librarian L : LibrarianList) {
            if (L.GetId() == lib_id) return L.SearchBookbyAuthor(author);
        }
        return new ArrayList<>();
    }

    ArrayList<Books> LibrarianSearchBookbySubject(String subject, int lib_id) {
        for (Librarian L : LibrarianList) {
            if (L.GetId() == lib_id) return L.SearchBookbySubject(subject);
        }
        return new ArrayList<>();
    }

    boolean AddNewBorrowerLibrarian(String borrower_name, char gender, String tel_num, String address, int lib_id) {
        for (Librarian L : LibrarianList) {
            if (L.GetId() == lib_id) {
                return L.AddaBorrower(UsersList, borrower_name, gender, tel_num, address);
            }
        }
        return false;
    }

    Boolean LibrarianUpdatingInfo(String Info, int choice, int lib_id, int user_id) {
        for (Librarian L : LibrarianList) {
            if (L.GetId() == lib_id) {
                return L.UpdatePerosnalInformation(UsersList, Info, choice, user_id);
            }
        }
        return false;
    }

    void LibrarianRecordFine(int user_id, int lib_id) {
        for (Librarian L : LibrarianList) {
            if (L.GetId() == lib_id) {
                L.PayFine(user_id, UsersList, LoanList);
            }
        }
    }

    void LibrarianCheckInItem(String ret_date, int book_id, int borrower_id, int lib_id) {
        for (Librarian L : LibrarianList) {
            if (L.GetId() == lib_id) {
                for (Users U : UsersList) {
                    if (borrower_id == U.GetId()) {
                        L.CheckInItem(ret_date, book_id, U, BooksList, LoanList);
                    }
                }
            }
        }
    }

    // Fixed: Added empty list check
    void LibrarianCheckOutItem(int book_id, int borrower_id, int lib_id) {
        for (Librarian L1 : LibrarianList) {
            if (L1.GetId() == lib_id) {
                for (Users U : UsersList) {
                    if (borrower_id == U.GetId()) {
                        int index = 1;
                        if (!LoanList.isEmpty()) {
                            Loan L = LoanList.get(LoanList.size() - 1);
                            index = L.GetLoanId() + 1;
                        }
                        Loan LoanObj = new Loan(index);
                        L1.CheckOutItem(book_id, U, BooksList, LoanObj, LoanList);
                    }
                }
            }
        }
    }

    void LibrarianRenewItem(int book_id, int option, int lib_id) {
        for (Librarian L : LibrarianList) {
            if (L.GetId() == lib_id) {
                L.RenewItem(book_id, BooksList, option);
            }
        }
    }

    void LibrarianAddNewBook(String NewAuthor, String NewTitle, String NewSubject, int quantity, int lib_id) {
        for (Librarian L : LibrarianList) {
            if (L.GetId() == lib_id) {
                L.AddBook(BooksList, NewAuthor, NewTitle, NewSubject, quantity);
            }
        }
    }

    boolean LibrarianDeleteBook(int book_id, int lib_id) {
        for (Librarian L : LibrarianList) {
            if (L.GetId() == lib_id) {
                return L.DeleteBook(BooksList, book_id);
            }
        }
        return false;
    }

    void LibrarianUpdateBookInfo(int book_id, int lib_id, String NewInfo, int newquantity, int command) {
        for (Librarian L : LibrarianList) {
            if (L.GetId() == lib_id) {
                L.ChangeInfo(BooksList, book_id, command, NewInfo, newquantity);
            }
        }
    }

    public static void main(String[] args) {
        // Test run
        new Library();
    }
}