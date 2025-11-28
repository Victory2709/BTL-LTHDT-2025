
package library;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class StudentMenuUI extends javax.swing.JFrame {

    int studentId; // ID của Student đang đăng nhập
    String studentName; // Tên của student

    // --- Bảng màu ---
    private static final Color COLOR_HEADER_RED = new Color(176, 0, 0); // Đỏ sẫm
    private static final Color COLOR_ACCENT_RED = new Color(210, 43, 43); // Đỏ nhấn
    private static final Color COLOR_PASTEL_RED = new Color(254, 242, 242); // Đỏ pastel
    private static final Color COLOR_WHITE = Color.WHITE;
    private static final Font FONT_TITLE = new Font("Arial", Font.BOLD, 24);
    private static final Font FONT_LABEL = new Font("Arial", Font.PLAIN, 14);
    private static final Font FONT_BUTTON = new Font("Arial", Font.BOLD, 14);

    // --- Biến Giao diện ---
    private JTabbedPane tabbedPane;
    private JTextArea historyTextArea; // Hiển thị lịch sử mượn
    private JTable searchResultTable; // Hiển thị kết quả tìm sách
    private DefaultTableModel tableModel;

    /**
     * Creates new form StudentMenuUI (Đã viết lại)
     */
    public StudentMenuUI(int id) {
        this.studentId = id;
        // *** TÌM TÊN SINH VIÊN ***
        Users currentUser = findUser(id);
        this.studentName = (currentUser != null) ? currentUser.GetName() : "Unknown";

        // --- 1. Cài đặt cửa sổ chính ---
        setTitle("Student Menu (ID: " + studentId + ")");
        setSize(800, 600);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // --- 2. Tạo Panel Chính (BorderLayout) ---
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(COLOR_WHITE);
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // --- 3. Header (NORTH) (*** ĐÃ SỬA DÒNG NÀY ***) ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(COLOR_HEADER_RED);
        headerPanel.setPreferredSize(new Dimension(800, 60));
        
        // *** SỬA TIÊU ĐỀ CHÀO MỪNG BẰNG TÊN ***
        JLabel titleLabel = new JLabel("  Chào mừng, " + this.studentName);
        titleLabel.setFont(FONT_TITLE);
        titleLabel.setForeground(COLOR_WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        JButton logoutButton = new JButton("Đăng xuất");
        // ** SỬA LỖI MỜ CHỮ TẠI ĐÂY **
        styleButton(logoutButton);
        headerPanel.add(logoutButton, BorderLayout.EAST);
        
        // --- 4. Giao diện TAB (CENTER) ---
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(FONT_LABEL);
        tabbedPane.setBackground(COLOR_PASTEL_RED);

        // Tạo 2 tab
        JPanel searchTab = createSearchTab();
        JPanel historyTab = createHistoryTab();
        
        tabbedPane.addTab("Tìm kiếm Sách", searchTab);
        tabbedPane.addTab("Lịch sử Mượn sách", historyTab);
        
        // --- 5. Gắn vào cửa sổ ---
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        // Thêm sự kiện
        logoutButton.addActionListener((e) -> handleLogout());
        
        add(mainPanel);
    }
    
    // Tạo giao diện cho Tab 1: Tìm Sách
    private JPanel createSearchTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(COLOR_WHITE);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Panel controls (NORTH)
        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlsPanel.setBackground(COLOR_WHITE);
        
        JTextField searchTextField = new JTextField(30);
        searchTextField.setFont(FONT_LABEL);
        
        JButton btnSearchTitle = new JButton("Tìm theo Tựa đề");
        JButton btnSearchAuthor = new JButton("Tìm theo Tác giả");
        JButton btnSearchSubject = new JButton("Tìm theo Chủ đề");
        
        // Style nút
        styleButton(btnSearchTitle);
        styleButton(btnSearchAuthor);
        styleButton(btnSearchSubject);
        
        controlsPanel.add(new JLabel("Nhập từ khóa:"));
        controlsPanel.add(searchTextField);
        controlsPanel.add(btnSearchTitle);
        controlsPanel.add(btnSearchAuthor);
        controlsPanel.add(btnSearchSubject);

        // Bảng kết quả (CENTER)
        String[] columnNames = {"Book ID", "Tựa đề", "Tác giả", "Chủ đề", "Số lượng"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho sửa
            }
        };
        searchResultTable = new JTable(tableModel);
        searchResultTable.getTableHeader().setBackground(COLOR_PASTEL_RED); // Nền header
        searchResultTable.getTableHeader().setFont(FONT_LABEL);
        searchResultTable.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(searchResultTable);
        
        // Thêm sự kiện cho các nút
        btnSearchTitle.addActionListener((e) -> handleSearch(searchTextField.getText(), 1));
        btnSearchAuthor.addActionListener((e) -> handleSearch(searchTextField.getText(), 2));
        btnSearchSubject.addActionListener((e) -> handleSearch(searchTextField.getText(), 3));
        
        panel.add(controlsPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }
    
    // Tạo giao diện cho Tab 2: Lịch sử Mượn
    private JPanel createHistoryTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(COLOR_WHITE);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JButton refreshButton = new JButton("Tải/Làm mới Lịch sử Mượn sách");
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
        
        // Tải thông tin lần đầu
        handleViewLoanInfo();
        
        return panel;
    }

    // --- CÁC HÀM XỬ LÝ LOGIC ---

    // Hàm style nút cho đẹp (đã sửa)
    private void styleButton(JButton button) {
        button.setBackground(COLOR_ACCENT_RED);
        button.setForeground(COLOR_WHITE);
        button.setFont(FONT_BUTTON);
        button.setFocusPainted(false);
        // ** FIX LỖI MỜ CHỮ **
        button.setOpaque(true);
        button.setBorderPainted(false);
    }
    
    // Hàm tìm kiếm chung
    private void handleSearch(String keyword, int searchType) {
        if (keyword == null || keyword.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập từ khóa tìm kiếm.");
            return;
        }
        
        Users currentUser = findUser(this.studentId);
        if (currentUser == null) return;
        
        ArrayList<Books> selectedBooks = null;
        String searchTitle = "";
        
        if (searchType == 1) { // Title
            selectedBooks = currentUser.SearchBookbyTitle(keyword);
            searchTitle = "tựa đề \"" + keyword + "\"";
        } else if (searchType == 2) { // Author
            selectedBooks = currentUser.SearchBookbyAuthor(keyword);
            searchTitle = "tác giả \"" + keyword + "\"";
        } else if (searchType == 3) { // Subject
            selectedBooks = currentUser.SearchBookbySubject(keyword);
            searchTitle = "chủ đề \"" + keyword + "\"";
        }
        
        // Xóa bảng cũ
        tableModel.setRowCount(0);

        // Hiển thị kết quả
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
            JOptionPane.showMessageDialog(this, "Không tìm thấy sách nào khớp với " + searchTitle);
        }
    }
    
    // Xử lý logic cho nút "Xem Lịch sử Mượn"
    private void handleViewLoanInfo() {
        if (historyTextArea == null) return;
        Users currentUser = findUser(this.studentId);
        if (currentUser == null) return;

        String printingMessage = currentUser.ViewInformation(Library.LoanList, studentId);
        historyTextArea.setText("Thông tin cá nhân và lịch sử mượn sách:\n\n" + printingMessage);
        historyTextArea.setCaretPosition(0); // Cuộn lên đầu
    }
    
    // Xử lý Đăng xuất
    private void handleLogout() {
        this.dispose(); // Đóng cửa sổ Student
        new LoginUI().setVisible(true); // Mở lại cửa sổ Login
    }

    // *** ĐÃ SỬA HÀM NÀY ***
    // Hàm trợ giúp tìm đối tượng User hiện tại
    private Users findUser(int idToFind) {
        // Phải kiểm tra Library.UsersList vì nó được tải lúc khởi động
        if (Library.UsersList == null) {
            showError("Lỗi: Danh sách người dùng chưa được tải.");
            return null;
        }
        for (Users u : Library.UsersList) {
            if (u.GetId() == idToFind) {
                return u;
            }
        }
        showError("Không tìm thấy thông tin User ID: " + idToFind);
        return null; 
    }
    
    // Hàm trợ giúp hiển thị lỗi
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
    
    // Hàm main (không cần thiết nếu chạy từ LoginUI)
    public static void main(String args[]) {
        // Cần set L&F ở đây nếu chạy file này độc lập
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(StudentMenuUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        java.awt.EventQueue.invokeLater(() -> {
            new Library(); // Phải khởi tạo Library để tải UsersList
            new StudentMenuUI(101).setVisible(true); // Test với ID 101
        });
    }

    // Các biến và hàm cũ không dùng đến
    @SuppressWarnings("unchecked")
    private void initComponents() {}
    private void ResultActionPerformed(java.awt.event.ActionEvent evt) {}
    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {}
}