import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class Baii9 extends JFrame {

    // ===== Font & màu sắc =====
    private Font fontTitle   = new Font("Segoe UI", Font.BOLD, 13);
    private Font fontNormal  = new Font("Segoe UI", Font.PLAIN, 13);
    private Color colorBg    = new Color(245, 248, 255);
    private Color colorTitle = new Color(33, 97, 140);
    private Color colorBtn   = new Color(41, 128, 185);
    private Color colorBtnXoa = new Color(180, 60, 60);

    private JTextField txtMaSV, txtHoTen, txtTuoi, txtSDT;
    private JComboBox<String> cboLop;

    private JTable table;
    private DefaultTableModel tableModel;

    public Baii9() {
        setTitle("Quản lý sinh viên");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(680, 460);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(0, 0));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(colorTitle);
        topPanel.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));

        JLabel lblTitle = new JLabel("Quản lý sinh viên");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitle.setForeground(Color.WHITE);
        topPanel.add(lblTitle, BorderLayout.WEST);
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 0));
        centerPanel.setBackground(colorBg);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(colorBg);
        formPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(colorTitle, 1),
            "Thêm sinh viên mới",
            0, 0, fontTitle, colorTitle
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtMaSV  = new JTextField(12);
        txtHoTen = new JTextField(14);
        txtTuoi  = new JTextField(8);
        txtSDT   = new JTextField(12);
        cboLop   = new JComboBox<>(new String[]{
            "Lớp CNTT1", "Lớp CNTT2", "Lớp CNTT3", "Lớp CNTT4"
        });

        stylizeField(txtMaSV);
        stylizeField(txtHoTen);
        stylizeField(txtTuoi);
        stylizeField(txtSDT);
        cboLop.setFont(fontNormal);

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        formPanel.add(makeLabel("Mã SV"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        formPanel.add(txtMaSV, gbc);
        gbc.gridx = 2; gbc.weightx = 0;
        formPanel.add(makeLabel("Họ tên"), gbc);
        gbc.gridx = 3; gbc.weightx = 1;
        formPanel.add(txtHoTen, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(makeLabel("Tuổi"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        formPanel.add(txtTuoi, gbc);
        gbc.gridx = 2; gbc.weightx = 0;
        formPanel.add(makeLabel("SĐT"), gbc);
        gbc.gridx = 3; gbc.weightx = 1;
        formPanel.add(txtSDT, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        formPanel.add(makeLabel("Lớp"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        formPanel.add(cboLop, gbc);

        JButton btnThem = createButton("Thêm", colorBtn);
        btnThem.addActionListener(e -> themSinhVien());
        gbc.gridx = 2; gbc.weightx = 0;
        formPanel.add(btnThem, gbc);

        JButton btnXoa = createButton("Xóa dòng được chọn", colorBtnXoa);
        btnXoa.addActionListener(e -> xoaSinhVien());
        gbc.gridx = 3; gbc.weightx = 1;
        formPanel.add(btnXoa, gbc);

        centerPanel.add(formPanel, BorderLayout.NORTH);

        String[] columns = {"Mã SV", "Họ tên", "Tuổi", "SĐT", "Lớp"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(tableModel);
        table.setFont(fontNormal);
        table.setRowHeight(24);
        table.getTableHeader().setFont(fontTitle);
        table.getTableHeader().setBackground(new Color(210, 228, 245));
        table.getTableHeader().setForeground(colorTitle);
        table.setSelectionBackground(new Color(200, 220, 245));
        table.setGridColor(new Color(200, 215, 235));

        tableModel.addRow(new Object[]{"12446677", "Trần Hoài Nam", "21", "09753533634", "Lớp CNTT1"});
        tableModel.addRow(new Object[]{"12345898", "Lê Văn Thắng",  "21", "09876537456", "Lớp CNTT2"});
        tableModel.addRow(new Object[]{"12376543", "Nguyễn Thị Hồng","22","09856764532", "Lớp CNTT1"});

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(174, 214, 241), 1));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
    }

    private void themSinhVien() {
        String maSV  = txtMaSV.getText().trim();
        String hoTen = txtHoTen.getText().trim();
        String tuoi  = txtTuoi.getText().trim();
        String sdt   = txtSDT.getText().trim();
        String lop   = (String) cboLop.getSelectedItem();

        if (maSV.isEmpty() || hoTen.isEmpty() || tuoi.isEmpty() || sdt.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng nhập đầy đủ thông tin!", "Thiếu dữ liệu",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Integer.parseInt(tuoi);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Tuổi phải là số nguyên!", "Dữ liệu không hợp lệ",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        tableModel.addRow(new Object[]{maSV, hoTen, tuoi, sdt, lop});

        txtMaSV.setText("");
        txtHoTen.setText("");
        txtTuoi.setText("");
        txtSDT.setText("");
        cboLop.setSelectedIndex(0);

        JOptionPane.showMessageDialog(this,
            "Thêm sinh viên thành công!", "Thông báo",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void xoaSinhVien() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Chưa có dòng nào được chọn!\nVui lòng chọn một sinh viên để xóa.",
                "Chưa chọn dòng", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String hoTen = (String) tableModel.getValueAt(selectedRow, 1);
        int confirm = JOptionPane.showConfirmDialog(this,
            "Bạn có chắc muốn xóa sinh viên: " + hoTen + "?",
            "Xác nhận xóa", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.removeRow(selectedRow);
            JOptionPane.showMessageDialog(this,
                "Đã xóa sinh viên: " + hoTen, "Thông báo",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private JLabel makeLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(fontTitle);
        lbl.setForeground(colorTitle);
        return lbl;
    }

    private void stylizeField(JTextField field) {
        field.setFont(fontNormal);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(174, 214, 241), 1),
            BorderFactory.createEmptyBorder(3, 6, 3, 6)
        ));
    }

    private JButton createButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(fontTitle);
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            Color orig;
            public void mouseEntered(MouseEvent e) {
                orig = btn.getBackground();
                btn.setBackground(orig.darker());
            }
            public void mouseExited(MouseEvent e) { btn.setBackground(orig); }
        });
        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Baii9().setVisible(true);
        });
    }
}