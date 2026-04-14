package iuh.view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class MaXacNhanFrame extends JFrame {

    static final Color BG_WHITE   = Color.WHITE;
    static final Color BG_RIGHT   = new Color(0xF2F3F7);
    static final Color BLUE       = new Color(0x1A3A6B);
    static final Color BLUE_BTN   = new Color(0x1E3A8A);
    static final Color BLUE_HOVER = new Color(0x152E6E);
    static final Color TEXT_DARK  = new Color(0x1A1A2E);
    static final Color TEXT_GRAY  = new Color(0x9AA0B0);
    static final Color BORDER_CLR = new Color(0xCCCCCC);
    static final Color RED_TIMER  = new Color(0xE04040);

    static final Font F_BACK  = new Font("Segoe UI", Font.PLAIN, 13);
    static final Font F_H1    = new Font("Segoe UI", Font.BOLD, 28);
    static final Font F_SUB   = new Font("Segoe UI", Font.PLAIN, 13);
    static final Font F_OTP   = new Font("Segoe UI", Font.BOLD, 22);
    static final Font F_TIMER = new Font("Segoe UI", Font.BOLD, 13);
    static final Font F_BTN   = new Font("Segoe UI", Font.BOLD, 14);

    private JLabel timerLabel;
    private javax.swing.Timer countdown;
    private int secondsLeft = 30;
    private final JTextField[] otpFields = new JTextField[4];

    public MaXacNhanFrame() {
        setTitle("Victorya - Mã xác nhận");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel root = new JPanel(new GridLayout(1, 2));
        root.add(buildLeft());
        root.add(buildRight());
        setContentPane(root);

        startCountdown();
        setVisible(true);
    }

    // ── Left panel ────────────────────────────────────────────────────────────
    private JPanel buildLeft() {
        JPanel p = new JPanel(null);
        p.setBackground(BG_WHITE);

        // ← Back to login
        JLabel back = new JLabel("‹  Back to login");
        back.setFont(F_BACK);
        back.setForeground(TEXT_GRAY);
        back.setBounds(36, 36, 150, 22);
        back.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        back.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                stopCountdown();
                dispose();
            }
            @Override public void mouseEntered(MouseEvent e) { back.setForeground(new Color(0x3B6FF0)); }
            @Override public void mouseExited (MouseEvent e) { back.setForeground(TEXT_GRAY); }
        });
        p.add(back);

        // Heading
        JLabel h1 = new JLabel("Mã xác nhận");
        h1.setFont(F_H1);
        h1.setForeground(TEXT_DARK);
        h1.setBounds(36, 72, 340, 40);
        p.add(h1);

        // Sub
        JLabel sub = new JLabel("Nhập mã xác nhận gửi về email của bạn");
        sub.setFont(F_SUB);
        sub.setForeground(TEXT_GRAY);
        sub.setBounds(36, 118, 320, 20);
        p.add(sub);

        // 4 OTP boxes
        int boxSize = 52, gap = 14, startX = 36, boxY = 158;
        for (int i = 0; i < 4; i++) {
            final int idx = i;
            JTextField tf = new JTextField("2") {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(BG_WHITE);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            tf.setFont(F_OTP);
            tf.setForeground(TEXT_DARK);
            tf.setBackground(BG_WHITE);
            tf.setHorizontalAlignment(JTextField.CENTER);
            tf.setBorder(new CompoundBorder(
                    new LineBorder(BORDER_CLR, 1, true),
                    new EmptyBorder(0, 0, 0, 0)
            ));
            tf.setBounds(startX + i * (boxSize + gap), boxY, boxSize, boxSize);

            // Auto-jump to next field
            tf.addKeyListener(new KeyAdapter() {
                @Override public void keyReleased(KeyEvent e) {
                    String txt = tf.getText();
                    if (txt.length() > 1) tf.setText(txt.substring(txt.length() - 1));
                    if (!tf.getText().isEmpty() && idx < 3) otpFields[idx + 1].requestFocus();
                    if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && tf.getText().isEmpty() && idx > 0)
                        otpFields[idx - 1].requestFocus();
                }
            });
            tf.addFocusListener(new FocusAdapter() {
                @Override public void focusGained(FocusEvent e) {
                    tf.setBorder(new CompoundBorder(
                            new LineBorder(new Color(0x3B6FF0), 2, true),
                            new EmptyBorder(0, 0, 0, 0)));
                    tf.selectAll();
                }
                @Override public void focusLost(FocusEvent e) {
                    tf.setBorder(new CompoundBorder(
                            new LineBorder(BORDER_CLR, 1, true),
                            new EmptyBorder(0, 0, 0, 0)));
                }
            });

            otpFields[i] = tf;
            p.add(tf);
        }

        // Countdown timer label
        timerLabel = new JLabel("00:30");
        timerLabel.setFont(F_TIMER);
        timerLabel.setForeground(RED_TIMER);
        timerLabel.setBounds(36, 222, 80, 20);
        p.add(timerLabel);

        // XÁC NHẬN button
        JButton btn = new JButton("XÁC NHẬN") {
            boolean hover = false, pressed = false;
            {
                addMouseListener(new MouseAdapter() {
                    @Override public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
                    @Override public void mouseExited (MouseEvent e) { hover = false; repaint(); }
                    @Override public void mousePressed(MouseEvent e) { pressed = true; repaint(); }
                    @Override public void mouseReleased(MouseEvent e){ pressed = false; repaint(); }
                });
            }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color c = pressed ? BLUE_HOVER : hover ? new Color(0x243F8F) : BLUE_BTN;
                g2.setColor(c);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(Color.WHITE);
                g2.setFont(F_BTN);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(),
                        (getWidth() - fm.stringWidth(getText())) / 2,
                        (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        btn.setOpaque(false); btn.setContentAreaFilled(false);
        btn.setBorderPainted(false); btn.setFocusPainted(false);
        btn.setFont(F_BTN); btn.setForeground(Color.WHITE);
        btn.setBounds(36, 255, 230, 44);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> verifyOtp());
        p.add(btn);

        return p;
    }

    private void verifyOtp() {
        StringBuilder code = new StringBuilder();
        for (JTextField f : otpFields) code.append(f.getText().trim());
        if (code.length() < 4) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đủ 4 chữ số!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        stopCountdown();
        dispose();
        new DatLaiMatKhauFrame();
    }

    // ── Right panel — phone + security illustration ───────────────────────────
    private JPanel buildRight() {
        JPanel p = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Card bg
                g2.setColor(BG_RIGHT);
                g2.fillRoundRect(20, 20, getWidth()-40, getHeight()-40, 24, 24);
                drawIllustration(g2, getWidth(), getHeight());
                g2.dispose();
            }
        };
        p.setBackground(BG_WHITE);
        return p;
    }

    private void drawIllustration(Graphics2D g2, int w, int h) {
        int cx = w / 2, cy = h / 2;

        // ── Phone body ────────────────────────────────────────────────────────
        int px = cx - 60, py = cy - 130, pw = 120, ph = 220;

        // Shadow
        g2.setColor(new Color(0, 0, 0, 28));
        g2.fillRoundRect(px + 6, py + 8, pw, ph, 22, 22);

        // White phone body
        g2.setColor(new Color(0xF8F9FE));
        g2.fillRoundRect(px, py, pw, ph, 22, 22);
        g2.setColor(new Color(0xDDDFEA));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(px, py, pw, ph, 22, 22);

        // Screen area (dark)
        int sx = px + 8, sy = py + 16, sw = pw - 16, sh = ph - 32;
        g2.setColor(new Color(0xEEEFF5));
        g2.fillRoundRect(sx, sy, sw, sh, 14, 14);

        // Camera dot
        g2.setColor(new Color(0x555570));
        g2.fillOval(cx - 4, py + 8, 8, 8);

        // ── Padlock on screen ─────────────────────────────────────────────────
        int lx = cx - 22, ly = cy - 65;
        drawPadlock(g2, lx, ly, 44);

        // ── Password dots row on screen ───────────────────────────────────────
        int rowY = cy + 20, rowX = cx - 38, rowW = 76, rowH = 20;
        GradientPaint gp = new GradientPaint(rowX, rowY, new Color(0x6BE07A), rowX+rowW, rowY, new Color(0x44CC55));
        g2.setPaint(gp);
        g2.fillRoundRect(rowX, rowY, rowW, rowH, rowH, rowH);
        // Dots
        for (int i = 0; i < 4; i++) {
            g2.setColor(new Color(180, 255, 180));
            g2.fillOval(rowX + 10 + i * 17, rowY + rowH/2 - 4, 8, 8);
        }

        // ── Hand holding phone ────────────────────────────────────────────────
        drawHand(g2, cx, cy + 90);

        // ── Green shield with checkmark (top right of phone) ─────────────────
        drawShield(g2, cx + 38, py - 18);
    }

    private void drawPadlock(Graphics2D g2, int x, int y, int size) {
        int bw = size, bh = (int)(size * 0.75f);
        int by = y + size / 2;

        // Shackle
        g2.setColor(new Color(0x44BB55));
        g2.setStroke(new BasicStroke(size * 0.13f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawArc(x + size/4, y, size/2, size/2, 0, 180);

        // Lock body shadow
        g2.setColor(new Color(0, 90, 0, 40));
        g2.fillRoundRect(x + 2, by + 3, bw, bh, 10, 10);

        // Lock body — green gradient
        GradientPaint gp = new GradientPaint(x, by, new Color(0x55CC66), x + bw, by + bh, new Color(0x229933));
        g2.setPaint(gp);
        g2.fillRoundRect(x, by, bw, bh, 10, 10);

        // Sheen
        g2.setColor(new Color(255, 255, 255, 50));
        g2.fillRoundRect(x + 4, by + 3, bw - 8, bh / 2 - 2, 8, 8);

        // Keyhole
        g2.setColor(new Color(0x117722));
        g2.fillOval(x + bw/2 - 6, by + bh/4, 12, 12);
        g2.fillRect(x + bw/2 - 3, by + bh/4 + 8, 6, 10);
    }

    private void drawHand(Graphics2D g2, int cx, int baseY) {
        // Simple stylized hand / sleeve
        // Sleeve (blue)
        GradientPaint sleeve = new GradientPaint(cx - 50, baseY, new Color(0x6B9EFF), cx + 40, baseY + 60, new Color(0x3B6FF0));
        g2.setPaint(sleeve);
        g2.fillRoundRect(cx - 50, baseY, 90, 70, 20, 20);

        // Palm (skin tone)
        GradientPaint palm = new GradientPaint(cx - 35, baseY - 30, new Color(0xFFCBAA), cx + 30, baseY, new Color(0xF0A87A));
        g2.setPaint(palm);
        // Palm shape
        int[] px = {cx - 35, cx - 38, cx - 30, cx + 18, cx + 28, cx + 22, cx + 10};
        int[] py = {baseY, baseY - 20, baseY - 55, baseY - 60, baseY - 40, baseY - 10, baseY + 5};
        g2.fillPolygon(px, py, 7);

        // Thumb outline
        g2.setColor(new Color(0xE09060));
        g2.setStroke(new BasicStroke(1f));
        g2.drawPolygon(px, py, 7);

        // Finger lines
        g2.setColor(new Color(0xD08860));
        g2.setStroke(new BasicStroke(0.8f));
        g2.drawLine(cx + 5, baseY - 55, cx + 8, baseY - 20);
        g2.drawLine(cx - 8, baseY - 57, cx - 5, baseY - 22);
    }

    private void drawShield(Graphics2D g2, int cx, int cy) {
        int r = 28;
        // Shield shadow
        g2.setColor(new Color(0, 120, 0, 30));
        g2.fillOval(cx - r + 3, cy - r + 3, r * 2, r * 2);

        // Shield circle
        GradientPaint gp = new GradientPaint(cx - r, cy - r, new Color(0x66DD55), cx + r, cy + r, new Color(0x229933));
        g2.setPaint(gp);
        g2.fillOval(cx - r, cy - r, r * 2, r * 2);

        // Sheen
        g2.setColor(new Color(255, 255, 255, 60));
        g2.fillOval(cx - r + 4, cy - r + 4, r - 2, r - 2);

        // Checkmark
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(cx - 12, cy, cx - 3, cy + 10);
        g2.drawLine(cx - 3, cy + 10, cx + 13, cy - 10);
    }

    // ── Timer ─────────────────────────────────────────────────────────────────
    private void startCountdown() {
        secondsLeft = 30;
        countdown = new javax.swing.Timer(1000, e -> {
            secondsLeft--;
            if (secondsLeft <= 0) {
                stopCountdown();
                timerLabel.setText("00:00");
                timerLabel.setForeground(TEXT_GRAY);
                JOptionPane.showMessageDialog(this,
                        "Mã xác nhận đã hết hạn. Vui lòng yêu cầu mã mới.",
                        "Hết hạn", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int min = secondsLeft / 60, sec = secondsLeft % 60;
            timerLabel.setText(String.format("%02d:%02d", min, sec));
        });
        countdown.start();
    }

    private void stopCountdown() {
        if (countdown != null) countdown.stop();
    }

    @Override
    public void dispose() {
        stopCountdown();
        super.dispose();
    }
}