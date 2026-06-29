package Bai5;

public class DocGia {
    private int    id;
    private String hoTen, ngaySinh;

    public DocGia() {}
    public DocGia(int id, String hoTen, String ngaySinh) {
        this.id = id; this.hoTen = hoTen; this.ngaySinh = ngaySinh;
    }

    public int    getId()       { return id; }
    public String getHoTen()    { return hoTen; }
    public String getNgaySinh() { return ngaySinh; }

    public void setId(int v)          { id = v; }
    public void setHoTen(String v)    { hoTen = v; }
    public void setNgaySinh(String v) { ngaySinh = v; }

    @Override public String toString() { return "[" + id + "] " + hoTen; }
}
