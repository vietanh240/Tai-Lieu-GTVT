package BaiTap;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;

class YeuCau {
    private String id;
    private String moTa;
    private int mucDoUuTien;

    public YeuCau(String id, String moTa, int mucDoUuTien) {
        this.id = id;
        this.moTa = moTa;
        this.mucDoUuTien = mucDoUuTien;
    }

    public int getMucDoUuTien() {
        return mucDoUuTien;
    }

    @Override
    public String toString() {
        return "ID: " + id +
               " | Mo ta: " + moTa +
               " | uu tien: " + mucDoUuTien;
    }
}

public class B8 {
    public static void main(String[] args) {

        // Comparator: ưu tiên cao xử lý trước
        PriorityQueue<YeuCau> hangDoi = new PriorityQueue<>(
            new Comparator<YeuCau>() {
                @Override
                public int compare(YeuCau yc1, YeuCau yc2) {
                    return yc2.getMucDoUuTien() - yc1.getMucDoUuTien();
                }
            }
        );

        Scanner sc = new Scanner(System.in);

        System.out.print("Nhap so luong yeu cau: ");
        int n = sc.nextInt();
        sc.nextLine();

        for (int i = 0; i < n; i++) {
            System.out.println("\nNhap yeu cau thu: " + (i + 1));

            System.out.print("ID: ");
            String id = sc.nextLine();

            System.out.print("Mo ta: ");
            String moTa = sc.nextLine();

            System.out.print("Muc do uu tien (lon hon la cao hon): ");
            int mucDo = sc.nextInt();
            sc.nextLine();

            YeuCau yc = new YeuCau(id, moTa, mucDo);
            hangDoi.add(yc);
        }

        System.out.println("\n--- Thu tu xu ly yeu cau ---");

        while (!hangDoi.isEmpty()) {
            System.out.println(hangDoi.poll());
        }

        sc.close();
    }
}
