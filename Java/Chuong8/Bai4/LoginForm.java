package Bai4;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class LoginForm extends JFrame {

    private final NhanVienDAO dao = new NhanVienDAO();
    private JTextField     txtUser;
    private JPasswordField txtPass;
    private JLabel         lblMsg;

    static final Color BG     = new Color(13,  17,  23);
    static final Color PANEL  = new Color(22,  27,  34);
    static final Color CARD   = new Color(33,  38,  45);
    static final Color ACCENT = new Color(35, 134,  54);
    static final Color INDIGO = new Color(88, 166,255);
    static final Color TEXT   = new Color(230,237,243);
    static final Color SUBTEXT= new Color(139,148,158);
    static final Color DANGER = new Color(248, 81, 73);

    public LoginForm() {
        setTitle("Đăng Nhập Hệ Thống Quản Lý Nhân Viên");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(420, 520);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(BG);
        setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel(new GridLayout(2, 1));
        header.setBackground(ACCENT);
        header.setPreferredSize(new Dimension(0, 100));
        header.setBorder(new EmptyBorder(12, 0, 12, 0));
        JLabel ico = new JLabel("🏢", SwingConstants.CENTER);
        ico.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        ico.setForeground(TEXT);
        JLabel title = new JLabel("QUẢN LÝ NHÂN VIÊN", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(TEXT);
        header.add(ico);
        header.add(title);
        add(header, BorderLayout.NORTH);

        // Card
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(PANEL);
        card.setBorder(new EmptyBorder(30, 40, 30, 40));

        JLabel loginTitle = new JLabel("Đăng Nhập Quản Trị");
        loginTitle.setForeground(INDIGO);
        loginTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        loginTitle.setAlignmentX(CENTER_ALIGNMENT);
        card.add(loginTitle);
        card.add(Box.createVerticalStrut(24));

        // Username
        card.add(makeLabel("Tên đăng nhập"));
        card.add(Box.createVerticalStrut(4));
        txtUser = makeField();
        txtUser.setText("admin");
        card.add(txtUser);
        card.add(Box.createVerticalStrut(14));

        // Password
        card.add(makeLabel("Mật khẩu"));
        card.add(Box.createVerticalStrut(4));
        txtPass = new JPasswordField();
        styleInput(txtPass);
        card.add(txtPass);
        card.add(Box.createVerticalStrut(22));

        // Login button
        JButton btnLogin = new JButton("  Đăng Nhập  ");
        btnLogin.setBackground(ACCENT);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnLogin.setAlignmentX(CENTER_ALIGNMENT);
        btnLogin.addActionListener(e -> doLogin());
        card.add(btnLogin);
        card.add(Box.createVerticalStrut(10));

        // Error msg
        lblMsg = new JLabel(" ");
        lblMsg.setForeground(DANGER);
        lblMsg.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblMsg.setAlignmentX(CENTER_ALIGNMENT);
        card.add(lblMsg);
        card.add(Box.createVerticalStrut(10));

        JLabel hint = new JLabel("Default: admin / admin123");
        hint.setForeground(SUBTEXT);
        hint.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        hint.setAlignmentX(CENTER_ALIGNMENT);
        card.add(hint);

        add(card, BorderLayout.CENTER);

        // Enter key
        getRootPane().setDefaultButton(btnLogin);
    }

    private void doLogin() {
        String user = txtUser.getText().trim();
        String pass = new String(txtPass.getPassword()).trim();
        String role = dao.login(user, pass);
        if (role != null) {
            dispose();
            new QuanLyNhanVienForm(user, role).setVisible(true);
        } else {
            lblMsg.setText("❌  Sai tên đăng nhập hoặc mật khẩu!");
        }
    }

    private JLabel makeLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(SUBTEXT);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setAlignmentX(CENTER_ALIGNMENT);
        return lbl;
    }

    private JTextField makeField() {
        JTextField tf = new JTextField();
        styleInput(tf);
        return tf;
    }

    private void styleInput(JTextField tf) {
        tf.setBackground(CARD);
        tf.setForeground(TEXT);
        tf.setCaretColor(TEXT);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(48,54,61)),
            new EmptyBorder(8, 10, 8, 10)
        ));
        tf.setMaximumSize(new Dimension(300, 40)); 
        tf.setAlignmentX(CENTER_ALIGNMENT);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }
}
