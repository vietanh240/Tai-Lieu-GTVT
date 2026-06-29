import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Baii3 extends JFrame {
    private JTextField txtTaiKhoan;
    private JPasswordField txtMatKhau;
    private JButton btnDangNhap, btnThoat;

    private final String TAI_KHOAN_DUNG = "admin";
    private final String MAT_KHAU_DUNG  = "123456";

    private Font fontLabel = new Font("Segoe UI", Font.BOLD, 14);
    private Font fontInput = new Font("Segoe UI", Font.PLAIN, 14);
    private Color colorBg    = new Color(245, 248, 255);
    private Color colorTitle = new Color(33, 97, 140);
    private Color colorBtn   = new Color(41, 128, 185);
    private Color colorBtnRed = new Color(180, 60, 60);

    public Baii3() {
        setTitle("Đăng Nhập Hệ Thống");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 300);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(colorBg);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("ĐĂNG NHẬP HỆ THỐNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(colorTitle);
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(lblTitle, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        mainPanel.add(createLabel("Tài khoản:"), gbc);

        txtTaiKhoan = new JTextField();
        txtTaiKhoan.setFont(fontInput);
        txtTaiKhoan.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(174, 214, 241), 1),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.7;
        mainPanel.add(txtTaiKhoan, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.3;
        mainPanel.add(createLabel("Mật khẩu:"), gbc);

        txtMatKhau = new JPasswordField();
        txtMatKhau.setFont(fontInput);
        txtMatKhau.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(174, 214, 241), 1),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 0.7;
        mainPanel.add(txtMatKhau, gbc);

        JPanel pnBtn = new JPanel(new GridLayout(1, 2, 10, 0));
        pnBtn.setBackground(colorBg);

        btnDangNhap = createButton("Đăng nhập", colorBtn);
        btnThoat    = createButton("Thoát",      colorBtnRed);

        btnDangNhap.addActionListener(e -> dangNhap());
        btnThoat.addActionListener(e -> System.exit(0));

        txtMatKhau.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) dangNhap();
            }
        });

        pnBtn.add(btnDangNhap);
        pnBtn.add(btnThoat);

        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2; gbc.weightx = 1.0;
        gbc.insets = new Insets(20, 5, 8, 5);
        mainPanel.add(pnBtn, gbc);

        add(mainPanel);
    }

    private void dangNhap() {
        String taiKhoan = txtTaiKhoan.getText().trim();
        String matKhau  = new String(txtMatKhau.getPassword()).trim();

        if (taiKhoan.isEmpty() || matKhau.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ tài khoản và mật khẩu!", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (taiKhoan.equals(TAI_KHOAN_DUNG) && matKhau.equals(MAT_KHAU_DUNG)) {
            JOptionPane.showMessageDialog(this, "Đăng nhập thành công!\nXin chào, " + taiKhoan + "!","Thành công", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Tài khoản hoặc mật khẩu không đúng!\nVui lòng thử lại.", "Thất bại", JOptionPane.ERROR_MESSAGE);
            txtMatKhau.setText("");
            txtMatKhau.requestFocus();
        }
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(fontLabel);
        lbl.setForeground(colorTitle);
        return lbl;
    }

    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(color.darker()); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(color); }
        });
        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Baii3().setVisible(true);
        });
    }
}