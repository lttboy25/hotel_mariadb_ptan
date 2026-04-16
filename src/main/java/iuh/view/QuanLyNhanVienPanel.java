package iuh.view;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * QuanLyNhanVienPanel - Giao diện Quản Lý Nhân Viên
 *
 * Bố cục:
 *   - Toolbar: ô tìm kiếm + nút Tìm kiếm + nút Thêm nhân viên
 *   - Bảng danh sách nhân viên (các cột quan trọng nhất)
 *   - Double-click vào dòng → mở NhanVienModal để xem/sửa chi tiết
 *
 * Các cột hiển thị trên bảng (chọn lọc từ database):
 *   Mã NV | Họ tên | Giới tính | Ngày sinh | SĐT | Email | Ngày bắt đầu | Trạng thái
 *   (CCCD, địa chỉ, tài khoản → chỉ hiện trong modal vì quá dài/nhạy cảm)
 */
public class QuanLyNhanVienPanel extends JPanel {

    // ── Màu sắc ──────────────────────────────────────────────────────────────
    static final Color BG_MAIN       = new Color(0xF4F6FB);
    static final Color BG_WHITE      = Color.WHITE;
    static final Color BLUE          = new Color(0x3B6FF0);
    static final Color BLUE_LIGHT    = new Color(0xEBF0FF);
    static final Color BLUE_HOVER    = new Color(0x2D5FD8);
    static final Color TEXT_DARK     = new Color(0x1A1A2E);
    static final Color TEXT_MID      = new Color(0x4A5268);
    static final Color TEXT_GRAY     = new Color(0xA0A8B8);
    static final Color BORDER_COL    = new Color(0xE4E9F2);
    static final Color HEADER_BG     = new Color(0xF8F9FD);
    static final Color ROW_ALT       = new Color(0xFAFBFD);
    static final Color ROW_SEL       = new Color(0xEBF0FF);
    static final Color GREEN         = new Color(0x27AE60);
    static final Color GREEN_BG      = new Color(0xE8F8EF);
    static final Color RED           = new Color(0xE04040);
    static final Color RED_BG        = new Color(0xFFF0F0);
    static final Color ORANGE        = new Color(0xF39C12);
    static final Color ORANGE_BG     = new Color(0xFFF8EC);

    // ── Font ─────────────────────────────────────────────────────────────────
    static final Font F_TITLE   = new Font("Segoe UI", Font.BOLD,  20);
    static final Font F_LABEL   = new Font("Segoe UI", Font.PLAIN, 13);
    static final Font F_BOLD13  = new Font("Segoe UI", Font.BOLD,  13);
    static final Font F_BOLD14  = new Font("Segoe UI", Font.BOLD,  14);
    static final Font F_TABLE   = new Font("Segoe UI", Font.PLAIN, 13);
    static final Font F_TABLE_H = new Font("Segoe UI", Font.BOLD,  12);
    static final Font F_SMALL   = new Font("Segoe UI", Font.PLAIN, 11);

    // ── Dữ liệu mẫu ──────────────────────────────────────────────────────────
    // Cột: Mã NV | Họ tên | Giới tính | Ngày sinh | SĐT | Email | Ngày bắt đầu | Trạng thái
    static final Object[][] SAMPLE_DATA = {
        {"NV001", "Nguyễn Văn An",    "Nam", "15/03/1990", "0901234567", "an.nv@victorya.com",    "01/06/2020", "Đang làm"},
        {"NV002", "Trần Thị Bích",    "Nữ",  "22/07/1995", "0912345678", "bich.tt@victorya.com",  "15/08/2021", "Đang làm"},
        {"NV003", "Lê Hoàng Cường",   "Nam", "08/11/1988", "0923456789", "cuong.lh@victorya.com", "10/01/2019", "Đang làm"},
        {"NV004", "Phạm Thị Dung",    "Nữ",  "30/04/1993", "0934567890", "dung.pt@victorya.com",  "20/03/2022", "Nghỉ phép"},
        {"NV005", "Hoàng Minh Đức",   "Nam", "14/09/1997", "0945678901", "duc.hm@victorya.com",   "05/11/2022", "Đang làm"},
        {"NV006", "Vũ Thị Lan",       "Nữ",  "25/12/1991", "0956789012", "lan.vt@victorya.com",   "12/07/2018", "Nghỉ việc"},
        {"NV007", "Đỗ Quang Huy",     "Nam", "03/06/1994", "0967890123", "huy.dq@victorya.com",   "08/09/2021", "Đang làm"},
        {"NV008", "Bùi Thị Mai",      "Nữ",  "19/02/1996", "0978901234", "mai.bt@victorya.com",   "14/04/2023", "Đang làm"},
    };

    static final String[] COLUMNS = {
        "Mã NV", "Họ và tên", "Giới tính", "Ngày sinh", "Số điện thoại", "Email", "Ngày bắt đầu", "Trạng thái"
    };

    // ── State ─────────────────────────────────────────────────────────────────
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField tfSearch;

    public QuanLyNhanVienPanel() {
        setLayout(new BorderLayout(0, 0));
        setBackground(BG_MAIN);
        setBorder(new EmptyBorder(28, 28, 28, 28));

        // Tiêu đề trang
        JLabel title = new JLabel("Quản lý nhân viên");
        title.setFont(F_TITLE);
        title.setForeground(TEXT_DARK);
        title.setBorder(new EmptyBorder(0, 0, 18, 0));
        add(title, BorderLayout.NORTH);

        // Nội dung chính
        JPanel content = new ThanhToanPanel.RoundedPanel(14, BG_WHITE);
        content.setLayout(new BorderLayout(0, 0));

        content.add(buildToolbar(), BorderLayout.NORTH);
        content.add(buildTable(),   BorderLayout.CENTER);

        add(content, BorderLayout.CENTER);
    }

    // =========================================================================
    //  TOOLBAR – Tìm kiếm + Nút Thêm
    // =========================================================================
    private JPanel buildToolbar() {
        JPanel bar = new JPanel(new BorderLayout(12, 0));
        bar.setOpaque(false);
        bar.setBorder(new EmptyBorder(16, 20, 12, 20));

        // ── Trái: ô tìm kiếm + nút Tìm ──
        JPanel leftGroup = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        leftGroup.setOpaque(false);

        // Ô tìm kiếm
        tfSearch = new JTextField(22);
        tfSearch.setFont(F_LABEL);
        tfSearch.setForeground(TEXT_DARK);
        tfSearch.setPreferredSize(new Dimension(240, 38));
        tfSearch.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COL, 1, true),
            new EmptyBorder(0, 12, 0, 12)
        ));

        // Placeholder
        tfSearch.setText("Tìm kiếm nhân viên...");
        tfSearch.setForeground(TEXT_GRAY);
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

        // Nút Tìm kiếm
        JButton btnSearch = createButton("Tìm kiếm", BLUE, BLUE_HOVER, Color.WHITE, 100, 38);

        leftGroup.add(tfSearch);
        leftGroup.add(btnSearch);

        // ── Phải: nút Thêm nhân viên ──
        JButton btnAdd = createButton("+ Thêm nhân viên", new Color(0x27AE60), new Color(0x1E8449), Color.WHITE, 160, 38);
        btnAdd.addActionListener(e -> openModal(null)); // null = chế độ thêm mới

        JPanel rightGroup = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightGroup.setOpaque(false);
        rightGroup.add(btnAdd);

        bar.add(leftGroup,  BorderLayout.WEST);
        bar.add(rightGroup, BorderLayout.EAST);

        // Đường kẻ phân cách
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(bar, BorderLayout.CENTER);
        wrapper.add(new JSeparator() {{ setForeground(BORDER_COL); }}, BorderLayout.SOUTH);

        return wrapper;
    }

    // =========================================================================
    //  BẢNG NHÂN VIÊN
    // =========================================================================
    private JScrollPane buildTable() {
        tableModel = new DefaultTableModel(SAMPLE_DATA, COLUMNS) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(tableModel);
        table.setFont(F_TABLE);
        table.setRowHeight(52);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(BORDER_COL);
        table.setBackground(BG_WHITE);
        table.setSelectionBackground(ROW_SEL);
        table.setSelectionForeground(TEXT_DARK);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFocusable(false);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // ── Header ──
        JTableHeader header = table.getTableHeader();
        header.setFont(F_TABLE_H);
        header.setForeground(TEXT_MID);
        header.setBackground(HEADER_BG);
        header.setBorder(new MatteBorder(0, 0, 1, 0, BORDER_COL));
        header.setPreferredSize(new Dimension(0, 42));
        header.setReorderingAllowed(false);
        ((DefaultTableCellRenderer) header.getDefaultRenderer())
            .setHorizontalAlignment(JLabel.LEFT);

        // ── Renderer chung ──
        DefaultTableCellRenderer baseRenderer = new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean focus, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, focus, row, col);
                setBackground(sel ? ROW_SEL : (row % 2 == 0 ? BG_WHITE : ROW_ALT));
                setForeground(col == 0 ? BLUE : TEXT_DARK);
                setFont(col == 0 ? F_BOLD13 : F_TABLE);
                setBorder(new EmptyBorder(0, 16, 0, 8));
                setHorizontalAlignment(JLabel.LEFT);
                return this;
            }
        };

        // ── Renderer trạng thái (badge màu) ──
        DefaultTableCellRenderer statusRenderer = new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean focus, int row, int col) {

                String status = val == null ? "" : val.toString();

                // Dùng JPanel để vẽ badge bo góc
                JPanel badge = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)) {
                    @Override protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                    }
                };
                badge.setBackground(sel ? ROW_SEL : (row % 2 == 0 ? BG_WHITE : ROW_ALT));
                badge.setBorder(new EmptyBorder(11, 16, 11, 8));

                Color bgCol, fgCol;
                switch (status) {
                    case "Đang làm"  -> { bgCol = GREEN_BG;  fgCol = GREEN;  }
                    case "Nghỉ phép" -> { bgCol = ORANGE_BG; fgCol = ORANGE; }
                    default          -> { bgCol = RED_BG;    fgCol = RED;    }
                }

                JLabel pill = new JLabel(status) {
                    @Override protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(bgCol);
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                        g2.dispose();
                        super.paintComponent(g);
                    }
                };
                pill.setFont(F_SMALL);
                pill.setForeground(fgCol);
                pill.setOpaque(false);
                pill.setBorder(new EmptyBorder(3, 10, 3, 10));

                badge.add(pill);
                return badge;
            }
        };

        // ── Áp dụng renderer ──
        for (int i = 0; i < COLUMNS.length - 1; i++)
            table.getColumnModel().getColumn(i).setCellRenderer(baseRenderer);
        table.getColumnModel().getColumn(COLUMNS.length - 1).setCellRenderer(statusRenderer);

        // ── Độ rộng cột ──
        int[] widths = {70, 160, 80, 100, 120, 200, 110, 110};
        for (int i = 0; i < widths.length; i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        // ── Double-click → mở modal ──
        table.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row >= 0) openModal(row);
                }
            }
            // Hover highlight nhẹ
            @Override public void mouseEntered(MouseEvent e) {}
        });

        // Tooltip gợi ý double-click
        table.setToolTipText("Double-click vào dòng để xem / chỉnh sửa");

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setBackground(BG_WHITE);
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        return sp;
    }

    // =========================================================================
    //  MỞ MODAL
    //  row = null  → chế độ Thêm mới
    //  row = index → chế độ Xem/Sửa
    // =========================================================================
    private void openModal(Integer row) {
        // Lấy dữ liệu dòng nếu có
        Object[] data = null;
        if (row != null && row >= 0) {
            data = new Object[tableModel.getColumnCount()];
            for (int c = 0; c < tableModel.getColumnCount(); c++)
                data[c] = tableModel.getValueAt(row, c);
        }

        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        NhanVienModal modal = new NhanVienModal(topFrame, data, row == null);
        modal.setVisible(true);
    }

    // =========================================================================
    //  HELPER – tạo nút bo góc
    // =========================================================================
    static JButton createButton(String text, Color bg, Color hover, Color fg, int w, int h) {
        JButton btn = new JButton(text) {
            private boolean hovered = false;
            {
                addMouseListener(new MouseAdapter() {
                    @Override public void mouseEntered(MouseEvent e) { hovered = true;  repaint(); }
                    @Override public void mouseExited(MouseEvent e)  { hovered = false; repaint(); }
                });
            }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? hover.darker() : hovered ? hover : bg);
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
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(w, h));
        return btn;
    }
}


// =============================================================================
//  MODAL CHI TIẾT / THÊM MỚI NHÂN VIÊN
// =============================================================================
class NhanVienModal extends JDialog {

    // Màu (kế thừa từ panel)
    private static final Color BG_WHITE   = QuanLyNhanVienPanel.BG_WHITE;
    private static final Color BG_MAIN    = QuanLyNhanVienPanel.BG_MAIN;
    private static final Color BLUE       = QuanLyNhanVienPanel.BLUE;
    private static final Color BLUE_LIGHT = QuanLyNhanVienPanel.BLUE_LIGHT;
    private static final Color TEXT_DARK  = QuanLyNhanVienPanel.TEXT_DARK;
    private static final Color TEXT_MID   = QuanLyNhanVienPanel.TEXT_MID;
    private static final Color TEXT_GRAY  = QuanLyNhanVienPanel.TEXT_GRAY;
    private static final Color BORDER_COL = QuanLyNhanVienPanel.BORDER_COL;
    private static final Color GREEN      = QuanLyNhanVienPanel.GREEN;
    private static final Color RED        = QuanLyNhanVienPanel.RED;
    private static final Color RED_BG     = QuanLyNhanVienPanel.RED_BG;

    private static final Font F_TITLE  = new Font("Segoe UI", Font.BOLD,  16);
    private static final Font F_LABEL  = new Font("Segoe UI", Font.BOLD,  12);
    private static final Font F_INPUT  = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font F_BOLD13 = QuanLyNhanVienPanel.F_BOLD13;

    // Các field nhập liệu
    private JTextField tfMaNV, tfCCCD, tfHoTen, tfEmail, tfSDT, tfNgaySinh, tfNgayBatDau, tfDiaChi;
    private JComboBox<String> cbGioiTinh, cbTrangThai;

    /**
     * @param owner    JFrame cha
     * @param data     Dữ liệu dòng (null nếu thêm mới)
     * @param isNew    true = chế độ thêm mới, false = chỉnh sửa
     */
    NhanVienModal(JFrame owner, Object[] data, boolean isNew) {
        super(owner, isNew ? "Thêm nhân viên mới" : "Chi tiết nhân viên", true);
        setSize(620, 640);
        setLocationRelativeTo(owner);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(BG_WHITE);

        root.add(buildHeader(isNew), BorderLayout.NORTH);
        root.add(buildForm(data, isNew), BorderLayout.CENTER);
        root.add(buildFooter(isNew), BorderLayout.SOUTH);

        setContentPane(root);
    }

    // ── Header modal ─────────────────────────────────────────────────────────
    private JPanel buildHeader(boolean isNew) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_WHITE);
        header.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, BORDER_COL),
            new EmptyBorder(18, 24, 16, 16)
        ));

        // Avatar placeholder + tiêu đề
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        left.setOpaque(false);

        // Avatar tròn
        JLabel avatar = new JLabel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BLUE_LIGHT);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.setColor(BLUE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 18));
                FontMetrics fm = g2.getFontMetrics();
                String initial = isNew ? "+" : "NV";
                g2.drawString(initial,
                    getWidth() / 2 - fm.stringWidth(initial) / 2,
                    getHeight() / 2 + fm.getAscent() / 2 - 2);
                g2.dispose();
            }
        };
        avatar.setPreferredSize(new Dimension(44, 44));

        JPanel titleBlock = new JPanel();
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));
        titleBlock.setOpaque(false);

        JLabel lblTitle = new JLabel(isNew ? "Thêm nhân viên mới" : "Thông tin nhân viên");
        lblTitle.setFont(F_TITLE);
        lblTitle.setForeground(TEXT_DARK);

        JLabel lblSub = new JLabel(isNew ? "Điền đầy đủ thông tin bên dưới" : "Xem và chỉnh sửa thông tin");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(TEXT_GRAY);

        titleBlock.add(lblTitle);
        titleBlock.add(Box.createVerticalStrut(2));
        titleBlock.add(lblSub);

        left.add(avatar);
        left.add(titleBlock);

        // Nút đóng X
        JButton btnClose = new JButton("✕") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? new Color(0xFFE0E0)
                          : getModel().isRollover() ? new Color(0xFFF0F0)
                          : BG_WHITE);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnClose.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnClose.setForeground(TEXT_MID);
        btnClose.setContentAreaFilled(false);
        btnClose.setBorderPainted(false);
        btnClose.setFocusPainted(false);
        btnClose.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnClose.setPreferredSize(new Dimension(30, 30));
        btnClose.addActionListener(e -> dispose());

        header.add(left, BorderLayout.CENTER);
        header.add(btnClose, BorderLayout.EAST);
        return header;
    }

    // ── Form nhập liệu ───────────────────────────────────────────────────────
    private JScrollPane buildForm(Object[] data, boolean isNew) {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(BG_WHITE);
        form.setBorder(new EmptyBorder(20, 24, 20, 24));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.insets  = new Insets(0, 0, 14, 12);
        gbc.weighty = 0;

        // Hàng 0: Mã NV + CCCD
        tfMaNV = createField(isNew ? "" : safeGet(data, 0));
        tfMaNV.setEditable(false); // Mã NV do hệ thống tự sinh
        tfMaNV.setBackground(new Color(0xF8F9FD));
        addFormRow(form, gbc, 0, "Mã nhân viên", tfMaNV, "CCCD / CMND",
                   tfCCCD = createField(isNew ? "" : "079xxxxxxxxx"));

        // Hàng 1: Họ tên (full row)
        tfHoTen = createField(isNew ? "" : safeGet(data, 1));
        addFormRowFull(form, gbc, 1, "Họ và tên", tfHoTen);

        // Hàng 2: Giới tính + Ngày sinh
        cbGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"});
        styleCombo(cbGioiTinh);
        if (!isNew) cbGioiTinh.setSelectedItem(safeGet(data, 2));
        tfNgaySinh = createField(isNew ? "dd/MM/yyyy" : safeGet(data, 3));
        addFormRow(form, gbc, 2, "Giới tính", cbGioiTinh, "Ngày sinh", tfNgaySinh);

        // Hàng 3: SĐT + Email
        tfSDT   = createField(isNew ? "" : safeGet(data, 4));
        tfEmail = createField(isNew ? "" : safeGet(data, 5));
        addFormRow(form, gbc, 3, "Số điện thoại", tfSDT, "Email", tfEmail);

        // Hàng 4: Ngày bắt đầu + Trạng thái
        tfNgayBatDau = createField(isNew ? "dd/MM/yyyy" : safeGet(data, 6));
        cbTrangThai  = new JComboBox<>(new String[]{"Đang làm", "Nghỉ phép", "Nghỉ việc"});
        styleCombo(cbTrangThai);
        if (!isNew) cbTrangThai.setSelectedItem(safeGet(data, 7));
        addFormRow(form, gbc, 4, "Ngày bắt đầu", tfNgayBatDau, "Trạng thái", cbTrangThai);

        // Hàng 5: Địa chỉ (full row)
        tfDiaChi = createField(isNew ? "" : "123 Nguyễn Huệ, Q.1, TP.HCM");
        addFormRowFull(form, gbc, 5, "Địa chỉ", tfDiaChi);

        // Spacer
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2; gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        form.add(Box.createVerticalGlue(), gbc);

        JScrollPane sp = new JScrollPane(form);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sp.getViewport().setBackground(BG_WHITE);
        return sp;
    }

    /** Thêm 1 hàng 2 cột vào form */
    private void addFormRow(JPanel form, GridBagConstraints gbc,
                             int row, String label1, JComponent field1,
                             String label2, JComponent field2) {
        gbc.gridy    = row * 2;
        gbc.weightx  = 0.5;
        gbc.gridwidth = 1;
        gbc.fill     = GridBagConstraints.HORIZONTAL;
        gbc.insets   = new Insets(0, 0, 4, 12);

        // Label 1
        gbc.gridx = 0;
        form.add(makeLabel(label1), gbc);
        // Label 2
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, 4, 0);
        form.add(makeLabel(label2), gbc);

        gbc.gridy = row * 2 + 1;
        gbc.insets = new Insets(0, 0, 14, 12);

        // Field 1
        gbc.gridx = 0;
        form.add(field1, gbc);
        // Field 2
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, 14, 0);
        form.add(field2, gbc);
    }

    /** Thêm 1 hàng full-width vào form */
    private void addFormRowFull(JPanel form, GridBagConstraints gbc,
                                  int row, String label, JComponent field) {
        gbc.gridy    = row * 2;
        gbc.gridx    = 0;
        gbc.gridwidth = 2;
        gbc.weightx  = 1.0;
        gbc.insets   = new Insets(0, 0, 4, 0);
        form.add(makeLabel(label), gbc);

        gbc.gridy    = row * 2 + 1;
        gbc.insets   = new Insets(0, 0, 14, 0);
        form.add(field, gbc);
    }

    /** Label nhỏ bên trên field */
    private JLabel makeLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(F_LABEL);
        lbl.setForeground(TEXT_MID);
        return lbl;
    }

    /** Tạo JTextField đồng nhất */
    private JTextField createField(String text) {
        JTextField tf = new JTextField(text);
        tf.setFont(F_INPUT);
        tf.setForeground(TEXT_DARK);
        tf.setPreferredSize(new Dimension(0, 38));
        tf.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COL, 1, true),
            new EmptyBorder(0, 12, 0, 12)
        ));
        return tf;
    }

    /** Style cho JComboBox */
    private void styleCombo(JComboBox<String> cb) {
        cb.setFont(F_INPUT);
        cb.setForeground(TEXT_DARK);
        cb.setBackground(BG_WHITE);
        cb.setPreferredSize(new Dimension(0, 38));
        cb.setBorder(new LineBorder(BORDER_COL, 1, true));
        cb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cb.setFocusable(false);
    }

    // ── Footer: nút hành động ────────────────────────────────────────────────
    private JPanel buildFooter(boolean isNew) {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        footer.setBackground(BG_WHITE);
        footer.setBorder(new MatteBorder(1, 0, 0, 0, BORDER_COL));

        // Nút Hủy
        JButton btnCancel = QuanLyNhanVienPanel.createButton(
            "Hủy", new Color(0xEEF0F5), new Color(0xDDE0E8), TEXT_MID, 90, 38);
        btnCancel.addActionListener(e -> dispose());

        if (isNew) {
            // Nút Lưu
            JButton btnSave = QuanLyNhanVienPanel.createButton(
                "Lưu", BLUE, new Color(0x2D5FD8), Color.WHITE, 110, 38);
            footer.add(btnCancel);
            footer.add(btnSave);
        } else {
            // Nút Xóa + Cập nhật
            JButton btnDelete = QuanLyNhanVienPanel.createButton(
                "Xóa", RED, RED.darker(), Color.WHITE, 90, 38);
            btnDelete.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc muốn xóa nhân viên này?",
                    "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) dispose();
            });

            JButton btnUpdate = QuanLyNhanVienPanel.createButton(
                "Cập nhật", BLUE, new Color(0x2D5FD8), Color.WHITE, 110, 38);

            footer.add(btnDelete);
            footer.add(btnCancel);
            footer.add(btnUpdate);
        }

        return footer;
    }

    // ── Util ─────────────────────────────────────────────────────────────────
    private String safeGet(Object[] arr, int idx) {
        if (arr == null || idx >= arr.length || arr[idx] == null) return "";
        return arr[idx].toString();
    }
}
