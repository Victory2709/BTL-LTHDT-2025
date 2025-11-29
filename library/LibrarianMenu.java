package library;

import java.awt.*;
import javax.swing.*;

public class LibrarianMenu extends ClerkMenuUI {

    public LibrarianMenu(int id, Library LibObj) {
        super(id, LibObj, "Librarian Menu (ID: " + id + ")", "Loading...");

        String name = findStaffName(id, LibObj.LibrarianList);
        this.staffName = name; 
        if (this.titleLabel != null) {
            this.titleLabel.setText("  Welcome, Librarian " + name);
        }
        
        tabbedPane.addTab("Manage Books (Admin)", createBookMgmtTab());
    }

    @Override
    protected void handleAddBorrower(String name, String genderStr, String tel, String address) {
        if (name.isEmpty() || genderStr.isEmpty() || tel.isEmpty() || address.isEmpty()) {
            showError("Please fill in all fields.");
            return;
        }
        boolean result = LibObj.AddNewBorrowerLibrarian(name, genderStr.charAt(0), tel, address, staffId);
        
        if (result) {
            log("Success: Added new borrower: " + name);
        } else {
            showError("Failed to add borrower.");
        }
    }

    @Override
    protected void handleUpdateBorrower(String userIdStr, String newInfo, int infoType) {
        try {
            int userId = Integer.parseInt(userIdStr);
            if (newInfo.isEmpty()) {
                showError("Please enter new information.");
                return;
            }
            boolean result = LibObj.LibrarianUpdatingInfo(newInfo, infoType, staffId, userId);
            
            if (result) {
                log("Success: Updated info for User ID " + userId + ".");
            } else {
                showError("Update failed.");
            }
        } catch (Exception e) {
            showError("User ID must be a number. Error: " + e.getMessage());
        }
    }

    private JPanel createBookMgmtTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = createGBC();

        addTitleRow(panel, gbc, 0, "Add New Book");
        JTextField addTitle = new JTextField(15);
        JTextField addAuthor = new JTextField(15);
        JTextField addSubject = new JTextField(15);
        JTextField addQty = new JTextField(5);
        JButton btnAddBook = new JButton("Add Book");
        styleButton(btnAddBook);
        
        addFormRow(panel, gbc, 1, new JLabel("Title:"), addTitle);
        addFormRow(panel, gbc, 2, new JLabel("Author:"), addAuthor);
        addFormRow(panel, gbc, 3, new JLabel("Subject:"), addSubject);
        addFormRow(panel, gbc, 4, new JLabel("Quantity:"), addQty, btnAddBook);

        addSeparatorRow(panel, gbc, 5);
        addTitleRow(panel, gbc, 6, "Delete Book");
        JTextField deleteBookId = new JTextField(15);
        JButton btnDeleteBook = new JButton("Delete Book");
        styleButton(btnDeleteBook);
        addFormRow(panel, gbc, 7, new JLabel("Book ID:"), deleteBookId, btnDeleteBook);
        
        addSeparatorRow(panel, gbc, 8);
        addTitleRow(panel, gbc, 9, "Update Book Info");
        JTextField updateBookId = new JTextField(15);
        JTextField updateBookInfo = new JTextField(15);
        JComboBox<String> updateType = new JComboBox<>(new String[]{"Title", "Author", "Subject", "Quantity"});
        JButton btnUpdateBook = new JButton("Update Book");
        styleButton(btnUpdateBook);
        
        addFormRow(panel, gbc, 10, new JLabel("Book ID:"), updateBookId);
        addFormRow(panel, gbc, 11, new JLabel("Update Field:"), updateType);
        addFormRow(panel, gbc, 12, new JLabel("New Info:"), updateBookInfo, btnUpdateBook);

        btnAddBook.addActionListener((e) -> handleAddBook(addTitle.getText(), addAuthor.getText(), addSubject.getText(), addQty.getText()));
        btnDeleteBook.addActionListener((e) -> handleDeleteBook(deleteBookId.getText()));
        btnUpdateBook.addActionListener((e) -> handleUpdateBook(updateBookId.getText(), updateBookInfo.getText(), updateType.getSelectedIndex()));
        
        return panel;
    }
    
    private void handleAddBook(String title, String author, String subject, String qtyStr) {
        try {
            int quantity = Integer.parseInt(qtyStr);
            LibObj.LibrarianAddNewBook(author, title, subject, quantity, staffId);
            log("Success: Added new book: " + title);
        } catch (Exception e) {
            showError("Quantity must be a number. Error: " + e.getMessage());
        }
    }
    
    private void handleDeleteBook(String bookIdStr) {
        try {
            int bookId = Integer.parseInt(bookIdStr);
            boolean result = LibObj.LibrarianDeleteBook(bookId, staffId);
            if (result) {
                log("Success: Deleted Book ID " + bookId + ".");
            } else {
                showError("Delete failed. (Book might be currently loaned out).");
            }
        } catch (Exception e) {
            showError("Book ID must be a number. Error: " + e.getMessage());
        }
    }
    
    private void handleUpdateBook(String bookIdStr, String newInfo, int typeIndex) {
        try {
            int bookId = Integer.parseInt(bookIdStr);
            int command = typeIndex + 1; 
            int newQuantity = 0;

            if (command == 4) { 
                newQuantity = Integer.parseInt(newInfo);
            }
            
            LibObj.LibrarianUpdateBookInfo(bookId, staffId, newInfo, newQuantity, command);
            log("Success: Updated Book ID " + bookId + ".");
        } catch (Exception e) {
            showError("ID/Quantity must be a number. Error: " + e.getMessage());
        }
    }
}