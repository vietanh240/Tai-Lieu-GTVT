package Bai4;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.List;

public class QuanLyNhanVienForm extends JFrame {

    private final NhanVienDAO dao = new NhanVienDAO();

    private JTextField txtMaNV, txtTenNV, txtPhongBan, txtHeSo, txtLuongCB, txtSearch;
    private JLabel     lblLuongPreview;
    private JButton    btnThem, btnSua, btnXoa, btnLamMoi, btnBaoCao, btnXuat, btnLogout;
    private JTable     table;
    private DefaultTableModel tableModel;
    private JLabel     lblStatus, lblUser;
    private JComboBox<String> cbSort;

    private String selectedMaNV = null;
    private final String username;
    private final String role;

    static final Color BG      = new Color(13,  17,  23);
    static final Color PANEL   = new Color(22,  27,  34);
    static final Color CARD    = new Color(33,  38,  45);
    static final Color ACCENT  = new Color(35, 134,  54);
    static final Color ACC2    = new Color(63, 185,  80);
    static final Color INDIGO  = new Color(88, 166,255);
    static final Color DANGER  = new Color(248, 81,  73);
    static final Color WARNING = new Color(227,179,  65);
    static final Color TEXT    = new Color(230,237,243);
    static final Color SUBTEXT = new Color(139,148,158);

    private static final DecimalFormat VND = new DecimalFormat("#,###");

    public QuanLyNhanVienForm(String username, String role) {
        this.username = username;
        this.role     = role;
        setTitle("Quản Lý Nhân Viên Công Ty");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1150, 700);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(950, 600));
        buildUI();
        loadTable("maNV");
    }

    private void buildUI() {
        getContentPane().setBackground(BG);
        setLayout(new BorderLayout());
        add(buildTopBar(),    BorderLayout.NORTH);
        add(buildContent(),   BorderLayout.CENTER);
        add(buildStatusBar(), BorderLayout.SOUTH);
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(ACCENT);
        bar.setPreferredSize(new Dimension(0, 52));
        bar.setBorder(new EmptyBorder(0, 20, 0, 20));

        JLabel title = new JLabel("Quản Lý Nhân Viên Công Ty");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(TEXT);

        JPanel rightBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        rightBar.setBackground(ACCENT);
        lblUser = new JLabel("" + username + "  |  " + role.toUpperCase());
        lblUser.setForeground(new Color(210,242,210));
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnLogout = new JButton("Đăng xuất");
        btnLogout.setBackground(new Color(26,107,39));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnLogout.setFocusPainted(false);
        btnLogout.setBorderPainted(false);
        btnLogout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLogout.addActionListener(e -> {
            dispose();
            new LoginForm().setVisible(true);
        });
        rightBar.add(lblUser);
        rightBar.add(btnLogout);

        bar.add(title,    BorderLayout.WEST);
        bar.add(rightBar, BorderLayout.EAST);
        return bar;
    }

    private JSplitPane buildContent() {
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                                           buildFormPanel(), buildTablePanel());
        split.setDividerLocation(310);
        split.setDividerSize(4);
        split.setBorder(null);
        split.setBackground(BG);
        return split;
    }

    private JPanel buildFormPanel() {
        JPanel p = new JPanel();
        p.setBackground(PANEL);
        p.setBorder(new EmptyBorder(16, 16, 16, 16));
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        addSectionTitle(p, "THÔNG TIN NHÂN VIÊN");

        txtMaNV    = addField(p, "Mã Nhân Viên");
        txtTenNV   = addField(p, "Họ và Tên");
        txtPhongBan= addField(p, "Phòng Ban");
        txtHeSo    = addField(p, "Hệ Số Lương");
        txtLuongCB = addField(p, "Lương Cơ Bản (VNĐ)");

        KeyAdapter salaryKey = new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) { previewSalary(); }
        };
        txtHeSo   .addKeyListener(salaryKey);
        txtLuongCB.addKeyListener(salaryKey);

        addDivider(p);
        lblLuongPreview = new JLabel("Lương thực nhận: —");
        lblLuongPreview.setForeground(ACC2);
        lblLuongPreview.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblLuongPreview.setAlignmentX(LEFT_ALIGNMENT);
        p.add(lblLuongPreview);
        addDivider(p);

        btnThem   = addButton(p, "Thêm",      ACCENT);
        btnSua    = addButton(p, "Sửa",        WARNING);
        btnXoa    = addButton(p, "Xóa",        DANGER);
        btnLamMoi = addButton(p, "Làm Mới",   PANEL);
        btnLamMoi.setForeground(TEXT);
        btnLamMoi.setBorder(BorderFactory.createLineBorder(new Color(48,54,61)));

        addDivider(p);

        btnBaoCao = addButton(p, "Báo Cáo Theo Phòng Ban", PANEL);
        btnBaoCao.setForeground(INDIGO);
        btnBaoCao.setBorder(BorderFactory.createLineBorder(INDIGO, 1));

        btnXuat = addButton(p, "Xuất CSV / TXT", PANEL);
        btnXuat.setForeground(ACC2);
        btnXuat.setBorder(BorderFactory.createLineBorder(ACC2, 1));

        btnThem   .addActionListener(e -> themNV());
        btnSua    .addActionListener(e -> suaNV());
        btnXoa    .addActionListener(e -> xoaNV());
        btnLamMoi .addActionListener(e -> lamMoi());
        btnBaoCao .addActionListener(e -> showBaoCao());
        btnXuat   .addActionListener(e -> xuatFile());

        return p;
    }

    private void previewSalary() {
        try {
            double hs = Double.parseDouble(txtHeSo.getText().trim());
            double cb = Double.parseDouble(txtLuongCB.getText().trim());
            lblLuongPreview.setText("Lương thực nhận: " + VND.format((long)(hs * cb)) + " ₫");
        } catch (Exception ex) {
            lblLuongPreview.setText("Lương thực nhận: —");
        }
    }

    private JPanel buildTablePanel() {
        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setBackground(BG);
        p.setBorder(new EmptyBorder(10, 4, 10, 10));

        JPanel topBar = new JPanel(new BorderLayout(8, 0));
        topBar.setBackground(BG);

        JPanel searchRow = new JPanel(new BorderLayout(6, 0));
        searchRow.setBackground(BG);
        JLabel ico = new JLabel("🔍");
        ico.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        ico.setForeground(TEXT);
        txtSearch = styledField();
        txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate (javax.swing.event.DocumentEvent e) { doSearch(); }
            public void removeUpdate (javax.swing.event.DocumentEvent e) { doSearch(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { doSearch(); }
        });
        JLabel hint = new JLabel("  tìm theo tên / phòng ban");
        hint.setForeground(SUBTEXT);
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        searchRow.add(ico,       BorderLayout.WEST);
        searchRow.add(txtSearch, BorderLayout.CENTER);
        searchRow.add(hint,      BorderLayout.EAST);

        JPanel sortRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        sortRow.setBackground(BG);
        JLabel sortLbl = new JLabel("Sắp xếp:");
        sortLbl.setForeground(SUBTEXT);
        sortLbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        cbSort = new JComboBox<>(new String[]{"Mã NV", "Họ Tên", "Phòng Ban", "Lương"});
        cbSort.setBackground(CARD);
        cbSort.setForeground(TEXT);
        cbSort.addActionListener(e -> {
            String[] cols = {"maNV","tenNV","phongBan","heSoLuong*luongCoBan"};
            loadTable(cols[cbSort.getSelectedIndex()]);
        });
        sortRow.add(sortLbl);
        sortRow.add(cbSort);

        topBar.add(searchRow, BorderLayout.CENTER);
        topBar.add(sortRow,   BorderLayout.EAST);
        p.add(topBar, BorderLayout.NORTH);

        String[] cols = {"Mã NV", "Họ Tên", "Phòng Ban", "Hệ Số", "Lương CB", "Lương Thực Nhận"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        styleTable();
        int[] widths = {85, 195, 120, 75, 130, 160};
        for (int i = 0; i < widths.length; i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) onTableSelect();
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(CARD);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(48,54,61)));
        p.add(scroll, BorderLayout.CENTER);

        return p;
    }

    private JPanel buildStatusBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 4));
        bar.setBackground(BG);
        lblStatus = new JLabel("Sẵn sàng");
        lblStatus.setForeground(SUBTEXT);
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        bar.add(lblStatus);
        return bar;
    }

    private void addSectionTitle(JPanel p, String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(ACC2);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        p.add(lbl);
        JSeparator sep = new JSeparator();
        sep.setForeground(ACCENT);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        p.add(sep);
        p.add(Box.createVerticalStrut(10));
    }

    private JTextField addField(JPanel p, String label) {
        JLabel lbl = new JLabel(label);
        lbl.setForeground(SUBTEXT);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        p.add(lbl);
        p.add(Box.createVerticalStrut(2));
        JTextField tf = styledField();
        p.add(tf);
        p.add(Box.createVerticalStrut(6));
        return tf;
    }

    private JTextField styledField() {
        JTextField tf = new JTextField();
        tf.setBackground(CARD);
        tf.setForeground(TEXT);
        tf.setCaretColor(TEXT);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(48,54,61)),
            new EmptyBorder(6, 8, 6, 8)
        ));
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        tf.setAlignmentX(LEFT_ALIGNMENT);
        return tf;
    }

    private JButton addButton(JPanel p, String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        btn.setAlignmentX(LEFT_ALIGNMENT);
        p.add(btn);
        p.add(Box.createVerticalStrut(4));
        return btn;
    }

    private void addDivider(JPanel p) {
        p.add(Box.createVerticalStrut(6));
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(48,54,61));
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        p.add(sep);
        p.add(Box.createVerticalStrut(6));
    }

    private void styleTable() {
        table.setBackground(CARD);
        table.setForeground(TEXT);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(30);
        table.setGridColor(new Color(48,54,61));
        table.setSelectionBackground(INDIGO);
        table.setSelectionForeground(Color.WHITE);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.getTableHeader().setBackground(ACCENT);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBorder(BorderFactory.createEmptyBorder());
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                setHorizontalAlignment(col == 1 ? LEFT : CENTER);
                if (!sel) {
                    setBackground(row % 2 == 0 ? CARD : new Color(28,38,28));
                    setForeground(col == 5 ? ACC2 : TEXT); // lương nổi bật
                }
                setBorder(new EmptyBorder(0, 8, 0, 8));
                return this;
            }
        });
    }

    private void loadTable(String orderBy) {
        fillTable(dao.getAll(orderBy));
    }

    private void fillTable(List<NhanVien> list) {
        tableModel.setRowCount(0);
        long totalQuy = 0;
        for (NhanVien nv : list) {
            long luong = (long) nv.getLuongThucNhan();
            totalQuy += luong;
            tableModel.addRow(new Object[]{
                nv.getMaNV(), nv.getTenNV(), nv.getPhongBan(),
                nv.getHeSoLuong(), VND.format((long) nv.getLuongCoBan()) + " ₫",
                VND.format(luong) + " ₫"
            });
        }
        lblStatus.setText("Tổng: " + list.size() + " nhân viên  |  Tổng quỹ lương: "
                           + VND.format(totalQuy) + " ₫");
    }

    private void doSearch() {
        String kw = txtSearch.getText().trim();
        if (kw.isEmpty()) loadTable("maNV");
        else fillTable(dao.search(kw));
    }

    private void onTableSelect() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        selectedMaNV = (String) tableModel.getValueAt(row, 0);
        txtMaNV    .setText(selectedMaNV);
        txtTenNV   .setText((String)  tableModel.getValueAt(row, 1));
        txtPhongBan.setText((String)  tableModel.getValueAt(row, 2));
        txtHeSo    .setText(String.valueOf(tableModel.getValueAt(row, 3)));

        // Strip formatting from CB
        String cbRaw = tableModel.getValueAt(row, 4).toString()
                         .replace(" ₫","").replace(",","");
        txtLuongCB.setText(cbRaw);
        txtMaNV.setEditable(false);
        previewSalary();
    }

    private NhanVien buildFromForm(boolean requireMa) {
        String maNV     = txtMaNV.getText().trim();
        String tenNV    = txtTenNV.getText().trim();
        String phong    = txtPhongBan.getText().trim();
        String heSoStr  = txtHeSo.getText().trim();
        String luongStr = txtLuongCB.getText().trim();

        if (requireMa && maNV.isEmpty()) {
            showWarn("Mã nhân viên không được để trống!");
            return null;
        }
        if (tenNV.isEmpty() || phong.isEmpty()) {
            showWarn("Họ tên và Phòng ban không được để trống!");
            return null;
        }
        try {
            double hs = Double.parseDouble(heSoStr);
            double cb = Double.parseDouble(luongStr);
            if (hs <= 0 || cb <= 0) throw new NumberFormatException();
            return new NhanVien(maNV, tenNV, phong, hs, cb);
        } catch (NumberFormatException ex) {
            showWarn("Hệ số và Lương cơ bản phải là số dương!");
            return null;
        }
    }

    private void themNV() {
        NhanVien nv = buildFromForm(true);
        if (nv == null) return;
        if (dao.insert(nv)) {
            lamMoi();
            lblStatus.setText("Đã thêm nhân viên: " + nv.getMaNV());
        } else {
            JOptionPane.showMessageDialog(this,
                "Mã nhân viên '" + nv.getMaNV() + "' đã tồn tại hoặc lỗi DB!",
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void suaNV() {
        if (selectedMaNV == null) { showInfo("Hãy chọn nhân viên trong bảng!"); return; }
        NhanVien nv = buildFromForm(false);
        if (nv == null) return;
        nv.setMaNV(selectedMaNV);
        if (dao.update(nv)) { lamMoi(); lblStatus.setText("Đã cập nhật nhân viên."); }
    }

    private void xoaNV() {
        if (selectedMaNV == null) { showInfo("Hãy chọn nhân viên trong bảng!"); return; }
        int c = JOptionPane.showConfirmDialog(this,
            "Xóa nhân viên '" + txtTenNV.getText() + "' (MaNV: " + selectedMaNV + ")?",
            "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (c == JOptionPane.YES_OPTION && dao.delete(selectedMaNV)) {
            lamMoi();
            lblStatus.setText("Đã xóa nhân viên.");
        }
    }

    private void lamMoi() {
        selectedMaNV = null;
        txtMaNV.setText("");     txtMaNV.setEditable(true);
        txtTenNV.setText("");    txtPhongBan.setText("");
        txtHeSo.setText("");     txtLuongCB.setText("");
        txtSearch.setText("");
        lblLuongPreview.setText("Lương thực nhận: —");
        table.clearSelection();
        loadTable("maNV");
    }

    private void showBaoCao() {
        List<Object[]> data = dao.baoCaoTheoPhongBan();

        JDialog dlg = new JDialog(this, "Báo Cáo Nhân Viên Theo Phòng Ban", true);
        dlg.setSize(580, 400);
        dlg.setLocationRelativeTo(this);
        dlg.getContentPane().setBackground(BG);
        dlg.setLayout(new BorderLayout(0, 10));

        JLabel title = new JLabel("Báo Cáo Nhân Viên Theo Phòng Ban", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(INDIGO);
        title.setBorder(new EmptyBorder(16, 0, 4, 0));
        dlg.add(title, BorderLayout.NORTH);

        String[] cols = {"Phòng Ban", "Số NV", "Hệ Số TB", "Tổng Quỹ Lương"};
        DefaultTableModel m = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        long totalQuy = 0;
        int  totalNV  = 0;
        for (Object[] row : data) {
            long quy = ((Double) row[3]).longValue();
            totalQuy += quy;
            totalNV  += (int) row[1];
            m.addRow(new Object[]{row[0], row[1], row[2], VND.format(quy) + " ₫"});
        }

        JTable t = new JTable(m);
        t.setBackground(CARD); t.setForeground(TEXT);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        t.setRowHeight(28);
        t.getTableHeader().setBackground(ACCENT);
        t.getTableHeader().setForeground(Color.WHITE);
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        t.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tb, Object v,
                    boolean s, boolean f, int r, int c) {
                super.getTableCellRendererComponent(tb, v, s, f, r, c);
                setHorizontalAlignment(c == 0 ? LEFT : CENTER);
                if (!s) { setBackground(r%2==0?CARD:new Color(28,38,28)); setForeground(TEXT); }
                setBorder(new EmptyBorder(0, 8, 0, 8));
                return this;
            }
        });

        JScrollPane sp = new JScrollPane(t);
        sp.getViewport().setBackground(CARD);
        sp.setBorder(new EmptyBorder(0,16,0,16));
        dlg.add(sp, BorderLayout.CENTER);

        JLabel footer = new JLabel(
            "Tổng: " + totalNV + " nhân viên  |  Tổng quỹ lương: " + VND.format(totalQuy) + " ₫",
            SwingConstants.CENTER);
        footer.setForeground(ACC2);
        footer.setFont(new Font("Segoe UI", Font.BOLD, 13));
        footer.setBorder(new EmptyBorder(8, 0, 12, 0));
        dlg.add(footer, BorderLayout.SOUTH);

        dlg.setVisible(true);
    }

    private void xuatFile() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Xuất danh sách nhân viên");
        fc.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV (Excel)", "csv"));
        fc.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Text File", "txt"));
        fc.setFileFilter(fc.getChoosableFileFilters()[1]);
        if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

        File file = fc.getSelectedFile();
        String fname = file.getName().toLowerCase();
        if (!fname.endsWith(".csv") && !fname.endsWith(".txt"))
            file = new File(file.getPath() + ".csv");

        List<NhanVien> list = dao.getAll("maNV");
        boolean isCsv = file.getName().toLowerCase().endsWith(".csv");
        String now = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());

        try (PrintWriter pw = new PrintWriter(
                new OutputStreamWriter(new FileOutputStream(file), "UTF-8"))) {
            if (isCsv) {
                pw.println("\uFEFFMã NV,Họ Tên,Phòng Ban,Hệ Số Lương,Lương Cơ Bản,Lương Thực Nhận");
                for (NhanVien nv : list)
                    pw.printf("%s,%s,%s,%.2f,%.0f,%.0f%n",
                        nv.getMaNV(), nv.getTenNV(), nv.getPhongBan(),
                        nv.getHeSoLuong(), nv.getLuongCoBan(), nv.getLuongThucNhan());
            } else {
                pw.println("DANH SÁCH NHÂN VIÊN – " + now);
                pw.println("=".repeat(85));
                pw.printf("%-8s %-20s %-14s %-8s %-16s %s%n",
                    "Mã NV","Họ Tên","Phòng Ban","Hệ Số","Lương CB","Lương Thực Nhận");
                pw.println("-".repeat(85));
                for (NhanVien nv : list)
                    pw.printf("%-8s %-20s %-14s %-8.2f %-16s %s%n",
                        nv.getMaNV(), nv.getTenNV(), nv.getPhongBan(),
                        nv.getHeSoLuong(),
                        VND.format((long) nv.getLuongCoBan()) + " ₫",
                        VND.format((long) nv.getLuongThucNhan()) + " ₫");
                pw.println("=".repeat(85));
                pw.println("Tổng: " + list.size() + " nhân viên");
            }
            JOptionPane.showMessageDialog(this, "Đã xuất file:\n" + file.getAbsolutePath(),
                                           "Thành công", JOptionPane.INFORMATION_MESSAGE);
            lblStatus.setText("Đã xuất → " + file.getName());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(),
                                           "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showWarn(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Thiếu / Sai dữ liệu", JOptionPane.WARNING_MESSAGE);
    }
    private void showInfo(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
}
