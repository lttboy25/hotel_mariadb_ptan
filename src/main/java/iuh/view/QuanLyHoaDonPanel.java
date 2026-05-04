package iuh.view;

import iuh.dto.HoaDonDTO;
import iuh.enums.TrangThaiHoaDon;
import iuh.network.ClientConnection;
import iuh.network.CommandType;
import iuh.network.Request;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * QuanLyHoaDonPanel – Quản lý Hóa Đơn (chỉ xem, không thêm/xóa/sửa)
 * <p>
 * - Tìm kiếm theo mã HĐ và tên khách hàng
 * - Lọc theo khoảng ngày tạo
 * - Lọc theo trạng thái
 * - Double-click xem chi tiết (read-only)
 * - Nút Làm mới reset toàn bộ filter
 */
public class QuanLyHoaDonPanel extends JPanel {

    // ═══════════════════════════════════════════════════════════════════
    // MÀU SẮC & FONT
    // ═══════════════════════════════════════════════════════════════════
    private static final Color BG_MAIN = QuanLyNhanVienPanel.BG_MAIN;
    private static final Color BG_WHITE = QuanLyNhanVienPanel.BG_WHITE;
    private static final Color BLUE = QuanLyNhanVienPanel.BLUE;
    private static final Color TEXT_DARK = QuanLyNhanVienPanel.TEXT_DARK;
    private static final Color TEXT_MID = QuanLyNhanVienPanel.TEXT_MID;
    private static final Color TEXT_GRAY = QuanLyNhanVienPanel.TEXT_GRAY;
    private static final Color BORDER_COL = QuanLyNhanVienPanel.BORDER_COL;
    private static final Color HEADER_BG = QuanLyNhanVienPanel.HEADER_BG;
    private static final Color ROW_SEL = QuanLyNhanVienPanel.ROW_SEL;
    private static final Color GREEN = QuanLyNhanVienPanel.GREEN;
    private static final Color GREEN_BG = QuanLyNhanVienPanel.GREEN_BG;
    private static final Color ORANGE = QuanLyNhanVienPanel.ORANGE;
    private static final Color ORANGE_BG = QuanLyNhanVienPanel.ORANGE_BG;

    private static final Font F_TITLE = QuanLyNhanVienPanel.F_TITLE;
    private static final Font F_LABEL = QuanLyNhanVienPanel.F_LABEL;
    private static final Font F_BOLD13 = QuanLyNhanVienPanel.F_BOLD13;
    private static final Font F_TABLE = QuanLyNhanVienPanel.F_TABLE;
    private static final Font F_TABLE_H = QuanLyNhanVienPanel.F_TABLE_H;

    private static final String SEARCH_PH = "Tìm theo mã HĐ, tên khách hàng...";
    private static final String DATE_PH = "dd/MM/yyyy";
    private static final DateTimeFormatter DTF_DISPLAY = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter DTF_DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // ═══════════════════════════════════════════════════════════════════
    // COMPONENTS
    // ═══════════════════════════════════════════════════════════════════
    private JTextField tfSearch;
    private JTextField tfTuNgay;
    private JTextField tfDenNgay;
    private JComboBox<String> cbTrangThai;
    private DefaultTableModel tableModel;
    private JTable table;

    private final NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
    private final ClientConnection client = ClientConnection.getInstance();

    // ═══════════════════════════════════════════════════════════════════
    // KHỞI TẠO
    // ═══════════════════════════════════════════════════════════════════
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
        card.add(buildTable(), BorderLayout.CENTER);
        add(card, BorderLayout.CENTER);

    }

    // ═══════════════════════════════════════════════════════════════════
    // TOOLBAR
    // ═══════════════════════════════════════════════════════════════════
    private JPanel buildToolbar() {
        // ── Hàng 1: tìm kiếm + Tìm / Làm mới ──
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        row1.setOpaque(false);

        tfSearch = new JTextField(26);
        tfSearch.setFont(F_LABEL);
        tfSearch.setPreferredSize(new Dimension(280, 38));
        tfSearch.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COL, 1, true),
                new EmptyBorder(0, 12, 0, 12)));
        setPlaceholder(tfSearch, SEARCH_PH);
        tfSearch.addActionListener(e -> search());

        JButton btnSearch = QuanLyNhanVienPanel.createButton(
                "Tìm kiếm", BLUE, new Color(0x2D5FD8), Color.WHITE, 100, 38);
        btnSearch.addActionListener(e -> search());

        JButton btnReset = QuanLyNhanVienPanel.createButton(
                "Làm mới", new Color(0xEEF0F5), new Color(0xDDE0E8), TEXT_MID, 95, 38);
        btnReset.addActionListener(e -> resetAndLoadAll());

        row1.add(tfSearch);
        row1.add(btnSearch);
        row1.add(btnReset);

        // ── Hàng 2: filter ngày + trạng thái + Lọc ──
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        row2.setOpaque(false);

        row2.add(lbl("Từ ngày:"));
        tfTuNgay = buildDateField();
        row2.add(tfTuNgay);

        row2.add(lbl("Đến ngày:"));
        tfDenNgay = buildDateField();
        row2.add(tfDenNgay);

        row2.add(Box.createHorizontalStrut(8));
        row2.add(lbl("Trạng thái:"));

        cbTrangThai = new JComboBox<>(
                new String[]{"Tất cả", "Đã thanh toán", "Chưa thanh toán"});
        cbTrangThai.setFont(F_LABEL);
        cbTrangThai.setBackground(BG_WHITE);
        cbTrangThai.setForeground(TEXT_DARK);
        cbTrangThai.setPreferredSize(new Dimension(170, 38));
        cbTrangThai.setBorder(new LineBorder(BORDER_COL, 1, true));
        cbTrangThai.setFocusable(false);
        row2.add(cbTrangThai);

        JButton btnLoc = QuanLyNhanVienPanel.createButton(
                "Lọc", BLUE, new Color(0x2D5FD8), Color.WHITE, 80, 38);
        btnLoc.addActionListener(e -> search());
        row2.add(btnLoc);

        // Gộp 2 hàng
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));
        filterPanel.setOpaque(false);
        filterPanel.setBorder(new EmptyBorder(16, 20, 12, 20));
        filterPanel.add(row1);
        filterPanel.add(Box.createVerticalStrut(8));
        filterPanel.add(row2);

        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);
        wrap.add(filterPanel, BorderLayout.CENTER);
        wrap.add(new JSeparator() {{
            setForeground(BORDER_COL);
        }}, BorderLayout.SOUTH);
        return wrap;
    }

    // ═══════════════════════════════════════════════════════════════════
    // BẢNG
    // ═══════════════════════════════════════════════════════════════════
    private JScrollPane buildTable() {
        tableModel = new DefaultTableModel(
                new Object[0][0],
                new String[]{"Mã HĐ", "Khách hàng", "Nhân viên", "Ngày tạo", "Tổng tiền", "Trạng thái"}
        ) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(tableModel);
        styleTable(table);
        table.setRowHeight(52);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Renderer chung cột 0-4
        DefaultTableCellRenderer base = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                setBackground(sel ? ROW_SEL : BG_WHITE);
                setBorder(new EmptyBorder(0, 16, 0, 8));
                setHorizontalAlignment(col == 4 ? JLabel.RIGHT : JLabel.LEFT);
                switch (col) {
                    case 0 -> {
                        setForeground(BLUE);
                        setFont(F_BOLD13);
                    }
                    case 4 -> {
                        setForeground(sel ? TEXT_DARK : ORANGE);
                        setFont(F_BOLD13);
                    }
                    default -> {
                        setForeground(TEXT_DARK);
                        setFont(F_TABLE);
                    }
                }
                return this;
            }
        };

        // Renderer pill trạng thái
        DefaultTableCellRenderer trangThaiR = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean foc, int row, int col) {
                String text = val == null ? "" : val.toString();
                JPanel cell = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
                cell.setBackground(sel ? ROW_SEL : BG_WHITE);
                cell.setBorder(new EmptyBorder(11, 16, 11, 8));

                boolean ok = "Đã thanh toán".equals(text);
                Color bg = ok ? GREEN_BG : ORANGE_BG;
                Color fg = ok ? GREEN : ORANGE;

                JLabel pill = new JLabel(text) {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(bg);
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                        g2.dispose();
                        super.paintComponent(g);
                    }
                };
                pill.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                pill.setForeground(fg);
                pill.setOpaque(false);
                pill.setBorder(new EmptyBorder(3, 10, 3, 10));
                cell.add(pill);
                return cell;
            }
        };

        for (int i = 0; i < 5; i++)
            table.getColumnModel().getColumn(i).setCellRenderer(base);
        table.getColumnModel().getColumn(5).setCellRenderer(trangThaiR);

        int[] widths = {90, 160, 140, 160, 130, 130};
        for (int i = 0; i < widths.length; i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        // Double-click → modal chi tiết
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row >= 0)
                        openDetail(String.valueOf(tableModel.getValueAt(row, 0)));
                }
            }
        });
        table.setToolTipText("Double-click để xem chi tiết hóa đơn");

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setBackground(BG_WHITE);
        return sp;
    }

    // ═══════════════════════════════════════════════════════════════════
    // GỌI SERVICE QUA SOCKET
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Load tất cả — gọi khi khởi tạo hoặc Làm mới
     */
    @SuppressWarnings("unchecked")
    private void loadAll() {
        List<HoaDonDTO> ds = (List<HoaDonDTO>) client
                .sendRequest(Request.builder()
                        .commandType(CommandType.GET_ALL_HOA_DON)
                        .build())
                .getObject();

        fillTable(ds);
    }

    /**
     * Tìm kiếm có filter — gọi khi nhấn Tìm kiếm / Lọc
     */
    @SuppressWarnings("unchecked")
    private void search() {
        Map<String, Object> params = new HashMap<>();
        params.put("keyword", getKeyword().isBlank() ? null : getKeyword());
        params.put("tuNgay", parseDate(tfTuNgay.getText().trim()));
        params.put("denNgay", parseDate(tfDenNgay.getText().trim()));
        params.put("trangThai", parseTrangThai((String) cbTrangThai.getSelectedItem()));

        List<HoaDonDTO> ds = (List<HoaDonDTO>) client
                .sendRequest(Request.builder()
                        .commandType(CommandType.SEARCH_HOA_DON)
                        .object(params)
                        .build())
                .getObject();
        fillTable(ds);
    }

    /**
     * Fetch chi tiết rồi mở modal
     */
    @SuppressWarnings("unchecked")
    private void openDetail(String maHD) {
        Map<String, Object> params = new HashMap<>();
        params.put("keyword", maHD);
        params.put("tuNgay", null);
        params.put("denNgay", null);
        params.put("trangThai", null);

        List<HoaDonDTO> ds = (List<HoaDonDTO>) client
                .sendRequest(Request.builder()
                        .commandType(CommandType.SEARCH_HOA_DON)
                        .object(params)
                        .build())
                .getObject();

        if (ds == null) return;
        ds.stream()
                .filter(h -> maHD.equals(h.getMaHoaDon()))
                .findFirst()
                .ifPresent(hd -> {
                    JFrame top = (JFrame) SwingUtilities.getWindowAncestor(this);
                    new HoaDonDetailModal(top, hd).setVisible(true);
                });
    }

    /**
     * Reset toàn bộ filter rồi load lại
     */
    private void resetAndLoadAll() {
        tfSearch.setText(SEARCH_PH);
        tfSearch.setForeground(TEXT_GRAY);
        tfTuNgay.setText(DATE_PH);
        tfTuNgay.setForeground(TEXT_GRAY);
        tfDenNgay.setText(DATE_PH);
        tfDenNgay.setForeground(TEXT_GRAY);
        cbTrangThai.setSelectedIndex(0);
        loadAll();
    }

    // ═══════════════════════════════════════════════════════════════════
    // NẠP DỮ LIỆU VÀO BẢNG
    // ═══════════════════════════════════════════════════════════════════
    private void fillTable(List<HoaDonDTO> ds) {
        tableModel.setRowCount(0);
        if (ds == null) return;
        for (HoaDonDTO hd : ds) {
            tableModel.addRow(new Object[]{
                    hd.getMaHoaDon(),
                    hd.getKhachHang() != null ? hd.getKhachHang().getTenKhachHang() : "—",
                    hd.getNhanVien() != null ? hd.getNhanVien().getTenNhanVien() : "—",
                    hd.getNgayTao() != null ? hd.getNgayTao().format(DTF_DISPLAY) : "—",
                    formatter.format(hd.getTongTien()) + " đ",
                    formatTrangThai(hd.getTrangThai())
            });
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    // HELPERS
    // ═══════════════════════════════════════════════════════════════════
    private String formatTrangThai(TrangThaiHoaDon tt) {
        if (tt == null) return "—";
        return tt == TrangThaiHoaDon.DA_THANH_TOAN ? "Đã thanh toán" : "Chưa thanh toán";
    }

    private TrangThaiHoaDon parseTrangThai(String s) {
        return switch (s) {
            case "Đã thanh toán" -> TrangThaiHoaDon.DA_THANH_TOAN;
            case "Chưa thanh toán" -> TrangThaiHoaDon.CHUA_THANH_TOAN;
            default -> null;
        };
    }

    private String getKeyword() {
        String s = tfSearch.getText().trim();
        return SEARCH_PH.equals(s) ? "" : s;
    }

    private LocalDate parseDate(String text) {
        if (text == null || text.isBlank() || DATE_PH.equals(text)) return null;
        try {
            return LocalDate.parse(text, DTF_DATE);
        } catch (Exception e) {
            return null;
        }
    }

    private JTextField buildDateField() {
        JTextField tf = new JTextField(10);
        tf.setFont(F_LABEL);
        tf.setPreferredSize(new Dimension(120, 38));
        tf.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COL, 1, true),
                new EmptyBorder(0, 10, 0, 10)));
        setPlaceholder(tf, DATE_PH);
        tf.addActionListener(e -> search());
        return tf;
    }

    private JLabel lbl(String text) {
        JLabel l = new JLabel(text);
        l.setFont(F_LABEL);
        l.setForeground(TEXT_MID);
        return l;
    }

    private void setPlaceholder(JTextField tf, String ph) {
        tf.setText(ph);
        tf.setForeground(TEXT_GRAY);
        tf.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (tf.getText().equals(ph)) {
                    tf.setText("");
                    tf.setForeground(TEXT_DARK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (tf.getText().isEmpty()) {
                    tf.setText(ph);
                    tf.setForeground(TEXT_GRAY);
                }
            }
        });
    }

    private void styleTable(JTable t) {
        t.setFont(F_TABLE);
        t.setShowVerticalLines(true);
        t.setShowHorizontalLines(true);
        t.setGridColor(BORDER_COL);
        t.setBackground(BG_WHITE);
        t.setSelectionBackground(ROW_SEL);
        t.setSelectionForeground(TEXT_DARK);
        t.setIntercellSpacing(new Dimension(1, 1));
        t.setFocusable(false);
        t.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTableHeader h = t.getTableHeader();
        h.setFont(F_TABLE_H);
        h.setForeground(TEXT_MID);
        h.setBackground(HEADER_BG);
        h.setBorder(new MatteBorder(0, 0, 1, 0, BORDER_COL));
        h.setPreferredSize(new Dimension(0, 42));
        h.setReorderingAllowed(false);
        ((DefaultTableCellRenderer) h.getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);
    }
}

// =============================================================================
//  MODAL CHI TIẾT HÓA ĐƠN – nhận thẳng HoaDonDTO, chỉ xem
// =============================================================================
class HoaDonDetailModal extends JDialog {

    private static final Color BG_WHITE = QuanLyNhanVienPanel.BG_WHITE;
    private static final Color BLUE = QuanLyNhanVienPanel.BLUE;
    private static final Color TEXT_DARK = QuanLyNhanVienPanel.TEXT_DARK;
    private static final Color TEXT_MID = QuanLyNhanVienPanel.TEXT_MID;
    private static final Color TEXT_GRAY = QuanLyNhanVienPanel.TEXT_GRAY;
    private static final Color BORDER_COL = QuanLyNhanVienPanel.BORDER_COL;
    private static final Color ORANGE = QuanLyNhanVienPanel.ORANGE;
    private static final Color ORANGE_BG = QuanLyNhanVienPanel.ORANGE_BG;
    private static final Color GREEN = QuanLyNhanVienPanel.GREEN;
    private static final Color GREEN_BG = QuanLyNhanVienPanel.GREEN_BG;

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private final NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));

    HoaDonDetailModal(JFrame owner, HoaDonDTO hd) {
        super(owner, "Chi tiết hóa đơn – " + hd.getMaHoaDon(), true);
        setSize(600, 640);
        setLocationRelativeTo(owner);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_WHITE);
        root.add(buildHeader(hd), BorderLayout.NORTH);
        root.add(buildBody(hd), BorderLayout.CENTER);
        root.add(buildFooter(), BorderLayout.SOUTH);
        setContentPane(root);
    }

    private JPanel buildHeader(HoaDonDTO hd) {
        JPanel h = new JPanel(new BorderLayout());
        h.setBackground(BG_WHITE);
        h.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 1, 0, BORDER_COL),
                new EmptyBorder(18, 24, 16, 16)));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        left.setOpaque(false);

        JLabel icon = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
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
        String ngay = hd.getNgayTao() != null ? hd.getNgayTao().format(DTF) : "—";
        JLabel t2 = new JLabel("Mã: " + hd.getMaHoaDon() + "  •  " + ngay);
        t2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        t2.setForeground(TEXT_GRAY);
        txt.add(t1);
        txt.add(Box.createVerticalStrut(2));
        txt.add(t2);

        left.add(icon);
        left.add(txt);
        h.add(left, BorderLayout.CENTER);
        h.add(makeCloseBtn(), BorderLayout.EAST);
        return h;
    }

    private JScrollPane buildBody(HoaDonDTO hd) {
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(BG_WHITE);
        body.setBorder(new EmptyBorder(20, 24, 16, 24));

        // Section 1: Thông tin chung
        body.add(sectionTitle("Thông tin chung"));
        body.add(Box.createVerticalStrut(10));
        JPanel g1 = infoGrid();
        addInfo(g1, "Mã hóa đơn", hd.getMaHoaDon());
        addInfo(g1, "Ngày tạo", hd.getNgayTao() != null ? hd.getNgayTao().format(DTF) : "—");
        addInfo(g1, "Khách hàng", hd.getKhachHang() != null ? hd.getKhachHang().getTenKhachHang() : "—");
        addInfo(g1, "Nhân viên lập", hd.getNhanVien() != null ? hd.getNhanVien().getTenNhanVien() : "—");
        addInfo(g1, "Khuyến mãi", hd.getKhuyenMai() != null ? hd.getKhuyenMai().getTenKhuyenMai() : "Không có");
        addInfo(g1, "Trạng thái", formatTrangThai(hd.getTrangThai()));
        body.add(g1);
        body.add(Box.createVerticalStrut(20));

        // Section 2: Chi tiết phòng
        body.add(sectionTitle("Chi tiết phòng"));
        body.add(Box.createVerticalStrut(10));
        if (hd.getChiTietHoaDon() != null && !hd.getChiTietHoaDon().isEmpty()) {
            String[] cols = {"Phòng", "Loại phòng", "Ngày tạo", "Thành tiền"};
            Object[][] rows = hd.getChiTietHoaDon().stream()
                    .map(ct -> new Object[]{
                            ct.getPhong() != null ? ct.getPhong().getSoPhong() : "—",
                            ct.getPhong() != null && ct.getPhong().getLoaiPhong() != null
                                    ? ct.getPhong().getLoaiPhong().getTenLoaiPhong() : "—",
                            ct.getNgayTao() != null ? ct.getNgayTao().format(DTF) : "—",
                            formatter.format(ct.getTongTien()) + " đ"
                    })
                    .toArray(Object[][]::new);
            body.add(buildInnerTable(cols, rows));
        } else {
            JLabel empty = new JLabel("Không có chi tiết phòng");
            empty.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            empty.setForeground(TEXT_GRAY);
            empty.setAlignmentX(Component.LEFT_ALIGNMENT);
            body.add(empty);
        }
        body.add(Box.createVerticalStrut(20));

        // Section 3: Thanh toán
        body.add(sectionTitle("Thông tin thanh toán"));
        body.add(Box.createVerticalStrut(10));
        JPanel g2 = infoGrid();
        addInfo(g2, "Tổng tiền", formatter.format(hd.getTongTien()) + " đ");
        addInfo(g2, "Tiền khách đưa", formatter.format(hd.getTienKhachDua()) + " đ");
        addInfo(g2, "Tiền thối lại", formatter.format(hd.getTienThoi()) + " đ");
        body.add(g2);
        body.add(Box.createVerticalStrut(16));

        // Tổng cộng highlight
        JPanel totalRow = new JPanel(new BorderLayout());
        totalRow.setOpaque(true);
        totalRow.setBackground(ORANGE_BG);
        totalRow.setBorder(new CompoundBorder(
                new LineBorder(new Color(0xF5CBA7), 1, true),
                new EmptyBorder(10, 16, 10, 16)));
        totalRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        totalRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lblNhan = new JLabel("Tổng thanh toán");
        lblNhan.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblNhan.setForeground(ORANGE);
        JLabel lblVal = new JLabel(formatter.format(hd.getTongTien()) + " đ");
        lblVal.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblVal.setForeground(ORANGE);
        lblVal.setHorizontalAlignment(JLabel.RIGHT);
        totalRow.add(lblNhan, BorderLayout.WEST);
        totalRow.add(lblVal, BorderLayout.EAST);
        body.add(totalRow);

        JScrollPane sp = new JScrollPane(body);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setBackground(BG_WHITE);
        return sp;
    }

    private JPanel buildFooter() {
        JPanel f = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        f.setBackground(BG_WHITE);
        f.setBorder(new MatteBorder(1, 0, 0, 0, BORDER_COL));
        JButton btn = QuanLyNhanVienPanel.createButton(
                "Đóng", BLUE, new Color(0x2D5FD8), Color.WHITE, 90, 38);
        btn.addActionListener(e -> dispose());
        f.add(btn);
        return f;
    }

    private JScrollPane buildInnerTable(String[] cols, Object[][] rows) {
        JTable t = new JTable(new DefaultTableModel(rows, cols) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
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
        for (int i = 0; i < cols.length; i++)
            t.getColumnModel().getColumn(i).setCellRenderer(center);
        int tableH = rows.length * 36 + 34;
        JScrollPane sp = new JScrollPane(t);
        sp.setBorder(new LineBorder(BORDER_COL, 1, true));
        sp.setPreferredSize(new Dimension(0, tableH));
        sp.setMaximumSize(new Dimension(Integer.MAX_VALUE, tableH));
        sp.setAlignmentX(Component.LEFT_ALIGNMENT);
        return sp;
    }

    private JPanel infoGrid() {
        JPanel g = new JPanel(new GridLayout(0, 2, 16, 12));
        g.setOpaque(false);
        g.setAlignmentX(Component.LEFT_ALIGNMENT);
        return g;
    }

    private void addInfo(JPanel grid, String label, String value) {
        JPanel cell = new JPanel();
        cell.setLayout(new BoxLayout(cell, BoxLayout.Y_AXIS));
        cell.setOpaque(false);
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbl.setForeground(TEXT_GRAY);
        JLabel val = new JLabel(value == null || value.isBlank() ? "—" : value);
        val.setFont(new Font("Segoe UI", Font.BOLD, 13));
        val.setForeground(TEXT_DARK);
        cell.add(lbl);
        cell.add(Box.createVerticalStrut(2));
        cell.add(val);
        grid.add(cell);
    }

    private JLabel sectionTitle(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 13));
        l.setForeground(BLUE);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private String formatTrangThai(TrangThaiHoaDon tt) {
        if (tt == null) return "—";
        return tt == TrangThaiHoaDon.DA_THANH_TOAN ? "Đã thanh toán" : "Chưa thanh toán";
    }

    private JButton makeCloseBtn() {
        JButton b = new JButton("✕") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? new Color(0xFFF0F0) : BG_WHITE);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        b.setForeground(TEXT_MID);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(30, 30));
        b.addActionListener(e -> dispose());
        return b;
    }
}