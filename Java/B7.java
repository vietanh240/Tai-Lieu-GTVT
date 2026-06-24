package BaiTap;
/*
Bài 7: Viết chương trình mô phỏng lấy số thứ tự khám bệnh.
• Người dùng lấy số thứ tự để được phục vụ
• Hệ thống xử lý khách hàng theo thứ tự
• Cho biết ai sẽ được phục vụ tiếp theo
Gợi ý:
• Dùng Queue<String> (LinkedList)
• Menu gồm: thêm, xem tiếp theo, phục vụ
*/

import static function.KeyBoard.readInt;
import static function.KeyBoard.readString;

import java.util.LinkedList;
import java.util.Queue;

public class B7 {
    public static void main(String[] args) {
        Queue<String> queue = new LinkedList<>();
        int chon;
        do{
            System.out.println("\n HE THONG LAY SO THU TU ");
            System.out.println("1. Them nguoi lay so");
            System.out.println("2. Xem nguoi tiep theo");
            System.out.println("3. Phuc vu nguoi tiep theo");
            System.out.println("0. Thoat");
            chon = readInt("Chon: ");
            
            switch (chon) {
                case 1:
                    String ten = readString("Nhap ten: ");
                    queue.offer(ten);
                    System.out.println("Da them "+ ten+" vao hang cho.");
                    break;
                case 2: 
                    if(queue.isEmpty()){
                        System.out.println("Khong co ai trong hang cho.");
                    }else{
                        System.out.println("Nguoi tiep theo la: "+ queue.peek());
                    }
                    break;
                case 3: 
                    if(queue.isEmpty()){
                        System.out.println("Khong co ai de phuc vu.");
                    }else{
                        System.out.println("Dang phuc vu: "+ queue.poll());
                    }
                    break;
                case 0: 
                    System.out.println("Thoat chuong trinhh.");
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ.");
            }
        } while(chon != 0);
    }
}
