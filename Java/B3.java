package BaiTap;

//Set<String>: không cho phép phần tử trùng
//HashSet: nhanh, đơn giản, không quan tâm thứ tự
//Khi add() số đã tồn tại → bị bỏ qua

/*
Bài 3: Nhập danh sách số điện thoại từ người dùng
• Lưu trữ và loại bỏ số trùng lặp
• In danh sách số điện thoại sau khi đã loại bỏ số trùng lặp
Gợi ý:
• Dùng Set<String> (HashSet)
*/



import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class B3 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Set<String> danhSachSDT = new HashSet<>();

        System.out.print("Nhap so luong SDT: ");
        int n = sc.nextInt();
        sc.nextLine(); // bo giong thua

        // Nhập danh sách số điện thoại
        for (int i = 0; i < n; i++) {
            System.out.print("Nhap so dien thoai thu "+(i+1)+": ");
            String sdt = sc.nextLine();
            danhSachSDT.add(sdt);

        }

        // In danh sách sau khi loại trùng
        System.out.println("\nDanh sach SDT sau khi loai so trung");
        for (String sdt : danhSachSDT) {
            System.out.println(sdt);
        }
        sc.close();


    }
}
