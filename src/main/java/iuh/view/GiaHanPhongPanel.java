package iuh.view;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * GiaHanPhongPanel - Giao diện trang Gia Hạn Phòng
 *
 * Bố cục 2 cột:
 * - Trái : Tìm kiếm số điện thoại + bảng danh sách phòng đang thuê
 * - Phải : Bảng gia hạn (chọn loại gia hạn, hiển thị ngày trả & ngày gia hạn
 * đến) + nút xác nhận
 */
public class GiaHanPhongPanel extends JPanel {

    // ── Màu sắc ──────────────────────────────────────────────────────────────
    private static final Color BG_MAIN = new Color(0xF4F6FB);
    private static final Color BG_WHITE = Color.WHITE;
    private static final Color BLUE = new Color(0x3B6FF0);
    private static final Color BLUE_LIGHT = new Color(0xEBF0FF);
    private static final Color BLUE_ROW = new Color(0xE8EFFE);
    private static final Color TEXT_DARK = new Color(0x1A1A2E);
    private static final Color TEXT_MID = new Color(0x4A5268);
    private static final Color TEXT_GRAY = new Color(0xA0A8B8);
    private static final Color BORDER_COL = new Color(0xE4E9F2);
    private static final Color GREEN = new Color(0x3DBE8A);
    private static final Color GREEN_DARK = new Color(0x27A070);
    private static final Color GREEN_LIGHT = new Color(0xD6F5EB);
    private static final Color GRAY_TAG = new Color(0xBEC5D4);
    private static final Color GRAY_TAG_BG = new Color(0xEEF0F5);
    private static final Color PURPLE_BTN = new Color(0x5B6CF9);
    private static final Color PURPLE_DARK = new Color(0x4455E0);
    private static final Color HEADER_BG = new Color(0xF0F4FF);

    // ── Font ─────────────────────────────────────────────────────────────────
    private static final Font F_SECTION = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font F_LABEL = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font F_BOLD13 = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font F_BOLD14 = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font F_SMALL = new Font("Segoe UI", Font.PLAIN, 11);
    private static final Font F_SMALL_B = new Font("Segoe UI", Font.BOLD, 11);
    private static final Font F_TABLE = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font F_TABLE_H = new Font("Segoe UI", Font.BOLD, 12);

    // ── Dữ liệu mẫu bảng trái ────────────────────────────────────────────────
    private static final Object[][] ROOM_DATA = {
            { "#001", "Double bed", "Floor - 1", "300.000VND", "5.000.000VND" },
            { "#002", "Single bed", "Floor - 2", "400.000VND", "7.000.000VND" },
            { "#003", "VIP", "Floor - 1", "700.000VND", "8.000.000VND" },
            { "#005", "Single bed", "Floor - 1", "350.000VND", "5.000.000VND" },
    };
    private static final String[] ROOM_COLS = {
            "Số phòng", "Loại phòng", "Tầng", "Giá theo ngày", "Giá theo ngày"
    };

    // ── Dữ liệu mẫu bảng phải (các phòng đã chọn gia hạn) ───────────────────
    // { soPhong, loaiGiaHan, ngayTra, giaHanDen, thoiGian }
    private static final Object[][] EXTEND_DATA = {
            { "#002", "Theo giờ", "12:00 30/09/2025", "15:00 30/09/2025", "3 giờ" },
            { "#005", "Theo ngày", "30/09/2025", "01/01/10/2025", "1 ngày" },
    };
    private static final String[] EXTEND_COLS = {
            "Số phòng", "Loại gia hạn", "Ngày trả", "Gia hạn đến", "Thời gian gia hạn"
    };

    // Lưu dòng đang được chọn ở bảng trái
    private int selectedRoomRow = 1; // mặc định chọn #002

    public GiaHanPhongPanel() {
        setLayout(new BorderLayout(0, 0));
        setBackground(BG_MAIN);
        setBorder(new EmptyBorder(28, 28, 28, 28));

        // Chia 2 cột
        JPanel mainRow = new JPanel(new GridBagLayout());
        mainRow.setBackground(BG_MAIN);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridy = 0;
        gbc.weighty = 1.0;

        // Cột trái
        gbc.gridx = 0;
        gbc.weightx = 0.60;
        gbc.insets = new Insets(0, 0, 0, 20);
        mainRow.add(buildLeftPanel(), gbc);

        // Cột phải
        gbc.gridx = 1;
        gbc.weightx = 0.40;
        gbc.insets = new Insets(0, 0, 0, 0);
        mainRow.add(buildRightPanel(), gbc);

        add(mainRow, BorderLayout.CENTER);
    }

    // =========================================================================
    // PANEL TRÁI
    // =========================================================================
    private JPanel buildLeftPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 14));
        panel.setBackground(BG_MAIN);

        // Tiêu đề section
        JLabel title = new JLabel("Chọn phòng gia hạn");
        title.setFont(F_SECTION);
        title.setForeground(TEXT_DARK);
        panel.add(title, BorderLayout.NORTH);

        // Card chứa search + bảng
        JPanel card = new ThanhToanPanel.RoundedPanel(12, BG_WHITE);
        card.setLayout(new BorderLayout(0, 10));
        card.setBorder(new EmptyBorder(14, 14, 14, 14));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Thanh tìm kiếm
        card.add(buildSearchField(), BorderLayout.NORTH);

        // Bảng phòng
        card.add(buildRoomTable(), BorderLayout.CENTER);

        panel.add(card, BorderLayout.CENTER);
        return panel;
    }

    /** Ô tìm kiếm có icon kính lúp */
    private JPanel buildSearchField() {
        JPanel row = new JPanel(new BorderLayout(0, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        // Icon kính lúp (vẽ tay)
        JPanel iconWrap = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
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

        JTextField tf = new JTextField("083205000961");
        tf.setFont(F_LABEL);
        tf.setForeground(TEXT_DARK);
        tf.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
        tf.setOpaque(false);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(BG_WHITE);
        wrapper.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COL, 1, true),
                new EmptyBorder(0, 0, 0, 8)));
        wrapper.add(iconWrap, BorderLayout.WEST);
        wrapper.add(tf, BorderLayout.CENTER);

        row.add(wrapper, BorderLayout.CENTER);
        return row;
    }

    /** Bảng danh sách phòng bên trái */
    private JScrollPane buildRoomTable() {
        DefaultTableModel model = new DefaultTableModel(ROOM_DATA, ROOM_COLS) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        JTable table = new JTable(model) {
            @Override
            public Component prepareRenderer(TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                if (isRowSelected(row)) {
                    c.setBackground(BLUE_ROW);
                    c.setForeground(BLUE);
                } else {
                    c.setBackground(row % 2 == 0 ? BG_WHITE : new Color(0xFAFBFD));
                    c.setForeground(col == 0 ? BLUE : TEXT_DARK);
                }
                return c;
            }
        };

        styleTable(table);
        table.setRowHeight(54);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().setSelectionInterval(selectedRoomRow, selectedRoomRow);

        // Tỉ lệ độ rộng cột (tự scale theo chiều rộng panel)
        int[] widths = { 70, 110, 90, 120, 120 };
        for (int i = 0; i < widths.length; i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        // Số phòng bôi xanh
        table.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean sel, boolean focus, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, focus, row, col);
                setForeground(sel ? BLUE : BLUE);
                setFont(F_BOLD13);
                setHorizontalAlignment(JLabel.CENTER);
                setBackground(sel ? BLUE_ROW : (row % 2 == 0 ? BG_WHITE : new Color(0xFAFBFD)));
                return this;
            }
        });

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setBackground(BG_WHITE);
        // Không giới hạn cứng để bảng tự co giãn theo panel
        sp.setAlignmentX(Component.LEFT_ALIGNMENT);
        return sp;
    }

    // =========================================================================
    // PANEL PHẢI
    // =========================================================================
    private JPanel buildRightPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_MAIN);

        // Tiêu đề section
        JLabel title = new JLabel("Chọn thời gian gia hạn");
        title.setFont(F_SECTION);
        title.setForeground(TEXT_DARK);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createVerticalStrut(14));

        // Card chứa bảng gia hạn
        JPanel card = new ThanhToanPanel.RoundedPanel(12, BG_WHITE);
        card.setLayout(new BorderLayout());
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(buildExtendHeader(), BorderLayout.NORTH);
        card.add(buildExtendRows(), BorderLayout.CENTER);

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

    /** Header bảng gia hạn bên phải */
    private JPanel buildExtendHeader() {
        JPanel header = new JPanel(new GridLayout(1, 5, 0, 0));
        header.setBackground(HEADER_BG);
        header.setBorder(new EmptyBorder(10, 16, 10, 16));
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        String[] labels = { "Số phòng", "Loại gia hạn", "Ngày trả", "Gia hạn đến", "Thời gian gia hạn" };
        Color[] colors = { TEXT_MID, new Color(0x5B6CF9), TEXT_MID, GREEN, TEXT_MID };

        for (int i = 0; i < labels.length; i++) {
            JLabel lbl = new JLabel(labels[i], JLabel.CENTER);
            lbl.setFont(F_TABLE_H);
            lbl.setForeground(colors[i]);
            header.add(lbl);
        }
        return header;
    }

    /** Các dòng dữ liệu gia hạn (vẽ custom để có badge, dropdown, v.v.) */
    private JPanel buildExtendRows() {
        JPanel rows = new JPanel();
        rows.setLayout(new BoxLayout(rows, BoxLayout.Y_AXIS));
        rows.setBackground(BG_WHITE);

        // Dòng 1: #002 – Theo giờ
        rows.add(buildExtendRow(
                "#002", "Theo giờ",
                "Ngày trả phòng", "12:00 30/09/2025",
                "Gia hạn đến", "15:00 30/09/2025",
                "3 giờ", false));

        rows.add(buildRowSeparator());

        // Dòng 2: #005 – Theo ngày
        rows.add(buildExtendRow(
                "#005", "Theo ngày",
                "Ngày trả phòng", "30/09/2025",
                "Gia hạn đến", "01/10/2025",
                "1 ngày", false));

        return rows;
    }

    /**
     * Xây dựng 1 dòng gia hạn gồm:
     * [✓ soPhong] [dropdown loai] [icon + nhanNgayTra + ngayTra] [icon + nhanDen +
     * ngayDen] [thoiGian]
     */
    private JPanel buildExtendRow(String soPhong, String loaiGiaHan,
            String nhanTra, String ngayTra,
            String nhanDen, String ngayDen,
            String thoiGian, boolean selected) {
        JPanel row = new JPanel(new GridLayout(1, 5, 8, 0));
        row.setBackground(BG_WHITE);
        row.setBorder(new EmptyBorder(14, 16, 14, 16));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        // ── Cột 1: checkbox check + số phòng ──
        JPanel col1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
        col1.setOpaque(false);
        col1.add(buildCheckIcon(true)); // mặc định đã check
        JLabel lblRoom = new JLabel(soPhong);
        lblRoom.setFont(F_BOLD13);
        lblRoom.setForeground(TEXT_DARK);
        col1.add(lblRoom);
        row.add(col1);

        // ── Cột 2: dropdown loại gia hạn ──
        JPanel col2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        col2.setOpaque(false);
        col2.add(buildTypeDropdown(loaiGiaHan));
        row.add(col2);

        // ── Cột 3: ngày trả phòng ──
        row.add(buildDateCell(nhanTra, ngayTra, false));

        // ── Cột 4: gia hạn đến ──
        row.add(buildDateCell(nhanDen, ngayDen, true));

        // ── Cột 5: thời gian gia hạn ──
        JPanel col5 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        col5.setOpaque(false);
        JLabel lblTime = new JLabel(thoiGian);
        lblTime.setFont(F_BOLD13);
        lblTime.setForeground(TEXT_MID);
        col5.add(lblTime);
        row.add(col5);

        return row;
    }

    /** Icon dấu check tròn xanh */
    private JLabel buildCheckIcon(boolean checked) {
        JLabel lbl = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int s = Math.min(getWidth(), getHeight()) - 2;
                int ox = (getWidth() - s) / 2, oy = (getHeight() - s) / 2;
                g2.setColor(checked ? GREEN : BORDER_COL);
                g2.fillOval(ox, oy, s, s);
                // Dấu tick
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

    /** Dropdown chọn loại gia hạn (Theo giờ / Theo ngày) */
    private JPanel buildTypeDropdown(String selected) {
        JPanel pill = new JPanel(new BorderLayout(4, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
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

        JLabel text = new JLabel(selected);
        text.setFont(F_BOLD13);
        text.setForeground(TEXT_MID);

        // Mũi tên xuống
        JLabel arrow = new JLabel("▾");
        arrow.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        arrow.setForeground(TEXT_GRAY);

        pill.add(text, BorderLayout.CENTER);
        pill.add(arrow, BorderLayout.EAST);

        // Tooltip gợi ý
        pill.setToolTipText("Nhấn để thay đổi loại gia hạn");

        return pill;
    }

    /**
     * Ô ngày (có icon lịch + nhãn + ngày)
     * 
     * @param label   Nhãn trên ("Ngày trả phòng" / "Gia hạn đến")
     * @param date    Giá trị ngày
     * @param isGreen Nếu true thì tô nền xanh lá (ô "Gia hạn đến")
     */
    private JPanel buildDateCell(String label, String date, boolean isGreen) {
        JPanel cell = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(isGreen ? GREEN : BG_WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                if (!isGreen) {
                    g2.setColor(BORDER_COL);
                    g2.setStroke(new BasicStroke(1f));
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        cell.setLayout(new BoxLayout(cell, BoxLayout.Y_AXIS));
        cell.setOpaque(false);
        cell.setBorder(new EmptyBorder(8, 12, 8, 12));

        Color textColor = isGreen ? Color.WHITE : TEXT_MID;
        Color dateColor = isGreen ? Color.WHITE : TEXT_DARK;

        // Hàng đầu: icon lịch + nhãn
        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        topRow.setOpaque(false);
        topRow.add(buildCalendarIcon(textColor));
        JLabel lblName = new JLabel(label);
        lblName.setFont(F_SMALL);
        lblName.setForeground(textColor);
        topRow.add(lblName);
        topRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Hàng 2: giá trị ngày
        JLabel lblDate = new JLabel(date);
        lblDate.setFont(F_BOLD13);
        lblDate.setForeground(dateColor);
        lblDate.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblDate.setBorder(new EmptyBorder(2, 0, 0, 0));

        cell.add(topRow);
        cell.add(lblDate);

        return cell;
    }

    /** Icon lịch nhỏ (vẽ tay) */
    private JLabel buildCalendarIcon(Color color) {
        JLabel ico = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(1.4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                int w = getWidth(), h = getHeight();
                int mx = 1, my = 2;
                // Khung lịch
                g2.drawRoundRect(mx, my + 2, w - mx * 2, h - my - 3, 3, 3);
                // 2 cọc trên
                g2.drawLine(w / 4, my - 1, w / 4, my + 3);
                g2.drawLine(3 * w / 4, my - 1, 3 * w / 4, my + 3);
                // Đường ngang header
                g2.drawLine(mx, my + 5, w - mx, my + 5);
                // Chấm ngày (2x2 grid)
                for (int r = 0; r < 2; r++)
                    for (int c = 0; c < 3; c++)
                        g2.fillOval(mx + 2 + c * (w - mx * 2 - 4) / 2,
                                my + 8 + r * 4, 2, 2);
                g2.dispose();
            }
        };
        ico.setPreferredSize(new Dimension(14, 14));
        return ico;
    }

    /** Đường kẻ phân cách giữa các dòng */
    private JSeparator buildRowSeparator() {
        JSeparator sep = new JSeparator(JSeparator.HORIZONTAL);
        sep.setForeground(BORDER_COL);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        return sep;
    }

    /** Nút "Gia hạn ngay" màu tím */
    private JButton buildConfirmButton() {
        JButton btn = new JButton("Gia hạn ngay") {
            private boolean hovered = false;
            {
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        hovered = true;
                        repaint();
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        hovered = false;
                        repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = getModel().isPressed() ? PURPLE_DARK
                        : hovered ? new Color(0x6B7CFF)
                                : PURPLE_BTN;
                g2.setColor(bg);
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
        btn.setPreferredSize(new Dimension(180, 46));
        return btn;
    }

    // ── Helper: style chung cho JTable ────────────────────────────────────────
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

        // Căn giữa tất cả các ô
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++)
            table.getColumnModel().getColumn(i).setCellRenderer(center);
    }
}