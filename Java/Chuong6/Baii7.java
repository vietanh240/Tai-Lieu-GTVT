import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Baii7 extends JFrame {

    // Lưu từng nét vẽ
    private ArrayList<ArrayList<Point>> danhSachNet = new ArrayList<>();
    private ArrayList<Color> danhSachMau = new ArrayList<>();
    private ArrayList<Integer> danhSachKichThuoc = new ArrayList<>();

    private Color mauHienTai = Color.BLACK;
    private int kichThuocHienTai = 3;

    private Font fontTitle = new Font("Segoe UI", Font.BOLD, 13);
    private Color colorBg    = new Color(245, 248, 255);
    private Color colorTitle = new Color(33, 97, 140);

    private JPanel drawPanel;
    private JLabel lblMauHienTai;

    public Baii7() {
        setTitle("Paint Đơn Giản");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        toolbar.setBackground(new Color(33, 97, 140));

        JButton btnMau = createButton("Chọn màu");
        btnMau.addActionListener(e -> {
            Color mauChon = JColorChooser.showDialog(this, "Chọn màu vẽ", mauHienTai);
            if (mauChon != null) {
                mauHienTai = mauChon;
                lblMauHienTai.setBackground(mauHienTai);
            }
        });

        lblMauHienTai = new JLabel("  ");
        lblMauHienTai.setOpaque(true);
        lblMauHienTai.setBackground(mauHienTai);
        lblMauHienTai.setPreferredSize(new Dimension(30, 30));
        lblMauHienTai.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

        JLabel lblSize = new JLabel("Nét vẽ:");
        lblSize.setFont(fontTitle);
        lblSize.setForeground(Color.WHITE);

        JSlider sliderSize = new JSlider(1, 20, 3);
        sliderSize.setBackground(new Color(33, 97, 140));
        sliderSize.setForeground(Color.WHITE);
        sliderSize.setPreferredSize(new Dimension(120, 30));
        sliderSize.addChangeListener(e -> kichThuocHienTai = sliderSize.getValue());

        JLabel lblSizeVal = new JLabel("3px");
        lblSizeVal.setFont(fontTitle);
        lblSizeVal.setForeground(Color.WHITE);
        sliderSize.addChangeListener(e -> {
            kichThuocHienTai = sliderSize.getValue();
            lblSizeVal.setText(kichThuocHienTai + "px");
        });

        JButton btnXoa = createButton("🗑 Xóa tất cả");
        btnXoa.setBackground(new Color(180, 60, 60));
        btnXoa.addActionListener(e -> {
            danhSachNet.clear();
            danhSachMau.clear();
            danhSachKichThuoc.clear();
            drawPanel.repaint();
        });

        toolbar.add(btnMau);
        toolbar.add(lblMauHienTai);
        toolbar.add(Box.createHorizontalStrut(10));
        toolbar.add(lblSize);
        toolbar.add(sliderSize);
        toolbar.add(lblSizeVal);
        toolbar.add(Box.createHorizontalStrut(10));
        toolbar.add(btnXoa);

        add(toolbar, BorderLayout.NORTH);

        drawPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

                for (int i = 0; i < danhSachNet.size(); i++) {
                    ArrayList<Point> net = danhSachNet.get(i);
                    g2d.setColor(danhSachMau.get(i));
                    g2d.setStroke(new BasicStroke(
                        danhSachKichThuoc.get(i),BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND
                    ));
                    for (int j = 1; j < net.size(); j++) {
                        g2d.drawLine(
                            net.get(j-1).x, net.get(j-1).y,
                            net.get(j).x,   net.get(j).y
                        );
                    }
                }
            }
        };

        drawPanel.setBackground(Color.WHITE);
        drawPanel.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        drawPanel.setBorder(BorderFactory.createLineBorder(new Color(174, 214, 241), 2));

        drawPanel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                ArrayList<Point> netMoi = new ArrayList<>();
                netMoi.add(e.getPoint());
                danhSachNet.add(netMoi);
                danhSachMau.add(mauHienTai);
                danhSachKichThuoc.add(kichThuocHienTai);
            }
        });

        drawPanel.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                danhSachNet.get(danhSachNet.size() - 1).add(e.getPoint());
                drawPanel.repaint();
            }
        });

        add(drawPanel, BorderLayout.CENTER);

        JLabel lblStatus = new JLabel("Nhấn giữ chuột và kéo để vẽ");
        lblStatus.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblStatus.setForeground(colorTitle);
        lblStatus.setBackground(colorBg);
        lblStatus.setOpaque(true);
        lblStatus.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
        add(lblStatus, BorderLayout.SOUTH);
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(fontTitle);
        btn.setBackground(new Color(41, 128, 185));
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
            new Baii7().setVisible(true);
        });
    }
}