package Bai1;

public class SinhVien {
    private int    id;
    private String hoTen;
    private String lop;
    private double diemTB;
    private String email;

    public SinhVien() {}

    public SinhVien(int id, String hoTen, String lop, double diemTB, String email) {
        this.id     = id;
        this.hoTen  = hoTen;
        this.lop    = lop;
        this.diemTB = diemTB;
        this.email  = email;
    }

    public int    getId()      { return id; }
    public String getHoTen()   { return hoTen; }
    public String getLop()     { return lop; }
    public double getDiemTB()  { return diemTB; }
    public String getEmail()   { return email; }

    public void setId(int id)           { this.id = id; }
    public void setHoTen(String hoTen)  { this.hoTen = hoTen; }
    public void setLop(String lop)      { this.lop = lop; }
    public void setDiemTB(double d)     { this.diemTB = d; }
    public void setEmail(String email)  { this.email = email; }

    @Override
    public String toString() {
        return hoTen + " [" + lop + "]";
    }
}
