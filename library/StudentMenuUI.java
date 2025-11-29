package library;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class StudentMenuUI extends javax.swing.JFrame {

    int studentId;
    String studentName;

    private static final Color COLOR_HEADER_RED = new Color(176, 0, 0);
    private static final Color COLOR_ACCENT_RED = new Color(210, 43, 43);
    private static final Color COLOR_PASTEL_RED = new Color(254, 242, 242);
    private static final Color COLOR_WHITE = Color.WHITE;
    private static final Font FONT_TITLE = new Font("Arial", Font.BOLD, 24);
    private static final Font FONT_LABEL = new Font("Arial", Font.PLAIN, 14);
    private static final Font FONT_BUTTON = new Font("Arial", Font.BOLD, 14);

    private JTabbedPane tabbedPane;
    private JTextArea historyTextArea;
    private JTable searchResultTable;
    private DefaultTableModel tableModel;

    public StudentMenuUI(int id) {
        this.studentId = id;
        Users currentUser = findUser(id);
        this.studentName = (currentUser != null) ? currentUser.GetName() : "Unknown";

        setTitle("Student Menu (ID: " + studentId + ")");
        setSize(800, 600);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(COLOR_WHITE);
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(COLOR_HEADER_RED);
        headerPanel.setPreferredSize(new Dimension(800, 60));
        
        JLabel titleLabel = new JLabel("  Welcome, " + this.studentName);
        titleLabel.setFont(FONT_TITLE);
        titleLabel.setForeground(COLOR_WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        JButton logoutButton = new JButton("Logout");
        styleButton(logoutButton);
        headerPanel.add(logoutButton, BorderLayout.EAST);
        
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(FONT_LABEL);
        tabbedPane.setBackground(COLOR_PASTEL_RED);

        JPanel searchTab = createSearchTab();
        JPanel historyTab = createHistoryTab();
        
        tabbedPane.addTab("Search Books", searchTab);
        tabbedPane.addTab("Loan History", historyTab);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        logoutButton.addActionListener((e) -> handleLogout());
        
        add(mainPanel);
    }
    
    private JPanel createSearchTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(COLOR_WHITE);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlsPanel.setBackground(COLOR_WHITE);
        
        JTextField searchTextField = new JTextField(20);
        searchTextField.setFont(FONT_LABEL);
        
        JButton btnSearchTitle = new JButton("Search by Title");
        JButton btnSearchAuthor = new JButton("Search by Author");
        JButton btnSearchSubject = new JButton("Search by Subject");
        JButton btnViewAll = new JButton("View All Books");
        
        styleButton(btnSearchTitle);
        styleButton(btnSearchAuthor);
        styleButton(btnSearchSubject);
        styleButton(btnViewAll);
        
        controlsPanel.add(new JLabel("Keyword:"));
        controlsPanel.add(searchTextField);
        controlsPanel.add(btnSearchTitle);
        controlsPanel.add(btnSearchAuthor);
        controlsPanel.add(btnSearchSubject);
        controlsPanel.add(btnViewAll);

        String[] columnNames = {"Book ID", "Title", "Author", "Subject", "Quantity"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        searchResultTable = new JTable(tableModel);
        searchResultTable.getTableHeader().setBackground(COLOR_PASTEL_RED);
        searchResultTable.getTableHeader().setFont(FONT_LABEL);
        searchResultTable.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(searchResultTable);
        
        btnSearchTitle.addActionListener((e) -> handleSearch(searchTextField.getText(), 1));
        btnSearchAuthor.addActionListener((e) -> handleSearch(searchTextField.getText(), 2));
        btnSearchSubject.addActionListener((e) -> handleSearch(searchTextField.getText(), 3));
        btnViewAll.addActionListener((e) -> handleViewAllBooks());
        
        panel.add(controlsPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createHistoryTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(COLOR_WHITE);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JButton refreshButton = new JButton("Refresh Loan History");
        styleButton(refreshButton);
        panel.add(refreshButton, BorderLayout.NORTH);
        
        historyTextArea = new JTextArea();
        historyTextArea.setEditable(false);
        historyTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        historyTextArea.setWrapStyleWord(true);
        historyTextArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(historyTextArea);
        
        refreshButton.addActionListener((e) -> handleViewLoanInfo());
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        handleViewLoanInfo();
        
        return panel;
    }

    private void styleButton(JButton button) {
        button.setBackground(COLOR_ACCENT_RED);
        button.setForeground(COLOR_WHITE);
        button.setFont(FONT_BUTTON);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorderPainted(false);
    }
    
    private void handleSearch(String keyword, int searchType) {
        if (keyword == null || keyword.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a search keyword.");
            return;
        }
        
        Users currentUser = findUser(this.studentId);
        if (currentUser == null) return;
        
        ArrayList<Books> selectedBooks = null;
        String searchTitle = "";
        
        if (searchType == 1) { 
            selectedBooks = currentUser.SearchBookbyTitle(keyword);
            searchTitle = "title \"" + keyword + "\"";
        } else if (searchType == 2) {
            selectedBooks = currentUser.SearchBookbyAuthor(keyword);
            searchTitle = "author \"" + keyword + "\"";
        } else if (searchType == 3) {
            selectedBooks = currentUser.SearchBookbySubject(keyword);
            searchTitle = "subject \"" + keyword + "\"";
        }
        
        tableModel.setRowCount(0);

        if (selectedBooks != null && !selectedBooks.isEmpty()) {
            for (Books b : selectedBooks) {
                tableModel.addRow(new Object[]{
                    b.GetBookId(),
                    b.GetTitle(),
                    b.GetAuthor(),
                    b.GetSubject(),
                    b.GetQuantity()
                });
            }
        } else {
            JOptionPane.showMessageDialog(this, "No books found matching " + searchTitle);
        }
    }

    private void handleViewAllBooks() {
        ArrayList<Books> allBooks = Library.BooksList;
        tableModel.setRowCount(0);

        if (allBooks != null && !allBooks.isEmpty()) {
            for (Books b : allBooks) {
                tableModel.addRow(new Object[]{
                    b.GetBookId(),
                    b.GetTitle(),
                    b.GetAuthor(),
                    b.GetSubject(),
                    b.GetQuantity()
                });
            }
        } else {
            JOptionPane.showMessageDialog(this, "Library is currently empty.");
        }
    }
    
    private void handleViewLoanInfo() {
        if (historyTextArea == null) return;
        Users currentUser = findUser(this.studentId);
        if (currentUser == null) return;

        String printingMessage = currentUser.ViewInformation(Library.LoanList, studentId);
        historyTextArea.setText("Personal Information and Loan History:\n\n" + printingMessage);
        historyTextArea.setCaretPosition(0); 
    }
    
    private void handleLogout() {
        this.dispose();
        new LoginUI().setVisible(true);
    }

    private Users findUser(int idToFind) {
        if (Library.UsersList == null) {
            showError("Error: User list not loaded.");
            return null;
        }
        for (Users u : Library.UsersList) {
            if (u.GetId() == idToFind) {
                return u;
            }
        }
        showError("User information not found for ID: " + idToFind);
        return null; 
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}