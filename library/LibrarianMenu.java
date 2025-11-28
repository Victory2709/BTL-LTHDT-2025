
package library;

import java.awt.*;
import javax.swing.*;

public class LibrarianMenu extends ClerkMenuUI { // <-- KẾ THỪA TỪ CLERK

    /**
     * Creates new form LibrarianMenu (Đã viết lại)
     */
    public LibrarianMenu(int id, Library LibObj) {
        // 1. GỌI SUPER() TRƯỚC TIÊN VỚI TIÊU ĐỀ TẠM
        super(id, LibObj, "Librarian Menu (ID: " + id + ")", "Đang tải...");

        // 2. BÂY GIỜ MỚI TÌM TÊN THẬT
        String name = findStaffName(id, LibObj.LibrarianList);
        
        // 3. CẬP NHẬT LẠI BIẾN VÀ TIÊU ĐỀ UI
        this.staffName = name; 
        if (this.titleLabel != null) { // Kiểm tra null
            this.titleLabel.setText("  Chào mừng, " + name); // Cập nhật trực tiếp UI
        }
        
        // 4. THÊM TAB THỨ 4
        tabbedPane.addTab("Quản lý Kho Sách (Admin)", createBookMgmtTab());
    }

    // --- TAB MỚI CỦA LIBRARIAN (ĐÃ SỬA LẠI LAYOUT) ---
    private JPanel createBookMgmtTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = createGBC();

        // --- Thêm Sách ---
        addTitleRow(panel, gbc, 0, "Thêm Sách Mới");
        JTextField addTitle = new JTextField(15);
        JTextField addAuthor = new JTextField(15);
        JTextField addSubject = new JTextField(15);
        JTextField addQty = new JTextField(5);
        JButton btnAddBook = new JButton("Thêm Sách Mới");
        styleButton(btnAddBook);
        
        addFormRow(panel, gbc, 1, new JLabel("Tựa đề:"), addTitle);
        addFormRow(panel, gbc, 2, new JLabel("Tác giả:"), addAuthor);
        addFormRow(panel, gbc, 3, new JLabel("Chủ đề:"), addSubject);
        addFormRow(panel, gbc, 4, new JLabel("Số lượng:"), addQty, btnAddBook);

        // --- Xóa Sách ---
        addSeparatorRow(panel, gbc, 5);
        addTitleRow(panel, gbc, 6, "Xóa Sách");
        JTextField deleteBookId = new JTextField(15);
        JButton btnDeleteBook = new JButton("Xóa Sách");
        styleButton(btnDeleteBook);
        addFormRow(panel, gbc, 7, new JLabel("Book ID cần xóa:"), deleteBookId, btnDeleteBook);
        
        // --- Cập nhật Sách ---
        addSeparatorRow(panel, gbc, 8);
        addTitleRow(panel, gbc, 9, "Cập nhật Thông tin Sách");
        JTextField updateBookId = new JTextField(15);
        JTextField updateBookInfo = new JTextField(15);
        JComboBox<String> updateType = new JComboBox<>(new String[]{"Tựa đề", "Tác giả", "Chủ đề", "Số lượng"});
        JButton btnUpdateBook = new JButton("Cập nhật Sách");
        styleButton(btnUpdateBook);
        
        addFormRow(panel, gbc, 10, new JLabel("Book ID cần cập nhật:"), updateBookId);
        addFormRow(panel, gbc, 11, new JLabel("Cập nhật thông tin:"), updateType);
        addFormRow(panel, gbc, 12, new JLabel("Thông tin mới:"), updateBookInfo, btnUpdateBook);

        // Thêm sự kiện
        btnAddBook.addActionListener((e) -> handleAddBook(addTitle.getText(), addAuthor.getText(), addSubject.getText(), addQty.getText()));
        btnDeleteBook.addActionListener((e) -> handleDeleteBook(deleteBookId.getText()));
        btnUpdateBook.addActionListener((e) -> handleUpdateBook(updateBookId.getText(), updateBookInfo.getText(), updateType.getSelectedIndex()));
        
        return panel;
    }
    
    // --- CÁC HÀM LOGIC MỚI CỦA LIBRARIAN ---

    private void handleAddBook(String title, String author, String subject, String qtyStr) {
        try {
            int quantity = Integer.parseInt(qtyStr);
            LibObj.LibrarianAddNewBook(author, title, subject, quantity, staffId);
            log("Thành công: Đã thêm sách mới: " + title);
        } catch (Exception e) {
            showError("Số lượng phải là số. Lỗi: " + e.getMessage());
        }
    }
    
    private void handleDeleteBook(String bookIdStr) {
        try {
            int bookId = Integer.parseInt(bookIdStr);
            boolean result = LibObj.LibrarianDeleteBook(bookId, staffId);
            if (result) {
                log("Thành công: Đã xóa sách Book ID " + bookId + ".");
            } else {
                showError("Xóa sách thất bại. (Có thể sách đang được mượn).");
            }
        } catch (Exception e) {
            showError("Book ID phải là số. Lỗi: " + e.getMessage());
        }
    }
    
    private void handleUpdateBook(String bookIdStr, String newInfo, int typeIndex) {
        try {
            int bookId = Integer.parseInt(bookIdStr);
            int command = typeIndex + 1; // 0=Title -> 1; 1=Author -> 2; ...
            int newQuantity = 0;

            if (command == 4) { // Nếu là cập nhật số lượng
                newQuantity = Integer.parseInt(newInfo);
            }
            
            LibObj.LibrarianUpdateBookInfo(bookId, staffId, newInfo, newQuantity, command);
            log("Thành công: Đã cập nhật sách Book ID " + bookId + ".");
        } catch (Exception e) {
            showError("ID/Số lượng phải là số. Lỗi: " + e.getMessage());
        }
    }
    
    // Các hàm và biến không dùng đến
    @SuppressWarnings("unchecked")
    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        pack();
    }
}