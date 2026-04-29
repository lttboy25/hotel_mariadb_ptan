package iuh.view;

import iuh.entity.ChiTietPhieuDatPhong;
import iuh.entity.Phong;
import iuh.service.ChiTietPhieuDatPhongService;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;

/**
 * GiaHanPhongPanel – dữ liệu thật qua ChiTietPhieuDatPhongService → ChitietPhieuDatPhongDaoImpl.
 *
 * Luồng:
 *  1. Nhập SĐT → nhấn "Tìm" (hoặc Enter) → bảng trái hiện phòng đang thuê
 *  2. Click dòng → toggle chọn → bảng phải hiện dòng gia hạn
 *  3. Chọn loại (Theo giờ / Theo ngày) + spinner số lượng → tự tính ngày mới
 *  4. Nhấn "Gia hạn ngay" → lưu DB → thông báo thành công
 */
public class GiaHanPhongPanel extends JPanel {

    // ── Màu sắc ──────────────────────────────────────────────────────────────
    private static final Color BG_MAIN    = new Color(0xF4F6FB);
    private static final Color BG_WHITE   = Color.WHITE;
    private static final Color BLUE       = new Color(0x3B6FF0);
    private static final Color BLUE_LIGHT = new Color(0xEBF0FF);
    private static final Color BLUE_ROW   = new Color(0xE8EFFE);
    private static final Color TEXT_DARK  = new Color(0x1A1A2E);
    private static final Color TEXT_MID   = new Color(0x4A5268);
    private static final Color TEXT_GRAY  = new Color(0xA0A8B8);
    private static final Color BORDER_COL = new Color(0xE4E9F2);
    private static final Color GREEN      = new Color(0x3DBE8A);
    private static final Color GRAY_TAG_BG = new Color(0xEEF0F5);
    private static final Color PURPLE_BTN  = new Color(0x5B6CF9);
    private static final Color PURPLE_DARK = new Color(0x4455E0);
    private static final Color HEADER_BG   = new Color(0xF0F4FF);
    private static final Color RED         = new Color(0xE04040);

    // ── Font ─────────────────────────────────────────────────────────────────
    private static final Font F_SECTION = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font F_LABEL   = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font F_BOLD13  = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font F_BOLD14  = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font F_SMALL   = new Font("Segoe UI", Font.PLAIN, 11);
    private static final Font F_TABLE   = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font F_TABLE_H = new Font("Segoe UI", Font.BOLD, 12);

    private static final DateTimeFormatter FMT_DT   = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
    private static final DateTimeFormatter FMT_DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // ── State ─────────────────────────────────────────────────────────────────
    /** Danh sách chi tiết phiếu đang hiển thị (load từ DB) */
    private List<ChiTietPhieuDatPhong> danhSachDangThue = new ArrayList<>();

    /**
     * Map: chiTietId → RowModel (loại gia hạn + số lượng).
     * Chỉ chứa các phòng đã được chọn ở bảng trái.
     */
    private final Map<Long, RowModel> selectedRows = new LinkedHashMap<>();

    // ── UI components ─────────────────────────────────────────────────────────
    private JTextField        searchField;
    private JLabel            statusLabel;
    private DefaultTableModel roomTableModel;
    private JTable            roomTable;
    private JPanel            extendRowsPanel;

    // ── Service ───────────────────────────────────────────────────────────────
    private final ChiTietPhieuDatPhongService service = new ChiTietPhieuDatPhongService();

    // ── Inner model mỗi dòng bên phải ────────────────────────────────────────
    private static class RowModel {
        final Long            chiTietId;
        final LocalDateTime   thoiGianTraCu;
        String loaiGiaHan = "Theo giờ";
        int    soLuong    = 1;

        RowModel(Long id, LocalDateTime tra) {
            this.chiTietId     = id;
            this.thoiGianTraCu = tra;
        }

        LocalDateTime tinhThoiGianTraMoi() {
            if (thoiGianTraCu == null) return null;
            return "Theo giờ".equals(loaiGiaHan)
                    ? thoiGianTraCu.plusHours(soLuong)
                    : thoiGianTraCu.plusDays(soLuong);
        }

        String thoiGianTraCuStr() {
            if (thoiGianTraCu == null) return "--";
            return "Theo giờ".equals(loaiGiaHan)
                    ? thoiGianTraCu.format(FMT_DT)
                    : thoiGianTraCu.format(FMT_DATE);
        }

        String thoiGianTraMoiStr() {
            LocalDateTime dt = tinhThoiGianTraMoi();
            if (dt == null) return "--";
            return "Theo giờ".equals(loaiGiaHan) ? dt.format(FMT_DT) : dt.format(FMT_DATE);
        }

        String thoiGianGiaHanStr() {
            return soLuong + ("Theo giờ".equals(loaiGiaHan) ? " giờ" : " ngày");
        }
    }

    // ── Constructor ───────────────────────────────────────────────────────────
    public GiaHanPhongPanel() {
        SwingUtilities.invokeLater(() -> doSearch());
        setLayout(new BorderLayout());
        setBackground(BG_MAIN);
        setBorder(new EmptyBorder(28, 28, 28, 28));

        JPanel mainRow = new JPanel(new GridBagLayout());
        mainRow.setBackground(BG_MAIN);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill    = GridBagConstraints.BOTH;
        gbc.gridy   = 0;
        gbc.weighty = 1.0;

        gbc.gridx   = 0;
        gbc.weightx = 0.60;
        gbc.insets  = new Insets(0, 0, 0, 20);
        mainRow.add(buildLeftPanel(), gbc);

        gbc.gridx   = 1;
        gbc.weightx = 0.40;
        gbc.insets  = new Insets(0, 0, 0, 0);
        mainRow.add(buildRightPanel(), gbc);

        add(mainRow, BorderLayout.CENTER);
    }

    // =========================================================================
    // PANEL TRÁI
    // =========================================================================
    private JPanel buildLeftPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 14));
        panel.setBackground(BG_MAIN);

        JLabel title = new JLabel("Chọn phòng gia hạn");
        title.setFont(F_SECTION);
        title.setForeground(TEXT_DARK);
        panel.add(title, BorderLayout.NORTH);

        JPanel card = new RoundedPanel(12, BG_WHITE);
        card.setLayout(new BorderLayout(0, 8));
        card.setBorder(new EmptyBorder(14, 14, 14, 14));

        // Search bar + status
        JPanel topArea = new JPanel(new BorderLayout(0, 6));
        topArea.setOpaque(false);
        topArea.add(buildSearchBar(), BorderLayout.NORTH);

        statusLabel = new JLabel("Nhập số điện thoại để tìm phòng đang thuê.");
        statusLabel.setFont(F_SMALL);
        statusLabel.setForeground(TEXT_GRAY);
        topArea.add(statusLabel, BorderLayout.SOUTH);

        card.add(topArea, BorderLayout.NORTH);
        card.add(buildRoomTable(), BorderLayout.CENTER);

        panel.add(card, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildSearchBar() {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setOpaque(false);

        // Icon kính lúp
        JPanel iconWrap = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(TEXT_GRAY);
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
        searchField.setForeground(TEXT_DARK);
        searchField.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
        searchField.setOpaque(false);
        searchField.setToolTipText("Nhập SĐT hoặc số phòng...");
        searchField.addActionListener(e -> doSearch());

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(BG_WHITE);
        wrapper.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COL, 1, true),
                new EmptyBorder(0, 0, 0, 8)));
        wrapper.add(iconWrap,    BorderLayout.WEST);
        wrapper.add(searchField, BorderLayout.CENTER);

        JButton btnSearch = buildBlueButton("Tìm", 60, 40);
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { doSearch(); }
            public void removeUpdate(DocumentEvent e) { doSearch(); }
            public void changedUpdate(DocumentEvent e) { doSearch(); }
        });

        row.add(wrapper,   BorderLayout.CENTER);
        row.add(btnSearch, BorderLayout.EAST);
        return row;
    }

    private JScrollPane buildRoomTable() {
        String[] cols = { "Số phòng", "Loại phòng", "Tầng", "Ngày nhận", "Ngày trả" };
        roomTableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        roomTable = new JTable(roomTableModel) {
            @Override public Component prepareRenderer(TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                boolean sel = isRowSelected(row);
                c.setBackground(sel ? BLUE_ROW : (row % 2 == 0 ? BG_WHITE : new Color(0xFAFBFD)));
                c.setForeground(sel ? BLUE : (col == 0 ? BLUE : TEXT_DARK));
                return c;
            }
        };
        styleTable(roomTable);
        roomTable.setRowHeight(52);

        // Click dòng → toggle selectedRows
        roomTable.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            int row = roomTable.getSelectedRow();
            if (row < 0 || row >= danhSachDangThue.size()) return;
            ChiTietPhieuDatPhong ct = danhSachDangThue.get(row);
            Long id = ct.getId();
            if (selectedRows.containsKey(id)) {
                selectedRows.remove(id);
            } else {
                selectedRows.put(id, new RowModel(id, ct.getThoiGianTraPhong()));
            }
            refreshExtendRows();
        });

        // Căn chỉnh độ rộng cột
        int[] widths = { 80, 120, 70, 130, 130 };
        for (int i = 0; i < widths.length; i++)
            roomTable.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        // Cột số phòng bôi xanh
        roomTable.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                setForeground(BLUE);
                setFont(F_BOLD13);
                setHorizontalAlignment(JLabel.CENTER);
                setBackground(sel ? BLUE_ROW : (row % 2 == 0 ? BG_WHITE : new Color(0xFAFBFD)));
                return this;
            }
        });

        JScrollPane sp = new JScrollPane(roomTable);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setBackground(BG_WHITE);
        return sp;
    }

    // =========================================================================
    // PANEL PHẢI
    // =========================================================================
    private JPanel buildRightPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_MAIN);

        JLabel title = new JLabel("Chọn thời gian gia hạn");
        title.setFont(F_SECTION);
        title.setForeground(TEXT_DARK);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createVerticalStrut(14));

        // Card header + rows
        JPanel card = new RoundedPanel(12, BG_WHITE);
        card.setLayout(new BorderLayout());
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(buildExtendHeader(), BorderLayout.NORTH);

        extendRowsPanel = new JPanel();
        extendRowsPanel.setLayout(new BoxLayout(extendRowsPanel, BoxLayout.Y_AXIS));
        extendRowsPanel.setBackground(BG_WHITE);
        showPlaceholder("Chọn phòng ở bảng bên trái để gia hạn.");

        JScrollPane scrollRight = new JScrollPane(extendRowsPanel);
        scrollRight.setBorder(BorderFactory.createEmptyBorder());
        scrollRight.getViewport().setBackground(BG_WHITE);
        card.add(scrollRight, BorderLayout.CENTER);

        panel.add(card);
        panel.add(Box.createVerticalStrut(16));

        // Nút Gia hạn ngay – canh phải
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        btnRow.setOpaque(false);
        btnRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnRow.add(buildConfirmButton());
        panel.add(btnRow);

        return panel;
    }

    private JPanel buildExtendHeader() {
        JPanel header = new JPanel(new GridLayout(1, 5, 0, 0));
        header.setBackground(HEADER_BG);
        header.setBorder(new EmptyBorder(10, 16, 10, 16));

        String[] labels = { "Số phòng", "Loại gia hạn", "Ngày trả", "Gia hạn đến", "Thời gian" };
        Color[]  colors = { TEXT_MID, new Color(0x5B6CF9), TEXT_MID, GREEN, TEXT_MID };
        for (int i = 0; i < labels.length; i++) {
            JLabel lbl = new JLabel(labels[i], JLabel.CENTER);
            lbl.setFont(F_TABLE_H);
            lbl.setForeground(colors[i]);
            header.add(lbl);
        }
        return header;
    }

    /** Vẽ lại danh sách dòng gia hạn theo selectedRows */
    private void refreshExtendRows() {
        extendRowsPanel.removeAll();
        if (selectedRows.isEmpty()) {
            showPlaceholder("Chọn phòng ở bảng bên trái để gia hạn.");
        } else {
            boolean first = true;
            for (RowModel model : selectedRows.values()) {
                if (!first) extendRowsPanel.add(buildRowSeparator());
                extendRowsPanel.add(buildExtendRow(model));
                first = false;
            }
        }
        extendRowsPanel.revalidate();
        extendRowsPanel.repaint();
    }

    private void showPlaceholder(String msg) {
        extendRowsPanel.removeAll();
        JLabel lbl = new JLabel(msg, JLabel.CENTER);
        lbl.setFont(F_SMALL);
        lbl.setForeground(TEXT_GRAY);
        lbl.setBorder(new EmptyBorder(28, 0, 28, 0));
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        extendRowsPanel.add(lbl);
        extendRowsPanel.revalidate();
        extendRowsPanel.repaint();
    }

    /**
     * Một dòng gia hạn gồm:
     * [✓ soPhong] [dropdown loaiGiaHan] [ngayTra cũ] [ngayTra mới] [spinner + đơn vị]
     */
    private JPanel buildExtendRow(RowModel model) {
        JPanel row = new JPanel(new GridLayout(1, 5, 8, 0));
        row.setBackground(BG_WHITE);
        row.setBorder(new EmptyBorder(12, 16, 12, 16));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        // Tìm tên phòng từ danh sách đang thuê
        String soPhong = danhSachDangThue.stream()
                .filter(ct -> ct.getId().equals(model.chiTietId))
                .map(ct -> ct.getPhong() != null ? ct.getPhong().getSoPhong() : "#???")
                .findFirst().orElse("#???");

        // Cột 1: ✓ + số phòng
        JPanel col1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
        col1.setOpaque(false);
        col1.add(buildCheckIcon());
        JLabel lblRoom = new JLabel(soPhong);
        lblRoom.setFont(F_BOLD13);
        lblRoom.setForeground(TEXT_DARK);
        col1.add(lblRoom);
        row.add(col1);

        // Cột 2: dropdown loại gia hạn
        JPanel col2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        col2.setOpaque(false);
        col2.add(buildTypeDropdown(model));
        row.add(col2);

        // Cột 3: ngày trả cũ
        row.add(buildDateCell(model.thoiGianTraCuStr(), false));

        // Cột 4: ngày gia hạn mới
        row.add(buildDateCell(model.thoiGianTraMoiStr(), true));

        // Cột 5: spinner số lượng + đơn vị
        JPanel col5 = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        col5.setOpaque(false);

        JSpinner spinner = new JSpinner(new SpinnerNumberModel(model.soLuong, 1, 999, 1));
        spinner.setFont(F_LABEL);
        spinner.setPreferredSize(new Dimension(60, 30));
        spinner.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COL, 1, true),
                new EmptyBorder(2, 4, 2, 4)));
        spinner.addChangeListener(e -> {
            model.soLuong = (int) spinner.getValue();
            refreshExtendRows();
        });

        JLabel lblUnit = new JLabel("Theo giờ".equals(model.loaiGiaHan) ? "Giờ" : "Ngày");
        lblUnit.setFont(F_SMALL);
        lblUnit.setForeground(TEXT_MID);

        col5.add(spinner);
        col5.add(lblUnit);
        row.add(col5);

        return row;
    }

    /** Dropdown Theo giờ / Theo ngày — cập nhật model rồi vẽ lại */
    private JPanel buildTypeDropdown(RowModel model) {
        JLabel text  = new JLabel(model.loaiGiaHan);
        text.setFont(F_BOLD13);
        text.setForeground(TEXT_MID);

        JLabel arrow = new JLabel("▾");
        arrow.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        arrow.setForeground(TEXT_GRAY);

        JPanel pill = new JPanel(new BorderLayout(4, 0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(GRAY_TAG_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        pill.setOpaque(false);
        pill.setBorder(new EmptyBorder(5, 12, 5, 8));
        pill.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        pill.add(text, BorderLayout.CENTER);
        pill.add(arrow, BorderLayout.EAST);

        pill.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                JPopupMenu menu = new JPopupMenu();
                menu.setBorder(new LineBorder(BORDER_COL, 1, true));
                for (String opt : new String[]{ "Theo giờ", "Theo ngày" }) {
                    JMenuItem item = new JMenuItem(opt);
                    item.setFont(F_LABEL);
                    item.addActionListener(ae -> {
                        model.loaiGiaHan = opt;
                        refreshExtendRows();
                    });
                    menu.add(item);
                }
                menu.show(pill, 0, pill.getHeight());
            }
        });
        return pill;
    }

    /** Ô hiển thị ngày — viền xám (ngày cũ) hoặc nền xanh (ngày mới) */
    private JPanel buildDateCell(String date, boolean isGreen) {
        JPanel cell = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(isGreen ? GREEN : BG_WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                if (!isGreen) {
                    g2.setColor(BORDER_COL);
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        cell.setLayout(new BoxLayout(cell, BoxLayout.Y_AXIS));
        cell.setOpaque(false);
        cell.setBorder(new EmptyBorder(6, 10, 6, 10));

        Color fg = isGreen ? Color.WHITE : TEXT_DARK;

        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        topRow.setOpaque(false);
        topRow.add(buildCalendarIcon(fg));
        JLabel lblName = new JLabel(isGreen ? "Gia hạn đến" : "Ngày trả phòng");
        lblName.setFont(F_SMALL);
        lblName.setForeground(fg);
        topRow.add(lblName);
        topRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblDate = new JLabel(date);
        lblDate.setFont(F_BOLD13);
        lblDate.setForeground(fg);
        lblDate.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblDate.setBorder(new EmptyBorder(2, 0, 0, 0));

        cell.add(topRow);
        cell.add(lblDate);
        return cell;
    }

    // =========================================================================
    // LOGIC CHÍNH
    // =========================================================================

    /** Tìm kiếm phòng đang thuê theo SĐT */
    private void doSearch() {
        String keyword = searchField.getText().trim();

        try {
            List<ChiTietPhieuDatPhong> results =
                    service.timPhongDangThue(keyword.isEmpty() ? null : keyword);

            danhSachDangThue = results;
            selectedRows.clear();
            refreshExtendRows();
            loadRoomTable(results);

            if (results.isEmpty()) {
                statusLabel.setText("Không tìm thấy phòng.");
                statusLabel.setForeground(RED);
            } else {
                statusLabel.setText("Hiển thị " + results.size() + " phòng.");
                statusLabel.setForeground(BLUE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi tìm kiếm: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    /** Đổ dữ liệu thật vào bảng trái */
    private void loadRoomTable(List<ChiTietPhieuDatPhong> list) {
        roomTableModel.setRowCount(0);
        for (ChiTietPhieuDatPhong ct : list) {
            Phong p       = ct.getPhong();
            String soPhong  = (p != null) ? p.getSoPhong()  : "--";
            String loai     = (p != null && p.getLoaiPhong() != null)
                    ? p.getLoaiPhong().getTenLoaiPhong() : "--";
            String tang     = (p != null) ? "Tầng " + p.getTang() : "--";
            String ngayNhan = ct.getThoiGianNhanPhong() != null
                    ? ct.getThoiGianNhanPhong().format(FMT_DT) : "--";
            String ngayTra  = ct.getThoiGianTraPhong() != null
                    ? ct.getThoiGianTraPhong().format(FMT_DT) : "--";
            roomTableModel.addRow(new Object[]{ soPhong, loai, tang, ngayNhan, ngayTra });
        }
    }

    /** Xác nhận gia hạn → gọi service → thông báo */
    private void doGiaHan() {
        if (selectedRows.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn ít nhất một phòng để gia hạn.",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Validate thời gian
        for (RowModel m : selectedRows.values()) {
            LocalDateTime newEnd = m.tinhThoiGianTraMoi();

            if (newEnd == null) {
                JOptionPane.showMessageDialog(this,
                        "Thời gian gia hạn không hợp lệ.",
                        "Dữ liệu không hợp lệ", JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean ok = service.isRoomAvailableForExtension(m.chiTietId, newEnd);

            if (!ok) {
                JOptionPane.showMessageDialog(this,
                        "Phòng đã có khách đặt trong khoảng thời gian này!\nKhông thể gia hạn.",
                        "Trùng lịch", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Xác nhận gia hạn " + selectedRows.size() + " phòng?",
                "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            Map<Long, LocalDateTime> requests = new LinkedHashMap<>();
            for (RowModel m : selectedRows.values())
                requests.put(m.chiTietId, m.tinhThoiGianTraMoi());

            service.giaHanNhieu(requests);

            JOptionPane.showMessageDialog(this,
                    "Gia hạn thành công " + requests.size() + " phòng!",
                    "Thành công", JOptionPane.INFORMATION_MESSAGE);

            // Reset và load lại
            selectedRows.clear();
            doSearch();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi gia hạn: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // =========================================================================
    // HELPERS VẼ
    // =========================================================================
    private JLabel buildCheckIcon() {
        JLabel lbl = new JLabel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int s = Math.min(getWidth(), getHeight()) - 2;
                int ox = (getWidth() - s) / 2, oy = (getHeight() - s) / 2;
                g2.setColor(GREEN);
                g2.fillOval(ox, oy, s, s);
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                int cx = ox + s / 2, cy = oy + s / 2;
                g2.drawLine(cx - 4, cy, cx - 1, cy + 3);
                g2.drawLine(cx - 1, cy + 3, cx + 4, cy - 3);
                g2.dispose();
            }
        };
        lbl.setPreferredSize(new Dimension(22, 22));
        return lbl;
    }

    private JLabel buildCalendarIcon(Color color) {
        JLabel ico = new JLabel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(1.4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                int w = getWidth(), h = getHeight(), mx = 1, my = 2;
                g2.drawRoundRect(mx, my + 2, w - mx * 2, h - my - 3, 3, 3);
                g2.drawLine(w / 4, my - 1, w / 4, my + 3);
                g2.drawLine(3 * w / 4, my - 1, 3 * w / 4, my + 3);
                g2.drawLine(mx, my + 5, w - mx, my + 5);
                for (int r = 0; r < 2; r++)
                    for (int c = 0; c < 3; c++)
                        g2.fillOval(mx + 2 + c * (w - mx * 2 - 4) / 2, my + 8 + r * 4, 2, 2);
                g2.dispose();
            }
        };
        ico.setPreferredSize(new Dimension(14, 14));
        return ico;
    }

    private JSeparator buildRowSeparator() {
        JSeparator sep = new JSeparator(JSeparator.HORIZONTAL);
        sep.setForeground(BORDER_COL);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        return sep;
    }

    private JButton buildConfirmButton() {
        JButton btn = new JButton("Gia hạn ngay") {
            private boolean hovered = false;
            { addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { hovered = true;  repaint(); }
                @Override public void mouseExited(MouseEvent e)  { hovered = false; repaint(); }
            }); }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? PURPLE_DARK
                        : hovered ? new Color(0x6B7CFF) : PURPLE_BTN);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(F_BOLD14);
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(160, 44));
        btn.addActionListener(e -> doGiaHan());
        return btn;
    }

    private JButton buildBlueButton(String text, int w, int h) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? new Color(0x2A5CD4)
                        : getModel().isRollover() ? new Color(0x5580F5) : BLUE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(F_BOLD13);
        btn.setForeground(BG_WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(w, h));
        return btn;
    }

    private void styleTable(JTable table) {
        table.setFont(F_TABLE);
        table.setForeground(TEXT_DARK);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(BORDER_COL);
        table.setBackground(BG_WHITE);
        table.setSelectionBackground(BLUE_LIGHT);
        table.setSelectionForeground(BLUE);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFocusable(false);

        JTableHeader header = table.getTableHeader();
        header.setFont(F_TABLE_H);
        header.setForeground(TEXT_MID);
        header.setBackground(HEADER_BG);
        header.setBorder(new MatteBorder(0, 0, 1, 0, BORDER_COL));
        header.setPreferredSize(new Dimension(0, 38));
        ((DefaultTableCellRenderer) header.getDefaultRenderer())
                .setHorizontalAlignment(JLabel.CENTER);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++)
            table.getColumnModel().getColumn(i).setCellRenderer(center);
    }

    static class RoundedPanel extends JPanel {
        private final int   radius;
        private final Color bg;
        RoundedPanel(int radius, Color bg) {
            this.radius = radius; this.bg = bg; setOpaque(false);
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}