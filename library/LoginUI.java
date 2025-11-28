
package library;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class LoginUI extends javax.swing.JFrame {

    // --- Bảng màu chuyên nghiệp ---
    private static final Color COLOR_HEADER_RED = new Color(176, 0, 0); // Đỏ sẫm
    private static final Color COLOR_ACCENT_RED = new Color(210, 43, 43); // Đỏ nhấn
    private static final Color COLOR_WHITE = Color.WHITE;
    private static final Font FONT_TITLE = new Font("Arial", Font.BOLD, 24);
    private static final Font FONT_LABEL = new Font("Arial", Font.PLAIN, 14);
    private static final Font FONT_BUTTON = new Font("Arial", Font.BOLD, 14);

    // --- Biến Giao diện ---
    private JRadioButton studentRadioButton, clerkRadioButton, librarianRadioButton;
    private ButtonGroup roleGroup;
    private JTextField userIdTextField;

    /**
     * Creates new form LoginUI (Đã viết lại hoàn toàn)
     */
    public LoginUI() {
        // --- 1. Cài đặt cửa sổ chính ---
        setTitle("Hệ thống Quản lý Thư viện - Đăng nhập");
        setSize(450, 420);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Ra giữa màn hình
        setResizable(false);

        // --- 2. Tạo Panel Chính (BorderLayout) ---
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(COLOR_WHITE);

        // --- 3. Panel Header (NORTH) ---
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(COLOR_HEADER_RED);
        headerPanel.setPreferredSize(new Dimension(450, 80));
        JLabel titleLabel = new JLabel("HỆ THỐNG QUẢN LÝ THƯ VIỆN");
        titleLabel.setFont(FONT_TITLE);
        titleLabel.setForeground(COLOR_WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        // Căn giữa JLabel trong Panel
        titleLabel.setVerticalAlignment(SwingConstants.CENTER);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0)); // Căn lề trên

        // --- 4. Panel Đăng nhập (CENTER) ---
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(COLOR_WHITE);
        loginPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST; // Căn lề trái

        // --- Hàng 0: Radio Button ---
        studentRadioButton = new JRadioButton("Student (Sinh viên)");
        clerkRadioButton = new JRadioButton("Clerk (Nhân viên)");
        librarianRadioButton = new JRadioButton("Librarian (Thủ thư)");
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

        // --- Hàng 3: Nhãn "User ID" ---
        JLabel idLabel = new JLabel("User ID:");
        idLabel.setFont(FONT_LABEL);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1; // Reset
        loginPanel.add(idLabel, gbc);

        // --- Hàng 3: Ô nhập "User ID" ---
        userIdTextField = new JTextField(20);
        userIdTextField.setFont(FONT_LABEL);
        gbc.gridx = 1;
        gbc.gridy = 3;
        loginPanel.add(userIdTextField, gbc);

        // --- Hàng 4: Nút "Login" ---
        JButton loginButton = new JButton("ĐĂNG NHẬP");
        // ** SỬA LỖI MỜ CHỮ TẠI ĐÂY **
        styleButton(loginButton);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL; // Kéo dãn nút
        loginPanel.add(loginButton, gbc);

        // --- 5. Gắn các Panel vào cửa sổ ---
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(loginPanel, BorderLayout.CENTER);
        
        // Thêm sự kiện click chuột
        loginButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                LogInButtonMouseClicked(evt);
            }
        });

        // Thêm panel chính vào Frame
        add(mainPanel);
    }
    
    // Hàm style nút (đã thêm)
    private void styleButton(JButton button) {
        button.setBackground(COLOR_ACCENT_RED);
        button.setForeground(COLOR_WHITE);
        button.setFont(FONT_BUTTON);
        button.setFocusPainted(false);
        // ** FIX LỖI MỜ CHỮ **
        button.setOpaque(true); 
        button.setBorderPainted(false);
    }
    
    // Giữ nguyên hàm logic xử lý đăng nhập của bạn
    private void LogInButtonMouseClicked(java.awt.event.MouseEvent evt) {
        String idText = userIdTextField.getText();
        if (idText.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập User ID.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "User ID phải là một con số.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Tải Library object một lần duy nhất
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
            // Đăng nhập thành công
            this.dispose();

            // Mở cửa sổ tương ứng
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
            // Thất bại
            JOptionPane.showMessageDialog(this, "Đăng nhập thất bại. ID hoặc Vai trò không đúng.", "Lỗi Đăng Nhập", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* * BƯỚC QUAN TRỌNG NHẤT: THIẾT LẬP "WINDOWS LOOK AND FEEL"
         * Đây là bí quyết để có giao diện phẳng, "không bo góc"
         */
        try {
            // Dùng "Windows" L&F.
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception ex) {
            // Nếu không phải Windows, dùng "Nimbus"
             try {
                 UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
             } catch (Exception e) {
                java.util.logging.Logger.getLogger(LoginUI.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
             }
        }
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new LoginUI().setVisible(true);
        });
    }

    // Các biến từ file .form cũ (không dùng đến nữa)
    // private javax.swing.ButtonGroup buttonGroup1;
    // ...
}