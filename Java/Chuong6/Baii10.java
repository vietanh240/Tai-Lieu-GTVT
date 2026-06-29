import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.util.Locale;

public class Baii10 extends JFrame {

    private Font fontTitle   = new Font("Segoe UI", Font.BOLD, 13);
    private Font fontNormal  = new Font("Segoe UI", Font.PLAIN, 13);
    private Color colorBg    = new Color(245, 248, 255);
    private Color colorTitle = new Color(33, 97, 140);
    private Color colorBtn   = new Color(41, 128, 185);
    private Color colorBtnSua  = new Color(39, 174, 96);
    private Color colorBtnXoa  = new Color(180, 60, 60);
    private Color colorBtnFind = new Color(142, 68, 173);

    private JTextField txtMaSP, txtTenSP, txtSoLuong, txtDonGia, txtTimKiem;

    private JTable table;
    private DefaultTableModel tableModel;

    private NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("vi", "VN"));

    public Baii10() {
        setTitle("Quản lý sản phẩm");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(750, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(0, 0));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(colorTitle);
        topPanel.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));

        JLabel lblTitle = new JLabel("Quản lý sản phẩm");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitle.setForeground(Color.WHITE);
        topPanel.add(lblTitle, BorderLayout.WEST);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        searchPanel.setBackground(colorTitle);
        txtTimKiem = new JTextField(16);
        txtTimKiem.setFont(fontNormal);
        txtTimKiem.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(174, 214, 241), 1),
            BorderFactory.createEmptyBorder(3, 6, 3, 6)
        ));
        JLabel lblSearch = new JLabel("Tìm kiếm:");
        lblSearch.setFont(fontTitle);
        lblSearch.setForeground(Color.WHITE);
        JButton btnTim = createButton("Tìm", colorBtnFind);
        btnTim.addActionListener(e -> timKiem());

        searchPanel.add(lblSearch);
        searchPanel.add(txtTimKiem);
        searchPanel.add(btnTim);
        topPanel.add(searchPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 0));
        centerPanel.setBackground(colorBg);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(colorBg);
        formPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(colorTitle, 1),
            "Thông tin sản phẩm",
            0, 0, fontTitle, colorTitle
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtMaSP    = new JTextField(10);
        txtTenSP   = new JTextField(16);
        txtSoLuong = new JTextField(8);
        txtDonGia  = new JTextField(10);

        stylizeField(txtMaSP);
        stylizeField(txtTenSP);
        stylizeField(txtSoLuong);
        stylizeField(txtDonGia);

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        formPanel.add(makeLabel("Mã sản phẩm"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        formPanel.add(txtMaSP, gbc);
        gbc.gridx = 2; gbc.weightx = 0;
        formPanel.add(makeLabel("Tên sản phẩm"), gbc);
        gbc.gridx = 3; gbc.weightx = 2;
        formPanel.add(txtTenSP, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(makeLabel("Số lượng"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        formPanel.add(txtSoLuong, gbc);
        gbc.gridx = 2; gbc.weightx = 0;
        formPanel.add(makeLabel("Đơn giá (VNĐ)"), gbc);
        gbc.gridx = 3; gbc.weightx = 2;
        formPanel.add(txtDonGia, gbc);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 4));
        btnPanel.setBackground(colorBg);

        JButton btnThem = createButton("Thêm", colorBtn);
        JButton btnSua  = createButton("Sửa",  colorBtnSua);
        JButton btnXoa  = createButton("Xóa",  colorBtnXoa);
        JButton btnLam  = createButton("Làm mới", new Color(100, 100, 100));

        btnThem.addActionListener(e -> themSanPham());
        btnSua.addActionListener(e  -> suaSanPham());
        btnXoa.addActionListener(e  -> xoaSanPham());
        btnLam.addActionListener(e  -> lamMoiForm());

        btnPanel.add(btnThem);
        btnPanel.add(btnSua);
        btnPanel.add(btnXoa);
        btnPanel.add(btnLam);

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 4;
        gbc.weightx = 1;
        formPanel.add(btnPanel, gbc);

        centerPanel.add(formPanel, BorderLayout.NORTH);

        String[] columns = {"Mã SP", "Tên sản phẩm", "Số lượng", "Đơn giá", "Thành tiền"};
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

        javax.swing.table.DefaultTableCellRenderer centerRenderer = new javax.swing.table.DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 2; i <= 4; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { dienVaoForm(); }
        });

        addSampleRow("SP001", "Bàn phím cơ",   "10", "850000");
        addSampleRow("SP002", "Chuột gaming",  "25", "450000");
        addSampleRow("SP003", "Màn hình 24 inch","5","3200000");

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(174, 214, 241), 1));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        JLabel lblStatus = new JLabel("Chọn dòng để sửa/xóa | Thành tiền = Số lượng × Đơn giá");
        lblStatus.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblStatus.setForeground(colorTitle);
        lblStatus.setBackground(colorBg);
        lblStatus.setOpaque(true);
        lblStatus.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
        add(lblStatus, BorderLayout.SOUTH);
    }

    private void themSanPham() {
        String maSP    = txtMaSP.getText().trim();
        String tenSP   = txtTenSP.getText().trim();
        String slStr   = txtSoLuong.getText().trim();
        String dgStr   = txtDonGia.getText().trim();

        if (maSP.isEmpty() || tenSP.isEmpty() || slStr.isEmpty() || dgStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng nhập đầy đủ thông tin sản phẩm!", "Thiếu dữ liệu",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int    soLuong   = Integer.parseInt(slStr);
            double donGia    = Double.parseDouble(dgStr);
            double thanhTien = soLuong * donGia;

            tableModel.addRow(new Object[]{
                maSP, tenSP,
                soLuong,
                formatter.format(donGia)    + " đ",
                formatter.format(thanhTien) + " đ"
            });

            lamMoiForm();
            JOptionPane.showMessageDialog(this,
                "Thêm sản phẩm thành công!", "Thông báo",
                JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Số lượng và đơn giá phải là số hợp lệ!", "Dữ liệu không hợp lệ",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void suaSanPham() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Chưa chọn dòng cần sửa!", "Chú ý",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String maSP  = txtMaSP.getText().trim();
        String tenSP = txtTenSP.getText().trim();
        String slStr = txtSoLuong.getText().trim();
        String dgStr = txtDonGia.getText().trim();

        if (maSP.isEmpty() || tenSP.isEmpty() || slStr.isEmpty() || dgStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng nhập đầy đủ thông tin!", "Thiếu dữ liệu",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int    soLuong   = Integer.parseInt(slStr);
            double donGia    = Double.parseDouble(dgStr);
            double thanhTien = soLuong * donGia;

            tableModel.setValueAt(maSP,  selectedRow, 0);
            tableModel.setValueAt(tenSP, selectedRow, 1);
            tableModel.setValueAt(soLuong, selectedRow, 2);
            tableModel.setValueAt(formatter.format(donGia)    + " đ", selectedRow, 3);
            tableModel.setValueAt(formatter.format(thanhTien) + " đ", selectedRow, 4);

            lamMoiForm();
            JOptionPane.showMessageDialog(this,
                "Cập nhật sản phẩm thành công!", "Thông báo",
                JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Số lượng và đơn giá phải là số hợp lệ!", "Dữ liệu không hợp lệ",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    // ===== Xử lý: Xóa sản phẩm =====
    private void xoaSanPham() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Chưa có dòng nào được chọn!\nVui lòng chọn một sản phẩm để xóa.",
                "Chưa chọn dòng", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String tenSP = (String) tableModel.getValueAt(selectedRow, 1);
        int confirm = JOptionPane.showConfirmDialog(this,
            "Bạn có chắc muốn xóa sản phẩm: " + tenSP + "?",
            "Xác nhận xóa", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.removeRow(selectedRow);
            lamMoiForm();
            JOptionPane.showMessageDialog(this,
                "Đã xóa sản phẩm: " + tenSP, "Thông báo",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // ===== Xử lý: Tìm kiếm =====
    private void timKiem() {
        String keyword = txtTimKiem.getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng nhập từ khóa tìm kiếm!", "Chú ý",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String maSP  = tableModel.getValueAt(i, 0).toString().toLowerCase();
            String tenSP = tableModel.getValueAt(i, 1).toString().toLowerCase();
            if (maSP.contains(keyword) || tenSP.contains(keyword)) {
                table.setRowSelectionInterval(i, i);
                table.scrollRectToVisible(table.getCellRect(i, 0, true));
                dienVaoForm();
                return;
            }
        }

        JOptionPane.showMessageDialog(this,
            "Không tìm thấy sản phẩm với từ khóa: " + txtTimKiem.getText(),
            "Không tìm thấy", JOptionPane.INFORMATION_MESSAGE);
    }

    private void dienVaoForm() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        txtMaSP.setText(tableModel.getValueAt(row, 0).toString());
        txtTenSP.setText(tableModel.getValueAt(row, 1).toString());
        txtSoLuong.setText(tableModel.getValueAt(row, 2).toString());

        String dgStr = tableModel.getValueAt(row, 3).toString()
            .replace(" đ", "").replace(".", "").replace(",", "");
        txtDonGia.setText(dgStr);
    }

    private void lamMoiForm() {
        txtMaSP.setText("");
        txtTenSP.setText("");
        txtSoLuong.setText("");
        txtDonGia.setText("");
        table.clearSelection();
    }

    private void addSampleRow(String ma, String ten, String sl, String dg) {
        int    soLuong   = Integer.parseInt(sl);
        double donGia    = Double.parseDouble(dg);
        double thanhTien = soLuong * donGia;
        tableModel.addRow(new Object[]{
            ma, ten, soLuong,
            formatter.format(donGia)    + " đ",
            formatter.format(thanhTien) + " đ"
        });
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
        btn.setPreferredSize(new Dimension(110, 32));
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
            new Baii10().setVisible(true);
        });
    }
}