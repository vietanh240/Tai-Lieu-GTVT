package BaiTap;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class B9 {
    static List<Student_B9> danhSach = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("1. Them sinh vien");
            System.out.println("2. Them/Sua diem");
            System.out.println("3. Hien thi danh sach");
            System.out.println("0. Thoat");

            int chon = sc.nextInt();
            sc.nextLine();

            switch (chon) {
                case 1:
                    themSinhVien();
                    break;
                case 2:
                    themHoacSuaDiem();
                    break;
                case 3:
                    hienThiDanhSach();
                    break;
                case 0:
                    return;
            }
        }
    }

    static void themSinhVien() {
        System.out.print("Nhap ma SV: ");
        String ma = sc.nextLine();
        System.out.print("Nhap ten SV: ");
        String ten = sc.nextLine();

        danhSach.add(new Student_B9(ma, ten));
        System.out.println("Da them sinh vien!");
    }

    static void themHoacSuaDiem() {
        System.out.print("Nhap ma SV: ");
        String ma = sc.nextLine();

        for (Student_B9 sv : danhSach) {
            if (sv.getMaSV().equals(ma)) {
                System.out.print("Nhap ten mon: ");
                String mon = sc.nextLine();
                System.out.print("Nhap diem: ");
                double diem = sc.nextDouble();
                sc.nextLine();

                sv.themHoacSuaDiem(mon, diem);
                System.out.println("Da cap nhat diem!");
                return;
            }
        }

        System.out.println("Khong tim thay sinh vien!");
    }

    static void hienThiDanhSach() {
        for (Student_B9 sv : danhSach) {
            sv.hienThi();
        }
    }
}