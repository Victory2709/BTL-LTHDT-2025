package library;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class ClerkMenuUI extends javax.swing.JFrame {

    protected int staffId;
    protected Library LibObj;
    protected String staffName;

    // --- COLORS ---
    protected static final Color COLOR_HEADER_RED = new Color(176, 0, 0);
    protected static final Color COLOR_ACCENT_RED = new Color(210, 43, 43);
    protected static final Color COLOR_PASTEL_RED = new Color(254, 242, 242);
    protected static final Color COLOR_WHITE = Color.WHITE;
    protected static final Font FONT_TITLE = new Font("Arial", Font.BOLD, 24);
    protected static final Font FONT_LABEL = new Font("Arial", Font.PLAIN, 14);
    protected static final Font FONT_BUTTON = new Font("Arial", Font.BOLD, 14);
    
    protected JTabbedPane tabbedPane;
    protected JTextArea logTextArea;
    protected JTable searchResultTable;
    protected DefaultTableModel tableModel;
    protected JLabel titleLabel;

    public ClerkMenuUI(int id, Library LibObj) {
        this.staffId = id;
        this.LibObj = LibObj;
        this.staffName = findStaffName(id, LibObj.ClerkList);
        initUI("Clerk Menu (ID: " + staffId + ")", "Welcome, " + this.staffName);
    }
    
    protected ClerkMenuUI(int id, Library LibObj, String title, String header) {
        this.staffId = id;
        this.LibObj = LibObj;
        initUI(title, header);
    }
    
    protected String findStaffName(int idToFind, ArrayList<? extends Users> staffList) {
        if (staffList == null) return "Unknown Staff";
        for (Users u : staffList) {
            if (u.GetId() == idToFind) return u.GetName();
        }
        return "Unknown (ID: " + idToFind + ")";
    }
    
    private void initUI(String title, String header) {
        setTitle(title);
        setSize(900, 700);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(COLOR_WHITE);
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(COLOR_HEADER_RED);
        headerPanel.setPreferredSize(new Dimension(800, 60));
        
        titleLabel = new JLabel("  " + header);
        titleLabel.setFont(FONT_TITLE);
        titleLabel.setForeground(COLOR_WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        JButton logoutButton = new JButton("Logout");
        styleButton(logoutButton);
        headerPanel.add(logoutButton, BorderLayout.EAST);
        
        // Tabbed Pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(FONT_LABEL);
        tabbedPane.setBackground(COLOR_PASTEL_RED);

        tabbedPane.addTab("Search", createSearchTab());
        tabbedPane.addTab("Transactions", createTransactionTab());
        tabbedPane.addTab("Manage Borrowers", createBorrowerTab());
        
        // Log Area
        logTextArea = new JTextArea(10, 50);
        logTextArea.setEditable(false);
        logTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        logTextArea.setWrapStyleWord(true);
        logTextArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(logTextArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Activity Log"));
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        mainPanel.add(scrollPane, BorderLayout.SOUTH);
        
        logoutButton.addActionListener((e) -> handleLogout());
        
        add(mainPanel);
        log("System Ready.");
    }

    private JPanel createSearchTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(COLOR_WHITE);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlsPanel.setBackground(COLOR_WHITE);
        
        JTextField searchTextField = new JTextField(20);
        searchTextField.setFont(FONT_LABEL);
        
        JButton btnSearchTitle = new JButton("Search Title");
        JButton btnSearchAuthor = new JButton("Search Author");
        JButton btnSearchSubject = new JButton("Search Subject");
        JButton btnViewAll = new JButton("View All Books");
        JButton btnSearchUser = new JButton("View User History");
        
        styleButton(btnSearchTitle);
        styleButton(btnSearchAuthor);
        styleButton(btnSearchSubject);
        styleButton(btnViewAll);
        styleButton(btnSearchUser);
        
        controlsPanel.add(new JLabel("Keyword/ID:"));
        controlsPanel.add(searchTextField);
        controlsPanel.add(btnSearchTitle);
        controlsPanel.add(btnSearchAuthor);
        controlsPanel.add(btnSearchSubject);
        controlsPanel.add(btnViewAll);
        controlsPanel.add(btnSearchUser);

        String[] columnNames = {"Book ID", "Title", "Author", "Subject", "Quantity"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        searchResultTable = new JTable(tableModel);
        searchResultTable.getTableHeader().setBackground(COLOR_PASTEL_RED);
        searchResultTable.getTableHeader().setFont(FONT_LABEL);
        searchResultTable.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(searchResultTable);
        
        btnSearchTitle.addActionListener((e) -> handleSearch(searchTextField.getText(), 1));
        btnSearchAuthor.addActionListener((e) -> handleSearch(searchTextField.getText(), 2));
        btnSearchSubject.addActionListener((e) -> handleSearch(searchTextField.getText(), 3));
        btnSearchUser.addActionListener((e) -> handleViewLoanInfo(searchTextField.getText()));
        btnViewAll.addActionListener((e) -> handleViewAllBooks());
        
        panel.add(controlsPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }
    
    // --- TAB 2: TRANSACTIONS (ĐÃ XÓA PHẦN UPDATE QUANTITY) ---
    private JPanel createTransactionTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = createGBC();
        
        // 1. Checkout
        addTitleRow(panel, gbc, 0, "Checkout Book");
        JTextField coUserId = new JTextField(15);
        JTextField coBookId = new JTextField(15);
        JButton btnCheckout = new JButton("Checkout");
        styleButton(btnCheckout);
        
        addFormRow(panel, gbc, 1, new JLabel("User ID:"), coUserId);
        addFormRow(panel, gbc, 2, new JLabel("Book ID:"), coBookId, btnCheckout);

        // 2. Checkin
        addSeparatorRow(panel, gbc, 3);
        addTitleRow(panel, gbc, 4, "Checkin Book");
        JTextField ciUserId = new JTextField(15);
        JTextField ciBookId = new JTextField(15);
        JTextField ciReturnDate = new JTextField("yyyy/MM/dd", 15);
        JButton btnCheckin = new JButton("Checkin");
        styleButton(btnCheckin);
        
        addFormRow(panel, gbc, 5, new JLabel("User ID:"), ciUserId);
        addFormRow(panel, gbc, 6, new JLabel("Book ID:"), ciBookId);
        addFormRow(panel, gbc, 7, new JLabel("Return Date:"), ciReturnDate, btnCheckin);

        // 3. Fine Payment
        addSeparatorRow(panel, gbc, 8);
        addTitleRow(panel, gbc, 9, "Record Fine Payment");
        JTextField fineUserId = new JTextField(15);
        JButton btnRecordFine = new JButton("Confirm Payment");
        styleButton(btnRecordFine);
        addFormRow(panel, gbc, 10, new JLabel("User ID:"), fineUserId, btnRecordFine);
        
        // --- KHÔNG CÒN PHẦN UPDATE QUANTITY Ở ĐÂY NỮA ---

        // Events
        btnCheckout.addActionListener((e) -> handleCheckout(coUserId.getText(), coBookId.getText()));
        btnCheckin.addActionListener((e) -> handleCheckin(ciUserId.getText(), ciBookId.getText(), ciReturnDate.getText()));
        btnRecordFine.addActionListener((e) -> handleRecordFine(fineUserId.getText()));
        
        return panel;
    }
    
    private JPanel createBorrowerTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = createGBC();

        // Add Borrower
        addTitleRow(panel, gbc, 0, "Add New Borrower");
        JTextField addName = new JTextField(15);
        JTextField addGender = new JTextField(5);
        JTextField addTel = new JTextField(15);
        JTextField addAddress = new JTextField(15);
        JButton btnAddBorrower = new JButton("Add Borrower");
        styleButton(btnAddBorrower);
        
        addFormRow(panel, gbc, 1, new JLabel("Name:"), addName);
        addFormRow(panel, gbc, 2, new JLabel("Gender (M/F):"), addGender);
        addFormRow(panel, gbc, 3, new JLabel("Phone:"), addTel);
        addFormRow(panel, gbc, 4, new JLabel("Address:"), addAddress, btnAddBorrower);

        // Update Borrower
        addSeparatorRow(panel, gbc, 5);
        addTitleRow(panel, gbc, 6, "Update Borrower Info");
        JTextField updateUserId = new JTextField(15);
        JTextField updateInfo = new JTextField(15);
        JButton btnUpdateName = new JButton("Update Name");
        JButton btnUpdateGender = new JButton("Update Gender");
        styleButton(btnUpdateName); styleButton(btnUpdateGender);
        
        addFormRow(panel, gbc, 7, new JLabel("User ID:"), updateUserId);
        addFormRow(panel, gbc, 8, new JLabel("New Info:"), updateInfo);
        
        gbc.gridy = 9;
        gbc.gridx = 1; 
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(btnUpdateName, gbc);
        gbc.gridx = 2; 
        panel.add(btnUpdateGender, gbc);

        // Events
        btnAddBorrower.addActionListener((e) -> handleAddBorrower(addName.getText(), addGender.getText(), addTel.getText(), addAddress.getText()));
        btnUpdateName.addActionListener((e) -> handleUpdateBorrower(updateUserId.getText(), updateInfo.getText(), 1));
        btnUpdateGender.addActionListener((e) -> handleUpdateBorrower(updateUserId.getText(), updateInfo.getText(), 2));

        return panel;
    }
    
    // --- LOGIC ---
    protected void handleSearch(String keyword, int searchType) {
        if (keyword.trim().isEmpty()) { showError("Please enter a keyword."); return; }
        
        ArrayList<Books> selectedBooks = null;
        
        if (searchType == 1) selectedBooks = LibObj.ClerkSearchBookbyTitle(keyword, staffId);
        else if (searchType == 2) selectedBooks = LibObj.ClerkSearchBookbyAuthor(keyword, staffId);
        else if (searchType == 3) selectedBooks = LibObj.ClerkSearchBookbySubject(keyword, staffId);
        
        tableModel.setRowCount(0);
        if (selectedBooks != null && !selectedBooks.isEmpty()) {
            for (Books b : selectedBooks) {
                tableModel.addRow(new Object[]{ b.GetBookId(), b.GetTitle(), b.GetAuthor(), b.GetSubject(), b.GetQuantity() });
            }
            log("Found " + selectedBooks.size() + " books.");
        } else {
            log("No books found.");
        }
    }
    
    protected void handleViewLoanInfo(String userIdStr) {
        try {
            int userId = Integer.parseInt(userIdStr);
            String info = LibObj.CheckLoanofUser(userId, staffId);
            log("--- History for User ID " + userId + " ---\n" + info + "\n--- End History ---");
        } catch (NumberFormatException e) {
            showError("User ID must be a number.");
        }
    }

    protected void handleViewAllBooks() {
        ArrayList<Books> allBooks = Library.BooksList;
        tableModel.setRowCount(0); 
        if (allBooks != null && !allBooks.isEmpty()) {
            for (Books b : allBooks) {
                tableModel.addRow(new Object[]{
                    b.GetBookId(), b.GetTitle(), b.GetAuthor(), b.GetSubject(), b.GetQuantity()
                });
            }
            log("Displaying all " + allBooks.size() + " books.");
        } else {
            log("No books in library.");
        }
    }
    
    protected void handleCheckout(String userIdStr, String bookIdStr) {
        try {
            int userId = Integer.parseInt(userIdStr);
            int bookId = Integer.parseInt(bookIdStr);
            LibObj.ClerkCheckOutItem(bookId, userId, staffId);
            log("Success: Checkout Book ID " + bookId + " for User " + userId);
        } catch (Exception e) {
            showError("ID must be a number. Error: " + e.getMessage());
        }
    }
    
    protected void handleCheckin(String userIdStr, String bookIdStr, String returnDate) {
        try {
            int userId = Integer.parseInt(userIdStr);
            int bookId = Integer.parseInt(bookIdStr);
            if (returnDate.isEmpty() || returnDate.equals("yyyy/MM/dd")) {
                showError("Please enter return date.");
                return;
            }
            LibObj.ClerkCheckInItem(returnDate, bookId, userId, staffId);
            log("Success: Checkin Book ID " + bookId + " from User " + userId);
        } catch (Exception e) {
            showError("ID must be a number or Invalid Date. Error: " + e.getMessage());
        }
    }
    
    protected void handleRecordFine(String userIdStr) {
        try {
            int userId = Integer.parseInt(userIdStr);
            LibObj.ClerkRecordFine(userId, staffId);
            log("Success: Fine recorded/paid for User " + userId);
        } catch (Exception e) {
            showError("User ID must be a number.");
        }
    }
    
    protected void handleAddBorrower(String name, String genderStr, String tel, String address) {
        if (name.isEmpty() || genderStr.isEmpty() || tel.isEmpty() || address.isEmpty()) {
            showError("Please fill all fields.");
            return;
        }
        boolean result = LibObj.AddNewBorrower(name, genderStr.charAt(0), tel, address, staffId);
        if (result) {
            log("Success: New borrower added: " + name);
        } else {
            showError("Failed to add borrower.");
        }
    }

    protected void handleUpdateBorrower(String userIdStr, String newInfo, int infoType) {
        try {
            int userId = Integer.parseInt(userIdStr);
            if (newInfo.isEmpty()) {
                showError("Please enter new info.");
                return;
            }
            boolean result = LibObj.ClerkUpdatingInfo(newInfo, infoType, staffId, userId);
            if (result) {
                log("Success: Updated info for User ID " + userId);
            } else {
                showError("Update failed.");
            }
        } catch (Exception e) {
            showError("User ID must be a number.");
        }
    }

    protected void handleLogout() {
        this.dispose();
        new LoginUI().setVisible(true);
    }
    
    protected void log(String message) {
        logTextArea.append(">> " + message + "\n");
        logTextArea.setCaretPosition(logTextArea.getDocument().getLength()); 
    }
    
    protected void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
        log("ERROR: " + message);
    }
    
    protected void styleButton(JButton button) {
        button.setBackground(COLOR_ACCENT_RED);
        button.setForeground(COLOR_WHITE);
        button.setFont(FONT_BUTTON);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorderPainted(false);
    }
    
    protected GridBagConstraints createGBC() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); 
        gbc.anchor = GridBagConstraints.WEST; 
        return gbc;
    }
    
    protected void addTitleRow(JPanel panel, GridBagConstraints gbc, int y, String title) {
        gbc.gridy = y;
        gbc.gridx = 0;
        gbc.gridwidth = 3; 
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(COLOR_ACCENT_RED);
        panel.add(titleLabel, gbc);
        gbc.gridwidth = 1; 
    }
    
    protected void addSeparatorRow(JPanel panel, GridBagConstraints gbc, int y) {
        gbc.gridy = y;
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 0, 15, 0); 
        JSeparator sep = new JSeparator();
        sep.setForeground(COLOR_PASTEL_RED);
        sep.setBackground(COLOR_PASTEL_RED);
        panel.add(sep, gbc);
        gbc.gridwidth = 1; 
        gbc.insets = new Insets(5, 5, 5, 5); 
    }

    protected void addFormRow(JPanel panel, GridBagConstraints gbc, int y, JLabel label, JComponent component) {
        gbc.gridy = y;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST; 
        panel.add(label, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2; 
        gbc.fill = GridBagConstraints.HORIZONTAL; 
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(component, gbc);
        gbc.gridwidth = 1; 
    }
    
    protected void addFormRow(JPanel panel, GridBagConstraints gbc, int y, JLabel label, JComponent component, JButton button) {
        gbc.gridy = y;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(label, gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(component, gbc);
        gbc.gridx = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(button, gbc);
    }
    
    
}