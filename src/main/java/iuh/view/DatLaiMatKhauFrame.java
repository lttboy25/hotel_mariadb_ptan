package iuh.view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class DatLaiMatKhauFrame extends JFrame {

    static final Color BG_WHITE   = Color.WHITE;
    static final Color BG_RIGHT   = new Color(0xF2F3F7);
    static final Color BLUE       = new Color(0x3B6FF0);
    static final Color BLUE_DARK  = new Color(0x2A5CD4);
    static final Color BLUE_LIGHT = new Color(0x4A7EFF);
    static final Color TEXT_DARK  = new Color(0x1A1A2E);
    static final Color TEXT_GRAY  = new Color(0x9AA0B0);
    static final Color BORDER_CLR = new Color(0xDDE3EF);

    static final Font F_LOGO  = new Font("Segoe UI", Font.BOLD, 32);
    static final Font F_H1    = new Font("Segoe UI", Font.BOLD, 24);
    static final Font F_SUB   = new Font("Segoe UI", Font.PLAIN, 13);
    static final Font F_LABEL = new Font("Segoe UI", Font.PLAIN, 11);
    static final Font F_INPUT = new Font("Segoe UI", Font.PLAIN, 14);
    static final Font F_BTN   = new Font("Segoe UI", Font.BOLD, 13);

    public DatLaiMatKhauFrame() {
        setTitle("Victorya - Đặt lại mật khẩu");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 520);
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
        JPanel p = new JPanel(null);
        p.setBackground(BG_WHITE);

        // Logo
        JLabel logo = new JLabel("Victorya");
        logo.setFont(F_LOGO);
        logo.setForeground(BLUE);
        logo.setBounds(36, 40, 220, 44);
        p.add(logo);

        // Heading
        JLabel h1 = new JLabel("Đặt lại mật khẩu mới");
        h1.setFont(F_H1);
        h1.setForeground(TEXT_DARK);
        h1.setBounds(36, 100, 340, 34);
        p.add(h1);

        // Sub
        JLabel sub = new JLabel("Vui lòng nhập mật khẩu mới của bạn gồm 8-32 ký tự");
        sub.setFont(F_SUB);
        sub.setForeground(TEXT_GRAY);
        sub.setBounds(36, 140, 360, 20);
        p.add(sub);

        // Password field 1
        JLabel lbl1 = new JLabel("Nhập mật khẩu mới");
        lbl1.setFont(F_LABEL);
        lbl1.setForeground(TEXT_GRAY);
        lbl1.setBounds(40, 176, 200, 14);
        p.add(lbl1);

        JPasswordField pw1 = buildPasswordField("7789BM6X@#H&$K_", 36, 192, p);

        // Password field 2
        JLabel lbl2 = new JLabel("Xác nhận mật khẩu mới");
        lbl2.setFont(F_LABEL);
        lbl2.setForeground(TEXT_GRAY);
        lbl2.setBounds(40, 242, 200, 14);
        p.add(lbl2);

        JPasswordField pw2 = buildPasswordField("7789BM6X@#H&$K_", 36, 258, p);

        // Đặt mật khẩu button
        JButton btn = new JButton("ĐẶT MẬT KHẨU") {
            boolean hover = false, pressed = false;
            {
                addMouseListener(new MouseAdapter() {
                    @Override public void mouseEntered(MouseEvent e) { hover=true; repaint(); }
                    @Override public void mouseExited (MouseEvent e) { hover=false; repaint(); }
                    @Override public void mousePressed(MouseEvent e) { pressed=true; repaint(); }
                    @Override public void mouseReleased(MouseEvent e){ pressed=false; repaint(); }
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
                        (getWidth()-fm.stringWidth(getText()))/2,
                        (getHeight()+fm.getAscent()-fm.getDescent())/2);
                g2.dispose();
            }
        };
        btn.setOpaque(false); btn.setContentAreaFilled(false);
        btn.setBorderPainted(false); btn.setFocusPainted(false);
        btn.setFont(F_BTN); btn.setForeground(Color.WHITE);
        btn.setBounds(36, 315, 308, 44);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> {
            String p1 = new String(pw1.getPassword()).trim();
            String p2 = new String(pw2.getPassword()).trim();
            if (p1.length() < 8) {
                JOptionPane.showMessageDialog(this, "Mật khẩu phải có ít nhất 8 ký tự!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!p1.equals(p2)) {
                JOptionPane.showMessageDialog(this, "Mật khẩu xác nhận không khớp!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(this, "Đặt lại mật khẩu thành công!\nVui lòng đăng nhập lại.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        });
        p.add(btn);

        return p;
    }

    /** Build a password field with floating label border + eye toggle */
    private JPasswordField buildPasswordField(String defaultVal, int x, int y, JPanel parent) {
        JPasswordField pf = new JPasswordField(defaultVal);
        pf.setFont(F_INPUT);
        pf.setForeground(TEXT_DARK);
        pf.setBackground(BG_WHITE);
        pf.setEchoChar((char)0); // show by default like the design
        pf.setBorder(new CompoundBorder(
                new LineBorder(BORDER_CLR, 1, true),
                new EmptyBorder(8, 12, 8, 40)
        ));
        pf.setBounds(x, y, 308, 42);
        pf.setCaretColor(BLUE);
        pf.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                pf.setBorder(new CompoundBorder(
                        new LineBorder(BLUE, 2, true), new EmptyBorder(7,11,7,39)));
            }
            @Override public void focusLost(FocusEvent e) {
                pf.setBorder(new CompoundBorder(
                        new LineBorder(BORDER_CLR,1,true), new EmptyBorder(8,12,8,40)));
            }
        });
        parent.add(pf);

        // Eye icon toggle button
        boolean[] visible = {true};
        JLabel eyeBtn = new JLabel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(TEXT_GRAY);
                g2.setStroke(new BasicStroke(1.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                int cx = getWidth()/2, cy2 = getHeight()/2;
                if (visible[0]) {
                    // Eye open
                    g2.drawArc(cx-9, cy2-5, 18, 10, 0, 180);
                    g2.drawArc(cx-9, cy2-5, 18, 10, 180, 180);
                    g2.fillOval(cx-3, cy2-3, 6, 6);
                } else {
                    // Eye closed
                    g2.drawArc(cx-9, cy2-5, 18, 10, 0, 180);
                    g2.drawLine(cx-9, cy2+2, cx+9, cy2+2);
                    g2.drawLine(cx-6, cy2+2, cx-8, cy2+7);
                    g2.drawLine(cx+6, cy2+2, cx+8, cy2+7);
                }
                g2.dispose();
            }
        };
        eyeBtn.setBounds(x + 274, y + 11, 22, 22);
        eyeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        eyeBtn.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                visible[0] = !visible[0];
                pf.setEchoChar(visible[0] ? (char)0 : '•');
                eyeBtn.repaint();
            }
        });
        parent.add(eyeBtn);

        return pf;
    }

    // ── Right panel — illustration ────────────────────────────────────────────
    private JPanel buildRight() {
        JPanel p = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
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
        int cx = w/2, cy = h/2;

        // ── White background card ─────────────────────────────────────────────
        g2.setColor(new Color(0xEEEFF8));
        g2.fillRoundRect(cx - 115, cy - 65, 230, 150, 18, 18);
        g2.setColor(new Color(0xD8DCF0));
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(cx - 115, cy - 65, 230, 150, 18, 18);

        // ── Gold/yellow password row ──────────────────────────────────────────
        int row1y = cy - 28, rowX = cx - 85, rowW = 170, rowH = 22;
        GradientPaint gp1 = new GradientPaint(rowX, row1y, new Color(0xF5C842), rowX+rowW, row1y, new Color(0xE8A800));
        g2.setPaint(gp1);
        g2.fillRoundRect(rowX, row1y, rowW, rowH, rowH, rowH);

        // ── Purple dots row ───────────────────────────────────────────────────
        int row2y = cy + 14;
        GradientPaint gp2 = new GradientPaint(rowX, row2y, new Color(0x7B7EFF), rowX+rowW, row2y, new Color(0x5558EE));
        g2.setPaint(gp2);
        g2.fillRoundRect(rowX, row2y, rowW, rowH, rowH, rowH);
        // Stars/asterisks
        g2.setColor(new Color(200, 200, 255));
        g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
        FontMetrics fm = g2.getFontMetrics();
        String stars = "* * * * * * *";
        g2.drawString(stars, cx - fm.stringWidth(stars)/2, row2y + rowH/2 + fm.getAscent()/2 - 2);

        // ── Padlock (centered above card) ─────────────────────────────────────
        drawPadlock(g2, cx - 30, cy - 175);

        // ── Key (left side) ───────────────────────────────────────────────────
        drawKey(g2, cx - 145, cy + 30);

        // ── Gear top-right ────────────────────────────────────────────────────
        drawGear(g2, cx + 110, cy - 125, 20);
        drawGear(g2, cx + 132, cy - 95, 13);

        // ── Warning triangle left ─────────────────────────────────────────────
        drawTriangle(g2, cx - 140, cy - 80, 34, new Color(0xF5C842));

        // ── Blue triangle bottom-right ────────────────────────────────────────
        drawTriangle(g2, cx + 112, cy + 55, 30, new Color(0xAAAAAFF));
    }

    private void drawPadlock(Graphics2D g2, int x, int y) {
        int bw = 62, bh = 50;
        int by = y + 38;

        // Shackle shadow
        g2.setColor(new Color(80, 80, 150, 50));
        g2.setStroke(new BasicStroke(10f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawArc(x + 7, y + 5, 48, 40, 0, 180);

        // Shackle — silver/gray gradient
        g2.setColor(new Color(0xA0A8D0));
        g2.setStroke(new BasicStroke(9f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawArc(x + 4, y, 52, 42, 0, 180);

        // Body shadow
        g2.setColor(new Color(60, 70, 160, 50));
        g2.setStroke(new BasicStroke(1));
        g2.fillRoundRect(x + 4, by + 4, bw, bh, 12, 12);

        // Body gradient blue/purple
        GradientPaint gp = new GradientPaint(x, by, new Color(0x7878E8), x + bw, by + bh, new Color(0x4444CC));
        g2.setPaint(gp);
        g2.fillRoundRect(x, by, bw, bh, 12, 12);

        // Sheen
        g2.setColor(new Color(255, 255, 255, 55));
        g2.fillRoundRect(x + 5, by + 4, bw - 10, bh/2 - 2, 8, 8);

        // Keyhole circle
        g2.setColor(new Color(0x3030AA));
        g2.fillOval(x + bw/2 - 8, by + 12, 16, 16);
        g2.fillRect(x + bw/2 - 4, by + 24, 8, 14);
    }

    private void drawKey(Graphics2D g2, int x, int y) {
        g2.setColor(new Color(0x9090B8));
        g2.setStroke(new BasicStroke(5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawOval(x, y - 14, 24, 24);
        g2.drawLine(x + 24, y - 2, x + 65, y - 2);
        g2.drawLine(x + 50, y - 2, x + 50, y + 9);
        g2.drawLine(x + 60, y - 2, x + 60, y + 7);
        // Sheen on ring
        g2.setColor(new Color(255, 255, 255, 80));
        g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawArc(x + 4, y - 10, 10, 10, 30, 120);
    }

    private void drawGear(Graphics2D g2, int cx, int cy, int r) {
        g2.setColor(new Color(0xBBBBCC));
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
        g2.setStroke(new BasicStroke(0.8f));
        g2.draw(gear);
        g2.setColor(BG_RIGHT);
        g2.fillOval(cx - r/3, cy - r/3, r*2/3, r*2/3);
    }

    private void drawTriangle(Graphics2D g2, int x, int y, int s, Color c) {
        int[] px = {x, x + s, x + s/2};
        int[] py = {y + s, y + s, y};
        g2.setColor(c);
        g2.fillPolygon(px, py, 3);
        // Optional outline
        g2.setColor(c.darker());
        g2.setStroke(new BasicStroke(1f));
        g2.drawPolygon(px, py, 3);
    }
}