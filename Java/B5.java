package BaiTap;

import static function.KeyBoard.readInt;

/*
Bài 5: Tạo từ điển Anh – Việt. Yêu cầu:
• Cho phép người dùng thêm từ vựng (tiếng Anh – tiếng Việt)
• Tra từ điển: Nhập từ tiếng Anh , tìm nghĩa tiếng Việt 
• Cho phép cập nhật lại nghĩa nếu từ đã tồn tại trong từ điển
• Hiển thị toàn bộ từ điển theo bảng chữ cái
Gợi ý:
• Dùng Map<String, String>
• TreeMap nếu muốn sắp xếp theo alphabet

* Ý tưởng thiết kế

- Dùng Map<String, String>

    + Key: từ tiếng Anh
    + Value: nghĩa tiếng Việt

- Dùng TreeMap để:

    + Tự động sắp xếp theo bảng chữ cái (alphabet).

- Chương trình chạy dạng menu:

    + Thêm từ
    + Tra từ
    + Cập nhật nghĩa
    + Hiển thị toàn bộ từ điển
    + Thoát

*/



import static function.KeyBoard.readString;

import java.util.Map;
import java.util.TreeMap;

public class B5 {

    static Map<String, String> tuDien = new TreeMap<>(); 
    // 1. Thêm từ vựng
    public static void  themTu(){
        String eng = readString("Nhap tu tieng anh: ");
        if(tuDien.containsKey(eng)){
            System.out.println("Tu nay da ton tai");
        }else{
            String tv = readString("Nhap nghia tieng viet: ");
            tuDien.put(eng, tv);
            System.out.println("Da them");
        }
    }
    // 2. Tra tu dien
    public static void traTu(){
        String eng = readString("Nhap tu tieng anh can tra: ");
        if(tuDien.containsKey(eng)){
            System.out.println("Nghia: "+ tuDien.get(eng));
        }else{
            System.out.println("Khong tim thay!");
        }
    }
    // 3. Cap nhat nghia

    public static void capNhapTu(){
        String eng = readString("Nhap tu tieng anh can cap nhat: ");
        
        if(tuDien.containsKey(eng)){
            String viet = readString("Nhap nghia moi: ");
            tuDien.put(eng, viet);
            System.out.println("Cap nhat thanh cong");

        }else{
            System.out.println("Tu ko ton tai!");
        }
    }
    // 4. Hien thi tu dien
    public static void hienThi() {
        System.out.println("----- TU DIEN ANH - VIET -----");
        System.out.printf("%-20s %-30s\n", "Tieng Anh", "Tieng Viet");
        for (String key : tuDien.keySet()) {
            System.out.printf("%-20s %-30s\n", key, tuDien.get(key));
        }
    }

    // Menu
    public static void menu() {
        System.out.println("\n===== MENU =====");
        System.out.println("1. Them tu vung");
        System.out.println("2. Tra tu");
        System.out.println("3. Cap nhat nghia");
        System.out.println("4. Hien thi tu dien");
        System.out.println("0. Thoat");
        System.out.print("Chon: ");
    }


    public static void main(String[] args) {
        int choice;
        
        do {
            menu();
            choice = readInt("");

            switch (choice) {
                case 1:
                    themTu();
                    break;
                case 2: 
                    traTu();
                    break;
                case 3: 
                    capNhapTu();
                    break;
                case 4: 
                    hienThi();
                    break;
                case 0:
                    System.out.println("Ket thuc chuong trinh");
                    break;
                default:
                    System.out.println("Lua chon ko hop le");
            }

        } while (choice!=0);
    }
}
