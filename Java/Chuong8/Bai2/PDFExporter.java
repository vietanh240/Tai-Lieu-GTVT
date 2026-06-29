package Bai2;

import java.io.*;
import java.text.*;
import java.util.*;


public class PDFExporter {

    private static final NumberFormat FMT = new DecimalFormat("#,###");
    public static String export(int idHD, List<GioHangItem> cart,
                                 double tongTien, String outputPath) throws Exception {
        try {
            Class.forName("com.itextpdf.text.Document");
            return exportWithIText(idHD, cart, tongTien, outputPath);
        } catch (ClassNotFoundException e) {
            // iText chưa được thêm → xuất HTML thay thế
            String htmlPath = outputPath.replace(".pdf", ".html");
            exportHTML(idHD, cart, tongTien, htmlPath);
            return htmlPath;
        }
    }

    private static String exportWithIText(int idHD, List<GioHangItem> cart,
                                           double tongTien, String path) throws Exception {
        Class<?> docClass  = Class.forName("com.itextpdf.text.Document");
        Class<?> writerCls = Class.forName("com.itextpdf.text.pdf.PdfWriter");
        Class<?> paraClass = Class.forName("com.itextpdf.text.Paragraph");
        Class<?> fontClass = Class.forName("com.itextpdf.text.Font");
        Class<?> bfClass   = Class.forName("com.itextpdf.text.pdf.BaseFont");
        Class<?> tableClass= Class.forName("com.itextpdf.text.pdf.PdfPTable");
        Class<?> cellClass = Class.forName("com.itextpdf.text.pdf.PdfPCell");
        Class<?> colorClass= Class.forName("com.itextpdf.text.BaseColor");
        Class<?> elemClass = Class.forName("com.itextpdf.text.Element");

        Object bf = bfClass.getMethod("createFont", String.class, String.class, boolean.class)
            .invoke(null, bfClass.getField("HELVETICA").get(null),
                         bfClass.getField("WINANSI").get(null), false);

        Object fTitle = fontClass.getConstructor(bfClass, float.class, int.class)
            .newInstance(bf, 18f, fontClass.getField("BOLD").getInt(null));
        Object fHead  = fontClass.getConstructor(bfClass, float.class, int.class)
            .newInstance(bf, 11f, fontClass.getField("BOLD").getInt(null));
        Object fNorm  = fontClass.getConstructor(bfClass, float.class, int.class)
            .newInstance(bf, 10f, fontClass.getField("NORMAL").getInt(null));

        Object doc = docClass.newInstance();
        FileOutputStream fos = new FileOutputStream(path);
        writerCls.getMethod("getInstance", docClass, OutputStream.class).invoke(null, doc, fos);
        docClass.getMethod("open").invoke(doc);

        java.util.function.BiConsumer<String, Object> addPara = (text, font) -> {
            try {
                Object p = paraClass.getConstructor(String.class, fontClass).newInstance(text, font);
                docClass.getMethod("add", Class.forName("com.itextpdf.text.Element")).invoke(doc, p);
            } catch (Exception ignored) {}
        };

        addPara.accept("HOA DON BAN HANG", fTitle);
        addPara.accept("Ma HD: #" + idHD, fNorm);
        addPara.accept("Ngay: " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), fNorm);
        addPara.accept(" ", fNorm);

        Object table = tableClass.getConstructor(int.class).newInstance(4);
        tableClass.getMethod("setWidthPercentage", float.class).invoke(table, 100f);
        tableClass.getMethod("setWidths", float[].class).invoke(table, new float[]{5f, 40f, 20f, 20f});

        Object blue = colorClass.getConstructor(int.class, int.class, int.class)
            .newInstance(37, 99, 235);
        Object white= colorClass.getConstructor(int.class, int.class, int.class)
            .newInstance(255, 255, 255);

        String[] headers = {"STT", "San pham", "Don gia", "Thanh tien"};
        for (String h : headers) {
            Object cell = cellClass.getConstructor(paraClass)
                .newInstance(paraClass.getConstructor(String.class, fontClass).newInstance(h, fHead));
            cellClass.getMethod("setBackgroundColor", colorClass).invoke(cell, blue);
            tableClass.getMethod("addCell", cellClass).invoke(table, cell);
        }

        Object grey = colorClass.getConstructor(int.class, int.class, int.class)
            .newInstance(248, 250, 252);
        int stt = 1;
        for (GioHangItem item : cart) {
            boolean alt = (stt % 2 == 0);
            String[] cols = {
                String.valueOf(stt++),
                item.getSanPham().getTenSP() + " x" + item.getSoLuong(),
                FMT.format(item.getSanPham().getDonGia()) + "d",
                FMT.format(item.getThanhTien()) + "d"
            };
            for (String col : cols) {
                Object cell = cellClass.getConstructor(paraClass)
                    .newInstance(paraClass.getConstructor(String.class, fontClass).newInstance(col, fNorm));
                if (alt) cellClass.getMethod("setBackgroundColor", colorClass).invoke(cell, grey);
                tableClass.getMethod("addCell", cellClass).invoke(table, cell);
            }
        }

        docClass.getMethod("add", Class.forName("com.itextpdf.text.Element")).invoke(doc, table);
        addPara.accept(" ", fNorm);
        addPara.accept("TONG TIEN: " + FMT.format(tongTien) + " VND", fHead);
        addPara.accept("Cam on quy khach!", fNorm);

        docClass.getMethod("close").invoke(doc);
        fos.close();
        return path;
    }

    public static void exportHTML(int idHD, List<GioHangItem> cart,
                                   double tongTien, String path) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html><html><head><meta charset='UTF-8'>")
          .append("<title>Hoa Don #").append(idHD).append("</title>")
          .append("<style>")
          .append("body{font-family:Arial,sans-serif;max-width:700px;margin:40px auto;color:#1e293b}")
          .append("h1{text-align:center;color:#2563eb;margin-bottom:4px}")
          .append(".meta{text-align:center;color:#64748b;font-size:13px;margin-bottom:20px}")
          .append("table{width:100%;border-collapse:collapse;margin-top:16px}")
          .append("th{background:#2563eb;color:white;padding:10px 12px;text-align:left}")
          .append("td{padding:9px 12px;border-bottom:1px solid #e2e8f0}")
          .append("tr:nth-child(even){background:#f8fafc}")
          .append(".total{text-align:right;font-size:16px;font-weight:bold;color:#2563eb;margin-top:16px}")
          .append(".footer{text-align:center;color:#94a3b8;font-size:12px;margin-top:30px}")
          .append("@media print{button{display:none}}")
          .append("button{display:block;margin:20px auto;padding:10px 28px;")
          .append("background:#2563eb;color:white;border:none;border-radius:6px;")
          .append("font-size:14px;cursor:pointer}")
          .append("</style></head><body>")
          .append("<h1>&#127Receipt; HÓA ĐƠN BÁN HÀNG</h1>")
          .append("<div class='meta'>Mã HD: <b>#").append(idHD).append("</b> &nbsp;|&nbsp; Ngày: <b>")
          .append(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date())).append("</b></div>")
          .append("<table><tr><th>STT</th><th>Sản phẩm</th><th>SL</th><th>Đơn giá</th><th>Thành tiền</th></tr>");

        int stt = 1;
        for (GioHangItem item : cart) {
            sb.append("<tr>")
              .append("<td>").append(stt++).append("</td>")
              .append("<td>").append(item.getSanPham().getTenSP()).append("</td>")
              .append("<td>").append(item.getSoLuong()).append("</td>")
              .append("<td>").append(FMT.format(item.getSanPham().getDonGia())).append(" ₫</td>")
              .append("<td><b>").append(FMT.format(item.getThanhTien())).append(" ₫</b></td>")
              .append("</tr>");
        }
        sb.append("</table>")
          .append("<div class='total'>TỔNG TIỀN: ").append(FMT.format(tongTien)).append(" VNĐ</div>")
          .append("<button onclick='window.print()'>🖨️ In hóa đơn</button>")
          .append("<div class='footer'>Cảm ơn quý khách! Hẹn gặp lại.</div>")
          .append("</body></html>");

        try (PrintWriter pw = new PrintWriter(new FileWriter(path, false))) {
            pw.print(sb);
        }
    }
}
