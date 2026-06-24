package BaiTap;

import java.util.HashMap;
import java.util.Map;

public class Student_B9 {
    private String maSV;
    private String tenSV;
    private Map<String, Double> diemMon; // Luu diem theo mon
    
    public Student_B9(String maSV, String tenSV) {
        this.maSV = maSV;
        this.tenSV = tenSV;
        this.diemMon = new HashMap<>();
    }

    // Them hoac sua diem

     public void themHoacSuaDiem(String mon, double diem) {
        diemMon.put(mon, diem); 
        // Nếu môn đã tồn tại → tự động cập nhật
    }

    // Tính điểm trung bình
    public double tinhTrungBinh() {
        if (diemMon.isEmpty()) return 0;

        double tong = 0;
        for (double diem : diemMon.values()) {
            tong += diem;
        }
        return tong / diemMon.size();
    }
    
    // Hiển thị thông tin
    public void hienThi() {
        System.out.println("Ma SV: " + maSV);
        System.out.println("Ten SV: " + tenSV);
        System.out.println("Diem cac mon: " + diemMon);
        System.out.println("Diem trung binh: " + tinhTrungBinh());
        System.out.println("-------------------------");
    }

    public String getMaSV() {
        return maSV;
    }
}
