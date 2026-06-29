import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Baii2 extends JFrame {
    private JTextField txtSo1, txtSo2;
    private JComboBox<String> cbPhepToan;
    private JLabel lblKetQua;
    private JButton btnTinh, btnLamLai;

    private Font fontLabel = new Font("Segoe UI", Font.BOLD, 14);
    private Font fontInput = new Font("Segoe UI", Font.PLAIN, 14);
    private Color colorBg    = new Color(245, 248, 255);
    private Color colorTitle = new Color(33, 97, 140);
    private Color colorBtn   = new Color(41, 128, 185);
    private Color colorBtnReset = new Color(180, 60, 60);

    public Baii2() {
        setTitle("Máy Tính");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(460, 320);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(colorBg);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("MÁY TÍNH", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(colorTitle);
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(lblTitle, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        mainPanel.add(createLabel("Số thứ nhất:"), gbc);

        txtSo1 = createTextField();
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.7;
        mainPanel.add(txtSo1, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.3;
        mainPanel.add(createLabel("Phép toán:"), gbc);

        cbPhepToan = new JComboBox<>(new String[]{"+", "-", "*", "/"});
        cbPhepToan.setFont(fontInput);
        cbPhepToan.setBackground(Color.WHITE);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 0.7;
        mainPanel.add(cbPhepToan, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.3;
        mainPanel.add(createLabel("Số thứ hai:"), gbc);

        txtSo2 = createTextField();
        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 0.7;
        mainPanel.add(txtSo2, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0.3;
        mainPanel.add(createLabel("Kết quả:"), gbc);

        lblKetQua = new JLabel();
        lblKetQua.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblKetQua.setForeground(new Color(20, 143, 119));
        gbc.gridx = 1; gbc.gridy = 4; gbc.weightx = 0.7;
        mainPanel.add(lblKetQua, gbc);

        JPanel pnBtn = new JPanel(new GridLayout(1, 2, 10, 0));
        pnBtn.setBackground(colorBg);

        btnTinh = createButton("Tính", colorBtn);
        btnLamLai = createButton("Làm lại", colorBtnReset);

        btnTinh.addActionListener(e -> tinh());
        btnLamLai.addActionListener(e -> lamLai());

        pnBtn.add(btnTinh);
        pnBtn.add(btnLamLai);

        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2; gbc.weightx = 1.0;
        gbc.insets = new Insets(20, 5, 8, 5);
        mainPanel.add(pnBtn, gbc);

        add(mainPanel);
    }

    private void tinh() {
        String s1 = txtSo1.getText().trim();
        String s2 = txtSo2.getText().trim();

        if (s1.isEmpty() || s2.isEmpty()) {
            showError("Vui lòng nhập đầy đủ 2 số!");
            return;
        }

        int so1, so2;

        try {
            so1 = Integer.parseInt(s1);
            so2 = Integer.parseInt(s2);
        } catch (NumberFormatException e) {
            showError("Vui lòng nhập đúng định dạng số!\nVí dụ: 1, 2, 3");
            return;
        }

        String phepToan = (String) cbPhepToan.getSelectedItem();
        int ketQua;

        switch (phepToan) {
            case "+": ketQua = so1 + so2; break;
            case "-": ketQua = so1 - so2; break;
            case "*": ketQua = so1 * so2; break;
            case "/":
                if (so2 == 0) {
                    showError("Không thể chia cho 0!");
                    return;
                }
                ketQua = so1 / so2;
                break;
            default: return;
        }

        String ketQuaStr = (ketQua == (long) ketQua)? String.valueOf((long) ketQua): String.valueOf(ketQua);

        lblKetQua.setText(so1 + " " + phepToan + " " + so2 + " = " + ketQuaStr);
        lblKetQua.setForeground(new Color(20, 143, 119));
    }

    private void lamLai() {
        txtSo1.setText("");
        txtSo2.setText("");
        cbPhepToan.setSelectedIndex(0);
        lblKetQua.setText("");
        lblKetQua.setForeground(new Color(20, 143, 119));
        txtSo1.requestFocus();
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Lỗi", JOptionPane.WARNING_MESSAGE);
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(fontLabel);
        lbl.setForeground(colorTitle);
        return lbl;
    }

    private JTextField createTextField() {
        JTextField tf = new JTextField();
        tf.setFont(fontInput);
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(174, 214, 241), 1),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        return tf;
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
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(color.darker());
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(color);
            }
        });
        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Baii2().setVisible(true);
        });
    }
}