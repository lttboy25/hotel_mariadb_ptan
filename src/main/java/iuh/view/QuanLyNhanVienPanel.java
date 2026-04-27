package iuh.view;

import iuh.entity.KhuyenMai;
import iuh.entity.NhanVien;
import iuh.entity.TrangThai;
import iuh.entity.TrangThaiNhanVien;
import iuh.service.KhuyenMaiService;
import iuh.service.NhanVienService;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;



public class QuanLyNhanVienPanel extends JPanel {
    // ── CONSTANTS (Dùng chung cho project) ──────────────────────────────────
    public static final Color BG_MAIN       = new Color(0xF4F6FB);
    public static final Color BG_WHITE      = Color.WHITE;
    public static final Color BLUE          = new Color(0x3B6FF0);
    public static final Color BLUE_HOVER    = new Color(0x2D5FD8);
    public static final Color BLUE_LIGHT    = new Color(0xEBF0FF);
    public static final Color TEXT_DARK     = new Color(0x1A1A2E);
    public static final Color TEXT_MID      = new Color(0x4A5268);
    public static final Color TEXT_GRAY     = new Color(0xA0A8B8);
    public static final Color BORDER_COL    = new Color(0xE4E9F2);
    public static final Color HEADER_BG     = new Color(0xF8F9FD);
    public static final Color ROW_ALT       = new Color(0xFAFBFD);
    public static final Color ROW_SEL       = new Color(0xEBF0FF);
    public static final Color GREEN         = new Color(0x27AE60);
    public static final Color GREEN_BG      = new Color(0xE8F8EF);
    public static final Color RED           = new Color(0xE04040);
    public static final Color RED_BG        = new Color(0xFFF0F0);
    public static final Color ORANGE        = new Color(0xF39C12);
    public static final Color ORANGE_BG     = new Color(0xFFF8EC);

    public static final Font F_TITLE   = new Font("Segoe UI", Font.BOLD,  20);
    public static final Font F_LABEL   = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font F_BOLD13  = new Font("Segoe UI", Font.BOLD,  13);
    public static final Font F_TABLE   = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font F_TABLE_H = new Font("Segoe UI", Font.BOLD,  12);
    public static final Font F_SMALL   = new Font("Segoe UI", Font.PLAIN, 11);

    // Thêm cột ẩn "Địa chỉ" vào cuối mảng COLUMNS để TableModel biết có 10 cột
    static final String[] COLUMNS = {
            "Mã NV", "CCCD", "Họ và tên", "Giới tính", "Ngày sinh", "Số điện thoại", "Email", "Ngày bắt đầu", "Trạng thái", "Địa chỉ"
    };

    private final NhanVienService nhanVienService = new NhanVienService();
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField tfSearch;

    public QuanLyNhanVienPanel() {
        setLayout(new BorderLayout(0, 0));
        setBackground(BG_MAIN);
        setBorder(new EmptyBorder(28, 28, 28, 28));

        JLabel title = new JLabel("Quản lý nhân viên");
        title.setFont(F_TITLE);
        title.setForeground(TEXT_DARK);
        title.setBorder(new EmptyBorder(0, 0, 18, 0));
        add(title, BorderLayout.NORTH);

        JPanel content = new ThanhToanPanel.RoundedPanel(14, BG_WHITE);
        content.setLayout(new BorderLayout(0, 0));
        content.add(buildToolbar(), BorderLayout.NORTH);
        content.add(buildTable(),   BorderLayout.CENTER);

        add(content, BorderLayout.CENTER);
        refreshTable();
    }



    private void refreshTable() {
        String keyword = tfSearch.getText().trim();
        List<NhanVien> list = (keyword.isBlank() || keyword.equals("Tìm kiếm nhân viên..."))
                ? nhanVienService.getAllNhanVien()
                : nhanVienService.searchNhanVienByName(keyword);

        tableModel.setRowCount(0);
        for (NhanVien nv : list) {
            tableModel.addRow(toRow(nv));
        }
    }

    private Object[] toRow(NhanVien nv) {
        return new Object[]{
                nv.getMaNhanVien(),   // 0
                nv.getCCCD(),         // 1
                nv.getTenNhanVien(),  // 2
                nv.isGioiTinh() ? "Nam" : "Nữ", // 3
                nv.getNgaySinh(),     // 4
                nv.getSoDienThoai(),  // 5
                nv.getEmail(),        // 6
                nv.getNgayBatDau(),   // 7
                nv.getTrangThai(),    // 8
                nv.getDiaChi()        // 9 - Cột này sẽ bị ẩn đi nhưng dữ liệu vẫn tồn tại
        };
    }

    private JPanel buildToolbar() {
        JPanel bar = new JPanel(new BorderLayout(12, 0));
        bar.setOpaque(false);
        bar.setBorder(new EmptyBorder(16, 20, 12, 20));

        tfSearch = new JTextField("Tìm kiếm nhân viên...", 22);
        tfSearch.setFont(F_LABEL);
        tfSearch.setForeground(TEXT_GRAY);
        tfSearch.setPreferredSize(new Dimension(240, 38));
        tfSearch.setBorder(new CompoundBorder(new LineBorder(BORDER_COL, 1, true), new EmptyBorder(0, 12, 0, 12)));
        tfSearch.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                if (tfSearch.getText().equals("Tìm kiếm nhân viên...")) {
                    tfSearch.setText(""); tfSearch.setForeground(TEXT_DARK);
                }
            }
            @Override public void focusLost(FocusEvent e) {
                if (tfSearch.getText().isEmpty()) {
                    tfSearch.setText("Tìm kiếm nhân viên..."); tfSearch.setForeground(TEXT_GRAY);
                }
            }
        });

        JButton btnSearch = createButton("Tìm kiếm", BLUE, BLUE_HOVER, Color.WHITE, 100, 38);
        btnSearch.addActionListener(e -> refreshTable());
        JButton btnRefresh = createButton("Làm mới", new Color(0x6C757D), new Color(0x5A6268), Color.WHITE, 100, 38);

        btnRefresh.addActionListener(e -> {
            tfSearch.setText("Tìm kiếm nhân viên...");
            tfSearch.setForeground(TEXT_GRAY);
            table.clearSelection();
            refreshTable();
        });

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        left.setOpaque(false);
        left.add(tfSearch);
        left.add(btnSearch);
        left.add(btnRefresh);

        JButton btnAdd = createButton("+ Thêm nhân viên", GREEN, new Color(0x1E8449), Color.WHITE, 160, 38);
        btnAdd.addActionListener(e -> openModal(null));

        bar.add(left, BorderLayout.WEST);
        bar.add(btnAdd, BorderLayout.EAST);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(bar, BorderLayout.CENTER);
        wrapper.add(new JSeparator() {{ setForeground(BORDER_COL); }}, BorderLayout.SOUTH);
        return wrapper;
    }

    private JScrollPane buildTable() {
        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setFont(F_TABLE);
        table.setRowHeight(52);
        table.setShowGrid(false);
        table.setSelectionBackground(ROW_SEL);
        table.setSelectionForeground(TEXT_DARK);

        // ẨN CỘT ĐỊA CHỈ (Index 9) KHỎI GIAO DIỆN NHƯNG GIỮ LẠI DỮ LIỆU
        table.getColumnModel().removeColumn(table.getColumnModel().getColumn(9));

        JTableHeader header = table.getTableHeader();
        header.setFont(F_TABLE_H);
        header.setBackground(HEADER_BG);
        header.setPreferredSize(new Dimension(0, 42));

        DefaultTableCellRenderer baseRenderer = new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                setBackground(sel ? ROW_SEL : (row % 2 == 0 ? BG_WHITE : ROW_ALT));
                setForeground(col == 0 ? BLUE : TEXT_DARK);
                setBorder(new EmptyBorder(0, 16, 0, 8));
                return this;
            }
        };

        DefaultTableCellRenderer statusRenderer = new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int row, int col) {
                TrangThaiNhanVien status = (val instanceof TrangThaiNhanVien) ? (TrangThaiNhanVien) val : null;
                String text = (status != null) ? status.getDisplay() : (val != null ? val.toString() : "");

                JPanel cell = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 11));
                cell.setBackground(sel ? ROW_SEL : (row % 2 == 0 ? BG_WHITE : ROW_ALT));
                cell.setBorder(new EmptyBorder(0, 16, 0, 8));

                Color bg = RED_BG, fg = RED;
                if (status == TrangThaiNhanVien.DANG_LAM) { bg = GREEN_BG; fg = GREEN; }
                else if (status == TrangThaiNhanVien.MOI_VAO || status == TrangThaiNhanVien.THU_VIEC) { bg = ORANGE_BG; fg = ORANGE; }

                Color finalBg = bg;
                JLabel pill = new JLabel(text) {
                    @Override protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(finalBg);
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                        g2.dispose();
                        super.paintComponent(g);
                    }
                };
                pill.setFont(new Font("Segoe UI", Font.BOLD, 11));
                pill.setForeground(fg);
                pill.setBorder(new EmptyBorder(3, 10, 3, 10));
                cell.add(pill);
                return cell;
            }
        };

        // Gán renderer cho các cột đang hiển thị (0-8)
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(i == 8 ? statusRenderer : baseRenderer);
        }

        table.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) openModal(table.getSelectedRow());
            }
        });

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setBackground(BG_WHITE);
        return sp;
    }


    private void openModal(Integer row) {
        Object[] data = null;
        if (row != null) {
            data = new Object[10];
            for (int c = 0; c < 10; c++) {
                data[c] = tableModel.getValueAt(row, c);
            }
        }
        // Khởi tạo modal
        boolean isNew = (row == null);

        NhanVien nv = null;

        if (row != null) {
            nv = nhanVienService
                    .getNhanVienById(tableModel.getValueAt(row, 0).toString())
                    .orElse(null);
        }

        NhanVienModal modal = new NhanVienModal(
                (JFrame) SwingUtilities.getWindowAncestor(this),
                data,
                nv,
                isNew,
                null // autoGeneratedCode bạn đã xử lý bên trong rồi
        );
        modal.setOnChanged(this::refreshTable);

        modal.setVisible(true);
    }

    public static JButton createButton(String text, Color bg, Color hover, Color fg, int w, int h) {
        JButton btn = new JButton(text) {
            private boolean isHover = false;
            {
                addMouseListener(new MouseAdapter() {
                    @Override public void mouseEntered(MouseEvent e) { isHover = true; repaint(); }
                    @Override public void mouseExited(MouseEvent e) { isHover = false; repaint(); }
                });
            }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(isHover ? hover : bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(F_BOLD13); btn.setForeground(fg);
        btn.setContentAreaFilled(false); btn.setBorderPainted(false); btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(w, h)); btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}

// ── CLASS MODAL (Giữ nguyên logic xử lý Enum) ────────────────────────────────
class NhanVienModal extends JDialog {
    private JTextField tfMaNV, tfCCCD, tfHoTen, tfEmail, tfSDT, tfNgaySinh, tfNgayBatDau, tfDiaChi;
    private JComboBox<String> cbGioiTinh;
    private JComboBox<TrangThaiNhanVien> cbTrangThai;
    private Runnable onChanged;
    private final boolean isNew;
    private final String autoGeneratedCode;
    private final NhanVien current;
    private final NhanVienService nhanVienService = new NhanVienService();



    public void setOnChanged(Runnable onChanged) {
        this.onChanged = onChanged;
    }

    private void onSave() {
        try {
            NhanVien nv = collectFormData();
            nhanVienService.addNhanVienAutoCode(nv);
            notifyChangedAndClose("Đã thêm nhan vien.");
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void onUpdate() {
        try {
            NhanVien nv = collectFormData();
            nhanVienService.updateNhanVien(nv);
            notifyChangedAndClose("Đã cập nhật nhân viên.");
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private NhanVien collectFormData() {
        String ma = isNew ? autoGeneratedCode : tfMaNV.getText().trim();
        String cccd = tfCCCD.getText().trim();
        String ten = tfHoTen.getText().trim();

        String gt = cbGioiTinh.getSelectedItem().toString();
        boolean gioiTinh = gt.equalsIgnoreCase("Nam");

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        LocalDate ngaySinh = LocalDate.parse(tfNgaySinh.getText());
        LocalDate ngayBatDau = LocalDate.parse(tfNgayBatDau.getText());

        String email = tfEmail.getText().trim();
        String sdt = tfSDT.getText().trim();
        TrangThaiNhanVien trangThai = (TrangThaiNhanVien) cbTrangThai.getSelectedItem();
        String diaChi = tfDiaChi.getText().trim();

        if (ma.isBlank()) throw new IllegalArgumentException("Mã nhân viên không được để trống.");
        if (ten.isBlank()) throw new IllegalArgumentException("Tên nhân viên không được để trống.");

        return NhanVien.builder()
                .maNhanVien(ma)
                .tenNhanVien(ten)
                .CCCD(cccd)
                .gioiTinh(gioiTinh)
                .email(email)
                .soDienThoai(sdt)
                .ngayBatDau(ngayBatDau)
                .ngaySinh(ngaySinh)
                .trangThai(trangThai)
                .diaChi(diaChi)
                .build();
    }

    private void onDelete() {
        int result = JOptionPane.showConfirmDialog(this, "Xóa nhân viên này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (result != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            nhanVienService.deleteNhanVien(tfMaNV.getText().trim());
            notifyChangedAndClose("Đã xóa nhân viên.");
        } catch (Exception ex) {
            showError(ex);
        }
    }
    private void showError(Exception ex) {
        JOptionPane.showMessageDialog(this,
                ex.getMessage() == null ? ex.toString() : ex.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    private void notifyChangedAndClose(String message) {
        JOptionPane.showMessageDialog(this, message, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        if (onChanged != null) {
            onChanged.run();
        }
        dispose();
    }


    NhanVienModal(JFrame owner, Object[] data, NhanVien nhanVien, boolean isNew, String autoGeneratedCode) {
        super(owner, isNew ? "Thêm nhân viên" : "Chi tiết nhân viên", true);
        setSize(620, 680); // Tăng chiều cao một chút để thoải mái
        setLocationRelativeTo(owner);
        setResizable(false);
        this.current = nhanVien == null ? new NhanVien() : nhanVien;
        this.isNew = isNew;
        this.autoGeneratedCode = isNew ? nhanVienService.generateNextMaNhanVien() : this.current.getMaNhanVien();

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);

        // 1. Header Section
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(new EmptyBorder(25, 30, 10, 30));

        JLabel lblTitle = new JLabel(isNew ? "Thêm nhân viên mới" : "Thông tin nhân viên");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(0x1A1A2E));

        JLabel lblSub = new JLabel("Xem và chỉnh sửa thông tin chi tiết");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(Color.GRAY);

        JPanel titleGroup = new JPanel(new GridLayout(2, 1, 0, 5));
        titleGroup.setOpaque(false);
        titleGroup.add(lblTitle);
        titleGroup.add(lblSub);
        header.add(titleGroup, BorderLayout.CENTER);

        // 2. Form Section
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(new EmptyBorder(10, 30, 20, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 15); // Khoảng cách giữa các ô
        gbc.weightx = 1.0; // Quan trọng: Bắt các thành phần chiếm hết chiều ngang

        // Khởi tạo các fields
        tfMaNV = createField(safeGet(data, 0)); tfMaNV.setEditable(false);
        tfCCCD = createField(safeGet(data, 1));
        tfHoTen = createField(safeGet(data, 2));

        cbGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"});
        styleComboBox(cbGioiTinh);
        if (data != null) cbGioiTinh.setSelectedItem(safeGet(data, 3));

        tfNgaySinh = createField(safeGet(data, 4));
        tfSDT = createField(safeGet(data, 5));
        tfEmail = createField(safeGet(data, 6));
        tfNgayBatDau = createField(safeGet(data, 7));

        cbTrangThai = new JComboBox<>(TrangThaiNhanVien.values());
        styleComboBox(cbTrangThai);
        cbTrangThai.setRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(JList<?> l, Object v, int i, boolean s, boolean f) {
                super.getListCellRendererComponent(l, v, i, s, f);
                if (v instanceof TrangThaiNhanVien) setText(((TrangThaiNhanVien) v).getDisplay());
                return this;
            }
        });
        if (data != null && data[8] instanceof TrangThaiNhanVien) cbTrangThai.setSelectedItem(data[8]);

        tfDiaChi = createField(safeGet(data, 9));

        // Add components vào Form
        addFormRow(form, gbc, 0, "Mã nhân viên", tfMaNV, "CCCD / CMND", tfCCCD);
        addFormFull(form, gbc, 1, "Họ và tên", tfHoTen);
        addFormRow(form, gbc, 2, "Giới tính", cbGioiTinh, "Ngày sinh", tfNgaySinh);
        addFormRow(form, gbc, 3, "Số điện thoại", tfSDT, "Email", tfEmail);
        addFormRow(form, gbc, 4, "Ngày bắt đầu", tfNgayBatDau, "Trạng thái", cbTrangThai);
        addFormFull(form, gbc, 5, "Địa chỉ", tfDiaChi);

        // 3. Footer Section
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 20));
        footer.setBackground(new Color(0xF8F9FD)); // Màu nền footer khác biệt một chút
        footer.setBorder(new MatteBorder(1, 0, 0, 0, new Color(0xE4E9F2)));

        JButton btnDelete = QuanLyNhanVienPanel.createButton("Xóa", new Color(0xE04040), new Color(0xC03030), Color.WHITE, 100, 42);
        btnDelete.addActionListener(e -> onDelete());
        JButton btnCancel = QuanLyNhanVienPanel.createButton("Hủy", new Color(0xEEF0F5), new Color(0xDDE0E8), new Color(0x4A5268), 90, 42);
        JButton btnSave = QuanLyNhanVienPanel.createButton(isNew ? "Lưu" : "Cập nhật", QuanLyNhanVienPanel.BLUE, QuanLyNhanVienPanel.BLUE_HOVER, Color.WHITE, 120, 42);
        btnSave.addActionListener(e -> onUpdate());
        btnSave.addActionListener(e->onSave());

        if (!isNew) footer.add(btnDelete); // Chỉ hiện nút xóa khi sửa
        footer.add(btnCancel);
        footer.add(btnSave);

        btnCancel.addActionListener(e -> dispose());

        root.add(header, BorderLayout.NORTH);
        root.add(form, BorderLayout.CENTER);
        root.add(footer, BorderLayout.SOUTH);
        setContentPane(root);
    }

    private void styleComboBox(JComboBox<?> cb) {
        cb.setPreferredSize(new Dimension(0, 40));
        cb.setBackground(Color.WHITE);
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    }

    private String safeGet(Object[] arr, int idx) {
        if (arr == null || idx >= arr.length || arr[idx] == null) return "";
        if (arr[idx] instanceof TrangThaiNhanVien) return ((TrangThaiNhanVien) arr[idx]).getDisplay();
        return arr[idx].toString();
    }

    private JTextField createField(String t) {
        JTextField f = new JTextField(t);
        f.setPreferredSize(new Dimension(0, 40));
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        f.setBorder(new CompoundBorder(new LineBorder(new Color(0xDCDFE6), 1, true), new EmptyBorder(0, 12, 0, 12)));
        return f;
    }

    private void addFormRow(JPanel p, GridBagConstraints c, int r, String l1, JComponent f1, String l2, JComponent f2) {
        JLabel lbl1 = new JLabel(l1); lbl1.setFont(new Font("Segoe UI", Font.BOLD, 13));
        JLabel lbl2 = new JLabel(l2); lbl2.setFont(new Font("Segoe UI", Font.BOLD, 13));

        c.gridwidth = 1;
        c.gridy = r * 2; c.gridx = 0; p.add(lbl1, c);
        c.gridx = 1; p.add(lbl2, c);

        c.gridy = r * 2 + 1; c.gridx = 0; p.add(f1, c);
        c.gridx = 1; p.add(f2, c);
    }

    private void addFormFull(JPanel p, GridBagConstraints c, int r, String l, JComponent f) {
        JLabel lbl = new JLabel(l); lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        c.gridwidth = 2;
        c.gridy = r * 2; c.gridx = 0; p.add(lbl, c);
        c.gridy = r * 2 + 1; p.add(f, c);
    }

}