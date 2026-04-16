package iuh.view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class QuenMatKhauFrame extends JFrame {

    static final Color BG_WHITE   = Color.WHITE;
    static final Color BG_RIGHT   = new Color(0xF2F3F7);
    static final Color BLUE       = new Color(0x3B6FF0);
    static final Color BLUE_DARK  = new Color(0x2A5CD4);
    static final Color BLUE_LIGHT = new Color(0x4A7EFF);
    static final Color TEXT_DARK  = new Color(0x1A1A2E);
    static final Color TEXT_GRAY  = new Color(0x9AA0B0);
    static final Color BORDER_CLR = new Color(0xDDE3EF);

    static final Font F_BACK   = new Font("Segoe UI", Font.PLAIN, 13);
    static final Font F_H1     = new Font("Segoe UI", Font.BOLD, 28);
    static final Font F_SUB    = new Font("Segoe UI", Font.PLAIN, 14);
    static final Font F_LABEL  = new Font("Segoe UI", Font.PLAIN, 12);
    static final Font F_INPUT  = new Font("Segoe UI", Font.PLAIN, 14);
    static final Font F_BTN    = new Font("Segoe UI", Font.BOLD, 14);

    public QuenMatKhauFrame() {
        setTitle("Victorya - Quên mật khẩu");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 560);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel root = new JPanel(new GridLayout(1, 2));
        root.add(buildLeft());
        root.add(buildRight());

        setContentPane(root);
        setVisible(true);
    }

    // ── Left panel ────────────────────────────────────────────────────────────
    private JPanel buildLeft() {
        JPanel p = new JPanel(null); // absolute layout for free positioning
        p.setBackground(BG_WHITE);

        // ← Quay lại
        JLabel back = new JLabel("‹  Quay lại");
        back.setFont(F_BACK);
        back.setForeground(TEXT_GRAY);
        back.setBounds(36, 36, 120, 22);
        back.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        back.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { dispose(); }
            @Override public void mouseEntered(MouseEvent e) { back.setForeground(BLUE); }
            @Override public void mouseExited (MouseEvent e) { back.setForeground(TEXT_GRAY); }
        });
        p.add(back);

        // Heading
        JLabel h1 = new JLabel("Quên mật khẩu?");
        h1.setFont(F_H1);
        h1.setForeground(TEXT_DARK);
        h1.setBounds(36, 72, 340, 40);
        p.add(h1);

        // Sub-label
        JLabel sub = new JLabel("Vui lòng nhập email của bạn");
        sub.setFont(F_SUB);
        sub.setForeground(TEXT_GRAY);
        sub.setBounds(36, 118, 340, 22);
        p.add(sub);

        // Email label
        JLabel emailLbl = new JLabel("Email");
        emailLbl.setFont(F_LABEL);
        emailLbl.setForeground(TEXT_GRAY);
        emailLbl.setBounds(36, 162, 300, 16);
        p.add(emailLbl);

        // Email field
        JTextField emailField = new JTextField("john.doe@gmail.com") {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
            }
        };
        emailField.setFont(F_INPUT);
        emailField.setForeground(TEXT_DARK);
        emailField.setBackground(BG_WHITE);
        emailField.setBorder(new CompoundBorder(
                new LineBorder(BORDER_CLR, 1, true),
                new EmptyBorder(8, 12, 8, 12)
        ));
        emailField.setBounds(36, 182, 290, 44);
        emailField.setCaretColor(BLUE);
        emailField.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                emailField.setBorder(new CompoundBorder(
                        new LineBorder(BLUE, 2, true), new EmptyBorder(7, 11, 7, 11)));
            }
            @Override public void focusLost(FocusEvent e) {
                emailField.setBorder(new CompoundBorder(
                        new LineBorder(BORDER_CLR, 1, true), new EmptyBorder(8, 12, 8, 12)));
            }
        });
        p.add(emailField);

        // Tiếp tục button
        JButton btn = new JButton("Tiếp tục") {
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
                Color c = pressed ? BLUE_DARK : hover ? BLUE_LIGHT : BLUE;
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
        btn.setBounds(36, 244, 290, 44);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> {
            String email = emailField.getText().trim();
            if (email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập email!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Open OTP verification screen
            dispose();
            new MaXacNhanFrame();
        });
        p.add(btn);

        return p;
    }

    // ── Right panel — illustration ────────────────────────────────────────────
    private JPanel buildRight() {
        JPanel p = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Rounded card background
                g2.setColor(BG_RIGHT);
                g2.fillRoundRect(20, 20, getWidth() - 40, getHeight() - 40, 24, 24);

                drawIllustration(g2, getWidth(), getHeight());
                g2.dispose();
            }
        };
        p.setBackground(BG_WHITE);
        return p;
    }

    private void drawIllustration(Graphics2D g2, int w, int h) {
        int cx = w / 2, cy = h / 2;

        // ── Background card (white rounded rect) ──────────────────────────────
        g2.setColor(WHITE_CARD);
        g2.fillRoundRect(cx - 110, cy - 70, 220, 140, 16, 16);
        g2.setColor(new Color(0xE0E4EE));
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(cx - 110, cy - 70, 220, 140, 16, 16);

        // ── Password rows on card ─────────────────────────────────────────────
        // Row 1 — gold/yellow bar
        drawPasswordRow(g2, cx - 80, cy - 30, 160, 18, new Color(0xF5C842), new Color(0xF5A800), 0);
        // Row 2 — purple/blue dots row
        drawPasswordRow(g2, cx - 80, cy + 10, 160, 18, new Color(0x6B6EF5), new Color(0x4A4DE8), 7);

        // ── Key (bottom-left of card) ─────────────────────────────────────────
        drawKey(g2, cx - 105, cy + 52);

        // ── Padlock (center-top, large 3D-ish) ────────────────────────────────
        drawPadlock(g2, cx - 10, cy - 145);

        // ── Envelope (left floating) ─────────────────────────────────────────
        drawEnvelope(g2, cx - 145, cy - 90);

        // ── Gear top-right ───────────────────────────────────────────────────
        drawGear(g2, cx + 118, cy - 110, 18);
        drawGear(g2, cx + 138, cy - 82, 12);

        // ── Check triangle (bottom right) ────────────────────────────────────
        drawCheckTriangle(g2, cx + 110, cy + 55);
    }

    static final Color WHITE_CARD = new Color(0xEEEFF5);

    private void drawPasswordRow(Graphics2D g2, int x, int y, int w, int h,
                                 Color c1, Color c2, int dotCount) {
        // Rounded pill background
        GradientPaint gp = new GradientPaint(x, y, c1, x + w, y, c2);
        g2.setPaint(gp);
        g2.fillRoundRect(x, y, w, h, h, h);

        if (dotCount > 0) {
            // Draw asterisk dots
            g2.setColor(new Color(0xAAAAAA, false));
            g2.setColor(new Color(180, 180, 220));
            int dotR = 4, spacing = 20, startX = x + 16;
            for (int i = 0; i < dotCount; i++) {
                g2.fillOval(startX + i * spacing - dotR, y + h/2 - dotR, dotR*2, dotR*2);
            }
        }
    }

    private void drawKey(Graphics2D g2, int x, int y) {
        g2.setColor(new Color(0x8888AA));
        g2.setStroke(new BasicStroke(4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        // Key handle (circle)
        g2.drawOval(x, y - 14, 22, 22);
        // Key stem
        g2.drawLine(x + 22, y - 3, x + 55, y - 3);
        // Key teeth
        g2.drawLine(x + 42, y - 3, x + 42, y + 7);
        g2.drawLine(x + 52, y - 3, x + 52, y + 5);
    }

    private void drawPadlock(Graphics2D g2, int x, int y) {
        // Shackle (arc)
        g2.setStroke(new BasicStroke(9f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        // Shadow shackle
        g2.setColor(new Color(0x7070B0));
        g2.drawArc(x + 5, y + 5, 60, 55, 0, 180);
        // Main shackle gradient simulation
        g2.setColor(new Color(0x9090D0));
        g2.drawArc(x, y, 60, 55, 0, 180);

        // Lock body
        int bx = x - 10, by = y + 36, bw = 80, bh = 62;
        // Shadow
        g2.setColor(new Color(0x7070B0));
        g2.fillRoundRect(bx + 3, by + 3, bw, bh, 14, 14);
        // Body gradient
        GradientPaint gp = new GradientPaint(bx, by, new Color(0xA0A8E8), bx + bw, by + bh, new Color(0x7078C8));
        g2.setPaint(gp);
        g2.fillRoundRect(bx, by, bw, bh, 14, 14);
        // Sheen
        g2.setColor(new Color(255, 255, 255, 40));
        g2.fillRoundRect(bx + 6, by + 4, bw - 12, bh / 2 - 4, 10, 10);

        // Keyhole
        g2.setColor(new Color(0x5058A8));
        g2.fillOval(bx + bw/2 - 9, by + 16, 18, 18);
        g2.setColor(new Color(0x3840A0));
        g2.fillRect(bx + bw/2 - 4, by + 28, 8, 16);
    }

    private void drawEnvelope(Graphics2D g2, int x, int y) {
        int ew = 52, eh = 38;
        // Body
        g2.setColor(new Color(0xF5C842));
        g2.fillRoundRect(x, y, ew, eh, 6, 6);
        // Flap lines
        g2.setColor(new Color(0xE8A800));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawLine(x, y, x + ew/2, y + eh/2);
        g2.drawLine(x + ew, y, x + ew/2, y + eh/2);
        // Border
        g2.setColor(new Color(0xE8A800));
        g2.drawRoundRect(x, y, ew, eh, 6, 6);
    }

    private void drawGear(Graphics2D g2, int cx, int cy, int r) {
        g2.setColor(new Color(0xBBBBCC));
        g2.setStroke(new BasicStroke(1f));
        int teeth = 8;
        double inner = r * 0.72, outer = r;
        Path2D gear = new Path2D.Float();
        for (int i = 0; i < teeth * 2; i++) {
            double angle = Math.PI * i / teeth - Math.PI / 2;
            double rad = (i % 2 == 0) ? outer : inner;
            float px = (float)(cx + rad * Math.cos(angle));
            float py = (float)(cy + rad * Math.sin(angle));
            if (i == 0) gear.moveTo(px, py); else gear.lineTo(px, py);
        }
        gear.closePath();
        g2.fill(gear);
        g2.setColor(new Color(0xAAAABB));
        g2.draw(gear);
        // Center hole
        g2.setColor(BG_RIGHT);
        g2.fillOval(cx - r/3, cy - r/3, r*2/3, r*2/3);
    }

    private void drawCheckTriangle(Graphics2D g2, int x, int y) {
        int s = 38;
        // Triangle body
        int[] px = {x, x + s, x + s/2};
        int[] py = {y + s, y + s, y};
        g2.setColor(new Color(0x4A7EFF));
        g2.fillPolygon(px, py, 3);
        // Check mark
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(x + 10, y + s - 14, x + s/2 - 2, y + s - 6);
        g2.drawLine(x + s/2 - 2, y + s - 6, x + s - 8, y + 10);
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}
        SwingUtilities.invokeLater(QuenMatKhauFrame::new);
    }
}