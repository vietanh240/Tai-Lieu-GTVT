import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

public class Baii4 extends JFrame {
    private JTextField txtMaSV, txtHoTen, txtLop;
    private JRadioButton rbNam, rbNu;
    private ButtonGroup bgGioiTinh;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnThem, btnXoa, btnXoaTat;

    private Font fontLabel = new Font("Segoe UI", Font.BOLD, 14);
    private Font fontInput = new Font("Segoe UI", Font.PLAIN, 14);
    private Color colorBg    = new Color(245, 248, 255);
    private Color colorTitle = new Color(33, 97, 140);
    private Color colorBtn      = new Color(41, 128, 185);
    private Color colorBtnRed   = new Color(180, 60, 60);
    private Color colorBtnOrange = new Color(200, 100, 30);

    public Baii4() {
        setTitle("Quản Lý Danh Sách Sinh Viên");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 550);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 10));
        mainPanel.setBackground(colorBg);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("QUẢN LÝ DANH SÁCH SINH VIÊN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(colorTitle);
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        JPanel pnInput = new JPanel(new GridBagLayout());
        pnInput.setBackground(colorBg);
        pnInput.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(174, 214, 241), 1),"Thông tin sinh viên",0, 0,fontLabel, colorTitle
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.2;
        pnInput.add(createLabel("Mã SV:"), gbc);
        txtMaSV = createTextField();
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.8;
        pnInput.add(txtMaSV, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.2;
        pnInput.add(createLabel("Họ tên:"), gbc);
        txtHoTen = createTextField();
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.8;
        pnInput.add(txtHoTen, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.2;
        pnInput.add(createLabel("Lớp:"), gbc);
        txtLop = createTextField();
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 0.8;
        pnInput.add(txtLop, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.2;
        pnInput.add(createLabel("Giới tính:"), gbc);
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
        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 0.8;
        pnInput.add(pnGioiTinh, gbc);

        JPanel pnBtn = new JPanel(new GridLayout(1, 3, 10, 0));
        pnBtn.setBackground(colorBg);
        btnThem   = createButton("Thêm",colorBtn);
        btnXoa    = createButton("Xóa",colorBtnRed);
        btnXoaTat = createButton("Xóa tất cả",colorBtnOrange);

        btnThem.addActionListener(e -> them());
        btnXoa.addActionListener(e -> xoa());
        btnXoaTat.addActionListener(e -> xoaTatCa());

        pnBtn.add(btnThem);
        pnBtn.add(btnXoa);
        pnBtn.add(btnXoaTat);

        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2; gbc.weightx = 1.0;
        gbc.insets = new Insets(12, 8, 8, 8);
        pnInput.add(pnBtn, gbc);

        mainPanel.add(pnInput, BorderLayout.CENTER);

        String[] columns = {"Mã SV", "Họ tên", "Lớp", "Giới tính"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) {
                return false; 
            }
        };

        table = new JTable(tableModel);
        table.setFont(fontInput);
        table.setRowHeight(28);
        table.getTableHeader().setFont(fontLabel);
        table.getTableHeader().setBackground(colorTitle);
        table.setForeground(new Color(30, 30, 30)); 
        table.getTableHeader().setForeground(Color.BLACK);
        table.setSelectionBackground(new Color(174, 214, 241));
        table.setSelectionForeground(Color.BLACK);
        table.setGridColor(new Color(200, 220, 240));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < columns.length; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(174, 214, 241), 1));
        scrollPane.setPreferredSize(new Dimension(0, 200));

        mainPanel.add(scrollPane, BorderLayout.SOUTH);
        add(mainPanel);
    }

    private void them() {
        String maSV  = txtMaSV.getText().trim();
        String hoTen = txtHoTen.getText().trim();
        String lop   = txtLop.getText().trim();

        if (maSV.isEmpty() || hoTen.isEmpty() || lop.isEmpty()) {
            JOptionPane.showMessageDialog(this,"Vui lòng nhập đầy đủ thông tin!","Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (tableModel.getValueAt(i, 0).equals(maSV)) {
                JOptionPane.showMessageDialog(this,"Mã sinh viên đã tồn tại!","Lỗi", JOptionPane.WARNING_MESSAGE);
                txtMaSV.requestFocus();
                return;
            }
        }

        String gioiTinh = rbNam.isSelected() ? "Nam" : "Nữ";
        tableModel.addRow(new Object[]{maSV, hoTen, lop, gioiTinh});
        xoaForm();
    }

    private void xoa() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,"Vui lòng chọn sinh viên cần xóa!", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa sinh viên này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.removeRow(row);
        }
    }

    private void xoaTatCa() {
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,"Danh sách đã trống!","Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa tất cả sinh viên?","Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.setRowCount(0);
        }
    }

    private void xoaForm() {
        txtMaSV.setText("");
        txtHoTen.setText("");
        txtLop.setText("");
        rbNam.setSelected(true);
        txtMaSV.requestFocus();
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

    private JRadioButton createRadioButton(String text) {
        JRadioButton rb = new JRadioButton(text);
        rb.setFont(fontInput);
        rb.setBackground(colorBg);
        rb.setForeground(new Color(44, 62, 80));
        return rb;
    }

    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
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
            new Baii4().setVisible(true);
        });
    }
}