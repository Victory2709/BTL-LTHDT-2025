package library;

import java.util.*;
import java.io.*;

abstract class Users {

    private int user_id;
    private String user_name;
    private char gender;

    Users() {
        this.user_id = -1;
        this.user_name = " ";
        this.gender = '-';
    }

    Users(int user_id, String user_name, char gender) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.gender = gender;
    }

    public ArrayList<Books> SearchBookbyTitle(String title) {
        ArrayList<Books> BooksList = new ArrayList<>();
        dbConnectivity db = new dbConnectivity();
        BooksList = db.SearchBookbyTitle(title);
        return BooksList;
    }

    public ArrayList<Books> SearchBookbySubject(String subject) {
        ArrayList<Books> BooksList = new ArrayList<>();
        dbConnectivity db = new dbConnectivity();
        BooksList = db.SearchBookbySubject(subject);
        return BooksList;
    }

    public ArrayList<Books> SearchBookbyAuthor(String author) {
        ArrayList<Books> BooksList = new ArrayList<>();
        dbConnectivity db = new dbConnectivity();
        BooksList = db.SearchBookbyAuthor(author);
        return BooksList;
    }

    public int GetId() {
        return this.user_id;
    }

    public String GetName() {
        return this.user_name;
    }

    public char GetGender() {
        return this.gender;
    }

    public void SetId(int id) {
        this.user_id = id;
    }

    public void SetName(String name) {
        this.user_name = name;
    }

    public void SetGender(char g) {
        this.gender = g;
    }

    public String PrintInformation() {
        String Resultant = "  Id: \t" + this.user_id + "\n"
                         + "  Name: \t" + this.user_name + "\n"
                         + "  Gender: \t" + this.gender + "\n";
        return Resultant;
    }
    
     void SetFineAmount(double fine)           
     {
         dbConnectivity db= new dbConnectivity ();
         db.SetFineAmount(user_id, fine);
     }
     
      boolean SetFineStatus(boolean status){
           dbConnectivity db= new dbConnectivity ();
          boolean result= db.SetFineStatus(user_id, status);
          return result;
      }
      
       void UpdateLoanInfo(Loan L, int i ) {}
       boolean GetFineStatus()
       {
           return true;
       }
       boolean AddLoanInfo(Loan Current_Loan)
       {
           return true;
       }

    abstract String ViewInformation(ArrayList<Loan> LoanLoanList, int user_id);
}