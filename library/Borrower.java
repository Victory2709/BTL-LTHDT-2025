package library;

import java.util.*;

public class Borrower extends Users {

    private String address;
    private String telephone;
    private boolean fine_defaulter;
    private double fine;
    private ArrayList<Loan> BookLoans;

    Borrower() {
        super();
        fine_defaulter = false;
        BookLoans = new ArrayList<>();
        fine = 0.0;
        address = " ";
        telephone = " ";
    }

    Borrower(int user_id, String user_name, char gender, String telephone, String address, boolean fine_defaulter, double fine) {
        super(user_id, user_name, gender);
        this.fine_defaulter = fine_defaulter;
        this.fine = fine;
        this.address = address;
        this.telephone = telephone;
        BookLoans = new ArrayList<>();
    }

    @Override
    public boolean GetFineStatus() { return new dbConnectivity().GetFineStatus(this.GetId()); }
    public double GetFineAmout() { return new dbConnectivity().GetFineAmount(this.GetId()); }
    public String GetAddress() { return this.address; }
    public String GetTelephone() { return this.telephone; }

    @Override
    public boolean SetFineStatus(boolean fine_defaulter) {
        this.fine_defaulter = fine_defaulter;
        return new dbConnectivity().SetFineStatus(this.GetId(), fine_defaulter);
    }

    public boolean SetTelephone(String Telephone) {
        this.telephone = Telephone;
        return new dbConnectivity().SetTelephone(this.GetId(), this.telephone);
    }

    public boolean SetAddress(String Address) {
        this.address = Address;
        return new dbConnectivity().SetAddress(this.GetId(), this.address);
    }
  
    @Override
    public void SetFineAmount(double user_fine) {
        this.fine = user_fine;
        new dbConnectivity().SetFineAmount(this.GetId(), user_fine);
    }

    public void SetName(String name) {
        super.SetName(name);
        new dbConnectivity().SetName(this.GetId(), name);
    }
    
    public void SetGender(char g) {
        super.SetGender(g);
        new dbConnectivity().SetGender(this.GetId(), g);
    }
    
    @Override
    public boolean AddLoanInfo(Loan LoanInfo) {
        BookLoans.add(LoanInfo);
        return true;
    }

    public void AllLoansofUser(ArrayList<Loan> LoansofUser) {
        this.BookLoans = new dbConnectivity().LoadLoanListofSpecificUser(this.GetId());
    }

     @Override
    public void UpdateLoanInfo(Loan Update, int book_id) {
      for (int counter=BookLoans.size()-1 ; counter >=0; counter--){
             Loan L = BookLoans.get(counter);
            if ((L.GetaBookId() == book_id)) {
                L.SetLoan(Update);
                break;
            }
        }
    }

    @Override
    public String ViewInformation(ArrayList<Loan> LoanList, int user_id) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("========================================\n");
        sb.append(" PERSONAL INFORMATION\n");
        sb.append("========================================\n");
        sb.append(super.PrintInformation());
        sb.append("  Address: \t").append(address).append("\n");
        sb.append("  Telephone: \t").append(telephone).append("\n");
        sb.append("  Fine Due: \t").append(fine).append(" VND\n\n");

        sb.append("========================================\n");
        sb.append(" LOAN HISTORY (" + BookLoans.size() + " records)\n");
        sb.append("========================================\n");

        if (!BookLoans.isEmpty()) {
            for (Loan L : BookLoans) {
                sb.append(L.PrintLoanInfo());
                sb.append("----------------------------------------\n");
            }
        } else {
            sb.append("  (No loan history found)\n");
        }
        
        return sb.toString();
    }
}