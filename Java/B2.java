package BaiTap;

/*
Bài 2: Viết chương trình quản lý danh sách sinh viên gồm: mã sinh viên, họ tên, giới tính, 
điểm chuyên cần, điểm kiểm tra, điểm bài tập lớn, điểm quá trình, điểm thi, điểm kết thúc 
học phần.
Thực hiện các chức năng:
• Thêm sinh viên
• Hiển thị danh sách sinh viên theo dạng bảng
• Tìm sinh viên theo mã
• Tính điểm quá trình = (điểm CC + 2*điểm KT + 3*điểm BTL)/6, điểm kết thúc 
học phần = (điểm quá trình + điểm thi)/2
• Sắp xếp theo điểm giảm dần
• Viết class main có tạo giao diện menu để thực hiện các chức năng.
Gợi ý:
• Tạo lớp Student
• Dùng List<Student>
• Sử dụng Collections.sort() với Comparator
*/


import static function.KeyBoard.readInt;
public class B2 {
    public static void main(String[] args) {
        QuanLySinhVien ql = new QuanLySinhVien();
        int chon;

        do{
            System.out.println("\n===== MENU =====");
            System.out.println("1. Them SV");
            System.out.println("2. Xuat danh sach");
            System.out.println("3. Tim sinh vien theo ma");
            System.out.println("4. Sap xep giam dan theo diem");
            System.out.println("0. Thoat");
            

            chon = readInt("Chon: ");

            switch (chon) {
                case 1:
                    ql.themSV();
                    break;
                case 2: 
                    ql.xuatDS();
                    break;
                case 3: 
                    ql.timSV();
                    break;
                case 4: 
                    ql.sapXepGiamDan();
                    break;
                case 0: 
                    System.out.println("Ket thuc");
                    break;
                default:
                    System.out.println("Ko co lua chon");
                    
            }
        }while (chon!=0);



    }
}
