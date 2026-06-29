package Bai3;

public class CauHoi {
    private int    id;
    private String noiDung;
    private String a, b, c, d;
    private String dapAn;

    public CauHoi() {}

    public CauHoi(int id, String noiDung, String a, String b, String c, String d, String dapAn) {
        this.id      = id;
        this.noiDung = noiDung;
        this.a       = a;
        this.b       = b;
        this.c       = c;
        this.d       = d;
        this.dapAn   = dapAn;
    }

    public int    getId()      { return id; }
    public String getNoiDung() { return noiDung; }
    public String getA()       { return a; }
    public String getB()       { return b; }
    public String getC()       { return c; }
    public String getD()       { return d; }
    public String getDapAn()   { return dapAn; }
}
