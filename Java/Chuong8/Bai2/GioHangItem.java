package Bai2;

public class GioHangItem {
    private SanPham sanPham;
    private int     soLuong;

    public GioHangItem(SanPham sanPham, int soLuong) {
        this.sanPham = sanPham;
        this.soLuong = soLuong;
    }

    public SanPham getSanPham()          { return sanPham; }
    public int     getSoLuong()          { return soLuong; }
    public void    setSoLuong(int sl)    { this.soLuong = sl; }

    public double getThanhTien() {
        return sanPham.getDonGia() * soLuong;
    }
}
