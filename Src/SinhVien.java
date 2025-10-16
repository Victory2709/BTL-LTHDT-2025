import java.util.*;

public class SinhVien {
    private String maSV;
    private String tenSV;
    private String namSinh;
    private String sdt;
    private String maLop;
    private int tuoi;

    public SinhVien() {
        maSV = "";
        tenSV = "";
        namSinh = "";
        sdt = "";
        maLop = "";
        tuoi = 0;
    }

    public void Input(Scanner sc) {
        System.out.print("Nhập mã sinh viên: ");
        maSV = sc.nextLine();
        System.out.print("Nhập tên sinh viên: ");
        tenSV = sc.nextLine();
        System.out.print("Nhập ngày sinh (dd/mm/yyyy): ");
        String ns = sc.nextLine();
        System.out.print("Nhập số điện thoại: ");
        sdt = sc.nextLine();
        System.out.print("Nhập mã lớp: ");
        maLop = sc.nextLine();
        System.out.print("Nhập tuổi: ");
        tuoi = sc.nextInt();
        sc.nextLine(); // Clear buffer

        // Format date
        String[] parts = ns.split("/");
        if (parts[0].length() == 1) parts[0] = "0" + parts[0];
        if (parts[1].length() == 1) parts[1] = "0" + parts[1];
        namSinh = parts[0] + "/" + parts[1] + "/" + parts[2];
    }

    public void Output() {
        System.out.printf("Mã SV: %s | Tên: %s | Ngày sinh: %s | SDT: %s | Lớp: %s | Tuổi: %d\n", nmaSV, tenSV, namSinh, sdt, maLop, tuoi);
    }

    // Getter methods
    public String getMaSV() { return maSV; }
    public String getTenSV() { return tenSV; }
    public String getNamSinh() { return namSinh; }
    public String getSdt() { return sdt; }
    public String getMaLop() { return maLop; }
    public int getTuoi() { return tuoi; }
}

class Sach {
    private String maSach;
    private String tenSach;
    private String tacGia;
    private String namXB;
    private String nhaXB;
    private String theLoai;
    private int soLuong;

    public Sach() {
        maSach = "";
        tenSach = "";
        tacGia = "";
        namXB = "";
        nhaXB = "";
        theLoai = "";
        soLuong = 0;
    }

    public void Input(Scanner sc) {
        System.out.print("Nhập mã sách: ");
        maSach = sc.nextLine();
        System.out.print("Nhập tên sách: ");
        tenSach = sc.nextLine();
        System.out.print("Nhập tác giả: ");
        tacGia = sc.nextLine();
        System.out.print("Nhập năm xuất bản: ");
        namXB = sc.nextLine();
        System.out.print("Nhập nhà xuất bản: ");
        nhaXB = sc.nextLine();
        System.out.print("Nhập thể loại: ");
        theLoai = sc.nextLine();
        System.out.print("Nhập số lượng: ");
        soLuong = sc.nextInt();
        sc.nextLine(); // Clear buffer
    }

    public void Output() {
        System.out.printf("Mã sách: %s | Tên: %s | Tác giả: %s | Năm XB: %s | Nhà XB: %s | Thể loại: %s | SL: %d\n", maSach, tenSach, tacGia, namXB, nhaXB, theLoai, soLuong);
    }

    // Getter methods
    public String getMaSach() { return maSach; }
    public String getTenSach() { return tenSach; }
    public String getTacGia() { return tacGia; }
    public String getNamXB() { return namXB; }
    public String getNhaXB() { return nhaXB; }
    public String getTheLoai() { return theLoai; }
    public int getSoLuong() { return soLuong; }
    
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }
}

class Phieu {
    private String maPhieu;
    private String maSV;
    private String maSach;
    private int soLuongMuon;
    private String ngayMuon;
    private String ngayTra;

    public Phieu() {
        maPhieu = "";
        maSV = "";
        maSach = "";
        soLuongMuon = 0;
        ngayMuon = "";
        ngayTra = "";
    }

    public void Input(Scanner sc) {
        System.out.print("Nhập mã phiếu: ");
        maPhieu = sc.nextLine();
        System.out.print("Nhập mã sinh viên: ");
        maSV = sc.nextLine();
        System.out.print("Nhập mã sách: ");
        maSach = sc.nextLine();
        System.out.print("Nhập số lượng mượn: ");
        soLuongMuon = sc.nextInt();
        sc.nextLine(); // Clear buffer
        System.out.print("Nhập ngày mượn (dd/mm/yyyy): ");
        ngayMuon = sc.nextLine();
        System.out.print("Nhập ngày trả (dd/mm/yyyy): ");
        ngayTra = sc.nextLine();
    }

    public void Output() {
        System.out.printf("Mã phiếu: %s | Mã SV: %s | Mã sách: %s | SL mượn: %d | Ngày mượn: %s | Ngày trả: %s\n", maPhieu, maSV, maSach, soLuongMuon, ngayMuon, ngayTra);
    }

    // Getter methods
    public String getMaPhieu() { return maPhieu; }
    public String getMaSV() { return maSV; }
    public String getMaSach() { return maSach; }
    public String getNgayMuon() { return ngayMuon; }
    public String getNgayTra() { return ngayTra; }
    public int getSoLuongMuon() { return soLuongMuon; }
}

public class QuanLyThuVien {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        System.out.println("=== NHẬP THÔNG TIN SINH VIÊN ===");
        SinhVien sv = new SinhVien();
        sv.Input(sc);
        
        System.out.println("\n=== NHẬP THÔNG TIN SÁCH ===");
        Sach sach = new Sach();
        sach.Input(sc);
        
        System.out.println("\n=== NHẬP THÔNG TIN PHIẾU MƯỢN ===");
        Phieu phieu = new Phieu();
        phieu.Input(sc);
        
        System.out.println("\n=== THÔNG TIN ĐÃ NHẬP ===");
        sv.Output();
        sach.Output();
        phieu.Output();
    }
}