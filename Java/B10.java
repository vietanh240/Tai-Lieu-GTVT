package BaiTap;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class B10 {
    static List<Product> danhSachSP = new ArrayList<>();
    static List<OrderItem> hoaDon = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        while (true) {
            System.out.println("\n===== MENU =====");
            System.out.println("1. Them san pham");
            System.out.println("2. Hien thi san pham");
            System.out.println("3. Mua san pham");
            System.out.println("4. Xem hoa don");
            System.out.println("0. Thoat");
            System.out.print("Chon: ");

            int chon = sc.nextInt();
            sc.nextLine();

            switch (chon) {
                case 1:
                    themSanPham();
                    break;
                case 2:
                    hienThiSanPham();
                    break;
                case 3:
                    muaSanPham();
                    break;
                case 4:
                    hienThiHoaDon();
                    break;
                case 0:
                    System.exit(0);
            }
        }
    }

    static void themSanPham() {
        System.out.print("Ma: ");
        String ma = sc.nextLine();

        System.out.print("Ten: ");
        String ten = sc.nextLine();

        System.out.print("Gia: ");
        double gia = sc.nextDouble();

        danhSachSP.add(new Product(ma, ten, gia));
        System.out.println("Them thanh cong!");
    }

    static void hienThiSanPham() {
        for (Product sp : danhSachSP) {
            System.out.println(sp);
        }
    }

    static void muaSanPham() {
        System.out.print("Nhap ma san pham: ");
        String ma = sc.nextLine();

        for (Product sp : danhSachSP) {
            if (sp.getMa().equalsIgnoreCase(ma)) {
                System.out.print("So luong: ");
                int sl = sc.nextInt();
                sc.nextLine();

                hoaDon.add(new OrderItem(sp, sl));
                System.out.println("Đa them vao hoa đon!");
                return;
            }
        }
        System.out.println("Khong tim thay san pham!");
    }

    static void hienThiHoaDon() {
        double tong = 0;

        System.out.println("\n===== HOA ĐON =====");
        for (OrderItem item : hoaDon) {
            System.out.println(item);
            tong += item.tinhThanhTien();
        }

        System.out.println("TONG TIEN: " + tong + " VND");
    }
}
