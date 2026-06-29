package Bai6;

import javax.swing.*;
import javax.swing.border.*;

import Bai5.ThuVienForm;
import java.awt.*;
import java.awt.event.*;

public class LoginForm extends JFrame {

    private static final Color C_BG   = new Color(248, 250, 252);
    private static final Color C_SIDE = new Color(15,  23,  42);
    private static final Color C_BLUE = new Color(37,  99, 235);
    private static final Color C_RED  = new Color(220,  38,  38);
    private static final Color C_TEXT = new Color(15,  23,  42);
    private static final Font  F_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font  F_NORM = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font  F_BTN  = new Font("Segoe UI", Font.BOLD, 13);

    private final NguoiDungDAO dao = new NguoiDungDAO();

    private JTextField     txtUser;
    private JPasswordField txtPass;
    private JLabel         lblError;

    public LoginForm() {
        setTitle("Đăng Nhập Hệ Thống");
        setSize(420, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());

        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 24));
        header.setBackground(C_SIDE);
        JLabel ico = new JLabel("🔐"); ico.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 34));
        JPanel txt = new JPanel(new GridLayout(2,1)); txt.setBackground(C_SIDE);
        JLabel t1  = new JLabel("Đăng Nhập Hệ Thống");
        t1.setFont(new Font("Segoe UI", Font.BOLD, 15)); t1.setForeground(Color.WHITE);
        JLabel t2  = new JLabel("Mật khẩu được mã hóa MD5");
        t2.setFont(new Font("Segoe UI", Font.PLAIN, 11)); t2.setForeground(new Color(100,116,140));
        txt.add(t1); txt.add(t2);
        header.add(ico); header.add(txt);
        add(header, BorderLayout.NORTH);

        // ── Form ────────────────────────────────────────────────────────────
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(C_BG); form.setBorder(new EmptyBorder(24, 36, 24, 36));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(7, 6, 7, 6); gc.fill = GridBagConstraints.HORIZONTAL;

        gc.gridx=0; gc.gridy=0; gc.weightx=0;
        form.add(labelBold("Tên đăng nhập:"), gc);
        gc.gridx=1; gc.weightx=1;
        txtUser = textField(); txtUser.setText("admin");
        form.add(txtUser, gc);

        gc.gridx=0; gc.gridy=1; gc.weightx=0;
        form.add(labelBold("Mật khẩu:"), gc);
        gc.gridx=1; gc.weightx=1;
        txtPass = new JPasswordField();
        txtPass.setFont(F_NORM);
        txtPass.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226,232,240)),
            BorderFactory.createEmptyBorder(6,10,6,10)));
        form.add(txtPass, gc);

        gc.gridx=0; gc.gridy=2; gc.gridwidth=2;
        lblError = new JLabel(" ");
        lblError.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblError.setForeground(C_RED);
        lblError.setHorizontalAlignment(SwingConstants.CENTER);
        form.add(lblError, gc);

        gc.gridy=3;
        JButton btnLogin = roundBtn("Đăng Nhập", C_BLUE, new Color(29,78,216));
        btnLogin.addActionListener(e -> doLogin());
        form.add(btnLogin, gc);

        add(form, BorderLayout.CENTER);
        getRootPane().setDefaultButton(btnLogin);
        setVisible(true);
    }

    private void doLogin() {
        String user = txtUser.getText().trim();
        String pass = new String(txtPass.getPassword());
        if (user.isEmpty() || pass.isEmpty()) { lblError.setText("Vui lòng nhập đầy đủ!"); return; }
        String hoTen = dao.login(user, pass);
        if (hoTen != null) {
            dao.ghiLog(user, true);
            dispose();
            new MainForm(user, hoTen, dao);
        } else {
            dao.ghiLog(user, false);
            lblError.setText("Sai tên đăng nhập hoặc mật khẩu!");
            txtPass.setText("");
        }
    }

    private JLabel labelBold(String t) { JLabel l = new JLabel(t); l.setFont(F_BOLD); l.setForeground(new Color(15,23,42)); return l; }

    private JTextField textField() {
        JTextField f = new JTextField(); f.setFont(F_NORM);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226,232,240)),
            BorderFactory.createEmptyBorder(6,10,6,10)));
        return f;
    }

    private JButton roundBtn(String text, Color bg, Color hover) {
        JButton b = new JButton(text) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? hover : bg);
                g2.fillRoundRect(0,0,getWidth(),getHeight(),8,8);
                g2.dispose(); super.paintComponent(g);
            }
        };
        b.setFont(F_BTN); b.setForeground(Color.WHITE);
        b.setContentAreaFilled(false); b.setBorderPainted(false);
        b.setFocusPainted(false); b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(10,20,10,20)); return b;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginForm().setVisible(true);
        });
    }
}
