package iuh.view;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import iuh.dto.*;
import iuh.network.ClientConnection;
import iuh.network.CommandType;
import iuh.network.Request;
import iuh.network.Response;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ThanhToanPanel - Giao diện trang Thanh Toán
 * <p>
 * Quy trình:
 * B1: Nhập CCCD → Tìm phòng cần thanh toán
 * B2: Tick checkbox chọn 1 hoặc nhiều phòng cần thanh toán
 * B3: Chọn phương thức thanh toán (Tiền mặt / Momo)
 * B4: Nhấn nút Thanh Toán
 */
public class ThanhToanPanel extends JPanel {

    // ═══════════════════════════════════════════════════════════════════
    // HẰNG SỐ MÀU SẮC
    // ═══════════════════════════════════════════════════════════════════
    private static final Color BG_MAIN = new Color(0xF4F6FB);
    private static final Color BG_WHITE = Color.WHITE;
    private static final Color BLUE = new Color(0x3B6FF0);
    private static final Color BLUE_LIGHT = new Color(0xEBF0FF);
    private static final Color TEXT_DARK = new Color(0x1A1A2E);
    private static final Color TEXT_MID = new Color(0x4A5268);
    private static final Color TEXT_GRAY = new Color(0xA0A8B8);
    private static final Color BORDER_COL = new Color(0xE4E9F2);
    private static final Color GREEN = new Color(0x27AE60);
    private static final Color GREEN_DARK = new Color(0x1E8449);
    private static final Color ORANGE = new Color(0xF39C12);

    // ═══════════════════════════════════════════════════════════════════
    // HẰNG SỐ FONT
    // ═══════════════════════════════════════════════════════════════════
    private static final Font F_TITLE = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font F_LABEL = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font F_BOLD13 = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font F_BOLD14 = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font F_BOLD16 = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font F_TABLE = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font F_TABLE_H = new Font("Segoe UI", Font.BOLD, 13);

    // ═══════════════════════════════════════════════════════════════════
    // DỮ LIỆU / CẤU HÌNH BẢNG
    // Cột 0: Checkbox (Boolean) | Cột 1-4: thông tin phòng
    // ═══════════════════════════════════════════════════════════════════
    // Header cột 0 để trống — sẽ thay bằng JCheckBox "chọn tất cả"
    private static final String[] COT_BANG = {
            "", "Phòng", "Loại Phòng", "Thời gian lưu trú", "Tổng tiền"
    };

    private static final double[] MENH_GIA_NHANH = {
            10_000, 30_000, 50_000, 100_000, 200_000, 300_000, 500_000
    };

    // ═══════════════════════════════════════════════════════════════════
    // BIẾN TOÀN CỤC
    // ═══════════════════════════════════════════════════════════════════

    // ── LEFT PANEL - Tìm kiếm & Bảng
    private JTextField tfCCCD;
    private JButton btnTimKiem;
    private JTable bangPhong;
    private DefaultTableModel modelBang;
    private JCheckBox chkTatCa; // Checkbox "chọn tất cả" trên header

    // ── LEFT PANEL - Tổng kết
    private JLabel lblTongTien, lblKhuyenMai, lblVAT, lblTotal;
    private JLabel lblSoPhongChon; // Hiển thị "Đang chọn X phòng"

    // ── RIGHT PANEL - Thanh Toán
    private JRadioButton rbTienMat, rbMomo;
    private JPanel panelTienMat, panelQR;
    private JButton[] btnMenhGia;
    private JTextField tfKhachDua;
    private JLabel lblTienThua;
    private JButton btnThanhToan;

    // Danh sách song song với bảng (index i <=> row i)
    private List<ChiTietPhieuDatPhongDTO> danhSachPhong = new ArrayList<>();

    // Danh sách các phòng đang được TICK — dùng khi thanh toán
    private List<ChiTietPhieuDatPhongDTO> listThanhToan = new ArrayList<>();

    private NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
    private double tongTienPhong = 0.0;
    private double tienKhachDua = 0.0;
    private double tienThua = 0.0;
    private double tongTien = 0.0;

    // ── Khuyến mãi
    private KhuyenMaiDTO selectedKhuyenMai = null;
    private JLabel lblKhuyenMaiTen; // hiển thị tên KM đã chọn bên summary
    private JButton btnChonKhuyenMai; // nút xanh mở modal

    private ClientConnection clientConnection = ClientConnection.getInstance();

    // ═══════════════════════════════════════════════════════════════════
    // KHỞI TẠO
    // ═══════════════════════════════════════════════════════════════════
    public ThanhToanPanel() {
        setLayout(new BorderLayout());
        setBackground(BG_MAIN);
        setBorder(new EmptyBorder(28, 28, 28, 28));

        JPanel mainRow = new JPanel(new BorderLayout(16, 0));
        mainRow.setBackground(BG_MAIN);

        JPanel rightPanel = buildRightPanel();
        rightPanel.setPreferredSize(new Dimension(360, 0));
        rightPanel.setMinimumSize(new Dimension(360, 0));
        rightPanel.setMaximumSize(new Dimension(360, Integer.MAX_VALUE));

        mainRow.add(buildLeftPanel(), BorderLayout.CENTER);
        mainRow.add(rightPanel, BorderLayout.EAST);

        add(mainRow, BorderLayout.CENTER);
    }

    // ═══════════════════════════════════════════════════════════════════
    // PANEL TRÁI
    // ═══════════════════════════════════════════════════════════════════
    private JPanel buildLeftPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_MAIN);

        panel.add(buildSearchBar());
        panel.add(Box.createVerticalStrut(18));
        panel.add(buildTableCard());
        panel.add(Box.createVerticalStrut(12));
        panel.add(buildSummaryCard());

        return panel;
    }

    private JPanel buildSearchBar() {
        JPanel card = new RoundedPanel(12, BG_WHITE);
        card.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 12));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 62));

        tfCCCD = new JTextField(22);
        tfCCCD.setFont(F_LABEL);
        tfCCCD.setText("Nhập CCCD");
        tfCCCD.setForeground(TEXT_GRAY);
        tfCCCD.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COL, 1, true),
                new EmptyBorder(6, 12, 6, 12)));
        tfCCCD.setPreferredSize(new Dimension(220, 36));
        tfCCCD.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (tfCCCD.getText().equals("Nhập CCCD")) {
                    tfCCCD.setText("");
                    tfCCCD.setForeground(TEXT_DARK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (tfCCCD.getText().isEmpty()) {
                    tfCCCD.setText("Nhập CCCD");
                    tfCCCD.setForeground(TEXT_GRAY);
                }
            }
        });
        // Nhấn Enter trong ô CCCD = nhấn nút Tìm kiếm
        tfCCCD.addActionListener(e -> timKiem());

        btnTimKiem = buildBlueButton("Tìm kiếm", 100, 36);
        btnTimKiem.addActionListener(e -> timKiem());

        // Nút Làm mới: xoá ô CCCD và reset toàn bộ bảng
        JButton btnLamMoi = buildOutlineIconButton("Làm mới", 100, 36);
        btnLamMoi.addActionListener(e -> refresh());

        card.add(tfCCCD);
        card.add(btnTimKiem);
        card.add(btnLamMoi);
        return card;
    }

    /**
     * Tìm kiếm phòng theo CCCD đang nhập trong tfCCCD.
     * Xoá kết quả cũ → gọi service → nạp vào bảng.
     */
    private void timKiem() {
        String cccd = tfCCCD.getText().trim();

        // Không tìm nếu ô trống hoặc còn placeholder
        if (cccd.isEmpty() || cccd.equals("Nhập CCCD")) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng nhập CCCD khách hàng trước khi tìm kiếm.",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Reset trạng thái trước khi nạp kết quả mới
        listThanhToan.clear();
        tongTienPhong = 0.0;
        tienKhachDua = 0.0;

        danhSachPhong = (List<ChiTietPhieuDatPhongDTO>) clientConnection.sendRequest(
                        Request
                                .builder()
                                .commandType(CommandType.GET_DANH_SACH_DE_THANH_TOAN)
                                .object(cccd)
                                .build())
                .getObject();

        modelBang.setRowCount(0); // xoá dòng cũ
        for (ChiTietPhieuDatPhongDTO ct : danhSachPhong) {
            modelBang.addRow(new Object[]{
                    false,
                    ct.getPhong().getSoPhong(),
                    ct.getPhong().getLoaiPhong(),
                    ct.getSoGioLuuTru(),
                    ct.tinhThanhTien()
            });
        }

        // Reset checkbox header
        chkTatCa.setSelected(false);
        bangPhong.getTableHeader().repaint();

        // Cập nhật summary về trạng thái rỗng
        lblSoPhongChon.setText("Chưa chọn phòng nào");
        lblSoPhongChon.setForeground(TEXT_GRAY);
        lblTongTien.setText("0 đ");
        lblVAT.setText("10%");
        lblTotal.setText("0 đ");
        tfKhachDua.setText("");
        lblTienThua.setText("—");
        lblTienThua.setForeground(GREEN);

        // Thông báo nếu không tìm thấy kết quả
        if (danhSachPhong.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Không tìm thấy phòng nào đang thuê với CCCD: " + cccd,
                    "Không có kết quả",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // ─────────────────────────────────────────────────────────────────
    // B2: Bảng phòng — CỘT 0 LÀ CHECKBOX để chọn nhiều phòng
    // ─────────────────────────────────────────────────────────────────
    private JPanel buildTableCard() {
        JPanel card = new RoundedPanel(12, BG_WHITE);
        card.setLayout(new BorderLayout());

        // Bảng khởi tạo RỖNG — chỉ load data khi nhấn Tìm kiếm
        modelBang = new DefaultTableModel(new Object[0][5], COT_BANG) {
            @Override
            public Class<?> getColumnClass(int col) {
                return col == 0 ? Boolean.class : Object.class;
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 0; // chỉ cột checkbox được click
            }
        };

        bangPhong = new JTable(modelBang);
        bangPhong.setFont(F_TABLE);
        bangPhong.setForeground(TEXT_DARK);
        bangPhong.setRowHeight(52);
        bangPhong.setShowVerticalLines(false);
        bangPhong.setShowHorizontalLines(true);
        bangPhong.setGridColor(BORDER_COL);
        bangPhong.setBackground(BG_WHITE);
        bangPhong.setSelectionBackground(BLUE_LIGHT);
        bangPhong.setSelectionForeground(TEXT_DARK);
        bangPhong.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bangPhong.setIntercellSpacing(new Dimension(0, 0));
        bangPhong.setFocusable(false);

        // ── Header: cột 0 thay bằng JCheckBox "chọn tất cả" ──
        chkTatCa = new JCheckBox();
        chkTatCa.setOpaque(true);
        chkTatCa.setBackground(BG_WHITE);
        chkTatCa.setHorizontalAlignment(JCheckBox.CENTER);
        chkTatCa.setToolTipText("Chọn / Bỏ chọn tất cả");

        bangPhong.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean focus, int row, int col) {
                if (col == 0) {
                    chkTatCa.setBackground(BG_WHITE);
                    return chkTatCa;
                }
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(
                        t, val, sel, focus, row, col);
                lbl.setFont(F_TABLE_H);
                lbl.setForeground(TEXT_MID);
                lbl.setBackground(BG_WHITE);
                lbl.setHorizontalAlignment(JLabel.CENTER);
                lbl.setBorder(new MatteBorder(0, 0, 2, 0, BORDER_COL));
                return lbl;
            }
        });

        JTableHeader header = bangPhong.getTableHeader();
        header.setFont(F_TABLE_H);
        header.setForeground(TEXT_MID);
        header.setBackground(BG_WHITE);
        header.setBorder(new MatteBorder(0, 0, 2, 0, BORDER_COL));
        header.setPreferredSize(new Dimension(0, 42));

        // Click header cột 0 -> tick/untick tất cả
        header.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (header.columnAtPoint(e.getPoint()) == 0) {
                    boolean trangThaiMoi = !chkTatCa.isSelected();
                    chkTatCa.setSelected(trangThaiMoi);
                    for (int r = 0; r < modelBang.getRowCount(); r++) {
                        modelBang.setValueAt(trangThaiMoi, r, 0);
                    }
                    // capNhatListThanhToan() tự gọi qua TableModelListener bên dưới
                }
            }
        });

        // ── Renderer cột dữ liệu (cột 1-4): zebra stripe + cột tiền màu cam ──
        bangPhong.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean focus, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, focus, row, col);
                setHorizontalAlignment(JLabel.CENTER);
                setBorder(new EmptyBorder(0, 8, 0, 8));
                if (!sel)
                    setBackground(row % 2 == 0 ? BG_WHITE : new Color(0xFAFBFD));
                if (col == t.getColumnCount() - 1) {
                    setForeground(sel ? TEXT_DARK : ORANGE);
                    setFont(F_BOLD13);
                } else {
                    setForeground(TEXT_DARK);
                    setFont(F_TABLE);
                }
                return this;
            }
        });

        // Renderer cột 4 (Tổng tiền): format số + đ
        bangPhong.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean focus, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, focus, row, col);
                setText(val instanceof Number ? formatter.format(val) + " đ" : "");
                setHorizontalAlignment(JLabel.CENTER);
                setForeground(sel ? TEXT_DARK : ORANGE);
                setFont(F_BOLD13);
                if (!sel)
                    setBackground(row % 2 == 0 ? BG_WHITE : new Color(0xFAFBFD));
                return this;
            }
        });

        // Renderer cột 0 (Boolean): JCheckBox với màu zebra
        bangPhong.getColumnModel().getColumn(0).setCellRenderer(new TableCellRenderer() {
            private final JCheckBox chk = new JCheckBox();

            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean focus, int row, int col) {
                chk.setSelected(Boolean.TRUE.equals(val));
                chk.setHorizontalAlignment(JCheckBox.CENTER);
                chk.setOpaque(true);
                chk.setBackground(sel ? BLUE_LIGHT
                        : (row % 2 == 0 ? BG_WHITE : new Color(0xFAFBFD)));
                return chk;
            }
        });

        // Khi checkbox thay đổi -> cập nhật listThanhToan + summary
        modelBang.addTableModelListener(e -> {
            if (e.getColumn() == 0) {
                capNhatListThanhToan();
            }
        });

        // Độ rộng cột
        bangPhong.getColumnModel().getColumn(0).setPreferredWidth(44);
        bangPhong.getColumnModel().getColumn(0).setMaxWidth(44);
        bangPhong.getColumnModel().getColumn(1).setPreferredWidth(70);
        bangPhong.getColumnModel().getColumn(2).setPreferredWidth(110);
        bangPhong.getColumnModel().getColumn(3).setPreferredWidth(140);
        bangPhong.getColumnModel().getColumn(4).setPreferredWidth(120);

        JScrollPane scroll = new JScrollPane(bangPhong);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(BG_WHITE);
        scroll.setPreferredSize(new Dimension(0, 220));

        card.add(scroll, BorderLayout.CENTER);
        return card;
    }

    /**
     * Duyệt bảng, tổng hợp các dòng đang được tick vào listThanhToan.
     * Cập nhật chkTatCa và Summary Card.
     */
    private void capNhatListThanhToan() {
        listThanhToan = new ArrayList<>();

        int tongDong = modelBang.getRowCount();
        int soDongChon = 0;

        for (int r = 0; r < tongDong; r++) {
            boolean daChon = Boolean.TRUE.equals(modelBang.getValueAt(r, 0));
            if (daChon) {
                soDongChon++;
                if (r < danhSachPhong.size()) {
                    listThanhToan.add(danhSachPhong.get(r));
                }
            }
        }

        // Đồng bộ checkbox header
        chkTatCa.setSelected(tongDong > 0 && soDongChon == tongDong);
        bangPhong.getTableHeader().repaint();

        // Tính lại tổng tiền từ listThanhToan
        tongTienPhong = listThanhToan.stream()
                .mapToDouble(ChiTietPhieuDatPhongDTO::tinhThanhTien)
                .sum();

        updateSummaryCard();
        if (tfKhachDua != null)
            tinhTienThua();
    }

    // ─────────────────────────────────────────────────────────────────
    // Summary Card
    // ─────────────────────────────────────────────────────────────────
    private JPanel buildSummaryCard() {
        JPanel card = new RoundedPanel(12, BG_WHITE);
        card.setLayout(new BorderLayout());

        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(new EmptyBorder(18, 28, 18, 28));

        // ── "Đang chọn X phòng" — căn giữa cố định ──
        lblSoPhongChon = new JLabel("Chưa chọn phòng nào", JLabel.CENTER);
        lblSoPhongChon.setFont(F_BOLD13);
        lblSoPhongChon.setForeground(TEXT_GRAY);
        lblSoPhongChon.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblSoPhongChon.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
        body.add(lblSoPhongChon);
        body.add(Box.createVerticalStrut(10));

        // ── Separator ──
        JSeparator sepTop = new JSeparator();
        sepTop.setForeground(BORDER_COL);
        sepTop.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        body.add(sepTop);
        body.add(Box.createVerticalStrut(10));

        // ── Hàng: nút Chọn KM (trái) + tên KM (phải) ──
        btnChonKhuyenMai = buildBlueButton("Chọn KM", 100, 30);
        btnChonKhuyenMai.addActionListener(e -> moModalKhuyenMai());

        lblKhuyenMaiTen = new JLabel("Chưa áp dụng khuyến mãi");
        lblKhuyenMaiTen.setFont(F_BOLD13);
        lblKhuyenMaiTen.setForeground(TEXT_GRAY);
        lblKhuyenMaiTen.setHorizontalAlignment(JLabel.RIGHT);

        JPanel rowKM = new JPanel(new BorderLayout(8, 0));
        rowKM.setOpaque(false);
        rowKM.setAlignmentX(Component.LEFT_ALIGNMENT);
        rowKM.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        rowKM.add(btnChonKhuyenMai, BorderLayout.WEST);
        rowKM.add(lblKhuyenMaiTen, BorderLayout.CENTER);
        body.add(rowKM);
        body.add(Box.createVerticalStrut(10));

        // ── Separator giữa KM và các dòng tổng ──
        JSeparator sepMid = new JSeparator();
        sepMid.setForeground(BORDER_COL);
        sepMid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        body.add(sepMid);
        body.add(Box.createVerticalStrut(10));

        // ── Khởi tạo labels ──
        lblTongTien = new JLabel("0 đ");
        lblKhuyenMai = new JLabel("0%");
        lblVAT = new JLabel("10%");
        lblTotal = new JLabel("0 đ");

        lblTongTien.setFont(F_BOLD13);
        lblTongTien.setForeground(TEXT_DARK);
        lblKhuyenMai.setFont(F_BOLD13);
        lblKhuyenMai.setForeground(GREEN);
        lblVAT.setFont(F_BOLD13);
        lblVAT.setForeground(TEXT_MID);
        lblTotal.setFont(F_BOLD16);
        lblTotal.setForeground(BLUE);

        body.add(makeSummaryRow("Tổng tiền phòng", lblTongTien));
        body.add(Box.createVerticalStrut(8));
        body.add(makeSummaryRow("Khuyến mãi", lblKhuyenMai));
        body.add(Box.createVerticalStrut(8));
        body.add(makeSummaryRow("VAT", lblVAT));
        body.add(Box.createVerticalStrut(12));

        // ── Separator dưới ──
        JSeparator sepBot = new JSeparator();
        sepBot.setForeground(BORDER_COL);
        sepBot.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        body.add(sepBot);
        body.add(Box.createVerticalStrut(12));

        // ── Tổng cộng ──
        JPanel totalRow = new JPanel(new BorderLayout());
        totalRow.setOpaque(true);
        totalRow.setBackground(BLUE_LIGHT);
        totalRow.setBorder(new CompoundBorder(
                new LineBorder(new Color(0xC5D5FF), 1, true),
                new EmptyBorder(10, 16, 10, 16)));
        totalRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        totalRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblNhanTotal = new JLabel("Tổng cộng");
        lblNhanTotal.setFont(F_BOLD14);
        lblNhanTotal.setForeground(BLUE);
        lblTotal.setHorizontalAlignment(JLabel.RIGHT);

        totalRow.add(lblNhanTotal, BorderLayout.WEST);
        totalRow.add(lblTotal, BorderLayout.EAST);
        body.add(totalRow);

        card.add(body, BorderLayout.CENTER);
        return card;
    }

    // Đổi tên thành makeSummaryRow để tránh xung đột với buildSummaryRow cũ
    private JPanel makeSummaryRow(String nhan, JLabel lblGiaTri) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));

        JLabel lblNhan = new JLabel(nhan);
        lblNhan.setFont(F_LABEL);
        lblNhan.setForeground(TEXT_MID);
        lblGiaTri.setHorizontalAlignment(JLabel.RIGHT);

        row.add(lblNhan, BorderLayout.WEST);
        row.add(lblGiaTri, BorderLayout.EAST);
        return row;
    }

    // ═══════════════════════════════════════════════════════════════════
    // PANEL PHẢI
    // Fix: bỏ setMaximumSize cứng trên radioCard/gridMenhGia -> text không bị cắt
    // ═══════════════════════════════════════════════════════════════════
    private JPanel buildRightPanel() {
        JPanel panel = new RoundedPanel(14, BG_WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(24, 20, 24, 20));

        // ── Tiêu đề ──
        JLabel title = new JLabel("Thanh Toán");
        title.setFont(F_TITLE.deriveFont(F_TITLE.getSize() * 1.3f));
        title.setForeground(TEXT_DARK);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(title);

        // ── Đường accent căn giữa ──
        JPanel accentLine = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BLUE);
                g2.fillRoundRect(0, 0, getWidth(), 3, 3, 3);
                g2.dispose();
            }
        };
        accentLine.setOpaque(false);
        accentLine.setPreferredSize(new Dimension(50, 3));
        accentLine.setMaximumSize(new Dimension(50, 3));
        accentLine.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(Box.createVerticalStrut(6));
        panel.add(accentLine);
        panel.add(Box.createVerticalStrut(18));

        // ── Label "Phương thức thanh toán" ──
        JLabel lblChon = new JLabel(" ");
        lblChon.setForeground(TEXT_MID);
        lblChon.setFont(F_BOLD13.deriveFont(F_BOLD13.getSize() * 1.1f));
        lblChon.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblChon);
        panel.add(Box.createVerticalStrut(8));

        // ── Nội dung thanh toán ──
        panelTienMat = buildPanelTienMat();
        panelTienMat.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(panelTienMat);

        panel.add(Box.createVerticalGlue());

        // ── Nút Thanh Toán ──
        btnThanhToan = buildGreenButton("Thanh Toán");
        btnThanhToan.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnThanhToan.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        btnThanhToan.setPreferredSize(new Dimension(Integer.MAX_VALUE, 48));
        btnThanhToan.addActionListener(e -> {

            if (listThanhToan.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn phòng");
                return;
            }

            // Kiểm tra ca làm việc trước khi thanh toán
            String maNV = CurrentUser.getInstance().getMaNhanVien();
            CaLamViecNhanVienDTO activeShift = (CaLamViecNhanVienDTO) clientConnection.sendRequest(
                    Request.builder()
                            .commandType(CommandType.GET_ACTIVE_SHIFT)
                            .object(maNV)
                            .build()

            ).getObject();
            if (activeShift == null) {
                JOptionPane.showMessageDialog(this,
                        "Bạn phải MỞ CA LÀM VIỆC trước khi thực hiện thanh toán!",
                        "Yêu cầu mở ca", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Map<String, Object> params = new HashMap<>();
            params.put("tienKhachDua", tienKhachDua);
            params.put("tongTien", tongTien);

            Response coTheThanhToan = clientConnection.sendRequest(
                    Request.builder()
                            .commandType(CommandType.CO_THE_THANH_TOAN)
                            .object(params)
                            .build());
            boolean isThanhToan = (boolean) coTheThanhToan.getObject();
            if (!isThanhToan || tfKhachDua.getText().equals("")) {
                JOptionPane.showMessageDialog(this,
                        "Thanh toán thất bại vì số tiền khách hàng đưa không đủ",
                        "Thanh toán thất bại",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                ThanhToanRequest thanhToanRequest = ThanhToanRequest.builder()
                        .listThanhToan(listThanhToan)
                        .tienKhachDua(tienKhachDua)
                        .tienThua(tienThua)
                        .tongTien(tongTien)
                        .maNhanVien(maNV)
                        .build();

                Response resHD = clientConnection.sendRequest(
                        Request
                                .builder()
                                .commandType(CommandType.THANH_TOAN)
                                .object(thanhToanRequest)
                                .build());

                HoaDonDTO hoaDon = (HoaDonDTO) resHD.getObject();
                if (hoaDon != null) {
                    HDPanel hdPanel = new HDPanel(hoaDon);
                    hdPanel.show(this);
                    refresh();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Thanh toán thất bại",
                            "Thất bại",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panel.add(btnThanhToan);

        return panel;
    }

    // ═══════════════════════════════════════════════════════════════════
    // 3.1 PANEL TIỀN MẶT
    // ═══════════════════════════════════════════════════════════════════
    private JPanel buildPanelTienMat() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel lblMenhGia = new JLabel("Mệnh giá nhanh");
        lblMenhGia.setForeground(TEXT_MID);
        lblMenhGia.setFont(F_BOLD13.deriveFont(F_BOLD13.getSize() * 1.1f));
        lblMenhGia.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblMenhGia);
        panel.add(Box.createVerticalStrut(8));

        // Grid 4x2 cho 7 mệnh giá (ô thứ 8 để trống)
        JPanel gridMenhGia = new JPanel(new GridLayout(4, 2, 8, 8));
        gridMenhGia.setOpaque(false);
        // FIX: không set height cứng -> nút không bị cắt chữ
        gridMenhGia.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        gridMenhGia.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnMenhGia = new JButton[MENH_GIA_NHANH.length];
        for (int i = 0; i < MENH_GIA_NHANH.length; i++) {
            final double soTien = MENH_GIA_NHANH[i];
            JButton btn = buildOutlineButton(formatter.format(soTien) + " đ");
            btn.addActionListener(e -> {
                if (tongTienPhong > 0)
                    capNhatKhachDua(soTien);
            });
            btnMenhGia[i] = btn;
            gridMenhGia.add(btn);
        }
        gridMenhGia.add(new JLabel()); // ô trống để grid 4x2 cân bằng
        panel.add(gridMenhGia);
        panel.add(Box.createVerticalStrut(14));

        JLabel lblNhap = new JLabel("Tiền khách đưa");
        lblNhap.setForeground(TEXT_MID);
        lblNhap.setFont(F_BOLD13.deriveFont(F_BOLD13.getSize() * 1.1f));
        lblNhap.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblNhap);
        panel.add(Box.createVerticalStrut(6));

        tfKhachDua = new JTextField();
        tfKhachDua.setForeground(TEXT_DARK);
        tfKhachDua.setFont(F_BOLD14.deriveFont(F_BOLD14.getSize() * 1.1f));
        tfKhachDua.setHorizontalAlignment(JTextField.RIGHT);
        tfKhachDua.setBorder(new CompoundBorder(
                new LineBorder(BLUE_LIGHT, 2, true),
                new EmptyBorder(8, 12, 8, 12)));
        tfKhachDua.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        tfKhachDua.setAlignmentX(Component.CENTER_ALIGNMENT);
        tfKhachDua.addActionListener(e -> tinhTienThua());
        tfKhachDua.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                tinhTienThua();
            }
        });
        panel.add(tfKhachDua);
        panel.add(Box.createVerticalStrut(10));

        JPanel rowTienThua = new JPanel(new BorderLayout(8, 0));
        rowTienThua.setOpaque(true);
        rowTienThua.setBackground(new Color(0xF0FFF5));
        rowTienThua.setBorder(new CompoundBorder(
                new LineBorder(new Color(0xB7EAC8), 1, true),
                new EmptyBorder(10, 14, 10, 14)));
        rowTienThua.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        rowTienThua.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblNhanThua = new JLabel("Tiền thừa trả lại");
        lblNhanThua.setFont(F_BOLD13.deriveFont(F_BOLD13.getSize() * 1.1f));
        lblNhanThua.setForeground(GREEN_DARK);

        lblTienThua = new JLabel("—");
        lblTienThua.setFont(F_BOLD14.deriveFont(F_BOLD14.getSize() * 1.1f));
        lblTienThua.setForeground(GREEN);
        lblTienThua.setHorizontalAlignment(JLabel.RIGHT);

        rowTienThua.add(lblNhanThua, BorderLayout.WEST);
        rowTienThua.add(lblTienThua, BorderLayout.EAST);
        panel.add(rowTienThua);

        return panel;
    }

    // ═══════════════════════════════════════════════════════════════════
    // TIỆN ÍCH THANH TOÁN
    // ═══════════════════════════════════════════════════════════════════

    private void capNhatKhachDua(double soTien) {
        tienKhachDua += soTien;
        tfKhachDua.setText(formatter.format(tienKhachDua));
        tinhTienThua();
    }

    private void tinhTienThua() {
        try {
            if (tongTien <= 0) {
                lblTienThua.setText("—");
                return;
            }

            String text = tfKhachDua.getText().replace("đ", "").trim();
            Number number = formatter.parse(text);
            tienKhachDua = number.doubleValue();

            // Dùng tongTien đã tính sẵn trong updateSummaryCard() (đã bao gồm KM + VAT)
            tienThua = tienKhachDua - tongTien;

            if (tienThua >= 0) {
                lblTienThua.setText(formatter.format(tienThua) + " đ");
                lblTienThua.setForeground(GREEN);
            } else {
                lblTienThua.setText("Thiếu " + formatter.format(-tienThua) + " đ");
                lblTienThua.setForeground(ORANGE);
            }
        } catch (Exception e) {
            lblTienThua.setText("—");
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    // CẬP NHẬT SUMMARY CARD (gọi mỗi khi listThanhToan thay đổi)
    // ═══════════════════════════════════════════════════════════════════
    private void updateSummaryCard() {
        int soPhong = listThanhToan.size();
        lblSoPhongChon.setText(soPhong > 0 ? "Đang chọn " + soPhong + " phòng" : "Chưa chọn phòng nào");
        lblSoPhongChon.setForeground(soPhong > 0 ? BLUE : TEXT_GRAY);
        lblTongTien.setText(formatter.format(tongTienPhong) + " đ");

        if (soPhong == 0) {
            tongTien = 0;
            lblKhuyenMai.setText("0%");
            lblKhuyenMaiTen.setText("Chưa áp dụng khuyến mãi");
            lblKhuyenMaiTen.setForeground(TEXT_GRAY);
            lblVAT.setText("10%");
            lblTotal.setText("0 đ");
            if (tfKhachDua != null) {
                tfKhachDua.setText("");
                lblTienThua.setText("—");
                lblTienThua.setForeground(GREEN);
            }
            return;  // ← thoát sớm, không gọi server
        }

        Map<String, Object> params = new HashMap<>();
        params.put("tongTien", tongTienPhong);
        params.put("km", selectedKhuyenMai);

        KiemTraKhuyenMaiResult result = (KiemTraKhuyenMaiResult) clientConnection.sendRequest(
                        Request.builder()
                                .commandType(CommandType.TIEN_SAU_KHI_AP_GIAM_GIA)
                                .object(params)
                                .build())
                .getObject();

        if (result == null) return;  // phòng thủ thêm
        tongTien = result.getTien();

        if (selectedKhuyenMai != null) {
            if (tongTienPhong >= selectedKhuyenMai.getTongTienToiThieu()) {
                lblKhuyenMai.setText("-" + Math.round(selectedKhuyenMai.getHeSo() * 100) + "%");
                lblKhuyenMaiTen.setText(selectedKhuyenMai.getTenKhuyenMai());
                lblKhuyenMaiTen.setForeground(GREEN);
            } else {
                lblKhuyenMai.setText("0%");
                lblKhuyenMaiTen.setText("Chưa đạt tối thiểu " +
                        formatter.format(selectedKhuyenMai.getTongTienToiThieu()) + " đ");
                lblKhuyenMaiTen.setForeground(ORANGE);
            }
        } else {
            lblKhuyenMai.setText("0%");
            lblKhuyenMaiTen.setText("Không có khuyến mãi");
            lblKhuyenMaiTen.setForeground(TEXT_GRAY);
        }

        lblVAT.setText("10%");
        lblTotal.setText(formatter.format(tongTien) + " đ");

        tienKhachDua = 0.0;
        if (tfKhachDua != null) {
            tfKhachDua.setText("");
            lblTienThua.setText("—");
            lblTienThua.setForeground(GREEN);
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    // REFRESH: Reset toàn bộ panel về trạng thái ban đầu
    // Gọi sau khi thanh toán thành công hoặc cần làm mới dữ liệu
    // ═══════════════════════════════════════════════════════════════════
    public void refresh() {

        // ── 1. Reset ô tìm kiếm về placeholder ──
        tfCCCD.setText("Nhập CCCD");
        tfCCCD.setForeground(TEXT_GRAY);

        // ── 2. Reset biến tiền trước để TableModelListener không tính sai khi addRow
        // ──
        listThanhToan = new ArrayList<>();
        tongTienPhong = 0.0;
        tienKhachDua = 0.0;

        // ── 3. Tải lại dữ liệu bảng từ ThanhToanServiceImpl ──
        // Tải lại theo CCCD đang nhập (nếu ô trống thì trả về danh sách rỗng)
        danhSachPhong = new ArrayList<>();

        modelBang.setRowCount(0); // xóa toàn bộ dòng cũ
        for (ChiTietPhieuDatPhongDTO ct : danhSachPhong) {
            modelBang.addRow(new Object[]{
                    false,
                    ct.getPhong().getSoPhong(),
                    ct.getPhong().getLoaiPhong(),
                    ct.getSoGioLuuTru(),
                    ct.tinhThanhTien()
            });
        }

        // ── 4. Reset checkbox header "chọn tất cả" ──
        chkTatCa.setSelected(false);
        bangPhong.getTableHeader().repaint();

        // ── 5. Reset summary card ──
        lblSoPhongChon.setText("Chưa chọn phòng nào");
        lblSoPhongChon.setForeground(TEXT_GRAY);
        lblTongTien.setText("0 đ");
        lblVAT.setText("10%");
        lblTotal.setText("0 đ");

        // ── 6. Reset panel tiền mặt ──
        tfKhachDua.setText("");
        lblTienThua.setText("—");
        lblTienThua.setForeground(GREEN);

        panelTienMat.setVisible(true);

        selectedKhuyenMai = null;
        lblKhuyenMai.setText("0%");
        lblKhuyenMaiTen.setText("Chưa áp dụng khuyến mãi");
        lblKhuyenMaiTen.setForeground(TEXT_GRAY);
    }

    // ═══════════════════════════════════════════════════════════════════
    // HELPER: Tạo các loại nút
    // ═══════════════════════════════════════════════════════════════════

    private JButton buildBlueButton(String text, int w, int h) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? BLUE.darker() : BLUE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(F_BOLD13);
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(w, h));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton buildBlueButtonFull(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? BLUE.darker() : BLUE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(F_BOLD13);
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    /**
     * Nút viền có icon — dùng cho nút Làm mới (kích thước cố định)
     */
    private JButton buildOutlineIconButton(String text, int w, int h) {
        JButton btn = buildOutlineButton(text);
        btn.setPreferredSize(new Dimension(w, h));
        btn.setForeground(TEXT_MID);
        return btn;
    }

    private void moModalKhuyenMai() {
        @SuppressWarnings("unchecked")
        List<KhuyenMaiDTO> dsKM = (List<KhuyenMaiDTO>) clientConnection.sendRequest(
                        Request.builder()
                                .commandType(CommandType.GET_DANH_SACH_KHUYEN_MAI_HOP_LE)
                                .build())
                .getObject();

        // Chỉ lọc theo tổng tiền — ngày đã được server xử lý
        List<KhuyenMaiDTO> dsHopLe = dsKM.stream()
                .filter(km -> tongTienPhong >= km.getTongTienToiThieu())
                .collect(Collectors.toList());

        Frame owner = (Frame) SwingUtilities.getWindowAncestor(this);
        JDialog modal = new JDialog(owner, "Chọn mã khuyến mãi", true);
        modal.setSize(680, 440);
        modal.setLocationRelativeTo(this);
        modal.setLayout(new BorderLayout());

        // ── Header modal ──
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BLUE);
        header.setBorder(new EmptyBorder(14, 20, 14, 20));
        JLabel titleLbl = new JLabel("Danh sách mã khuyến mãi");
        titleLbl.setFont(F_BOLD14);
        titleLbl.setForeground(Color.WHITE);
        header.add(titleLbl, BorderLayout.WEST);
        if (selectedKhuyenMai != null) {
            JButton btnBo = new JButton("Bỏ khuyến mãi");
            btnBo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            btnBo.setForeground(Color.WHITE);
            btnBo.setBackground(new Color(0x2A5FD8));
            btnBo.setBorder(new EmptyBorder(4, 10, 4, 10));
            btnBo.setFocusPainted(false);
            btnBo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btnBo.addActionListener(e -> {
                apDungKhuyenMai(null);
                modal.dispose();
            });
            header.add(btnBo, BorderLayout.EAST);
        }
        modal.add(header, BorderLayout.NORTH);

        // ── Bảng khuyến mãi ──
        String[] cols = {"Tên khuyến mãi", "Tối thiểu", "Tối đa", "Từ ngày", "Đến ngày", "Giảm"};
        DefaultTableModel modelKM = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (KhuyenMaiDTO km : dsHopLe) {
            modelKM.addRow(new Object[]{
                    km.getTenKhuyenMai(),
                    formatter.format(km.getTongTienToiThieu()) + " đ",
                    formatter.format(km.getTongKhuyenMaiToiDa()) + " đ",
                    km.getNgayBatDau() != null ? km.getNgayBatDau().format(dtf) : "—",
                    km.getNgayKetThuc() != null ? km.getNgayKetThuc().format(dtf) : "—",
                    Math.round(km.getHeSo() * 100) + "%"
            });
        }

        JTable tableKM = new JTable(modelKM);
        tableKM.setFont(F_TABLE);
        tableKM.setRowHeight(48);
        tableKM.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableKM.setShowVerticalLines(false);
        tableKM.setShowHorizontalLines(true);
        tableKM.setGridColor(BORDER_COL);
        tableKM.setBackground(BG_WHITE);
        tableKM.setSelectionBackground(BLUE_LIGHT);
        tableKM.setSelectionForeground(TEXT_DARK);
        tableKM.getTableHeader().setFont(F_TABLE_H);
        tableKM.getTableHeader().setBackground(new Color(0xF0F4FF));
        tableKM.getTableHeader().setForeground(TEXT_MID);
        tableKM.getTableHeader().setPreferredSize(new Dimension(0, 40));

        // Tô xanh dòng đang được chọn sẵn
        if (selectedKhuyenMai != null) {
            for (int i = 0; i < dsHopLe.size(); i++) {
                if (dsHopLe.get(i).getMaKhuyenMai().equals(selectedKhuyenMai.getMaKhuyenMai())) {
                    tableKM.setRowSelectionInterval(i, i);
                    break;
                }
            }
        }

        // Renderer cột "Giảm" màu xanh
        tableKM.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean focus, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, focus, row, col);
                setHorizontalAlignment(JLabel.CENTER);
                setForeground(sel ? TEXT_DARK : GREEN);
                setFont(F_BOLD13);
                if (!sel)
                    setBackground(row % 2 == 0 ? BG_WHITE : new Color(0xFAFBFD));
                return this;
            }
        });

        // Renderer zebra cho các cột còn lại
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean focus, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, focus, row, col);
                setHorizontalAlignment(JLabel.CENTER);
                if (!sel)
                    setBackground(row % 2 == 0 ? BG_WHITE : new Color(0xFAFBFD));
                return this;
            }
        };
        for (int c = 0; c < 5; c++)
            tableKM.getColumnModel().getColumn(c).setCellRenderer(centerRenderer);

        JScrollPane scroll = new JScrollPane(tableKM);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        // Thông báo nếu không có KM hợp lệ
        if (dsHopLe.isEmpty()) {
            JPanel emptyPanel = new JPanel(new BorderLayout());
            emptyPanel.setBackground(BG_WHITE);
            JLabel emptyLbl = new JLabel("Không có mã khuyến mãi hợp lệ", JLabel.CENTER);
            emptyLbl.setFont(F_LABEL);
            emptyLbl.setForeground(TEXT_GRAY);
            emptyPanel.add(emptyLbl, BorderLayout.CENTER);
            modal.add(emptyPanel, BorderLayout.CENTER);
        } else {
            modal.add(scroll, BorderLayout.CENTER);
        }

        // ── Footer: nút Áp dụng + Đóng ──
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        footer.setBackground(BG_MAIN);
        footer.setBorder(new MatteBorder(1, 0, 0, 0, BORDER_COL));

        JButton btnDong = buildOutlineButton("Đóng");
        btnDong.setPreferredSize(new Dimension(100, 38));
        btnDong.addActionListener(e -> modal.dispose());

        JButton btnApDung = buildBlueButton("Áp dụng", 110, 38);
        btnApDung.addActionListener(e -> {
            int row = tableKM.getSelectedRow();
            if (row < 0 || dsHopLe.isEmpty()) {
                JOptionPane.showMessageDialog(modal,
                        "Vui lòng chọn một mã khuyến mãi.",
                        "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            apDungKhuyenMai(dsHopLe.get(row));
            modal.dispose();
        });

        footer.add(btnDong);
        footer.add(btnApDung);
        modal.add(footer, BorderLayout.SOUTH);

        modal.setVisible(true);
    }

    private JButton buildOutlineButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? BLUE_LIGHT : BG_WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(getModel().isPressed() ? BLUE : BORDER_COL);
                g2.setStroke(new java.awt.BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(F_BOLD13);
        btn.setForeground(TEXT_MID);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(110, 42));
        return btn;
    }

    private JButton buildGreenButton(String text) {
        JButton btn = new JButton(text) {
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
                Color bgColor = getModel().isPressed() ? GREEN_DARK
                        : hovered ? new Color(0x1E9E50) : GREEN;
                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(F_BOLD14.deriveFont(F_BOLD14.getSize() * 1.2f));
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JRadioButton createRadio(String text, boolean selected) {
        JRadioButton rb = new JRadioButton(text, selected);
        rb.setFont(F_LABEL.deriveFont(F_LABEL.getSize() * 1.15f));
        rb.setForeground(TEXT_DARK);
        rb.setOpaque(false);
        rb.setFocusPainted(false);
        rb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        rb.setIcon(createRadioIcon(false));
        rb.setSelectedIcon(createRadioIcon(true));
        return rb;
    }

    private Icon createRadioIcon(boolean checked) {
        return new Icon() {
            @Override
            public int getIconWidth() {
                return 18;
            }

            @Override
            public int getIconHeight() {
                return 18;
            }

            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BORDER_COL);
                g2.fillOval(x, y, 17, 17);
                g2.setColor(checked ? GREEN : Color.WHITE);
                g2.fillOval(x + 1, y + 1, 15, 15);
                if (checked) {
                    g2.setColor(Color.WHITE);
                    g2.fillOval(x + 5, y + 5, 7, 7);
                } else {
                    g2.setColor(BORDER_COL);
                    g2.setStroke(new java.awt.BasicStroke(1.5f));
                    g2.drawOval(x + 1, y + 1, 15, 15);
                }
                g2.dispose();
            }
        };
    }

    // ═══════════════════════════════════════════════════════════════════
    // QRCodePanel – Vẽ mã QR giả lập
    // ═══════════════════════════════════════════════════════════════════
    static class QRCodePanel extends JPanel {
        private final int size;
        private final BufferedImage qrImage;

        QRCodePanel(int size) {
            this.size = size;
            setOpaque(false);
            setPreferredSize(new Dimension(size, size));
            qrImage = generateFakeQR(size);
        }

        private BufferedImage generateFakeQR(int px) {
            int modules = 25;
            int cellSize = px / modules;
            int actualPx = cellSize * modules;

            BufferedImage img = new BufferedImage(actualPx, actualPx, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = img.createGraphics();
            g2.setColor(Color.WHITE);
            g2.fillRect(0, 0, actualPx, actualPx);

            java.util.Random rng = new java.util.Random(0xABCDEF);
            boolean[][] luoi = new boolean[modules][modules];
            for (int r = 0; r < modules; r++)
                for (int c = 0; c < modules; c++)
                    luoi[r][c] = rng.nextBoolean();

            for (int r = 0; r < modules; r++) {
                for (int c = 0; c < modules; c++) {
                    if (isVungFinderPattern(r, c, modules))
                        continue;
                    if (luoi[r][c]) {
                        g2.setColor(Color.BLACK);
                        g2.fillRect(c * cellSize, r * cellSize, cellSize - 1, cellSize - 1);
                    }
                }
            }

            veFinderPattern(g2, 0, 0, cellSize);
            veFinderPattern(g2, 0, (modules - 7) * cellSize, cellSize);
            veFinderPattern(g2, (modules - 7) * cellSize, 0, cellSize);

            g2.dispose();
            return img;
        }

        private void veFinderPattern(Graphics2D g2, int py, int px, int cell) {
            g2.setColor(Color.BLACK);
            g2.fillRect(px, py, cell * 7, cell * 7);
            g2.setColor(Color.WHITE);
            g2.fillRect(px + cell, py + cell, cell * 5, cell * 5);
            g2.setColor(Color.BLACK);
            g2.fillRect(px + cell * 2, py + cell * 2, cell * 3, cell * 3);
        }

        private boolean isVungFinderPattern(int r, int c, int n) {
            return (r <= 7 && c <= 7)
                    || (r <= 7 && c >= n - 8)
                    || (r >= n - 8 && c <= 7);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, size, size, 8, 8);
            g2.setColor(new Color(0xDDE3EE));
            g2.setStroke(new java.awt.BasicStroke(1.5f));
            g2.drawRoundRect(0, 0, size - 1, size - 1, 8, 8);
            g2.drawImage(qrImage, 6, 6, size - 12, size - 12, null);
            g2.dispose();
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    // RoundedPanel – JPanel bo góc + đổ bóng nhẹ
    // ═══════════════════════════════════════════════════════════════════
    static class RoundedPanel extends JPanel {
        private final int arc;
        private final Color bg;

        RoundedPanel(int arc, Color bg) {
            this.arc = arc;
            this.bg = bg;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
            g2.setColor(new Color(0, 0, 0, 12));
            g2.setStroke(new java.awt.BasicStroke(1f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private void apDungKhuyenMai(KhuyenMaiDTO km) {
        selectedKhuyenMai = km;
        if (km == null) {
            // updateSummaryCard không xử lý case null → phải set thủ công
            lblKhuyenMai.setText("0%");
            lblKhuyenMaiTen.setText("Chưa áp dụng khuyến mãi");
            lblKhuyenMaiTen.setForeground(TEXT_GRAY);
        }
        updateSummaryCard();
        tinhTienThua();
    }

}