package Bai5;

public class MuonSach {
    private int    idMuon, idDocGia, idSach;
    private String tenDocGia, tenSach, ngayMuon, ngayTra;

    public MuonSach() {}
    public MuonSach(int idMuon, int idDocGia, int idSach,
                    String tenDocGia, String tenSach,
                    String ngayMuon, String ngayTra) {
        this.idMuon    = idMuon;  this.idDocGia = idDocGia; this.idSach   = idSach;
        this.tenDocGia = tenDocGia; this.tenSach = tenSach;
        this.ngayMuon  = ngayMuon; this.ngayTra  = ngayTra;
    }

    public int    getIdMuon()    { return idMuon; }
    public int    getIdDocGia()  { return idDocGia; }
    public int    getIdSach()    { return idSach; }
    public String getTenDocGia() { return tenDocGia; }
    public String getTenSach()   { return tenSach; }
    public String getNgayMuon()  { return ngayMuon; }
    public String getNgayTra()   { return ngayTra; }
}
