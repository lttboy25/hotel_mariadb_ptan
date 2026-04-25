package iuh.view;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import iuh.entity.ChiTietPhieuDatPhong;
import iuh.entity.LoaiPhong;
import iuh.entity.PhieuDatPhong;
import iuh.entity.Phong;
import iuh.service.ChiTietPhieuDatPhongService;
import iuh.service.PhieuDatPhongService;
import iuh.service.ThanhToanService;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * ThanhToanPanel - Giao diện trang Thanh Toán
 *
 * Quy trình:
 * B1: Nhập CCCD → Tìm phòng cần thanh toán
 * B2: Chọn phòng trong bảng
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
    // DỮ LIỆU MẪU
    // ═══════════════════════════════════════════════════════════════════
    private static final String[] COT_BANG = {
            "Phòng", "Loại Phòng", "Thời gian lưu trú", "Tổng tiền"
    };


    private static final double[] MENH_GIA_NHANH = {
            10_000, 30_000, 50_000, 100_000,  200_000,300_000, 500_000
    };

    private static final long TONG_TIEN = 2_530_000;

    // ═══════════════════════════════════════════════════════════════════
    // BIẾN TOÀN CỤC
    // ═══════════════════════════════════════════════════════════════════
    private JTextField tfCCCD;
    private JButton btnTimKiem;
    private JTable bangPhong;
    private DefaultTableModel modelBang;
    private JLabel lblTongTien, lblKhuyenMai, lblVAT, lblTotal;
    private JRadioButton rbTienMat, rbMomo;
    private JPanel panelTienMat, panelQR;
    private JButton[] btnMenhGia;
    private JTextField tfKhachDua;
    private JLabel lblTienThua;
    private QRCodePanel qrCodePanel;
    private JButton btnThanhToan;
    private ThanhToanService thanhToanService;
    private ChiTietPhieuDatPhongService chiTietPhieuDatPhongService = new ChiTietPhieuDatPhongService();
    private PhieuDatPhongService phieuDatPhongService = new PhieuDatPhongService();
    private double tongTienPhong = 0.0;
    private double tienKhachDua = 0.0;
    private ChiTietPhieuDatPhong chiTietPhieuDatPhongDangChon = null;
    private NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));




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

        btnTimKiem = buildBlueButton("Tìm kiếm", 100, 36);
        card.add(tfCCCD);
        card.add(btnTimKiem);
        return card;
    }

    private JPanel buildTableCard() {
        JPanel card = new RoundedPanel(12, BG_WHITE);
        card.setLayout(new BorderLayout());

        List<ChiTietPhieuDatPhong> ds = getDanhSachPhieuDatPhongDeThanhToan();


        Object[][] data = new Object[ds.size()][4];

        for (int i = 0; i < ds.size(); i++) {
            ChiTietPhieuDatPhong ctpdp = ds.get(i);
            data[i][0] = ctpdp.getPhong().getSoPhong();
            data[i][1] = ctpdp.getPhong().getLoaiPhong();
            data[i][2] = ctpdp.getSoGioLuuTru();
            data[i][3] = ctpdp.tinhThanhTien();

        }

        modelBang = new DefaultTableModel(data, COT_BANG) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
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

        JTableHeader header = bangPhong.getTableHeader();
        header.setFont(F_TABLE_H);
        header.setForeground(TEXT_MID);
        header.setBackground(BG_WHITE);
        header.setBorder(new MatteBorder(0, 0, 2, 0, BORDER_COL));
        header.setPreferredSize(new Dimension(0, 42));
        ((DefaultTableCellRenderer) header.getDefaultRenderer())
                .setHorizontalAlignment(JLabel.CENTER);

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

        bangPhong.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            protected void setValue(Object value) {
                if (value instanceof Number) {
                    setText(formatter.format(value) + " đ");
                } else {
                    setText("");
                }
            }
        });

        bangPhong.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                int row = bangPhong.getSelectedRow();
                // "Phòng", "Loại Phòng", "Thời gian lưu trú", "Tổng tiền"

                if(row != -1) {
                    tongTienPhong = (double) bangPhong.getValueAt(row, 3);
                    updateSummaryCard();

                }

            }
        });

        JScrollPane scroll = new JScrollPane(bangPhong);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(BG_WHITE);
        scroll.setPreferredSize(new Dimension(0, 220));

        card.add(scroll, BorderLayout.CENTER);
        return card;
    }

    private JPanel buildSummaryCard() {
        JPanel card = new RoundedPanel(12, BG_WHITE);
        card.setLayout(new BorderLayout());

        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(new EmptyBorder(18, 28, 18, 28));


        lblTongTien = new JLabel(formatter.format(tongTienPhong) + " đ");
        lblKhuyenMai = new JLabel("0%");
        lblVAT = new JLabel("10%");
        lblTotal = new JLabel("2.530.000 đ");

        lblTongTien.setFont(F_BOLD13);
        lblTongTien.setForeground(TEXT_DARK);
        lblKhuyenMai.setFont(F_BOLD13);
        lblKhuyenMai.setForeground(GREEN);
        lblVAT.setFont(F_BOLD13);
        lblVAT.setForeground(TEXT_MID);
        lblTotal.setFont(F_BOLD16);
        lblTotal.setForeground(BLUE);

        body.add(buildSummaryRow("Tổng tiền phòng", lblTongTien));
        body.add(Box.createVerticalStrut(10));
        body.add(buildSummaryRow("Khuyến mãi", lblKhuyenMai));
        body.add(Box.createVerticalStrut(10));
        body.add(buildSummaryRow("VAT", lblVAT));
        body.add(Box.createVerticalStrut(12));

        JSeparator sep = new JSeparator();
        sep.setForeground(BORDER_COL);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        body.add(sep);
        body.add(Box.createVerticalStrut(12));

        JPanel totalRow = new JPanel(new BorderLayout());
        totalRow.setOpaque(true);
        totalRow.setBackground(BLUE_LIGHT);
        totalRow.setBorder(new CompoundBorder(
                new LineBorder(new Color(0xC5D5FF), 1, true),
                new EmptyBorder(10, 16, 10, 16)));
        totalRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));

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

    private JPanel buildSummaryRow(String nhan, JLabel lblGiaTri) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setOpaque(false);
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

        // ── Đường accent canh giữa ──
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
        JLabel lblChon = new JLabel("Phương thức thanh toán");
        lblChon.setForeground(TEXT_MID);
        lblChon.setFont(F_BOLD13.deriveFont(F_BOLD13.getSize() * 1.1f));
        lblChon.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblChon);
        panel.add(Box.createVerticalStrut(8));

        // ── Radio card ──
        ButtonGroup bg = new ButtonGroup();
        rbTienMat = createRadio("Tiền mặt", true);
        rbMomo = createRadio("Momo", false);
        bg.add(rbTienMat);
        bg.add(rbMomo);

        JPanel radioCard = new JPanel();
        radioCard.setLayout(new BoxLayout(radioCard, BoxLayout.Y_AXIS));
        radioCard.setBackground(new Color(0xF8F9FF));
        radioCard.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COL, 1, true),
                new EmptyBorder(10, 20, 10, 20)));
        radioCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        radioCard.setAlignmentX(Component.CENTER_ALIGNMENT);
        rbTienMat.setAlignmentX(Component.LEFT_ALIGNMENT);
        rbMomo.setAlignmentX(Component.LEFT_ALIGNMENT);
        radioCard.add(rbTienMat);
        radioCard.add(Box.createVerticalStrut(6));
        radioCard.add(rbMomo);
        panel.add(radioCard);
        panel.add(Box.createVerticalStrut(16));

        // ── Nội dung thanh toán ──
        panelTienMat = buildPanelTienMat();
        panelTienMat.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(panelTienMat);

        panelQR = buildPanelQR();
        panelQR.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelQR.setVisible(false);
        panel.add(panelQR);

        rbTienMat.addActionListener(e -> {
            panelTienMat.setVisible(true);
            panelQR.setVisible(false);
        });
        rbMomo.addActionListener(e -> {
            panelTienMat.setVisible(false);
            panelQR.setVisible(true);
        });

        panel.add(Box.createVerticalGlue());

        // ── Nút Thanh Toán ──
        btnThanhToan = buildGreenButton("Thanh Toán");
        btnThanhToan.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnThanhToan.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        btnThanhToan.setPreferredSize(new Dimension(Integer.MAX_VALUE, 48));
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

        // Label "Mệnh giá nhanh"
        JLabel lblMenhGia = new JLabel("Mệnh giá nhanh");
        lblMenhGia.setForeground(TEXT_MID);
        lblMenhGia.setFont(F_BOLD13.deriveFont(F_BOLD13.getSize() * 1.1f));
        lblMenhGia.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblMenhGia);
        panel.add(Box.createVerticalStrut(8));

        // Grid mệnh giá 2×2
        JPanel gridMenhGia = new JPanel(new GridLayout(3, 2, 8, 8));
        gridMenhGia.setOpaque(false);
        gridMenhGia.setMaximumSize(new Dimension(Integer.MAX_VALUE, 96));
        gridMenhGia.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnMenhGia = new JButton[MENH_GIA_NHANH.length];
        for (int i = 0; i < MENH_GIA_NHANH.length; i++) {
            final double soTien = MENH_GIA_NHANH[i];
            JButton btn = buildOutlineButton(formatter.format(soTien) + " đ");
            btn.addActionListener(e -> {
                if (tongTienPhong>0) {
                    capNhatKhachDua(soTien);
                }
                });
            btnMenhGia[i] = btn;
            gridMenhGia.add(btn);
        }
        panel.add(gridMenhGia);
        panel.add(Box.createVerticalStrut(14));

        // Label "Tiền khách đưa"
        JLabel lblNhap = new JLabel("Tiền khách đưa");
        lblNhap.setForeground(TEXT_MID);
        lblNhap.setFont(F_BOLD13.deriveFont(F_BOLD13.getSize() * 1.1f));
        lblNhap.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblNhap);
        panel.add(Box.createVerticalStrut(6));

        // TextField
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

        // Row tiền thừa
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
    // 3.2 PANEL QR MOMO
    // ═══════════════════════════════════════════════════════════════════
    private JPanel buildPanelQR() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel lblHuongDan = new JLabel("Quét mã QR để thanh toán:");
        lblHuongDan.setFont(F_BOLD13);
        lblHuongDan.setForeground(TEXT_MID);
        lblHuongDan.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblHuongDan);
        panel.add(Box.createVerticalStrut(12));

        qrCodePanel = new QRCodePanel(190);
        qrCodePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(qrCodePanel);

        panel.add(Box.createVerticalStrut(8));

        JLabel lblSoTien = new JLabel("Số tiền: " + formatter.format(tongTienPhong) + " đ");
        lblSoTien.setFont(F_BOLD14);
        lblSoTien.setForeground(BLUE);
        lblSoTien.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblSoTien);

        return panel;
    }

    // ═══════════════════════════════════════════════════════════════════
    // TIỆN ÍCH THANH TOÁN
    // ═══════════════════════════════════════════════════════════════════

    /** Điền số tiền từ nút mệnh giá nhanh vào ô nhập */
    private void capNhatKhachDua(double soTien) {
        tienKhachDua+=soTien;
        tfKhachDua.setText(formatter.format(tienKhachDua));
        tinhTienThua();
    }

    /** Tính và hiển thị tiền thừa = khách đưa − tổng tiền */
    private void tinhTienThua() {
        try {
            if (tongTienPhong <= 0) {
                lblTienThua.setText("—");
                return;
            }

            String text = tfKhachDua.getText().replace("đ", "").trim();
            Number number = formatter.parse(text);
            tienKhachDua = number.doubleValue();

            double vat = tongTienPhong * 0.1;
            double total = tongTienPhong + vat;

            double tienThua = tienKhachDua - total;

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
    // HELPER: Tạo các loại nút
    // ═══════════════════════════════════════════════════════════════════

    /** Nút nền xanh dương (Tìm kiếm) */
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

    /** Nút viền (mệnh giá nhanh) – font gốc để chữ không bị cắt */
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

    /** Nút nền xanh lá (Thanh Toán) với hiệu ứng hover */
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
                Color bg = getModel().isPressed() ? GREEN_DARK
                        : hovered ? new Color(0x1E9E50) : GREEN;
                g2.setColor(bg);
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

    /** Tạo JRadioButton với icon tròn tùy chỉnh */
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

    /** Vẽ icon hình tròn cho radio button */
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

    public List<ChiTietPhieuDatPhong> getDanhSachPhieuDatPhongDeThanhToan() {
        List<PhieuDatPhong> dsPhieuDatPhongDangThue = new ArrayList<>();
        List<ChiTietPhieuDatPhong> dsPhongDangThue = new ArrayList<>();

        if (phieuDatPhongService.getByTrangThai("Đã nhận phòng").size() > 0) {
            dsPhieuDatPhongDangThue = phieuDatPhongService.getByTrangThai("Đã nhận phòng");
        }
        else {
            return dsPhongDangThue;
        }

        dsPhieuDatPhongDangThue.forEach(d -> {
            List<ChiTietPhieuDatPhong> dsChiTietTuongUng = chiTietPhieuDatPhongService.getChiTietPhieuDatPhongByMaPDP(d.getMaPhieuDatPhong());
            dsChiTietTuongUng.forEach(ctpdp -> {
                dsPhongDangThue.add(ctpdp);
            });
        });
        return dsPhongDangThue;
    }

    private void updateSummaryCard() {

        lblTongTien.setText(formatter.format(tongTienPhong) + " đ");

        double vat = tongTienPhong * 0.1;
        double total = tongTienPhong + vat;

        lblVAT.setText("10%");
        lblTotal.setText(formatter.format(total) + " đ");
    }

}