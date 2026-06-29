package Bai3;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import Bai2.QuanLyBanHangForm;

import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class QuizGameForm extends JFrame {

    private static final Color C_BG      = new Color(248, 250, 252);
    private static final Color C_SIDE    = new Color(15,  23,  42);
    private static final Color C_WHITE   = Color.WHITE;
    private static final Color C_BORDER  = new Color(226, 232, 240);
    private static final Color C_BLUE    = new Color(37,  99,  235);
    private static final Color C_BLUE_DK = new Color(29,  78,  216);
    private static final Color C_GREEN   = new Color(22,  163,  74);
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
    private static final Font F_Q     = new Font("Segoe UI", Font.BOLD, 15);

    private final CauHoiDAO dao = new CauHoiDAO();
    private List<CauHoi>    dsCauHoi;
    private int  cauHienTai = 0;
    private int  diem       = 0;
    private boolean[] ketQua; 

    private JLabel       lblSoCau, lblDiem, lblStatus;
    private JLabel       lblNoiDung;
    private JRadioButton rbA, rbB, rbC, rbD;
    private ButtonGroup  bg;
    private JButton      btnXacNhan, btnTiep, btnChoi;
    private JLabel       lblHint;
    private JPanel       pnlAnswer;
    private JTabbedPane  tabs;
    private DefaultTableModel modelLS;
    private JTextField   txtTen;

    public QuizGameForm() {
        setTitle("🎯 Game Đố Vui – Quiz Game");
        setSize(1100, 680);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(buildSidebar(), BorderLayout.WEST);
        add(buildContent(), BorderLayout.CENTER);
        add(buildStatusBar(), BorderLayout.SOUTH);

        loadDuLieu();
        setVisible(true);
    }

    private JPanel buildSidebar() {
        JPanel p = new JPanel();
        p.setBackground(C_SIDE);
        p.setPreferredSize(new Dimension(200, 0));
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        JPanel logo = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 18));
        logo.setBackground(C_SIDE);
        logo.setMaximumSize(new Dimension(200, 64));
        JLabel ico = new JLabel("🎯");
        ico.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 26));
        JLabel nm = new JLabel("<html><b style='color:white;font-size:12px'>Quiz Game</b><br>" +
                               "<span style='color:#64748b;font-size:10px'>XAMPP • MySQL</span></html>");
        logo.add(ico); logo.add(nm);
        p.add(logo);
        p.add(sideDiv());

        lblSoCau = sideInfoLabel();
        lblDiem  = sideInfoLabel();
        p.add(sideInfoPanel("📋", "Câu hỏi", lblSoCau));
        p.add(sideInfoPanel("⭐", "Điểm",     lblDiem));
        p.add(sideDiv());
        p.add(Box.createVerticalGlue());

        JLabel ver = new JLabel("  v1.0 — Java Swing");
        ver.setFont(F_SMALL);
        ver.setForeground(new Color(51, 65, 85));
        ver.setBorder(new EmptyBorder(0, 0, 12, 0));
        ver.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(ver);
        return p;
    }

    private JSeparator sideDiv() {
        JSeparator s = new JSeparator();
        s.setForeground(new Color(30, 41, 59));
        s.setMaximumSize(new Dimension(200, 1));
        return s;
    }

    private JLabel sideInfoLabel() {
        JLabel l = new JLabel("—");
        l.setFont(new Font("Segoe UI", Font.BOLD, 13));
        l.setForeground(Color.WHITE);
        return l;
    }

    private JPanel sideInfoPanel(String icon, String label, JLabel valLabel) {
        JPanel p = new JPanel(new BorderLayout(8, 0));
        p.setBackground(C_SIDE);
        p.setMaximumSize(new Dimension(200, 52));
        p.setBorder(new EmptyBorder(8, 14, 8, 14));
        JLabel ico = new JLabel(icon);
        ico.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        JPanel txt = new JPanel(new GridLayout(2, 1));
        txt.setBackground(C_SIDE);
        JLabel lbl = new JLabel(label);
        lbl.setFont(F_SMALL);
        lbl.setForeground(new Color(100, 116, 140));
        txt.add(lbl); txt.add(valLabel);
        p.add(ico, BorderLayout.WEST);
        p.add(txt, BorderLayout.CENTER);
        return p;
    }

    private JTabbedPane buildContent() {
        tabs = new JTabbedPane();
        tabs.setFont(F_BOLD);
        tabs.setBackground(C_BG);
        tabs.addTab("Chơi Game", buildTabGame());
        tabs.addTab("Lịch Sử",  buildTabLichSu());
        return tabs;
    }

    private JPanel buildTabGame() {
        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBackground(C_BG);
        root.setBorder(new EmptyBorder(16, 16, 16, 16));

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        topBar.setBackground(C_BG);
        topBar.add(label("Người chơi:", F_BOLD, C_TEXT));
        txtTen = new JTextField(14);
        txtTen.setFont(F_NORM);
        txtTen.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(C_BORDER),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        txtTen.setText("Người chơi 1");
        topBar.add(txtTen);
        btnChoi = roundBtn("Bắt Đầu", C_BLUE, C_BLUE_DK);
        btnChoi.addActionListener(e -> batDauChoi());
        topBar.add(btnChoi);
        root.add(topBar, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(0, 14));
        center.setBackground(C_BG);

        JPanel cardQ = new JPanel(new BorderLayout(0, 10));
        cardQ.setBackground(C_WHITE);
        cardQ.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(C_BORDER),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        lblNoiDung = new JLabel("<html><body style='width:500px'>Nhấn \"Bắt Đầu\" để chơi!</body></html>");
        lblNoiDung.setFont(F_Q);
        lblNoiDung.setForeground(C_TEXT);
        cardQ.add(lblNoiDung, BorderLayout.NORTH);

        pnlAnswer = new JPanel(new GridLayout(2, 2, 10, 10));
        pnlAnswer.setBackground(C_WHITE);
        bg  = new ButtonGroup();
        rbA = answerBtn("A. —"); rbB = answerBtn("B. —");
        rbC = answerBtn("C. —"); rbD = answerBtn("D. —");
        bg.add(rbA); bg.add(rbB); bg.add(rbC); bg.add(rbD);
        pnlAnswer.add(rbA); pnlAnswer.add(rbB);
        pnlAnswer.add(rbC); pnlAnswer.add(rbD);
        cardQ.add(pnlAnswer, BorderLayout.CENTER);

        lblHint = new JLabel(" ");
        lblHint.setFont(F_BOLD);
        lblHint.setHorizontalAlignment(SwingConstants.CENTER);
        cardQ.add(lblHint, BorderLayout.SOUTH);

        center.add(cardQ, BorderLayout.CENTER);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnRow.setBackground(C_BG);
        btnXacNhan = roundBtn("Xác Nhận", C_GREEN, new Color(15, 118, 54));
        btnTiep    = roundBtn("Tiếp Theo", C_BLUE,  C_BLUE_DK);
        btnXacNhan.setEnabled(false);
        btnTiep.setEnabled(false);
        btnXacNhan.addActionListener(e -> xacNhan());
        btnTiep.addActionListener(e    -> tiepTheo());
        btnRow.add(btnXacNhan); btnRow.add(btnTiep);
        center.add(btnRow, BorderLayout.SOUTH);

        root.add(center, BorderLayout.CENTER);
        return root;
    }

    private JRadioButton answerBtn(String text) {
        JRadioButton rb = new JRadioButton(text);
        rb.setFont(F_NORM);
        rb.setBackground(C_WHITE);
        rb.setForeground(C_TEXT);
        rb.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(C_BORDER),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        rb.setOpaque(true);
        return rb;
    }

    private JPanel buildTabLichSu() {
        JPanel root = new JPanel(new BorderLayout(0, 10));
        root.setBackground(C_BG);
        root.setBorder(new EmptyBorder(16, 16, 16, 16));

        JLabel title = new JLabel("Lịch sử chơi game");
        title.setFont(F_TITLE);
        title.setForeground(C_TEXT);
        root.add(title, BorderLayout.NORTH);

        String[] cols = {"Người chơi", "Kết quả", "Thời gian"};
        modelLS = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tbl = new JTable(modelLS);
        styleTable(tbl);
        JScrollPane sp = new JScrollPane(tbl);
        sp.setBorder(BorderFactory.createLineBorder(C_BORDER));
        root.add(sp, BorderLayout.CENTER);

        JButton btnRefresh = roundBtn("Làm mới", C_BLUE, C_BLUE_DK);
        btnRefresh.addActionListener(e -> loadLichSu());
        JPanel bot = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bot.setBackground(C_BG);
        bot.add(btnRefresh);
        root.add(bot, BorderLayout.SOUTH);

        return root;
    }

    private JPanel buildStatusBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 4));
        bar.setBackground(new Color(241, 245, 249));
        bar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, C_BORDER));
        lblStatus = new JLabel("Sẵn sàng");
        lblStatus.setFont(F_SMALL);
        lblStatus.setForeground(C_MUTED);
        bar.add(lblStatus);
        return bar;
    }

    private void loadDuLieu() {
        dsCauHoi = dao.getAll();
        lblSoCau.setText(dsCauHoi.size() + " câu");
        lblStatus.setText("Đã tải " + dsCauHoi.size() + " câu hỏi từ CSDL.");
    }

    private void batDauChoi() {
        if (dsCauHoi == null || dsCauHoi.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có câu hỏi trong CSDL!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        cauHienTai = 0;
        diem       = 0;
        ketQua     = new boolean[dsCauHoi.size()];
        lblDiem.setText("0/" + dsCauHoi.size());
        hienThiCau(cauHienTai);
        btnXacNhan.setEnabled(true);
        btnChoi.setEnabled(false);
        lblStatus.setText("Game bắt đầu! Người chơi: " + txtTen.getText());
    }

    private void hienThiCau(int idx) {
        CauHoi cq = dsCauHoi.get(idx);
        lblNoiDung.setText("<html><body style='width:500px'><b>Câu " + (idx + 1) + "/" +
                dsCauHoi.size() + ":</b> " + cq.getNoiDung() + "</body></html>");
        rbA.setText("A. " + cq.getA()); rbB.setText("B. " + cq.getB());
        rbC.setText("C. " + cq.getC()); rbD.setText("D. " + cq.getD());
        bg.clearSelection();
        resetAnswerColors();
        lblHint.setText(" ");
        lblHint.setForeground(C_TEXT);
        btnXacNhan.setEnabled(true);
        btnTiep.setEnabled(false);
    }

    private void xacNhan() {
        String chon = getChon();
        if (chon == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn đáp án!", "Nhắc nhở", JOptionPane.WARNING_MESSAGE);
            return;
        }
        CauHoi cq    = dsCauHoi.get(cauHienTai);
        boolean dung = chon.equalsIgnoreCase(cq.getDapAn());
        ketQua[cauHienTai] = dung;
        if (dung) diem++;

        // Tô màu đáp án
        highlightAnswer(cq.getDapAn(), chon);
        if (dung) {
            lblHint.setText("Chính xác!");
            lblHint.setForeground(C_GREEN);
        } else {
            lblHint.setText("Sai! Đáp án đúng: " + cq.getDapAn());
            lblHint.setForeground(C_RED);
        }

        lblDiem.setText(diem + "/" + dsCauHoi.size());
        btnXacNhan.setEnabled(false);
        boolean lastCau = (cauHienTai == dsCauHoi.size() - 1);
        btnTiep.setText(lastCau ? "Kết quả" : "Tiếp Theo");
        btnTiep.setEnabled(true);
    }

    private void tiepTheo() {
        if (cauHienTai < dsCauHoi.size() - 1) {
            cauHienTai++;
            hienThiCau(cauHienTai);
        } else {
            String tenNguoi = txtTen.getText().trim();
            if (tenNguoi.isEmpty()) tenNguoi = "Ẩn danh";
            dao.ghiLichSu(tenNguoi, diem, dsCauHoi.size());
            loadLichSu();

            StringBuilder sb = new StringBuilder();
            sb.append("<html><b>Kết quả:</b> ").append(diem).append("/").append(dsCauHoi.size()).append("<br><br>");
            for (int i = 0; i < ketQua.length; i++) {
                sb.append("Câu ").append(i + 1).append(": ")
                  .append(ketQua[i] ? "<font color='green'>✅ Đúng</font>" : "<font color='red'>❌ Sai</font>")
                  .append("<br>");
            }
            sb.append("</html>");
            JOptionPane.showMessageDialog(this, new JLabel(sb.toString()), "🏁 Kết quả", JOptionPane.INFORMATION_MESSAGE);

            btnChoi.setEnabled(true);
            btnXacNhan.setEnabled(false);
            btnTiep.setEnabled(false);
            lblNoiDung.setText("<html><body style='width:500px'>Nhấn \"Bắt Đầu\" để chơi lại!</body></html>");
            rbA.setText("A. —"); rbB.setText("B. —"); rbC.setText("C. —"); rbD.setText("D. —");
            bg.clearSelection();
            resetAnswerColors();
            lblHint.setText(" ");
            tabs.setSelectedIndex(1); 
        }
    }

    private String getChon() {
        if (rbA.isSelected()) return "A";
        if (rbB.isSelected()) return "B";
        if (rbC.isSelected()) return "C";
        if (rbD.isSelected()) return "D";
        return null;
    }

    private void highlightAnswer(String dapAn, String chon) {
        JRadioButton[] btns = {rbA, rbB, rbC, rbD};
        String[]       keys = {"A","B","C","D"};
        for (int i = 0; i < 4; i++) {
            if (keys[i].equalsIgnoreCase(dapAn)) {
                btns[i].setBackground(new Color(187, 247, 208)); // xanh
            } else if (keys[i].equalsIgnoreCase(chon)) {
                btns[i].setBackground(new Color(254, 202, 202)); // đỏ
            }
        }
    }

    private void resetAnswerColors() {
        for (JRadioButton rb : new JRadioButton[]{rbA, rbB, rbC, rbD}) rb.setBackground(C_WHITE);
    }

    private void loadLichSu() {
        modelLS.setRowCount(0);
        for (String[] row : dao.getLichSu()) modelLS.addRow(row);
        lblStatus.setText("Lịch sử: " + modelLS.getRowCount() + " lượt chơi.");
    }

    private JLabel label(String text, Font f, Color c) {
        JLabel l = new JLabel(text); l.setFont(f); l.setForeground(c); return l;
    }

    private JButton roundBtn(String text, Color bg, Color hover) {
        JButton b = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? hover : bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setFont(F_BTN);
        b.setForeground(Color.WHITE);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(7, 18, 7, 18));
        return b;
    }

    private void styleTable(JTable t) {
        t.setFont(F_NORM);
        t.setRowHeight(30);
        t.setGridColor(C_BORDER);
        t.setShowGrid(true);
        t.setBackground(C_WHITE);
        t.setSelectionBackground(C_ROW_SEL);
        t.getTableHeader().setFont(F_BOLD);
        t.getTableHeader().setBackground(new Color(241, 245, 249));
        t.getTableHeader().setForeground(C_TEXT);
        t.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable tbl, Object val, boolean sel, boolean focus, int row, int col) {
                Component c = super.getTableCellRendererComponent(tbl, val, sel, focus, row, col);
                c.setBackground(sel ? C_ROW_SEL : (row % 2 == 0 ? C_WHITE : C_ROW_ALT));
                setBorder(new EmptyBorder(0, 8, 0, 8));
                return c;
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new QuizGameForm().setVisible(true);
        });
    }
}
