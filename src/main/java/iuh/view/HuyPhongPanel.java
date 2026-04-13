package iuh.view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;

public class HuyPhongPanel extends JPanel {

    static final Color BG      = new Color(0xF4F6FB);
    static final Color WHITE   = Color.WHITE;
    static final Color BLUE    = new Color(0x3B6FF0);
    static final Color BLUE_L  = new Color(0xEBF0FF);
    static final Color GREEN   = new Color(0x22C55E);
    static final Color GREEN_L = new Color(0xDCFCE7);
    static final Color DARK    = new Color(0x1A1A2E);
    static final Color MID     = new Color(0x4A5268);
    static final Color GRAY    = new Color(0xA0A8B8);
    static final Color BORDER  = new Color(0xE4E9F2);
    static final Color RED     = new Color(0xE04040);
    static final Color RED_L   = new Color(0xFFF0F0);
    static final Color TEAL_L  = new Color(0xE6FAF5);
    static final Color TEAL    = new Color(0x0EA47A);

    static final Font F_TITLE  = new Font("Segoe UI", Font.BOLD, 16);
    static final Font F_BOLD14 = new Font("Segoe UI", Font.BOLD, 14);
    static final Font F_BOLD13 = new Font("Segoe UI", Font.BOLD, 13);
    static final Font F_BOLD12 = new Font("Segoe UI", Font.BOLD, 12);
    static final Font F_PLAIN13= new Font("Segoe UI", Font.PLAIN, 13);
    static final Font F_PLAIN12= new Font("Segoe UI", Font.PLAIN, 12);
    static final Font F_SMALL  = new Font("Segoe UI", Font.PLAIN, 11);
    static final Font F_LABEL  = new Font("Segoe UI", Font.PLAIN, 11);

    // Room booking model
    static class Booking {
        String type, checkIn, duration, guests, price, imageLabel;
        boolean selected;
        Booking(String type, String checkIn, String duration, String guests, String price, String imageLabel) {
            this.type = type; this.checkIn = checkIn; this.duration = duration;
            this.guests = guests; this.price = price; this.imageLabel = imageLabel;
        }
    }

    private final List<Booking> bookings = new ArrayList<>();
    private JPanel bookingListPanel;
    private JPanel summaryPanel;
    private JTextArea reasonArea;
    private int selectedIndex = 1; // default: second selected

    public HuyPhongPanel() {
        setLayout(new BorderLayout());
        setBackground(BG);
        initData();

        add(buildHeader(), BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(buildBody());
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(12);
        scroll.setBackground(BG);
        scroll.getViewport().setBackground(BG);
        add(scroll, BorderLayout.CENTER);
    }

    private void initData() {
        bookings.add(new Booking("Phòng thường", "10/10/2025", "2 ngày 1 đêm",  "2 người lớn", "2.067.000VND", "PT"));
        bookings.add(new Booking("Phòng VIP",    "12/10/2025", "2ngày 2 đêm",   "4 người lớn", "4.133.000VND", "VIP"));
        bookings.get(selectedIndex).selected = true;
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
                g2.drawString("N", 17 - fm.stringWidth("N")/2, 17 + fm.getAscent()/2 - 2);
                g2.dispose();
            }
        };
        avatar.setOpaque(false);
        avatar.setPreferredSize(new Dimension(34, 34));
        right.add(avatar);
        h.add(right, BorderLayout.EAST);
        return h;
    }

    // ── Body ──────────────────────────────────────────────────────────────────
    private JPanel buildBody() {
        JPanel body = new JPanel(new GridBagLayout());
        body.setBackground(BG);
        body.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1.0;
        gc.gridx = 0;

        // Search CCCD bar
        gc.gridy = 0; gc.weighty = 0;
        body.add(buildSearchBar(), gc);

        gc.gridy = 1;
        body.add(Box.createVerticalStrut(14), gc);

        // Main content row: booking list + bottom section
        gc.gridy = 2; gc.weighty = 0; gc.fill = GridBagConstraints.BOTH;
        body.add(buildMainContent(), gc);

        gc.gridy = 3; gc.weighty = 0; gc.fill = GridBagConstraints.HORIZONTAL;
        body.add(Box.createVerticalStrut(20), gc);

        return body;
    }

    // ── Search bar ────────────────────────────────────────────────────────────
    private JPanel buildSearchBar() {
        JPanel card = card();
        card.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 14));

        // CCCD input field
        JTextField cccdField = new JTextField("Nhập CCCD") {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() || getText().equals("Nhập CCCD")) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(GRAY);
                    g2.setFont(F_PLAIN13);
                    Insets ins = getInsets();
                    if (getText().equals("Nhập CCCD"))
                        g2.drawString("Nhập CCCD", ins.left + 2, getHeight()/2 + g2.getFontMetrics().getAscent()/2 - 2);
                    g2.dispose();
                }
            }
        };
        cccdField.setFont(F_PLAIN13);
        cccdField.setForeground(GRAY);
        cccdField.setPreferredSize(new Dimension(240, 38));
        cccdField.setBorder(new CompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(6, 12, 6, 12)
        ));
        cccdField.setBackground(WHITE);
        cccdField.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                if (cccdField.getText().equals("Nhập CCCD")) { cccdField.setText(""); cccdField.setForeground(DARK); }
                cccdField.setBorder(new CompoundBorder(new LineBorder(BLUE, 2, true), new EmptyBorder(5, 11, 5, 11)));
            }
            @Override public void focusLost(FocusEvent e) {
                if (cccdField.getText().isEmpty()) { cccdField.setText("Nhập CCCD"); cccdField.setForeground(GRAY); }
                cccdField.setBorder(new CompoundBorder(new LineBorder(BORDER, 1, true), new EmptyBorder(6, 12, 6, 12)));
            }
        });
        card.add(cccdField);

        JButton searchBtn = blueBtn("Tìm kiếm", 110, 38);
        card.add(searchBtn);

        return card;
    }

    // ── Main content: booking list on top, reason+summary below ──────────────
    private JPanel buildMainContent() {
        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setOpaque(false);

        // Booking list card
        main.add(buildBookingListCard());
        main.add(Box.createVerticalStrut(14));

        // Bottom row: reason textarea + summary card
        JPanel bottomRow = new JPanel(new GridBagLayout());
        bottomRow.setOpaque(false);
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.BOTH;
        gc.weighty = 1; gc.gridy = 0;

        gc.gridx = 0; gc.weightx = 0.72; gc.insets = new Insets(0, 0, 0, 0);
        bottomRow.add(buildReasonCard(), gc);

        gc.gridx = 1; gc.weightx = 0.28; gc.insets = new Insets(0, 14, 0, 0);
        summaryPanel = buildSummaryCard();
        bottomRow.add(summaryPanel, gc);

        bottomRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        main.add(bottomRow);

        return main;
    }

    // ── Booking list card ─────────────────────────────────────────────────────
    private JPanel buildBookingListCard() {
        JPanel card = card();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(18, 20, 18, 20));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 10000));

        JLabel title = lbl("Danh sách phòng đã đặt", F_TITLE, DARK);
        title.setAlignmentX(LEFT_ALIGNMENT);
        title.setBorder(new EmptyBorder(0, 0, 14, 0));
        card.add(title);

        bookingListPanel = new JPanel();
        bookingListPanel.setLayout(new BoxLayout(bookingListPanel, BoxLayout.Y_AXIS));
        bookingListPanel.setOpaque(false);
        bookingListPanel.setAlignmentX(LEFT_ALIGNMENT);

        for (int i = 0; i < bookings.size(); i++) {
            final int idx = i;
            bookingListPanel.add(buildBookingItem(bookings.get(i), idx));
            if (i < bookings.size() - 1) {
                bookingListPanel.add(Box.createVerticalStrut(12));
                JSeparator sep = new JSeparator();
                sep.setForeground(BORDER);
                sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
                bookingListPanel.add(sep);
                bookingListPanel.add(Box.createVerticalStrut(12));
            }
        }

        card.add(bookingListPanel);
        return card;
    }

    private JPanel buildBookingItem(Booking b, int idx) {
        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        row.setAlignmentX(LEFT_ALIGNMENT);

        // Room image placeholder
        JPanel img = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0xCCD0DA));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                // Room type label
                g2.setColor(new Color(0x8890A4));
                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                FontMetrics fm = g2.getFontMetrics();
                String lbl2 = b.imageLabel;
                g2.drawString(lbl2, getWidth()/2 - fm.stringWidth(lbl2)/2,
                        getHeight()/2 + fm.getAscent()/2 - 2);
                g2.dispose();
            }
        };
        img.setPreferredSize(new Dimension(88, 88));
        img.setMinimumSize(new Dimension(88, 88));
        img.setMaximumSize(new Dimension(88, 88));
        row.add(img, BorderLayout.WEST);

        // Info panel
        JPanel info = new JPanel(new GridBagLayout());
        info.setOpaque(false);
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1; gc.gridy = 0; gc.gridx = 0;
        gc.anchor = GridBagConstraints.WEST;

        // Room type name
        gc.gridy = 0;
        info.add(lbl(b.type, F_BOLD14, DARK), gc);

        // Details row
        gc.gridy = 1; gc.insets = new Insets(6, 0, 0, 0);
        JPanel details = new JPanel(new FlowLayout(FlowLayout.LEFT, 28, 0));
        details.setOpaque(false);
        details.add(detailItem("Nhận phòng:", b.checkIn));
        details.add(detailItem("Thời gian:", b.duration));
        details.add(detailItem("Số lượng khách:", b.guests));
        info.add(details, gc);

        // Price
        gc.gridy = 2; gc.insets = new Insets(6, 0, 0, 0);
        info.add(lbl(b.price, F_BOLD13, DARK), gc);

        row.add(info, BorderLayout.CENTER);

        // Radio button on the right
        JPanel radioWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        radioWrapper.setOpaque(false);
        radioWrapper.setPreferredSize(new Dimension(40, 88));

        JPanel radioBtn = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int cx = getWidth()/2, cy = getHeight()/2, r = 9;
                g2.setColor(WHITE);
                g2.fillOval(cx-r, cy-r, r*2, r*2);
                g2.setColor(b.selected ? BLUE : BORDER);
                g2.setStroke(new BasicStroke(b.selected ? 2f : 1.5f));
                g2.drawOval(cx-r, cy-r, r*2, r*2);
                if (b.selected) {
                    g2.setColor(BLUE);
                    g2.fillOval(cx-5, cy-5, 10, 10);
                }
                g2.dispose();
            }
        };
        radioBtn.setOpaque(false);
        radioBtn.setPreferredSize(new Dimension(24, 88));
        radioBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        radioBtn.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                // Deselect all, select this
                for (Booking bo : bookings) bo.selected = false;
                b.selected = true;
                selectedIndex = idx;
                refreshBookingList();
                refreshSummary();
            }
        });
        radioWrapper.add(radioBtn);
        row.add(radioWrapper, BorderLayout.EAST);

        return row;
    }

    private JPanel detailItem(String key, String value) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        p.setOpaque(false);
        p.add(lbl(key, F_SMALL, GRAY));
        p.add(lbl(value, F_SMALL, MID));
        return p;
    }

    private void refreshBookingList() {
        bookingListPanel.removeAll();
        for (int i = 0; i < bookings.size(); i++) {
            final int idx = i;
            bookingListPanel.add(buildBookingItem(bookings.get(i), idx));
            if (i < bookings.size() - 1) {
                bookingListPanel.add(Box.createVerticalStrut(12));
                JSeparator sep = new JSeparator();
                sep.setForeground(BORDER);
                sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
                bookingListPanel.add(sep);
                bookingListPanel.add(Box.createVerticalStrut(12));
            }
        }
        bookingListPanel.revalidate();
        bookingListPanel.repaint();
    }

    // ── Reason textarea ───────────────────────────────────────────────────────
    private JPanel buildReasonCard() {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.setColor(BORDER);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 14, 14);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(0, 180));

        reasonArea = new JTextArea("Lý do hủy phòng");
        reasonArea.setFont(F_PLAIN13);
        reasonArea.setForeground(GRAY);
        reasonArea.setLineWrap(true);
        reasonArea.setWrapStyleWord(true);
        reasonArea.setOpaque(false);
        reasonArea.setBorder(new EmptyBorder(16, 16, 16, 16));
        reasonArea.setBackground(WHITE);

        reasonArea.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                if (reasonArea.getText().equals("Lý do hủy phòng")) {
                    reasonArea.setText(""); reasonArea.setForeground(DARK);
                }
            }
            @Override public void focusLost(FocusEvent e) {
                if (reasonArea.getText().trim().isEmpty()) {
                    reasonArea.setText("Lý do hủy phòng"); reasonArea.setForeground(GRAY);
                }
            }
        });

        // Wrap in opaque white panel so rounded corners work
        JPanel inner = new JPanel(new BorderLayout());
        inner.setBackground(WHITE);
        inner.add(reasonArea, BorderLayout.CENTER);

        // Use a custom clip panel
        JScrollPane scroll = new JScrollPane(inner);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        card.add(scroll, BorderLayout.CENTER);

        return card;
    }

    // ── Summary card ──────────────────────────────────────────────────────────
    private JPanel buildSummaryCard() {
        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                // Teal border
                g2.setColor(TEAL);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 14, 14);
                g2.dispose();
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(14, 16, 14, 16));
        card.setPreferredSize(new Dimension(0, 180));

        refreshSummaryContent(card);
        return card;
    }

    private void refreshSummaryContent(JPanel card) {
        card.removeAll();

        Booking sel = selectedIndex >= 0 && selectedIndex < bookings.size()
                ? bookings.get(selectedIndex) : null;

        String total = sel != null ? sel.price : "0VND";
        // Parse price for calculation (rough)
        int totalVal = 6200000;
        String cocPct = "30%";
        String totalPrice = "3.000.000VND";

        // Header label with teal bg
        JLabel header = new JLabel("Tiền hoàn trả") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(TEAL_L);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose(); super.paintComponent(g);
            }
        };
        header.setFont(F_BOLD12);
        header.setForeground(TEAL);
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(4, 10, 4, 10));
        header.setAlignmentX(LEFT_ALIGNMENT);
        card.add(header);
        card.add(Box.createVerticalStrut(12));

        card.add(summaryRow("Tổng tiền phòng", "6.200.000VND"));
        card.add(Box.createVerticalStrut(8));
        card.add(summaryRow("Cọc", cocPct));
        card.add(Box.createVerticalStrut(8));

        // Divider
        JSeparator sep = new JSeparator();
        sep.setForeground(BORDER);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setAlignmentX(LEFT_ALIGNMENT);
        card.add(sep);
        card.add(Box.createVerticalStrut(8));

        card.add(summaryRowBold("Total price", totalPrice));
        card.add(Box.createVerticalGlue());

        // Hủy ngay button
        card.add(Box.createVerticalStrut(12));
        JButton huyBtn = new JButton("Hủy ngay") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color c = getModel().isRollover() ? new Color(0x2A5CD4) : BLUE;
                g2.setColor(c);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(WHITE); g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth()-fm.stringWidth(getText()))/2,
                        (getHeight()+fm.getAscent()-fm.getDescent())/2);
                g2.dispose();
            }
        };
        huyBtn.setOpaque(false); huyBtn.setContentAreaFilled(false);
        huyBtn.setBorderPainted(false); huyBtn.setForeground(WHITE);
        huyBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        huyBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        huyBtn.setPreferredSize(new Dimension(0, 44));
        huyBtn.setAlignmentX(LEFT_ALIGNMENT);
        huyBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        huyBtn.addActionListener(e -> {
            String reason = reasonArea.getText().trim();
            if (reason.isEmpty() || reason.equals("Lý do hủy phòng")) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập lý do hủy phòng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (selectedIndex < 0 || selectedIndex >= bookings.size()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn phòng cần hủy!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Xác nhận hủy phòng " + bookings.get(selectedIndex).type + "?",
                    "Xác nhận hủy", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                bookings.remove(selectedIndex);
                selectedIndex = bookings.isEmpty() ? -1 : 0;
                if (!bookings.isEmpty()) bookings.get(0).selected = true;
                refreshBookingList();
                refreshSummary();
                reasonArea.setText("Lý do hủy phòng");
                reasonArea.setForeground(GRAY);
                JOptionPane.showMessageDialog(this, "Hủy phòng thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        card.add(huyBtn);

        card.revalidate();
        card.repaint();
    }

    private void refreshSummary() {
        refreshSummaryContent(summaryPanel);
    }

    private JPanel summaryRow(String key, String value) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));
        p.setAlignmentX(LEFT_ALIGNMENT);
        p.add(lbl(key, F_PLAIN12, MID), BorderLayout.WEST);
        p.add(lbl(value, F_PLAIN12, DARK), BorderLayout.EAST);
        return p;
    }

    private JPanel summaryRowBold(String key, String value) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));
        p.setAlignmentX(LEFT_ALIGNMENT);
        p.add(lbl(key, F_BOLD13, DARK), BorderLayout.WEST);
        p.add(lbl(value, F_BOLD13, DARK), BorderLayout.EAST);
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