package iuh.view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;

public class DoiPhongPanel extends JPanel {

    static final Color BG      = new Color(0xF4F6FB);
    static final Color WHITE   = Color.WHITE;
    static final Color BLUE    = new Color(0x3B6FF0);
    static final Color BLUE_L  = new Color(0xEBF0FF);
    static final Color DARK    = new Color(0x1A1A2E);
    static final Color MID     = new Color(0x4A5268);
    static final Color GRAY    = new Color(0xA0A8B8);
    static final Color BORDER  = new Color(0xE4E9F2);
    static final Color SEL_BG  = new Color(0xEEF4FF);
    static final Color SEL_BD  = new Color(0x3B6FF0);
    static final Color TEAL    = new Color(0x0EA47A);
    static final Color TEAL_L  = new Color(0xE6FAF5);
    static final Color RED     = new Color(0xE04040);
    static final Color RED_L   = new Color(0xFFF0F0);

    static final Font F_PAGE   = new Font("Segoe UI", Font.BOLD, 18);
    static final Font F_TITLE  = new Font("Segoe UI", Font.BOLD, 15);
    static final Font F_BOLD14 = new Font("Segoe UI", Font.BOLD, 14);
    static final Font F_BOLD13 = new Font("Segoe UI", Font.BOLD, 13);
    static final Font F_BOLD12 = new Font("Segoe UI", Font.BOLD, 12);
    static final Font F_PLAIN13= new Font("Segoe UI", Font.PLAIN, 13);
    static final Font F_PLAIN12= new Font("Segoe UI", Font.PLAIN, 12);
    static final Font F_SMALL  = new Font("Segoe UI", Font.PLAIN, 11);
    static final Font F_COL    = new Font("Segoe UI", Font.PLAIN, 11);

    static class Room {
        String id, type, floor, oldPrice, newPrice;
        Room(String id, String type, String floor, String oldPrice, String newPrice) {
            this.id = id; this.type = type; this.floor = floor;
            this.oldPrice = oldPrice; this.newPrice = newPrice;
        }
    }

    // Rooms currently booked (left table)
    private final List<Room> bookedRooms = new ArrayList<>();
    // Available rooms to swap into (right/bottom table)
    private final List<Room> availRooms  = new ArrayList<>();

    private int selectedBooked = 1;  // #002 selected by default
    private int selectedAvail  = 0;  // #005 selected by default

    private JPanel bookedTableBody;
    private JPanel availTableBody;
    private JPanel summaryPanel;

    public DoiPhongPanel() {
        setLayout(new BorderLayout());
        setBackground(BG);
        initData();

        add(buildHeader(), BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                buildLeft(), buildRight());
        split.setDividerLocation(560);
        split.setDividerSize(0);
        split.setBorder(null);
        split.setBackground(BG);
        split.setContinuousLayout(true);
        add(split, BorderLayout.CENTER);
    }

    private void initData() {
        bookedRooms.add(new Room("#001", "VIP",           "Tầng 1", "300.000VND", "5.000.000VND"));
        bookedRooms.add(new Room("#002", "Phòng thường",  "Tầng 2", "400.000VND", "7.000.000VND"));
        bookedRooms.add(new Room("#003", "VIP",           "Tầng 1", "700.000VND", "8000.000VND"));

        availRooms.add(new Room("#005", "Phòng thường",   "Floor -1","350.000VND","5.000.000VND"));
        availRooms.add(new Room("#006", "VIP",            "Tầng 3",  "500.000VND","6.500.000VND"));
        availRooms.add(new Room("#007", "Phòng thường",   "Tầng 4",  "320.000VND","4.800.000VND"));
    }

    // ── Header ────────────────────────────────────────────────────────────────
    private JPanel buildHeader() {
        JPanel h = new JPanel(new BorderLayout());
        h.setBackground(WHITE);
        h.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 1, 0, BORDER),
                new EmptyBorder(10, 24, 10, 24)));
        h.setPreferredSize(new Dimension(0, 54));

        JPanel search = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0xF4F6FB));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(BORDER);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
                g2.dispose();
            }
        };
        search.setOpaque(false);
        search.setPreferredSize(new Dimension(240, 34));
        search.add(lbl("🔍", new Font("Segoe UI", Font.PLAIN, 13), GRAY));
        search.add(lbl("Search for rooms and offers", F_PLAIN12, GRAY));
        h.add(search, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        right.setOpaque(false);
        right.add(lbl("🔔", new Font("Segoe UI", Font.PLAIN, 18), GRAY));
        JPanel avatar = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0x6B9EFF));
                g2.fillOval(0, 0, 34, 34);
                g2.setColor(WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString("N", 17-fm.stringWidth("N")/2, 17+fm.getAscent()/2-2);
                g2.dispose();
            }
        };
        avatar.setOpaque(false);
        avatar.setPreferredSize(new Dimension(34, 34));
        right.add(avatar);
        h.add(right, BorderLayout.EAST);
        return h;
    }

    // =========================================================================
    //  LEFT PANEL
    // =========================================================================
    private JPanel buildLeft() {
        JPanel left = new JPanel(new GridBagLayout());
        left.setBackground(BG);
        left.setBorder(new EmptyBorder(20, 20, 20, 10));

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1.0; gc.gridx = 0;

        // Title
        gc.gridy = 0; gc.weighty = 0;
        JLabel title1 = lbl("Chọn phòng cần đổi", F_PAGE, DARK);
        left.add(title1, gc);

        gc.gridy = 1;
        left.add(Box.createVerticalStrut(14), gc);

        // Search + button
        gc.gridy = 2;
        left.add(buildSearchBar(), gc);

        gc.gridy = 3;
        left.add(Box.createVerticalStrut(10), gc);

        // Booked rooms table
        gc.gridy = 4; gc.weighty = 0;
        left.add(buildBookedTable(), gc);

        gc.gridy = 5;
        left.add(Box.createVerticalStrut(22), gc);

        // Title 2
        gc.gridy = 6;
        JPanel title2Row = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        title2Row.setOpaque(false);
        title2Row.add(lbl("Chọn phòng muốn đổi", F_PAGE, DARK));
        title2Row.add(Box.createHorizontalStrut(14));
        title2Row.add(buildChonBtn());
        left.add(title2Row, gc);

        gc.gridy = 7;
        left.add(Box.createVerticalStrut(10), gc);

        // Available rooms table
        gc.gridy = 8; gc.weighty = 0;
        left.add(buildAvailTable(), gc);

        // Push to top
        gc.gridy = 9; gc.weighty = 1.0;
        left.add(Box.createVerticalGlue(), gc);

        return left;
    }

    private JPanel buildSearchBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        bar.setOpaque(false);

        // Search input
        JPanel sf = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(BORDER);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
                g2.dispose();
            }
        };
        sf.setOpaque(false);
        sf.setPreferredSize(new Dimension(200, 36));
        sf.add(lbl("🔍", new Font("Segoe UI", Font.PLAIN, 11), GRAY));
        JTextField tf = new JTextField("083205000961");
        tf.setFont(F_PLAIN12); tf.setForeground(DARK);
        tf.setBorder(null); tf.setOpaque(false);
        tf.setPreferredSize(new Dimension(150, 22));
        sf.add(tf);
        bar.add(sf);

        bar.add(blueBtn("Tìm kiếm", 100, 36));
        return bar;
    }

    private JButton buildChonBtn() {
        JButton btn = new JButton("Chọn") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BLUE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(WHITE); g2.setFont(F_BOLD12);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth()-fm.stringWidth(getText()))/2,
                        (getHeight()+fm.getAscent()-fm.getDescent())/2);
                g2.dispose();
            }
        };
        btn.setOpaque(false); btn.setContentAreaFilled(false);
        btn.setBorderPainted(false); btn.setFont(F_BOLD12);
        btn.setPreferredSize(new Dimension(72, 30));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> {
            if (selectedAvail < 0 || selectedAvail >= availRooms.size()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn phòng muốn đổi!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            refreshSummary();
        });
        return btn;
    }

    // ── Booked rooms table ────────────────────────────────────────────────────
    private JPanel buildBookedTable() {
        JPanel wrapper = tableWrapper();

        // Column headers
        String[] cols = {"Số phòng", "Loại phòng", "Tầng", "Giá theo ngày", "Giá theo ngày"};
        wrapper.add(buildColHeader(cols), BorderLayout.NORTH);

        bookedTableBody = new JPanel();
        bookedTableBody.setLayout(new BoxLayout(bookedTableBody, BoxLayout.Y_AXIS));
        bookedTableBody.setBackground(WHITE);
        refreshBookedTable();
        wrapper.add(bookedTableBody, BorderLayout.CENTER);
        return wrapper;
    }

    private void refreshBookedTable() {
        bookedTableBody.removeAll();
        for (int i = 0; i < bookedRooms.size(); i++) {
            final int idx = i;
            Room r = bookedRooms.get(i);
            boolean sel = (i == selectedBooked);
            JPanel row = buildTableRow(r, sel, () -> {
                selectedBooked = idx;
                refreshBookedTable();
                refreshSummary();
            });
            bookedTableBody.add(row);
        }
        bookedTableBody.revalidate();
        bookedTableBody.repaint();
    }

    // ── Available rooms table ─────────────────────────────────────────────────
    private JPanel buildAvailTable() {
        JPanel wrapper = tableWrapper();

        // No header for avail table — just rows
        availTableBody = new JPanel();
        availTableBody.setLayout(new BoxLayout(availTableBody, BoxLayout.Y_AXIS));
        availTableBody.setBackground(WHITE);
        refreshAvailTable();
        wrapper.add(availTableBody, BorderLayout.CENTER);
        return wrapper;
    }

    private void refreshAvailTable() {
        availTableBody.removeAll();
        for (int i = 0; i < availRooms.size(); i++) {
            final int idx = i;
            Room r = availRooms.get(i);
            boolean sel = (i == selectedAvail);
            JPanel row = buildTableRow(r, sel, () -> {
                selectedAvail = idx;
                refreshAvailTable();
                refreshSummary();
            });
            availTableBody.add(row);
        }
        availTableBody.revalidate();
        availTableBody.repaint();
    }

    private JPanel buildTableRow(Room r, boolean selected, Runnable onClick) {
        JPanel row = new JPanel(new GridLayout(1, 5, 0, 0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(selected ? SEL_BG : WHITE);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        row.setOpaque(false);
        row.setPreferredSize(new Dimension(0, 46));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));

        // Selected row gets blue left border
        if (selected) {
            row.setBorder(new CompoundBorder(
                    new MatteBorder(0, 0, 1, 0, BORDER),
                    new MatteBorder(0, 3, 0, 0, SEL_BD)
            ));
        } else {
            row.setBorder(new MatteBorder(0, 0, 1, 0, BORDER));
        }

        row.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        row.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { onClick.run(); }
            @Override public void mouseEntered(MouseEvent e) {
                if (!selected) { row.setBackground(new Color(0xF8FAFF)); row.repaint(); }
            }
            @Override public void mouseExited(MouseEvent e) {
                if (!selected) { row.setBackground(WHITE); row.repaint(); }
            }
        });

        String[] vals = {r.id, r.type, r.floor, r.oldPrice, r.newPrice};
        for (int c = 0; c < 5; c++) {
            JPanel cell = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
            cell.setOpaque(false);
            Color fg = selected ? BLUE : (c == 0 ? DARK : MID);
            Font f   = selected && c == 0 ? F_BOLD13 : F_PLAIN13;
            cell.add(lbl(vals[c], f, fg));
            row.add(cell);
        }
        return row;
    }

    // =========================================================================
    //  RIGHT PANEL — summary
    // =========================================================================
    private JPanel buildRight() {
        JPanel right = new JPanel(new GridBagLayout());
        right.setBackground(BG);
        right.setBorder(new EmptyBorder(20, 10, 20, 20));

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1.0; gc.gridx = 0;

        // Phòng ban đầu card
        gc.gridy = 0; gc.weighty = 0;
        right.add(buildOriginalCard(), gc);

        gc.gridy = 1;
        right.add(Box.createVerticalStrut(12), gc);

        // Phòng đổi sang card
        gc.gridy = 2;
        right.add(buildNewCard(), gc);

        gc.gridy = 3;
        right.add(Box.createVerticalStrut(12), gc);

        // Phí chênh lệch card
        gc.gridy = 4;
        summaryPanel = buildFeeCard();
        right.add(summaryPanel, gc);

        gc.gridy = 5;
        right.add(Box.createVerticalStrut(14), gc);

        // Xác nhận button
        gc.gridy = 6;
        right.add(buildConfirmBtn(), gc);

        // Push up
        gc.gridy = 7; gc.weighty = 1.0;
        right.add(Box.createVerticalGlue(), gc);

        return right;
    }

    private JPanel buildOriginalCard() {
        return buildInfoCard("Phòng ban đầu", TEAL, TEAL_L, true);
    }

    private JPanel buildNewCard() {
        return buildInfoCard("Phòng đổi sang", TEAL, TEAL_L, false);
    }

    private JPanel buildInfoCard(String headerText, Color borderColor, Color headerBg, boolean isOriginal) {
        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(borderColor);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 12, 12);
                g2.dispose();
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(0, 0, 0, 0));

        // Header
        JPanel header = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(headerBg);
                // Round only top corners
                g2.fillRoundRect(0, 0, getWidth(), getHeight() + 10, 12, 12);
                g2.dispose();
            }
        };
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(8, 14, 8, 14));
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        JLabel hdrLbl = lbl(headerText, F_BOLD13, borderColor);
        header.add(hdrLbl, BorderLayout.WEST);
        card.add(header);

        // Body
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setOpaque(false);
        body.setBorder(new EmptyBorder(10, 14, 14, 14));
        body.setAlignmentX(LEFT_ALIGNMENT);

        Room r = isOriginal
                ? (selectedBooked < bookedRooms.size() ? bookedRooms.get(selectedBooked) : null)
                : (selectedAvail  < availRooms.size()  ? availRooms.get(selectedAvail)   : null);

        String id    = r != null ? r.id    : "—";
        String type  = r != null ? r.type  : "—";
        String price = r != null ? r.newPrice + "/ngày" : "—";

        body.add(infoRow("Số phòng",  id));
        body.add(Box.createVerticalStrut(8));
        body.add(sep());
        body.add(Box.createVerticalStrut(8));
        body.add(infoRow("Loại phòng", type));
        body.add(Box.createVerticalStrut(8));
        body.add(sep());
        body.add(Box.createVerticalStrut(8));
        body.add(infoRow("Giá", price));

        card.add(body);
        return card;
    }

    private JPanel buildFeeCard() {
        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(RED);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 12, 12);
                g2.dispose();
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);

        // Header
        JPanel header = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(RED_L);
                g2.fillRoundRect(0, 0, getWidth(), getHeight() + 10, 12, 12);
                g2.dispose();
            }
        };
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(8, 14, 8, 14));
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        header.add(lbl("Phí chênh lệch", F_BOLD13, RED), BorderLayout.WEST);
        card.add(header);

        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setOpaque(false);
        body.setBorder(new EmptyBorder(10, 14, 14, 14));
        body.setAlignmentX(LEFT_ALIGNMENT);

        body.add(infoRow("Tổng tiền phòng", "6.200.000VND"));
        body.add(Box.createVerticalStrut(8));
        body.add(infoRow("Cọc", "30%"));
        body.add(Box.createVerticalStrut(8));
        body.add(infoRow("Tổng tiền cọc", "3.000.000VND"));
        card.add(body);
        return card;
    }

    private JPanel buildConfirmBtn() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        JButton btn = new JButton("Xác nhận") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color c = getModel().isRollover() ? new Color(0x2A5CD4) : BLUE;
                g2.setColor(c);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 15));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth()-fm.stringWidth(getText()))/2,
                        (getHeight()+fm.getAscent()-fm.getDescent())/2);
                g2.dispose();
            }
        };
        btn.setOpaque(false); btn.setContentAreaFilled(false);
        btn.setBorderPainted(false); btn.setForeground(WHITE);
        btn.setPreferredSize(new Dimension(0, 48));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> {
            if (selectedBooked < 0 || selectedAvail < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn đủ hai phòng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Room from = bookedRooms.get(selectedBooked);
            Room to   = availRooms.get(selectedAvail);
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Xác nhận đổi phòng " + from.id + " → " + to.id + "?",
                    "Xác nhận đổi phòng", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(this,
                        "Đổi phòng thành công!\n" + from.id + " (" + from.type + ") → " + to.id + " (" + to.type + ")",
                        "Thành công", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        wrapper.add(btn, BorderLayout.CENTER);
        return wrapper;
    }

    private void refreshSummary() {
        // Rebuild right panel info cards by updating parent container
        Container parent = summaryPanel.getParent();
        if (parent == null) return;
        // Rebuild all cards inline — remove and re-add to right panel
        JPanel right = (JPanel) parent;
        right.removeAll();

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1.0; gc.gridx = 0;

        gc.gridy = 0; right.add(buildInfoCard("Phòng ban đầu",  TEAL, TEAL_L, true),  gc);
        gc.gridy = 1; right.add(Box.createVerticalStrut(12), gc);
        gc.gridy = 2; right.add(buildInfoCard("Phòng đổi sang", TEAL, TEAL_L, false), gc);
        gc.gridy = 3; right.add(Box.createVerticalStrut(12), gc);
        gc.gridy = 4;
        summaryPanel = buildFeeCard();
        right.add(summaryPanel, gc);
        gc.gridy = 5; right.add(Box.createVerticalStrut(14), gc);
        gc.gridy = 6; right.add(buildConfirmBtn(), gc);
        gc.gridy = 7; gc.weighty = 1.0;
        right.add(Box.createVerticalGlue(), gc);

        right.revalidate();
        right.repaint();
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    private JPanel tableWrapper() {
        JPanel p = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(BORDER);
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 12, 12);
                g2.dispose();
            }
        };
        p.setOpaque(false);
        return p;
    }

    private JPanel buildColHeader(String[] cols) {
        JPanel h = new JPanel(new GridLayout(1, cols.length));
        h.setBackground(new Color(0xF8F9FE));
        h.setPreferredSize(new Dimension(0, 36));
        h.setBorder(new MatteBorder(0, 0, 1, 0, BORDER));
        for (String c : cols) {
            JLabel l = lbl(c, F_COL, GRAY);
            l.setBorder(new EmptyBorder(0, 14, 0, 0));
            h.add(l);
        }
        return h;
    }

    private JPanel infoRow(String key, String value) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        p.setAlignmentX(LEFT_ALIGNMENT);
        p.add(lbl(key, F_PLAIN12, GRAY), BorderLayout.WEST);
        p.add(lbl(value, F_PLAIN12, DARK), BorderLayout.EAST);
        return p;
    }

    private JSeparator sep() {
        JSeparator s = new JSeparator();
        s.setForeground(BORDER);
        s.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        return s;
    }

    static JLabel lbl(String t, Font f, Color c) {
        JLabel l = new JLabel(t); l.setFont(f); l.setForeground(c); return l;
    }

    static JButton blueBtn(String text, int w, int h) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color col = getModel().isRollover() ? new Color(0x2A5CD4) : BLUE;
                g2.setColor(col);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(WHITE); g2.setFont(F_BOLD12);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth()-fm.stringWidth(getText()))/2,
                        (getHeight()+fm.getAscent()-fm.getDescent())/2);
                g2.dispose();
            }
        };
        btn.setOpaque(false); btn.setContentAreaFilled(false);
        btn.setBorderPainted(false); btn.setForeground(WHITE);
        btn.setFont(F_BOLD12); btn.setPreferredSize(new Dimension(w, h));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}