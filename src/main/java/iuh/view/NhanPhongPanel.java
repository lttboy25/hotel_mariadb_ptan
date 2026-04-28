package iuh.view;

import iuh.entity.ChiTietPhieuDatPhong;
import iuh.entity.KhachHang;
import iuh.entity.Phong;
import iuh.service.NhanPhongService;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * NhanPhongPanel - Giao diện trang Nhận Phòng
 *
 * Quy trình:
 * B1: Nhập CCCD → Tìm phiếu đặt phòng đã đặt cọc
 * B2: Tick checkbox chọn 1 hoặc nhiều phòng muốn nhận
 * B3: Nhấn "Xác nhận nhận phòng" → phòng chuyển trạng thái → refresh
 *
 * Double-click vào dòng → hiện popup chi tiết ChiTietPhieuDatPhong
 */
public class NhanPhongPanel extends JPanel {

    // ═══════════════════════════════════════════════════════════════════
    // HẰNG SỐ MÀU SẮC
    // ═══════════════════════════════════════════════════════════════════
    private static final Color BG_MAIN     = new Color(0xF4F6FB);
    private static final Color BG_WHITE    = Color.WHITE;
    private static final Color BLUE        = new Color(0x3B6FF0);
    private static final Color BLUE_LIGHT  = new Color(0xEBF0FF);
    private static final Color TEXT_DARK   = new Color(0x1A1A2E);
    private static final Color TEXT_MID    = new Color(0x4A5268);
    private static final Color TEXT_GRAY   = new Color(0xA0A8B8);
    private static final Color BORDER_COL  = new Color(0xE4E9F2);
    private static final Color GREEN       = new Color(0x27AE60);
    private static final Color GREEN_DARK  = new Color(0x1E8449);
    private static final Color GREEN_LIGHT = new Color(0xEAF9F0);
    private static final Color ORANGE      = new Color(0xF39C12);

    // ═══════════════════════════════════════════════════════════════════
    // HẰNG SỐ FONT
    // ═══════════════════════════════════════════════════════════════════
    private static final Font F_TITLE   = new Font("Segoe UI", Font.BOLD,  18);
    private static final Font F_LABEL   = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font F_BOLD13  = new Font("Segoe UI", Font.BOLD,  13);
    private static final Font F_BOLD14  = new Font("Segoe UI", Font.BOLD,  14);
    private static final Font F_BOLD12  = new Font("Segoe UI", Font.BOLD,  12);
    private static final Font F_TABLE   = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font F_TABLE_H = new Font("Segoe UI", Font.BOLD,  13);
    private static final Font F_SMALL   = new Font("Segoe UI", Font.PLAIN, 12);

    // ═══════════════════════════════════════════════════════════════════
    // CẤU HÌNH BẢNG
    // Cột 0: Checkbox | Cột 1-5: thông tin phòng
    // ═══════════════════════════════════════════════════════════════════
    private static final String[] COT_BANG = {
            "", "Số Phòng", "Loại Phòng", "Ngày nhận", "Ngày trả", "Trạng thái"
    };

    // ═══════════════════════════════════════════════════════════════════
    // BIẾN TOÀN CỤC
    // ═══════════════════════════════════════════════════════════════════

    // ── Search bar
    private JTextField tfCCCD;
    private JButton    btnTimKiem;
    private JButton    btnLamMoi;

    // ── Bảng
    private JTable            bangPhong;
    private DefaultTableModel modelBang;
    private JCheckBox         chkTatCa;

    // ── Footer info + nút xác nhận
    private JLabel  lblSoPhongChon;
    private JLabel  lblTenKhach;
    private JLabel  lblMaPhieu;
    private JButton btnXacNhan;

    // ── Service & data
    private NhanPhongService nhanPhongService = new NhanPhongService();

    private List<ChiTietPhieuDatPhong> danhSachChiTiet = new ArrayList<>();
    private List<ChiTietPhieuDatPhong> listNhanPhong   = new ArrayList<>();

    private static final DateTimeFormatter FMT_DATE =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter FMT_DATE_SHORT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));

    // ═══════════════════════════════════════════════════════════════════
    // CONSTRUCTOR
    // ═══════════════════════════════════════════════════════════════════
    public NhanPhongPanel() {
        setLayout(new BorderLayout());
        setBackground(BG_MAIN);
        setBorder(new EmptyBorder(28, 28, 28, 28));

        JPanel mainCol = new JPanel();
        mainCol.setLayout(new BoxLayout(mainCol, BoxLayout.Y_AXIS));
        mainCol.setBackground(BG_MAIN);

        mainCol.add(Box.createVerticalStrut(16));
        mainCol.add(buildSearchBar());
        mainCol.add(Box.createVerticalStrut(16));
        mainCol.add(buildTableCard());
        mainCol.add(Box.createVerticalStrut(16));
        mainCol.add(buildFooterBar());

        add(mainCol, BorderLayout.CENTER);
    }

    // ═══════════════════════════════════════════════════════════════════
    // B1: SEARCH BAR — Nhập CCCD + Tìm kiếm + Làm mới
    // ═══════════════════════════════════════════════════════════════════
    private JPanel buildSearchBar() {
        JPanel card = new RoundedPanel(12, BG_WHITE);
        card.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 12));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 62));

        JLabel lbl = new JLabel("CCCD khách hàng:");
        lbl.setFont(F_BOLD13);
        lbl.setForeground(TEXT_MID);

        tfCCCD = new JTextField(24);
        tfCCCD.setFont(F_LABEL);
        tfCCCD.setText("Nhập số CCCD...");
        tfCCCD.setForeground(TEXT_GRAY);
        tfCCCD.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COL, 1, true),
                new EmptyBorder(6, 12, 6, 12)
        ));
        tfCCCD.setPreferredSize(new Dimension(240, 36));
        tfCCCD.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                if (tfCCCD.getText().equals("Nhập số CCCD...")) {
                    tfCCCD.setText("");
                    tfCCCD.setForeground(TEXT_DARK);
                }
            }
            @Override public void focusLost(FocusEvent e) {
                if (tfCCCD.getText().isEmpty()) {
                    tfCCCD.setText("Nhập số CCCD...");
                    tfCCCD.setForeground(TEXT_GRAY);
                }
            }
        });
        tfCCCD.addActionListener(e -> timKiem());

        btnTimKiem = buildBlueButton("Tìm kiếm", 120, 36);
        btnTimKiem.addActionListener(e -> timKiem());

        btnLamMoi = buildOutlineButton("Làm mới", 100, 36);
        btnLamMoi.addActionListener(e -> refresh());

        card.add(lbl);
        card.add(tfCCCD);
        card.add(btnTimKiem);
        card.add(btnLamMoi);
        return card;
    }

    // ═══════════════════════════════════════════════════════════════════
    // B2: BẢNG PHÒNG
    // ═══════════════════════════════════════════════════════════════════
    private JPanel buildTableCard() {
        JPanel card = new RoundedPanel(12, BG_WHITE);
        card.setLayout(new BorderLayout());

        modelBang = new DefaultTableModel(new Object[0][6], COT_BANG) {
            @Override
            public Class<?> getColumnClass(int col) {
                return col == 0 ? Boolean.class : Object.class;
            }
            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 0;
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
        header.setBackground(BG_WHITE);
        header.setBorder(new MatteBorder(0, 0, 2, 0, BORDER_COL));
        header.setPreferredSize(new Dimension(0, 44));

        header.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (header.columnAtPoint(e.getPoint()) == 0) {
                    boolean moi = !chkTatCa.isSelected();
                    chkTatCa.setSelected(moi);
                    for (int r = 0; r < modelBang.getRowCount(); r++) {
                        modelBang.setValueAt(moi, r, 0);
                    }
                }
            }
        });

        bangPhong.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean focus, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, focus, row, col);
                setHorizontalAlignment(JLabel.CENTER);
                setBorder(new EmptyBorder(0, 8, 0, 8));
                if (!sel) setBackground(row % 2 == 0 ? BG_WHITE : new Color(0xFAFBFD));
                setForeground(TEXT_DARK);
                setFont(F_TABLE);
                return this;
            }
        });

        bangPhong.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean focus, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, focus, row, col);
                String trangThai = val == null ? "" : val.toString();
                setHorizontalAlignment(JLabel.CENTER);
                setFont(F_BOLD12);
                if (!sel) {
                    setBackground(row % 2 == 0 ? BG_WHITE : new Color(0xFAFBFD));
                }
                if (trangThai.contains("Đã đặt")) {
                    setForeground(ORANGE);
                } else if (trangThai.contains("Đã nhận")) {
                    setForeground(GREEN);
                } else {
                    setForeground(TEXT_MID);
                }
                return this;
            }
        });

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

        modelBang.addTableModelListener(e -> {
            if (e.getColumn() == 0) capNhatListNhanPhong();
        });

        bangPhong.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = bangPhong.rowAtPoint(e.getPoint());
                    if (row >= 0 && row < danhSachChiTiet.size()) {
                        hienThiChiTiet(danhSachChiTiet.get(row));
                    }
                }
            }
        });

        bangPhong.getColumnModel().getColumn(0).setPreferredWidth(44);
        bangPhong.getColumnModel().getColumn(0).setMaxWidth(44);
        bangPhong.getColumnModel().getColumn(1).setPreferredWidth(90);
        bangPhong.getColumnModel().getColumn(2).setPreferredWidth(130);
        bangPhong.getColumnModel().getColumn(3).setPreferredWidth(140);
        bangPhong.getColumnModel().getColumn(4).setPreferredWidth(140);
        bangPhong.getColumnModel().getColumn(5).setPreferredWidth(110);

        JScrollPane scroll = new JScrollPane(bangPhong) {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (modelBang.getRowCount() == 0) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setFont(F_LABEL);
                    g2.setColor(TEXT_GRAY);
                    String msg = "Nhập CCCD và nhấn Tìm kiếm để xem danh sách phòng đã đặt";
                    FontMetrics fm = g2.getFontMetrics();
                    int x = (getWidth() - fm.stringWidth(msg)) / 2;
                    int y = getHeight() / 2;
                    g2.drawString(msg, x, y);
                    g2.dispose();
                }
            }
        };
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(BG_WHITE);
        scroll.setPreferredSize(new Dimension(0, 320));

        card.add(scroll, BorderLayout.CENTER);
        return card;
    }

    // ═══════════════════════════════════════════════════════════════════
    // B3: FOOTER BAR
    // ═══════════════════════════════════════════════════════════════════
    private JPanel buildFooterBar() {
        JPanel card = new RoundedPanel(12, BG_WHITE);
        card.setLayout(new BorderLayout(16, 0));
        card.setBorder(new EmptyBorder(14, 24, 14, 24));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 72));

        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 0, 2));
        infoPanel.setOpaque(false);

        lblTenKhach = new JLabel("Chưa tìm kiếm");
        lblTenKhach.setFont(F_BOLD13);
        lblTenKhach.setForeground(TEXT_MID);

        lblMaPhieu = new JLabel("");
        lblMaPhieu.setFont(F_SMALL);
        lblMaPhieu.setForeground(TEXT_GRAY);

        infoPanel.add(lblTenKhach);
        infoPanel.add(lblMaPhieu);

        lblSoPhongChon = new JLabel("Chưa chọn phòng nào");
        lblSoPhongChon.setFont(F_BOLD13);
        lblSoPhongChon.setForeground(TEXT_GRAY);
        lblSoPhongChon.setHorizontalAlignment(JLabel.CENTER);

        btnXacNhan = buildGreenButton("Xác nhận nhận phòng", 200, 44);
        btnXacNhan.addActionListener(e -> xacNhanNhanPhong());

        card.add(infoPanel,      BorderLayout.WEST);
        card.add(lblSoPhongChon, BorderLayout.CENTER);
        card.add(btnXacNhan,     BorderLayout.EAST);
        return card;
    }

    // ═══════════════════════════════════════════════════════════════════
    // LOGIC TÌM KIẾM
    // ═══════════════════════════════════════════════════════════════════
    private void timKiem() {
        String cccd = tfCCCD.getText().trim();

        if (cccd.isEmpty() || cccd.equals("Nhập số CCCD...")) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng nhập CCCD khách hàng trước khi tìm kiếm.",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        listNhanPhong.clear();
        danhSachChiTiet.clear();
        modelBang.setRowCount(0);

        danhSachChiTiet = nhanPhongService.getDanhSachPhongDaDatByCCCD(cccd);

        for (ChiTietPhieuDatPhong ct : danhSachChiTiet) {
            modelBang.addRow(new Object[]{
                    false,
                    ct.getPhong().getSoPhong(),
                    ct.getPhong().getLoaiPhong(),
                    ct.getThoiGianNhanPhong() != null
                            ? ct.getThoiGianNhanPhong().format(FMT_DATE_SHORT) : "—",
                    ct.getThoiGianTraPhong() != null
                            ? ct.getThoiGianTraPhong().format(FMT_DATE_SHORT) : "—",
                    ct.getTrangThai()
            });
        }

        if (!danhSachChiTiet.isEmpty()) {
            KhachHang kh = danhSachChiTiet.get(0).getPhieuDatPhong().getKhachHang();
            lblTenKhach.setText(kh.getTenKhachHang() + "  |  CCCD: " + kh.getCCCD());
            lblTenKhach.setForeground(TEXT_DARK);
            String maPDP = danhSachChiTiet.get(0).getPhieuDatPhong().getMaPhieuDatPhong();
            lblMaPhieu.setText("Mã phiếu: " + maPDP);
        } else {
            lblTenKhach.setText("Không tìm thấy phiếu đặt phòng nào với CCCD: " + cccd);
            lblTenKhach.setForeground(ORANGE);
            lblMaPhieu.setText("");
            JOptionPane.showMessageDialog(this,
                    "Không tìm thấy phiếu đặt phòng nào với CCCD: " + cccd,
                    "Không có kết quả", JOptionPane.INFORMATION_MESSAGE);
        }

        chkTatCa.setSelected(false);
        bangPhong.getTableHeader().repaint();
        capNhatFooter();
    }

    // ═══════════════════════════════════════════════════════════════════
    // LOGIC CẬP NHẬT LIST NHẬN PHÒNG
    // ═══════════════════════════════════════════════════════════════════
    private void capNhatListNhanPhong() {
        listNhanPhong.clear();

        int tongDong   = modelBang.getRowCount();
        int soDongChon = 0;

        for (int r = 0; r < tongDong; r++) {
            boolean daChon = Boolean.TRUE.equals(modelBang.getValueAt(r, 0));
            if (daChon) {
                soDongChon++;
                if (r < danhSachChiTiet.size()) {
                    listNhanPhong.add(danhSachChiTiet.get(r));
                }
            }
        }

        chkTatCa.setSelected(tongDong > 0 && soDongChon == tongDong);
        bangPhong.getTableHeader().repaint();
        capNhatFooter();
    }

    private void capNhatFooter() {
        int soPhong = listNhanPhong.size();
        if (soPhong > 0) {
            lblSoPhongChon.setText("Đang chọn  " + soPhong + "  phòng để nhận");
            lblSoPhongChon.setForeground(BLUE);
        } else {
            lblSoPhongChon.setText("Chưa chọn phòng nào");
            lblSoPhongChon.setForeground(TEXT_GRAY);
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    // LOGIC XÁC NHẬN NHẬN PHÒNG
    // ═══════════════════════════════════════════════════════════════════
    private void xacNhanNhanPhong() {
        if (listNhanPhong.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn ít nhất 1 phòng để nhận.",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Xác nhận nhận " + listNhanPhong.size() + " phòng đã chọn?",
                "Xác nhận", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            boolean ok = nhanPhongService.nhanPhong(listNhanPhong);
            if (ok) {
                JOptionPane.showMessageDialog(this,
                        "Nhận phòng thành công!",
                        "Thành công", JOptionPane.INFORMATION_MESSAGE);
                refresh();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Nhận phòng thất bại. Vui lòng thử lại.",
                        "Thất bại", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    // DOUBLE-CLICK → POPUP CHI TIẾT
    // ═══════════════════════════════════════════════════════════════════
    private void hienThiChiTiet(ChiTietPhieuDatPhong ct) {
        JDialog dialog = new JDialog(
                (Frame) SwingUtilities.getWindowAncestor(this),
                "Chi tiết phiếu đặt phòng", true);
        dialog.setSize(500, 480);
        dialog.setLocationRelativeTo(this);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(BG_MAIN);
        content.setBorder(new EmptyBorder(24, 24, 24, 24));

        JLabel title = new JLabel("Chi tiết phiếu đặt phòng");
        title.setFont(F_TITLE);
        title.setForeground(TEXT_DARK);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(title);
        content.add(Box.createVerticalStrut(4));

        JPanel accent = new JPanel();
        accent.setBackground(BLUE);
        accent.setMaximumSize(new Dimension(40, 3));
        accent.setPreferredSize(new Dimension(40, 3));
        accent.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(accent);
        content.add(Box.createVerticalStrut(20));

        JPanel card = new RoundedPanel(12, BG_WHITE);
        card.setLayout(new GridLayout(0, 2, 0, 0));
        card.setBorder(new EmptyBorder(16, 20, 16, 20));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        Phong phong = ct.getPhong();
        KhachHang kh = ct.getPhieuDatPhong().getKhachHang();

        addDetailRow(card, "Số phòng",       phong.getSoPhong() != null ? phong.getSoPhong() : "—");
        addDetailRow(card, "Loại phòng",     phong.getLoaiPhong() != null ? phong.getLoaiPhong().toString() : "—");
        addDetailRow(card, "Trạng thái phòng", ct.getTrangThai() != null ? ct.getTrangThai() : "—");
        addDetailRow(card, "Khách hàng",     kh.getTenKhachHang() != null ? kh.getTenKhachHang() : "—");
        addDetailRow(card, "CCCD",           kh.getCCCD() != null ? kh.getCCCD() : "—");
        addDetailRow(card, "Số điện thoại",  kh.getSoDienThoai() != null ? kh.getSoDienThoai() : "—");
        addDetailRow(card, "Ngày nhận phòng",
                ct.getThoiGianNhanPhong() != null ? ct.getThoiGianNhanPhong().format(FMT_DATE) : "—");
        addDetailRow(card, "Ngày trả phòng",
                ct.getThoiGianTraPhong() != null ? ct.getThoiGianTraPhong().format(FMT_DATE) : "—");
        addDetailRow(card, "Số giờ lưu trú", ct.getSoGioLuuTru() + " giờ");
        addDetailRow(card, "Thành tiền",     formatter.format(ct.tinhThanhTien()) + " đ");
        addDetailRow(card, "Mã phiếu đặt",   ct.getPhieuDatPhong().getMaPhieuDatPhong());

        content.add(card);
        content.add(Box.createVerticalStrut(20));

        JButton btnDong = buildBlueButton("Đóng", 100, 38);
        btnDong.setAlignmentX(Component.RIGHT_ALIGNMENT);
        btnDong.addActionListener(e -> dialog.dispose());
        content.add(btnDong);

        dialog.setContentPane(new JScrollPane(content));
        dialog.setVisible(true);
    }

    private void addDetailRow(JPanel parent, String label, String value) {
        JLabel lblKey = new JLabel(label);
        lblKey.setFont(F_LABEL);
        lblKey.setForeground(TEXT_GRAY);
        lblKey.setBorder(new EmptyBorder(8, 0, 8, 16));

        JLabel lblVal = new JLabel(value);
        lblVal.setFont(F_BOLD13);
        lblVal.setForeground(TEXT_DARK);
        lblVal.setBorder(new EmptyBorder(8, 0, 8, 0));

        parent.add(lblKey);
        parent.add(lblVal);
    }

    // ═══════════════════════════════════════════════════════════════════
    // REFRESH
    // ═══════════════════════════════════════════════════════════════════
    public void refresh() {
        tfCCCD.setText("Nhập số CCCD...");
        tfCCCD.setForeground(TEXT_GRAY);

        listNhanPhong.clear();
        danhSachChiTiet.clear();
        modelBang.setRowCount(0);

        chkTatCa.setSelected(false);
        bangPhong.getTableHeader().repaint();

        lblTenKhach.setText("Chưa tìm kiếm");
        lblTenKhach.setForeground(TEXT_MID);
        lblMaPhieu.setText("");
        lblSoPhongChon.setText("Chưa chọn phòng nào");
        lblSoPhongChon.setForeground(TEXT_GRAY);
    }

    // ═══════════════════════════════════════════════════════════════════
    // HELPER: Tạo các loại nút
    // ═══════════════════════════════════════════════════════════════════

    private JButton buildBlueButton(String text, int w, int h) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
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

    private JButton buildOutlineButton(String text, int w, int h) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
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
        btn.setPreferredSize(new Dimension(w, h));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton buildGreenButton(String text, int w, int h) {
        JButton btn = new JButton(text) {
            private boolean hovered = false;
            {
                addMouseListener(new MouseAdapter() {
                    @Override public void mouseEntered(MouseEvent e) { hovered = true;  repaint(); }
                    @Override public void mouseExited (MouseEvent e) { hovered = false; repaint(); }
                });
            }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = getModel().isPressed() ? GREEN_DARK
                        : hovered ? new Color(0x1E9E50) : GREEN;
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
        btn.setPreferredSize(new Dimension(w, h));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ═══════════════════════════════════════════════════════════════════
    // RoundedPanel
    // ═══════════════════════════════════════════════════════════════════
    static class RoundedPanel extends JPanel {
        private final int arc;
        private final Color bg;

        RoundedPanel(int arc, Color bg) {
            this.arc = arc;
            this.bg  = bg;
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
}