package Bai5;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import Bai3.QuizGameForm;

import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ThuVienForm extends JFrame {

    private static final Color C_BG      = new Color(248, 250, 252);
    private static final Color C_SIDE    = new Color(15,  23,  42);
    private static final Color C_WHITE   = Color.WHITE;
    private static final Color C_BORDER  = new Color(226, 232, 240);
    private static final Color C_BLUE    = new Color(37,  99, 235);
    private static final Color C_BLUE_DK = new Color(29,  78, 216);
    private static final Color C_GREEN   = new Color(22, 163,  74);
    private static final Color C_RED     = new Color(220,  38,  38);
    private static final Color C_ORANGE  = new Color(234,  88,  12);
    private static final Color C_TEXT    = new Color(15,  23,  42);
    private static final Color C_MUTED   = new Color(100, 116, 140);
    private static final Color C_ROW_ALT = new Color(248, 250, 252);
    private static final Color C_ROW_SEL = new Color(219, 234, 254);

    private static final Font F_TITLE = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font F_BOLD  = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font F_NORM  = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font F_SMALL = new Font("Segoe UI", Font.PLAIN, 11);
    private static final Font F_BTN   = new Font("Segoe UI", Font.BOLD, 12);

    private final ThuVienDAO dao = new ThuVienDAO();

    // Labels sidebar
    private JLabel lblSoSach, lblSoDG, lblStatus;

    // Tab Sách
    private DefaultTableModel modelSach;
    private JTable tableSach;
    private JTextField txtTenSach, txtTacGia, txtNamXB, txtSearchSach;
    private JComboBox<String> cmbTinhTrang;
    private JLabel lblSachId;

    // Tab Đọc giả
    private DefaultTableModel modelDG;
    private JTable tableDG;
    private JTextField txtHoTen, txtNgaySinh;
    private JLabel lblDGId;

    // Tab Mượn/Trả
    private DefaultTableModel modelMuon;
    private JTable tableMuon;
    private JComboBox<DocGia> cmbDG;
    private JComboBox<Sach>   cmbSach;

    // Tab Thống kê
    private DefaultTableModel modelTre, modelTop;

    public ThuVienForm() {
        setTitle("📚 Quản Lý Thư Viện");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(buildSidebar(),   BorderLayout.WEST);
        add(buildContent(),   BorderLayout.CENTER);
        add(buildStatusBar(), BorderLayout.SOUTH);

        refreshAll();
        setVisible(true);
    }

    private JPanel buildSidebar() {
        JPanel p = new JPanel();
        p.setBackground(C_SIDE);
        p.setPreferredSize(new Dimension(210, 0));
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        JPanel logo = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 18));
        logo.setBackground(C_SIDE); logo.setMaximumSize(new Dimension(210, 64));
        JLabel ico = new JLabel("📚"); ico.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 26));
        JLabel nm = new JLabel("<html><b style='color:white;font-size:12px'>Thư Viện</b><br>" +
                               "<span style='color:#64748b;font-size:10px'>XAMPP • MySQL</span></html>");
        logo.add(ico); logo.add(nm);
        p.add(logo);
        p.add(sideSep());

        lblSoSach = sideVal("—"); lblSoDG = sideVal("—");
        p.add(sideInfoPanel("📖", "Sách",     lblSoSach));
        p.add(sideInfoPanel("👤", "Đọc giả",  lblSoDG));
        p.add(sideSep());
        p.add(Box.createVerticalGlue());

        JLabel ver = new JLabel("  v1.0 — Java Swing");
        ver.setFont(F_SMALL); ver.setForeground(new Color(51,65,85));
        ver.setBorder(new EmptyBorder(0,0,12,0));
        ver.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(ver);
        return p;
    }

    private JSeparator sideSep() {
        JSeparator s = new JSeparator();
        s.setForeground(new Color(30,41,59)); s.setMaximumSize(new Dimension(210,1)); return s;
    }

    private JLabel sideVal(String t) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("Segoe UI", Font.BOLD, 13)); l.setForeground(Color.WHITE); return l;
    }

    private JPanel sideInfoPanel(String icon, String label, JLabel val) {
        JPanel p = new JPanel(new BorderLayout(8,0));
        p.setBackground(C_SIDE); p.setMaximumSize(new Dimension(210,52));
        p.setBorder(new EmptyBorder(8,14,8,14));
        JLabel ico = new JLabel(icon); ico.setFont(new Font("Segoe UI Emoji", Font.PLAIN,18));
        JPanel txt = new JPanel(new GridLayout(2,1)); txt.setBackground(C_SIDE);
        JLabel lbl = new JLabel(label); lbl.setFont(F_SMALL); lbl.setForeground(new Color(100,116,140));
        txt.add(lbl); txt.add(val);
        p.add(ico, BorderLayout.WEST); p.add(txt, BorderLayout.CENTER); return p;
    }

    // ── Content (Tabs) ───────────────────────────────────────────────────────
    private JTabbedPane buildContent() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(F_BOLD); tabs.setBackground(C_BG);
        tabs.addTab("Quản lý Sách",    buildTabSach());
        tabs.addTab("Đọc Giả",          buildTabDocGia());
        tabs.addTab("Mượn / Trả",       buildTabMuon());
        tabs.addTab("Thống Kê",          buildTabThongKe());
        return tabs;
    }

    // ── Tab Sách ─────────────────────────────────────────────────────────────
    private JPanel buildTabSach() {
        JPanel root = new JPanel(new BorderLayout(0,10));
        root.setBackground(C_BG); root.setBorder(new EmptyBorder(16,16,16,16));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT,8,0));
        top.setBackground(C_BG);
        top.add(lbl("Tìm:")); txtSearchSach = field(16); top.add(txtSearchSach);
        JButton btnTim = btn("Tìm",   C_BLUE, C_BLUE_DK);
        JButton btnAll = btn("Tất cả", new Color(100,116,140), new Color(71,85,105));
        btnTim.addActionListener(e -> loadSach(dao.searchSach(txtSearchSach.getText())));
        btnAll.addActionListener(e -> { txtSearchSach.setText(""); loadSach(dao.getAllSach()); });
        top.add(btnTim); top.add(btnAll);
        root.add(top, BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setDividerLocation(620); split.setBorder(null);

        String[] cols = {"ID","Tên sách","Tác giả","Năm XB","Tình trạng"};
        modelSach = new DefaultTableModel(cols,0){public boolean isCellEditable(int r,int c){return false;}};
        tableSach = new JTable(modelSach); styleTable(tableSach);
        tableSach.getSelectionModel().addListSelectionListener(e -> { if(!e.getValueIsAdjusting()) fillFormSach(); });
        split.setLeftComponent(new JScrollPane(tableSach));

        split.setRightComponent(buildFormSach());
        root.add(split, BorderLayout.CENTER);
        return root;
    }

    private JPanel buildFormSach() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(C_WHITE); p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(C_BORDER), new EmptyBorder(18,18,18,18)));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6,6,6,6); gc.fill = GridBagConstraints.HORIZONTAL;

        lblSachId = new JLabel("(Mới)"); lblSachId.setFont(F_SMALL); lblSachId.setForeground(C_MUTED);

        row(p, gc, 0, "ID:", lblSachId);
        txtTenSach = field(14); row(p, gc, 1, "Tên sách:", txtTenSach);
        txtTacGia  = field(14); row(p, gc, 2, "Tác giả:",  txtTacGia);
        txtNamXB   = field(6);  row(p, gc, 3, "Năm XB:",   txtNamXB);
        cmbTinhTrang = new JComboBox<>(new String[]{"Còn","Đang mượn","Hỏng"});
        cmbTinhTrang.setFont(F_NORM); row(p, gc, 4, "Tình trạng:", cmbTinhTrang);

        gc.gridy=5; gc.gridx=0; gc.gridwidth=2;
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT,6,0));
        btns.setBackground(C_WHITE);
        JButton btnLuu  = btn("Lưu",      C_GREEN, new Color(15,118,54));
        JButton btnXoa  = btn("Xóa",      C_RED,   new Color(185,28,28));
        JButton btnClear= btn("Xóa trắng", new Color(100,116,140), new Color(71,85,105));
        btnLuu.addActionListener(e   -> saveSach());
        btnXoa.addActionListener(e   -> deleteSach());
        btnClear.addActionListener(e -> clearSach());
        btns.add(btnClear); btns.add(btnXoa); btns.add(btnLuu);
        p.add(btns, gc);
        return p;
    }

    private JPanel buildTabDocGia() {
        JPanel root = new JPanel(new BorderLayout(0,10));
        root.setBackground(C_BG); root.setBorder(new EmptyBorder(16,16,16,16));
        root.add(new JLabel("Quản lý Đọc Giả") {{ setFont(F_TITLE); setForeground(C_TEXT); }}, BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setDividerLocation(580); split.setBorder(null);

        String[] cols = {"ID","Họ tên","Ngày sinh"};
        modelDG = new DefaultTableModel(cols,0){public boolean isCellEditable(int r,int c){return false;}};
        tableDG = new JTable(modelDG); styleTable(tableDG);
        tableDG.getSelectionModel().addListSelectionListener(e -> { if(!e.getValueIsAdjusting()) fillFormDG(); });
        split.setLeftComponent(new JScrollPane(tableDG));
        split.setRightComponent(buildFormDG());
        root.add(split, BorderLayout.CENTER);
        return root;
    }

    private JPanel buildFormDG() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(C_WHITE); p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(C_BORDER), new EmptyBorder(18,18,18,18)));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6,6,6,6); gc.fill = GridBagConstraints.HORIZONTAL;

        lblDGId = new JLabel("(Mới)"); lblDGId.setFont(F_SMALL); lblDGId.setForeground(C_MUTED);
        row(p, gc, 0, "ID:",       lblDGId);
        txtHoTen   = field(14); row(p, gc, 1, "Họ tên:",    txtHoTen);
        txtNgaySinh= field(14); row(p, gc, 2, "Ngày sinh:", txtNgaySinh);

        gc.gridy=3; gc.gridx=0; gc.gridwidth=2;
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT,6,0));
        btns.setBackground(C_WHITE);
        JButton btnLuu  = btn("Lưu",      C_GREEN, new Color(15,118,54));
        JButton btnXoa  = btn("Xóa",      C_RED,   new Color(185,28,28));
        JButton btnClear= btn("Xóa trắng", new Color(100,116,140), new Color(71,85,105));
        btnLuu.addActionListener(e   -> saveDG());
        btnXoa.addActionListener(e   -> deleteDG());
        btnClear.addActionListener(e -> clearDG());
        btns.add(btnClear); btns.add(btnXoa); btns.add(btnLuu);
        p.add(btns, gc);
        return p;
    }

    private JPanel buildTabMuon() {
        JPanel root = new JPanel(new BorderLayout(0,10));
        root.setBackground(C_BG); root.setBorder(new EmptyBorder(16,16,16,16));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT,8,4));
        top.setBackground(C_BG);
        top.add(lbl("Đọc giả:")); cmbDG   = new JComboBox<>(); cmbDG.setFont(F_NORM); top.add(cmbDG);
        top.add(lbl("Sách:"));    cmbSach  = new JComboBox<>(); cmbSach.setFont(F_NORM); top.add(cmbSach);
        JButton btnMuon = btn("Mượn sách", C_BLUE,  C_BLUE_DK);
        JButton btnTra  = btn("Trả sách",  C_GREEN, new Color(15,118,54));
        JButton btnRef  = btn("Làm mới",   new Color(100,116,140), new Color(71,85,105));
        btnMuon.addActionListener(e -> muonSach());
        btnTra.addActionListener(e  -> traSach());
        btnRef.addActionListener(e  -> refreshMuon());
        top.add(btnMuon); top.add(btnTra); top.add(btnRef);
        root.add(top, BorderLayout.NORTH);

        String[] cols = {"ID Mượn","Đọc giả","Sách","Ngày mượn","Ngày trả"};
        modelMuon = new DefaultTableModel(cols,0){public boolean isCellEditable(int r,int c){return false;}};
        JTable tbl = new JTable(modelMuon); styleTable(tbl);
        tbl.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean focus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t,v,sel,focus,row,col);
                String ngayTra = (String) modelMuon.getValueAt(row, 4);
                if (!sel) c.setBackground("Chưa trả".equals(ngayTra) ? new Color(255,237,213) : C_WHITE);
                setBorder(new EmptyBorder(0,8,0,8)); return c;
            }
        });
        root.add(new JScrollPane(tbl), BorderLayout.CENTER);
        return root;
    }

    private JPanel buildTabThongKe() {
        JPanel root = new JPanel(new GridLayout(1,2,12,0));
        root.setBackground(C_BG); root.setBorder(new EmptyBorder(16,16,16,16));

        JPanel panelTre = new JPanel(new BorderLayout(0,8));
        panelTre.setBackground(C_BG);
        JLabel t1 = new JLabel("⚠️ Sách trễ hạn (> 14 ngày)"); t1.setFont(F_BOLD); t1.setForeground(C_RED);
        panelTre.add(t1, BorderLayout.NORTH);
        String[] c1 = {"Đọc giả","Tên sách","Ngày mượn","Số ngày trễ"};
        modelTre = new DefaultTableModel(c1,0){public boolean isCellEditable(int r,int c){return false;}};
        JTable tblTre = new JTable(modelTre); styleTable(tblTre);
        panelTre.add(new JScrollPane(tblTre), BorderLayout.CENTER);

        JPanel panelTop = new JPanel(new BorderLayout(0,8));
        panelTop.setBackground(C_BG);
        JLabel t2 = new JLabel("🏆 Sách được mượn nhiều nhất"); t2.setFont(F_BOLD); t2.setForeground(C_BLUE);
        panelTop.add(t2, BorderLayout.NORTH);
        String[] c2 = {"Tên sách","Tác giả","Số lần mượn"};
        modelTop = new DefaultTableModel(c2,0){public boolean isCellEditable(int r,int c){return false;}};
        JTable tblTop = new JTable(modelTop); styleTable(tblTop);
        panelTop.add(new JScrollPane(tblTop), BorderLayout.CENTER);

        JButton btnRef = btn("Làm mới", C_BLUE, C_BLUE_DK);
        btnRef.addActionListener(e -> loadThongKe());
        JPanel bot = new JPanel(new FlowLayout(FlowLayout.RIGHT)); bot.setBackground(C_BG); bot.add(btnRef);
        panelTop.add(bot, BorderLayout.SOUTH);

        root.add(panelTre); root.add(panelTop);
        loadThongKe();
        return root;
    }

    private JPanel buildStatusBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT,12,4));
        bar.setBackground(new Color(241,245,249));
        bar.setBorder(BorderFactory.createMatteBorder(1,0,0,0,C_BORDER));
        lblStatus = new JLabel("Sẵn sàng"); lblStatus.setFont(F_SMALL); lblStatus.setForeground(C_MUTED);
        bar.add(lblStatus); return bar;
    }

    private void refreshAll() {
        loadSach(dao.getAllSach());
        loadDocGia(dao.getAllDocGia());
        refreshMuon();
    }

    private void loadSach(List<Sach> list) {
        modelSach.setRowCount(0);
        for (Sach s : list) modelSach.addRow(new Object[]{s.getId(),s.getTenSach(),s.getTacGia(),s.getNamXB(),s.getTinhTrang()});
        lblSoSach.setText(list.size() + " quyển");
        cmbSach.removeAllItems();
        for (Sach s : dao.getAllSach()) if ("Còn".equals(s.getTinhTrang())) cmbSach.addItem(s);
        lblStatus.setText("Sách: " + list.size());
    }

    private void loadDocGia(List<DocGia> list) {
        modelDG.setRowCount(0);
        for (DocGia dg : list) modelDG.addRow(new Object[]{dg.getId(),dg.getHoTen(),dg.getNgaySinh()});
        lblSoDG.setText(list.size() + " người");
        cmbDG.removeAllItems();
        for (DocGia dg : list) cmbDG.addItem(dg);
    }

    private void refreshMuon() {
        modelMuon.setRowCount(0);
        for (MuonSach m : dao.getAllMuon())
            modelMuon.addRow(new Object[]{m.getIdMuon(),m.getTenDocGia(),m.getTenSach(),m.getNgayMuon(),m.getNgayTra()});
    }

    private void loadThongKe() {
        modelTre.setRowCount(0);
        for (String[] r : dao.sachTreHan())    modelTre.addRow(r);
        modelTop.setRowCount(0);
        for (String[] r : dao.thongKeSachMuonNhieu()) modelTop.addRow(r);
    }

    private void fillFormSach() {
        int row = tableSach.getSelectedRow(); if (row<0) return;
        lblSachId.setText(String.valueOf(modelSach.getValueAt(row,0)));
        txtTenSach.setText((String) modelSach.getValueAt(row,1));
        txtTacGia.setText((String)  modelSach.getValueAt(row,2));
        txtNamXB.setText((String)   modelSach.getValueAt(row,3));
        cmbTinhTrang.setSelectedItem(modelSach.getValueAt(row,4));
    }

    private void saveSach() {
        String ten = txtTenSach.getText().trim();
        if (ten.isEmpty()) { JOptionPane.showMessageDialog(this,"Tên sách không được trống!","Lỗi",JOptionPane.ERROR_MESSAGE); return; }
        Sach s = new Sach(0, ten, txtTacGia.getText().trim(), txtNamXB.getText().trim(), (String)cmbTinhTrang.getSelectedItem());
        String idStr = lblSachId.getText();
        boolean ok;
        if (idStr.equals("(Mới)")) {
            ok = dao.addSach(s);
        } else {
            s.setId(Integer.parseInt(idStr));
            ok = dao.updateSach(s);
        }
        if (ok) { JOptionPane.showMessageDialog(this,"Lưu thành công!","OK",JOptionPane.INFORMATION_MESSAGE); loadSach(dao.getAllSach()); clearSach(); }
        else     JOptionPane.showMessageDialog(this,"Lưu thất bại!","Lỗi",JOptionPane.ERROR_MESSAGE);
    }

    private void deleteSach() {
        String idStr = lblSachId.getText();
        if (idStr.equals("(Mới)")) { JOptionPane.showMessageDialog(this,"Chọn sách trước!","Nhắc",JOptionPane.WARNING_MESSAGE); return; }
        if (JOptionPane.showConfirmDialog(this,"Xóa sách này?","Xác nhận",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
            if (dao.deleteSach(Integer.parseInt(idStr))) { loadSach(dao.getAllSach()); clearSach(); }
        }
    }

    private void clearSach() { lblSachId.setText("(Mới)"); txtTenSach.setText(""); txtTacGia.setText(""); txtNamXB.setText(""); cmbTinhTrang.setSelectedIndex(0); tableSach.clearSelection(); }

    // DocGia form
    private void fillFormDG() {
        int row = tableDG.getSelectedRow(); if(row<0) return;
        lblDGId.setText(String.valueOf(modelDG.getValueAt(row,0)));
        txtHoTen.setText((String) modelDG.getValueAt(row,1));
        txtNgaySinh.setText((String) modelDG.getValueAt(row,2));
    }

    private void saveDG() {
        String ten = txtHoTen.getText().trim();
        if (ten.isEmpty()) { JOptionPane.showMessageDialog(this,"Họ tên không được trống!","Lỗi",JOptionPane.ERROR_MESSAGE); return; }
        DocGia dg = new DocGia(0, ten, txtNgaySinh.getText().trim());
        String idStr = lblDGId.getText(); boolean ok;
        if (idStr.equals("(Mới)")) ok = dao.addDocGia(dg);
        else { dg.setId(Integer.parseInt(idStr)); ok = dao.updateDocGia(dg); }
        if (ok) { JOptionPane.showMessageDialog(this,"Lưu thành công!","OK",JOptionPane.INFORMATION_MESSAGE); loadDocGia(dao.getAllDocGia()); clearDG(); }
        else JOptionPane.showMessageDialog(this,"Lưu thất bại!","Lỗi",JOptionPane.ERROR_MESSAGE);
    }

    private void deleteDG() {
        String idStr = lblDGId.getText();
        if (idStr.equals("(Mới)")) return;
        if (JOptionPane.showConfirmDialog(this,"Xóa đọc giả này?","Xác nhận",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
            if (dao.deleteDocGia(Integer.parseInt(idStr))) { loadDocGia(dao.getAllDocGia()); clearDG(); }
        }
    }

    private void clearDG() { lblDGId.setText("(Mới)"); txtHoTen.setText(""); txtNgaySinh.setText(""); tableDG.clearSelection(); }

    private void muonSach() {
        DocGia dg = (DocGia) cmbDG.getSelectedItem();
        Sach   s  = (Sach)   cmbSach.getSelectedItem();
        if (dg==null||s==null) { JOptionPane.showMessageDialog(this,"Chọn đọc giả và sách!","Nhắc",JOptionPane.WARNING_MESSAGE); return; }
        if (dao.muonSach(dg.getId(), s.getId())) { JOptionPane.showMessageDialog(this,"Mượn sách thành công!","OK",JOptionPane.INFORMATION_MESSAGE); refreshAll(); }
        else JOptionPane.showMessageDialog(this,"Mượn thất bại!","Lỗi",JOptionPane.ERROR_MESSAGE);
    }

    private void traSach() {
        String idMuonStr = JOptionPane.showInputDialog(this, "Nhập ID Mượn cần trả sách:", "Trả sách", JOptionPane.QUESTION_MESSAGE);
        if (idMuonStr == null || idMuonStr.trim().isEmpty()) return;
        try {
            int idMuon = Integer.parseInt(idMuonStr.trim());
            int idSach = -1;
            for (int i = 0; i < modelMuon.getRowCount(); i++) {
                if (String.valueOf(modelMuon.getValueAt(i,0)).equals(idMuonStr.trim())) {
                    String tenSach = (String) modelMuon.getValueAt(i,2);
                    for (Sach s : dao.getAllSach()) { if (s.getTenSach().equals(tenSach)) { idSach=s.getId(); break; } }
                    break;
                }
            }
            if (idSach < 0) { JOptionPane.showMessageDialog(this,"Không tìm thấy ID mượn!","Lỗi",JOptionPane.ERROR_MESSAGE); return; }
            if (dao.traSach(idMuon, idSach)) { JOptionPane.showMessageDialog(this,"Trả sách thành công!","OK",JOptionPane.INFORMATION_MESSAGE); refreshAll(); }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,"ID Mượn phải là số!","Lỗi",JOptionPane.ERROR_MESSAGE);
        }
    }

    private JLabel lbl(String t) { JLabel l = new JLabel(t); l.setFont(F_BOLD); return l; }

    private JTextField field(int cols) {
        JTextField f = new JTextField(cols); f.setFont(F_NORM);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(C_BORDER), BorderFactory.createEmptyBorder(4,8,4,8)));
        return f;
    }

    private void row(JPanel p, GridBagConstraints gc, int y, String label, Component comp) {
        gc.gridy=y; gc.gridx=0; gc.weightx=0; gc.gridwidth=1;
        p.add(lbl(label), gc);
        gc.gridx=1; gc.weightx=1;
        p.add(comp, gc);
    }

    private JButton btn(String text, Color bg, Color hover) {
        JButton b = new JButton(text) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover()?hover:bg);
                g2.fillRoundRect(0,0,getWidth(),getHeight(),8,8);
                g2.dispose(); super.paintComponent(g);
            }
        };
        b.setFont(F_BTN); b.setForeground(Color.WHITE);
        b.setContentAreaFilled(false); b.setBorderPainted(false);
        b.setFocusPainted(false); b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(7,14,7,14)); return b;
    }

    private void styleTable(JTable t) {
        t.setFont(F_NORM); t.setRowHeight(30); t.setGridColor(C_BORDER); t.setShowGrid(true);
        t.setBackground(C_WHITE); t.setSelectionBackground(C_ROW_SEL);
        t.getTableHeader().setFont(F_BOLD);
        t.getTableHeader().setBackground(new Color(241,245,249));
        t.getTableHeader().setForeground(C_TEXT);
        t.setDefaultRenderer(Object.class, new DefaultTableCellRenderer(){
            public Component getTableCellRendererComponent(JTable tbl,Object val,boolean sel,boolean focus,int row,int col){
                Component c=super.getTableCellRendererComponent(tbl,val,sel,focus,row,col);
                c.setBackground(sel?C_ROW_SEL:(row%2==0?C_WHITE:C_ROW_ALT));
                setBorder(new EmptyBorder(0,8,0,8)); return c;
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ThuVienForm().setVisible(true);
        });
    }
}
