package iuh.view;

import iuh.dto.NhanVienDTO;
import iuh.entity.TrangThaiNhanVien;
import iuh.network.ClientConnection;
import iuh.network.CommandType;
import iuh.network.Request;
import iuh.network.Response;
import iuh.service.impl.NhanVienServiceImpl;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public class QuanLyNhanVienPanel extends JPanel {
    public static final Color BG_MAIN = new Color(0xF4F6FB);
    public static final Color BG_WHITE = Color.WHITE;
    public static final Color BLUE = new Color(0x3B6FF0);
    public static final Color BLUE_HOVER = new Color(0x2D5FD8);
    public static final Color BLUE_LIGHT = new Color(0xEBF0FF);
    public static final Color TEXT_DARK = new Color(0x1A1A2E);
    public static final Color TEXT_MID = new Color(0x4A5268);
    public static final Color TEXT_GRAY = new Color(0xA0A8B8);
    public static final Color BORDER_COL = new Color(0xE4E9F2);
    public static final Color HEADER_BG = new Color(0xF8F9FD);
    public static final Color ROW_ALT = new Color(0xFAFBFD);
    public static final Color ROW_SEL = new Color(0xEBF0FF);
    public static final Color GREEN = new Color(0x27AE60);
    public static final Color GREEN_BG = new Color(0xE8F8EF);
    public static final Color RED = new Color(0xE04040);
    public static final Color RED_BG = new Color(0xFFF0F0);
    public static final Color ORANGE = new Color(0xF39C12);
    public static final Color ORANGE_BG = new Color(0xFFF8EC);
    public static final Font F_TITLE = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font F_LABEL = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font F_BOLD13 = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font F_TABLE = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font F_TABLE_H = new Font("Segoe UI", Font.BOLD, 12);
    public static final Font F_SMALL = new Font("Segoe UI", Font.PLAIN, 11);

    private static final String[] COLUMNS = {
            "Mã NV", "CCCD", "Họ tên", "Giới tính", "Ngày sinh",
            "Số điện thoại", "Email", "Ngày bắt đầu", "Trạng thái", "Địa chỉ"
    };

    private final DefaultTableModel tableModel = new DefaultTableModel(COLUMNS, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private JTable table;
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

        JPanel content = new TaiKhoanPanel.RoundedCard(14);
        content.setLayout(new BorderLayout());
        content.add(buildToolbar(), BorderLayout.NORTH);
        content.add(buildTable(), BorderLayout.CENTER);
        add(content, BorderLayout.CENTER);

        refreshTable();
    }

    private JPanel buildToolbar() {
        JPanel bar = new JPanel(new BorderLayout(12, 0));
        bar.setOpaque(false);
        bar.setBorder(new EmptyBorder(16, 20, 12, 20));

        tfSearch = new JTextField();
        tfSearch.setPreferredSize(new Dimension(240, 38));
        tfSearch.setBorder(new CompoundBorder(new LineBorder(BORDER_COL, 1, true), new EmptyBorder(0, 12, 0, 12)));

        JButton btnSearch = createButton("Tìm kiếm", BLUE, BLUE_HOVER, Color.WHITE, 100, 38);
        btnSearch.addActionListener(e -> refreshTable());

        JButton btnRefresh = createButton("Làm mới", new Color(0x6C757D), new Color(0x5A6268), Color.WHITE, 100, 38);
        btnRefresh.addActionListener(e -> {
            tfSearch.setText("");
            table.clearSelection();
            refreshTable();
        });

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        left.setOpaque(false);
        left.add(tfSearch);
        left.add(btnSearch);
        left.add(btnRefresh);

        JButton btnAdd = createButton("+ Thêm nhân viên", GREEN, new Color(0x1E8449), Color.WHITE, 170, 38);
        btnAdd.addActionListener(e -> openModal(null));

        bar.add(left, BorderLayout.WEST);
        bar.add(btnAdd, BorderLayout.EAST);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(bar, BorderLayout.CENTER);
        wrapper.add(new JSeparator(), BorderLayout.SOUTH);
        return wrapper;
    }

    private JScrollPane buildTable() {
        table = new JTable(tableModel);
        table.setFont(F_TABLE);
        table.setRowHeight(42);
        table.setSelectionBackground(new Color(0xEBF0FF));
        table.setSelectionForeground(TEXT_DARK);
        table.setShowGrid(false);
        table.setAutoCreateRowSorter(true);

        JTableHeader header = table.getTableHeader();
        header.setFont(F_TABLE_H);
        header.setBackground(HEADER_BG);
        header.setPreferredSize(new Dimension(0, 38));

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && table.getSelectedRow() >= 0) {
                    openModal(table.convertRowIndexToModel(table.getSelectedRow()));
                }
            }
        });

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setBackground(BG_WHITE);
        return sp;
    }

    private void refreshTable() {
        String keyword = tfSearch.getText().trim();
        List<NhanVienDTO> list;

        if (keyword.isBlank()) {
            Request request = Request.builder()
                    .commandType(CommandType.GET_ALL_NHAN_VIEN)
                    .build();
            Response response = ClientConnection.getInstance().sendRequest(request);
            //@SuppressWarnings("unchecked")
                    list = (List<NhanVienDTO>) response.getObject();
        } else {
            Request request = Request.builder()
                    .commandType(CommandType.SEARCH_NHAN_VIEN)
                    .object(keyword)
                    .build();
            Response response = ClientConnection.getInstance().sendRequest(request);
            //@SuppressWarnings("unchecked")
                    list = (List<NhanVienDTO>) response.getObject();
        }

        tableModel.setRowCount(0);
        if (list != null) {
            for (NhanVienDTO nv : list) {
                tableModel.addRow(toRow(nv));
            }
        }
    }

    private Object[] toRow(NhanVienDTO nv) {
        return new Object[]{
                nv.getMaNhanVien(),
                nv.getCCCD(),
                nv.getTenNhanVien(),
                nv.isGioiTinh() ? "Nam" : "Nữ",
                nv.getNgaySinh(),
                nv.getSoDienThoai(),
                nv.getEmail(),
                nv.getNgayBatDau(),
                nv.getTrangThai(),
                nv.getDiaChi()
        };
    }

    private void openModal(Integer row) {
        NhanVienDTO nv = null;
        if (row != null) {
            String maNV = String.valueOf(tableModel.getValueAt(row, 0));
            Request request = Request.builder()
                    .commandType(CommandType.GET_NHAN_VIEN_BY_ID)
                    .object(maNV)
                    .build();
            Response response = ClientConnection.getInstance().sendRequest(request);
            nv = (NhanVienDTO) response.getObject();
        }

        NhanVienModal modal = new NhanVienModal(
                (JFrame) SwingUtilities.getWindowAncestor(this),
                nv,
                row == null
        );
        modal.setOnChanged(this::refreshTable);
        modal.setVisible(true);
    }

    public static JButton createButton(String text, Color bg, Color hover, Color fg, int w, int h) {
        JButton btn = new JButton(text) {
            private boolean hoverState = false;

            {
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        hoverState = true;
                        repaint();
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        hoverState = false;
                        repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(hoverState ? hover : bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(F_BOLD13);
        btn.setForeground(fg);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(w, h));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}

class NhanVienModal extends JDialog {
    private final NhanVienServiceImpl nhanVienServiceImpl = new NhanVienServiceImpl();
    private final boolean isNew;
    private final NhanVienDTO current;
    private Runnable onChanged;

    private JTextField tfMaNV;
    private JTextField tfCCCD;
    private JTextField tfHoTen;
    private JComboBox<String> cbGioiTinh;
    private JSpinner spNgaySinh;
    private JTextField tfSDT;
    private JTextField tfEmail;
    private JSpinner spNgayBatDau;
    private JComboBox<TrangThaiNhanVien> cbTrangThai;
    private JTextField tfDiaChi;

    private void validateForm() {
        String ten = tfHoTen.getText().trim();
        String cccd = tfCCCD.getText().trim();
        String sdt = tfSDT.getText().trim();
        String email = tfEmail.getText().trim();

        if (ten.isBlank()) {
            throw new IllegalArgumentException("Tên nhân viên không được để trống");
        }

        if (cccd.isBlank()) {
            throw new IllegalArgumentException("CCCD không được để trống");
        }

        if (!cccd.matches("\\d{12}")) {
            throw new IllegalArgumentException("CCCD phải gồm đúng 12 số");
        }

        if (sdt.isBlank()) {
            throw new IllegalArgumentException("Số điện thoại không được để trống");
        }

        if (!sdt.matches("0\\d{9}")) {
            throw new IllegalArgumentException("SĐT phải 10 số và bắt đầu bằng 0");
        }

        if (email.isBlank()) {
            throw new IllegalArgumentException("Email không được để trống");
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Email không hợp lệ");
        }
    }

    NhanVienModal(JFrame owner, NhanVienDTO nhanVien, boolean isNew) {
        super(owner, isNew ? "Thêm nhân viên" : "Cập nhật nhân viên", true);
        this.isNew = isNew;
        this.current = nhanVien;

        setSize(620, 620);
        setLocationRelativeTo(owner);
        setResizable(false);
        setContentPane(buildContent());
        fillData();
    }

    public void setOnChanged(Runnable onChanged) {
        this.onChanged = onChanged;
    }

    private JPanel buildContent() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(new EmptyBorder(25, 30, 10, 30));
        JLabel lblTitle = new JLabel(isNew ? "Thêm nhân viên mới" : "Thông tin nhân viên");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        header.add(lblTitle, BorderLayout.CENTER);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(new EmptyBorder(10, 30, 20, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 15);
        gbc.weightx = 1.0;

        tfMaNV = createField();
        tfMaNV.setEditable(false);
        tfCCCD = createField();
        tfHoTen = createField();
        cbGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"});
        spNgaySinh = createDateSpinner();
        tfSDT = createField();
        tfEmail = createField();
        spNgayBatDau = createDateSpinner();
        cbTrangThai = new JComboBox<>(TrangThaiNhanVien.values());
        tfDiaChi = createField();

        addFormRow(form, gbc, 0, "Mã nhân viên", tfMaNV, "CCCD", tfCCCD);
        addFormFull(form, gbc, 1, "Họ và tên", tfHoTen);
        addFormRow(form, gbc, 2, "Giới tính", cbGioiTinh, "Ngày sinh", spNgaySinh);
        addFormRow(form, gbc, 3, "Số điện thoại", tfSDT, "Email", tfEmail);
        addFormRow(form, gbc, 4, "Ngày bắt đầu", spNgayBatDau, "Trạng thái", cbTrangThai);
        addFormFull(form, gbc, 5, "Địa chỉ", tfDiaChi);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 20));
        footer.setBackground(new Color(0xF8F9FD));
        footer.setBorder(new MatteBorder(1, 0, 0, 0, new Color(0xE4E9F2)));

        if (!isNew) {
            JButton btnDelete = QuanLyNhanVienPanel.createButton("Xóa", new Color(0xE04040), new Color(0xC03030), Color.WHITE, 100, 42);
            btnDelete.addActionListener(e -> onDelete());
            footer.add(btnDelete);
        }

        JButton btnCancel = QuanLyNhanVienPanel.createButton("Hủy", new Color(0xEEF0F5), new Color(0xDDE0E8), new Color(0x4A5268), 90, 42);
        btnCancel.addActionListener(e -> dispose());

        JButton btnSave = QuanLyNhanVienPanel.createButton(isNew ? "Lưu" : "Cập nhật", QuanLyNhanVienPanel.BLUE, QuanLyNhanVienPanel.BLUE_HOVER, Color.WHITE, 120, 42);
        btnSave.addActionListener(e -> {
            if (isNew) {
                onSave();
            } else {
                onUpdate();
            }
        });

        footer.add(btnCancel);
        footer.add(btnSave);

        root.add(header, BorderLayout.NORTH);
        root.add(form, BorderLayout.CENTER);
        root.add(footer, BorderLayout.SOUTH);
        return root;
    }

    private void fillData() {
        if (isNew) {
            tfMaNV.setText(nhanVienServiceImpl.generateNextMaNhanVien());
            return;
        }

        if (current == null) {
            return;
        }

        tfMaNV.setText(current.getMaNhanVien());
        tfCCCD.setText(value(current.getCCCD()));
        tfHoTen.setText(value(current.getTenNhanVien()));
        cbGioiTinh.setSelectedItem(current.isGioiTinh() ? "Nam" : "Nữ");
        if (current.getNgaySinh() != null) {
            spNgaySinh.setValue(java.sql.Date.valueOf(current.getNgaySinh()));
        }
        tfSDT.setText(value(current.getSoDienThoai()));
        tfEmail.setText(value(current.getEmail()));
        if (current.getNgayBatDau() != null) {
            spNgayBatDau.setValue(java.sql.Date.valueOf(current.getNgayBatDau()));
        }
        cbTrangThai.setSelectedItem(current.getTrangThai());
        tfDiaChi.setText(value(current.getDiaChi()));
    }

    private void onSave() {
        try {
            validateForm();
            NhanVienDTO saved = nhanVienServiceImpl.addNhanVienAutoCode(collectFormData());
            String matKhauMacDinh = nhanVienServiceImpl.taoMatKhauMacDinh(saved.getMaNhanVien());
            notifyChangedAndClose("Đã thêm nhân viên. Mật khẩu mặc định: " + matKhauMacDinh);
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void onUpdate() {
        try {
            validateForm();
            nhanVienServiceImpl.updateNhanVien(collectFormData());
            notifyChangedAndClose("Đã cập nhật nhân viên.");
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void onDelete() {
        int result = JOptionPane.showConfirmDialog(this, "Xóa nhân viên này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (result != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            nhanVienServiceImpl.deleteNhanVien(tfMaNV.getText().trim());
            notifyChangedAndClose("Đã xóa nhân viên.");
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private NhanVienDTO collectFormData() {
        String ma = isNew ? tfMaNV.getText().trim() : tfMaNV.getText().trim();
        String ten = tfHoTen.getText().trim();
        if (ma.isBlank()) {
            throw new IllegalArgumentException("Mã nhân viên không được để trống.");
        }
        if (ten.isBlank()) {
            throw new IllegalArgumentException("Tên nhân viên không được để trống.");
        }

        return NhanVienDTO.builder()
                .maNhanVien(ma)
                .CCCD(tfCCCD.getText().trim())
                .tenNhanVien(ten)
                .gioiTinh("Nam".equals(cbGioiTinh.getSelectedItem()))
                .ngaySinh(spinnerToLocalDate(spNgaySinh))
                .soDienThoai(tfSDT.getText().trim())
                .email(tfEmail.getText().trim())
                .ngayBatDau(spinnerToLocalDate(spNgayBatDau))
                .trangThai((TrangThaiNhanVien) cbTrangThai.getSelectedItem())
                .diaChi(tfDiaChi.getText().trim())
                .build();
    }

    private JTextField createField() {
        JTextField f = new JTextField();
        f.setPreferredSize(new Dimension(0, 40));
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        f.setBorder(new CompoundBorder(new LineBorder(new Color(0xDCDFE6), 1, true), new EmptyBorder(0, 12, 0, 12)));
        return f;
    }

    private JSpinner createDateSpinner() {
        JSpinner spinner = new JSpinner(new SpinnerDateModel());
        spinner.setEditor(new JSpinner.DateEditor(spinner, "dd/MM/yyyy"));
        spinner.setPreferredSize(new Dimension(0, 40));
        return spinner;
    }

    private LocalDate spinnerToLocalDate(JSpinner spinner) {
        Object value = spinner.getValue();
        if (!(value instanceof java.util.Date date)) {
            return null;
        }
        return Instant.ofEpochMilli(date.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    private void addFormRow(JPanel p, GridBagConstraints c, int r, String l1, JComponent f1, String l2, JComponent f2) {
        JLabel lbl1 = new JLabel(l1);
        lbl1.setFont(new Font("Segoe UI", Font.BOLD, 13));
        JLabel lbl2 = new JLabel(l2);
        lbl2.setFont(new Font("Segoe UI", Font.BOLD, 13));

        c.gridwidth = 1;
        c.gridy = r * 2;
        c.gridx = 0;
        p.add(lbl1, c);
        c.gridx = 1;
        p.add(lbl2, c);

        c.gridy = r * 2 + 1;
        c.gridx = 0;
        p.add(f1, c);
        c.gridx = 1;
        p.add(f2, c);
    }

    private void addFormFull(JPanel p, GridBagConstraints c, int r, String l, JComponent f) {
        JLabel lbl = new JLabel(l);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        c.gridwidth = 2;
        c.gridy = r * 2;
        c.gridx = 0;
        p.add(lbl, c);
        c.gridy = r * 2 + 1;
        p.add(f, c);
    }

    private void showError(Exception ex) {
        JOptionPane.showMessageDialog(this,
                ex.getMessage() == null ? ex.toString() : ex.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
    }

    private void notifyChangedAndClose(String message) {
        JOptionPane.showMessageDialog(this, message, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        if (onChanged != null) {
            onChanged.run();
        }
        dispose();
    }

    private String value(String text) {
        return text == null ? "" : text;
    }
}
