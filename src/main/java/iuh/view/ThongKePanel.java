package iuh.view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;

public class ThongKePanel extends JPanel {

    static final Color BG_MAIN    = new Color(0xF4F6FB);
    static final Color BG_WHITE   = Color.WHITE;
    static final Color BLUE       = new Color(0x3B6FF0);
    static final Color BLUE_LIGHT = new Color(0xDDE8FF);
    static final Color TEXT_DARK  = new Color(0x1A1A2E);
    static final Color TEXT_MID   = new Color(0x5A6070);
    static final Color TEXT_GRAY  = new Color(0xA0A8B8);
    static final Color BORDER     = new Color(0xE4E9F2);
    static final Color RED_TEXT   = new Color(0xE04040);
    static final Color RED_BG     = new Color(0xFFF0F0);
    static final Color YELLOW     = new Color(0xF5B942);
    static final Color PINK       = new Color(0xE86FA8);
    static final Color PURPLE     = new Color(0x7B61FF);

    static final Font F_H1     = new Font("Segoe UI", Font.BOLD, 32);
    static final Font F_H2     = new Font("Segoe UI", Font.BOLD, 22);
    static final Font F_TITLE  = new Font("Segoe UI", Font.BOLD, 15);
    static final Font F_LABEL  = new Font("Segoe UI", Font.PLAIN, 12);
    static final Font F_SMALL  = new Font("Segoe UI", Font.PLAIN, 11);
    static final Font F_BOLD12 = new Font("Segoe UI", Font.BOLD, 12);

    public ThongKePanel() {
        setLayout(new BorderLayout());
        setBackground(BG_MAIN);

        JScrollPane scroll = new JScrollPane(buildContent());
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(12);
        scroll.setBackground(BG_MAIN);
        scroll.getViewport().setBackground(BG_MAIN);
        add(scroll, BorderLayout.CENTER);
    }

    private JPanel buildContent() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(BG_MAIN);
        content.setBorder(new EmptyBorder(20, 24, 24, 24));

        // Row 1: 3 stat cards
        JPanel row1 = new JPanel(new GridLayout(1, 3, 16, 0));
        row1.setOpaque(false);
        row1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));
        row1.add(buildKhachCard());
        row1.add(buildTyLeCard());
        row1.add(buildDatPhongCard());

        content.add(row1);
        content.add(Box.createVerticalStrut(18));

        // Row 2: bar chart + line chart side by side
        JPanel row2 = new JPanel(new GridBagLayout());
        row2.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        gbc.gridy = 0;

        gbc.gridx = 0; gbc.weightx = 0.62;
        row2.add(buildBarChartCard(), gbc);
        gbc.gridx = 1; gbc.weightx = 0.38;
        gbc.insets = new Insets(0, 16, 0, 0);
        row2.add(buildLineChartCard(), gbc);

        JPanel row2Wrapper = new JPanel(new BorderLayout());
        row2Wrapper.setOpaque(false);
        row2Wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 320));
        row2Wrapper.add(row2);
        content.add(row2Wrapper);
        content.add(Box.createVerticalStrut(18));

        // Row 3: area chart + donut chart side by side
        JPanel row3 = new JPanel(new GridBagLayout());
        row3.setOpaque(false);
        GridBagConstraints gbc3 = new GridBagConstraints();
        gbc3.fill = GridBagConstraints.BOTH;
        gbc3.weighty = 1.0;
        gbc3.gridy = 0;

        gbc3.gridx = 0; gbc3.weightx = 0.62;
        row3.add(buildAreaChartCard(), gbc3);
        gbc3.gridx = 1; gbc3.weightx = 0.38;
        gbc3.insets = new Insets(0, 16, 0, 0);
        row3.add(buildDonutCard(), gbc3);

        JPanel row3Wrapper = new JPanel(new BorderLayout());
        row3Wrapper.setOpaque(false);
        row3Wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
        row3Wrapper.add(row3);
        content.add(row3Wrapper);

        return content;
    }

    // ── Card 1: Khách đang lưu trú ──────────────────────────────────────────
    private JPanel buildKhachCard() {
        JPanel card = card();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(18, 20, 16, 20));

        card.add(label("Khách đang lưu trú", F_LABEL, TEXT_GRAY));
        card.add(Box.createVerticalStrut(4));
        card.add(label("48", F_H1, TEXT_DARK));
        card.add(Box.createVerticalStrut(10));

        // Progress bar area
        JPanel progRow = new JPanel(new BorderLayout());
        progRow.setOpaque(false);
        JLabel pct = label("32%", F_SMALL, TEXT_GRAY);
        JLabel cap = label("48 / 150", F_SMALL, TEXT_GRAY);
        progRow.add(pct, BorderLayout.WEST);
        progRow.add(cap, BorderLayout.EAST);
        card.add(progRow);
        card.add(Box.createVerticalStrut(4));

        JPanel bar = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0xE8EEFF));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                int fill = (int)(getWidth() * 0.32);
                g2.setColor(BLUE);
                g2.fillRoundRect(0, 0, fill, getHeight(), 6, 6);
                g2.dispose();
            }
        };
        bar.setOpaque(false);
        bar.setPreferredSize(new Dimension(0, 8));
        bar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 8));
        card.add(bar);
        card.add(Box.createVerticalStrut(8));
        card.add(label("Đang sử dụng", F_SMALL, TEXT_GRAY));
        return card;
    }

    // ── Card 2: Tỷ lệ hủy phòng ─────────────────────────────────────────────
    private JPanel buildTyLeCard() {
        JPanel card = card();
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(18, 20, 16, 20));

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);
        left.add(label("Tỷ lệ hủy phòng", F_LABEL, TEXT_GRAY));
        left.add(Box.createVerticalStrut(4));
        left.add(label("65%", F_H1, TEXT_DARK));
        left.add(Box.createVerticalStrut(10));
        left.add(label("70% trong tuần này", F_SMALL, TEXT_MID));
        left.add(Box.createVerticalStrut(2));
        left.add(label("52% trong tháng trước", F_SMALL, TEXT_MID));

        // Goal ring
        JPanel ring = new GoalRing(65);
        ring.setPreferredSize(new Dimension(90, 90));

        card.add(left, BorderLayout.CENTER);
        card.add(ring, BorderLayout.EAST);
        return card;
    }

    // ── Card 3: Đặt phòng trong 30 ngày ─────────────────────────────────────
    private JPanel buildDatPhongCard() {
        JPanel card = card();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(18, 20, 16, 20));

        card.add(label("Đặt phòng trong", F_LABEL, TEXT_GRAY));
        card.add(label("30 ngày", F_LABEL, TEXT_GRAY));
        card.add(Box.createVerticalStrut(4));
        card.add(label("352", F_H1, TEXT_DARK));
        card.add(Box.createVerticalStrut(8));

        JPanel badgeRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        badgeRow.setOpaque(false);
        JLabel ago = label("30 ngày qua", F_SMALL, TEXT_GRAY);
        badgeRow.add(ago);
        badgeRow.add(Box.createHorizontalStrut(8));

        JLabel badge = new JLabel("↓ 36.24%") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(RED_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        badge.setFont(F_BOLD12);
        badge.setForeground(RED_TEXT);
        badge.setOpaque(false);
        badge.setBorder(new EmptyBorder(3, 8, 3, 8));
        badgeRow.add(badge);

        card.add(badgeRow);
        return card;
    }

    // ── Bar Chart: Thống kê tỷ lệ đặt phòng ────────────────────────────────
    private JPanel buildBarChartCard() {
        JPanel card = card();
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(18, 20, 16, 20));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.add(label("Thống kê tỷ lệ đặt phòng", F_TITLE, TEXT_DARK), BorderLayout.WEST);

        JButton monthBtn = pillBtn("📅  Tháng");
        header.add(monthBtn, BorderLayout.EAST);
        card.add(header, BorderLayout.NORTH);
        card.add(Box.createVerticalStrut(12));

        int[] vals = {82, 68, 55, 40, 78, 60, 92, 85, 88, 75};
        String[] months = {"May","Jun","Jul","Aug","Sep","Oct","Nov","Dec","Jan","Feb"};

        JPanel chart = new BarChart(vals, months);
        card.add(chart, BorderLayout.CENTER);
        return card;
    }

    // ── Line Chart: Khách hàng mới ───────────────────────────────────────────
    private JPanel buildLineChartCard() {
        JPanel card = card();
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(18, 20, 16, 20));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.add(label("Khách hàng mới", F_TITLE, TEXT_DARK), BorderLayout.WEST);

        JPanel legend = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        legend.setOpaque(false);
        legend.add(legendDot(BLUE, "Tháng này"));
        legend.add(legendDot(YELLOW, "Tháng 1"));
        header.add(legend, BorderLayout.EAST);
        card.add(header, BorderLayout.NORTH);

        int[] line1 = {20, 35, 28, 50, 45, 60, 70};
        int[] line2 = {10, 25, 40, 55, 48, 72, 90};
        String[] xlabels = {"JAN","FEB","MAR","APR","MAY","JUN","JUL"};

        JPanel chart = new LineChart(line1, line2, xlabels);
        card.add(chart, BorderLayout.CENTER);
        return card;
    }

    // ── Area Chart: Doanh thu ────────────────────────────────────────────────
    private JPanel buildAreaChartCard() {
        JPanel card = card();
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(18, 20, 16, 20));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.add(label("Doanh thu", F_TITLE, TEXT_DARK), BorderLayout.WEST);

        JButton monthBtn = pillBtn("October  ▾");
        header.add(monthBtn, BorderLayout.EAST);
        card.add(header, BorderLayout.NORTH);

        int[] vals = {20,28,25,35,30,38,45,40,50,64,48,42,55,50,45,52,48,42,38,45,50,48,55,52,48,50,45,42,48,50,45,52,48,55,60,58,52};
        String[] xlabels = {"5k","10k","15k","20k","25k","30k","35k","40k","45k","50k","55k","60k"};

        JPanel chart = new AreaChart(vals, xlabels);
        card.add(chart, BorderLayout.CENTER);
        return card;
    }

    // ── Donut Chart: Sử dụng dịch vụ ────────────────────────────────────────
    private JPanel buildDonutCard() {
        JPanel card = card();
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(18, 20, 16, 20));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.add(label("Sử dụng dịch vụ", F_TITLE, TEXT_DARK), BorderLayout.WEST);

        JButton reportBtn = new JButton("Xem báo cáo") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(BORDER);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        reportBtn.setFont(F_LABEL);
        reportBtn.setForeground(TEXT_MID);
        reportBtn.setOpaque(false);
        reportBtn.setContentAreaFilled(false);
        reportBtn.setBorderPainted(false);
        reportBtn.setBorder(new EmptyBorder(5, 12, 5, 12));
        header.add(reportBtn, BorderLayout.EAST);
        card.add(header, BorderLayout.NORTH);

        JLabel sub = label("From 1-6 Dec, 2021", F_SMALL, TEXT_GRAY);
        JPanel subPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 4));
        subPanel.setOpaque(false);
        subPanel.add(sub);
        card.add(subPanel, BorderLayout.AFTER_LAST_LINE);

        // Donut
        int[] segments = {40, 32, 28};
        Color[] colors = {BLUE, PURPLE, PINK};
        JPanel donut = new DonutChart(segments, colors);
        card.add(donut, BorderLayout.CENTER);

        // Legend
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 4));
        legendPanel.setOpaque(false);
        String[] names = {"Ăn uống", "Giặt ủi", "Khác"};
        String[] pcts  = {"40%", "32%", "28%"};
        for (int i = 0; i < 3; i++) {
            JPanel leg = new JPanel();
            leg.setLayout(new BoxLayout(leg, BoxLayout.Y_AXIS));
            leg.setOpaque(false);
            JLabel dot = new JLabel("●");
            dot.setForeground(colors[i]);
            dot.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            dot.setAlignmentX(Component.CENTER_ALIGNMENT);
            JLabel nm = label(names[i], F_SMALL, TEXT_MID);
            nm.setAlignmentX(Component.CENTER_ALIGNMENT);
            JLabel pc = label(pcts[i], F_BOLD12, TEXT_DARK);
            pc.setAlignmentX(Component.CENTER_ALIGNMENT);
            leg.add(dot); leg.add(nm); leg.add(pc);
            legendPanel.add(leg);
        }
        card.add(legendPanel, BorderLayout.SOUTH);
        return card;
    }

    // ── Helpers ──────────────────────────────────────────────────────────────
    static JPanel card() {
        JPanel p = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.setColor(new Color(0xE8EEF8));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 14, 14);
                g2.dispose();
            }
        };
        p.setOpaque(false);
        return p;
    }

    static JLabel label(String text, Font f, Color c) {
        JLabel l = new JLabel(text);
        l.setFont(f);
        l.setForeground(c);
        return l;
    }

    static JButton pillBtn(String text) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(new Color(0xDDE3EF));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btn.setForeground(TEXT_MID);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setBorder(new EmptyBorder(5, 14, 5, 14));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    static JPanel legendDot(Color c, String text) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        p.setOpaque(false);
        JLabel dot = new JLabel("●");
        dot.setForeground(c);
        dot.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbl.setForeground(TEXT_MID);
        p.add(dot); p.add(lbl);
        return p;
    }
}

// ── Goal Ring ────────────────────────────────────────────────────────────────
class GoalRing extends JPanel {
    int percent;
    GoalRing(int percent) {
        this.percent = percent;
        setOpaque(false);
    }
    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int s = Math.min(getWidth(), getHeight()) - 10;
        int x = (getWidth() - s) / 2;
        int y = (getHeight() - s) / 2;
        g2.setStroke(new BasicStroke(9, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(new Color(0xE8EEFF));
        g2.drawOval(x, y, s, s);
        g2.setColor(new Color(0x3B6FF0));
        int arc = (int)(360 * percent / 100.0);
        g2.drawArc(x, y, s, s, 90, -arc);
        // Label
        g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
        g2.setColor(new Color(0x1A1A2E));
        FontMetrics fm = g2.getFontMetrics();
        String goal = "Goal";
        g2.drawString(goal, getWidth()/2 - fm.stringWidth(goal)/2, getHeight()/2 + fm.getAscent()/2 - 2);
        g2.dispose();
    }
}

// ── Bar Chart ────────────────────────────────────────────────────────────────
class BarChart extends JPanel {
    int[] vals; String[] labels;
    BarChart(int[] vals, String[] labels) {
        this.vals = vals; this.labels = labels;
        setOpaque(false);
    }
    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth(), h = getHeight();
        int padL = 40, padR = 10, padT = 10, padB = 30;
        int chartW = w - padL - padR;
        int chartH = h - padT - padB;

        // Y grid lines + labels
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        g2.setStroke(new BasicStroke(1));
        int[] yPcts = {0,25,50,75,100};
        for (int yp : yPcts) {
            int y = padT + chartH - (int)(chartH * yp / 100.0);
            g2.setColor(new Color(0xEEF0F6));
            g2.drawLine(padL, y, w - padR, y);
            g2.setColor(new Color(0xA0A8B8));
            g2.drawString(yp + "%", 2, y + 4);
        }

        // Bars
        int n = vals.length;
        int barW = (int)(chartW / (n * 1.6));
        int gap  = (chartW - barW * n) / (n + 1);
        for (int i = 0; i < n; i++) {
            int bh = (int)(chartH * vals[i] / 100.0);
            int bx = padL + gap + i * (barW + gap);
            int by = padT + chartH - bh;
            g2.setColor(new Color(0x3B6FF0));
            RoundRectangle2D rr = new RoundRectangle2D.Float(bx, by, barW, bh, 6, 6);
            g2.fill(rr);
            // Month label
            g2.setColor(new Color(0xA0A8B8));
            FontMetrics fm = g2.getFontMetrics();
            int lx = bx + barW/2 - fm.stringWidth(labels[i])/2;
            g2.drawString(labels[i], lx, h - padB + 16);
        }
        g2.dispose();
    }
}

// ── Line Chart ───────────────────────────────────────────────────────────────
class LineChart extends JPanel {
    int[] line1, line2; String[] xlabels;
    LineChart(int[] line1, int[] line2, String[] xlabels) {
        this.line1 = line1; this.line2 = line2; this.xlabels = xlabels;
        setOpaque(false);
    }
    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth(), h = getHeight();
        int padL = 10, padR = 10, padT = 16, padB = 24;
        int chartW = w - padL - padR;
        int chartH = h - padT - padB;
        int n = xlabels.length;

        drawLine(g2, line1, n, padL, padT, chartW, chartH, new Color(0x3B6FF0));
        drawLine(g2, line2, n, padL, padT, chartW, chartH, new Color(0xF5B942));

        // X labels
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        g2.setColor(new Color(0xA0A8B8));
        for (int i = 0; i < n; i++) {
            int px = padL + (int)(chartW * i / (n - 1.0));
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(xlabels[i], px - fm.stringWidth(xlabels[i])/2, h - padB + 14);
        }
        g2.dispose();
    }

    private void drawLine(Graphics2D g2, int[] vals, int n, int padL, int padT, int chartW, int chartH, Color c) {
        int maxV = 100;
        int[] xs = new int[n], ys = new int[n];
        for (int i = 0; i < n; i++) {
            xs[i] = padL + (int)(chartW * i / (n - 1.0));
            ys[i] = padT + chartH - (int)(chartH * vals[i] / maxV);
        }
        // Draw line
        g2.setColor(c);
        g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        for (int i = 0; i < n - 1; i++) {
            g2.drawLine(xs[i], ys[i], xs[i+1], ys[i+1]);
        }
        // Dots
        for (int i = 0; i < n; i++) {
            g2.setColor(Color.WHITE);
            g2.fillOval(xs[i]-3, ys[i]-3, 6, 6);
            g2.setColor(c);
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawOval(xs[i]-3, ys[i]-3, 6, 6);
        }
    }
}

// ── Area Chart ───────────────────────────────────────────────────────────────
class AreaChart extends JPanel {
    int[] vals; String[] xlabels;
    int tooltipIdx = 9;
    AreaChart(int[] vals, String[] xlabels) {
        this.vals = vals; this.xlabels = xlabels;
        setOpaque(false);
    }
    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth(), h = getHeight();
        int padL = 32, padR = 10, padT = 30, padB = 28;
        int chartW = w - padL - padR;
        int chartH = h - padT - padB;
        int n = vals.length;
        int maxV = 100;

        // Y grid
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        int[] yVals = {20,40,60,80,100};
        for (int yv : yVals) {
            int y = padT + chartH - (int)(chartH * yv / 100.0);
            g2.setColor(new Color(0xEEF0F6));
            g2.setStroke(new BasicStroke(1));
            g2.drawLine(padL, y, w - padR, y);
            g2.setColor(new Color(0xA0A8B8));
            g2.drawString(yv+"", 2, y + 4);
        }

        // Compute points
        int[] xs = new int[n], ys = new int[n];
        for (int i = 0; i < n; i++) {
            xs[i] = padL + (int)(chartW * i / (n - 1.0));
            ys[i] = padT + chartH - (int)(chartH * vals[i] / maxV);
        }

        // Fill area
        Polygon poly = new Polygon();
        poly.addPoint(xs[0], padT + chartH);
        for (int i = 0; i < n; i++) poly.addPoint(xs[i], ys[i]);
        poly.addPoint(xs[n-1], padT + chartH);
        GradientPaint gp = new GradientPaint(0, padT, new Color(0x3B6FF0, true), 0, padT+chartH, new Color(0xDDE8FF, false));
        // Use alpha gradient
        g2.setPaint(new GradientPaint(0, padT, new Color(59, 111, 240, 80), 0, padT+chartH, new Color(59, 111, 240, 10)));
        g2.fillPolygon(poly);

        // Line
        g2.setColor(new Color(0x3B6FF0));
        g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        for (int i = 0; i < n - 1; i++) {
            g2.drawLine(xs[i], ys[i], xs[i+1], ys[i+1]);
        }

        // Dots
        for (int i = 0; i < n; i++) {
            g2.setColor(Color.WHITE);
            g2.fillOval(xs[i]-3, ys[i]-3, 6, 6);
            g2.setColor(new Color(0x3B6FF0));
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawOval(xs[i]-3, ys[i]-3, 6, 6);
        }

        // Tooltip at peak (index 9 = 64.3664)
        if (tooltipIdx < n) {
            int tx = xs[tooltipIdx], ty = ys[tooltipIdx];
            String tip = "64,3664";
            g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
            FontMetrics fm = g2.getFontMetrics();
            int tw = fm.stringWidth(tip) + 16, th = 22;
            int bx = tx - tw/2, by = ty - th - 8;
            g2.setColor(new Color(0x3B6FF0));
            g2.fillRoundRect(bx, by, tw, th, 6, 6);
            g2.setColor(Color.WHITE);
            g2.drawString(tip, bx + 8, by + th - 6);
            // Arrow
            int[] ax = {tx-5, tx+5, tx};
            int[] ay = {by+th, by+th, by+th+6};
            g2.setColor(new Color(0x3B6FF0));
            g2.fillPolygon(ax, ay, 3);
        }

        // X labels
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        g2.setColor(new Color(0xA0A8B8));
        int step = n / xlabels.length;
        for (int i = 0; i < xlabels.length; i++) {
            int idx = i * step;
            if (idx >= n) idx = n - 1;
            FontMetrics fm = g2.getFontMetrics();
            int lx = xs[idx] - fm.stringWidth(xlabels[i])/2;
            g2.drawString(xlabels[i], lx, h - 10);
        }
        g2.dispose();
    }
}

// ── Donut Chart ───────────────────────────────────────────────────────────────
class DonutChart extends JPanel {
    int[] segments; Color[] colors;
    DonutChart(int[] segments, Color[] colors) {
        this.segments = segments; this.colors = colors;
        setOpaque(false);
        setPreferredSize(new Dimension(0, 160));
    }
    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int size = Math.min(getWidth(), getHeight()) - 20;
        if (size < 20) { g2.dispose(); return; }
        int x = (getWidth() - size) / 2;
        int y = (getHeight() - size) / 2;
        int total = 0; for (int s : segments) total += s;
        float start = -90;
        for (int i = 0; i < segments.length; i++) {
            float arc = 360f * segments[i] / total;
            g2.setColor(colors[i]);
            g2.setStroke(new BasicStroke(22, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
            g2.drawArc(x+11, y+11, size-22, size-22, (int)start, -(int)arc);
            start -= arc;
        }
        // White background gap (simulate donut hole)
        g2.setColor(new Color(0xF4F6FB));
        int inner = size - 50;
        g2.fillOval((getWidth()-inner)/2, (getHeight()-inner)/2, inner, inner);
        g2.dispose();
    }
}