import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Baii1 extends JFrame {
    private JTextField txtHoTen;
    private JRadioButton rbNam, rbNu;
    private ButtonGroup bgGioiTinh;
    private JComboBox<String> cbQuocTich;
    private JCheckBox cbDocSach, cbDuLich, cbAmNhac;
    private JButton btnXacNhan;

    private Font fontLabel  = new Font("Segoe UI", Font.BOLD, 14);
    private Font fontInput  = new Font("Segoe UI", Font.PLAIN, 14);
    private Color colorBg   = new Color(245, 248, 255);
    private Color colorTitle = new Color(33, 97, 140);
    private Color colorBtn  = new Color(41, 128, 185);

    public Baii1() {
        setTitle("Form Nhập Thông Tin Cá Nhân");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 380);
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

        JLabel lblTitle = new JLabel("THÔNG TIN CÁ NHÂN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(colorTitle);
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(lblTitle, gbc);

        gbc.gridwidth = 1; 

        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0.3; 
        mainPanel.add(createLabel("Họ và tên:"), gbc);

        txtHoTen = new JTextField();
        txtHoTen.setFont(fontInput);
        txtHoTen.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(174, 214, 241), 1),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.weightx = 0.7; 
        mainPanel.add(txtHoTen, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.weightx = 0.3;
        mainPanel.add(createLabel("Giới tính:"), gbc);

        JPanel pnGioiTinh = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnGioiTinh.setBackground(colorBg);
        rbNam = createRadioButton("Nam");
        rbNu  = createRadioButton("Nữ");
        bgGioiTinh = new ButtonGroup();
        bgGioiTinh.add(rbNam);
        bgGioiTinh.add(rbNu);
        rbNam.setSelected(true);
        pnGioiTinh.add(rbNam);
        pnGioiTinh.add(Box.createHorizontalStrut(20));
        pnGioiTinh.add(rbNu);
        gbc.gridx = 1; gbc.gridy = 2;
        gbc.weightx = 0.7;
        mainPanel.add(pnGioiTinh, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        gbc.weightx = 0.3;
        mainPanel.add(createLabel("Quốc tịch:"), gbc);

        String[] quocGia = {"Việt Nam", "Hoa Kỳ", "Nhật Bản", "Hàn Quốc", "Trung Quốc", "Anh", "Pháp", "Đức", "Úc", "Canada"};
        cbQuocTich = new JComboBox<>(quocGia);
        cbQuocTich.setFont(fontInput);
        cbQuocTich.setBackground(Color.WHITE);
        gbc.gridx = 1; gbc.gridy = 3;
        gbc.weightx = 0.7;
        mainPanel.add(cbQuocTich, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        gbc.weightx = 0.3;
        mainPanel.add(createLabel("Sở thích:"), gbc);

        JPanel pnSoThich = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnSoThich.setBackground(colorBg);
        cbDocSach = createCheckBox("Đọc sách");
        cbDuLich  = createCheckBox("Du lịch");
        cbAmNhac  = createCheckBox("Âm nhạc");
        pnSoThich.add(cbDocSach);
        pnSoThich.add(cbDuLich);
        pnSoThich.add(cbAmNhac);
        gbc.gridx = 1; gbc.gridy = 4;
        gbc.weightx = 0.7;
        mainPanel.add(pnSoThich, gbc);

        btnXacNhan = new JButton("Xác nhận");
        btnXacNhan.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnXacNhan.setBackground(colorBtn);
        btnXacNhan.setForeground(Color.WHITE);
        btnXacNhan.setFocusPainted(false);
        btnXacNhan.setBorderPainted(false);
        btnXacNhan.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnXacNhan.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnXacNhan.setBackground(new Color(21, 101, 154)); }
            public void mouseExited(MouseEvent e)  { btnXacNhan.setBackground(colorBtn); }
        });
        btnXacNhan.addActionListener(e -> xacNhan());

        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(20, 5, 8, 5);
        mainPanel.add(btnXacNhan, gbc);

        add(mainPanel);
    }

    private void xacNhan() {
        String hoTen = txtHoTen.getText().trim();
        if (hoTen.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập họ và tên!", "Lỗi", JOptionPane.WARNING_MESSAGE);
            txtHoTen.requestFocus();
            return;
        }
        String gioiTinh = rbNam.isSelected() ? "Nam" : "Nữ";
        String quocTich = (String) cbQuocTich.getSelectedItem();

        StringBuilder soThich = new StringBuilder();
        if (cbDocSach.isSelected()) soThich.append("Đọc sách, ");
        if (cbDuLich.isSelected())  soThich.append("Du lịch, ");
        if (cbAmNhac.isSelected())  soThich.append("Âm nhạc, ");
        String soThichStr = soThich.length() > 0? soThich.substring(0, soThich.length() - 2) : "Không có";

        String thongTin = String.format(
            "    KẾT QUẢ XÁC NHẬN       \n" +
            "  Họ và tên : %s\n" +
            "  Giới tính : %s\n" +
            "  Quốc tịch : %s\n" +
            "  Sở thích  : %s\n",
            hoTen, gioiTinh, quocTich, soThichStr
        );

        JTextArea textArea = new JTextArea(thongTin);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        textArea.setEditable(false);
        textArea.setBackground(new Color(245, 248, 255));
        JOptionPane.showMessageDialog(this, textArea, "Thông tin cá nhân", JOptionPane.INFORMATION_MESSAGE);
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(fontLabel);
        lbl.setForeground(colorTitle);
        return lbl;
    }

    private JRadioButton createRadioButton(String text) {
        JRadioButton rb = new JRadioButton(text);
        rb.setFont(fontInput);
        rb.setBackground(colorBg);
        rb.setForeground(new Color(44, 62, 80));
        return rb;
    }

    private JCheckBox createCheckBox(String text) {
        JCheckBox cb = new JCheckBox(text);
        cb.setFont(fontInput);
        cb.setBackground(colorBg);
        cb.setForeground(new Color(44, 62, 80));
        return cb;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Baii1().setVisible(true);
        });
    }
}