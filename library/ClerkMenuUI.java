
package library;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class ClerkMenuUI extends javax.swing.JFrame {

    protected int staffId; // ID của Clerk/Librarian
    protected Library LibObj; // Đối tượng Library
    protected String staffName; // Tên nhân viên

    // --- Bảng màu ---
    protected static final Color COLOR_HEADER_RED = new Color(176, 0, 0); // Đỏ sẫm
    protected static final Color COLOR_ACCENT_RED = new Color(210, 43, 43); // Đỏ nhấn
    protected static final Color COLOR_PASTEL_RED = new Color(254, 242, 242); // Đỏ pastel
    protected static final Color COLOR_WHITE = Color.WHITE;
    protected static final Font FONT_TITLE = new Font("Arial", Font.BOLD, 24);
    protected static final Font FONT_LABEL = new Font("Arial", Font.PLAIN, 14);
    protected static final Font FONT_BUTTON = new Font("Arial", Font.BOLD, 14);
    
    // --- Biến Giao diện ---
    protected JTabbedPane tabbedPane;
    protected JTextArea logTextArea;
    protected JTable searchResultTable;
    protected DefaultTableModel tableModel;
    
    // *** SỬA DÒNG NÀY: 'private' thành 'protected' ***
    protected JLabel titleLabel;

    /**
     * Creates new form ClerkMenuUI (Đã viết lại)
     */
    public ClerkMenuUI(int id, Library LibObj) {
        this.staffId = id;
        this.LibObj = LibObj;
        // TÌM TÊN NHÂN VIÊN
        this.staffName = findStaffName(id, LibObj.ClerkList);
        
        initUI("Clerk Menu (ID: " + staffId + ")", "Chào mừng, " + this.staffName);
    }
    
    // Hàm tạo (constructor) này dành cho Librarian kế thừa
    protected ClerkMenuUI(int id, Library LibObj, String title, String header) {
        this.staffId = id;
        this.LibObj = LibObj;
        initUI(title, header);
    }
    
    // HÀM MỚI: TÌM TÊN NHÂN VIÊN
    protected String findStaffName(int idToFind, ArrayList<? extends Users> staffList) {
        if (staffList == null) {
            return "Unknown Staff";
        }
        for (Users u : staffList) {
            if (u.GetId() == idToFind) {
                return u.GetName();
            }
        }
        return "Unknown (ID: " + idToFind + ")";
    }
    
    // Hàm khởi tạo UI chung
    private void initUI(String title, String header) {
        // --- 1. Cài đặt cửa sổ chính ---
        setTitle(title);
        setSize(900, 700);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // --- 2. Tạo Panel Chính (BorderLayout) ---
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(COLOR_WHITE);
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // --- 3. Header (NORTH) ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(COLOR_HEADER_RED);
        headerPanel.setPreferredSize(new Dimension(800, 60));
        
        // *** SỬA DÒNG NÀY: Khởi tạo biến 'titleLabel' (đã là 'protected')
        titleLabel = new JLabel("  " + header); // <-- DÙNG HEADER ĐỘNG
        titleLabel.setFont(FONT_TITLE);
        titleLabel.setForeground(COLOR_WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        JButton logoutButton = new JButton("Đăng xuất");
        styleButton(logoutButton); // Sửa lỗi mờ chữ
        headerPanel.add(logoutButton, BorderLayout.EAST);
        
        // --- 4. Giao diện TAB (CENTER) ---
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(FONT_LABEL);
        tabbedPane.setBackground(COLOR_PASTEL_RED);

        // Tạo các tab
        tabbedPane.addTab("Tra cứu Sách & User", createSearchTab());
        tabbedPane.addTab("Giao dịch Mượn/Trả", createTransactionTab());
        tabbedPane.addTab("Quản lý Người mượn", createBorrowerTab());
        
        // --- 5. Khung Log (SOUTH) ---
        logTextArea = new JTextArea(10, 50);
        logTextArea.setEditable(false);
        logTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        logTextArea.setWrapStyleWord(true);
        logTextArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(logTextArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Log hoạt động"));
        
        // --- 6. Gắn vào cửa sổ ---
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        mainPanel.add(scrollPane, BorderLayout.SOUTH);
        
        // Thêm sự kiện
        logoutButton.addActionListener((e) -> handleLogout());
        
        add(mainPanel);
        
        log("Hệ thống sẵn sàng.");
    }

    // (Toàn bộ code còn lại của ClerkMenuUI.java giữ nguyên)
    // ...
        private JPanel createSearchTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(COLOR_WHITE);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Panel controls (NORTH)
        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlsPanel.setBackground(COLOR_WHITE);
        
        JTextField searchTextField = new JTextField(30);
        searchTextField.setFont(FONT_LABEL);
        
        JButton btnSearchTitle = new JButton("Tìm sách theo Tựa đề");
        JButton btnSearchAuthor = new JButton("Tìm sách theo Tác giả");
        JButton btnSearchSubject = new JButton("Tìm sách theo Chủ đề");
        JButton btnSearchUser = new JButton("Xem lịch sử User");
        
        styleButton(btnSearchTitle);
        styleButton(btnSearchAuthor);
        styleButton(btnSearchSubject);
        styleButton(btnSearchUser);
        
        controlsPanel.add(new JLabel("Nhập từ khóa/ID:"));
        controlsPanel.add(searchTextField);
        controlsPanel.add(btnSearchTitle);
        controlsPanel.add(btnSearchAuthor);
        controlsPanel.add(btnSearchSubject);
        controlsPanel.add(btnSearchUser);

        // Bảng kết quả (CENTER)
        String[] columnNames = {"Book ID", "Tựa đề", "Tác giả", "Chủ đề", "Số lượng"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        searchResultTable = new JTable(tableModel);
        searchResultTable.getTableHeader().setBackground(COLOR_PASTEL_RED);
        searchResultTable.getTableHeader().setFont(FONT_LABEL);
        searchResultTable.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(searchResultTable);
        
        // Thêm sự kiện
        btnSearchTitle.addActionListener((e) -> handleSearch(searchTextField.getText(), 1));
        btnSearchAuthor.addActionListener((e) -> handleSearch(searchTextField.getText(), 2));
        btnSearchSubject.addActionListener((e) -> handleSearch(searchTextField.getText(), 3));
        btnSearchUser.addActionListener((e) -> handleViewLoanInfo(searchTextField.getText()));
        
        panel.add(controlsPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createTransactionTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = createGBC();
        
        // --- Checkout (Mượn) ---
        addTitleRow(panel, gbc, 0, "Cho Mượn Sách");
        JTextField coUserId = new JTextField(15);
        JTextField coBookId = new JTextField(15);
        JButton btnCheckout = new JButton("Xác nhận Mượn");
        styleButton(btnCheckout);
        
        addFormRow(panel, gbc, 1, new JLabel("User ID:"), coUserId);
        addFormRow(panel, gbc, 2, new JLabel("Book ID:"), coBookId, btnCheckout);

        // --- Checkin (Trả) ---
        addSeparatorRow(panel, gbc, 3);
        addTitleRow(panel, gbc, 4, "Nhận Trả Sách");
        JTextField ciUserId = new JTextField(15);
        JTextField ciBookId = new JTextField(15);
        JTextField ciReturnDate = new JTextField("yyyy/MM/dd", 15);
        JButton btnCheckin = new JButton("Xác nhận Trả");
        styleButton(btnCheckin);
        
        addFormRow(panel, gbc, 5, new JLabel("User ID:"), ciUserId);
        addFormRow(panel, gbc, 6, new JLabel("Book ID:"), ciBookId);
        addFormRow(panel, gbc, 7, new JLabel("Ngày Trả (yyyy/MM/dd):"), ciReturnDate, btnCheckin);

        // --- Ghi Phạt ---
        addSeparatorRow(panel, gbc, 8);
        addTitleRow(panel, gbc, 9, "Ghi nhận Trả Phạt");
        JTextField fineUserId = new JTextField(15);
        JButton btnRecordFine = new JButton("Xác nhận Trả Phạt");
        styleButton(btnRecordFine);
        addFormRow(panel, gbc, 10, new JLabel("User ID:"), fineUserId, btnRecordFine);
        
        // --- Cập nhật SL ---
        addSeparatorRow(panel, gbc, 11);
        addTitleRow(panel, gbc, 12, "Cập nhật Số Lượng Sách");
        JTextField renewBookId = new JTextField(15);
        JButton btnIncrease = new JButton("Tăng SL");
        JButton btnDecrease = new JButton("Giảm SL");
        styleButton(btnIncrease); styleButton(btnDecrease);
        addFormRow(panel, gbc, 13, new JLabel("Book ID:"), renewBookId, btnIncrease);
        gbc.gridx = 2; // Đặt nút Giảm SL bên cạnh nút Tăng
        panel.add(btnDecrease, gbc);
        
        // Thêm sự kiện
        btnCheckout.addActionListener((e) -> handleCheckout(coUserId.getText(), coBookId.getText()));
        btnCheckin.addActionListener((e) -> handleCheckin(ciUserId.getText(), ciBookId.getText(), ciReturnDate.getText()));
        btnRecordFine.addActionListener((e) -> handleRecordFine(fineUserId.getText()));
        btnIncrease.addActionListener((e) -> handleRenewItem(renewBookId.getText(), 1)); // 1 = Tăng
        btnDecrease.addActionListener((e) -> handleRenewItem(renewBookId.getText(), 2)); // 2 = Giảm
        
        return panel;
    }
    
    private JPanel createBorrowerTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = createGBC();

        // --- Thêm Người mượn ---
        addTitleRow(panel, gbc, 0, "Thêm Người mượn Mới");
        JTextField addName = new JTextField(15);
        JTextField addGender = new JTextField(5);
        JTextField addTel = new JTextField(15);
        JTextField addAddress = new JTextField(15);
        JButton btnAddBorrower = new JButton("Thêm Người mượn");
        styleButton(btnAddBorrower);
        
        addFormRow(panel, gbc, 1, new JLabel("Tên:"), addName);
        addFormRow(panel, gbc, 2, new JLabel("Giới tính (M/F):"), addGender);
        addFormRow(panel, gbc, 3, new JLabel("Số điện thoại:"), addTel);
        addFormRow(panel, gbc, 4, new JLabel("Địa chỉ:"), addAddress, btnAddBorrower);

        // --- Cập nhật Người mượn ---
        addSeparatorRow(panel, gbc, 5);
        addTitleRow(panel, gbc, 6, "Cập nhật Người mượn");
        JTextField updateUserId = new JTextField(15);
        JTextField updateInfo = new JTextField(15);
        JButton btnUpdateName = new JButton("Cập nhật Tên");
        JButton btnUpdateGender = new JButton("Cập nhật Giới tính");
        styleButton(btnUpdateName); styleButton(btnUpdateGender);
        
        addFormRow(panel, gbc, 7, new JLabel("User ID cần cập nhật:"), updateUserId);
        
        // ** SỬA LỖI TẠI ĐÂY **
        // Hàng 8: Chỉ có Label và Text Field
        addFormRow(panel, gbc, 8, new JLabel("Thông tin mới:"), updateInfo);
        
        // Hàng 9: Chỉ có 2 nút
        gbc.gridy = 9;
        gbc.gridx = 1; // Đặt nút ở cột 1
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(btnUpdateName, gbc);
        
        gbc.gridx = 2; // Đặt nút ở cột 2
        panel.add(btnUpdateGender, gbc);

        // Thêm sự kiện
        btnAddBorrower.addActionListener((e) -> handleAddBorrower(addName.getText(), addGender.getText(), addTel.getText(), addAddress.getText()));
        btnUpdateName.addActionListener((e) -> handleUpdateBorrower(updateUserId.getText(), updateInfo.getText(), 1)); // 1 = Tên
        btnUpdateGender.addActionListener((e) -> handleUpdateBorrower(updateUserId.getText(), updateInfo.getText(), 2)); // 2 = Giới

        return panel;
    }
    
    // --- CÁC HÀM XỬ LÝ LOGIC (Lấy từ code cũ) ---
    
    // Các hàm xử lý tìm kiếm
    protected void handleSearch(String keyword, int searchType) {
        if (keyword.trim().isEmpty()) { showError("Vui lòng nhập từ khóa."); return; }
        
        ArrayList<Books> selectedBooks = null;
        String searchTitle = "";
        
        if (searchType == 1) { // Title
            selectedBooks = LibObj.ClerkSearchBookbyTitle(keyword, staffId);
            searchTitle = "tựa đề \"" + keyword + "\"";
        } else if (searchType == 2) { // Author
            selectedBooks = LibObj.ClerkSearchBookbyAuthor(keyword, staffId);
            searchTitle = "tác giả \"" + keyword + "\"";
        } else if (searchType == 3) { // Subject
            selectedBooks = LibObj.ClerkSearchBookbySubject(keyword, staffId);
            searchTitle = "chủ đề \"" + keyword + "\"";
        }
        
        tableModel.setRowCount(0); // Xóa bảng cũ
        if (selectedBooks != null && !selectedBooks.isEmpty()) {
            for (Books b : selectedBooks) {
                tableModel.addRow(new Object[]{ b.GetBookId(), b.GetTitle(), b.GetAuthor(), b.GetSubject(), b.GetQuantity() });
            }
            log("Tìm thấy " + selectedBooks.size() + " kết quả cho " + searchTitle);
        } else {
            log("Không tìm thấy kết quả nào cho " + searchTitle);
        }
    }
    
    // Xem lịch sử user
    protected void handleViewLoanInfo(String userIdStr) {
        try {
            int userId = Integer.parseInt(userIdStr);
            String info = LibObj.CheckLoanofUser(userId, staffId);
            log("--- Lịch sử User ID " + userId + " ---\n" + info + "\n--- Kết thúc Lịch sử ---");
        } catch (NumberFormatException e) {
            showError("User ID phải là một con số.");
        }
    }
    
    // Checkout
    protected void handleCheckout(String userIdStr, String bookIdStr) {
        try {
            int userId = Integer.parseInt(userIdStr);
            int bookId = Integer.parseInt(bookIdStr);
            LibObj.ClerkCheckOutItem(bookId, userId, staffId);
            log("Thành công: Đã cho mượn sách " + bookId + " cho User " + userId + ".");
        } catch (Exception e) {
            showError("ID phải là số. Lỗi: " + e.getMessage());
        }
    }
    
    // Checkin
    protected void handleCheckin(String userIdStr, String bookIdStr, String returnDate) {
        try {
            int userId = Integer.parseInt(userIdStr);
            int bookId = Integer.parseInt(bookIdStr);
            if (returnDate.isEmpty() || returnDate.equals("yyyy/MM/dd")) {
                showError("Vui lòng nhập ngày trả.");
                return;
            }
            LibObj.ClerkCheckInItem(returnDate, bookId, userId, staffId);
            log("Thành công: Đã nhận trả sách " + bookId + " từ User " + userId + ".");
        } catch (Exception e) {
            showError("ID phải là số hoặc sai định dạng ngày. Lỗi: " + e.getMessage());
        }
    }
    
    // Ghi phạt
    protected void handleRecordFine(String userIdStr) {
        try {
            int userId = Integer.parseInt(userIdStr);
            LibObj.ClerkRecordFine(userId, staffId);
            log("Thành công: Đã ghi nhận trả phạt cho User " + userId + ".");
        } catch (Exception e) {
            showError("User ID phải là số. Lỗi: " + e.getMessage());
        }
    }
    
    // Cập nhật SL
    protected void handleRenewItem(String bookIdStr, int option) {
        try {
            int bookId = Integer.parseInt(bookIdStr);
            LibObj.ClerkRenewItem(bookId, option, staffId);
            String action = (option == 1) ? "Tăng" : "Giảm";
            log("Thành công: Đã " + action + " số lượng cho Book ID " + bookId + ".");
        } catch (Exception e) {
            showError("Book ID phải là số. Lỗi: " + e.getMessage());
        }
    }
    
    // Thêm người mượn
    protected void handleAddBorrower(String name, String genderStr, String tel, String address) {
        if (name.isEmpty() || genderStr.isEmpty() || tel.isEmpty() || address.isEmpty()) {
            showError("Vui lòng điền đầy đủ thông tin.");
            return;
        }
        boolean result = LibObj.AddNewBorrower(name, genderStr.charAt(0), tel, address, staffId);
        if (result) {
            log("Thành công: Đã thêm người mượn mới: " + name);
        } else {
            showError("Thêm người mượn thất bại.");
        }
    }

    // Cập nhật người mượn
    protected void handleUpdateBorrower(String userIdStr, String newInfo, int infoType) {
        try {
            int userId = Integer.parseInt(userIdStr);
            if (newInfo.isEmpty()) {
                showError("Vui lòng nhập thông tin mới.");
                return;
            }
            boolean result = LibObj.ClerkUpdatingInfo(newInfo, infoType, staffId, userId);
            if (result) {
                log("Thành công: Cập nhật thông tin cho User ID " + userId + ".");
            } else {
                showError("Cập nhật thất bại.");
            }
        } catch (Exception e) {
            showError("User ID phải là số. Lỗi: " + e.getMessage());
        }
    }

    // --- CÁC HÀM TRỢ GIÚP ---
    
    protected void handleLogout() {
        this.dispose();
        new LoginUI().setVisible(true);
    }
    
    protected void log(String message) {
        logTextArea.append(">> " + message + "\n");
        logTextArea.setCaretPosition(logTextArea.getDocument().getLength()); // Tự cuộn
    }
    
    protected void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
        log("LỖI: " + message);
    }
    
    protected void styleButton(JButton button) {
        button.setBackground(COLOR_ACCENT_RED);
        button.setForeground(COLOR_WHITE);
        button.setFont(FONT_BUTTON);
        button.setFocusPainted(false);
        // ** FIX LỖI MỜ CHỮ **
        button.setOpaque(true);
        button.setBorderPainted(false);
    }
    
    // --- CÁC HÀM TRỢ GIÚP LAYOUT (ĐÃ SỬA LẠI) ---
    
    // Tạo GBC cơ bản
    protected GridBagConstraints createGBC() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // 5px padding
        gbc.anchor = GridBagConstraints.WEST; // Anchor trái
        return gbc;
    }
    
    // Thêm một hàng tiêu đề (ví dụ: "Cho Mượn Sách")
    protected void addTitleRow(JPanel panel, GridBagConstraints gbc, int y, String title) {
        gbc.gridy = y;
        gbc.gridx = 0;
        gbc.gridwidth = 3; // Kéo dài 3 cột
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(COLOR_ACCENT_RED);
        panel.add(titleLabel, gbc);
        gbc.gridwidth = 1; // Reset
    }
    
    // Thêm một hàng phân cách
    protected void addSeparatorRow(JPanel panel, GridBagConstraints gbc, int y) {
        gbc.gridy = y;
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 0, 15, 0); // Tăng padding cho separator
        JSeparator sep = new JSeparator();
        sep.setForeground(COLOR_PASTEL_RED);
        sep.setBackground(COLOR_PASTEL_RED);
        panel.add(sep, gbc);
        gbc.gridwidth = 1; // Reset
        gbc.insets = new Insets(5, 5, 5, 5); // Reset padding
    }

    // Thêm một hàng form (Label + Component)
    protected void addFormRow(JPanel panel, GridBagConstraints gbc, int y, JLabel label, JComponent component) {
        gbc.gridy = y;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST; // Căn phải label
        panel.add(label, gbc);
        
        gbc.gridx = 1;
        gbc.gridwidth = 2; // Kéo dài 2 cột
        gbc.fill = GridBagConstraints.HORIZONTAL; // Cho component giãn ra
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(component, gbc);
        gbc.gridwidth = 1; // Reset
    }
    
    // Thêm một hàng form (Label + Component + Button)
    protected void addFormRow(JPanel panel, GridBagConstraints gbc, int y, JLabel label, JComponent component, JButton button) {
        gbc.gridy = y;
        // Label
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(label, gbc);
        
        // Component
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(component, gbc);
        
        // Button
        gbc.gridx = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(button, gbc);
    }
    

    // Các hàm và biến không dùng đến
    @SuppressWarnings("unchecked")
    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        pack();
    }
}