package iuh.view;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import iuh.dto.PhongDTO;
import iuh.network.ClientConnection;
import iuh.network.CommandType;
import iuh.network.Request;
import iuh.network.Response;
import iuh.service.impl.DoiPhongServiceImpl;
import iuh.entity.Phong;

public class DoiPhongPanel extends JPanel {

    // ── Palette ─── đồng bộ GiaHanPhongPanel ─────────────────────────────────
    static final Color BG          = new Color(0xF4F6FB);
    static final Color WHITE       = Color.WHITE;
    static final Color BLUE        = new Color(0x3B6FF0);
    static final Color BLUE_DARK   = new Color(0x2A5CD4);
    static final Color BLUE_LIGHT  = new Color(0xEBF0FF);
    static final Color BLUE_ROW    = new Color(0xE8EFFE);
    static final Color DARK        = new Color(0x1A1A2E);
    static final Color MID         = new Color(0x4A5268);
    static final Color GRAY        = new Color(0xA0A8B8);
    static final Color BORDER      = new Color(0xE4E9F2);
    static final Color TEAL        = new Color(0x0EA47A);
    static final Color TEAL_L      = new Color(0xE6FAF5);
    static final Color RED         = new Color(0xE04040);
    static final Color RED_L       = new Color(0xFFF5F5);
    static final Color PANEL_R     = new Color(0xF8FAFD);
    static final Color HEADER_BG   = new Color(0xF0F4FF);

    // badge
    static final Color STD_BG = new Color(0xF1F5F9); static final Color STD_FG = new Color(0x475569);
    static final Color SUP_BG = new Color(0xEFF6FF); static final Color SUP_FG = new Color(0x2563EB);
    static final Color DEL_BG = new Color(0xFEFCE8); static final Color DEL_FG = new Color(0x854D0E);

    // ── Fonts ─── đồng bộ GiaHanPhongPanel ───────────────────────────────────
    static final Font F_SECTION = new Font("Segoe UI", Font.BOLD,  16);
    static final Font F_BOLD14  = new Font("Segoe UI", Font.BOLD,  14);
    static final Font F_BOLD13  = new Font("Segoe UI", Font.BOLD,  13);
    static final Font F_BOLD12  = new Font("Segoe UI", Font.BOLD,  12);
    static final Font F_LABEL   = new Font("Segoe UI", Font.PLAIN, 13);
    static final Font F_PLAIN12 = new Font("Segoe UI", Font.PLAIN, 12);
    static final Font F_PLAIN11 = new Font("Segoe UI", Font.PLAIN, 11);
    static final Font F_TABLE   = new Font("Segoe UI", Font.PLAIN, 13);
    static final Font F_TABLE_H = new Font("Segoe UI", Font.BOLD,  12);

    // ── Data ──────────────────────────────────────────────────────────────────
    static class Room {
        String id, type, floor, oldPrice, newPrice;
        Room(String id, String type, String floor, String oldPrice, String newPrice) {
            this.id = id; this.type = type; this.floor = floor;
            this.oldPrice = oldPrice; this.newPrice = newPrice;
        }
    }

    private final List<Room> bookedRooms    = new ArrayList<>();
    private final List<Room> availRooms     = new ArrayList<>();
    private final List<Room> allBookedRooms = new ArrayList<>();

    private int selectedBooked = 0;
    private int selectedAvail  = 0;

    private DefaultTableModel bookedModel;
    private DefaultTableModel availModel;
    private JTable bookedTable;
    private JTable availTable;
    private JPanel    summaryPanel;
    private JTextField searchField;

    private final DoiPhongServiceImpl doiPhongServiceImpl = new DoiPhongServiceImpl();

    // ── Constructor ───────────────────────────────────────────────────────────
    public DoiPhongPanel(String maPDP) {
        setLayout(new BorderLayout());
        setBackground(BG);
        initData(maPDP);

        add(buildHeader(), BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                buildLeft(), buildRight());
        split.setDividerLocation(580);
        split.setDividerSize(0);
        split.setBorder(null);
        split.setBackground(BG);
        split.setContinuousLayout(true);
        add(split, BorderLayout.CENTER);

        refreshBookedTable();
        refreshAvailTable();
    }

    // ── Data helpers ──────────────────────────────────────────────────────────
    private String formatPrice(double price) {
        return String.format("%,.0f VND", price);
    }

    private double parsePrice(String price) {
        try {
            return Double.parseDouble(price.replace(",", "").replace(" VND", "").trim());
        } catch (NumberFormatException e) { return 0; }
    }

    private void initData(String maPDP) {
        bookedRooms.clear();
        availRooms.clear();
        allBookedRooms.clear();

        Request rs = Request.builder().commandType(CommandType.GET_ALL_BOOKED_ROOMS).build();
        Response rp = ClientConnection.getInstance().sendRequest(rs);
        @SuppressWarnings("unchecked")
        List<PhongDTO> listAllBookedRooms = (List<PhongDTO>) rp.getObject();
        if (listAllBookedRooms != null) {
            for (PhongDTO p : listAllBookedRooms) {
                Room r = new Room(
                        p.getMaPhong(),
                        p.getLoaiPhong().getTenLoaiPhong(),
                        "Tầng " + p.getTang(),
                        formatPrice(p.getLoaiPhong().getGia()),
                        formatPrice(p.getLoaiPhong().getGia())
                );
                bookedRooms.add(r);
                allBookedRooms.add(r);
            }
        }

        Request request = Request.builder().commandType(CommandType.GET_AVAILABLE_ROOMS).build();
        Response response = ClientConnection.getInstance().sendRequest(request);
        @SuppressWarnings("unchecked")
        List<PhongDTO> list = (List<PhongDTO>) response.getObject();
        for (PhongDTO p : list) {
            availRooms.add(new Room(p.getMaPhong(),
                    p.getLoaiPhong().getTenLoaiPhong(),
                    "Tầng " + p.getTang(),
                    formatPrice(p.getLoaiPhong().getGia()),
                    formatPrice(p.getLoaiPhong().getGia())));
        }
    }

    private void reloadAll() {
        selectedBooked = 0;
        selectedAvail  = 0;
        if (searchField != null) searchField.setText("");
        initData(null);
        refreshBookedTable();
        refreshAvailTable();
        refreshSummary();
    }

    private String getMaPDPByPhong(String maPhong) {
        return doiPhongServiceImpl.getMaPDPByPhong(maPhong);
    }

    // =========================================================================
    //  HEADER
    // =========================================================================
    private JPanel buildHeader() {
        JPanel h = new JPanel(new BorderLayout());
        h.setBackground(WHITE);
        h.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 1, 0, BORDER),
                new EmptyBorder(10, 24, 10, 24)));
        h.setPreferredSize(new Dimension(0, 54));

        // Left: title + subtitle
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);
        JLabel title = new JLabel("Đổi phòng");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(DARK);
        JLabel sub = new JLabel("Chọn phòng cần đổi và phòng muốn chuyển sang");
        sub.setFont(F_PLAIN11);
        sub.setForeground(GRAY);
        left.add(title);
        left.add(sub);
        h.add(left, BorderLayout.WEST);

        // Right: avatar
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
                g2.drawString("N", 17 - fm.stringWidth("N") / 2, 17 + fm.getAscent() / 2 - 2);
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
        JPanel left = new JPanel(new BorderLayout(0, 14));
        left.setBackground(BG);
        left.setBorder(new EmptyBorder(20, 20, 20, 12));

        // Section 1
        JPanel sec1 = new JPanel(new BorderLayout(0, 8));
        sec1.setOpaque(false);
        JLabel title1 = new JLabel("Chọn phòng cần đổi");
        title1.setFont(F_SECTION);
        title1.setForeground(DARK);
        sec1.add(title1, BorderLayout.NORTH);
        sec1.add(buildSearchBar(), BorderLayout.CENTER);

        // Card bảng booked
        JPanel bookedCard = buildBookedTable();

        JPanel top = new JPanel(new BorderLayout(0, 10));
        top.setOpaque(false);
        top.add(sec1, BorderLayout.NORTH);
        top.add(bookedCard, BorderLayout.CENTER);

        // Section 2
        JPanel sec2Header = new JPanel(new BorderLayout());
        sec2Header.setOpaque(false);
        JLabel title2 = new JLabel("Chọn phòng muốn đổi");
        title2.setFont(F_SECTION);
        title2.setForeground(DARK);
        sec2Header.add(title2, BorderLayout.WEST);

        JPanel sec2 = new JPanel(new BorderLayout(0, 8));
        sec2.setOpaque(false);
        sec2.add(sec2Header, BorderLayout.NORTH);
        sec2.add(buildAvailTable(), BorderLayout.CENTER);

        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.BOTH; gc.weightx = 1; gc.gridx = 0;
        gc.gridy = 0; gc.weighty = 0.45; center.add(top, gc);
        gc.gridy = 1; gc.weighty = 0;    center.add(Box.createVerticalStrut(20), gc);
        gc.gridy = 2; gc.weighty = 0.55; center.add(sec2, gc);

        left.add(center, BorderLayout.CENTER);
        return left;
    }

    // ── Search bar ────────────────────────────────────────────────────────────
    private JPanel buildSearchBar() {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        // Icon kính lúp vẽ tay — giống GiaHanPhongPanel
        JPanel iconWrap = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(GRAY);
                g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                int cx = getWidth() / 2 - 1, cy = getHeight() / 2 - 1, r = 5;
                g2.drawOval(cx - r, cy - r, r * 2, r * 2);
                g2.drawLine(cx + r - 1, cy + r - 1, cx + r + 3, cy + r + 3);
                g2.dispose();
            }
        };
        iconWrap.setOpaque(false);
        iconWrap.setPreferredSize(new Dimension(34, 40));

        searchField = new JTextField();
        searchField.setFont(F_LABEL);
        searchField.setForeground(DARK);
        searchField.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
        searchField.setOpaque(false);
        searchField.setToolTipText("Tìm theo mã phòng, loại phòng...");

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(WHITE);
        wrapper.setBorder(new CompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(0, 0, 0, 8)));
        wrapper.setPreferredSize(new Dimension(0, 40));
        wrapper.add(iconWrap, BorderLayout.WEST);
        wrapper.add(searchField, BorderLayout.CENTER);

        JButton searchBtn = roundBtn("Tìm kiếm", BLUE, WHITE, 100, 40, 8);
        ActionListener doSearch = e -> {
            String kw = searchField.getText().trim().toLowerCase();
            bookedRooms.clear();
            if (kw.isEmpty()) {
                bookedRooms.addAll(allBookedRooms);
            } else {
                allBookedRooms.stream()
                        .filter(r -> r.id.toLowerCase().contains(kw)
                                || r.type.toLowerCase().contains(kw)
                                || r.floor.toLowerCase().contains(kw))
                        .forEach(bookedRooms::add);
            }
            selectedBooked = 0;
            refreshBookedTable();
            if (!bookedRooms.isEmpty()) refreshSummary();
        };
        searchBtn.addActionListener(doSearch);
        searchField.addActionListener(doSearch);

        JButton refreshBtn = new JButton("Làm mới") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? new Color(0x5A6478) : new Color(0x6B7280));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(WHITE); g2.setFont(F_BOLD12);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(),
                        (getWidth() - fm.stringWidth(getText())) / 2,
                        (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        refreshBtn.setOpaque(false); refreshBtn.setContentAreaFilled(false);
        refreshBtn.setBorderPainted(false); refreshBtn.setForeground(WHITE);
        refreshBtn.setFont(F_BOLD12);
        refreshBtn.setPreferredSize(new Dimension(90, 40));
        refreshBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        refreshBtn.addActionListener(e -> reloadAll());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        btnPanel.setOpaque(false);
        btnPanel.add(searchBtn);
        btnPanel.add(refreshBtn);

        row.add(wrapper, BorderLayout.CENTER);
        row.add(btnPanel, BorderLayout.EAST);
        return row;
    }

    // ── "Chọn phòng này" button ───────────────────────────────────────────────
    private JButton buildChonBtn() {
        JButton btn = roundBtn("Chọn phòng này", BLUE, WHITE, 130, 30, 6);
        btn.addActionListener(e -> {
            if (selectedAvail < 0 || selectedAvail >= availRooms.size()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn phòng muốn đổi!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            refreshSummary();
        });
        return btn;
    }

    // ── Booked table ──────────────────────────────────────────────────────────
    private JPanel buildBookedTable() {
        String[] cols = {"Số phòng", "Loại phòng", "Tầng", "Giá / ngày"};
        bookedModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        bookedTable = createStyledTable(bookedModel);
        bookedTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = bookedTable.getSelectedRow();
                if (row >= 0 && row < bookedRooms.size()) {
                    selectedBooked = row;
                    if (searchField != null) searchField.setText(bookedRooms.get(row).id);
                    refreshSummary();
                }
            }
        });
        JScrollPane sp = new JScrollPane(bookedTable);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setBackground(WHITE);
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(WHITE);
        card.setBorder(new LineBorder(BORDER, 1, true));
        card.add(sp, BorderLayout.CENTER);
        return card;
    }

    private void refreshBookedTable() {
        bookedModel.setRowCount(0);
        for (Room r : bookedRooms)
            bookedModel.addRow(new Object[]{r.id, r.type, r.floor, r.newPrice});
        if (!bookedRooms.isEmpty() && selectedBooked < bookedRooms.size())
            bookedTable.getSelectionModel().setSelectionInterval(selectedBooked, selectedBooked);
    }

    // ── Available table ───────────────────────────────────────────────────────
    private JPanel buildAvailTable() {
        String[] cols = {"Số phòng", "Loại phòng", "Tầng", "Giá / ngày"};
        availModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        availTable = createStyledTable(availModel);
        availTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = availTable.getSelectedRow();
                if (row >= 0 && row < availRooms.size()) {
                    selectedAvail = row;
                    refreshSummary();
                }
            }
        });
        JScrollPane sp = new JScrollPane(availTable);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setBackground(WHITE);
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(WHITE);
        card.setBorder(new LineBorder(BORDER, 1, true));
        card.add(sp, BorderLayout.CENTER);
        return card;
    }

    private void refreshAvailTable() {
        availModel.setRowCount(0);
        for (Room r : availRooms)
            availModel.addRow(new Object[]{r.id, r.type, r.floor, r.newPrice});
        if (!availRooms.isEmpty() && selectedAvail < availRooms.size())
            availTable.getSelectionModel().setSelectionInterval(selectedAvail, selectedAvail);
    }

    // ── JTable factory — đồng bộ GiaHanPhongPanel ────────────────────────────
    private JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model) {
            @Override public Component prepareRenderer(TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                if (isRowSelected(row)) {
                    c.setBackground(BLUE_ROW);
                    c.setForeground(BLUE);
                } else {
                    c.setBackground(row % 2 == 0 ? WHITE : new Color(0xFAFBFD));
                    c.setForeground(col == 0 ? BLUE : DARK);
                }
                return c;
            }
        };
        table.setFont(F_TABLE);
        table.setRowHeight(54);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(BORDER);
        table.setBackground(WHITE);
        table.setSelectionBackground(BLUE_ROW);
        table.setSelectionForeground(BLUE);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFocusable(false);
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTableHeader header = table.getTableHeader();
        header.setFont(F_TABLE_H);
        header.setForeground(MID);
        header.setBackground(HEADER_BG);
        header.setBorder(new MatteBorder(0, 0, 1, 0, BORDER));
        header.setPreferredSize(new Dimension(0, 38));
        ((DefaultTableCellRenderer) header.getDefaultRenderer())
                .setHorizontalAlignment(JLabel.CENTER);

        // Số phòng: blue + bold + center
        table.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean sel, boolean focus, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, focus, row, col);
                setForeground(BLUE);
                setFont(F_BOLD13);
                setHorizontalAlignment(JLabel.CENTER);
                setBackground(sel ? BLUE_ROW : (row % 2 == 0 ? WHITE : new Color(0xFAFBFD)));
                return this;
            }
        });

        // Các cột còn lại: căn giữa
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 1; i < table.getColumnCount(); i++)
            table.getColumnModel().getColumn(i).setCellRenderer(center);

        return table;
    }

    // =========================================================================
    //  RIGHT PANEL — summary
    // =========================================================================
    private JPanel buildRight() {
        JPanel right = new JPanel(new GridBagLayout());
        right.setBackground(PANEL_R);
        right.setBorder(new CompoundBorder(
                new MatteBorder(0, 1, 0, 0, BORDER),
                new EmptyBorder(20, 18, 20, 18)));

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1.0; gc.gridx = 0;

        gc.gridy = 0;
        JLabel sumTitle = new JLabel("Tóm tắt đổi phòng");
        sumTitle.setFont(F_SECTION);
        sumTitle.setForeground(DARK);
        right.add(sumTitle, gc);

        gc.gridy = 1;
        right.add(Box.createVerticalStrut(10), gc);

        // Arrow chip row
        gc.gridy = 2;
        right.add(buildArrowChip(), gc);

        gc.gridy = 3;
        right.add(Box.createVerticalStrut(14), gc);

        gc.gridy = 4;
        right.add(buildInfoCard("Phòng ban đầu", TEAL, TEAL_L, true), gc);

        gc.gridy = 5;
        right.add(Box.createVerticalStrut(10), gc);

        gc.gridy = 6;
        right.add(buildInfoCard("Phòng đổi sang", TEAL, TEAL_L, false), gc);

        gc.gridy = 7;
        right.add(Box.createVerticalStrut(10), gc);

        gc.gridy = 8;
        summaryPanel = buildFeeCard();
        right.add(summaryPanel, gc);

        gc.gridy = 9;
        right.add(Box.createVerticalStrut(16), gc);

        gc.gridy = 10;
        right.add(buildConfirmBtn(), gc);

        gc.gridy = 11; gc.weighty = 1.0;
        right.add(Box.createVerticalGlue(), gc);

        return right;
    }

    // ── Arrow chip: P001 → P303 ───────────────────────────────────────────────
    private JPanel buildArrowChip() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        p.setOpaque(false);

        Room from = safeGet(bookedRooms, selectedBooked);
        Room to   = safeGet(availRooms,  selectedAvail);

        p.add(chip(from != null ? from.id : "—"));
        JLabel arrow = lbl("→", new Font("Segoe UI", Font.PLAIN, 16), GRAY);
        p.add(arrow);
        p.add(chip(to != null ? to.id : "—"));
        return p;
    }

    private JLabel chip(String text) {
        JLabel l = new JLabel(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        l.setFont(F_BOLD13);
        l.setForeground(DARK);
        l.setOpaque(false);
        l.setBorder(new EmptyBorder(5, 14, 5, 14));
        return l;
    }

    // ── Info card (phòng ban đầu / đổi sang) ──────────────────────────────────
    private JPanel buildInfoCard(String headerText, Color accentColor, Color headerBg, boolean isOriginal) {
        JPanel card = roundCard(accentColor);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        // Header
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(headerBg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight() + 10, 10, 10);
                g2.dispose();
            }
        };
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(8, 12, 8, 12));
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

        // dot
        JPanel dot = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(accentColor);
                g2.fillOval(0, 3, 6, 6);
                g2.dispose();
            }
        };
        dot.setOpaque(false);
        dot.setPreferredSize(new Dimension(6, 12));
        header.add(dot);
        header.add(lbl(headerText.toUpperCase(), new Font("Segoe UI", Font.BOLD, 10), accentColor));
        card.add(header);

        // Body
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setOpaque(false);
        body.setBorder(new EmptyBorder(10, 14, 12, 14));
        body.setAlignmentX(LEFT_ALIGNMENT);

        Room r = isOriginal ? safeGet(bookedRooms, selectedBooked) : safeGet(availRooms, selectedAvail);
        String id    = r != null ? r.id       : "—";
        String type  = r != null ? r.type     : "—";
        String price = r != null ? r.newPrice + " / ngày" : "—";

        body.add(infoRow("Số phòng",  id,    true));
        body.add(Box.createVerticalStrut(7));
        body.add(sep());
        body.add(Box.createVerticalStrut(7));
        body.add(infoRow("Loại phòng", type, false));
        body.add(Box.createVerticalStrut(7));
        body.add(sep());
        body.add(Box.createVerticalStrut(7));
        body.add(infoRow("Giá",        price,false));
        card.add(body);
        return card;
    }

    // ── Fee card ──────────────────────────────────────────────────────────────
    private JPanel buildFeeCard() {
        Room from = safeGet(bookedRooms, selectedBooked);
        Room to   = safeGet(availRooms,  selectedAvail);

        double giacu  = from != null ? parsePrice(from.newPrice) : 0;
        double giamoi = to   != null ? parsePrice(to.newPrice)   : 0;
        double chenh  = giamoi - giacu;

        String strTienDaCoc   = formatPrice(giacu  * 0.3);
        String strCocMoi      = formatPrice(giamoi * 0.3);
        String strTienCocThem = formatPrice(Math.max(0, chenh) * 0.3);

        JPanel card = roundCard(RED);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        // Header
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(RED_L);
                g2.fillRoundRect(0, 0, getWidth(), getHeight() + 10, 10, 10);
                g2.dispose();
            }
        };
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(8, 12, 8, 12));
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        JPanel rdot = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(RED);
                g2.fillOval(0, 3, 6, 6);
                g2.dispose();
            }
        };
        rdot.setOpaque(false);
        rdot.setPreferredSize(new Dimension(6, 12));
        header.add(rdot);
        header.add(lbl("PHÍ CHÊNH LỆCH", new Font("Segoe UI", Font.BOLD, 10), RED));
        card.add(header);

        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setOpaque(false);
        body.setBorder(new EmptyBorder(10, 14, 12, 14));
        body.setAlignmentX(LEFT_ALIGNMENT);

        body.add(infoRow("Tiền đã cọc",            strTienDaCoc,   false));
        body.add(Box.createVerticalStrut(7));
        body.add(sep());
        body.add(Box.createVerticalStrut(7));
        body.add(infoRow("Cọc phòng mới (30%)",    strCocMoi,      false));
        body.add(Box.createVerticalStrut(7));
        body.add(sep());
        body.add(Box.createVerticalStrut(7));

        // Tiền cọc thêm — highlight in red
        JPanel feeRow = new JPanel(new BorderLayout());
        feeRow.setOpaque(false);
        feeRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));
        feeRow.setAlignmentX(LEFT_ALIGNMENT);
        feeRow.add(lbl("Tiền cọc thêm", F_BOLD12, DARK), BorderLayout.WEST);
        JLabel feeVal = lbl(strTienCocThem, F_BOLD13, RED);
        feeRow.add(feeVal, BorderLayout.EAST);
        body.add(feeRow);

        card.add(body);
        return card;
    }

    // ── Confirm button ────────────────────────────────────────────────────────
    private JPanel buildConfirmBtn() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);

        JButton btn = new JButton("Xác nhận đổi phòng") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? BLUE_DARK : BLUE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(),
                        (getWidth() - fm.stringWidth(getText())) / 2,
                        (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        btn.setOpaque(false); btn.setContentAreaFilled(false);
        btn.setBorderPainted(false); btn.setForeground(WHITE);
        btn.setPreferredSize(new Dimension(0, 46));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addActionListener(e -> {
            if (selectedBooked < 0 || selectedBooked >= bookedRooms.size()
                    || selectedAvail  < 0 || selectedAvail  >= availRooms.size()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn đủ hai phòng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Room from = bookedRooms.get(selectedBooked);
            Room to   = availRooms.get(selectedAvail);
            double giacu  = parsePrice(from.newPrice);
            double giamoi = parsePrice(to.newPrice);

            if (giamoi < giacu) {
                JOptionPane.showMessageDialog(this,
                        "Không thể đổi sang phòng có giá thấp hơn!\n"
                                + from.id + " (" + formatPrice(giacu) + ") → "
                                + to.id   + " (" + formatPrice(giamoi) + ")",
                        "Không hợp lệ", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Xác nhận đổi phòng " + from.id + " → " + to.id + "?",
                    "Xác nhận đổi phòng", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    doiPhongServiceImpl.doiPhong(getMaPDPByPhong(from.id), from.id, to.id);
                    JOptionPane.showMessageDialog(this,
                            "Đổi phòng thành công!\n" + from.id + " → " + to.id,
                            "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    reloadAll();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Thất bại", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        wrapper.add(btn, BorderLayout.CENTER);
        return wrapper;
    }

    // ── refreshSummary ────────────────────────────────────────────────────────
    private void refreshSummary() {
        Container parent = summaryPanel.getParent();
        if (parent == null) return;
        JPanel right = (JPanel) parent;
        right.removeAll();

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1.0; gc.gridx = 0;

        gc.gridy = 0;
        JLabel sumTitle2 = new JLabel("Tóm tắt đổi phòng");
        sumTitle2.setFont(F_SECTION); sumTitle2.setForeground(DARK);
        right.add(sumTitle2, gc);
        gc.gridy = 1;  right.add(Box.createVerticalStrut(10), gc);
        gc.gridy = 2;  right.add(buildArrowChip(), gc);
        gc.gridy = 3;  right.add(Box.createVerticalStrut(14), gc);
        gc.gridy = 4;  right.add(buildInfoCard("Phòng ban đầu",  TEAL, TEAL_L, true),  gc);
        gc.gridy = 5;  right.add(Box.createVerticalStrut(10), gc);
        gc.gridy = 6;  right.add(buildInfoCard("Phòng đổi sang", TEAL, TEAL_L, false), gc);
        gc.gridy = 7;  right.add(Box.createVerticalStrut(10), gc);
        gc.gridy = 8;  summaryPanel = buildFeeCard(); right.add(summaryPanel, gc);
        gc.gridy = 9;  right.add(Box.createVerticalStrut(16), gc);
        gc.gridy = 10; right.add(buildConfirmBtn(), gc);
        gc.gridy = 11; gc.weighty = 1.0; right.add(Box.createVerticalGlue(), gc);

        right.revalidate();
        right.repaint();
    }

    // =========================================================================
    //  HELPERS
    // =========================================================================
    private Room safeGet(List<Room> list, int idx) {
        return (idx >= 0 && idx < list.size()) ? list.get(idx) : null;
    }

    private JPanel roundCard(Color borderColor) {
        JPanel p = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(borderColor);
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.dispose();
            }
        };
        p.setOpaque(false);
        return p;
    }

    private JPanel infoRow(String key, String value, boolean accentValue) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        p.setAlignmentX(LEFT_ALIGNMENT);
        p.add(lbl(key, F_PLAIN12, GRAY), BorderLayout.WEST);
        p.add(lbl(value, F_PLAIN12, accentValue ? BLUE : DARK), BorderLayout.EAST);
        return p;
    }

    private JSeparator sep() {
        JSeparator s = new JSeparator();
        s.setForeground(BORDER);
        s.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        return s;
    }

    private JButton roundBtn(String text, Color bg, Color fg, int w, int h, int arc) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? BLUE_DARK : bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
                g2.setColor(fg); g2.setFont(F_BOLD12);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(),
                        (getWidth() - fm.stringWidth(getText())) / 2,
                        (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        btn.setOpaque(false); btn.setContentAreaFilled(false);
        btn.setBorderPainted(false); btn.setForeground(fg);
        btn.setFont(F_BOLD12);
        btn.setPreferredSize(new Dimension(w, h));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    static JLabel lbl(String t, Font f, Color c) {
        JLabel l = new JLabel(t); l.setFont(f); l.setForeground(c); return l;
    }
}