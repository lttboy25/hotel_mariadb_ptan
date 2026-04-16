package iuh.view;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

/**
 * QuanLyHoaDonPanel – Quản lý Hóa Đơn (chỉ xem, không thêm/xóa/sửa)
 *
 * Cột bảng được chọn lọc:
 *   Mã HĐ | Khách hàng | Nhân viên | Ngày tạo | Tổng tiền | Khuyến mãi | Trạng thái
 *   (Chi tiết phòng, dịch vụ → chỉ hiện trong modal)
 */
public class QuanLyHoaDonPanel extends JPanel {

    // ── màu / font dùng chung từ QuanLyNhanVienPanel ─────────────────────────
    private static final Color BG_MAIN    = QuanLyNhanVienPanel.BG_MAIN;
    private static final Color BG_WHITE   = QuanLyNhanVienPanel.BG_WHITE;
    private static final Color BLUE       = QuanLyNhanVienPanel.BLUE;
    private static final Color TEXT_DARK  = QuanLyNhanVienPanel.TEXT_DARK;
    private static final Color TEXT_MID   = QuanLyNhanVienPanel.TEXT_MID;
    private static final Color TEXT_GRAY  = QuanLyNhanVienPanel.TEXT_GRAY;
    private static final Color BORDER_COL = QuanLyNhanVienPanel.BORDER_COL;
    private static final Color HEADER_BG  = QuanLyNhanVienPanel.HEADER_BG;
    private static final Color ROW_ALT    = QuanLyNhanVienPanel.ROW_ALT;
    private static final Color ROW_SEL    = QuanLyNhanVienPanel.ROW_SEL;
    private static final Color GREEN      = QuanLyNhanVienPanel.GREEN;
    private static final Color GREEN_BG   = QuanLyNhanVienPanel.GREEN_BG;
    private static final Color RED        = QuanLyNhanVienPanel.RED;
    private static final Color RED_BG     = QuanLyNhanVienPanel.RED_BG;
    private static final Color ORANGE     = QuanLyNhanVienPanel.ORANGE;
    private static final Color ORANGE_BG  = QuanLyNhanVienPanel.ORANGE_BG;

    private static final Font F_TITLE   = QuanLyNhanVienPanel.F_TITLE;
    private static final Font F_LABEL   = QuanLyNhanVienPanel.F_LABEL;
    private static final Font F_BOLD13  = QuanLyNhanVienPanel.F_BOLD13;
    private static final Font F_TABLE   = QuanLyNhanVienPanel.F_TABLE;
    private static final Font F_TABLE_H = QuanLyNhanVienPanel.F_TABLE_H;
    private static final Font F_SMALL   = QuanLyNhanVienPanel.F_SMALL;

    // ── Dữ liệu mẫu ──────────────────────────────────────────────────────────
    // Cột: Mã HĐ | Khách hàng | Nhân viên | Ngày tạo | Tổng tiền | Khuyến mãi | Trạng thái
    private static final Object[][] DATA = {
        {"HD001", "Nguyễn Văn A",  "Trần Thị B", "10/03/2025 08:30", "2.300.000 đ", "KM001 - 10%", "Đã thanh toán"},
        {"HD002", "Lê Hoàng C",    "Vũ Thị L",   "11/03/2025 14:00", "1.500.000 đ", "-",           "Đã thanh toán"},
        {"HD003", "Phạm Thị D",    "Đỗ Quang H", "12/03/2025 09:15", "3.800.000 đ", "KM002 - 15%", "Chưa thanh toán"},
        {"HD004", "Hoàng Minh Đ",  "Bùi Thị M",  "12/03/2025 16:45", "900.000 đ",   "-",           "Đã thanh toán"},
        {"HD005", "Vũ Quốc T",     "Trần Thị B", "13/03/2025 10:00", "5.200.000 đ", "KM001 - 10%", "Đã thanh toán"},
        {"HD006", "Đinh Thị H",    "Đỗ Quang H", "14/03/2025 11:30", "1.200.000 đ", "-",           "Chưa thanh toán"},
    };

    private static final String[] COLS = {
        "Mã HĐ", "Khách hàng", "Nhân viên", "Ngày tạo", "Tổng tiền", "Khuyến mãi", "Trạng thái"
    };

    private DefaultTableModel tableModel;
    private JTextField tfSearch;

    public QuanLyHoaDonPanel() {
        setLayout(new BorderLayout());
        setBackground(BG_MAIN);
        setBorder(new EmptyBorder(28, 28, 28, 28));

        JLabel title = new JLabel("Quản lý hóa đơn");
        title.setFont(F_TITLE);
        title.setForeground(TEXT_DARK);
        title.setBorder(new EmptyBorder(0, 0, 18, 0));
        add(title, BorderLayout.NORTH);

        JPanel card = new ThanhToanPanel.RoundedPanel(14, BG_WHITE);
        card.setLayout(new BorderLayout());
        card.add(buildToolbar(), BorderLayout.NORTH);
        card.add(buildTable(),   BorderLayout.CENTER);
        add(card, BorderLayout.CENTER);
    }

    // ── Toolbar: chỉ tìm kiếm, KHÔNG có nút Thêm ─────────────────────────────
    private JPanel buildToolbar() {
        JPanel bar = new JPanel(new BorderLayout(12, 0));
        bar.setOpaque(false);
        bar.setBorder(new EmptyBorder(16, 20, 12, 20));

        JPanel leftGroup = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        leftGroup.setOpaque(false);

        tfSearch = new JTextField(22);
        tfSearch.setFont(F_LABEL);
        tfSearch.setForeground(TEXT_DARK);
        tfSearch.setPreferredSize(new Dimension(240, 38));
        tfSearch.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COL, 1, true),
            new EmptyBorder(0, 12, 0, 12)
        ));
        setPlaceholder(tfSearch, "Tìm theo mã, khách hàng...");

        JButton btnSearch = QuanLyNhanVienPanel.createButton("Tìm kiếm", BLUE, new Color(0x2D5FD8), Color.WHITE, 100, 38);
        leftGroup.add(tfSearch);
        leftGroup.add(btnSearch);

        // Ghi chú chỉ-xem bên phải
        JLabel readOnly = new JLabel("📋  Chỉ xem – không thể chỉnh sửa hóa đơn");
        readOnly.setFont(F_SMALL);
        readOnly.setForeground(TEXT_GRAY);
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        right.setOpaque(false);
        right.add(readOnly);

        bar.add(leftGroup, BorderLayout.WEST);
        bar.add(right,     BorderLayout.EAST);

        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);
        wrap.add(bar, BorderLayout.CENTER);
        wrap.add(new JSeparator() {{ setForeground(BORDER_COL); }}, BorderLayout.SOUTH);
        return wrap;
    }

    private JScrollPane buildTable() {
        tableModel = new DefaultTableModel(DATA, COLS) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(tableModel);
        styleTable(table);
        table.setRowHeight(52);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Renderer chung
        DefaultTableCellRenderer base = new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                setBackground(sel ? ROW_SEL : (row % 2 == 0 ? BG_WHITE : ROW_ALT));
                setForeground(col == 0 ? BLUE : TEXT_DARK);
                setFont(col == 0 ? F_BOLD13 : (col == 4 ? F_BOLD13 : F_TABLE));
                if (col == 4) setForeground(sel ? TEXT_DARK : new Color(0xF39C12));
                setBorder(new EmptyBorder(0, 16, 0, 8));
                setHorizontalAlignment(col == 4 ? JLabel.RIGHT : JLabel.LEFT);
                return this;
            }
        };

        // Renderer trạng thái
        DefaultTableCellRenderer statusR = buildStatusRenderer(
            new String[]{"Đã thanh toán", "Chưa thanh toán"},
            new Color[]{GREEN, ORANGE},
            new Color[]{GREEN_BG, ORANGE_BG}
        );

        for (int i = 0; i < COLS.length - 1; i++)
            table.getColumnModel().getColumn(i).setCellRenderer(base);
        table.getColumnModel().getColumn(COLS.length - 1).setCellRenderer(statusR);

        int[] widths = {80, 150, 130, 150, 120, 160, 130};
        for (int i = 0; i < widths.length; i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        // Double-click → modal xem chi tiết (read-only)
        table.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row >= 0) {
                        Object[] rowData = new Object[tableModel.getColumnCount()];
                        for (int c = 0; c < rowData.length; c++)
                            rowData[c] = tableModel.getValueAt(row, c);
                        JFrame top = (JFrame) SwingUtilities.getWindowAncestor(QuanLyHoaDonPanel.this);
                        new HoaDonModal(top, rowData).setVisible(true);
                    }
                }
            }
        });
        table.setToolTipText("Double-click để xem chi tiết hóa đơn");

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setBackground(BG_WHITE);
        return sp;
    }

    // ── Helpers ──────────────────────────────────────────────────────────────
    private void styleTable(JTable t) {
        t.setFont(F_TABLE);
        t.setShowVerticalLines(false);
        t.setShowHorizontalLines(true);
        t.setGridColor(BORDER_COL);
        t.setBackground(BG_WHITE);
        t.setSelectionBackground(ROW_SEL);
        t.setSelectionForeground(TEXT_DARK);
        t.setIntercellSpacing(new Dimension(0, 0));
        t.setFocusable(false);
        t.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        t.getTableHeader().setFont(F_TABLE_H);
        t.getTableHeader().setForeground(TEXT_MID);
        t.getTableHeader().setBackground(HEADER_BG);
        t.getTableHeader().setBorder(new MatteBorder(0, 0, 1, 0, BORDER_COL));
        t.getTableHeader().setPreferredSize(new Dimension(0, 42));
        t.getTableHeader().setReorderingAllowed(false);
        ((DefaultTableCellRenderer) t.getTableHeader().getDefaultRenderer())
            .setHorizontalAlignment(JLabel.LEFT);
    }

    static DefaultTableCellRenderer buildStatusRenderer(
            String[] statuses, Color[] fgColors, Color[] bgColors) {
        return new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean foc, int row, int col) {
                String s = val == null ? "" : val.toString();
                JPanel cell = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
                cell.setBackground(sel ? ROW_SEL : (row % 2 == 0 ? BG_WHITE : ROW_ALT));
                cell.setBorder(new EmptyBorder(11, 16, 11, 8));

                Color bg = new Color(0xEEF0F5), fg = TEXT_MID;
                for (int i = 0; i < statuses.length; i++) {
                    if (s.equals(statuses[i])) { fg = fgColors[i]; bg = bgColors[i]; break; }
                }
                final Color finalBg = bg, finalFg = fg;

                JLabel pill = new JLabel(s) {
                    @Override protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(finalBg);
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                        g2.dispose();
                        super.paintComponent(g);
                    }
                };
                pill.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                pill.setForeground(finalFg);
                pill.setOpaque(false);
                pill.setBorder(new EmptyBorder(3, 10, 3, 10));
                cell.add(pill);
                return cell;
            }
        };
    }

    private void setPlaceholder(JTextField tf, String ph) {
        tf.setText(ph);
        tf.setForeground(TEXT_GRAY);
        tf.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                if (tf.getText().equals(ph)) { tf.setText(""); tf.setForeground(TEXT_DARK); }
            }
            @Override public void focusLost(FocusEvent e) {
                if (tf.getText().isEmpty()) { tf.setText(ph); tf.setForeground(TEXT_GRAY); }
            }
        });
    }
}

// =============================================================================
//  MODAL CHI TIẾT HÓA ĐƠN – chỉ xem
// =============================================================================
class HoaDonModal extends JDialog {

    private static final Color BG_WHITE   = QuanLyNhanVienPanel.BG_WHITE;
    private static final Color BG_MAIN    = QuanLyNhanVienPanel.BG_MAIN;
    private static final Color BLUE       = QuanLyNhanVienPanel.BLUE;
    private static final Color BLUE_LIGHT = QuanLyNhanVienPanel.BLUE_LIGHT;
    private static final Color TEXT_DARK  = QuanLyNhanVienPanel.TEXT_DARK;
    private static final Color TEXT_MID   = QuanLyNhanVienPanel.TEXT_MID;
    private static final Color BORDER_COL = QuanLyNhanVienPanel.BORDER_COL;
    private static final Color ORANGE     = QuanLyNhanVienPanel.ORANGE;
    private static final Color ORANGE_BG  = QuanLyNhanVienPanel.ORANGE_BG;
    private static final Color GREEN      = QuanLyNhanVienPanel.GREEN;
    private static final Color GREEN_BG   = QuanLyNhanVienPanel.GREEN_BG;

    HoaDonModal(JFrame owner, Object[] data) {
        super(owner, "Chi tiết hóa đơn – " + data[0], true);
        setSize(560, 580);
        setLocationRelativeTo(owner);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_WHITE);
        root.add(buildHeader(data), BorderLayout.NORTH);
        root.add(buildBody(data),   BorderLayout.CENTER);
        root.add(buildFooter(),     BorderLayout.SOUTH);
        setContentPane(root);
    }

    private JPanel buildHeader(Object[] data) {
        JPanel h = new JPanel(new BorderLayout());
        h.setBackground(BG_WHITE);
        h.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, BORDER_COL),
            new EmptyBorder(18, 24, 16, 16)
        ));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        left.setOpaque(false);

        JLabel icon = new JLabel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(ORANGE_BG);
                g2.fillOval(0, 0, 44, 44);
                g2.setColor(ORANGE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 18));
                g2.drawString("₫", 13, 30);
                g2.dispose();
            }
        };
        icon.setPreferredSize(new Dimension(44, 44));

        JPanel txt = new JPanel();
        txt.setLayout(new BoxLayout(txt, BoxLayout.Y_AXIS));
        txt.setOpaque(false);
        JLabel t1 = new JLabel("Chi tiết hóa đơn");
        t1.setFont(new Font("Segoe UI", Font.BOLD, 16));
        t1.setForeground(TEXT_DARK);
        JLabel t2 = new JLabel("Mã: " + data[0] + "  •  " + data[3]);
        t2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        t2.setForeground(new Color(0xA0A8B8));
        txt.add(t1); txt.add(Box.createVerticalStrut(2)); txt.add(t2);

        left.add(icon); left.add(txt);

        JButton btnX = makeCloseBtn();
        h.add(left, BorderLayout.CENTER);
        h.add(btnX, BorderLayout.EAST);
        return h;
    }

    private JScrollPane buildBody(Object[] data) {
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(BG_WHITE);
        body.setBorder(new EmptyBorder(20, 24, 16, 24));

        // ── Section: Thông tin chung ──────────────────────────────────────────
        body.add(sectionTitle("Thông tin chung"));
        body.add(Box.createVerticalStrut(10));

        JPanel grid = new JPanel(new GridLayout(0, 2, 16, 10));
        grid.setOpaque(false);
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);

        addInfo(grid, "Mã hóa đơn",    safeStr(data, 0));
        addInfo(grid, "Ngày tạo",       safeStr(data, 3));
        addInfo(grid, "Khách hàng",     safeStr(data, 1));
        addInfo(grid, "Nhân viên lập",  safeStr(data, 2));
        addInfo(grid, "Khuyến mãi",     safeStr(data, 5));
        addInfo(grid, "Trạng thái",     safeStr(data, 6));
        body.add(grid);
        body.add(Box.createVerticalStrut(20));

        // ── Section: Chi tiết phòng ───────────────────────────────────────────
        body.add(sectionTitle("Chi tiết phòng"));
        body.add(Box.createVerticalStrut(10));
        body.add(buildInnerTable(
            new String[]{"Phòng", "Loại phòng", "Ngày nhận", "Ngày trả", "Thành tiền"},
            new Object[][]{
                {"101", "Thường",  "10/03/2025 14:00", "12/03/2025 12:00", "800.000 đ"},
                {"201", "VIP",     "10/03/2025 14:00", "12/03/2025 12:00", "1.200.000 đ"},
            }
        ));
        body.add(Box.createVerticalStrut(20));

        // ── Section: Dịch vụ kèm theo ────────────────────────────────────────
        body.add(sectionTitle("Dịch vụ sử dụng"));
        body.add(Box.createVerticalStrut(10));
        body.add(buildInnerTable(
            new String[]{"Dịch vụ", "Đơn vị tính", "Số lượng", "Đơn giá", "Thành tiền"},
            new Object[][]{
                {"Ăn sáng",    "Lần",    "2", "150.000 đ", "300.000 đ"},
                {"Giặt ủi",    "Kg",     "1", "50.000 đ",  "50.000 đ"},
            }
        ));
        body.add(Box.createVerticalStrut(16));

        // ── Tổng tiền ─────────────────────────────────────────────────────────
        JPanel total = new JPanel(new BorderLayout());
        total.setOpaque(false);
        total.setAlignmentX(Component.LEFT_ALIGNMENT);
        total.setBorder(new CompoundBorder(
            new MatteBorder(1, 0, 0, 0, BORDER_COL),
            new EmptyBorder(12, 0, 0, 0)
        ));
        JLabel k = new JLabel("Tổng thanh toán");
        k.setFont(new Font("Segoe UI", Font.BOLD, 14));
        k.setForeground(TEXT_DARK);
        JLabel v = new JLabel(safeStr(data, 4));
        v.setFont(new Font("Segoe UI", Font.BOLD, 18));
        v.setForeground(ORANGE);
        total.add(k, BorderLayout.WEST);
        total.add(v, BorderLayout.EAST);
        body.add(total);

        JScrollPane sp = new JScrollPane(body);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setBackground(BG_WHITE);
        return sp;
    }

    private JPanel buildFooter() {
        JPanel f = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        f.setBackground(BG_WHITE);
        f.setBorder(new MatteBorder(1, 0, 0, 0, BORDER_COL));
        JButton btnClose = QuanLyNhanVienPanel.createButton("Đóng", BLUE, new Color(0x2D5FD8), Color.WHITE, 90, 38);
        btnClose.addActionListener(e -> dispose());
        f.add(btnClose);
        return f;
    }

    // ── Mini table bên trong modal ───────────────────────────────────────────
    private JScrollPane buildInnerTable(String[] cols, Object[][] rows) {
        JTable t = new JTable(new DefaultTableModel(rows, cols) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        });
        t.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        t.setRowHeight(36);
        t.setShowVerticalLines(false);
        t.setShowHorizontalLines(true);
        t.setGridColor(BORDER_COL);
        t.setBackground(BG_WHITE);
        t.setFocusable(false);
        t.setEnabled(false);
        t.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JTableHeader h = t.getTableHeader();
        h.setFont(new Font("Segoe UI", Font.BOLD, 11));
        h.setForeground(TEXT_MID);
        h.setBackground(new Color(0xF8F9FD));
        h.setBorder(new MatteBorder(0, 0, 1, 0, BORDER_COL));
        h.setPreferredSize(new Dimension(0, 32));
        h.setReorderingAllowed(false);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        center.setBorder(new EmptyBorder(0, 8, 0, 8));
        for (int i = 0; i < cols.length; i++) t.getColumnModel().getColumn(i).setCellRenderer(center);

        JScrollPane sp = new JScrollPane(t);
        sp.setBorder(new LineBorder(BORDER_COL, 1, true));
        sp.setPreferredSize(new Dimension(0, rows.length * 36 + 34));
        sp.setMaximumSize(new Dimension(Integer.MAX_VALUE, rows.length * 36 + 34));
        sp.setAlignmentX(Component.LEFT_ALIGNMENT);
        return sp;
    }

    private JLabel sectionTitle(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 13));
        l.setForeground(BLUE);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private void addInfo(JPanel grid, String label, String value) {
        JPanel cell = new JPanel();
        cell.setLayout(new BoxLayout(cell, BoxLayout.Y_AXIS));
        cell.setOpaque(false);
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbl.setForeground(new Color(0xA0A8B8));
        JLabel val = new JLabel(value.isEmpty() ? "—" : value);
        val.setFont(new Font("Segoe UI", Font.BOLD, 13));
        val.setForeground(TEXT_DARK);
        cell.add(lbl); cell.add(Box.createVerticalStrut(2)); cell.add(val);
        grid.add(cell);
    }

    private JButton makeCloseBtn() {
        JButton b = new JButton("✕") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? new Color(0xFFF0F0) : BG_WHITE);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose(); super.paintComponent(g);
            }
        };
        b.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        b.setForeground(TEXT_MID);
        b.setContentAreaFilled(false); b.setBorderPainted(false); b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(30, 30));
        b.addActionListener(e -> dispose());
        return b;
    }

    private String safeStr(Object[] arr, int i) {
        return (arr != null && i < arr.length && arr[i] != null) ? arr[i].toString() : "";
    }
}
