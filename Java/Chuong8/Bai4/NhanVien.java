package Bai4;
public class NhanVien {
    private String maNV;
    private String tenNV;
    private String phongBan;
    private double heSoLuong;
    private double luongCoBan;

    public NhanVien() {}

    public NhanVien(String maNV, String tenNV, String phongBan,
                    double heSoLuong, double luongCoBan) {
        this.maNV       = maNV;
        this.tenNV      = tenNV;
        this.phongBan   = phongBan;
        this.heSoLuong  = heSoLuong;
        this.luongCoBan = luongCoBan;
    }

    public String getMaNV()        { return maNV; }
    public String getTenNV()       { return tenNV; }
    public String getPhongBan()    { return phongBan; }
    public double getHeSoLuong()   { return heSoLuong; }
    public double getLuongCoBan()  { return luongCoBan; }
    public double getLuongThucNhan() { return heSoLuong * luongCoBan; }

    public void setMaNV(String s)       { this.maNV = s; }
    public void setTenNV(String s)      { this.tenNV = s; }
    public void setPhongBan(String s)   { this.phongBan = s; }
    public void setHeSoLuong(double d)  { this.heSoLuong = d; }
    public void setLuongCoBan(double d) { this.luongCoBan = d; }

    @Override
    public String toString() { return tenNV + " [" + phongBan + "]"; }
}
