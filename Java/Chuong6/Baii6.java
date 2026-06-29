import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Baii6 extends JFrame {

    private Font fontMenu  = new Font("Segoe UI", Font.PLAIN, 14);
    private Font fontTitle = new Font("Segoe UI", Font.BOLD, 14);
    private Color colorBg    = new Color(245, 248, 255);
    private Color colorTitle = new Color(33, 97, 140);

    public Baii6() {
        setTitle("Ứng Dụng Quản Lý");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(colorBg);

        JLabel lblWelcome = new JLabel("Chào mừng! Hãy chọn menu để bắt đầu.", SwingConstants.CENTER);
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblWelcome.setForeground(colorTitle);
        mainPanel.add(lblWelcome, BorderLayout.CENTER);
        add(mainPanel);

        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(33, 97, 140));
        menuBar.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));

        JMenu menuFile = createMenu("File");
        JMenuItem itemExit = createMenuItem("Exit");
        itemExit.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn thoát?",
                "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) System.exit(0);
        });
        menuFile.add(itemExit);

        JMenu menuOpen = createMenu("Open");

        JMenuItem itemBai1 = createMenuItem("Bài 1 - Form Thông Tin Cá Nhân");
        JMenuItem itemBai2 = createMenuItem("Bài 2 - Máy Tính Đơn Giản");
        JMenuItem itemBai3 = createMenuItem("Bài 3 - Form Đăng Nhập");
        JMenuItem itemBai4 = createMenuItem("Bài 4 - Quản Lý Sinh Viên");

        itemBai1.addActionListener(e -> new Baii1().setVisible(true));
        itemBai2.addActionListener(e -> new Baii2().setVisible(true));
        itemBai3.addActionListener(e -> new Baii3().setVisible(true));
        itemBai4.addActionListener(e -> new Baii4().setVisible(true));

        menuOpen.add(itemBai1);
        menuOpen.add(itemBai2);
        menuOpen.add(itemBai3);
        menuOpen.add(itemBai4);

        JMenu menuHelp = createMenu("Help");
        JMenuItem itemHelp = createMenuItem("Hướng dẫn sử dụng");
        itemHelp.addActionListener(e -> showHelp());
        menuHelp.add(itemHelp);

        menuBar.add(menuFile);
        menuBar.add(menuOpen);
        menuBar.add(menuHelp);
        setJMenuBar(menuBar);
    }

    private void showHelp() {
        JDialog dialog = new JDialog(this, "Hướng dẫn sử dụng", true);
        dialog.setSize(420, 420);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(colorBg);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel lblTitle = new JLabel("HƯỚNG DẪN SỬ DỤNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(colorTitle);
        panel.add(lblTitle, BorderLayout.NORTH);

        JTextArea txtHelp = new JTextArea(
            "• File → Exit      : Thoát khỏi chương trình\n\n" +
            "• Open → Bài 1     : Mở form nhập thông tin cá nhân\n\n" +
            "• Open → Bài 2     : Mở máy tính đơn giản\n\n" +
            "• Open → Bài 3     : Mở form đăng nhập hệ thống\n\n" +
            "• Open → Bài 4     : Mở quản lý danh sách sinh viên\n\n" +
            "• Help → Hướng dẫn : Hiển thị hộp thoại này"
        );
        txtHelp.setFont(fontMenu);
        txtHelp.setEditable(false);
        txtHelp.setBackground(colorBg);
        txtHelp.setForeground(new Color(44, 62, 80));
        panel.add(txtHelp, BorderLayout.CENTER);

        JButton btnDong = new JButton("Đóng");
        btnDong.setFont(fontTitle);
        btnDong.setBackground(colorTitle);
        btnDong.setForeground(Color.WHITE);
        btnDong.setFocusPainted(false);
        btnDong.setBorderPainted(false);
        btnDong.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDong.addActionListener(e -> dialog.dispose());
        btnDong.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnDong.setBackground(new Color(21, 101, 154)); }
            public void mouseExited(MouseEvent e)  { btnDong.setBackground(colorTitle); }
        });

        JPanel pnBtn = new JPanel();
        pnBtn.setBackground(colorBg);
        pnBtn.add(btnDong);
        panel.add(pnBtn, BorderLayout.SOUTH);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private JMenu createMenu(String text) {
        JMenu menu = new JMenu(text);
        menu.setFont(fontTitle);
        menu.setForeground(Color.BLACK);
        menu.setOpaque(false);
        menu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return menu;
    }

    private JMenuItem createMenuItem(String text) {
        JMenuItem item = new JMenuItem(text);
        item.setFont(fontMenu);
        item.setBackground(Color.WHITE);
        item.setForeground(new Color(44, 62, 80));
        item.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return item;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Baii6().setVisible(true);
        });
    }
}