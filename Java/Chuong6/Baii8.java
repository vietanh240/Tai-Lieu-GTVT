import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Baii8 extends JFrame {

    private Font fontTitle  = new Font("Segoe UI", Font.BOLD, 13);
    private Font fontQuestion = new Font("Segoe UI", Font.BOLD, 14);
    private Font fontOption   = new Font("Segoe UI", Font.PLAIN, 13);
    private Color colorBg    = new Color(245, 248, 255);
    private Color colorTitle = new Color(33, 97, 140);
    private Color colorBtn   = new Color(41, 128, 185);
    private Color colorBottom = new Color(255, 182, 193);

    private String[] questions = {
        "Câu 1: Layout nào sắp xếp component theo hàng ngang mặc định?",
        "Câu 2: Layout nào chia cửa sổ thành 5 vùng (North, South, East, West, Center)?",
        "Câu 3: Layout nào sắp xếp component theo chiều dọc hoặc ngang linh hoạt?",
        "Câu 4: Bố cục nào sắp xếp thành lưới đồng đều?",
        "Câu 5: JFrame kế thừa từ class nào trong Java AWT?"
    };

    private String[][] options = {
        {"FlowLayout", "GridLayout", "BorderLayout", "BoxLayout"},
        {"GridLayout",  "FlowLayout", "BorderLayout", "BoxLayout"},
        {"BorderLayout", "FlowLayout", "GridLayout",  "BoxLayout"},
        {"GridLayout",  "FlowLayout", "BoxLayout",   "BorderLayout"},
        {"JDialog",     "JPanel",     "Frame",        "Container"}
    };

    private int[] answers = {0, 2, 3, 0, 2};

    private int currentQuestion = 0;
    private int score = 0;

    private JLabel       lblQuestion;
    private JRadioButton[] radioButtons = new JRadioButton[4];
    private ButtonGroup  buttonGroup;
    private JButton      btnNext;
    private JLabel       lblProgress;

    public Baii8() {
        setTitle("Quiz Layout Java");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(440, 340);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponents();
        loadQuestion(currentQuestion);
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(colorTitle);
        topPanel.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));

        JLabel lblTitle = new JLabel("Quiz Trắc Nghiệm Java");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitle.setForeground(Color.WHITE);

        lblProgress = new JLabel("Câu 1 / " + questions.length);
        lblProgress.setFont(fontTitle);
        lblProgress.setForeground(new Color(200, 230, 255));

        topPanel.add(lblTitle,    BorderLayout.WEST);
        topPanel.add(lblProgress, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(colorBg);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(16, 20, 10, 20));

        lblQuestion = new JLabel();
        lblQuestion.setFont(fontQuestion);
        lblQuestion.setForeground(colorTitle);
        lblQuestion.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerPanel.add(lblQuestion);
        centerPanel.add(Box.createVerticalStrut(14));

        buttonGroup = new ButtonGroup();
        for (int i = 0; i < 4; i++) {
            radioButtons[i] = new JRadioButton();
            radioButtons[i].setFont(fontOption);
            radioButtons[i].setBackground(colorBg);
            radioButtons[i].setFocusPainted(false);
            radioButtons[i].setAlignmentX(Component.LEFT_ALIGNMENT);
            radioButtons[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
            buttonGroup.add(radioButtons[i]);
            centerPanel.add(radioButtons[i]);
            centerPanel.add(Box.createVerticalStrut(6));
        }

        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 8));
        bottomPanel.setBackground(colorBottom);

        btnNext = createButton("Tiếp theo");
        btnNext.addActionListener(e -> handleNext());

        bottomPanel.add(btnNext);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(fontTitle);
        btn.setBackground(colorBtn);
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
            public void mouseExited(MouseEvent e) {
                btn.setBackground(orig);
            }
        });
        return btn;
    }

    private void loadQuestion(int index) {
        lblQuestion.setText(questions[index]);
        buttonGroup.clearSelection();
        for (int i = 0; i < 4; i++) {
            radioButtons[i].setText(options[index][i]);
        }
        lblProgress.setText("Câu " + (index + 1) + " / " + questions.length);
        btnNext.setText(index == questions.length - 1 ? "Nộp bài" : "Tiếp theo");
    }

    private void handleNext() {
        int selected = getSelectedIndex();

        if (selected == -1) {
            JOptionPane.showMessageDialog(
                this, "Vui lòng chọn một đáp án!", "Chú ý",JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (selected == answers[currentQuestion]) {
            score++;
            JOptionPane.showMessageDialog(
                this, "Chính xác! Chúc mừng bạn", "Kết quả",
                JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            String correctAns = options[currentQuestion][answers[currentQuestion]];
            JOptionPane.showMessageDialog(
                this, "Sai rồi! Đáp án đúng là: " + correctAns, "Kết quả",
                JOptionPane.ERROR_MESSAGE
            );
        }

        currentQuestion++;

        if (currentQuestion < questions.length) {
            loadQuestion(currentQuestion);
        } else {
            showFinalScore();
        }
    }

    private int getSelectedIndex() {
        for (int i = 0; i < radioButtons.length; i++) {
            if (radioButtons[i].isSelected()) return i;
        }
        return -1;
    }

    private void showFinalScore() {
        double diem = (double) score / questions.length * 10;
        JOptionPane.showMessageDialog(
            this,
            String.format(
                "Bài kiểm tra hoàn thành!\nSố câu đúng: %d / %d\nĐiểm số: %.1f / 10",
                score, questions.length, diem
            ),
            "Kết quả cuối cùng",
            JOptionPane.INFORMATION_MESSAGE
        );
        System.exit(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Baii8().setVisible(true);
        });
    }
}