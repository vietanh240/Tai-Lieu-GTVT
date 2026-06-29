package Bai2;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.text.*;
import java.util.*;
import java.util.List;

public class QuanLyBanHangForm extends JFrame {

    private static final Color C_BG      = new Color(248, 250, 252);
    private static final Color C_SIDE    = new Color(15,  23,  42);
    private static final Color C_WHITE   = Color.WHITE;
    private static final Color C_BORDER  = new Color(226, 232, 240);
    private static final Color C_BLUE    = new Color(37,  99,  235);
    private static final Color C_BLUE_DK = new Color(29,  78,  216);
    private static final Color C_GREEN   = new Color(22,  163,  74);
    private static final Color C_RED     = new Color(220,  38,  38);
    private static final Color C_ORANGE  = new Color(234, 88,   12);
    private static final Color C_TEXT    = new Color(15,  23,  42);
    private static final Color C_MUTED   = new Color(100, 116, 140);
    private static final Color C_ROW_ALT = new Color(248, 250, 252);
    private static final Color C_ROW_SEL = new Color(219, 234, 254);

    private static final Font F_TITLE = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font F_BOLD  = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font F_NORM  = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font F_SMALL = new Font("Segoe UI", Font.PLAIN, 11);
    private static final Font F_BTN   = new Font("Segoe UI", Font.BOLD, 12);

    private static final NumberFormat FMT = new DecimalFormat("#,###");

    private final SanPhamDAO dao = new SanPhamDAO();
    private final List<GioHangItem> cart = new ArrayList<>();

    private DefaultTableModel modelSP, modelCart, modelHD;
    private JTable tableSP, tableCart, tableHD;
    private JTextField txtSearch;
    private JLabel lblTongTien, lblStatus;
    private JSpinner spnSoLuong;
    private JTabbedPane tabs;

    public QuanLyBanHangForm() {
        setTitle("🛒 Quản Lý Bán Hàng Mini");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(buildSidebar(),   BorderLayout.WEST);
        add(buildContent(),   BorderLayout.CENTER);
        add(buildStatusBar(), BorderLayout.SOUTH);

        loadSanPham(dao.getAll());
        loadHoaDon();
        setVisible(true);
    }

    private JPanel buildSidebar() {
        JPanel p = new JPanel();
        p.setBackground(C_SIDE);
        p.setPreferredSize(new Dimension(210, 0));
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        JPanel logo = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 18));
        logo.setBackground(C_SIDE);
        logo.setMaximumSize(new Dimension(210, 64));
        JLabel ico = new JLabel("🛒");
        ico.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 26));
        JLabel nm  = new JLabel("<html><b style='color:white;font-size:12px'>Bán Hàng Mini</b><br>" +
                                "<span style='color:#64748b;font-size:10px'>XAMPP • MySQL</span></html>");
        logo.add(ico); logo.add(nm);
        p.add(logo);

        p.add(sideDiv());

        p.add(sideInfo("📦", "Sản phẩm",  String.valueOf(dao.getAll().size())));
        p.add(sideInfo("🧾", "Hóa đơn",  String.valueOf(dao.getDanhSachHoaDon().size())));
        p.add(sideInfo("🛒", "Giỏ hàng", "0 mặt hàng"));

        p.add(sideDiv());
        p.add(Box.createVerticalGlue());

        return p;
    }

    private JSeparator sideDiv() {
        JSeparator s = new JSeparator();
        s.setForeground(new Color(30, 41, 59));
        s.setMaximumSize(new Dimension(210, 1));
        return s;
    }

    private JPanel sideInfo(String icon, String label, String value) {
        JPanel p = new JPanel(new BorderLayout(8, 0));
        p.setBackground(C_SIDE);
        p.setMaximumSize(new Dimension(210, 52));
        p.setBorder(new EmptyBorder(8, 14, 8, 14));

        JLabel ico = new JLabel(icon);
        ico.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));

        JPanel txt = new JPanel(new GridLayout(2, 1));
        txt.setBackground(C_SIDE);
        JLabel lbl = new JLabel(label);
        lbl.setFont(F_SMALL);
        lbl.setForeground(new Color(100, 116, 140));
        JLabel val = new JLabel(value);
        val.setFont(new Font("Segoe UI", Font.BOLD, 13));
        val.setForeground(Color.WHITE);
        txt.add(lbl); txt.add(val);

        p.add(ico,  BorderLayout.WEST);
        p.add(txt,  BorderLayout.CENTER);
        return p;
    }

    private JTabbedPane buildContent() {
        tabs = new JTabbedPane();
        tabs.setFont(F_BOLD);
        tabs.setBackground(C_BG);

        tabs.addTab("Bán Hàng",  buildTabBanHang());
        tabs.addTab("Hóa Đơn",   buildTabHoaDon());

        return tabs;
    }

    private JPanel buildTabBanHang() {
        JPanel root = new JPanel(new GridLayout(1, 2, 10, 0));
        root.setBackground(C_BG);
        root.setBorder(new EmptyBorder(12, 12, 12, 12));

        root.add(buildPanelSanPham());
        root.add(buildPanelGioHang());
        return root;
    }

    private JPanel buildPanelSanPham() {
        JPanel card = card();
        card.setLayout(new BorderLayout(0, 8));
        card.setBorder(new CompoundBorder(card.getBorder(), new EmptyBorder(12, 12, 12, 12)));

        JLabel title = new JLabel("Danh Sách Sản Phẩm");
        title.setFont(F_BOLD);
        title.setForeground(C_TEXT);

        JPanel searchRow = new JPanel(new BorderLayout(6, 0));
        searchRow.setBackground(C_WHITE);
        txtSearch = styledField("Tìm sản phẩm...", 200);
        JButton btnSearch = btn("Tìm", C_BLUE);
        JButton btnReset  = btn("↺", new Color(71, 85, 105));
        btnSearch.addActionListener(e -> loadSanPham(dao.search(txtSearch.getText().trim())));
        btnReset.addActionListener(e -> { txtSearch.setText(""); loadSanPham(dao.getAll()); });
        txtSearch.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) { if (e.getKeyCode() == KeyEvent.VK_ENTER) loadSanPham(dao.search(txtSearch.getText().trim())); }
        });
        searchRow.add(txtSearch,  BorderLayout.CENTER);
        searchRow.add(btnSearch,  BorderLayout.EAST);

        JPanel topRow = new JPanel(new BorderLayout(6, 4));
        topRow.setBackground(C_WHITE);
        topRow.add(title,     BorderLayout.NORTH);
        topRow.add(searchRow, BorderLayout.CENTER);

        modelSP = new DefaultTableModel(new String[]{"ID", "Tên sản phẩm", "Đơn giá", "Tồn kho"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tableSP = styledTable(modelSP);
        tableSP.getColumnModel().getColumn(0).setPreferredWidth(40);
        tableSP.getColumnModel().getColumn(1).setPreferredWidth(200);
        tableSP.getColumnModel().getColumn(2).setPreferredWidth(100);
        tableSP.getColumnModel().getColumn(3).setPreferredWidth(70);

        JScrollPane scroll = new JScrollPane(tableSP);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        JPanel addRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        addRow.setBackground(C_WHITE);
        addRow.add(new JLabel("Số lượng:"));
        spnSoLuong = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
        spnSoLuong.setPreferredSize(new Dimension(70, 28));
        JButton btnAdd = btn("Thêm vào giỏ", C_GREEN);
        btnAdd.addActionListener(e -> themVaoGio());
        addRow.add(spnSoLuong);
        addRow.add(btnAdd);

        card.add(topRow, BorderLayout.NORTH);
        card.add(scroll,  BorderLayout.CENTER);
        card.add(addRow,  BorderLayout.SOUTH);
        return card;
    }

    private JPanel buildPanelGioHang() {
        JPanel card = card();
        card.setLayout(new BorderLayout(0, 8));
        card.setBorder(new CompoundBorder(card.getBorder(), new EmptyBorder(12, 12, 12, 12)));

        JLabel title = new JLabel("Giỏ Hàng");
        title.setFont(F_BOLD);
        title.setForeground(C_TEXT);

        modelCart = new DefaultTableModel(new String[]{"Tên SP", "SL", "Đơn giá", "Thành tiền"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tableCart = styledTable(modelCart);
        tableCart.getColumnModel().getColumn(0).setPreferredWidth(160);
        tableCart.getColumnModel().getColumn(1).setPreferredWidth(40);
        tableCart.getColumnModel().getColumn(2).setPreferredWidth(90);
        tableCart.getColumnModel().getColumn(3).setPreferredWidth(100);

        JScrollPane scroll = new JScrollPane(tableCart);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        lblTongTien = new JLabel("TỔNG TIỀN: 0 VNĐ");
        lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTongTien.setForeground(C_BLUE);
        lblTongTien.setHorizontalAlignment(SwingConstants.RIGHT);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
        btnRow.setBackground(C_WHITE);
        JButton btnXoa      = btn("Xóa mục",   C_RED);
        JButton btnXoaAll   = btn("Xóa giỏ",    new Color(71, 85, 105));
        JButton btnThanhToan= btn("Thanh Toán", C_BLUE);

        btnXoa.addActionListener(e -> xoaMucGio());
        btnXoaAll.addActionListener(e -> { cart.clear(); refreshCart(); });
        btnThanhToan.addActionListener(e -> thanhToan());

        btnRow.add(btnXoa);
        btnRow.add(btnXoaAll);
        btnRow.add(btnThanhToan);

        JPanel south = new JPanel(new BorderLayout());
        south.setBackground(C_WHITE);
        south.add(lblTongTien, BorderLayout.NORTH);
        south.add(btnRow,      BorderLayout.SOUTH);

        card.add(title,  BorderLayout.NORTH);
        card.add(scroll, BorderLayout.CENTER);
        card.add(south,  BorderLayout.SOUTH);
        return card;
    }

    private JPanel buildTabHoaDon() {
        JPanel card = card();
        card.setLayout(new BorderLayout(0, 8));
        card.setBorder(new CompoundBorder(card.getBorder(), new EmptyBorder(12, 12, 12, 12)));

        JLabel title = new JLabel("Danh Sách Hóa Đơn Gần Đây");
        title.setFont(F_BOLD);

        modelHD = new DefaultTableModel(new String[]{"Mã HD", "Ngày lập", "Số mặt hàng", "Tổng tiền"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tableHD = styledTable(modelHD);
        tableHD.getColumnModel().getColumn(0).setPreferredWidth(60);
        tableHD.getColumnModel().getColumn(1).setPreferredWidth(160);
        tableHD.getColumnModel().getColumn(2).setPreferredWidth(100);
        tableHD.getColumnModel().getColumn(3).setPreferredWidth(140);

        JScrollPane scroll = new JScrollPane(tableHD);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        JButton btnRefresh = btn("Làm mới", C_BLUE);
        btnRefresh.addActionListener(e -> loadHoaDon());

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(C_WHITE);
        top.add(title, BorderLayout.WEST);
        top.add(btnRefresh, BorderLayout.EAST);

        card.add(top,    BorderLayout.NORTH);
        card.add(scroll, BorderLayout.CENTER);
        return card;
    }

    private JPanel buildStatusBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(new Color(30, 41, 59));
        bar.setPreferredSize(new Dimension(0, 28));
        bar.setBorder(new EmptyBorder(0, 12, 0, 12));

        lblStatus = new JLabel("Sẵn sàng");
        lblStatus.setFont(F_SMALL);
        lblStatus.setForeground(new Color(148, 163, 184));

        JLabel hint = new JLabel("Chọn SP → Thêm vào giỏ → Thanh Toán");
        hint.setFont(F_SMALL);
        hint.setForeground(new Color(71, 85, 105));

        bar.add(lblStatus, BorderLayout.WEST);
        bar.add(hint,      BorderLayout.EAST);
        return bar;
    }

    private void themVaoGio() {
        int row = tableSP.getSelectedRow();
        if (row < 0) { status("Chọn sản phẩm muốn thêm!", C_ORANGE); return; }

        int id      = (int) modelSP.getValueAt(row, 0);
        String ten  = modelSP.getValueAt(row, 1).toString();
        double gia  = parseGia(modelSP.getValueAt(row, 2).toString());
        int    ton  = Integer.parseInt(modelSP.getValueAt(row, 3).toString());
        int    sl   = (int) spnSoLuong.getValue();

        if (sl > ton) { status("Số lượng vượt tồn kho (" + ton + ")!", C_RED); return; }

        for (GioHangItem item : cart) {
            if (item.getSanPham().getId() == id) {
                int newSl = item.getSoLuong() + sl;
                if (newSl > ton) { status("Tổng vượt tồn kho!", C_RED); return; }
                item.setSoLuong(newSl);
                refreshCart();
                status("Cập nhật giỏ: " + ten, C_GREEN);
                return;
            }
        }
        cart.add(new GioHangItem(new SanPham(id, ten, gia, ton), sl));
        refreshCart();
        status("Đã thêm: " + ten + " x" + sl, C_GREEN);
    }

    private void xoaMucGio() {
        int row = tableCart.getSelectedRow();
        if (row < 0) { status("⚠ Chọn mục muốn xóa!", C_ORANGE); return; }
        cart.remove(row);
        refreshCart();
        status("Đã xóa khỏi giỏ hàng", C_MUTED);
    }

    private void thanhToan() {
        if (cart.isEmpty()) { status("⚠ Giỏ hàng trống!", C_ORANGE); return; }

        double total = cart.stream().mapToDouble(GioHangItem::getThanhTien).sum();

        String msg = String.format(
            "<html><b>Xác nhận thanh toán?</b><br><br>" +
            "Số mặt hàng: %d<br>Tổng tiền: <b>%s VNĐ</b></html>",
            cart.size(), FMT.format(total));
        int confirm = JOptionPane.showConfirmDialog(this, msg, "Thanh Toán", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        int idHD = dao.luuHoaDon(new ArrayList<>(cart), total);
        if (idHD == -2) { status("Không đủ hàng trong kho!", C_RED); return; }
        if (idHD < 0)   { status("Lỗi lưu hóa đơn!", C_RED);         return; }

        status("Thanh toán thành công! Mã HD: #" + idHD, C_GREEN);

        int pdfConfirm = JOptionPane.showConfirmDialog(this,
            "Thanh toán thành công!\nMã hóa đơn: #" + idHD +
            "\n\nBạn có muốn xuất hóa đơn không?",
            "Thành Công", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

        List<GioHangItem> savedCart = new ArrayList<>(cart);

        cart.clear();
        refreshCart();
        loadSanPham(dao.getAll()); 
        loadHoaDon();

        if (pdfConfirm == JOptionPane.YES_OPTION) {
            xuatHoaDon(idHD, savedCart, total);
        }
    }

    private void xuatHoaDon(int idHD, List<GioHangItem> savedCart, double total) {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Lưu hóa đơn");
        fc.setSelectedFile(new File("HoaDon_" + idHD + ".html"));
        if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

        String path = fc.getSelectedFile().getAbsolutePath();
        if (!path.endsWith(".html") && !path.endsWith(".pdf")) path += ".html";

        try {
            String out = PDFExporter.export(idHD, savedCart, total, path.replace(".html", ".pdf"));
            status("Đã xuất hóa đơn: " + out, C_GREEN);
            // Mở file
            if (Desktop.isDesktopSupported()) Desktop.getDesktop().open(new File(out));
        } catch (Exception ex) {
            status("Lỗi xuất file: " + ex.getMessage(), C_RED);
        }
    }


    private void loadSanPham(List<SanPham> list) {
        modelSP.setRowCount(0);
        for (SanPham sp : list) {
            modelSP.addRow(new Object[]{
                sp.getId(), sp.getTenSP(),
                FMT.format(sp.getDonGia()) + " ₫",
                sp.getSoLuong()
            });
        }
        status("📦 " + list.size() + " sản phẩm", C_MUTED);
    }

    private void refreshCart() {
        modelCart.setRowCount(0);
        double total = 0;
        for (GioHangItem item : cart) {
            modelCart.addRow(new Object[]{
                item.getSanPham().getTenSP(),
                item.getSoLuong(),
                FMT.format(item.getSanPham().getDonGia()) + " ₫",
                FMT.format(item.getThanhTien()) + " ₫"
            });
            total += item.getThanhTien();
        }
        lblTongTien.setText("TỔNG TIỀN: " + FMT.format(total) + " VNĐ");
    }

    private void loadHoaDon() {
        modelHD.setRowCount(0);
        for (String[] row : dao.getDanhSachHoaDon()) {
            modelHD.addRow(new Object[]{
                "#" + row[0], row[1], row[3] + " mặt hàng",
                FMT.format(Double.parseDouble(row[2])) + " ₫"
            });
        }
    }

    private void status(String msg, Color color) {
        lblStatus.setText(msg);
        lblStatus.setForeground(color);
    }

    private JPanel card() {
        JPanel p = new JPanel();
        p.setBackground(C_WHITE);
        p.setBorder(new LineBorder(C_BORDER, 1, true));
        return p;
    }

    private JTextField styledField(String hint, int w) {
        JTextField f = new JTextField();
        f.setFont(F_NORM);
        f.setForeground(C_MUTED);
        f.setText(hint);
        f.setBorder(new CompoundBorder(new LineBorder(C_BORDER, 1, true), new EmptyBorder(5, 8, 5, 8)));
        f.setPreferredSize(new Dimension(w, 32));
        f.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { if (f.getText().equals(hint)) { f.setText(""); f.setForeground(C_TEXT); } }
            public void focusLost(FocusEvent e)   { if (f.getText().isEmpty())    { f.setText(hint); f.setForeground(C_MUTED); } }
        });
        return f;
    }

    private JButton btn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setFont(F_BTN);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(6, 14, 6, 14));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { b.setBackground(bg.darker()); }
            public void mouseExited(MouseEvent e)  { b.setBackground(bg); }
        });
        return b;
    }

    private JTable styledTable(DefaultTableModel model) {
        JTable t = new JTable(model);
        t.setFont(F_NORM);
        t.setRowHeight(34);
        t.setShowGrid(false);
        t.setIntercellSpacing(new Dimension(0, 0));
        t.setSelectionBackground(C_ROW_SEL);
        t.setSelectionForeground(C_TEXT);
        t.setBackground(C_WHITE);
        t.setFillsViewportHeight(true);

        JTableHeader h = t.getTableHeader();
        h.setFont(new Font("Segoe UI", Font.BOLD, 12));
        h.setBackground(new Color(241, 245, 249));
        h.setForeground(C_MUTED);
        h.setBorder(new MatteBorder(0, 0, 2, 0, C_BORDER));
        h.setPreferredSize(new Dimension(0, 36));
        h.setReorderingAllowed(false);

        t.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable tb, Object v, boolean s, boolean f, int r, int c) {
                super.getTableCellRendererComponent(tb, v, s, f, r, c);
                setFont(F_NORM);
                setBorder(new EmptyBorder(0, 10, 0, 10));
                if (s) { setBackground(C_ROW_SEL); setForeground(C_TEXT); }
                else   { setBackground(r % 2 == 0 ? C_WHITE : C_ROW_ALT); setForeground(C_TEXT); }
                return this;
            }
        });
        return t;
    }

    private double parseGia(String s) {
        try { return Double.parseDouble(s.replaceAll("[^0-9.]", "")); }
        catch (Exception e) { return 0; }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new QuanLyBanHangForm().setVisible(true);
        });
    }
}
