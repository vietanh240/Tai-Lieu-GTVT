package Bai5;

public class Sach {
    private int    id;
    private String tenSach, tacGia, namXB, tinhTrang;

    public Sach() {}
    public Sach(int id, String tenSach, String tacGia, String namXB, String tinhTrang) {
        this.id = id; this.tenSach = tenSach; this.tacGia = tacGia;
        this.namXB = namXB; this.tinhTrang = tinhTrang;
    }

    public int    getId()        { return id; }
    public String getTenSach()   { return tenSach; }
    public String getTacGia()    { return tacGia; }
    public String getNamXB()     { return namXB; }
    public String getTinhTrang() { return tinhTrang; }

    public void setId(int v)           { id = v; }
    public void setTenSach(String v)   { tenSach = v; }
    public void setTacGia(String v)    { tacGia = v; }
    public void setNamXB(String v)     { namXB = v; }
    public void setTinhTrang(String v) { tinhTrang = v; }

    @Override public String toString() { return "[" + id + "] " + tenSach; }
}
