package iuh.view;

import iuh.dto.ChiTietPhieuDatPhongDTO;
import iuh.dto.HuyPhongRequest;
import iuh.dto.HuyPhongResultDTO;
import iuh.network.ClientConnection;
import iuh.network.CommandType;
import iuh.network.Request;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HuyPhongPanel extends JPanel {

    // --- BẢNG MÀU CHUẨN FORM ---
    static final Color BG = new Color(0xF4F6FB);
    static final Color WHITE = Color.WHITE;
    static final Color BLUE = new Color(0x3B6FF0);
    static final Color RED = new Color(0xEF4444);
    static final Color GREEN = new Color(0x22C55E);
    static final Color DARK = new Color(0x1A1A2E);
    static final Color MID = new Color(0x4A5268);
    static final Color BORDER = new Color(0xE4E9F2);

    static final Font F_H1 = new Font("Segoe UI", Font.BOLD, 24);
    static final Font F_LABEL = new Font("Segoe UI", Font.BOLD, 14);
    static final Font F_TEXT = new Font("Segoe UI", Font.PLAIN, 14);
    static final Font F_BOLD12 = new Font("Segoe UI", Font.BOLD, 13);
    static final Font F_TABLE = new Font("Segoe UI", Font.PLAIN, 14);

    private JTable tblDanhSach;
    private DefaultTableModel tableModel;
    private JTextField txtTimKiem;

    // Khai báo 2 Label thông báo riêng biệt cho Tìm kiếm và Thao tác
    private JLabel lblSearchMsg;
    private JLabel lblActionMsg;

    private List<ChiTietPhieuDatPhongDTO> dsChiTiet = new ArrayList<>();

    public HuyPhongPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(BG);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // =========================================================
        // 1. PANEL HEADER
        // =========================================================
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(BG);

        JLabel lblTitle = lbl("Hủy phòng", F_H1, DARK);
        pnlHeader.add(lblTitle, BorderLayout.WEST);

        JPanel pnlRightWrapper = new JPanel(new BorderLayout());
        pnlRightWrapper.setBackground(BG);

        JPanel pnlTimKiem = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlTimKiem.setBackground(BG);

        pnlTimKiem.add(lbl("CCCD Khách hàng:", F_LABEL, MID));
        txtTimKiem = new JTextField(15);
        txtTimKiem.setFont(F_TEXT);
        txtTimKiem.setPreferredSize(new Dimension(200, 40));
        txtTimKiem.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(0, 10, 0, 10)));
        txtTimKiem.addActionListener(e -> timKiemPhongThoCCCD());
        pnlTimKiem.add(txtTimKiem);

        JButton btnTim = blueBtn("Tìm Kiếm", 100, 40);
        btnTim.addActionListener(e -> timKiemPhongThoCCCD());
        pnlTimKiem.add(btnTim);

        lblSearchMsg = lbl(" ", new Font("Segoe UI", Font.ITALIC, 13), RED);
        lblSearchMsg.setHorizontalAlignment(SwingConstants.RIGHT);
        lblSearchMsg.setBorder(new EmptyBorder(5, 0, 0, 110));

        pnlRightWrapper.add(pnlTimKiem, BorderLayout.CENTER);
        pnlRightWrapper.add(lblSearchMsg, BorderLayout.SOUTH);

        pnlHeader.add(pnlRightWrapper, BorderLayout.EAST);
        add(pnlHeader, BorderLayout.NORTH);

        // =========================================================
        // 2. BẢNG DANH SÁCH
        // =========================================================
        String[] cols = {"Chọn", "Mã Phiếu", "Phòng", "Loại", "Tên KH", "CCCD", "Ngày Nhận", "Ngày Trả"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? Boolean.class : String.class;
            }
        };
        tblDanhSach = new JTable(tableModel);
        styleTable(tblDanhSach);

        JScrollPane scrollPane = new JScrollPane(tblDanhSach);
        scrollPane.setBorder(new LineBorder(BORDER, 1, true));
        scrollPane.getViewport().setBackground(WHITE);
        add(scrollPane, BorderLayout.CENTER);

        // =========================================================
        // 3. PANEL FOOTER (Thêm Label thông báo lỗi ở đây)
        // =========================================================
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        pnlFooter.setBackground(BG);

        // Label hiển thị lỗi kế bên nút Hủy
        lblActionMsg = lbl(" ", new Font("Segoe UI", Font.ITALIC, 13), RED);
        pnlFooter.add(lblActionMsg);

        JButton btnHuy = redBtn("Hủy Các Phòng Đã Chọn", 200, 46);
        btnHuy.addActionListener(e -> xuLyHuyPhong());
        pnlFooter.add(btnHuy);

        add(pnlFooter, BorderLayout.SOUTH);
    }

    // =========================================================
    // LOGIC TÌM KIẾM
    // =========================================================
    private void timKiemPhongThoCCCD() {
        String cccd = txtTimKiem.getText().trim();

        if (cccd.isEmpty()) {
            lblSearchMsg.setText("Vui lòng nhập số CCCD để tìm kiếm!");
            lblSearchMsg.setForeground(RED);
            dsChiTiet.clear();
            loadDataToTable();
            return;
        }

        dsChiTiet = (List<ChiTietPhieuDatPhongDTO>) ClientConnection.getInstance().sendRequest(
                Request.builder().commandType(CommandType.GET_DS_PHONG_DE_HUY).object(cccd).build()).getObject();

        if (dsChiTiet.isEmpty()) {
            lblSearchMsg.setText("Không tìm thấy phòng nào đang đặt cho CCCD: " + cccd);
            lblSearchMsg.setForeground(RED);
        } else {
            lblSearchMsg.setText("Đã tìm thấy " + dsChiTiet.size() + " phòng hợp lệ.");
            lblSearchMsg.setForeground(GREEN);
        }

        loadDataToTable();
    }

    private void loadDataToTable() {
        tableModel.setRowCount(0);
        lblActionMsg.setText(" "); // Xóa thông báo báo lỗi ở nút khi tải lại bảng

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        for (ChiTietPhieuDatPhongDTO ct : dsChiTiet) {
            tableModel.addRow(new Object[]{
                    false,
                    ct.getPhieuDatPhong() != null ? ct.getPhieuDatPhong().getMaPhieuDatPhong() : "",
                    ct.getPhong() != null ? ct.getPhong().getMaPhong() : "",
                    (ct.getPhong() != null && ct.getPhong().getLoaiPhong() != null)
                            ? ct.getPhong().getLoaiPhong().getTenLoaiPhong()
                            : "",
                    (ct.getPhieuDatPhong() != null && ct.getPhieuDatPhong().getKhachHang() != null)
                            ? ct.getPhieuDatPhong().getKhachHang().getTenKhachHang()
                            : "",
                    (ct.getPhieuDatPhong() != null && ct.getPhieuDatPhong().getKhachHang() != null)
                            ? ct.getPhieuDatPhong().getKhachHang().getCCCD()
                            : "",
                    ct.getThoiGianNhanPhong() != null ? ct.getThoiGianNhanPhong().format(formatter) : "",
                    ct.getThoiGianTraPhong() != null ? ct.getThoiGianTraPhong().format(formatter) : ""
            });
        }
    }

    // =========================================================
    // LOGIC HỦY & HOÀN TIỀN
    // =========================================================
    private void xuLyHuyPhong() {
        List<ChiTietPhieuDatPhongDTO> danhSachPhongBiHuy = new ArrayList<>();
        for (int i = 0; i < tblDanhSach.getRowCount(); i++) {
            Boolean isChecked = (Boolean) tblDanhSach.getValueAt(i, 0);
            if (isChecked != null && isChecked) {
                danhSachPhongBiHuy.add(dsChiTiet.get(i));
            }
        }

        if (danhSachPhongBiHuy.isEmpty()) {
            lblActionMsg.setText("Vui lòng chọn ít nhất một phòng để hủy!");
            lblActionMsg.setForeground(RED);
            return;
        } else {
            lblActionMsg.setText(" ");
        }

        // --- 1. TÍNH TOÁN CỘNG DỒN TIỀN CỌC & HOÀN THEO CHÍNH SÁCH ---
        double tongTienCoc = 0;
        double tongTienHoan = 0;
        LocalDateTime bayGio = LocalDateTime.now();
        java.util.Set<String> dsMaPDP = new java.util.HashSet<>();

        for (ChiTietPhieuDatPhongDTO ct : danhSachPhongBiHuy) {
            if (ct.getPhieuDatPhong() != null) {
                String ma = ct.getPhieuDatPhong().getMaPhieuDatPhong();
                double tienCocCuaPhieu = ct.getPhieuDatPhong().getTienDatCoc();

                // Cộng dồn tiền cọc (Chặn cộng đúp nếu chọn nhiều phòng cùng 1 phiếu)
                if (!dsMaPDP.contains(ma)) {
                    tongTienCoc += tienCocCuaPhieu;
                    dsMaPDP.add(ma);
                }

                // Tính chính sách hoàn tiền
                LocalDateTime checkIn = ct.getThoiGianNhanPhong();
                long gioConLai = java.time.Duration.between(bayGio, checkIn).toHours();
                double phanTramHoan = (gioConLai >= 72) ? 1.0 : (gioConLai >= 24 ? 0.5 : 0.0);

                // Ở đây ta cộng dồn tiền hoàn tính trên cọc của các phòng được chọn
                tongTienHoan += (tienCocCuaPhieu * phanTramHoan);
            }
        }

        java.text.NumberFormat fm = java.text.NumberFormat.getInstance(new java.util.Locale("vi", "VN"));

        // --- 2. GIAO DIỆN MODAL HIỂN THỊ ---
        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(parentWindow, "Xác nhận Hủy Phòng", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(480, 420); // Tăng chiều cao để form thoáng
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        JPanel pnlContent = new JPanel(new BorderLayout(10, 15));
        pnlContent.setBackground(WHITE);
        pnlContent.setBorder(new EmptyBorder(25, 30, 25, 30));

        JLabel lblTitle = lbl("Xác Nhận Hủy & Hoàn Tiền", F_H1.deriveFont(20f), DARK);
        pnlContent.add(lblTitle, BorderLayout.NORTH);

        // Sử dụng BoxLayout trục Y để các thành phần xếp chồng lên nhau full chiều
        // ngang
        JPanel pnlInput = new JPanel();
        pnlInput.setLayout(new BoxLayout(pnlInput, BoxLayout.Y_AXIS));
        pnlInput.setBackground(WHITE);

        // A. LÝ DO
        pnlInput.add(lbl("Lý do hủy phòng (Bắt buộc):", F_LABEL, MID));
        pnlInput.add(Box.createVerticalStrut(5));
        JTextArea txtLyDo = new JTextArea(3, 20);
        txtLyDo.setFont(F_TEXT);
        txtLyDo.setLineWrap(true);
        txtLyDo.setWrapStyleWord(true);
        JScrollPane scrollLyDo = new JScrollPane(txtLyDo);
        scrollLyDo.setBorder(
                BorderFactory.createCompoundBorder(new LineBorder(BORDER, 1, true), new EmptyBorder(5, 8, 5, 8)));
        pnlInput.add(scrollLyDo);

        pnlInput.add(Box.createVerticalStrut(15));

        // B. TIỀN CỌC (CHỈ HIỂN THỊ)
        pnlInput.add(lbl("Tổng tiền cọc cộng dồn:", F_LABEL, MID));
        pnlInput.add(Box.createVerticalStrut(5));
        JTextField txtTienCocDisp = new JTextField(fm.format(tongTienCoc) + " VNĐ");
        txtTienCocDisp.setFont(F_TEXT);
        txtTienCocDisp.setEditable(false);
        txtTienCocDisp.setFocusable(false);
        txtTienCocDisp.setBackground(new Color(0xF8FAFC));
        txtTienCocDisp.setBorder(
                BorderFactory.createCompoundBorder(new LineBorder(BORDER, 1, true), new EmptyBorder(0, 10, 0, 10)));
        txtTienCocDisp.setPreferredSize(new Dimension(0, 40));
        txtTienCocDisp.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        pnlInput.add(txtTienCocDisp);

        pnlInput.add(Box.createVerticalStrut(15));

        // C. TIỀN HOÀN (CHỮ ĐỎ - KHÓA SỬA)
        pnlInput.add(lbl("Số tiền hoàn trả (Theo chính sách):", F_LABEL, DARK));
        pnlInput.add(Box.createVerticalStrut(5));
        JTextField txtHoanDisp = new JTextField(fm.format(tongTienHoan) + " VNĐ");
        txtHoanDisp.setFont(new Font("Segoe UI", Font.BOLD, 15));
        txtHoanDisp.setForeground(RED); // SỐ MÀU ĐỎ
        txtHoanDisp.setEditable(false); // KHÔNG CHO SỬA
        txtHoanDisp.setFocusable(false);
        txtHoanDisp.setBackground(new Color(0xFFF1F1)); // Nền đỏ siêu nhạt
        txtHoanDisp.setBorder(
                BorderFactory.createCompoundBorder(new LineBorder(RED, 1, true), new EmptyBorder(0, 10, 0, 10)));
        txtHoanDisp.setPreferredSize(new Dimension(0, 40));
        txtHoanDisp.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        pnlInput.add(txtHoanDisp);

        pnlContent.add(pnlInput, BorderLayout.CENTER);

        // FOOTER
        JPanel pnlBottom = new JPanel(new BorderLayout());
        pnlBottom.setBackground(WHITE);
        JLabel lblError = lbl(" ", new Font("Segoe UI", Font.ITALIC, 12), RED);
        pnlBottom.add(lblError, BorderLayout.NORTH);

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        pnlButtons.setBackground(WHITE);
        JButton btnCancel = createBtn("Hủy bỏ", 110, 40, MID, new Color(0x323846));
        JButton btnConfirm = createBtn("Xác nhận Hủy", 150, 40, RED, new Color(0xB91C1C));

        final String[] finalLyDo = {null};
        btnCancel.addActionListener(e -> dialog.dispose());

        double finalTongTienHoan = tongTienHoan;
        btnConfirm.addActionListener(e -> {
            String lyDo = txtLyDo.getText().trim();
            if (lyDo.isEmpty()) {
                lblError.setText("Vui lòng nhập lý do hủy phòng!");
                txtLyDo.requestFocus();
            } else {
                finalLyDo[0] = lyDo;
                dialog.dispose();
            }
        });

        pnlButtons.add(btnCancel);
        pnlButtons.add(btnConfirm);
        pnlBottom.add(pnlButtons, BorderLayout.CENTER);
        pnlContent.add(pnlBottom, BorderLayout.SOUTH);

        dialog.setContentPane(pnlContent);
        dialog.setVisible(true);

        // --- 3. XỬ LÝ LƯU DATABASE ---
        if (finalLyDo[0] != null) {
            // Tạo danh sách PhieuHuyPhong DTO từ ChiTietPhieuDatPhong
            List<iuh.dto.PhieuHuyPhongDTO> dsPhieuHuyDTO = new ArrayList<>();
            for (ChiTietPhieuDatPhongDTO ct : danhSachPhongBiHuy) {
                dsPhieuHuyDTO.add(iuh.dto.PhieuHuyPhongDTO.builder()
                        .lyDo(finalLyDo[0])
                        .ngayHuy(LocalDateTime.now())
                        .chiTietPhieuDatPhong(ct)
                        .build());
            }

            // Tạo HuyPhongRequest DTO từ các dữ liệu
            HuyPhongRequest request = HuyPhongRequest.builder()
                    .listPhieuHuy(dsPhieuHuyDTO)
                    .tienHoan(finalTongTienHoan)
                    .build();

            // Gọi service với request DTO
            HuyPhongResultDTO resultDTO = (HuyPhongResultDTO) ClientConnection.getInstance().sendRequest(
                    Request.builder().commandType(CommandType.HUY_PHONG).object(request).build()).getObject();

            // Xử lý response DTO
            if (resultDTO.isSuccess()) {
                dsChiTiet.removeAll(danhSachPhongBiHuy);
                loadDataToTable();
                lblActionMsg.setText("Đã hủy thành công. Đã hoàn: " + fm.format(resultDTO.getTienHoanThuc()) + " VNĐ");
                lblActionMsg.setForeground(GREEN);
            } else {
                lblActionMsg.setText(resultDTO.getMessage());
                lblActionMsg.setForeground(RED);
            }
        }
    }

    // =========================================================
    // CÁC HÀM TIỆN ÍCH VẼ GIAO DIỆN
    // =========================================================
    static JLabel lbl(String text, Font f, Color c) {
        JLabel l = new JLabel(text);
        l.setFont(f);
        l.setForeground(c);
        return l;
    }

    static JButton blueBtn(String text, int w, int h) {
        return createBtn(text, w, h, BLUE, new Color(0x2A5CD4));
    }

    static JButton redBtn(String text, int w, int h) {
        return createBtn(text, w, h, RED, new Color(0xB91C1C));
    }

    private static JButton createBtn(String text, int w, int h, Color normal, Color hover) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? hover : normal);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(WHITE);
                g2.setFont(F_BOLD12);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                        (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(w, h));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void styleTable(JTable table) {
        table.setFont(F_TABLE);
        table.setRowHeight(42);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(BORDER);
        table.setSelectionBackground(new Color(0xE8EFFE));
        table.setSelectionForeground(DARK);

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setFont(F_LABEL);
                c.setBackground(WHITE);
                c.setForeground(MID);
                setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER),
                        new EmptyBorder(0, 10, 0, 10)));
                setHorizontalAlignment(column == 0 ? SwingConstants.CENTER : SwingConstants.LEFT);
                return c;
            }
        };

        DefaultTableCellRenderer textRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBorder(new EmptyBorder(0, 10, 0, 10));
                return c;
            }
        };

        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(0, 45));
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
            if (i > 0)
                table.getColumnModel().getColumn(i).setCellRenderer(textRenderer);
        }

        table.getColumnModel().getColumn(0).setMaxWidth(60);
        table.getColumnModel().getColumn(0).setMinWidth(60);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(80);
        table.getColumnModel().getColumn(3).setPreferredWidth(130);
        table.getColumnModel().getColumn(4).setPreferredWidth(180);
        table.getColumnModel().getColumn(5).setPreferredWidth(130);
        table.getColumnModel().getColumn(6).setPreferredWidth(140);
        table.getColumnModel().getColumn(7).setPreferredWidth(140);
    }
}