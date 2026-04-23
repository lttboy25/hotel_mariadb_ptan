package iuh.view;

import iuh.dao.DatPhongDao;
import iuh.entity.Phong;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

public class DatPhongPanel extends JPanel {

    static final Color BG       = new Color(0xF4F6FB);
    static final Color WHITE    = Color.WHITE;
    static final Color BLUE     = new Color(0x3B6FF0);
    static final Color BLUE_L   = new Color(0xDCEAFF);
    static final Color GREEN    = new Color(0x22C55E);
    static final Color GREEN_L  = new Color(0xDCFCE7);
    static final Color DARK     = new Color(0x1A1A2E);
    static final Color MID      = new Color(0x4A5268);
    static final Color GRAY     = new Color(0xA0A8B8);
    static final Color BORDER   = new Color(0xE4E9F2);
    static final Color SEL_ROW  = new Color(0xF0F5FF);

    static final Font F_TITLE  = new Font("Segoe UI", Font.BOLD, 15);
    static final Font F_BOLD13 = new Font("Segoe UI", Font.BOLD, 13);
    static final Font F_BOLD12 = new Font("Segoe UI", Font.BOLD, 12);
    static final Font F_PLAIN13= new Font("Segoe UI", Font.PLAIN, 13);
    static final Font F_PLAIN12= new Font("Segoe UI", Font.PLAIN, 12);
    static final Font F_SMALL  = new Font("Segoe UI", Font.PLAIN, 11);
    static final Font F_LABEL  = new Font("Segoe UI", Font.PLAIN, 11);

    // Room data model
    static class Room {
        String id, type, floor;
        boolean available;
        int maxAdults;
        int maxChildren;
        Room(String id, String type, String floor, boolean available, int maxAdults, int maxChildren) {
            this.id = id;
            this.type = type;
            this.floor = floor;
            this.available = available;
            this.maxAdults = maxAdults;
            this.maxChildren = maxChildren;
        }
    }

    private final List<Room> allRooms = new ArrayList<>();
    private final List<Room> selectedRooms = new ArrayList<>();
    private JPanel roomTableBody;
    private JPanel bookingList;
    private JTextField searchField;
    private JLabel allRoomTabLabel;
    private JButton searchButton;
    private JLabel suggestionLabel;
    private DateTimePickerField checkInPicker;
    private DateTimePickerField checkOutPicker;
    private JSpinner spAdults;
    private JSpinner spChildren;
    private String filterType = "Loại phòng";
    private String filterFloor = "Tầng";
    private String activeTab = "All room(0)";

    public DatPhongPanel() {
        setLayout(new BorderLayout());
        setBackground(BG);
        loadAvailableRooms();

        // Header
        add(buildHeader(), BorderLayout.NORTH);

        // Split: left search/table | right booking summary
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, buildLeft(), buildRight());
        split.setDividerLocation(620);
        split.setDividerSize(0);
        split.setBorder(null);
        split.setBackground(BG);
        split.setContinuousLayout(true);
        add(split, BorderLayout.CENTER);
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
                g2.setColor(WHITE); g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString("N", 17 - fm.stringWidth("N")/2, 17 + fm.getAscent()/2 - 2);
                g2.dispose();
            }
        };
        avatar.setOpaque(false); avatar.setPreferredSize(new Dimension(34, 34));
        right.add(avatar);
        h.add(right, BorderLayout.EAST);
        return h;
    }

    // ── LEFT PANEL ────────────────────────────────────────────────────────────
    private JPanel buildLeft() {
        JPanel left = new JPanel(new BorderLayout());
        left.setBackground(BG);
        left.setBorder(new EmptyBorder(16, 20, 16, 10));

        // Top: tabs + date pickers
        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setOpaque(false);

        top.add(buildTabsAndDates());
        top.add(Box.createVerticalStrut(14));
        top.add(buildFilterBar());
        top.add(Box.createVerticalStrut(10));

        left.add(top, BorderLayout.NORTH);

        // Room table
        JScrollPane scroll = new JScrollPane(buildRoomTable());
        scroll.setBorder(null);
        scroll.setBackground(WHITE);
        scroll.getViewport().setBackground(WHITE);
        scroll.getVerticalScrollBar().setUnitIncrement(10);
        left.add(scroll, BorderLayout.CENTER);

        return left;
    }

    private JPanel buildTabsAndDates() {
        JPanel wrapper = card();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBorder(new EmptyBorder(16, 18, 16, 18));

        // Tabs row
        JPanel tabRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        tabRow.setOpaque(false);
        tabRow.setAlignmentX(LEFT_ALIGNMENT);

        String[] tabs = {activeTab, "VIP"};
        for (String tab : tabs) {
            JPanel tabBtn = tabButton(tab, tab.equals(activeTab));
            if (tab.equals(activeTab)) {
                allRoomTabLabel = (JLabel) tabBtn.getComponent(0);
            }
            tabRow.add(tabBtn);
        }
        tabRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        wrapper.add(tabRow);
        wrapper.add(Box.createVerticalStrut(14));

        // Date row
        JPanel dateRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        dateRow.setOpaque(false);
        dateRow.setAlignmentX(LEFT_ALIGNMENT);
        dateRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        checkInPicker = new DateTimePickerField(LocalDateTime.now().plusHours(1));
        checkOutPicker = new DateTimePickerField(LocalDateTime.now().plusDays(1));

        dateRow.add(buildDateField("Check in", checkInPicker));
        dateRow.add(buildDateField("Check out", checkOutPicker));

        JPanel guestRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        guestRow.setOpaque(false);
        guestRow.setAlignmentX(LEFT_ALIGNMENT);
        guestRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        guestRow.add(buildGuestField("Người lớn", true));
        guestRow.add(buildGuestField("Trẻ em", false));

        wrapper.add(dateRow);
        wrapper.add(Box.createVerticalStrut(8));
        wrapper.add(guestRow);
        wrapper.add(Box.createVerticalStrut(12));

        // Search button
        searchButton = blueBtn("Tìm kiếm", 110, 38);
        searchButton.setAlignmentX(LEFT_ALIGNMENT);
        searchButton.addActionListener(e -> applyRoomFilters());
        wrapper.add(searchButton);
        wrapper.add(Box.createVerticalStrut(8));

        suggestionLabel = lbl("Nhập yêu cầu và nhấn Tìm kiếm để gợi ý phòng phù hợp.", F_PLAIN12, BLUE);
        suggestionLabel.setAlignmentX(LEFT_ALIGNMENT);
        wrapper.add(suggestionLabel);

        return wrapper;
    }

    private JPanel tabButton(String text, boolean active) {
        JPanel btn = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (active) {
                    g2.setColor(WHITE);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                    g2.setColor(BLUE);
                    g2.setStroke(new BasicStroke(1.5f));
                    g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
                } else {
                    g2.setColor(WHITE);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                    g2.setColor(BORDER);
                    g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
                }
                g2.dispose();
            }
        };
        btn.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btn.setOpaque(false);
        btn.setPreferredSize(new Dimension(100, 30));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        JLabel lbl = lbl(text, F_PLAIN12, active ? BLUE : MID);
        btn.add(lbl);
        return btn;
    }

    private JPanel buildDateField(String label, DateTimePickerField picker) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        JLabel lbl = lbl(label, F_LABEL, GRAY);
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        p.add(lbl);
        p.add(Box.createVerticalStrut(4));

        picker.setAlignmentX(LEFT_ALIGNMENT);
        picker.setPreferredSize(new Dimension(260, 38));
        picker.setMaximumSize(new Dimension(260, 38));
        p.add(picker);
        return p;
    }

    private JPanel buildGuestField(String label, boolean adult) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);

        JLabel lbl = lbl(label, F_LABEL, GRAY);
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        p.add(lbl);
        p.add(Box.createVerticalStrut(4));

        JSpinner spinner = new JSpinner(new SpinnerNumberModel(adult ? 1 : 0, 0, 20, 1));
        spinner.setFont(F_PLAIN12);
        spinner.setPreferredSize(new Dimension(84, 36));
        spinner.setMaximumSize(new Dimension(84, 36));
        spinner.setBorder(new CompoundBorder(new LineBorder(BORDER, 1, true), new EmptyBorder(2, 8, 2, 8)));

        if (adult) {
            spAdults = spinner;
        } else {
            spChildren = spinner;
        }

        p.add(spinner);
        p.setAlignmentX(LEFT_ALIGNMENT);
        return p;
    }

    private void loadAvailableRooms() {
        try {
            List<Phong> phongs = new DatPhongDao().getDsPhongTrong();
            allRooms.clear();
            for (Phong phong : phongs) {
                allRooms.add(toRoom(phong));
            }
            activeTab = "All room(" + allRooms.size() + ")";
        } catch (Exception e) {
            allRooms.clear();
            activeTab = "All room(0)";
            JOptionPane.showMessageDialog(this,
                    "Không thể tải danh sách phòng trống: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Room toRoom(Phong phong) {
        String id = phong.getMaPhong();
        String type = phong.getLoaiPhong() == null || phong.getLoaiPhong().getTenLoaiPhong() == null
                ? "Chưa xác định"
                : phong.getLoaiPhong().getTenLoaiPhong();
        String floor = phong.getTang() <= 0 ? "" : "Tầng " + phong.getTang();
        boolean available = "Trống".equalsIgnoreCase(phong.getTinhTrang());
        int maxAdults = phong.getLoaiPhong() == null ? 0 : phong.getLoaiPhong().getSoNguoiLonToiDa();
        int maxChildren = phong.getLoaiPhong() == null ? 0 : phong.getLoaiPhong().getSoTreEmToiDa();
        return new Room(id, type, floor, available, maxAdults, maxChildren);
    }

    private JPanel buildFilterBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        bar.setOpaque(false);
        bar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        bar.setAlignmentX(LEFT_ALIGNMENT);

        // Search field
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
        sf.setPreferredSize(new Dimension(160, 34));
        sf.add(lbl("🔍", new Font("Segoe UI", Font.PLAIN, 11), GRAY));
        searchField = new JTextField("Số phòng");
        searchField.setFont(F_PLAIN12);
        searchField.setForeground(GRAY);
        searchField.setBorder(null);
        searchField.setOpaque(false);
        searchField.setPreferredSize(new Dimension(110, 22));
        searchField.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Số phòng")) { searchField.setText(""); searchField.setForeground(DARK); }
            }
            @Override public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) { searchField.setText("Số phòng"); searchField.setForeground(GRAY); }
            }
        });
        sf.add(searchField);
        bar.add(sf);

        // Dropdowns
        bar.add(dropDown(true, "Loại phòng ▾", new String[]{"Loại phòng","Phòng thường","Phòng Đôi Deluxe","Phòng Gia Đình Suite","VIP"}));
        bar.add(dropDown(false, "Tầng ▾", new String[]{"Tầng","Tầng 1","Tầng 2","Tầng 3","Tầng 4","Tầng 5"}));

        return bar;
    }

    private JPanel dropDown(boolean isTypeFilter, String defaultText, String[] options) {
        JPanel btn = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0)) {
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
        btn.setOpaque(false);
        btn.setPreferredSize(new Dimension(120, 34));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        JLabel lbl = lbl(defaultText, F_PLAIN12, MID);
        btn.add(lbl);
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                JPopupMenu menu = new JPopupMenu();
                menu.setBorder(new LineBorder(BORDER, 1, true));
                for (String opt : options) {
                    JMenuItem item = new JMenuItem(opt);
                    item.setFont(F_PLAIN12);
                    item.setForeground(DARK);
                    item.addActionListener(ae -> {
                        lbl.setText(opt + " ▾");
                        if (isTypeFilter) {
                            filterType = opt;
                        } else {
                            filterFloor = opt;
                        }
                    });
                    menu.add(item);
                }
                menu.show(btn, 0, btn.getHeight());
            }
        });
        return btn;
    }

    // Room table
    private JPanel buildRoomTable() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(WHITE);
        wrapper.setBorder(new CompoundBorder(
                new MatteBorder(1, 1, 1, 1, BORDER),
                new EmptyBorder(0, 0, 0, 0)
        ));

        // Table header
        JPanel header = new JPanel(new GridLayout(1, 5));
        header.setBackground(new Color(0xF8F9FE));
        header.setPreferredSize(new Dimension(0, 38));
        header.setBorder(new MatteBorder(0, 0, 1, 0, BORDER));

        String[] cols = {"Trạng thái", "", "Số Phòng", "Loại Phòng", "Tầng"};
        for (String col : cols) {
            JLabel h = lbl(col, F_BOLD12, GRAY);
            h.setBorder(new EmptyBorder(0, 12, 0, 0));
            header.add(h);
        }
        wrapper.add(header, BorderLayout.NORTH);

        roomTableBody = new JPanel();
        roomTableBody.setLayout(new BoxLayout(roomTableBody, BoxLayout.Y_AXIS));
        roomTableBody.setBackground(WHITE);
        refreshTable(allRooms);
        wrapper.add(roomTableBody, BorderLayout.CENTER);

        return wrapper;
    }

    private void refreshTable(List<Room> rooms) {
        roomTableBody.removeAll();
        for (Room room : rooms) {
            roomTableBody.add(buildRoomRow(room));
        }
        roomTableBody.add(Box.createVerticalGlue());
        roomTableBody.revalidate();
        roomTableBody.repaint();
    }

    private void applyRoomFilters() {
        LocalDateTime checkIn = checkInPicker == null ? null : checkInPicker.getValue();
        LocalDateTime checkOut = checkOutPicker == null ? null : checkOutPicker.getValue();
        if (checkIn != null && checkOut != null && !checkOut.isAfter(checkIn)) {
            JOptionPane.showMessageDialog(this,
                    "Check-out phải sau Check-in.",
                    "Dữ liệu chưa hợp lệ",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int requestedAdults = spAdults == null ? 1 : (int) spAdults.getValue();
        int requestedChildren = spChildren == null ? 0 : (int) spChildren.getValue();

        String keyword = normalizePlaceholder(searchField == null ? "" : searchField.getText(), "Số phòng");
        String type = normalizePlaceholder(filterType, "Loại phòng");
        String floor = normalizePlaceholder(filterFloor, "Tầng");

        List<Room> filtered = new ArrayList<>();
        for (Room room : allRooms) {
            if (!matchesKeyword(room, keyword)) {
                continue;
            }
            if (!matchesType(room, type)) {
                continue;
            }
            if (!matchesFloor(room, floor)) {
                continue;
            }
            if (!matchesCapacity(room, requestedAdults, requestedChildren)) {
                continue;
            }
            filtered.add(room);
        }

        filtered.sort(Comparator
                .comparingInt((Room r) -> capacityGapScore(r, requestedAdults, requestedChildren))
                .thenComparing(r -> r.id));

        refreshTable(filtered);
        activeTab = "All room(" + filtered.size() + ")";
        if (allRoomTabLabel != null) {
            allRoomTabLabel.setText(activeTab);
        }

        if (filtered.isEmpty()) {
            suggestionLabel.setText("Không có phòng phù hợp với yêu cầu hiện tại.");
        } else {
            Room best = filtered.get(0);
            suggestionLabel.setText("Gợi ý tốt nhất: " + best.id + " - " + best.type
                    + " (" + best.maxAdults + " NL, " + best.maxChildren + " TE)");
        }
        revalidate();
        repaint();
    }

    private String normalizePlaceholder(String value, String placeholder) {
        if (value == null) {
            return "";
        }
        String trimmed = value.trim();
        return placeholder.equals(trimmed) ? "" : trimmed;
    }

    private boolean matchesKeyword(Room room, String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return true;
        }
        String k = keyword.toLowerCase();
        return (room.id != null && room.id.toLowerCase().contains(k))
                || (room.type != null && room.type.toLowerCase().contains(k))
                || (room.floor != null && room.floor.toLowerCase().contains(k));
    }

    private boolean matchesType(Room room, String type) {
        if (type == null || type.isBlank()) {
            return true;
        }
        if (type.equalsIgnoreCase("Phòng thường") || type.equalsIgnoreCase("VIP")) {
            return room.type != null && room.type.toLowerCase().contains(type.toLowerCase());
        }
        return room.type != null && room.type.equalsIgnoreCase(type);
    }

    private boolean matchesFloor(Room room, String floor) {
        if (floor == null || floor.isBlank()) {
            return true;
        }
        if (floor.startsWith("Tầng ")) {
            return floor.equalsIgnoreCase(room.floor);
        }
        return room.floor != null && room.floor.equalsIgnoreCase(floor);
    }

    private boolean matchesCapacity(Room room, int adults, int children) {
        return room.maxAdults >= adults && room.maxChildren >= children;
    }

    private int capacityGapScore(Room room, int adults, int children) {
        int adultGap = room.maxAdults - adults;
        int childGap = room.maxChildren - children;
        return Math.max(0, adultGap) + Math.max(0, childGap);
    }

    private JPanel buildRoomRow(Room room) {
        boolean isSelected = selectedRooms.contains(room);
        JPanel row = new JPanel(new GridLayout(1, 5)) {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(isSelected ? SEL_ROW : WHITE);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        row.setOpaque(false);
        row.setPreferredSize(new Dimension(0, 44));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        row.setBorder(new MatteBorder(0, 0, 1, 0, BORDER));

        // Status badge
        JPanel statusCell = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        statusCell.setOpaque(false);
        JLabel statusBadge = new JLabel(room.available ? "Có sẵn" : "Đang ở") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(room.available ? GREEN_L : new Color(0xFFEEEE));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose(); super.paintComponent(g);
            }
        };
        statusBadge.setFont(F_LABEL);
        statusBadge.setForeground(room.available ? GREEN : new Color(0xE04040));
        statusBadge.setOpaque(false);
        statusBadge.setBorder(new EmptyBorder(3, 8, 3, 8));
        statusCell.add(statusBadge);

        // Checkbox
        JPanel cbCell = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        cbCell.setOpaque(false);
        JCheckBox cb = new JCheckBox();
        cb.setOpaque(false);
        cb.setSelected(isSelected);
        cb.addActionListener(e -> {
            if (cb.isSelected()) {
                if (!selectedRooms.contains(room)) selectedRooms.add(room);
            } else {
                selectedRooms.remove(room);
            }
            refreshBookingList();
            row.repaint();
        });
        cbCell.add(cb);

        // Room number
        JPanel numCell = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        numCell.setOpaque(false);
        numCell.add(lbl(room.id, F_BOLD13, DARK));

        // Type
        JPanel typeCell = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        typeCell.setOpaque(false);
        typeCell.add(lbl(room.type + " (" + room.maxAdults + " NL, " + room.maxChildren + " TE)", F_PLAIN13, MID));

        // Floor
        JPanel floorCell = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        floorCell.setOpaque(false);
        floorCell.add(lbl(room.floor, F_PLAIN13, MID));

        row.add(statusCell);
        row.add(cbCell);
        row.add(numCell);
        row.add(typeCell);
        row.add(floorCell);
        return row;
    }

    // ── RIGHT PANEL ───────────────────────────────────────────────────────────
    private JPanel buildRight() {
        JPanel right = new JPanel(new BorderLayout());
        right.setBackground(BG);
        right.setBorder(new EmptyBorder(16, 10, 16, 20));

        JPanel inner = new JPanel(new BorderLayout());
        inner.setBackground(WHITE);
        inner.setBorder(new CompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(0, 0, 0, 0)
        ));

        // Table header
        JPanel colHeader = new JPanel(new GridLayout(1, 4));
        colHeader.setBackground(new Color(0xF8F9FE));
        colHeader.setPreferredSize(new Dimension(0, 38));
        colHeader.setBorder(new MatteBorder(0, 0, 1, 0, BORDER));
        String[] cols = {"Số phòng", "Check-in", "Check-out", "Thời gian"};
        for (String c : cols) {
            JLabel cl = lbl(c, F_BOLD12, GRAY);
            cl.setBorder(new EmptyBorder(0, 12, 0, 0));
            colHeader.add(cl);
        }
        inner.add(colHeader, BorderLayout.NORTH);

        // Booking list (scrollable)
        bookingList = new JPanel();
        bookingList.setLayout(new BoxLayout(bookingList, BoxLayout.Y_AXIS));
        bookingList.setBackground(WHITE);

        JScrollPane scroll = new JScrollPane(bookingList);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(10);
        inner.add(scroll, BorderLayout.CENTER);

        // Confirm button
        JButton confirmBtn = new JButton("Xác nhận") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BLUE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth()-fm.stringWidth(getText()))/2,
                        (getHeight()+fm.getAscent()-fm.getDescent())/2);
                g2.dispose();
            }
        };
        confirmBtn.setOpaque(false); confirmBtn.setContentAreaFilled(false);
        confirmBtn.setBorderPainted(false); confirmBtn.setForeground(WHITE);
        confirmBtn.setPreferredSize(new Dimension(0, 48));
        confirmBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        confirmBtn.addActionListener(e -> {
            if (selectedRooms.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn ít nhất một phòng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Đặt phòng thành công! Đã đặt " + selectedRooms.size() + " phòng.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                selectedRooms.clear();
                refreshBookingList();
                refreshTable(allRooms);
            }
        });
        inner.add(confirmBtn, BorderLayout.SOUTH);

        right.add(inner, BorderLayout.CENTER);

        // Initial dummy booking entries to match the design
        addDefaultBookings();

        return right;
    }

    private void addDefaultBookings() {
        bookingList.removeAll();
        String[][] defaults = {
                {"#002", "12:00 30/09/2025", "15:00 30/09/2025", "3 giờ"},
                {"#005", "30/09/2025",        "01/10/2025",       "1 ngày"},
                {"#005", "30/09/2025",        "01/10/2025",       "1 ngày"},
                {"#005", "30/09/2025",        "01/10/2025",       "1 ngày"},
        };
        for (String[] b : defaults) {
            bookingList.add(buildBookingRow(b[0], b[1], b[2], b[3]));
        }
        bookingList.add(Box.createVerticalGlue());
        bookingList.revalidate();
        bookingList.repaint();
    }

    private void refreshBookingList() {
        bookingList.removeAll();
        if (selectedRooms.isEmpty()) {
            addDefaultBookings();
            return;
        }
        for (Room r : selectedRooms) {
            bookingList.add(buildBookingRow("#" + r.id, "30/09/2025", "01/10/2025", "1 ngày"));
        }
        bookingList.add(Box.createVerticalGlue());
        bookingList.revalidate();
        bookingList.repaint();
    }

    private JPanel buildBookingRow(String roomId, String checkIn, String checkOut, String duration) {
        JPanel row = new JPanel(new GridLayout(1, 4, 0, 0));
        row.setBackground(WHITE);
        row.setPreferredSize(new Dimension(0, 90));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        row.setBorder(new MatteBorder(0, 0, 1, 0, BORDER));

        // Room ID cell
        JPanel idCell = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        idCell.setOpaque(false);
        // Green check circle
        JPanel checkCircle = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(GREEN);
                g2.fillOval(0, 0, 18, 18);
                g2.setColor(WHITE);
                g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(4, 9, 7, 13);
                g2.drawLine(7, 13, 14, 5);
                g2.dispose();
            }
        };
        checkCircle.setOpaque(false);
        checkCircle.setPreferredSize(new Dimension(18, 18));
        idCell.add(checkCircle);
        idCell.add(lbl(roomId, F_BOLD12, DARK));

        // Check-in cell
        JPanel ciCell = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 8));
        ciCell.setOpaque(false);
        ciCell.add(buildDateBadge("Check-in", checkIn, BLUE_L, BLUE));

        // Check-out cell
        JPanel coCell = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 8));
        coCell.setOpaque(false);
        coCell.add(buildDateBadge("Check-out", checkOut, GREEN_L, GREEN));

        // Duration cell
        JPanel durCell = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        durCell.setOpaque(false);
        durCell.add(lbl(duration, F_PLAIN12, MID));

        row.add(idCell);
        row.add(ciCell);
        row.add(coCell);
        row.add(durCell);
        return row;
    }

    private JPanel buildDateBadge(String label, String date, Color bg, Color fg) {
        JPanel badge = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
            }
        };
        badge.setLayout(new BoxLayout(badge, BoxLayout.Y_AXIS));
        badge.setOpaque(false);
        badge.setBorder(new EmptyBorder(8, 12, 8, 12));

        // Icon + label row
        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        topRow.setOpaque(false);
        // Mini calendar icon
        JLabel calIcon = new JLabel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(fg);
                g2.setStroke(new BasicStroke(1.3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawRoundRect(1, 2, 12, 11, 2, 2);
                g2.drawLine(1, 6, 13, 6);
                g2.drawLine(4, 0, 4, 4);
                g2.drawLine(10, 0, 10, 4);
                g2.dispose();
            }
        };
        calIcon.setPreferredSize(new Dimension(14, 16));
        topRow.add(calIcon);
        topRow.add(lbl(label, F_BOLD12, fg));
        topRow.setAlignmentX(LEFT_ALIGNMENT);

        JLabel dateLbl = lbl(date, F_SMALL, fg);
        dateLbl.setAlignmentX(LEFT_ALIGNMENT);

        badge.add(topRow);
        badge.add(dateLbl);
        return badge;
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

    static JButton blueBtn(String text, int w, int h) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color hover = getModel().isRollover() ? new Color(0x2A5CD4) : BLUE;
                g2.setColor(hover);
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