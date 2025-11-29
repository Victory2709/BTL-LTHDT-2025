package library;

import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class Staff extends Users {

    Staff(int user_id, String user_name, char gender) {
        super(user_id, user_name, gender);
    }

    public boolean CheckOutItem(int book_id, Users current_borrower, ArrayList<Books> BooksList, Loan Current_Loan, ArrayList<Loan> LoanList) {
        if (current_borrower.GetFineStatus()) {
            System.out.println("User has outstanding fines. Cannot borrow more books.");
            return false;
        }

        for (Books b : BooksList) {
            if (b.GetBookId() == book_id) {
                // Check availability
                if (!b.ChekcAvailability(book_id)) {
                    System.out.println("Book is out of stock.");
                    return false;
                }

                // Decrease quantity
                RenewItem(book_id, BooksList, 2); 
                
                Current_Loan.SetaBook(b);
                Current_Loan.SetaBorrower(current_borrower);
                current_borrower.AddLoanInfo(Current_Loan);
                LoanList.add(Current_Loan);
                
                return new dbConnectivity().AddNewLoan(Current_Loan);
            }
        }
        System.out.println("Book ID not found.");
        return false;
    }

    public boolean CheckInItem(String ret_date, int book_id, Users current_borrower, ArrayList<Books> BooksList, ArrayList<Loan> LoanList) {
        for (int counter = LoanList.size() - 1; counter >= 0; counter--) {
            Loan L = LoanList.get(counter);

            if ((L.GetaBookId() == book_id) && (L.GetaBorrowerId() == current_borrower.GetId()) && !L.GetStatus()) {
                
                L.SetReturnStatus(true);
                
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
                try {
                    Date date = formatter.parse(ret_date);
                    L.SetReturnedDate(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                double user_fine = L.CalculateFine();
                if (user_fine > 0.0) {
                    L.SetFineStatus("Fined");
                    current_borrower.SetFineStatus(true);
                    current_borrower.SetFineAmount(user_fine);
                } else {
                    L.SetFineStatus("Paid"); // No fine implies clear
                }

                // Increase quantity back
                RenewItem(book_id, BooksList, 1);
                current_borrower.UpdateLoanInfo(L, book_id);
                return true;
            }
        }
        return false;
    }

    @Override
    public String ViewInformation(ArrayList<Loan> LoanList, int user_id) {
        // Staff usually views their own profile info here
        return super.PrintInformation(); 
    }

    public boolean AddaBorrower(ArrayList<Users> BorrowerList, String borrower_name, char borrower_gender, String telephone_number, String address) {
        int new_id = 1;
        if (!BorrowerList.isEmpty()) {
            Users LastBorrower = BorrowerList.get(BorrowerList.size() - 1);
            new_id = LastBorrower.GetId() + 1;
        }

        Borrower NewBorrower = new Borrower(new_id, borrower_name, borrower_gender, telephone_number, address, false, 0.0);
        BorrowerList.add(NewBorrower);

        return new dbConnectivity().AddBorrower(new_id, borrower_name, borrower_gender, address, telephone_number);
    }

    public boolean UpdatePerosnalInformation(ArrayList<Users> BorrowerList, String Info, int command, int user_id) {
        for (Users B : BorrowerList) {
            if (B.GetId() == user_id) {
                if (command == 1) {
                    B.SetName(Info);
                    return true;
                } else if (command == 2) {
                    B.SetGender(Info.charAt(0));
                    return true;
                }
            }
        }
        return false;
    }

    public void RenewItem(int book_id, ArrayList<Books> BooksList, int type) {
        for (Books b : BooksList) {
            if (b.GetBookId() == book_id) {
                if (type == 1) {
                    b.IncreaseQuantity();
                } else {
                    b.DecreaseQuantity();
                }
                break;
            }
        }
    }

    void PayFine(int user_id, ArrayList<Users> BorrowerLists, ArrayList<Loan> LoanList) {
        for (int counter = LoanList.size() - 1; counter >= 0; counter--) {
            Loan L = LoanList.get(counter);
            String F = L.GetFineStatus();
            
            if (L.GetaBorrowerId() == user_id && "Fined".equals(F)) {
                L.SetFineStatus("Paid");
                for (Users b : BorrowerLists) {
                    int book_id = L.GetaBookId();
                    if (b.GetId() == user_id) {
                        b.SetFineAmount(0);
                        b.SetFineStatus(false);
                        b.UpdateLoanInfo(L, book_id);
                        break;
                    }
                }
            }
        }
    }
}