package Bai6;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

public class MainForm extends JFrame {

    private static final Color C_BG      = new Color(248, 250, 252);
    private static final Color C_SIDE    = new Color(15,  23,  42);
    private static final Color C_WHITE   = Color.WHITE;
    private static final Color C_BORDER  = new Color(226, 232, 240);
    private static final Color C_BLUE    = new Color(37,  99, 235);
    private static final Color C_BLUE_DK = new Color(29,  78, 216);
    private static final Color C_GREEN   = new Color(22, 163,  74);
    private static final Color C_RED     = new Color(220,  38,  38);
    private static final Color C_TEXT    = new Color(15,  23,  42);
    private static final Color C_MUTED   = new Color(100, 116, 140);
    private static final Color C_ROW_ALT = new Color(248, 250, 252);
    private static final Color C_ROW_SEL = new Color(219, 234, 254);

    private static final Font F_TITLE = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font F_BOLD  = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font F_NORM  = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font F_SMALL = new Font("Segoe UI", Font.PLAIN, 11);
    private static final Font F_BTN   = new Font("Segoe UI", Font.BOLD, 12);

    private final String       username;
    private final String       hoTen;
    private final NguoiDungDAO dao;

    private JPasswordField txtCu, txtMoi, txtNhapLai;
    private JLabel         lblMsg, lblStatus;
    private DefaultTableModel modelLog;

    public MainForm(String username, String hoTen, NguoiDungDAO dao) {
        this.username = username;
        this.hoTen    = hoTen;
        this.dao      = dao;

        setTitle("Hệ Thống Quản Lý Tài Khoản — " + hoTen);
        setSize(1000, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { confirmLogout(); }
        });
        setLayout(new BorderLayout());

        add(buildSidebar(),   BorderLayout.WEST);
        add(buildContent(),   BorderLayout.CENTER);
        add(buildStatusBar(), BorderLayout.SOUTH);

        loadLog();
        setVisible(true);
    }

    private JPanel buildSidebar() {
        JPanel p = new JPanel();
        p.setBackground(C_SIDE);
        p.setPreferredSize(new Dimension(210, 0));
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        // Logo
        JPanel logo = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 18));
        logo.setBackground(C_SIDE); logo.setMaximumSize(new Dimension(210, 64));
        JLabel ico = new JLabel("👤"); ico.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 26));
        JLabel nm  = new JLabel("<html><b style='color:white;font-size:12px'>Tài Khoản</b><br>" +
                                "<span style='color:#64748b;font-size:10px'>XAMPP • MySQL</span></html>");
        logo.add(ico); logo.add(nm);
        p.add(logo);
        p.add(sep());

        // User info
        JPanel info = new JPanel(new BorderLayout(8,0));
        info.setBackground(C_SIDE); info.setMaximumSize(new Dimension(210,60));
        info.setBorder(new EmptyBorder(10,14,10,14));
        JPanel txt = new JPanel(new GridLayout(2,1)); txt.setBackground(C_SIDE);
        JLabel l1 = new JLabel(hoTen); l1.setFont(new Font("Segoe UI",Font.BOLD,13)); l1.setForeground(Color.WHITE);
        JLabel l2 = new JLabel("@" + username); l2.setFont(F_SMALL); l2.setForeground(new Color(100,116,140));
        txt.add(l1); txt.add(l2);
        info.add(txt, BorderLayout.CENTER);
        p.add(info);
        p.add(sep());

        p.add(Box.createVerticalGlue());
        JButton btnOut = new JButton("Đăng Xuất");
        btnOut.setFont(F_BTN); btnOut.setForeground(new Color(248,113,113));
        btnOut.setBackground(C_SIDE); btnOut.setBorderPainted(false); btnOut.setFocusPainted(false);
        btnOut.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnOut.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnOut.setBorder(new EmptyBorder(10,14,10,14));
        btnOut.addActionListener(e -> confirmLogout());
        p.add(btnOut);
        return p;
    }

    private JSeparator sep() {
        JSeparator s = new JSeparator(); s.setForeground(new Color(30,41,59));
        s.setMaximumSize(new Dimension(210,1)); return s;
    }

    private JTabbedPane buildContent() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(F_BOLD); tabs.setBackground(C_BG);
        tabs.addTab("Đổi Mật Khẩu", buildTabDoiMK());
        tabs.addTab("Lịch Sử Đăng Nhập", buildTabLog());
        return tabs;
    }

    private JPanel buildTabDoiMK() {
        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(C_BG); root.setBorder(new EmptyBorder(32, 80, 32, 80));

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(C_WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(C_BORDER), new EmptyBorder(30, 30, 30, 30)));

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(8,8,8,8); gc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        gc.gridx=0; gc.gridy=0; gc.gridwidth=2;
        JLabel title = new JLabel("Đổi Mật Khẩu");
        title.setFont(F_TITLE); title.setForeground(C_TEXT);
        card.add(title, gc);

        gc.gridy=1; gc.gridwidth=1;
        JLabel hint = new JLabel("Tài khoản: @" + username);
        hint.setFont(F_SMALL); hint.setForeground(C_MUTED);
        card.add(hint, gc);

        gc.gridy=2; gc.gridwidth=2;
        card.add(new JSeparator(), gc);

        // Fields
        gc.gridwidth=1; gc.weightx=0;
        gc.gridx=0; gc.gridy=3; card.add(lbl("Mật khẩu hiện tại:"), gc);
        gc.gridx=1; gc.weightx=1; txtCu = passField(); card.add(txtCu, gc);

        gc.gridx=0; gc.gridy=4; gc.weightx=0; card.add(lbl("Mật khẩu mới:"), gc);
        gc.gridx=1; gc.weightx=1; txtMoi = passField(); card.add(txtMoi, gc);

        gc.gridx=0; gc.gridy=5; gc.weightx=0; card.add(lbl("Nhập lại mật khẩu mới:"), gc);
        gc.gridx=1; gc.weightx=1; txtNhapLai = passField(); card.add(txtNhapLai, gc);

        gc.gridx=0; gc.gridy=6; gc.gridwidth=2;
        lblMsg = new JLabel(" ");
        lblMsg.setFont(F_BOLD); lblMsg.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(lblMsg, gc);

        gc.gridy=7;
        JButton btnDoi = roundBtn("Đổi Mật Khẩu", C_BLUE, C_BLUE_DK);
        JButton btnClear = roundBtn("Xóa trắng", new Color(100,116,140), new Color(71,85,105));
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btns.setBackground(C_WHITE);
        btnClear.addActionListener(e -> { txtCu.setText(""); txtMoi.setText(""); txtNhapLai.setText(""); lblMsg.setText(" "); });
        btnDoi.addActionListener(e -> doDoiMK());
        btns.add(btnClear); btns.add(btnDoi);
        card.add(btns, gc);

        root.add(card);
        return root;
    }

    private JPanel buildTabLog() {
        JPanel root = new JPanel(new BorderLayout(0,10));
        root.setBackground(C_BG); root.setBorder(new EmptyBorder(16,16,16,16));
        JLabel title = new JLabel("Lịch sử đăng nhập");
        title.setFont(F_TITLE); title.setForeground(C_TEXT);
        root.add(title, BorderLayout.NORTH);

        String[] cols = {"Tài khoản","Thời gian","Kết quả"};
        modelLog = new DefaultTableModel(cols,0){public boolean isCellEditable(int r,int c){return false;}};
        JTable tbl = new JTable(modelLog);
        tbl.setDefaultRenderer(Object.class, new DefaultTableCellRenderer(){
            public Component getTableCellRendererComponent(JTable t,Object v,boolean sel,boolean focus,int row,int col){
                Component c=super.getTableCellRendererComponent(t,v,sel,focus,row,col);
                String kq=(String)modelLog.getValueAt(row,2);
                if (!sel) c.setBackground("Thất bại".equals(kq)?new Color(254,202,202):C_WHITE);
                setBorder(new EmptyBorder(0,8,0,8)); return c;
            }
        });
        styleTable(tbl);
        JScrollPane sp = new JScrollPane(tbl); sp.setBorder(BorderFactory.createLineBorder(C_BORDER));
        root.add(sp, BorderLayout.CENTER);

        JButton btnRef = roundBtn("Làm mới", C_BLUE, C_BLUE_DK);
        btnRef.addActionListener(e -> loadLog());
        JPanel bot = new JPanel(new FlowLayout(FlowLayout.RIGHT)); bot.setBackground(C_BG); bot.add(btnRef);
        root.add(bot, BorderLayout.SOUTH);
        return root;
    }

    private JPanel buildStatusBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT,12,4));
        bar.setBackground(new Color(241,245,249));
        bar.setBorder(BorderFactory.createMatteBorder(1,0,0,0,C_BORDER));
        lblStatus = new JLabel("Đã đăng nhập: " + hoTen);
        lblStatus.setFont(F_SMALL); lblStatus.setForeground(C_MUTED);
        bar.add(lblStatus); return bar;
    }

    private void doDoiMK() {
        String cu      = new String(txtCu.getPassword());
        String moi     = new String(txtMoi.getPassword());
        String nhapLai = new String(txtNhapLai.getPassword());
        if (cu.isEmpty() || moi.isEmpty() || nhapLai.isEmpty()) {
            msg("Vui lòng nhập đầy đủ!", C_RED); return;
        }
        if (!moi.equals(nhapLai)) {
            msg("Mật khẩu mới không khớp!", C_RED); return;
        }
        if (moi.length() < 6) {
            msg("Mật khẩu mới phải ít nhất 6 ký tự!", C_RED); return;
        }
        if (dao.doiMatKhau(username, cu, moi)) {
            msg("Đổi mật khẩu thành công!", C_GREEN);
            txtCu.setText(""); txtMoi.setText(""); txtNhapLai.setText("");
            lblStatus.setText("Đổi mật khẩu lúc: " + new java.util.Date());
        } else {
            msg("Mật khẩu hiện tại không đúng!", C_RED);
        }
    }

    private void msg(String text, Color color) { lblMsg.setText(text); lblMsg.setForeground(color); }

    private void loadLog() {
        modelLog.setRowCount(0);
        for (String[] row : dao.getLog()) modelLog.addRow(row);
        lblStatus.setText("Log: " + modelLog.getRowCount() + " bản ghi.");
    }

    private void confirmLogout() {
        int ok = JOptionPane.showConfirmDialog(this,
            "Bạn có chắc muốn đăng xuất?", "Xác nhận đăng xuất", JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION) { dispose(); new LoginForm(); }
    }

    private JLabel lbl(String t) { JLabel l = new JLabel(t); l.setFont(F_BOLD); l.setForeground(C_TEXT); return l; }

    private JPasswordField passField() {
        JPasswordField f = new JPasswordField(); f.setFont(F_NORM);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(C_BORDER), BorderFactory.createEmptyBorder(6,10,6,10)));
        return f;
    }

    private JButton roundBtn(String text, Color bg, Color hover) {
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
        b.setBorder(new EmptyBorder(8,16,8,16)); return b;
    }

    private void styleTable(JTable t) {
        t.setFont(F_NORM); t.setRowHeight(30); t.setGridColor(C_BORDER); t.setShowGrid(true);
        t.setBackground(C_WHITE); t.setSelectionBackground(C_ROW_SEL);
        t.getTableHeader().setFont(F_BOLD);
        t.getTableHeader().setBackground(new Color(241,245,249)); t.getTableHeader().setForeground(C_TEXT);
    }
}
