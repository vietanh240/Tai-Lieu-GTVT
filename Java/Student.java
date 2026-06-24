package BaiTap;

public class Student {
    private String maSV, hoTen, gioiTinh;
    private double diemCC, diemKT, diemBTL, diemQT, diemThi, diemKTHP;
   

    public Student(String maSV, String hoTen, String gioiTinh, double diemCC, double diemKT, double diemBTL,double diemQT, double diemThi) {      
        this.maSV = maSV;
        this.hoTen = hoTen;
        this.gioiTinh = gioiTinh;
        this.diemCC = diemCC;
        this.diemKT = diemKT;
        this.diemBTL = diemBTL;
        this.diemQT = diemQT;
        this.diemThi = diemThi;
        tinhDiem();
    }

    public void tinhDiem(){
        diemQT = (diemCC + 2 * diemKT + 3 * diemBTL)/6;
        diemKTHP = (diemQT+diemThi)/2;
    }

    public String getMaSV() {
        return maSV;
    }

    public double getDiemKTHP() {
        return diemKTHP;
    }

    public void xuat(){
        System.out.printf("%-10s %-20s %-8s %6.1f %6.1f %6.1f %6.2f %6.1f %6.2f%n",
            maSV, hoTen, gioiTinh,
            diemCC, diemKT, diemBTL, diemQT, diemThi, diemKTHP);
    }
    
}
