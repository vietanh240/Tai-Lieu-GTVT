package BaiTap;

import static function.KeyBoard.readDouble;
import static function.KeyBoard.readString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class QuanLySinhVien {
    private List<Student> ds = new ArrayList<>();
    
    // Them SV
    public void themSV(){
        String ma = readString("Ma SV: ");
        String ten = readString("Ten: ");
        String gt = readString("Gioi tinh: ");
        double cc = readDouble("Diem chuyen can: ");
        double kt = readDouble("Diem kiem tra: ");
        double btl = readDouble("Diem BTL: ");
        double thi = readDouble("Diem thi: ");

        ds.add(new Student(ma, ten, gt, cc, kt, btl, btl, thi));
        System.out.println("Da them");
    }
    // Xuat danh sach

    public void xuatDS(){
        System.out.printf(
            "%-10s %-20s %-8s %6s %6s %6s %6s %6s %6s%n",
            "MaSV", "HoTen", "GT", "CC", "KT", "BTL", "QT", "Thi", "KTHP"
        );
        for (Student s : ds) {
            s.xuat();
        }
    }

    //Tim sinh vien theo ma

    public void timSV(){
        String ma = readString("Nhap ma SV can tim: ");
        boolean found = false;

        for (Student s : ds) {
            if(s.getMaSV().equalsIgnoreCase(ma)){
                xuatDS();
                found = true;
                break;
            }
        }

        if(!found){
            System.out.println("Ko tim thay");
        }
    }

    //Xap sep theo diem giam dan

    public void sapXepGiamDan(){
        Collections.sort(ds, new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2){
                return Double.compare(o2.getDiemKTHP(), o1.getDiemKTHP());
            }
        });
        System.out.println("Da sap xep");
    }
    
}
