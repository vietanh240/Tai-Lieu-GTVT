package Bai1;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class QuanLySinhVienForm extends JFrame {

    // ── DAO ──────────────────────────────────────────────────
    private final SinhVienDAO dao = new SinhVienDAO();

    // ── FORM FIELDS ──────────────────────────────────────────
    private JTextField txtId, txtHoTen, txtLop, txtDiemTB, txtEmail, txtSearch;
    private JButton    btnThem, btnSua, btnXoa, btnLamMoi, btnThongKe, btnXuat;
    private JTable     table;
    private DefaultTableModel tableModel;
    private JLabel     lblStatus;
    private JComboBox<String> cbSort;

    private int selectedId = -1;

    static final Color BG      = new Color(15,  23,  42);
    static final Color PANEL   = new Color(30,  41,  59);
    static final Color CARD    = new Color(38,  50,  72);
    static final Color ACCENT  = new Color(99, 102, 241);
    static final Color ACC2    = new Color(129,140,248);
    static final Color SUCCESS = new Color(34, 197,  94);
    static final Color DANGER  = new Color(239, 68,  68);
    static final Color WARNING = new Color(245,158,  11);
    static final Color TEXT    = new Color(241,245,249);
    static final Color SUBTEXT = new Color(148,163,184);

    public QuanLySinhVienForm() {
        setTitle("Quản Lý Sinh Viên");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 680);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 600));

        buildUI();
        loadTable("id");
    }

    // ── BUILD UI ─────────────────────────────────────────────
    private void buildUI() {
        getContentPane().setBackground(BG);
        setLayout(new BorderLayout(0, 0));

        add(buildTopBar(),   BorderLayout.NORTH);
        add(buildContent(),  BorderLayout.CENTER);
        add(buildStatusBar(),BorderLayout.SOUTH);
    }

    // ── TOP BAR ──────────────────────────────────────────────
    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(ACCENT);
        bar.setPreferredSize(new Dimension(0, 52));
        bar.setBorder(new EmptyBorder(0, 20, 0, 20));

        JLabel title = new JLabel("Quản Lý Sinh Viên");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(TEXT);

        JLabel sub = new JLabel("MySQL • Java Swing • JDBC");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        sub.setForeground(new Color(199,210,254));

        bar.add(title, BorderLayout.WEST);
        bar.add(sub,   BorderLayout.EAST);
        return bar;
    }

    private JSplitPane buildContent() {
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                                           buildFormPanel(), buildTablePanel());
        split.setDividerLocation(290);
        split.setDividerSize(4);
        split.setBorder(null);
        split.setBackground(BG);
        return split;
    }

    private JPanel buildFormPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(PANEL);
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        addSectionTitle(panel, "THÔNG TIN SINH VIÊN");

        String[] labels = {"Mã SV (ID)", "Họ và Tên", "Lớp", "Điểm TB", "Email"};
        txtId     = addField(panel, labels[0]);
        txtHoTen  = addField(panel, labels[1]);
        txtLop    = addField(panel, labels[2]);
        txtDiemTB = addField(panel, labels[3]);
        txtEmail  = addField(panel, labels[4]);
        txtId.setEditable(false);
        txtId.setBackground(BG);
        txtId.setForeground(SUBTEXT);

        panel.add(Box.createVerticalStrut(8));

        btnThem    = addButton(panel, "Thêm",     SUCCESS);
        btnSua     = addButton(panel, "Sửa",      WARNING);
        btnXoa     = addButton(panel, "Xóa",      DANGER);
        btnLamMoi  = addButton(panel, "Làm Mới",  ACCENT);

        addDivider(panel);

        btnThongKe = addButton(panel, "Thống Kê Theo Lớp", PANEL);
        btnThongKe.setForeground(ACC2);
        btnThongKe.setBorder(BorderFactory.createLineBorder(ACC2, 1));

        btnXuat = addButton(panel, "Xuất Excel / TXT", PANEL);
        btnXuat.setForeground(SUCCESS);
        btnXuat.setBorder(BorderFactory.createLineBorder(SUCCESS, 1));

        btnThem   .addActionListener(e -> themSV());
        btnSua    .addActionListener(e -> suaSV());
        btnXoa    .addActionListener(e -> xoaSV());
        btnLamMoi .addActionListener(e -> lamMoi());
        btnThongKe.addActionListener(e -> showThongKe());
        btnXuat   .addActionListener(e -> xuatFile());

        return panel;
    }

    private JPanel buildTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(BG);
        panel.setBorder(new EmptyBorder(10, 4, 10, 10));

        JPanel topBar = new JPanel(new BorderLayout(8, 0));
        topBar.setBackground(BG);

        JPanel searchBar = new JPanel(new BorderLayout(6, 0));
        searchBar.setBackground(BG);
        JLabel ico = new JLabel("Tìm Kiếm");
        ico.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        ico.setForeground(TEXT);
        txtSearch = styledField();
        txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate (javax.swing.event.DocumentEvent e) { doSearch(); }
            public void removeUpdate (javax.swing.event.DocumentEvent e) { doSearch(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { doSearch(); }
        });
        searchBar.add(ico,       BorderLayout.WEST);
        searchBar.add(txtSearch, BorderLayout.CENTER);

        JPanel sortBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        sortBar.setBackground(BG);
        JLabel sortLbl = new JLabel("Sắp xếp:");
        sortLbl.setForeground(SUBTEXT);
        sortLbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        cbSort = new JComboBox<>(new String[]{"Mã SV", "Họ Tên", "Điểm TB"});
        cbSort.setBackground(CARD);
        cbSort.setForeground(TEXT);
        cbSort.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        cbSort.addActionListener(e -> {
            String[] cols = {"id", "hoTen", "diemTB"};
            loadTable(cols[cbSort.getSelectedIndex()]);
        });
        sortBar.add(sortLbl);
        sortBar.add(cbSort);

        topBar.add(searchBar, BorderLayout.CENTER);
        topBar.add(sortBar,   BorderLayout.EAST);
        panel.add(topBar, BorderLayout.NORTH);

        String[] cols = {"ID", "Họ Tên", "Lớp", "Điểm TB", "Email"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        styleTable(table);

        int[] widths = {55, 230, 100, 85, 230};
        for (int i = 0; i < widths.length; i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) onTableSelect();
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(CARD);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(51,65,85)));
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
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
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(51,65,85)),
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
        p.add(Box.createVerticalStrut(8));
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(51,65,85));
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        p.add(sep);
        p.add(Box.createVerticalStrut(8));
    }

    private void styleTable(JTable t) {
        t.setBackground(CARD);
        t.setForeground(TEXT);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        t.setRowHeight(30);
        t.setGridColor(new Color(51,65,85));
        t.setSelectionBackground(ACCENT);
        t.setSelectionForeground(Color.WHITE);
        t.setShowHorizontalLines(true);
        t.setShowVerticalLines(false);
        t.getTableHeader().setBackground(ACCENT);
        t.getTableHeader().setForeground(Color.WHITE);
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        t.getTableHeader().setBorder(BorderFactory.createEmptyBorder());
        t.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object val,
                    boolean sel, boolean focus, int row, int col) {
                super.getTableCellRendererComponent(tbl, val, sel, focus, row, col);
                setHorizontalAlignment(col == 1 || col == 4 ? LEFT : CENTER);
                if (!sel) {
                    setBackground(row % 2 == 0 ? CARD : new Color(45,60,85));
                    setForeground(TEXT);
                }
                setBorder(new EmptyBorder(0, 8, 0, 8));
                return this;
            }
        });
    }

    private void loadTable(String orderBy) {
        List<SinhVien> list = dao.getAll(orderBy);
        fillTable(list);
    }

    private void fillTable(List<SinhVien> list) {
        tableModel.setRowCount(0);
        for (SinhVien sv : list) {
            tableModel.addRow(new Object[]{
                sv.getId(), sv.getHoTen(), sv.getLop(), sv.getDiemTB(), sv.getEmail()
            });
        }
        lblStatus.setText("Tổng: " + list.size() + " sinh viên");
    }

    private void doSearch() {
        String kw = txtSearch.getText().trim();
        if (kw.isEmpty()) loadTable("id");
        else fillTable(dao.search(kw));
    }

    private void onTableSelect() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        selectedId = (int) tableModel.getValueAt(row, 0);
        txtId    .setText(String.valueOf(selectedId));
        txtHoTen .setText((String) tableModel.getValueAt(row, 1));
        txtLop   .setText((String) tableModel.getValueAt(row, 2));
        txtDiemTB.setText(String.valueOf(tableModel.getValueAt(row, 3)));
        txtEmail .setText((String) tableModel.getValueAt(row, 4));
    }

    private SinhVien buildFromForm() {
        String hoTen  = txtHoTen.getText().trim();
        String lop    = txtLop.getText().trim();
        String diemStr = txtDiemTB.getText().trim();
        String email  = txtEmail.getText().trim();

        if (hoTen.isEmpty() || lop.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Họ tên và Lớp không được để trống!",
                                           "Thiếu dữ liệu", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        double diem;
        try {
            diem = Double.parseDouble(diemStr);
            if (diem < 0 || diem > 10) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Điểm TB phải là số từ 0 – 10!",
                                           "Dữ liệu sai", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        return new SinhVien(0, hoTen, lop, diem, email);
    }

    private void themSV() {
        SinhVien sv = buildFromForm();
        if (sv == null) return;
        if (dao.insert(sv)) {
            lamMoi();
            lblStatus.setText("Đã thêm sinh viên mới.");
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void suaSV() {
        if (selectedId < 0) {
            JOptionPane.showMessageDialog(this, "Hãy chọn một sinh viên trong bảng!");
            return;
        }
        SinhVien sv = buildFromForm();
        if (sv == null) return;
        sv.setId(selectedId);
        if (dao.update(sv)) {
            lamMoi();
            lblStatus.setText("Đã cập nhật thông tin.");
        }
    }

    private void xoaSV() {
        if (selectedId < 0) {
            JOptionPane.showMessageDialog(this, "Hãy chọn một sinh viên trong bảng!");
            return;
        }
        String name = txtHoTen.getText();
        int confirm = JOptionPane.showConfirmDialog(this,
            "Xóa sinh viên '" + name + "'?", "Xác nhận xóa",
            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            if (dao.delete(selectedId)) {
                lamMoi();
                lblStatus.setText("Đã xóa sinh viên.");
            }
        }
    }

    private void lamMoi() {
        selectedId = -1;
        txtId.setText("");
        txtHoTen.setText("");
        txtLop.setText("");
        txtDiemTB.setText("");
        txtEmail.setText("");
        txtSearch.setText("");
        table.clearSelection();
        loadTable("id");
    }

    private void showThongKe() {
        List<Object[]> stats = dao.thongKeTheoLop();

        JDialog dlg = new JDialog(this, "Thống Kê Theo Lớp", true);
        dlg.setSize(480, 380);
        dlg.setLocationRelativeTo(this);
        dlg.getContentPane().setBackground(BG);
        dlg.setLayout(new BorderLayout(0, 10));

        JLabel title = new JLabel("Thống Kê Số Sinh Viên Theo Lớp", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(ACC2);
        title.setBorder(new EmptyBorder(16, 0, 4, 0));
        dlg.add(title, BorderLayout.NORTH);

        String[] cols = {"Lớp", "Số SV", "Điểm TB Lớp"};
        DefaultTableModel m = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        for (Object[] row : stats) m.addRow(row);

        JTable t = new JTable(m);
        styleTable(t);
        t.setRowHeight(28);
        JScrollPane sp = new JScrollPane(t);
        sp.getViewport().setBackground(CARD);
        sp.setBorder(BorderFactory.createLineBorder(new Color(51,65,85)));
        dlg.add(sp, BorderLayout.CENTER);

        int total = stats.stream().mapToInt(r -> (int) r[1]).sum();
        JLabel footer = new JLabel("Tổng: " + total + " sinh viên  |  " + stats.size() + " lớp",
                                    SwingConstants.CENTER);
        footer.setForeground(SUCCESS);
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footer.setBorder(new EmptyBorder(6, 0, 6, 0));
        dlg.add(footer, BorderLayout.SOUTH);

        dlg.setVisible(true);
    }

    private void xuatFile() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Xuất danh sách sinh viên");
        fc.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV (Excel)", "csv"));
        fc.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Text File", "txt"));
        fc.setFileFilter(fc.getChoosableFileFilters()[1]);

        if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

        File file = fc.getSelectedFile();
        String name = file.getName().toLowerCase();
        if (!name.endsWith(".csv") && !name.endsWith(".txt"))
            file = new File(file.getPath() + ".csv");

        List<SinhVien> list = dao.getAll("id");
        boolean isCsv = file.getName().toLowerCase().endsWith(".csv");

        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(
                new FileOutputStream(file), "UTF-8"))) {
            if (isCsv) {
                pw.println("\uFEFFID,Họ Tên,Lớp,Điểm TB,Email");
                for (SinhVien sv : list)
                    pw.printf("%d,%s,%s,%.2f,%s%n",
                        sv.getId(), sv.getHoTen(), sv.getLop(), sv.getDiemTB(), sv.getEmail());
            } else {
                String now = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
                pw.println("DANH SÁCH SINH VIÊN – Xuất lúc " + now);
                pw.println("=".repeat(72));
                pw.printf("%-6s %-25s %-10s %-10s %s%n", "ID", "Họ Tên", "Lớp", "Điểm TB", "Email");
                pw.println("-".repeat(72));
                for (SinhVien sv : list)
                    pw.printf("%-6d %-25s %-10s %-10.2f %s%n",
                        sv.getId(), sv.getHoTen(), sv.getLop(), sv.getDiemTB(), sv.getEmail());
                pw.println("=".repeat(72));
                pw.println("Tổng: " + list.size() + " sinh viên");
            }
            JOptionPane.showMessageDialog(this, "Đã xuất file:\n" + file.getAbsolutePath(),
                                           "Thành công", JOptionPane.INFORMATION_MESSAGE);
            lblStatus.setText("Đã xuất " + list.size() + " sinh viên → " + file.getName());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi xuất file: " + ex.getMessage(),
                                           "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new QuanLySinhVienForm().setVisible(true));
    }
}
