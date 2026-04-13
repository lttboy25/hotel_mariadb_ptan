package iuh.view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class TrangChuPanel extends JPanel {

    static final Color BG      = new Color(0xF4F6FB);
    static final Color WHITE   = Color.WHITE;
    static final Color BLUE    = new Color(0x3B6FF0);
    static final Color BL      = new Color(0xEBF0FF);
    static final Color DARK    = new Color(0x1A1A2E);
    static final Color MID     = new Color(0x4A5268);
    static final Color GRAY    = new Color(0xA0A8B8);
    static final Color BORDER  = new Color(0xE4E9F2);
    static final Color GREEN   = new Color(0x22C55E);
    static final Color GREEN_L = new Color(0xDCFCE7);

    static final Font F_TITLE  = new Font("Segoe UI", Font.BOLD, 15);
    static final Font F_NUM    = new Font("Segoe UI", Font.BOLD, 28);
    static final Font F_LABEL  = new Font("Segoe UI", Font.PLAIN, 11);
    static final Font F_SMALL  = new Font("Segoe UI", Font.PLAIN, 12);
    static final Font F_BOLD12 = new Font("Segoe UI", Font.BOLD, 12);
    static final Font F_BOLD13 = new Font("Segoe UI", Font.BOLD, 13);
    static final Font F_PLAIN13= new Font("Segoe UI", Font.PLAIN, 13);

    public TrangChuPanel() {
        setLayout(new BorderLayout());
        setBackground(BG);

        // Top header bar
        JPanel header = buildHeader();
        add(header, BorderLayout.NORTH);

        // Scrollable body
        JScrollPane scroll = new JScrollPane(buildBody());
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(12);
        scroll.setBackground(BG);
        scroll.getViewport().setBackground(BG);
        add(scroll, BorderLayout.CENTER);
    }

    // ── Header ────────────────────────────────────────────────────────────────
    private JPanel buildHeader() {
        JPanel h = new JPanel(new BorderLayout());
        h.setBackground(WHITE);
        h.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 1, 0, BORDER),
                new EmptyBorder(10, 24, 10, 24)
        ));
        h.setPreferredSize(new Dimension(0, 54));

        // Search box
        JPanel search = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0xF4F6FB));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2.dispose();
            }
        };
        search.setOpaque(false);
        search.setPreferredSize(new Dimension(240, 34));

        JLabel searchIcon = new JLabel("🔍");
        searchIcon.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JLabel searchText = new JLabel("Search for rooms and offers");
        searchText.setFont(F_SMALL);
        searchText.setForeground(GRAY);
        search.add(searchIcon);
        search.add(searchText);

        h.add(search, BorderLayout.WEST);

        // Right: bell + avatar
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        right.setOpaque(false);

        JLabel bell = new JLabel("🔔");
        bell.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        // Avatar circle
        JPanel avatar = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0x6B9EFF));
                g2.fillOval(0, 0, 34, 34);
                g2.setColor(WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString("N", 17 - fm.stringWidth("N")/2, 17 + fm.getAscent()/2 - 2);
                g2.dispose();
            }
        };
        avatar.setOpaque(false);
        avatar.setPreferredSize(new Dimension(34, 34));

        right.add(bell);
        right.add(avatar);
        h.add(right, BorderLayout.EAST);
        return h;
    }

    // ── Body ──────────────────────────────────────────────────────────────────
    private JPanel buildBody() {
        // Use BorderLayout wrapper so the inner GridBagLayout gets full width
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(BG);
        outer.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel body = new JPanel(new GridBagLayout());
        body.setBackground(BG);
        outer.add(body, BorderLayout.NORTH);

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1.0;
        gc.gridx = 0;

        // ── Row 1: Tổng quan card (full width) ────────────────────────────────
        gc.gridy = 0; gc.weighty = 0;
        body.add(buildTongQuanCard(), gc);

        gc.gridy = 1;
        body.add(Box.createVerticalStrut(16), gc);

        // ── Row 2: left(Phòng + Trạng thái + Thống kê)  |  right(Nhân viên + Phản hồi)
        JPanel row2 = new JPanel(new GridBagLayout());
        row2.setOpaque(false);
        GridBagConstraints g2 = new GridBagConstraints();
        g2.fill = GridBagConstraints.BOTH;
        g2.weighty = 1;
        g2.gridy = 0;

        g2.gridx = 0; g2.weightx = 0.64; g2.insets = new Insets(0, 0, 0, 0);
        row2.add(buildLeftColumn(), g2);

        g2.gridx = 1; g2.weightx = 0.36; g2.insets = new Insets(0, 14, 0, 0);
        row2.add(buildRightColumn(), g2);

        gc.gridy = 2; gc.weighty = 0; gc.fill = GridBagConstraints.BOTH;
        body.add(row2, gc);

        gc.gridy = 3; gc.weighty = 0; gc.fill = GridBagConstraints.HORIZONTAL;
        body.add(Box.createVerticalStrut(20), gc);

        return outer;
    }

    // ── Tổng quan card ────────────────────────────────────────────────────────
    private JPanel buildTongQuanCard() {
        JPanel card = card();
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(16, 20, 16, 20));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

        // Title row
        JPanel titleRow = new JPanel(new BorderLayout());
        titleRow.setOpaque(false);
        JLabel title = lbl("Tổng quan", F_TITLE, DARK);
        JLabel date = lbl("Friday, November 18,2022", F_SMALL, GRAY);

        // Tạo đặt phòng button
        JButton btn = new JButton("Tạo đặt phòng") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BLUE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(WHITE);
                g2.setFont(F_BOLD12);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth()-fm.stringWidth(getText()))/2, (getHeight()+fm.getAscent()-fm.getDescent())/2);
                g2.dispose();
            }
        };
        btn.setOpaque(false); btn.setContentAreaFilled(false); btn.setBorderPainted(false);
        btn.setForeground(WHITE); btn.setFont(F_BOLD12);
        btn.setBorder(new EmptyBorder(8, 16, 8, 16));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        titleRow.add(title, BorderLayout.WEST);
        JPanel middle = new JPanel(new FlowLayout(FlowLayout.CENTER));
        middle.setOpaque(false); middle.add(date);
        titleRow.add(middle, BorderLayout.CENTER);
        titleRow.add(btn, BorderLayout.EAST);
        card.add(titleRow, BorderLayout.NORTH);

        // Stats row
        JPanel stats = new JPanel(new GridLayout(1, 5, 0, 0));
        stats.setOpaque(false);
        stats.setBorder(new EmptyBorder(12, 0, 0, 0));

        stats.add(statItem("Today's", "Check-in", "23", BLUE));
        stats.add(statItem("Today's", "Check-out", "13", BLUE));
        stats.add(statItem("Total", "Phòng", "130", BLUE));
        stats.add(statItem("Tổng", "Phòng trống", "10", BLUE));
        stats.add(statItem("Tổng", "Phòng đang ở", "90", BLUE));
        card.add(stats, BorderLayout.CENTER);

        return card;
    }

    private JPanel statItem(String sub, String label, String value, Color numColor) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(0, 6, 0, 6));

        JLabel subLbl = lbl(sub, F_LABEL, GRAY);
        subLbl.setAlignmentX(LEFT_ALIGNMENT);

        JPanel valueRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        valueRow.setOpaque(false);
        valueRow.setAlignmentX(LEFT_ALIGNMENT);
        JLabel labelLbl = lbl(label, F_LABEL, GRAY);
        JLabel valueLbl = lbl(value, F_NUM, numColor);
        valueRow.add(labelLbl);
        valueRow.add(valueLbl);

        p.add(subLbl);
        p.add(valueRow);
        return p;
    }

    // ── Left column: Phòng + Trạng thái + Thống kê ───────────────────────────
    private JPanel buildLeftColumn() {
        JPanel col = new JPanel();
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
        col.setOpaque(false);

        col.add(buildPhongCard());
        col.add(Box.createVerticalStrut(14));
        col.add(buildTrangThaiCard());
        col.add(Box.createVerticalStrut(14));
        col.add(buildThongKeChart());

        return col;
    }

    // Phòng card with 3 room types
    private JPanel buildPhongCard() {
        JPanel card = card();
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(16, 18, 16, 18));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));

        JLabel title = lbl("Phòng", F_TITLE, DARK);
        card.add(title, BorderLayout.NORTH);

        JPanel rooms = new JPanel(new GridLayout(1, 3, 12, 0));
        rooms.setOpaque(false);
        rooms.setBorder(new EmptyBorder(10, 0, 0, 0));

        rooms.add(roomCard("VIP",     "2/30",  "$ 568",  "/ngày", new Color(0x22C55E)));
        rooms.add(roomCard("Thường",  "2/35",  "$ 1,068","/ngày", new Color(0x3B6FF0)));
        rooms.add(roomCardRevenue());
        card.add(rooms, BorderLayout.CENTER);
        return card;
    }

    private JPanel roomCard(String type, String count, String price, String unit, Color tagColor) {
        JPanel p = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0xF8F9FE));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(BORDER);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);
                g2.dispose();
            }
        };
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(10, 12, 10, 12));

        // Tag
        JLabel tag = new JLabel("2 Deals") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(tagColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        tag.setFont(new Font("Segoe UI", Font.BOLD, 10));
        tag.setForeground(WHITE);
        tag.setOpaque(false);
        tag.setBorder(new EmptyBorder(2, 8, 2, 8));
        tag.setAlignmentX(LEFT_ALIGNMENT);

        JLabel typeLbl = lbl(type, F_BOLD12, MID);
        typeLbl.setAlignmentX(LEFT_ALIGNMENT);

        JLabel countLbl = lbl(count, F_NUM, DARK);
        countLbl.setFont(new Font("Segoe UI", Font.BOLD, 22));
        countLbl.setAlignmentX(LEFT_ALIGNMENT);

        JPanel priceRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        priceRow.setOpaque(false);
        priceRow.setAlignmentX(LEFT_ALIGNMENT);
        JLabel priceLbl = lbl(price, F_BOLD12, BLUE);
        JLabel unitLbl = lbl(unit, F_LABEL, GRAY);
        priceRow.add(priceLbl); priceRow.add(unitLbl);

        p.add(tag);
        p.add(Box.createVerticalStrut(6));
        p.add(typeLbl);
        p.add(countLbl);
        p.add(priceRow);
        return p;
    }

    private JPanel roomCardRevenue() {
        JPanel p = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0xF8F9FE));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(BORDER);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);
                g2.dispose();
            }
        };
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(10, 12, 10, 12));

        JLabel tag = new JLabel("2 Deals") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0xF59E0B));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose(); super.paintComponent(g);
            }
        };
        tag.setFont(new Font("Segoe UI", Font.BOLD, 10));
        tag.setForeground(WHITE); tag.setOpaque(false);
        tag.setBorder(new EmptyBorder(2, 8, 2, 8)); tag.setAlignmentX(LEFT_ALIGNMENT);

        JLabel typeLbl = lbl("Tổng tiền ca", F_BOLD12, MID); typeLbl.setAlignmentX(LEFT_ALIGNMENT);
        JLabel revLbl = lbl("1.200.000 vnd", new Font("Segoe UI", Font.BOLD, 16), BLUE);
        revLbl.setAlignmentX(LEFT_ALIGNMENT);

        p.add(tag); p.add(Box.createVerticalStrut(6));
        p.add(typeLbl); p.add(Box.createVerticalStrut(6));
        p.add(revLbl);
        return p;
    }

    // Trạng thái phòng card
    private JPanel buildTrangThaiCard() {
        JPanel card = card();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(16, 18, 16, 18));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

        JLabel title = lbl("Trạng thái phòng", F_TITLE, DARK);
        title.setAlignmentX(LEFT_ALIGNMENT);
        card.add(title);
        card.add(Box.createVerticalStrut(12));

        JPanel row1 = new JPanel(new GridLayout(1, 2));
        row1.setOpaque(false);
        row1.setAlignmentX(LEFT_ALIGNMENT);
        row1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        row1.add(statusItem("Phòng đang ở", "90"));
        row1.add(statusItem("Phòng trống", "10"));
        card.add(row1);
        card.add(Box.createVerticalStrut(8));

        JPanel row2 = new JPanel(new GridLayout(1, 2));
        row2.setOpaque(false);
        row2.setAlignmentX(LEFT_ALIGNMENT);
        row2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        row2.add(statusItem("Phòng đã đặt", "30"));
        row2.add(new JPanel() {{ setOpaque(false); }});
        card.add(row2);

        return card;
    }

    private JPanel statusItem(String label, String value) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        p.setOpaque(false);
        p.add(lbl(label, F_SMALL, MID));
        p.add(lbl(value, F_BOLD13, DARK));
        return p;
    }

    // Thống kê mini bar chart
    private JPanel buildThongKeChart() {
        JPanel card = card();
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(16, 18, 16, 18));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.add(lbl("Thống kê", F_TITLE, DARK), BorderLayout.WEST);
        JButton monthBtn = pillBtn("📅  Tháng");
        header.add(monthBtn, BorderLayout.EAST);
        card.add(header, BorderLayout.NORTH);

        int[] vals = {82, 68, 55, 40, 78, 60, 92, 85, 88, 75};
        String[] months = {"May","Jun","Jul","Aug","Sep","Oct","Nov","Dec","Jan","Feb"};
        card.add(new MiniBarChart(vals, months), BorderLayout.CENTER);
        return card;
    }

    // ── Right column: Thông tin nhân viên + Phản hồi khách hàng ─────────────
    private JPanel buildRightColumn() {
        JPanel col = new JPanel();
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
        col.setOpaque(false);

        col.add(buildNhanVienCard());
        col.add(Box.createVerticalStrut(14));
        col.add(buildPhanHoiCard());
        col.add(Box.createVerticalGlue());

        return col;
    }

    private JPanel buildNhanVienCard() {
        JPanel card = card();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(16, 18, 16, 18));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 310));

        // Header
        JLabel title = lbl("Thông tin nhân viên", F_TITLE, DARK);
        title.setAlignmentX(LEFT_ALIGNMENT);
        card.add(title);
        card.add(Box.createVerticalStrut(4));

        // Employee name + email
        JLabel name = lbl("Nguyen Hoang", F_BOLD13, DARK);
        name.setAlignmentX(LEFT_ALIGNMENT);
        JLabel email = lbl("yourname@gmail.com", F_SMALL, GRAY);
        email.setAlignmentX(LEFT_ALIGNMENT);
        card.add(name); card.add(email);
        card.add(Box.createVerticalStrut(14));

        // Divider
        JSeparator sep = new JSeparator();
        sep.setForeground(BORDER);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        card.add(sep);
        card.add(Box.createVerticalStrut(10));

        // Info rows
        card.add(infoRow("Họ và tên",    "Nguyen Hoang"));
        card.add(Box.createVerticalStrut(10));
        card.add(sep()); card.add(Box.createVerticalStrut(10));
        card.add(infoRow("Email",         "yourname@gmail.com"));
        card.add(Box.createVerticalStrut(10));
        card.add(sep()); card.add(Box.createVerticalStrut(10));
        card.add(infoRow("Số điện thoại", "0123456789"));
        card.add(Box.createVerticalStrut(10));
        card.add(sep()); card.add(Box.createVerticalStrut(10));
        card.add(infoRow("Chức vụ",       "Nhân Viên"));

        return card;
    }

    private JPanel infoRow(String key, String value) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));
        p.setAlignmentX(LEFT_ALIGNMENT);
        p.add(lbl(key, F_SMALL, GRAY), BorderLayout.WEST);
        p.add(lbl(value, F_SMALL, DARK), BorderLayout.EAST);
        return p;
    }

    private JSeparator sep() {
        JSeparator s = new JSeparator();
        s.setForeground(BORDER);
        s.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        return s;
    }

    private JPanel buildPhanHoiCard() {
        JPanel card = card();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(16, 18, 16, 18));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));

        JPanel headerRow = new JPanel(new BorderLayout());
        headerRow.setOpaque(false);
        headerRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        headerRow.setAlignmentX(LEFT_ALIGNMENT);
        headerRow.add(lbl("Phản hồi khách hàng", F_TITLE, DARK), BorderLayout.WEST);
        JLabel dots = lbl("⋯", new Font("Segoe UI", Font.BOLD, 18), GRAY);
        headerRow.add(dots, BorderLayout.EAST);
        card.add(headerRow);
        card.add(Box.createVerticalStrut(12));

        // Feedback entries
        String[][] feedbacks = {
                {"Mark",      "A201", "Đồ ăn có thể tốt hơn."},
                {"Christian", "A101", "Cơ sở vật chất không tương xứng với số tiền đã trả."},
                {"Alexander", "A301", "Việc dọn phòng có thể tốt hơn."},
        };

        for (String[] fb : feedbacks) {
            card.add(feedbackItem(fb[0], fb[1], fb[2]));
            card.add(Box.createVerticalStrut(10));
        }
        return card;
    }

    private JPanel feedbackItem(String guestName, String room, String comment) {
        JPanel p = new JPanel(new BorderLayout(8, 0));
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        p.setAlignmentX(LEFT_ALIGNMENT);

        // Name + comment on left
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);
        JLabel nameLbl = lbl(guestName, F_BOLD12, DARK);
        nameLbl.setAlignmentX(LEFT_ALIGNMENT);
        JLabel commentLbl = lbl(comment, F_LABEL, GRAY);
        commentLbl.setAlignmentX(LEFT_ALIGNMENT);
        left.add(nameLbl);
        left.add(commentLbl);

        // Room tag on right
        JLabel roomLbl = new JLabel(room) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0xF0F4FF));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose(); super.paintComponent(g);
            }
        };
        roomLbl.setFont(F_BOLD12);
        roomLbl.setForeground(BLUE);
        roomLbl.setOpaque(false);
        roomLbl.setBorder(new EmptyBorder(3, 8, 3, 8));

        p.add(left, BorderLayout.CENTER);
        p.add(roomLbl, BorderLayout.EAST);
        return p;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    static JPanel card() {
        JPanel p = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.setColor(new Color(0xE8EEF8));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 14, 14);
                g2.dispose();
            }
        };
        p.setOpaque(false);
        p.setAlignmentX(LEFT_ALIGNMENT);
        return p;
    }

    static JLabel lbl(String text, Font f, Color c) {
        JLabel l = new JLabel(text); l.setFont(f); l.setForeground(c); return l;
    }

    static JButton pillBtn(String text) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(new Color(0xDDE3EF));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
                g2.dispose(); super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btn.setForeground(new Color(0x4A5268));
        btn.setOpaque(false); btn.setContentAreaFilled(false); btn.setBorderPainted(false);
        btn.setBorder(new EmptyBorder(5, 14, 5, 14));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}

// ── Mini Bar Chart ────────────────────────────────────────────────────────────
class MiniBarChart extends JPanel {
    int[] vals; String[] labels;
    MiniBarChart(int[] vals, String[] labels) {
        this.vals = vals; this.labels = labels; setOpaque(false);
    }
    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth(), h = getHeight();
        int padL = 36, padR = 8, padT = 8, padB = 26;
        int cw = w - padL - padR, ch = h - padT - padB;

        // Y grid
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        int[] yp = {0, 25, 50, 75, 100};
        for (int p : yp) {
            int y = padT + ch - (int)(ch * p / 100.0);
            g2.setColor(new Color(0xEEF0F6));
            g2.setStroke(new BasicStroke(1));
            g2.drawLine(padL, y, w - padR, y);
            g2.setColor(new Color(0xA0A8B8));
            g2.drawString(p + "%", 0, y + 4);
        }

        // Bars
        int n = vals.length, barW = (int)(cw / (n * 1.6f));
        int gap = (cw - barW * n) / (n + 1);
        for (int i = 0; i < n; i++) {
            int bh = (int)(ch * vals[i] / 100.0);
            int bx = padL + gap + i * (barW + gap);
            int by = padT + ch - bh;
            g2.setColor(new Color(0x3B6FF0));
            g2.fill(new RoundRectangle2D.Float(bx, by, barW, bh, 5, 5));
            g2.setColor(new Color(0xA0A8B8));
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 9));
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(labels[i], bx + barW/2 - fm.stringWidth(labels[i])/2, h - padB + 14);
        }
        g2.dispose();
    }
}