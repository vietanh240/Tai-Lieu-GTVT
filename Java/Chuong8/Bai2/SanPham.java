package Bai2;

public class SanPham {
    private int    id;
    private String tenSP;
    private double donGia;
    private int    soLuong;

    public SanPham() {}

    public SanPham(int id, String tenSP, double donGia, int soLuong) {
        this.id      = id;
        this.tenSP   = tenSP;
        this.donGia  = donGia;
        this.soLuong = soLuong;
    }

    public int    getId()       { return id; }
    public String getTenSP()    { return tenSP; }
    public double getDonGia()   { return donGia; }
    public int    getSoLuong()  { return soLuong; }

    public void setId(int id)             { this.id = id; }
    public void setTenSP(String tenSP)    { this.tenSP = tenSP; }
    public void setDonGia(double donGia)  { this.donGia = donGia; }
    public void setSoLuong(int soLuong)   { this.soLuong = soLuong; }

    @Override
    public String toString() { return tenSP; }
}
