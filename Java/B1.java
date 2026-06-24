package BaiTap;

/*
Bài 1: Viết chương trình nhập danh sách n số nguyên từ bàn phím. Thực hiện chức năng:
• In danh sách theo thứ tự nhập
• In danh sách tăng dần
• Tính trung bình cộng
• In ra phần tử lớn nhất, nhỏ nhất
• Xóa tất cả số chẵn
*/

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class B1 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        ArrayList<Integer> list = new ArrayList<>();

        System.out.print("Nhap so ptu: ");
        int n = sc.nextInt();
        

        for (int i = 0; i < n; i++) {
            System.out.print("Nhap ptu thu "+ (i+1)+": ");
            list.add(sc.nextInt());
            
        }

        for (int x : list) {
            System.out.print(x+" ");
        }

        // Sap xep tang dan
        ArrayList<Integer> newList = new ArrayList<>(list);
        Collections.sort(newList);

        System.out.println("\nDanh sach tang dan");
        for (int x : newList) {
            System.out.print(x+" ");
        }

        // TBC

        int sum = 0;
        
        
        for (Integer x : list) {
            sum+=x;
        }
        double tbc = (double)sum/list.size();
        System.out.println("\nTBC: "+tbc);

        //Tìm max, min
        int max = list.get(0);
        int min = list.get(0);

        for (int i = 0; i < list.size(); i++) {
            if(list.get(i) > max){
                max = list.get(i);
            }
            if (list.get(i) < min) {
                min = list.get(i);
            }
        }

        System.out.println("Max: " + max+ " Min: "+min);

        //Xoa so chan

        for (int i = 0; i < list.size(); i++) {
            if(list.get(i) %2==0){
                list.remove(i);
            }
        }
        System.out.println("List sau khi xoa so chan: "+ list);

    }
}
