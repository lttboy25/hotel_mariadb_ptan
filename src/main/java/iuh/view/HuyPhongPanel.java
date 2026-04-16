package iuh.view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;

public class HuyPhongPanel extends JPanel {

    // --- HỆ THỐNG MÀU SẮC & FONT ---
    static final Color BG        = new Color(0xF8FAFC);
    static final Color WHITE     = Color.WHITE;
    static final Color PRIMARY   = new Color(0x3B82F6);
    static final Color TEXT_D    = new Color(0x1E293B);
    static final Color TEXT_M    = new Color(0x64748B);
    static final Color BORDER    = new Color(0xE2E8F0);
    static final Color INPUT_BG  = new Color(0xF1F5F9);
    static final Color TEAL      = new Color(0x0EA47A);
    static final Color TEAL_L    = new Color(0xE6FAF5);

    static final Font F_TITLE    = new Font("Segoe UI", Font.BOLD, 16);
    static final Font F_BOLD13   = new Font("Segoe UI", Font.BOLD, 13);
    static final Font F_PLAIN    = new Font("Segoe UI", Font.PLAIN, 13);
    static final Font F_SMALL    = new Font("Segoe UI", Font.PLAIN, 11);

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
    private JPanel summaryContentPanel;
    private int selectedIndex = 1;

    public HuyPhongPanel() {
        setLayout(new BorderLayout());
        setBackground(BG);
        initData();

        add(buildHeader(), BorderLayout.NORTH);

        JPanel mainContent = buildBody();
        JScrollPane scroll = new JScrollPane(mainContent);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getViewport().setBackground(BG);
        add(scroll, BorderLayout.CENTER);
    }

    private void initData() {
        bookings.add(new Booking("Phòng thường", "10/10/2025", "2 ngày 1 đêm", "2 người lớn", "2.067.000VND", "PT"));
        bookings.add(new Booking("Phòng VIP", "12/10/2025", "2 ngày 2 đêm", "4 người lớn", "4.133.000VND", "VIP"));
        if (bookings.size() > selectedIndex) bookings.get(selectedIndex).selected = true;
    }

    private JPanel buildHeader() {
        JPanel h = new JPanel(new BorderLayout());
        h.setBackground(WHITE);
        h.setPreferredSize(new Dimension(0, 65));
        h.setBorder(new MatteBorder(0, 0, 1, 0, BORDER));

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

        // Sử dụng CustomIcon nội bộ thay vì TrangChuPanel.CustomIcon
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
                g2.setPaint(new GradientPaint(0, 0, new Color(0x60A5FA), 0, getHeight(), PRIMARY));
                g2.fillOval(0, 0, 38, 38);
                g2.setColor(WHITE); g2.setFont(F_BOLD13);
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

    private JPanel buildBody() {
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(BG);
        body.setBorder(new EmptyBorder(25, 30, 25, 30));

        JPanel searchBar = card();
        searchBar.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));
        JTextField txtCCCD = new JTextField("Nhập CCCD");
        txtCCCD.setPreferredSize(new Dimension(300, 40));
        txtCCCD.setBorder(new CompoundBorder(new LineBorder(BORDER, 1, true), new EmptyBorder(0, 12, 0, 12)));
        searchBar.add(txtCCCD);
        searchBar.add(styledBtn("Tìm kiếm", PRIMARY));

        body.add(searchBar);
        body.add(Box.createVerticalStrut(20));
        body.add(buildBookingListCard());
        body.add(Box.createVerticalStrut(20));

        JPanel bottomRow = new JPanel(new GridBagLayout());
        bottomRow.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // Cột trái
        gbc.gridx = 0; gbc.weightx = 0.7;
        JPanel reasonCard = card();
        reasonCard.setLayout(new BorderLayout(0, 10));
        reasonCard.setBorder(new EmptyBorder(20, 20, 20, 20));
        reasonCard.add(lbl("Lý do hủy phòng", F_BOLD13, TEXT_D), BorderLayout.NORTH);
        JTextArea area = new JTextArea("Lý do hủy phòng");
        area.setBorder(new LineBorder(BORDER, 1, true));
        reasonCard.add(new JScrollPane(area), BorderLayout.CENTER);
        bottomRow.add(reasonCard, gbc);

        // Cột phải
        gbc.gridx = 1; gbc.weightx = 0.3; gbc.insets = new Insets(0, 20, 0, 0);
        bottomRow.add(buildFixedSummaryCard(), gbc);

        body.add(bottomRow);
        return body;
    }

    private JPanel buildBookingListCard() {
        JPanel card = card();
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(20, 25, 20, 25));
        card.add(lbl("Danh sách phòng đã đặt", F_TITLE, TEXT_D), BorderLayout.NORTH);

        bookingListPanel = new JPanel();
        bookingListPanel.setLayout(new BoxLayout(bookingListPanel, BoxLayout.Y_AXIS));
        bookingListPanel.setOpaque(false);
        bookingListPanel.setBorder(new EmptyBorder(15, 0, 0, 0));

        refreshBookingList();
        card.add(bookingListPanel, BorderLayout.CENTER);
        return card;
    }

    private JPanel buildFixedSummaryCard() {
        Dimension fixedDim = new Dimension(120, 260);

        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(WHITE); g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(TEAL); g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 16, 16);
                g2.dispose();
            }
        };
        card.setLayout(new BorderLayout());
        card.setOpaque(false);
        card.setPreferredSize(fixedDim);
        card.setMinimumSize(fixedDim);
        card.setMaximumSize(fixedDim);

        JPanel inner = new JPanel();
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setOpaque(false);
        inner.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Nhãn tiêu đề căn giữa
        JLabel tag = lbl("Tiền hoàn trả", F_BOLD13, TEAL);
        tag.setOpaque(true); tag.setBackground(TEAL_L);
        tag.setBorder(new EmptyBorder(5, 12, 5, 12));
        tag.setAlignmentX(Component.CENTER_ALIGNMENT); // CĂN GIỮA

        inner.add(tag);
        inner.add(Box.createVerticalStrut(20));

        summaryContentPanel = new JPanel();
        summaryContentPanel.setLayout(new BoxLayout(summaryContentPanel, BoxLayout.Y_AXIS));
        summaryContentPanel.setOpaque(false);
        summaryContentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        updateSummaryData();
        inner.add(summaryContentPanel);
        inner.add(Box.createVerticalGlue());

        JButton btn = styledBtn("Hủy ngay", PRIMARY);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        inner.add(btn);

        card.add(inner);
        return card;
    }

    private void updateSummaryData() {
        if (summaryContentPanel == null) return;
        summaryContentPanel.removeAll();

        String tong = selectedIndex == 0 ? "2.067.000đ" : "6.200.000đ";
        String hoan = selectedIndex == 0 ? "1.000.000đ" : "3.000.000đ";

        summaryContentPanel.add(row("Tổng tiền phòng", tong));
        summaryContentPanel.add(Box.createVerticalStrut(10));
        summaryContentPanel.add(row("Cọc (30%)", "30%"));
        summaryContentPanel.add(Box.createVerticalStrut(15));

        JSeparator s = new JSeparator(); s.setForeground(BORDER);
        summaryContentPanel.add(s);
        summaryContentPanel.add(Box.createVerticalStrut(15));

        summaryContentPanel.add(rowBold("Total price", hoan));

        summaryContentPanel.revalidate();
        summaryContentPanel.repaint();
    }

    private JPanel row(String k, String v) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.add(lbl(k, F_PLAIN, TEXT_M), BorderLayout.WEST);
        p.add(lbl(v, F_PLAIN, TEXT_D), BorderLayout.EAST);
        return p;
    }

    private JPanel rowBold(String k, String v) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.add(lbl(k, F_BOLD13, TEXT_D), BorderLayout.WEST);
        p.add(lbl(v, F_BOLD13, TEXT_D), BorderLayout.EAST);
        return p;
    }

    private void refreshBookingList() {
        bookingListPanel.removeAll();
        for (int i = 0; i < bookings.size(); i++) {
            Booking b = bookings.get(i);
            int idx = i;
            JPanel item = new JPanel(new BorderLayout(20, 0));
            item.setOpaque(false);

            JPanel img = new JPanel(); img.setPreferredSize(new Dimension(80, 80));
            img.setBackground(new Color(0xCBD5E1));

            JPanel info = new JPanel(new GridLayout(2, 1));
            info.setOpaque(false);
            info.add(lbl(b.type, F_BOLD13, TEXT_D));
            info.add(lbl(b.price, F_BOLD13, PRIMARY));

            JRadioButton rb = new JRadioButton();
            rb.setSelected(b.selected);
            rb.setOpaque(false);
            rb.addActionListener(e -> {
                for (Booking x : bookings) x.selected = false;
                b.selected = true;
                selectedIndex = idx;
                refreshBookingList();
                updateSummaryData();
            });

            item.add(img, BorderLayout.WEST);
            item.add(info, BorderLayout.CENTER);
            item.add(rb, BorderLayout.EAST);
            bookingListPanel.add(item);
            bookingListPanel.add(Box.createVerticalStrut(15));
        }
        bookingListPanel.revalidate(); bookingListPanel.repaint();
    }

    static JPanel card() {
        JPanel p = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(WHITE); g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(BORDER); g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 16, 16);
                g2.dispose();
            }
        };
        p.setOpaque(false); return p;
    }

    static JLabel lbl(String t, Font f, Color c) {
        JLabel l = new JLabel(t); l.setFont(f); l.setForeground(c); return l;
    }

    static JButton styledBtn(String text, Color bg) {
        JButton b = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg); g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g); g2.dispose();
            }
        };
        b.setFont(F_BOLD13); b.setForeground(WHITE);
        b.setOpaque(false); b.setContentAreaFilled(false); b.setBorderPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    // --- CLASS CUSTOM ICON ĐƯỢC THÊM VÀO ĐỂ FIX LỖI ---
    static class CustomIcon implements Icon {
        private String type;
        public CustomIcon(String type) { this.type = type; }
        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(TEXT_M);
            if ("search".equals(type)) {
                g2.drawOval(x, y, 12, 12);
                g2.drawLine(x + 10, y + 10, x + 15, y + 15);
            } else if ("bell".equals(type)) {
                g2.drawRoundRect(x, y, 14, 14, 4, 4);
                g2.fillOval(x + 5, y + 15, 4, 2);
            }
            g2.dispose();
        }
        @Override public int getIconWidth() { return 20; }
        @Override public int getIconHeight() { return 20; }
    }
}