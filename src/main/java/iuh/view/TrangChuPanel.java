package iuh.view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class TrangChuPanel extends JPanel {

    // --- BẢNG MÀU HIỆN ĐẠI ---
    static final Color BG      = new Color(0xF8FAFC);
    static final Color WHITE   = Color.WHITE;
    static final Color PRIMARY = new Color(0x3B82F6);
    static final Color TEXT_D  = new Color(0x1E293B);
    static final Color TEXT_M  = new Color(0x64748B);
    static final Color BORDER  = new Color(0xE2E8F0);
    static final Color INPUT_BG = new Color(0xF1F5F9);
    static final Color GREEN   = new Color(0x22C55E);

    static final Font F_TITLE  = new Font("Segoe UI", Font.BOLD, 16);
    static final Font F_NUM    = new Font("Segoe UI", Font.BOLD, 26);
    static final Font F_PLAIN  = new Font("Segoe UI", Font.PLAIN, 13);
    static final Font F_BOLD12 = new Font("Segoe UI", Font.BOLD, 12);
    static final Font F_BOLD13 = new Font("Segoe UI", Font.BOLD, 13);

    public TrangChuPanel() {
        setLayout(new BorderLayout());
        setBackground(BG);

        add(buildHeader(), BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(buildBody());
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getViewport().setBackground(BG);
        add(scroll, BorderLayout.CENTER);
    }

    // ── HEADER ──────────────────────────────────────────────────────────────
    private JPanel buildHeader() {
        JPanel h = new JPanel(new BorderLayout());
        h.setBackground(WHITE);
        h.setPreferredSize(new Dimension(0, 65));
        h.setBorder(new MatteBorder(0, 0, 1, 0, BORDER));

        // Search Box
        JPanel searchBox = new JPanel(new BorderLayout(10, 0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(INPUT_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
            }
        };
        searchBox.setOpaque(false);
        searchBox.setPreferredSize(new Dimension(300, 38));
        searchBox.setBorder(new EmptyBorder(0, 15, 0, 15));

        JLabel searchIcon = new JLabel(new CustomIcon("search"));
        JTextField searchField = new JTextField("Search for rooms and offers...");
        searchField.setBorder(null); searchField.setOpaque(false);
        searchField.setFont(F_PLAIN); searchField.setForeground(TEXT_M);

        searchBox.add(searchIcon, BorderLayout.WEST);
        searchBox.add(searchField, BorderLayout.CENTER);

        JPanel leftWrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 13));
        leftWrap.setOpaque(false);
        leftWrap.add(searchBox);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 13));
        right.setOpaque(false);

        JLabel bell = new JLabel(new CustomIcon("bell"));
        JPanel avatar = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(0x60A5FA), 0, getHeight(), PRIMARY);
                g2.setPaint(gp);
                g2.fillOval(0, 0, 38, 38);
                g2.setColor(WHITE);
                g2.setFont(F_BOLD13);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString("N", 19 - fm.stringWidth("N")/2, 19 + fm.getAscent()/2 - 2);
                g2.dispose();
            }
        };
        avatar.setPreferredSize(new Dimension(38, 38)); avatar.setOpaque(false);

        right.add(bell); right.add(avatar);
        h.add(leftWrap, BorderLayout.WEST);
        h.add(right, BorderLayout.EAST);
        return h;
    }

    // ── BODY ────────────────────────────────────────────────────────────────
    private JPanel buildBody() {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(BG);
        outer.setBorder(new EmptyBorder(25, 30, 25, 30));

        JPanel body = new JPanel(new GridBagLayout());
        body.setBackground(BG);
        outer.add(body, BorderLayout.NORTH);

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1.0; gc.gridx = 0;

        gc.gridy = 0; body.add(buildTongQuanCard(), gc);
        gc.gridy = 1; body.add(Box.createVerticalStrut(20), gc);

        JPanel row2 = new JPanel(new GridBagLayout());
        row2.setOpaque(false);
        GridBagConstraints g2 = new GridBagConstraints();
        g2.fill = GridBagConstraints.BOTH; g2.weighty = 1;

        g2.gridx = 0; g2.weightx = 0.65;
        row2.add(buildLeftColumn(), g2);

        g2.gridx = 1; g2.weightx = 0.35; g2.insets = new Insets(0, 20, 0, 0);
        row2.add(buildRightColumn(), g2);

        gc.gridy = 2; body.add(row2, gc);
        return outer;
    }

    private JPanel buildTongQuanCard() {
        JPanel card = card();
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(20, 25, 20, 25));

        JPanel titleRow = new JPanel(new BorderLayout());
        titleRow.setOpaque(false);
        titleRow.add(lbl("Tổng quan", F_TITLE, TEXT_D), BorderLayout.WEST);
        JLabel date = lbl("Friday, Nov 18, 2022", F_PLAIN, TEXT_M);
        date.setHorizontalAlignment(SwingConstants.CENTER);
        titleRow.add(date, BorderLayout.CENTER);
        titleRow.add(styledBtn("Tạo đặt phòng"), BorderLayout.EAST);
        card.add(titleRow, BorderLayout.NORTH);

        JPanel stats = new JPanel(new GridLayout(1, 5, 10, 0));
        stats.setOpaque(false);
        stats.setBorder(new EmptyBorder(20, 0, 0, 0));
        stats.add(statItem("Today's", "Check-in", "23"));
        stats.add(statItem("Today's", "Check-out", "13"));
        stats.add(statItem("Total", "Phòng", "130"));
        stats.add(statItem("Tổng", "Trống", "10"));
        stats.add(statItem("Tổng", "Đang ở", "90"));
        card.add(stats, BorderLayout.CENTER);

        return card;
    }

    // ── LEFT COLUMN ─────────────────────────────────────────────────────────
    private JPanel buildLeftColumn() {
        JPanel col = new JPanel();
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
        col.setOpaque(false);

        // Phòng Card
        JPanel phongCard = card();
        phongCard.setLayout(new BorderLayout());
        phongCard.setBorder(new EmptyBorder(16, 18, 16, 18));
        phongCard.add(lbl("Phòng", F_TITLE, TEXT_D), BorderLayout.NORTH);

        JPanel rooms = new JPanel(new GridLayout(1, 3, 12, 0));
        rooms.setOpaque(false);
        rooms.setBorder(new EmptyBorder(12, 0, 0, 0));
        rooms.add(roomTypeCard("VIP", "2/30", "$ 568", GREEN));
        rooms.add(roomTypeCard("Thường", "2/35", "$ 1,068", PRIMARY));
        rooms.add(roomRevenueCard());
        phongCard.add(rooms, BorderLayout.CENTER);

        col.add(phongCard);
        col.add(Box.createVerticalStrut(20));

        // Trạng thái Card
        JPanel statusCard = card();
        statusCard.setLayout(new BoxLayout(statusCard, BoxLayout.Y_AXIS));
        statusCard.setBorder(new EmptyBorder(16, 18, 16, 18));
        statusCard.add(lbl("Trạng thái phòng", F_TITLE, TEXT_D));
        statusCard.add(Box.createVerticalStrut(12));

        JPanel sGrid = new JPanel(new GridLayout(2, 2, 0, 8));
        sGrid.setOpaque(false);
        sGrid.add(statusEntry("Phòng đang ở", "90"));
        sGrid.add(statusEntry("Phòng trống", "10"));
        sGrid.add(statusEntry("Phòng đã đặt", "30"));
        statusCard.add(sGrid);

        col.add(statusCard);
        col.add(Box.createVerticalStrut(20));

        // Chart Card
        JPanel chartCard = card();
        chartCard.setLayout(new BorderLayout());
        chartCard.setBorder(new EmptyBorder(16, 18, 16, 18));
        chartCard.setPreferredSize(new Dimension(0, 220));
        chartCard.add(lbl("Thống kê", F_TITLE, TEXT_D), BorderLayout.NORTH);
        int[] vals = {82, 68, 55, 40, 78, 60, 92, 85, 88, 75};
        String[] months = {"May","Jun","Jul","Aug","Sep","Oct","Nov","Dec","Jan","Feb"};
        chartCard.add(new MiniBarChart(vals, months), BorderLayout.CENTER);

        col.add(chartCard);
        return col;
    }

    // ── RIGHT COLUMN ────────────────────────────────────────────────────────
    private JPanel buildRightColumn() {
        JPanel col = new JPanel();
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
        col.setOpaque(false);

        // Nhân viên
        JPanel nvCard = card();
        nvCard.setLayout(new BoxLayout(nvCard, BoxLayout.Y_AXIS));
        nvCard.setBorder(new EmptyBorder(16, 18, 16, 18));
        nvCard.add(lbl("Thông tin nhân viên", F_TITLE, TEXT_D));
        nvCard.add(Box.createVerticalStrut(10));
        nvCard.add(lbl("Nguyen Hoang", F_BOLD13, TEXT_D));
        nvCard.add(lbl("yourname@gmail.com", F_PLAIN, TEXT_M));
        nvCard.add(Box.createVerticalStrut(15));
        nvCard.add(sep());
        nvCard.add(Box.createVerticalStrut(10));
        nvCard.add(infoRow("Số điện thoại", "0123456789"));
        nvCard.add(Box.createVerticalStrut(8));
        nvCard.add(infoRow("Chức vụ", "Nhân Viên"));

        col.add(nvCard);
        col.add(Box.createVerticalStrut(20));

        // Phản hồi
        JPanel fbCard = card();
        fbCard.setLayout(new BoxLayout(fbCard, BoxLayout.Y_AXIS));
        fbCard.setBorder(new EmptyBorder(16, 18, 16, 18));
        fbCard.add(lbl("Phản hồi khách hàng", F_TITLE, TEXT_D));
        fbCard.add(Box.createVerticalStrut(12));
        fbCard.add(feedbackItem("Mark", "A201", "Đồ ăn có thể tốt hơn."));
        fbCard.add(Box.createVerticalStrut(10));
        fbCard.add(feedbackItem("Christian", "A101", "Cơ sở tốt."));

        col.add(fbCard);
        return col;
    }

    // ── COMPONENT HELPERS ───────────────────────────────────────────────────
    private JPanel roomTypeCard(String type, String count, String price, Color color) {
        JPanel p = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0xF8FAFC));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(BORDER);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 12, 12);
                g2.dispose();
            }
        };
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(12, 12, 12, 12));
        p.setOpaque(false);

        JLabel tag = lbl("2 Deals", new Font("Segoe UI", Font.BOLD, 10), WHITE);
        tag.setOpaque(true); tag.setBackground(color);
        tag.setBorder(new EmptyBorder(2, 6, 2, 6));

        p.add(tag); p.add(Box.createVerticalStrut(8));
        p.add(lbl(type, F_BOLD12, TEXT_M));
        p.add(lbl(count, F_BOLD13, TEXT_D));
        p.add(lbl(price, F_BOLD12, PRIMARY));
        return p;
    }

    private JPanel roomRevenueCard() {
        JPanel p = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0xEFF6FF));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
            }
        };
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(12, 12, 12, 12));
        p.setOpaque(false);
        p.add(lbl("Tổng doanh thu", F_BOLD12, TEXT_M));
        p.add(Box.createVerticalStrut(5));
        p.add(lbl("1.200.000đ", F_BOLD13, PRIMARY));
        return p;
    }

    private JPanel statusEntry(String label, String val) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        p.setOpaque(false);
        p.add(lbl(label + ": ", F_PLAIN, TEXT_M));
        p.add(lbl(val, F_BOLD13, TEXT_D));
        return p;
    }

    private JPanel infoRow(String k, String v) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.add(lbl(k, F_PLAIN, TEXT_M), BorderLayout.WEST);
        p.add(lbl(v, F_PLAIN, TEXT_D), BorderLayout.EAST);
        return p;
    }

    private JPanel feedbackItem(String name, String room, String msg) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        JPanel left = new JPanel(); left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);
        left.add(lbl(name, F_BOLD12, TEXT_D));
        left.add(lbl(msg, new Font("Segoe UI", Font.PLAIN, 11), TEXT_M));
        p.add(left, BorderLayout.CENTER);
        JLabel rTag = lbl(room, F_BOLD12, PRIMARY);
        rTag.setBorder(new EmptyBorder(2, 6, 2, 6));
        p.add(rTag, BorderLayout.EAST);
        return p;
    }

    private JSeparator sep() { JSeparator s = new JSeparator(); s.setForeground(BORDER); return s; }

    private JPanel statItem(String sub, String label, String value) {
        JPanel p = new JPanel(); p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        p.add(lbl(sub, new Font("Segoe UI", Font.PLAIN, 11), TEXT_M));
        p.add(lbl(label, new Font("Segoe UI", Font.PLAIN, 12), TEXT_M));
        p.add(lbl(value, F_NUM, PRIMARY));
        return p;
    }

    static JPanel card() {
        JPanel p = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(BORDER);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 16, 16);
                g2.dispose();
            }
        };
        p.setOpaque(false);
        return p;
    }

    static JLabel lbl(String t, Font f, Color c) {
        JLabel l = new JLabel(t); l.setFont(f); l.setForeground(c); return l;
    }

    static JButton styledBtn(String text) {
        JButton b = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(PRIMARY);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        b.setFont(F_BOLD13); b.setForeground(WHITE);
        b.setOpaque(false); b.setContentAreaFilled(false); b.setBorderPainted(false);
        b.setBorder(new EmptyBorder(8, 18, 8, 18));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    class CustomIcon implements Icon {
        String type;
        CustomIcon(String type) { this.type = type; }
        @Override public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setStroke(new BasicStroke(1.6f));
            if (type.equals("search")) {
                g2.setColor(TEXT_M); g2.drawOval(x, y, 11, 11); g2.drawLine(x+10, y+10, x+14, y+14);
            } else if (type.equals("bell")) {
                g2.setColor(TEXT_D); g2.drawRoundRect(x+2, y, 12, 13, 4, 4); g2.fillOval(x+6, y+14, 4, 2);
            }
            g2.dispose();
        }
        @Override public int getIconWidth() { return 18; }
        @Override public int getIconHeight() { return 18; }
    }
}

// ── GIỮ NGUYÊN MINI BAR CHART CỦA BẠN (Cập nhật màu PRIMARY) ───────────────
class MiniBarChart extends JPanel {
    int[] vals; String[] labels;
    MiniBarChart(int[] vals, String[] labels) { this.vals = vals; this.labels = labels; setOpaque(false); }
    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth(), h = getHeight();
        int padL = 36, padR = 8, padT = 8, padB = 26;
        int cw = w - padL - padR, ch = h - padT - padB;
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        for (int p = 0; p <= 100; p += 25) {
            int y = padT + ch - (int)(ch * p / 100.0);
            g2.setColor(new Color(0xF1F5F9));
            g2.drawLine(padL, y, w - padR, y);
            g2.setColor(new Color(0x94A3B8));
            g2.drawString(p + "%", 5, y + 4);
        }
        int n = vals.length, barW = (int)(cw / (n * 1.6f)), gap = (cw - barW * n) / (n + 1);
        for (int i = 0; i < n; i++) {
            int bh = (int)(ch * vals[i] / 100.0), bx = padL + gap + i * (barW + gap), by = padT + ch - bh;
            g2.setColor(new Color(0x3B82F6)); // Màu PRIMARY
            g2.fill(new RoundRectangle2D.Float(bx, by, barW, bh, 6, 6));
            g2.setColor(new Color(0x64748B));
            g2.drawString(labels[i], bx + barW/2 - g2.getFontMetrics().stringWidth(labels[i])/2, h - 8);
        }
        g2.dispose();
    }
}