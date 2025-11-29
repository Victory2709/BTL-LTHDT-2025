package library;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginUI extends javax.swing.JFrame {

    private static final Color COLOR_HEADER_RED = new Color(176, 0, 0);
    private static final Color COLOR_ACCENT_RED = new Color(210, 43, 43);
    private static final Color COLOR_WHITE = Color.WHITE;
    private static final Font FONT_TITLE = new Font("Arial", Font.BOLD, 24);
    private static final Font FONT_LABEL = new Font("Arial", Font.PLAIN, 14);
    private static final Font FONT_BUTTON = new Font("Arial", Font.BOLD, 14);

    private JRadioButton studentRadioButton, clerkRadioButton, librarianRadioButton;
    private ButtonGroup roleGroup;
    private JTextField userIdTextField;

    public LoginUI() {
        setTitle("Library Management System - Login");
        setSize(450, 420);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(COLOR_WHITE);

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(COLOR_HEADER_RED);
        headerPanel.setPreferredSize(new Dimension(450, 80));
        JLabel titleLabel = new JLabel("LIBRARY MANAGEMENT SYSTEM");
        titleLabel.setFont(FONT_TITLE);
        titleLabel.setForeground(COLOR_WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        titleLabel.setVerticalAlignment(SwingConstants.CENTER);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(COLOR_WHITE);
        loginPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        studentRadioButton = new JRadioButton("Student");
        clerkRadioButton = new JRadioButton("Clerk");
        librarianRadioButton = new JRadioButton("Librarian");
        studentRadioButton.setBackground(COLOR_WHITE);
        clerkRadioButton.setBackground(COLOR_WHITE);
        librarianRadioButton.setBackground(COLOR_WHITE);
        studentRadioButton.setSelected(true);
        roleGroup = new ButtonGroup();
        roleGroup.add(studentRadioButton);
        roleGroup.add(clerkRadioButton);
        roleGroup.add(librarianRadioButton);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        loginPanel.add(studentRadioButton, gbc);
        gbc.gridy = 1;
        loginPanel.add(clerkRadioButton, gbc);
        gbc.gridy = 2;
        loginPanel.add(librarianRadioButton, gbc);

        JLabel idLabel = new JLabel("User ID:");
        idLabel.setFont(FONT_LABEL);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1; 
        loginPanel.add(idLabel, gbc);

        userIdTextField = new JTextField(20);
        userIdTextField.setFont(FONT_LABEL);
        gbc.gridx = 1;
        gbc.gridy = 3;
        loginPanel.add(userIdTextField, gbc);

        JButton loginButton = new JButton("LOGIN");
        styleButton(loginButton);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL; 
        loginPanel.add(loginButton, gbc);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(loginPanel, BorderLayout.CENTER);
        
        loginButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                LogInButtonMouseClicked(evt);
            }
        });

        add(mainPanel);
    }
    
    private void styleButton(JButton button) {
        button.setBackground(COLOR_ACCENT_RED);
        button.setForeground(COLOR_WHITE);
        button.setFont(FONT_BUTTON);
        button.setFocusPainted(false);
        button.setOpaque(true); 
        button.setBorderPainted(false);
    }
    
    private void LogInButtonMouseClicked(java.awt.event.MouseEvent evt) {
        String idText = userIdTextField.getText();
        if (idText.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter User ID.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "User ID must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Library LibObj = new Library();
        boolean flag = false;
        String role = "";

        if (studentRadioButton.isSelected()) {
            flag = LibObj.IsBorrowerPresent(id);
            role = "Student";
        } else if (clerkRadioButton.isSelected()) {
            flag = LibObj.IsClerkPresent(id);
            role = "Clerk";
        } else if (librarianRadioButton.isSelected()) {
            flag = LibObj.IsLibrarianPresent(id);
            role = "Librarian";
        }

        if (flag) {
            this.dispose();

            if (role.equals("Student")) {
                java.awt.EventQueue.invokeLater(() -> {
                    new StudentMenuUI(id).setVisible(true);
                });
            } else if (role.equals("Clerk")) {
                java.awt.EventQueue.invokeLater(() -> {
                    new ClerkMenuUI(id, LibObj).setVisible(true);
                });
            } else if (role.equals("Librarian")) {
                java.awt.EventQueue.invokeLater(() -> {
                    new LibrarianMenu(id, LibObj).setVisible(true);
                });
            }
        } else {
            JOptionPane.showMessageDialog(this, "Login Failed. Invalid ID or Role.", "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception ex) {
             try {
                 UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
             } catch (Exception e) {
                e.printStackTrace();
             }
        }
        
        java.awt.EventQueue.invokeLater(() -> {
            new LoginUI().setVisible(true);
        });
    }
}